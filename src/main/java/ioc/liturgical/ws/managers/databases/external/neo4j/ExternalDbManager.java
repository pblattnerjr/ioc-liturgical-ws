package ioc.liturgical.ws.managers.databases.external.neo4j;

import java.text.Normalizer;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import ioc.liturgical.ws.managers.interfaces.HighLevelDataStoreInterface;
import ioc.liturgical.ws.app.ServiceProvider;
import ioc.liturgical.ws.constants.HTTP_RESPONSE_CODES;
import ioc.liturgical.ws.constants.VERBS;
import ioc.liturgical.ws.managers.databases.external.neo4j.constants.MATCHERS;
import ioc.liturgical.ws.managers.databases.external.neo4j.utils.DomainTopicMapBuilder;
import ioc.liturgical.ws.managers.databases.external.neo4j.utils.Neo4jConnectionManager;
import ioc.liturgical.ws.managers.databases.internal.InternalDbManager;
import ioc.liturgical.ws.managers.exceptions.DbException;
import ioc.liturgical.ws.models.RequestStatus;
import ioc.liturgical.ws.models.ResultJsonObjectArray;
import ioc.liturgical.ws.models.db.docs.Reference;
import ioc.liturgical.ws.models.db.forms.ReferenceCreateForm;
import net.ages.alwb.utils.core.datastores.json.exceptions.BadIdException;
import net.ages.alwb.utils.core.datastores.json.exceptions.MissingSchemaIdException;
import net.ages.alwb.utils.core.datastores.json.models.LTKVJsonObject;
import net.ages.alwb.utils.core.error.handling.ErrorUtils;
import net.ages.alwb.utils.core.id.managers.IdManager;;


/**
 * @author Michael Colburn
 * @since 2016
 */
public class ExternalDbManager implements HighLevelDataStoreInterface{
	
	/**
	 * TODO: need to make sure that the dropdownItems are rebuilt when a put or post
	 * invalidates them.
	 */
	private static final Logger logger = LoggerFactory.getLogger(ExternalDbManager.class);
	private boolean logAllQueries = false;
	private boolean logQueriesWithNoMatches = false;
	private boolean   prettyPrint = true;
	private Gson gson = new Gson();

	  JsonParser parser = new JsonParser();
	  Pattern punctPattern = Pattern.compile("[˙·,.;!?(){}\\[\\]<>%]"); // punctuation 
	  DomainTopicMapBuilder domainTopicMapbuilder = new DomainTopicMapBuilder();
	  JsonObject dropdownItems = new JsonObject();
	  public static Neo4jConnectionManager neo4jManager = null;
	  InternalDbManager internalManager = null;
	  
	  public ExternalDbManager(
			  String neo4jDomain
			  , boolean logQueries
			  , boolean logQueriesWithNoMatches
			  , InternalDbManager internalManager
			  ) {
		  this.internalManager = internalManager; 
		  neo4jManager = new Neo4jConnectionManager(
				  neo4jDomain
				  , ServiceProvider.ws_usr
				  , ServiceProvider.ws_pwd
				  );
		  this.logAllQueries = logQueries;
		  this.logQueriesWithNoMatches = logQueriesWithNoMatches;
		  buildDomainTopicMap();
	  }
	  
	  public ExternalDbManager(
			  String neo4jDomain
			  , boolean logQueries
			  , boolean logQueriesWithNoMatches
			  , String adminUserId
			  , String adminUserPassword
			  , boolean buildDomainMap
			  , InternalDbManager internalManager
			  ) {
		  this.internalManager = internalManager; 
		  neo4jManager = new Neo4jConnectionManager(
				  neo4jDomain
				  , adminUserId
				  , adminUserPassword
				  );
		  this.logAllQueries = logQueries;
		  this.logQueriesWithNoMatches = logQueriesWithNoMatches;
		  if (buildDomainMap) {
			  buildDomainTopicMap();
		  }
	  }

