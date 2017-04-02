package ioc.liturgical.ws.managers.databases.external.neo4j;

import java.text.Normalizer;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import ioc.liturgical.ws.managers.interfaces.HighLevelDataStoreInterface;
import ioc.liturgical.ws.app.ServiceProvider;
import ioc.liturgical.ws.constants.HTTP_RESPONSE_CODES;
import ioc.liturgical.ws.constants.RELATIONSHIP_TYPES;
import ioc.liturgical.ws.constants.VERBS;
import ioc.liturgical.ws.managers.databases.external.neo4j.constants.MATCHERS;
import ioc.liturgical.ws.managers.databases.external.neo4j.utils.DomainTopicMapBuilder;
import ioc.liturgical.ws.managers.databases.external.neo4j.utils.Neo4jConnectionManager;
import ioc.liturgical.ws.managers.databases.internal.InternalDbManager;
import ioc.liturgical.ws.managers.exceptions.DbException;
import ioc.liturgical.ws.models.RequestStatus;
import ioc.liturgical.ws.models.ResultJsonObjectArray;
import ioc.liturgical.ws.models.db.forms.LinkRefersToBiblicalTextCreateForm;
import ioc.liturgical.ws.models.db.links.LinkRefersToBiblicalText;
import ioc.liturgical.ws.models.db.returns.LinkRefersToTextToTextTableRow;
import ioc.liturgical.ws.models.db.supers.LTKDb;
import ioc.liturgical.ws.models.options.DropdownOptionEntry;
import ioc.liturgical.ws.models.options.DropdownOptions;
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
	private boolean readOnly = false;
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
			  , boolean readOnly
			  , InternalDbManager internalManager
			  ) {
		  this.internalManager = internalManager; 
		  this.readOnly = readOnly;
		  neo4jManager = new Neo4jConnectionManager(
				  neo4jDomain
				  , ServiceProvider.ws_usr
				  , ServiceProvider.ws_pwd
				  , readOnly
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
			  , boolean readOnly
			  , InternalDbManager internalManager
			  ) {
		  this.readOnly = readOnly;
		  this.internalManager = internalManager; 
		  neo4jManager = new Neo4jConnectionManager(
				  neo4jDomain
				  , adminUserId
				  , adminUserPassword
				  , readOnly
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
				, String relationshipJson
				) {
			RequestStatus result = new RequestStatus();
			LinkRefersToBiblicalTextCreateForm form = new LinkRefersToBiblicalTextCreateForm("","","");
			form = (LinkRefersToBiblicalTextCreateForm) form.fromJsonString(relationshipJson);
			if (internalManager.authorized(requestor, VERBS.POST, form.getLibrary())) {
				String validation = form.validate(relationshipJson);
				if (validation.length() == 0) {
					try {
						LinkRefersToBiblicalText ref = new LinkRefersToBiblicalText(form); // set ref values from the form values
						ref.setCreatedBy(requestor);
						ref.setModifiedBy(requestor);
						ref.setCreatedWhen(getTimestamp());
						ref.setModifiedWhen(ref.getCreatedWhen());
						result = this.addLTKVDbObjectAsRelationship(
								ref
								, RELATIONSHIP_TYPES.REFERS_TO_BIBLICAL_TEXT
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
			ResultJsonObjectArray result = neo4jManager.getResultObjectForQuery(query);
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

		/**
		 * if the operator is an 'or':
		 * 
		 * any (x in ["a", "b"] where x in link.labels)
		 *
		 * if the operator is an 'and':
		 * 
		 * all (x in ["a", "b"] where x in link.labels)
		 * 
		 * @param property
		 * @param labels
		 * @param operator
		 * @return
		 */
		private String labelMatcher(String property, String labels, String operator) {
			StringBuffer result = new StringBuffer();
			String theOperator = " all "; // initialize but change to any if need be
			if (operator.trim().equals("or")) {
				theOperator = " any ";
			}
			if (labels != null && labels.length() > 0) {
				try {
					String [] parts = labels.split(",");
					if (parts.length > 0) {
						result.append(" and ");
						result.append(theOperator);
						result.append(" (x in [");
					    result.append("\"");
					    result.append(parts[0].trim());
					    result.append("\" ");
					}
					if (parts.length > 1) {
						for (int i=1; i < parts.length; i++) {
						    result.append(", \"");
						    result.append(parts[i].trim());
						    result.append("\"");
						}
					}
					result.append("] where x in ");
					result.append(property);
					result.append(") ");
				} catch (Exception e) {
					ErrorUtils.report(logger, e);
				}
			}
			return result.toString();
		}
		
		public ResultJsonObjectArray searchRelationships(
				String type // to match
				, String library // library to match
				, String labels // labels to match
				, String operator // for labels, e.g. AND, OR
				) {
			ResultJsonObjectArray result  = new ResultJsonObjectArray(true);
			try {
				// TODO: create a query builder for relationship searches
				String q = "match (from)-[link:" + type + "]->(to)  " 
					  + "where link.library = \"" + library + "\" " 
					  + labelMatcher("link.labels", labels, operator)
			          + LinkRefersToTextToTextTableRow.getReturnClause();
				result  = neo4jManager.getForQuery(q);
				result.setQuery(q.toString());
				result.setValueSchemas(internalManager.getSchemas(result.getResult(), null));
			} catch (Exception e) {
				result.setStatusCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
				result.setStatusMessage(e.getMessage());
			}
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
			boolean prefixProps = false;
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
			CypherQueryBuilder builder = new CypherQueryBuilder(prefixProps)
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
			builder.RETURN("id, library, topic, key, value");
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

		/**
		 * 
		 * The topic of the json ID must be set to the 'from' aka 'start' node ID
		 * of the relationship, and the key must be set to the 'to' aka 'end' node ID.
		 * @param json
		 * @return RequestStatus
		 * @throws BadIdException
		 * @throws DbException
		 * @throws MissingSchemaIdException
		 */
		public RequestStatus addLTKVDbObjectAsRelationship(
				LTKDb json
				, RELATIONSHIP_TYPES type
				) throws 
					BadIdException
					, DbException
					, MissingSchemaIdException {
			RequestStatus result = new RequestStatus();
			if (internalManager.existsSchema(json.get_valueSchemaId())) {
				if (this.existsUniqueRelationship(json.getId())) {
					result.setCode(HTTP_RESPONSE_CODES.CONFLICT.code);
					result.setMessage(HTTP_RESPONSE_CODES.CONFLICT.message + ": " + json.getId());
				} else {
					    result = neo4jManager.createRelationship(json.getTopic(), json, json.getKey(), type);		
				}
			} else {
				throw new MissingSchemaIdException(json.get_valueSchemaId());
			}
			return result;
		}

		public RequestStatus getRelationshipById(String id) {
			String query = "match (from)-[link]->(to) where link.id = \"" 
					+ id 
					+ "\"" 
					+ LinkRefersToTextToTextTableRow.getReturnClause()
					;
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

		public RequestStatus updateLTKVDbObjectAsReference(
				LTKDb json
				) throws BadIdException, DbException, MissingSchemaIdException {
			RequestStatus result = new RequestStatus();
			if (internalManager.existsSchema(json.get_valueSchemaId())) {
				if (existsUniqueRelationship(json.getId())) {
					neo4jManager.updateWhereRelationshipEqual(json);		
				} else {
					result.setCode(HTTP_RESPONSE_CODES.NOT_FOUND.code);
					result.setMessage(HTTP_RESPONSE_CODES.NOT_FOUND.message + ": " + json.getId());
				}
			} else {
				throw new MissingSchemaIdException(json.get_valueSchemaId());
			}
			return result;
		}

		public RequestStatus updateReference(
				String requestor
				, String id
				, String json
				) {
			RequestStatus result = new RequestStatus();
			LinkRefersToBiblicalText obj = new LinkRefersToBiblicalText("","","");
			String validation = obj.validate(json);
			if (validation.length() == 0) {
				try {
					obj = (LinkRefersToBiblicalText) obj.fromJsonString(json);
					obj.setModifiedBy(requestor);
					obj.setModifiedWhen(getTimestamp());
					result = updateLTKVDbObjectAsReference(obj);
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
				String q = "match ()-[r]->() where r.id =\"" + id + "\" return properties(r)";
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
				String q = "match (f)-[r]->(t) where r.id = \"" + id + "\" return properties(r)";
				result  = neo4jManager.getForQuery(q);
				List<JsonObject> refs = new ArrayList<JsonObject>();
				List<JsonObject> objects = result.getValues();
				for (JsonObject object : objects) {
					try {
						
						LinkRefersToBiblicalText ref = (LinkRefersToBiblicalText) gson.fromJson(
								object
								, LinkRefersToBiblicalText.class
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
				String q = "match (from)-[link]->(to) where link.id = \"" 
						+ id 
						+ "\"" 
						+ LinkRefersToTextToTextTableRow.getReturnClause();
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
		public ResultJsonObjectArray getRelationshipForType(String type) {
			ResultJsonObjectArray result  = new ResultJsonObjectArray(true);
			try {
				String q = "match ()-[link:" + type + "]->() return link";
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

		
		public LinkRefersToBiblicalText getReference(String id) {
		    	ResultJsonObjectArray result = getReferenceObjectByRefId(id);
				LinkRefersToBiblicalText ref = (LinkRefersToBiblicalText) gson.fromJson(
						result.getValues().get(0)
						, LinkRefersToBiblicalText.class
				);	
				return ref;
		}

		public RequestStatus deleteForId(String requestor, String id) {
			RequestStatus result = new RequestStatus();
			IdManager idManager = new IdManager(id);
			if (internalManager.authorized(
					requestor
					, VERBS.DELETE
					, idManager.getLibrary()
					)) {
		    	result = deleteForId(id);
			} else {
				result.setCode(HTTP_RESPONSE_CODES.UNAUTHORIZED.code);
				result.setMessage(HTTP_RESPONSE_CODES.UNAUTHORIZED.message);
			}
			return result;
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
		public RequestStatus deleteForRelationshipId(String requestor, String id) {
			RequestStatus result = new RequestStatus();
			try {
				IdManager idManager = new IdManager(id, 1, 4);
				if (internalManager.authorized(
						requestor
						, VERBS.DELETE
						, idManager.getLibrary()
						)) {
			    	result = neo4jManager.deleteRelationshipWhereEqual(id);
				} else {
					result.setCode(HTTP_RESPONSE_CODES.UNAUTHORIZED.code);
					result.setMessage(HTTP_RESPONSE_CODES.UNAUTHORIZED.message);
				}
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
		
		/**
		 * For each relationship type, returns the properties from the LTKDb
		 * subclass associated with the type.  The result can be used on the client 
		 * side to render a dropdown based on the user's selection.
		 * @return
		 */
		public ResultJsonObjectArray getRelationshipTypePropertyMaps() {
			ResultJsonObjectArray result = new ResultJsonObjectArray(false);
			try {
				Map<String,List<String>> map = RELATIONSHIP_TYPES.propertyMap();
				List<JsonObject> list = new ArrayList<JsonObject>();
				JsonObject json = new JsonObject();
				for ( Entry<String, List<String>> entry : map.entrySet()) {
					JsonArray array = new JsonArray();
					for (String prop : entry.getValue()) {
						array.add(prop);
					}
					json.add(entry.getKey(), array);
				}
				list.add(json);
				result.setResult(list);
			} catch (Exception e) {
				result.setStatusCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
				result.setStatusMessage(e.getMessage());
			}
			return result;
		}

		/**
		 * Get the unique set of labels currently in use for the specified relationship type
		 * @param type name of the relationship
		 * @return
		 */
		public JsonArray getRelationshipLabels(String type) {
			JsonArray result  = new JsonArray();
			try {
				String q = "match ()-[link:" + type + "]->() return distinct link.labels as " + type;
				JsonArray queryResult  = neo4jManager.getForQuery(q).getResult().get(0).get(type).getAsJsonArray();
				List<String> labels  = new ArrayList<String>();
				// combine the labels into a unique list
				for (JsonElement obj : queryResult) {
					if (! labels.contains(obj.getAsString())) {
						labels.add(obj.getAsString());
					}
				}
				// sort the labels
				java.util.Collections.sort(labels);
				// add the labels to a JsonArray of Option Entries.
				for (String label : labels) {
					DropdownOptionEntry entry = new DropdownOptionEntry(label);
					result.add(entry.toJsonObject());
				}
			} catch (Exception e) {
				ErrorUtils.report(logger, e);
			}
			return result;
		}

		/**
		 * Get the unique set of labels currently in use for the specified relationship type
		 * @param type name of the relationship
		 * @return
		 */
		public JsonObject getRelationshipLabelsForAllTypes() {
			JsonObject result  = new JsonObject();
			try {
				for (RELATIONSHIP_TYPES t : RELATIONSHIP_TYPES.values()) {
					JsonArray value = getRelationshipLabels(t.typename);
					result.add(t.typename, value);
				}
			} catch (Exception e) {
				ErrorUtils.report(logger, e);
			}
			return result;
		}

		/**
		 * Returns a JsonObject with the following structure:
		 * dropdown: {
		 * 		typelist: [{Option}] // for a dropdown list of the available relationship types, as options, e.g. value : label
		 *     , typeprops: {
		 *     		someTypeName: [{Option}]
		 *         , someOtherTypeName: [{Option}]
		 *     }
		 *     , typelabels: {
		 *     		someTypeName: [string, string, string]
		 *         , someOtherTypeName: [string, string, string]
		 *     }
		 *     
		 *     How to use:
		 *     1. Load typelist into a dropdown list.
		 *     2. When user selects a type:
		 *          2.1 Use the typename to lookup the typeprops and set the properties dropdown to them
		 *          2.2 Use the typename to lookup the typelabels and set the labels using them.
		 * }
		 * @return
		 */
		public ResultJsonObjectArray getRelationshipSearchDropdown() {
			ResultJsonObjectArray result  = new ResultJsonObjectArray(true);
			try {
				JsonObject values = new JsonObject();
				values.add("typelist", getRelationshipTypesArray());
				values.add("typeprops", RELATIONSHIP_TYPES.propertyJson());
				values.add("typelabels", getRelationshipLabelsForAllTypes());

				JsonObject jsonDropdown = new JsonObject();
				jsonDropdown.add("dropdown", values);

				List<JsonObject> list = new ArrayList<JsonObject>();
				list.add(jsonDropdown);

				result.setResult(list);
				result.setQuery("get dropdowns for relationship search");

			} catch (Exception e) {
				result.setStatusCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
				result.setStatusMessage(e.getMessage());
			}
			return result;
		}
		
		public JsonArray getRelationshipTypesArray() {
			JsonArray result = new JsonArray();
			try {
				DropdownOptions types = new DropdownOptions();
				for (RELATIONSHIP_TYPES t : RELATIONSHIP_TYPES.values()) {
					types.addSingleton(t.typename);
				}
				result = types.toJsonArray();
			} catch (Exception e) {
				ErrorUtils.report(logger, e);
			}
			return result;
		}
}