	  public void buildDomainTopicMap() {
		 dropdownItems = domainTopicMapbuilder.getDropdownItems();
	  }
	  
		public RequestStatus addReference(
				String requestor
				, String fromLibrary
				, String fromTopic
			    , String fromKey
				, String toLibrary
				, String toTopic
				, String toKey
				, String relationshipJson
				) {
			RequestStatus result = new RequestStatus();
			ReferenceCreateForm form = new ReferenceCreateForm();
			form = (ReferenceCreateForm) form.fromJsonString(relationshipJson);
			if (internalManager.authorized(requestor, VERBS.POST, form.getDomain())) {
				String validation = form.validate(relationshipJson);
				if (validation.length() == 0) {
					try {
						IdManager fromIdManager = new IdManager(fromLibrary, fromTopic, fromKey);
						IdManager toIdManager = new IdManager(toLibrary, toTopic, toKey);
						Reference ref = new Reference(form); // set ref values from the form values
						ref.setCreatedBy(requestor);
						ref.setModifiedBy(requestor);
						ref.setCreatedWhen(getTimestamp());
						ref.setModifiedWhen(ref.getCreatedWhen());
						result = addLTKVJsonObjectAsRelationship(
								fromIdManager.getId()
								, toIdManager.getId()
								, form.getDomain()
								, form.getIdReferredByText()
								, form.getIdReferredToText()
								, ref.schemaIdAsString()
								, ref.toJsonObject()
								);
					} catch (Exception e) {
						result.setCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
						result.setMessage(HTTP_RESPONSE_CODES.BAD_REQUEST.message);
					}
				} else {
					result.setCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
					JsonObject message = stringToJson(validation);
					if (message == null) {
						result.setMessage(validation);
					} else {
						result.setMessage(message.get("message").getAsString());
					}
				}
			} else {
				result.setCode(HTTP_RESPONSE_CODES.UNAUTHORIZED.code);
				result.setMessage(HTTP_RESPONSE_CODES.UNAUTHORIZED.message);
			}
			return result;
		}


	  private ResultJsonObjectArray setValueSchemas(ResultJsonObjectArray result) {
			List<JsonObject> jsonList = result.getResult();
			result.setValueSchemas(internalManager.getSchemas(jsonList, null));
			result.setResult(jsonList);
		  return result;
	  }

	  public JsonObject getForQuery(String query) {
			ResultJsonObjectArray result = neo4jManager.getResultObjectForQuery(query, true);
			if (logAllQueries
					|| 
					(logQueriesWithNoMatches && result.getResultCount() == 0) 
					) {
				logger.info(query);
				logger.info("Result count: " + result.getResultCount());
			}
			result = setValueSchemas(result);
			return result.toJsonObject();
	}
	  
		public JsonObject search(
				String type
				, String domain
				, String book
				, String chapter
				, String query
				, String property
				, String matcher
				) {
			JsonObject result = null;
			if (type == null) {
				type = "id";
			}
			/**
			 * This is a workaround for ambiguity between the codes used with the Hieratikon sections
			 * and those of the Horologion.  Fr. Seraphim uses "s01" etc for both, but with different meaning.
			 * So the dropdown uses "his01" for Hieratikon sections.  But the database uses "s01". 
			 * So we will intercept this and change his into s for the db search.
			 */
			if (chapter.startsWith("his")) {
				chapter = chapter.replaceFirst("hi", "");
			}
			result = getForQuery(getCypherQuery(type, domain, book, chapter, query, property, matcher));
			return result;
		}

		private String removePunctuation(String s) {
			try {
				return punctPattern.matcher(s).replaceAll("");
			} catch (Exception e) {
				ErrorUtils.report(logger, e);
				return s;
			}
		}
		
	   private String normalized(String s) {
		   return Normalizer.normalize(s, Normalizer.Form.NFD)
					.replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();
	   }
	   
		private String getCypherQuery(
				String type
				, String domain
				, String book
				, String chapter
				, String query
				, String property
				, String matcher
				) {
			String theQuery = query;
			if (matcher.startsWith("rx")) {
				// ignore
			} else {
				// remove accents and punctuation if requested
				if (property.startsWith("nnp") || property.startsWith("nwp")) {
					theQuery = normalized(theQuery);
					if (property.startsWith("nnp")) {
						theQuery = removePunctuation(normalized(theQuery));
					}
				}
			}
			CypherQueryBuilder builder = new CypherQueryBuilder()
					.MATCH()
					.LABEL(type)
					.LABEL(domain)
					.LABEL(book)
					.LABEL(chapter)
					.WHERE(property)
					;
			
			MATCHERS matcherEnum = MATCHERS.forLabel(matcher);
			
			switch (matcherEnum) {
			case STARTS_WITH: {
				builder.STARTS_WITH(theQuery);
				break;
			}
			case ENDS_WITH: {
				builder.ENDS_WITH(theQuery);
				break;
			}
			case REG_EX: {
				builder.MATCHES_PATTERN(theQuery);
				break;
			} 
			default: {
				builder.CONTAINS(theQuery);
				break;
			}
			}
			builder.RETURN("id, value");
			builder.ORDER_BY("doc.seq"); // TODO: in future this could be a parameter in REST API

			CypherQuery q = builder.build();
			
			return q.toString();
		}
		
		public JsonObject getDropdownItems() {
			return dropdownItems;
		}

		@Override
		public RequestStatus addLTKVJsonObject(
				String library
				, String topic
				, String key
				, String schemaId
				, JsonObject json
				) throws 
					BadIdException
					, DbException
					, MissingSchemaIdException {
			RequestStatus result = new RequestStatus();
			if (internalManager.existsSchema(schemaId)) {
				String id = new IdManager(library,topic,key).getId();
				if (existsUnique(id)) {
					result.setCode(HTTP_RESPONSE_CODES.CONFLICT.code);
					result.setMessage(HTTP_RESPONSE_CODES.CONFLICT.message + ": " + id);
				} else {
					LTKVJsonObject record = 
							new LTKVJsonObject(
								library
								, topic
								, key
								, schemaId
								, json
								);
					    neo4jManager.insert(record);		
				    	result.setCode(HTTP_RESPONSE_CODES.CREATED.code);
				    	result.setMessage(HTTP_RESPONSE_CODES.CREATED.message + ": " + id);
				}
			} else {
				throw new MissingSchemaIdException(schemaId);
			}
			return result;
		}

		public RequestStatus addLTKVJsonObjectAsRelationship(
				String fromId
				, String toId
				, String library
				, String topic
				, String key
				, String schemaId
				, JsonObject json
				) throws 
					BadIdException
					, DbException
					, MissingSchemaIdException {
			RequestStatus result = new RequestStatus();
			if (internalManager.existsSchema(schemaId)) {
				String id = new IdManager(library,topic,key).getId();
				if (this.existsUniqueRelationship(id)) {
					result.setCode(HTTP_RESPONSE_CODES.CONFLICT.code);
					result.setMessage(HTTP_RESPONSE_CODES.CONFLICT.message + ": " + id);
				} else {
					LTKVJsonObject record = 
							new LTKVJsonObject(
								library
								, topic
								, key
								, schemaId
								, json
								);
					    result = neo4jManager.createRelationship(fromId, record, toId);		
				}
			} else {
				throw new MissingSchemaIdException(schemaId);
			}
			return result;
		}
		
		public RequestStatus getRelationshipById(String id) {
			String query = "match (f)-[r]->(t) where r.id = \"" + id + "\" return f.id, f.value, r.value, t.id, t.value";
			return new RequestStatus();
		}

		public RequestStatus getRelationshipByType(String type) {
			String query = "match (f)-[r:" + type + "]->(t) return f.id, f.value, r.value, t.id, t.value";
			return new RequestStatus();
		}

		/**
		 * Converts a comma delimited string of labels into
		 * a Cyper query statement.
		 * @param labels e.g. a, b
		 * @param operator and or or
		 * @return e.g. "r.labels contains "a" or r.labels contains "b"
		 */
		public String labelsAsQuery(String labels, String operator) {
			StringBuffer result = new StringBuffer();
			String [] parts = labels.split(",");
			result.append("\"" + parts[0].trim() + "\"");
			if (parts.length > 1) {
				for (int i=1; i < parts.length; i++) {
					result.append(" " + operator + " r.labels contains \"" + parts[i].trim() + "\"");
				}
			}
			return result.toString();
		}
		
		public RequestStatus getRelationshipByLabels(String labels) {
			String query = "match (f)-[r]->(t) where r.labels contains " + labelsAsQuery(labels, "and") + " return f.id, f.value, r.value, t.id, t.value";
			return new RequestStatus();
		}

		public RequestStatus getRelationshipByFromId(String id) {
			return new RequestStatus();
		}

		public RequestStatus getRelationshipByToId(String id) {
			return new RequestStatus();
		}

		public RequestStatus getRelationshipByNodeIds(String fromId, String toId) {
			return new RequestStatus();
		}

		@Override
		public RequestStatus updateLTKVJsonObject(
				String library
				, String topic
				, String key
				, String schemaId
				, JsonObject json
				) throws BadIdException, DbException, MissingSchemaIdException {
			RequestStatus result = new RequestStatus();
			if (internalManager.existsSchema(schemaId)) {
				String id = new IdManager(library,topic,key).getId();
				if (existsUnique(id)) {
					LTKVJsonObject record;
					record = new LTKVJsonObject(
							library
							, topic
							, key
							, schemaId
							, json
							);
					neo4jManager.updateWhereEqual(record);		
				} else {
					result.setCode(HTTP_RESPONSE_CODES.NOT_FOUND.code);
					result.setMessage(HTTP_RESPONSE_CODES.NOT_FOUND.message + ": " + id);
				}
			} else {
				throw new MissingSchemaIdException(schemaId);
			}
			return result;
		}

		public RequestStatus updateLTKVJsonObjectAsReference(
				String library
				, String topic
				, String key
				, String schemaId
				, JsonObject json
				) throws BadIdException, DbException, MissingSchemaIdException {
			RequestStatus result = new RequestStatus();
			if (internalManager.existsSchema(schemaId)) {
				String id = new IdManager(library,topic,key).getId();
				if (existsUniqueRelationship(id)) {
					LTKVJsonObject record;
					record = new LTKVJsonObject(
							library
							, topic
							, key
							, schemaId
							, json
							);
					neo4jManager.updateWhereReferenceEqual(record);		
				} else {
					result.setCode(HTTP_RESPONSE_CODES.NOT_FOUND.code);
					result.setMessage(HTTP_RESPONSE_CODES.NOT_FOUND.message + ": " + id);
				}
			} else {
				throw new MissingSchemaIdException(schemaId);
			}
			return result;
		}

		public RequestStatus updateReference(
				String requestor
				, String id
				, String json
				) {
			RequestStatus result = new RequestStatus();
			Reference obj = new Reference();
			String validation = obj.validate(json);
			if (validation.length() == 0) {
				try {
					obj = (Reference) obj.fromJsonString(json);
					obj.setModifiedBy(requestor);
					obj.setModifiedWhen(getTimestamp());
					result = updateReference(obj);
				} catch (Exception e) {
					result.setCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
					result.setMessage(HTTP_RESPONSE_CODES.BAD_REQUEST.message);
				}
			} else {
				result.setCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
				result.setMessage(validation);
			}
			return result;
		}
		
		private RequestStatus updateReference(Reference obj
				) {
			RequestStatus result = new RequestStatus();
			try {
		    	result = updateLTKVJsonObjectAsReference(
		    			obj.getDomain()
		    			, obj.getIdReferredByText()
		    			, obj.getIdReferredToText()
		    			, obj.schemaIdAsString()
		    			, obj.toJsonObject()
		    			);
			} catch (MissingSchemaIdException e) {
				ErrorUtils.report(logger, e);
				result.setCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
				result.setMessage(HTTP_RESPONSE_CODES.BAD_REQUEST.message);
			} catch (Exception e) {
				result.setCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
				result.setMessage(e.getMessage());
			}
			return result;

		}

		@Override
		public boolean existsUnique(String id) {
			try {
				JsonObject json = this.getForId(id);
				return json.get("valueCount").getAsInt() == 1;
			} catch (Exception e) {
				return false;
			}
		}
		
		public boolean existsUniqueRelationship(String id) {
			try {
				JsonObject json = this.getForIdOfRelationship(id);
				return json.get("valueCount").getAsInt() == 1;
			} catch (Exception e) {
				return false;
			}
		}

		private String getTimestamp() {
			return Instant.now().toString();
		}

		private JsonObject stringToJson(String s) {
			try {
				return new JsonParser().parse(s).getAsJsonObject();
			} catch (Exception e) {
				ErrorUtils.report(logger, e);
			}
			return null;
		}

		@Override
		public JsonObject getForId(String id) {
			ResultJsonObjectArray result  = new ResultJsonObjectArray(true);
			try {
				CypherQueryBuilder builder = new CypherQueryBuilder(false)
						.MATCH()
						.WHERE("id")
						.EQUALS(id)
						.RETURN("domain,topic,id,value,_valueSchemaId,key,labels")
						;
				CypherQuery q = builder.build();
				result  = neo4jManager.getForQuery(q.toString());
				result.setQuery(q.toString());
				result.setValueSchemas(internalManager.getSchemas(result.getResult(), null));
			} catch (Exception e) {
				result.setStatusCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
				result.setStatusMessage(e.getMessage());
			}
			return result.toJsonObject();
		}

		public JsonObject getForIdOfRelationship(String id) {
			ResultJsonObjectArray result  = new ResultJsonObjectArray(true);
			try {
				String q = "match ()-[r]->() where r.id =\"" + id + "\" return r.value";
				result  = neo4jManager.getForQuery(q.toString());
				result.setQuery(q);
				result.setValueSchemas(internalManager.getSchemas(result.getResult(), null));
			} catch (Exception e) {
				result.setStatusCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
				result.setStatusMessage(e.getMessage());
			}
			return result.toJsonObject();
		}

		public ResultJsonObjectArray getReferenceObjectByRefId(String id) {
			ResultJsonObjectArray result  = new ResultJsonObjectArray(true);
			try {
				String q = "match (f)-[r]->(t) where r.id = \"" + id + "\" return r.value";
				result  = neo4jManager.getForQuery(q);
				List<JsonObject> refs = new ArrayList<JsonObject>();
				List<JsonObject> objects = result.getValues();
				for (JsonObject object : objects) {
					try {
						
						Reference ref = (Reference) gson.fromJson(
								object.get("r.value").getAsString()
								, Reference.class
						);
						refs.add(ref.toJsonObject());
					} catch (Exception e) {
						ErrorUtils.report(logger, e);
					}
				}
				result.setResult(refs);
				result.setQuery(q.toString());
				result.setValueSchemas(internalManager.getSchemas(result.getResult(), null));
			} catch (Exception e) {
				result.setStatusCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
				result.setStatusMessage(e.getMessage());
			}
			return result;
		}

		/**
		 * Gets by id the reference, and returns the id and value of the from and to sides
		 * and the reference properties, with the reference labels also split out.
		 * @param id
		 * @return
		 */
		public ResultJsonObjectArray getReferenceObjectAndNodesByRefId(String id) {
			ResultJsonObjectArray result  = new ResultJsonObjectArray(true);
			try {
				String q = "match (f)-[r]->(t) where r.id = \"" + id + "\" return f.id, f.value, r, t.id, t.value";
				result  = neo4jManager.getForQuery(q);
				result.setQuery(q.toString());
				result.setValueSchemas(internalManager.getSchemas(result.getResult(), null));
			} catch (Exception e) {
				result.setStatusCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
				result.setStatusMessage(e.getMessage());
			}
			return result;
		}

		/**
		 * Get all references of the specified type.
		 * Returns the id and value for the from and to nodes.
		 * Returns all properties of the relationship and r.labels.
		 * 
		 * This is useful for listing references in a table, and when selected, you
		 * have the details of the reference immediately available without calling
		 * the REST api again.
		 * 
		 * @param type
		 * @return
		 */
		public ResultJsonObjectArray getReferenceObjectsAndNodes(String type) {
			ResultJsonObjectArray result  = new ResultJsonObjectArray(true);
			try {
				String q = "match (f)-[r:" + type + "]->(t)  return f.id, f.value, r.value, t.id, t.value";
				result  = neo4jManager.getForQuery(q);
				result.setQuery(q.toString());
				result.setValueSchemas(internalManager.getSchemas(result.getResult(), null));
			} catch (Exception e) {
				result.setStatusCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
				result.setStatusMessage(e.getMessage());
			}
			return result;
		}

		@Override
		public JsonObject getForIdStartsWith(String id) {
			CypherQueryBuilder builder = new CypherQueryBuilder()
					.MATCH()
					.WHERE("id")
					.STARTS_WITH(id)
					;
			builder.RETURN("id, value");
			builder.ORDER_BY("doc.seq");
			CypherQuery q = builder.build();
			JsonObject result = getForQuery(q.toString());
			return result;
		}

		
		public Reference getReference(String id) {
		    	ResultJsonObjectArray result = getReferenceObjectByRefId(id);
				Reference ref = (Reference) gson.fromJson(
						result.getValues().get(0)
						, Reference.class
				);	
				return ref;
		}

		public RequestStatus deleteForId(String requestor, String id) {
			// TODO: add test to make sure requestor is authorized
			return deleteForId(id);
		}
		
		@Override
		public RequestStatus deleteForId(String id) {
			RequestStatus result = new RequestStatus();
			try {
		    	result = neo4jManager.deleteWhereEqual(id);
			} catch (DbException e) {
				ErrorUtils.report(logger, e);
				result.setCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
				result.setMessage(HTTP_RESPONSE_CODES.BAD_REQUEST.message);
			} catch (Exception e) {
				result.setCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
				result.setMessage(e.getMessage());
			}
			return result;
		}

		public RequestStatus deleteRelationshipForId(String id) {
			RequestStatus result = new RequestStatus();
			try {
		    	result = neo4jManager.deleteRelationshipWhereEqual(id);
			} catch (DbException e) {
				ErrorUtils.report(logger, e);
				result.setCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
				result.setMessage(HTTP_RESPONSE_CODES.BAD_REQUEST.message);
			} catch (Exception e) {
				result.setCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
				result.setMessage(e.getMessage());
			}
			return result;
		}

		/**
		 * 
		 * @param id of the relationship
		 * @return
		 */
		public RequestStatus deleteForRelationshipId(String id) {
			RequestStatus result = new RequestStatus();
			try {
		    	result = neo4jManager.deleteRelationshipWhereEqual(id);
			} catch (DbException e) {
				ErrorUtils.report(logger, e);
				result.setCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
				result.setMessage(HTTP_RESPONSE_CODES.BAD_REQUEST.message);
			} catch (Exception e) {
				result.setCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
				result.setMessage(e.getMessage());
			}
			return result;
		}

		public boolean isPrettyPrint() {
			return prettyPrint;
		}

		public void setPrettyPrint(boolean prettyPrint) {
			this.prettyPrint = prettyPrint;
		}	
}