package ioc.liturgical.ws.managers.databases.external.neo4j;

import java.text.Normalizer;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import ioc.liturgical.ws.managers.interfaces.HighLevelDataStoreInterface;
import ioc.liturgical.ws.app.ServiceProvider;
import ioc.liturgical.ws.constants.BIBLICAL_BOOKS;
import ioc.liturgical.ws.constants.Constants;
import ioc.liturgical.ws.constants.EXTERNAL_DB_SCHEMA_CLASSES;
import ioc.liturgical.ws.constants.HTTP_RESPONSE_CODES;
import ioc.liturgical.ws.constants.NEW_FORM_CLASSES_DB_API;
import ioc.liturgical.ws.constants.ONTOLOGY_TOPICS;
import ioc.liturgical.ws.constants.RELATIONSHIP_TYPES;
import ioc.liturgical.ws.constants.UTILITIES;
import ioc.liturgical.ws.constants.VERBS;
import ioc.liturgical.ws.managers.databases.external.neo4j.constants.MATCHERS;
import ioc.liturgical.ws.managers.databases.external.neo4j.cypher.CypherQueryBuilderForDocs;
import ioc.liturgical.ws.managers.databases.external.neo4j.cypher.CypherQueryBuilderForLinks;
import ioc.liturgical.ws.managers.databases.external.neo4j.cypher.CypherQueryForDocs;
import ioc.liturgical.ws.managers.databases.external.neo4j.cypher.CypherQueryForLinks;
import ioc.liturgical.ws.managers.databases.external.neo4j.utils.DomainTopicMapBuilder;
import ioc.liturgical.ws.managers.databases.external.neo4j.utils.Neo4jConnectionManager;
import ioc.liturgical.ws.managers.databases.external.neo4j.utils.OntologyGenerator;
import ioc.liturgical.ws.managers.databases.internal.InternalDbManager;
import ioc.liturgical.ws.managers.exceptions.DbException;
import ioc.liturgical.ws.models.RequestStatus;
import ioc.liturgical.ws.models.ResultJsonObjectArray;
import ioc.liturgical.ws.models.db.docs.grammar.PerseusAnalyses;
import ioc.liturgical.ws.models.db.docs.grammar.PerseusAnalysis;
import ioc.liturgical.ws.models.db.links.LinkRefersToBiblicalText;
import ioc.liturgical.ws.models.db.returns.LinkRefersToTextToTextTableRow;
import ioc.liturgical.ws.models.db.returns.ResultNewForms;
import ioc.liturgical.ws.models.db.supers.LTK;
import ioc.liturgical.ws.models.db.supers.LTKDb;
import ioc.liturgical.ws.models.db.supers.LTKDbOntologyEntry;
import ioc.liturgical.ws.models.db.supers.LTKLink;
import ioc.liturgical.ws.models.ws.db.Utility;
import net.ages.alwb.utils.core.datastores.json.exceptions.BadIdException;
import net.ages.alwb.utils.core.datastores.json.exceptions.MissingSchemaIdException;
import net.ages.alwb.utils.core.datastores.json.models.DropdownArray;
import net.ages.alwb.utils.core.datastores.json.models.DropdownItem;
import net.ages.alwb.utils.core.datastores.json.models.LTKVJsonObject;
import net.ages.alwb.utils.core.error.handling.ErrorUtils;
import net.ages.alwb.utils.core.id.managers.IdManager;
import net.ages.alwb.utils.nlp.fetchers.Lexigram;
import net.ages.alwb.utils.nlp.fetchers.PerseusMorph;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.tokenize.Tokenizer;


/**
 * Provides the high level interface to the low level database, Neo4j
 * 
 * Notes:
 * - Neo4j supports unique property constraints only on nodes.  
 *   So, we have to programmatically enforce a unique value on
 *   the id property of a relationship, if it has properties.  They
 *   do not have to have them as far as Neo4j is concerned.
 * 
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
	private String adminUserId = "";

	  JsonParser parser = new JsonParser();
	  Pattern punctPattern = Pattern.compile("[˙·,.;!?(){}\\[\\]<>%]"); // punctuation 
	  DomainTopicMapBuilder domainTopicMapbuilder = new DomainTopicMapBuilder();
	  JsonObject dropdownItemsForSearchingText = new JsonObject();
	  JsonArray ontologyTypesArray = new JsonArray();
	  JsonObject ontologyTypesProperties = new JsonObject();
	  JsonArray relationshipTypesArray = new JsonArray();
	  JsonObject relationshipTypesProperties = new JsonObject();
	  JsonArray tagOperatorsDropdown = new JsonArray();
	  List<DropdownItem> biblicalBookNamesDropdown = new ArrayList<DropdownItem>();
	  List<DropdownItem> biblicalChapterNumbersDropdown = new ArrayList<DropdownItem>();
	  List<DropdownItem> biblicalVerseNumbersDropdown = new ArrayList<DropdownItem>();
	  List<DropdownItem> biblicalVerseSubVersesDropdown = new ArrayList<DropdownItem>();
	  public static Neo4jConnectionManager neo4jManager = null;
	  InternalDbManager internalManager = null;
	  
	  public ExternalDbManager(
			  String neo4jDomain
			  , boolean logQueries
			  , boolean logQueriesWithNoMatches
			  , boolean readOnly
			  , InternalDbManager internalManager
			  ) {
		  this.adminUserId = ServiceProvider.ws_usr;
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
		  buildRelationshipDropdownMaps();
		  buildBiblicalDropdowns();
		  if (neo4jManager.isConnectionOK()) {
			  buildOntologyDropdownMaps();
			  initializeOntology();
			  if (! this.existsUnique("en_sys_grammar~ἀβλαβεῖς~ἀβλαβής/ADJ.PL.F.VOC")) {
				  this.loadTheophanyGrammar();
			  }
		  }
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
		  this.adminUserId = adminUserId;
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
			  buildOntologyDropdownMaps();
			  buildBiblicalDropdowns();
			  buildRelationshipDropdownMaps();
		  }
		  if (neo4jManager.isConnectionOK()) {
			  buildOntologyDropdownMaps();
			  initializeOntology();
		  }
	  }

	  
	  private void buildBiblicalDropdowns() {
		  buildBiblicalBookNamesDropdown();
		  buildBiblicalChapterNumbersDropdown();
		  buildBiblicalVerseNumbersDropdown();
		  buildBiblicalVerseSubVersesDropdown();
	  }

	  private void buildBiblicalBookNamesDropdown() {
		  this.biblicalBookNamesDropdown = BIBLICAL_BOOKS.toDropdownList();
	  }
	  
	  private void buildBiblicalChapterNumbersDropdown() {
		  biblicalChapterNumbersDropdown = getNumbersDropdown("C", 151);
	  }

	  private void buildBiblicalVerseNumbersDropdown() {
		  biblicalVerseNumbersDropdown = getNumbersDropdown("", 180);
	  }

	  private List<DropdownItem> getNumbersDropdown(String prefix, int max) {
		  List<DropdownItem> result = new ArrayList<DropdownItem>();
			 for (int i = 1; i < max+1; i++) {
				 String c = prefix;
				 if (i < 10) {
					 c = c + "00" + i;
				 } else if (i < 100) {
					 c = c + "0" + i;
				 } else {
					 c = c + i;
				 }
				 result.add(new DropdownItem(c));
			 }
			 return result;
	  }

	  private void buildBiblicalVerseSubVersesDropdown() {
		  char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
			 biblicalVerseSubVersesDropdown.add(new DropdownItem("Not applicable","*"));
		  for (char c : alphabet) {
			  	String s = String.valueOf(c);
				 biblicalVerseSubVersesDropdown.add(new DropdownItem(s,s));
		  }
	  }
	  
	  /**
	   * If the database ontology has not yet been initialized,
	   * this will populate it with a basic set of entries.
	   */
	  private void initializeOntology() {
		  if (dbIsWritable() && dbMissingOntologyEntries()) {
			  try {
				  logger.info("Initializing ontology for the database.");
				  this.createConstraintUniqueNodeId(ONTOLOGY_TOPICS.ROOT.keyname);
				  OntologyGenerator generator = new OntologyGenerator();
				  for (LTKDbOntologyEntry entry : generator.getEntries()) {
					  try {
						  RequestStatus status = this.addLTKDbObject(
								  this.adminUserId
								  , entry.toJsonString()
								  );
						  if (status.getCode() != HTTP_RESPONSE_CODES.CREATED.code) {
							  throw new Exception("Error creating ontology");
						  } else {
							  logger.info("Added to the ontology relationship " + entry.fetchOntologyLabelsList().toString() + ": " + entry.getName());
						  }
					  } catch (Exception e) {
						  ErrorUtils.report(logger, e);
					  }
				  }
				  for (LTKLink link : generator.getLinks()) {
					  try {
						  RequestStatus status = this.addLTKVDbObjectAsRelationship(
								  link
								  );
						  if (status.getCode() != HTTP_RESPONSE_CODES.CREATED.code) {
							  throw new Exception("Error creating ontology");
						  } else {
							  logger.info("Added to the ontology " 
									  + link.getTopic() + " " 
									  + link.getType() + " " 
									  + link.getKey());
						  }
					  } catch (Exception e) {
						  ErrorUtils.report(logger, e);
					  }
				  }
			  } catch (Exception e) {
				  ErrorUtils.report(logger, e);
			  }
		  }
	  }

	  /**
	   * When DB manager initializes, we will read the database to load
	   * relationship dropdown items that are static.
	   */
	  public void buildRelationshipDropdownMaps() {
		  relationshipTypesArray = this.getRelationshipTypesArray();
		  relationshipTypesProperties = EXTERNAL_DB_SCHEMA_CLASSES.relationshipPropertyJson();
		  tagOperatorsDropdown = getTagOperatorsArray();
	  }

	  public void buildOntologyDropdownMaps() {
		  ontologyTypesProperties = EXTERNAL_DB_SCHEMA_CLASSES.ontologyPropertyJson();
		  ontologyTypesArray = EXTERNAL_DB_SCHEMA_CLASSES.ontologyTypesJson();
	  }

	  public JsonArray getTagOperatorsArray() {
			JsonArray result = new JsonArray();
			try {
				DropdownArray types = new DropdownArray();
				types.add(new DropdownItem("All","all"));
				types.add(new DropdownItem("Any","any"));
				result = types.toJsonArray();
			} catch (Exception e) {
				ErrorUtils.report(logger, e);
			}
			return result;
	  }
	  
	  public void buildDomainTopicMap() {
		 dropdownItemsForSearchingText = domainTopicMapbuilder.getDropdownItems();
	  }
	  
	  /**
	   * Adds a reference that subclasses LTK and is a LinkRefersTo relationship.
	   * 
	   * @param requestor - id of user who is making the request
	   * @param relationshipJson - must be subclass of LTK
	   * @return
	   */
		public RequestStatus addReference(
				String requestor
				, String json
				) {
			RequestStatus result = new RequestStatus();
			try {
				// First use the LTK superclass so we can extract the valueSchemaId
				LTK form = gson.fromJson(json, LTK.class);
				// Now get a handle to the instances for the specified schema
				EXTERNAL_DB_SCHEMA_CLASSES schema = EXTERNAL_DB_SCHEMA_CLASSES.classForSchemaName(form.get_valueSchemaId());
				form = 
						gson.fromJson(
								json
								, schema.ltk.getClass()
					);
				// Create the database version
				LTKLink ref = 
						(LTKLink) gson.fromJson(
								json
								, schema.ltkDb.getClass()
					);
				if (internalManager.authorized(requestor, VERBS.POST, form.getLibrary())) {
					String validation = form.validate(json);
					if (validation.length() == 0) {
						ref.setCreatedBy(requestor);
						ref.setModifiedBy(requestor);
						ref.setCreatedWhen(Instant.now().toString());
						ref.setModifiedWhen(ref.getCreatedWhen());
						result = this.addLTKVDbObjectAsRelationship(
								ref
								);
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
			} catch (Exception e) {
				result.setCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
				result.setMessage(HTTP_RESPONSE_CODES.BAD_REQUEST.message);
			}
			return result;
		}

	  private ResultJsonObjectArray setValueSchemas(ResultJsonObjectArray result) {
			List<JsonObject> jsonList = result.getResult();
			result.setValueSchemas(internalManager.getSchemas(jsonList, null));
			result.setResult(jsonList);
		  return result;
	  }

	  public JsonObject getForQuery(String query, boolean setValueSchemas) {
			ResultJsonObjectArray result = neo4jManager.getResultObjectForQuery(query);
			result.setQuery(query);
			if (logAllQueries
					|| 
					(logQueriesWithNoMatches && result.getResultCount() == 0) 
					) {
				logger.info(query);
				logger.info("Result count: " + result.getResultCount());
			}
			if (setValueSchemas) {
				result = setValueSchemas(result);
			}
			return result.toJsonObject();
	}
	  
	  /**
	   * 
	   * @param label - the node label to use
	   * @param idRegEx  - a regular expression for the ID, e.g. "gr_gr_cog~me.*text"
	   * @return
	   */
	  public JsonObject getWordListWithFrequencyCounts(
			  String label
			  , String idRegEx
			  ) {
		  String query = "MATCH (n:" 
				  + label 
				  + ")"
				  + " WHERE n.id =~ \""
				  + idRegEx
				  + "\""
				  + " WITH split(n.nnp,\" \") as words"
				  + " UNWIND range(0,size(words)-2) as idx"
				  + " WITH distinct words[idx] as word, count(words[idx]) as count"
				  + " RETURN word, count order by count descending";
		  return this.getForQuery(query, false).getAsJsonObject();
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
			result = getForQuery(getCypherQueryForDocSearch(type, domain, book, chapter, query, property, matcher), true);
			return result;
		}

		
		public JsonObject searchOntology(
				String type // to match
				, String genericType // generic type to match
				, String query
				, String property
				, String matcher
				, String tags // tags to match
				, String operator // for tags, e.g. AND, OR
				) {
			JsonObject result = null;

			result = getForQuery(
					getCypherQueryForOntologySearch(
							type
							, genericType
							, query
							, property
							, matcher
							, tags 
							, operator
							)
					, true
					);
			return result;
		}

		public JsonObject searchRelationships(
				String type // to match
				, String library // library to match
				, String query
				, String property
				, String matcher
				, String tags // tags to match
				, String operator // for tags, e.g. AND, OR
				) {
			JsonObject result = null;

			result = getForQuery(
					getCypherQueryForLinkSearch(
							type
							, library
							, query
							, property
							, matcher
							, tags 
							, operator
							)
					, true
					);
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
	   
		private String getCypherQueryForDocSearch(
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
			CypherQueryBuilderForDocs builder = new CypherQueryBuilderForDocs(prefixProps)
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
			builder.RETURN("id, library, topic, key, value, seq");
			builder.ORDER_BY("doc.seq"); // TODO: in future this could be a parameter in REST API

			CypherQueryForDocs q = builder.build();
			
			return q.toString();
		}
		
		private String getCypherQueryForOntologySearch(
				String type
				, String genericType
				, String query
				, String property
				, String matcher
				, String tags // tags to match
				, String operator // for tags, e.g. AND, OR
				) {
			boolean prefixProps = false;
			String theGenericType = genericType;
			if (genericType.startsWith("*") && type.startsWith("*")) {
				theGenericType = ONTOLOGY_TOPICS.ROOT.keyname;
			}
			String theQuery = query;
			CypherQueryBuilderForDocs builder = new CypherQueryBuilderForDocs(prefixProps)
					.MATCH()
					.TOPIC(type)
					.LABEL(theGenericType)
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
			builder.TAGS(tags);
			builder.TAG_OPERATOR(operator);

			builder.RETURN("id, library, topic, key, name, description, tags");
			builder.ORDER_BY("doc.seq"); // 

			CypherQueryForDocs q = builder.build();
			
			return q.toString();
		}

		private String getCypherQueryForLinkSearch(
				String type
				, String library
				, String query
				, String property
				, String matcher
				, String tags // tags to match
				, String operator // for tags, e.g. AND, OR
				) {
			
			boolean prefixProps = false;
			String theQuery = query;
			CypherQueryBuilderForLinks builder = new CypherQueryBuilderForLinks(prefixProps)
					.MATCH()
					.TYPE(type)
					.LIBRARY(library)
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
			builder.TAGS(tags);
			builder.TAG_OPERATOR(operator);
			builder.RETURN("link.library as library, link.topic as fromId, type(link) as type, link.key as toId, link.tags as tags");
			builder.ORDER_BY("fromId + type + toId ascending"); // TODO: in future this could be a parameter in REST API

			CypherQueryForLinks q = builder.build();
			
			return q.toString();
		}


		public JsonObject getDropdownItemsForSearchingText() {
			return dropdownItemsForSearchingText;
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
		 * @param requestor
		 * @param entry
		 * @return
		 * @throws BadIdException
		 * @throws DbException
		 * @throws MissingSchemaIdException
		 */
		public RequestStatus addLTKDbObject(
				String requestor
				, String json // must be a subclass of LTKDb
				)  {
			RequestStatus result = new RequestStatus();
			LTK form = gson.fromJson(json, LTK.class);
			if (internalManager.authorized(requestor, VERBS.POST, form.getLibrary())) {
				String validation = EXTERNAL_DB_SCHEMA_CLASSES.validate(json);
				if (validation.length() == 0) {
				try {
						LTKDb record = 
								 gson.fromJson(
										json
										, EXTERNAL_DB_SCHEMA_CLASSES
											.classForSchemaName(
													form.get_valueSchemaId())
											.ltkDb.getClass()
							);
						record.setSubClassProperties(json);
						record.setActive(true);
						record.setCreatedBy(requestor);
						record.setModifiedBy(requestor);
						record.setCreatedWhen(getTimestamp());
						record.setModifiedWhen(record.getCreatedWhen());
					    RequestStatus insertStatus = neo4jManager.insert(record);		
					    result.setCode(insertStatus.getCode());
					    result.setDeveloperMessage(insertStatus.getDeveloperMessage());
					    result.setUserMessage(insertStatus.getUserMessage());
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
				LTKLink json
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
					    result = neo4jManager.createRelationship(
					    		json.getTopic()
					    		, json
					    		, json.getKey()
					    		, json.getType()
					    		);		
				}
			} else {
				throw new MissingSchemaIdException(json.get_valueSchemaId());
			}
			return result;
		}

		public ResultJsonObjectArray getRelationshipById(
				String requestor
				, String library
				,  String id) {
			ResultJsonObjectArray result = new ResultJsonObjectArray(true);
			if (internalManager.authorized(
					requestor
					, VERBS.DELETE
					, library
					)) {
		    	result = getForIdOfRelationship(id);
			} else {
				result.setStatusCode(HTTP_RESPONSE_CODES.UNAUTHORIZED.code);
				result.setStatusMessage(HTTP_RESPONSE_CODES.UNAUTHORIZED.message);
			}
			return result;
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

		public RequestStatus updateLTKVDbObjectAsRelationship(
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

		public RequestStatus updateLTKDbObject(
				String requestor
				, String json // must be a subclass of LTKDbOntologyEntry
				)  {
			RequestStatus result = new RequestStatus();
			try {
				LTKDb record = gson.fromJson(json, LTKDb.class);
				if (internalManager.authorized(requestor, VERBS.PUT, record.getLibrary())) {
					// convert it to the proper subclass of LTKDb
					record = 
							gson.fromJson(
									json
									, EXTERNAL_DB_SCHEMA_CLASSES
										.classForSchemaName(
												record.get_valueSchemaId())
										.ltkDb.getClass()
						);
					String validation = record.validate(json);
					if (validation.length() == 0) {
						record.setCreatedBy(requestor);
						record.setModifiedBy(requestor);
						record.setCreatedWhen(getTimestamp());
						record.setModifiedWhen(record.getCreatedWhen());
						neo4jManager.updateWhereEqual(record);
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
			} catch (Exception e) {
				result.setCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
				result.setMessage(HTTP_RESPONSE_CODES.BAD_REQUEST.message);
			}
			return result;
		}

		/**
		 * 
		 * @param requestor
		 * @param id
		 * @param json must be from on object of type LTKDb
		 * @return
		 */
		public RequestStatus updateReference(
				String requestor
				, String id
				, String json
				) {
			RequestStatus result = new RequestStatus();
			try {
				LTKDb obj = gson.fromJson(json, LTKDb.class);
				obj = 
						gson.fromJson(
								json
								, EXTERNAL_DB_SCHEMA_CLASSES
									.classForSchemaName(
											obj.get_valueSchemaId())
									.ltkDb.getClass()
					);
				String validation = obj.validate(json);
				if (validation.length() == 0) {
						obj.setModifiedBy(requestor);
						obj.setModifiedWhen(getTimestamp());
						result = updateLTKVDbObjectAsRelationship(obj);
				} else {
					result.setCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
					result.setMessage(validation);
				}
			} catch (Exception e) {
				result.setCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
				result.setMessage(HTTP_RESPONSE_CODES.BAD_REQUEST.message);
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
				ResultJsonObjectArray json = this.getForIdOfRelationship(id);
				return json.getResultCount() == 1;
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
				CypherQueryBuilderForDocs builder = new CypherQueryBuilderForDocs(false)
						.MATCH()
						.WHERE("id")
						.EQUALS(id)
						.RETURN("*")
						;
				CypherQueryForDocs q = builder.build();
				result  = neo4jManager.getForQuery(q.toString());
				result.setQuery(q.toString());
				result.setValueSchemas(internalManager.getSchemas(result.getResult(), null));
			} catch (Exception e) {
				result.setStatusCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
				result.setStatusMessage(e.getMessage());
			}
			return result.toJsonObject();
		}

		public ResultJsonObjectArray getForIdOfRelationship(String id) {
			ResultJsonObjectArray result  = new ResultJsonObjectArray(true);
			try {
				String q = "match ()-[link]->() where link.id =\"" + id + "\" return properties(link)";
				result  = neo4jManager.getForQuery(q.toString());
				result.setQuery(q);
				result.setValueSchemas(internalManager.getSchemas(result.getResult(), null));
			} catch (Exception e) {
				result.setStatusCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
				result.setStatusMessage(e.getMessage());
			}
			return result;
		}

		public ResultJsonObjectArray getReferenceObjectByRefId(String id) {
			ResultJsonObjectArray result  = new ResultJsonObjectArray(true);
			try {
				String q = "match ()-[link]->() where link.id = \"" + id + "\" return properties(link)";
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
				result.setValueSchemas(
						internalManager.getSchemas(
								result.getResult()
								, null
						)
				);
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
			CypherQueryBuilderForDocs builder = new CypherQueryBuilderForDocs()
					.MATCH()
					.WHERE("id")
					.STARTS_WITH(id)
					;
			builder.RETURN("id, value, seq");
			builder.ORDER_BY("doc.seq");
			CypherQueryForDocs q = builder.build();
			JsonObject result = getForQuery(q.toString(), true);
			return result;
		}


		/**
		 * Get the record for the specified ID
		 * as well as the n number before and 
		 * n number after 
		 * @param id
		 * @param n
		 * @return
		 */
		public JsonObject getContext(
				String id
				, int n
				) {
			JsonObject result = new JsonObject();
			String seq = this.getSequenceForId(id);
			if (seq != null) {
				IdManager idManager = new IdManager(seq);
				int startingSeq = IdManager.getWindowPrefixIndex(seq, n);
				int endingSeq = IdManager.getWindowSuffixIndex(seq, n);
				result = this.getForSeqRange(
						idManager.getLibrary()
						, idManager.getTopic()
						, startingSeq
						, endingSeq
						);
			}
			return result;
		}
		
		/**
		 * For the specified ID, retrieves the record and
		 * if it has a sequence property (seq), it will
		 * return the sequence.
		 * @param id
		 * @return
		 */
		public String getSequenceForId(String id) {
			String result = null;
			JsonObject record = this.getForId(id);
			try {
				result = record.get("values").getAsJsonArray().get(0).getAsJsonObject().get("seq").getAsString();
			} catch (Exception e) {
				result = null;
			}
			return result;
		}


		/**
		 * For the specified library / topic, get all the entries whose sequence
		 * is greater than or equal to the starting sequence and
		 * less than or equal to the ending sequence.
		 * @param library
		 * @param topic
		 * @param startingSeq
		 * @param endingSeq
		 * @return
		 */
		public JsonObject getForSeqRange(
				String library
				, String topic
				, int startingSeq
				, int endingSeq
				) {
			CypherQueryBuilderForDocs builder = new CypherQueryBuilderForDocs()
					.MATCH()
					.WHERE("seq")
					.GREATER_THAN_OR_EQUAL(IdManager.createSeqNbr(library, topic, startingSeq))
					.LESS_THAN_OR_EQUAL(IdManager.createSeqNbr(library, topic, endingSeq))
					;
			builder.RETURN("id, value, seq");
			builder.ORDER_BY("doc.seq");
			CypherQueryForDocs q = builder.build();
			JsonObject result = getForQuery(q.toString(), true);
			return result;
		}

		/**
		 * Get all records for the specified library and topic
		 * @param library
		 * @param topic
		 * @return
		 */
		public JsonObject getTopic(String library, String topic) {
			return getForIdStartsWith(library + "~" + topic);
		}
		
		/**
		 * Reads all the records for the specified libary and topic
		 * and returns them as a string, that is formatted 
		 * as a resource file (res*.tex) for the 
		 * OCMC ShareLatex Liturgical Workbench.
		 * 
		 * Records for Liturgical text have a seq property,
		 * e.g. gr_gr_cog~me.m01.d01~L0010
		 * that indicates the sequence number as originally read from an ALWB ares file.
		 * 
		 * The startingSequence and endingSequence can be used to get a subset of
		 * the lines.  
		 * 
		 * see http://stackoverflow.com/questions/27244780/how-download-file-using-java-spark
		 * @param library
		 * @param topic
		 * @param startingSequence - will ignore if set to -1
		 * @param endingSequence - will ignore if set to -1
		 * @return
		 */
		public JsonObject getTopicAsOslwFileContents(
				String library
				, String topic
				, int startingSequence  
				, int endingSequence  
				) {
			ResultJsonObjectArray result = new ResultJsonObjectArray(this.prettyPrint);
			boolean useSequenceNumbers = startingSequence != -1 && endingSequence != -1;
			JsonObject queryResult = null;
			
			if (useSequenceNumbers) {
				queryResult = this.getForSeqRange(library, topic, startingSequence, endingSequence);
			} else {
				queryResult = this.getForIdStartsWith(library + Constants.ID_DELIMITER + topic);
			}
			StringBuffer sb = new StringBuffer();
			for (JsonElement e : queryResult.get("values").getAsJsonArray()) {
					JsonObject record = e.getAsJsonObject();
						sb.append(this.getAsOslwResource(
								record.get("doc.id").getAsString()
								, record.get("doc.value").getAsString()
								)
						);
			}
			JsonObject json = new JsonObject();
			JsonElement value = new JsonPrimitive(library);
			json.add("library", value);
			value = new JsonPrimitive(topic);
			json.add("topic", value);
			value = new JsonPrimitive(sb.toString());
			json.add("keys", value);
			result.addValue(json);
			return result.toJsonObject();
		}
		
		public JsonObject getTopicAsJson(
				String library
				, String topic
				, int startingSequence  
				, int endingSequence  
				) {
			JsonObject result = new JsonObject();
			boolean useSequenceNumbers = startingSequence != -1 && endingSequence != -1;
			if (useSequenceNumbers) {
				result = this.getForSeqRange(library, topic, startingSequence, endingSequence);
			} else {
				result = this.getForIdStartsWith(library + Constants.ID_DELIMITER + topic);
			}
			return result;
		}
		
		public Set<String> getTopicUniqueTokens(
				String library
				, String topic
				, int startingSequence  
				, int endingSequence  
				) {
			Set<String> result = new TreeSet<String>();
			JsonObject queryResult = getTopicAsJson(
					library
					, topic
					, startingSequence  
					, endingSequence  
					);
				for (JsonElement e : queryResult.get("values").getAsJsonArray()) {
					String value = e.getAsJsonObject().get("doc.value").getAsString();
					Tokenizer tokenizer = SimpleTokenizer.INSTANCE;
			        String [] theTokens = tokenizer.tokenize(value);
			        for (String token : theTokens) {
			        	String lower = token.toLowerCase();
			        	if (! result.contains(lower)) {
			        		result.add(lower);
			        	}
			        }
			}
			return result;
		}

		private String getAsOslwResource(String id, String value) {
			StringBuffer result = new StringBuffer();
			IdManager idManager = new IdManager(id);
			result.append(idManager.getOslwResourceForValue(value));
			return result.toString();
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
		    	result = neo4jManager.deleteNodeWhereEqual(id);
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
		
		public ResultJsonObjectArray getOntologySearchDropdown() {
			ResultJsonObjectArray result  = new ResultJsonObjectArray(true);
			try {
				JsonObject values = new JsonObject();
				values.add("typeList", this.ontologyTypesArray);
				values.add("typeProps", this.ontologyTypesProperties);
				values.add("typeTags", getOntologyTagsForAllTypes());
				values.add("tagOperators", tagOperatorsDropdown);
				JsonObject jsonDropdown = new JsonObject();
				jsonDropdown.add("dropdown", values);

				List<JsonObject> list = new ArrayList<JsonObject>();
				list.add(jsonDropdown);

				result.setResult(list);
				result.setQuery("get dropdowns for ontology search");

			} catch (Exception e) {
				result.setStatusCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
				result.setStatusMessage(e.getMessage());
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
				Map<String,List<String>> map = EXTERNAL_DB_SCHEMA_CLASSES.relationshipPropertyMap();
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
		 * Get the unique set of tags currently in use for the specified relationship type
		 * @param type name of the relationship
		 * @return
		 */
		public JsonArray getRelationshipTags(String type) {
			JsonArray result  = new JsonArray();
			try {
				String q = "match ()-[link:" + type + "]->() return distinct link.tags as " + type;
				ResultJsonObjectArray query = neo4jManager.getForQuery(q);
				if (query.getResultCount() > 0) {
					TreeSet<String> labels  = new TreeSet<String>();
					for (JsonObject obj : query.getResult()) {
						JsonArray queryResult  = obj.get(type).getAsJsonArray();
						// combine the labels into a unique list
						for (JsonElement e : queryResult) {
							if (! labels.contains(e.getAsString())) {
								labels.add(e.getAsString());
							}
						}
					}
					// sort the labels
					// add the labels to a JsonArray of Option Entries.
					for (String label : labels) {
						DropdownItem entry = new DropdownItem(label);
						result.add(entry.toJsonObject());
					}
				}
			} catch (Exception e) {
				ErrorUtils.report(logger, e);
			}
			return result;
		}

		public JsonArray getOntologyTags(String type) {
			JsonArray result  = new JsonArray();
			try {
				String q = "match (n:"+ type + ") return distinct n.tags as " + type;
				ResultJsonObjectArray query = neo4jManager.getForQuery(q);
				if (query.getResultCount() > 0) {
					TreeSet<String> labels  = new TreeSet<String>();
					for (JsonObject obj : query.getResult()) {
						if (obj.has(type)) {
							JsonArray queryResult  = obj.get(type).getAsJsonArray();
							// combine the labels into a unique list
							for (JsonElement e : queryResult) {
								if (! labels.contains(e.getAsString())) {
									labels.add(e.getAsString());
								}
							}
						}
					}
					// add the labels to a JsonArray of Option Entries.
					for (String label : labels) {
						DropdownItem entry = new DropdownItem(label);
						result.add(entry.toJsonObject());
					}
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
		public JsonObject getRelationshipTagsForAllTypes() {
			JsonObject result  = new JsonObject();
			try {
				for (RELATIONSHIP_TYPES t : RELATIONSHIP_TYPES.values()) {
					JsonArray value = getRelationshipTags(t.typename);
					result.add(t.typename, value);
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
		public JsonObject getOntologyTagsForAllTypes() {
			JsonObject result  = new JsonObject();
			try {
				for (ONTOLOGY_TOPICS t : ONTOLOGY_TOPICS.values()) {
					JsonArray value = getOntologyTags(t.keyname);
					result.add(t.keyname, value);
				}
			} catch (Exception e) {
				ErrorUtils.report(logger, e);
			}
			return result;
			
		}

		public JsonArray getRelationshipLibrarysAsDropdownItems(String type) {
			JsonArray result  = new JsonArray();
			result.add(new DropdownItem("Any","*").toJsonObject());
			try {
				String q = "match ()-[link:" + type + "]->() return distinct link.library  order by link.library ascending";
				ResultJsonObjectArray query = neo4jManager.getForQuery(q);
				if (query.getResultCount() > 0) {
					for (JsonObject item : query.getResult()) {
						result.add(new DropdownItem(item.get("link.library").getAsString()).toJsonObject());
					}
				}
			} catch (Exception e) {
				ErrorUtils.report(logger, e);
			}
			return result;
		}

		public JsonObject getRelationshipLibrarysForAllTypes() {
			JsonObject result  = new JsonObject();
			try {
				JsonArray any  = new JsonArray();
				any.add(new DropdownItem("Any","*").toJsonObject());
				result.add("*", any);
				for (RELATIONSHIP_TYPES t : RELATIONSHIP_TYPES.values()) {
					JsonArray value = getRelationshipLibrarysAsDropdownItems(t.typename);
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
		 * 		typeList: [{Option}] // for a dropdown list of the available relationship types, as options, e.g. value : label
		 *     , typeLibraries: {
		 *     		someTypeName: [{Option}]
		 *         , someOtherTypeName: [{Option}]
		 *     }
		 *     , typeProps: {
		 *     		someTypeName: [{Option}]
		 *         , someOtherTypeName: [{Option}]
		 *     }
		 *     , typeTags: {
		 *     		someTypeName: [string, string, string]
		 *         , someOtherTypeName: [string, string, string]
		 *     }
		 *     
		 *     How to use:
		 *     1. Load typelist into a dropdown list.
		 *     2. When user selects a type:
		 *          2.1 Use the typename to lookup the typeLibraries and set the libraries dropdown to them
		 *          2.2 Use the typename to lookup the typeProps and set the properties dropdown to them
		 *          2.3 Use the typename to lookup the typeTags and set the tags using them.
		 * }
		 * @return
		 */
		public ResultJsonObjectArray getRelationshipSearchDropdown() {
			ResultJsonObjectArray result  = new ResultJsonObjectArray(true);
			try {
				JsonObject values = new JsonObject();
				values.add("typeList", this.relationshipTypesArray);
				values.add("typeLibraries", this.getRelationshipLibrarysForAllTypes());
				values.add("typeProps", this.relationshipTypesProperties);
				values.add("typeTags", getRelationshipTagsForAllTypes());
				values.add("tagOperators", tagOperatorsDropdown);
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
				DropdownArray types = new DropdownArray();
				types.add(new DropdownItem("Any","*"));
				for (RELATIONSHIP_TYPES t : RELATIONSHIP_TYPES.values()) {
					types.addSingleton(t.typename);
				}
				result = types.toJsonArray();
			} catch (Exception e) {
				ErrorUtils.report(logger, e);
			}
			return result;
		}
		
		public boolean dbMissingOntologyEntries() {
			return ! dbHasOntologyEntries();
		}
		
		/**
		 * Checks to see whether the database contains Ontology Entries
		 * @return
		 */
		public boolean dbHasOntologyEntries() {
			boolean result = false;
			try {
				JsonObject entries = getForQuery("match (n:OntoRoot) return count(n)", false);
				int count = entries.get("values").getAsJsonArray().get(0).getAsJsonObject().get("count(n)").getAsInt();
				result = count > 0;
			} catch (Exception e) {
				ErrorUtils.report(logger, e);
			}
			return result;
		}
		
		/**
		 * Get a JsonArray of the instances for the specified type of ontology
		 * @param username
		 * @return
		 */
		public JsonArray getDropdownInstancesForOntologyType(String type) {
			JsonArray result = new JsonArray();
			String query  = "match (n:OntoRoot) where n.topic = \"" + type + "\" return n.id as id, n.name as name";
			JsonArray entries = getForQuery(query, false)
					.get("values").getAsJsonArray();
			for (JsonElement entry : entries) {
				String value = entry.getAsJsonObject().get("id").getAsString();
				String label = entry.getAsJsonObject().get("name").getAsString();
				result.add(new DropdownItem(label, value).toJsonObject());
			}
			return result;
		}

		/**
		 * Get dropdowns of instances of each type of ontology
		 * @return
		 */
		public Map<String, JsonArray> getDropdownsForOntologyInstances() {
			Map<String,JsonArray> result = new TreeMap<String,JsonArray>();
			for (ONTOLOGY_TOPICS t : ONTOLOGY_TOPICS.values()) {
				result.put(t.keyname, this.getDropdownInstancesForOntologyType(t.keyname));
			}
			return result;
		}

		/**
		 * Get a list of users known by the database
		 * @return
		 */
		public JsonObject callDbmsSecurityListUsers() {
			JsonObject result = getForQuery(
					"call dbms.security.listUsers"
					, false
					);
			return result;
		}

		/**
		 * "values":[{"description":"CONSTRAINT ON ( animal:Animal ) ASSERT animal.id IS UNIQUE"},{"desc...etc
		 * @return
		 */
		public JsonObject callDbConstraints() {
			JsonObject result = getForQuery(
					"call db.constraints"
					, false
					);
			if (result.get("valueCount").getAsInt() > 0) {
				JsonArray constraints = result.get("values").getAsJsonArray();
				result = new JsonObject();
				result.add("constraints", constraints);
			}
			return result;
		}

		/**
		 * A constraint is based on the combination of
		 * either a node Label or a relationship Type 
		 * plus some property.  At this point, all our
		 * constraints are based on the id property,
		 * whether it is a node or a type.  So, all we
		 * are checking for is the existence of a
		 * constraint based on the node Label name or the
		 * relationship Type name.
		 * @param constraint
		 * @return
		 */
		public boolean dbHasConstraint(String constraint) {
			return getDbConstraints().contains(constraint);
		}
		
		public List<String> getDbConstraints() {
			List<String> result = new ArrayList<String>();
			Pattern p = Pattern.compile("^CONSTRAINT ON (.*) ASSERT.*");
			for (JsonElement constraint : callDbConstraints().get("constraints").getAsJsonArray()) {
				try {
					String description  = constraint.getAsJsonObject().get("description").getAsString();
					Matcher m = p.matcher(description);
					if (m.matches()) {
						description = m.group(1);
						description = description.replace("( ", "");
						description = description.replace(" )", "");
						description = description.split(":")[1];
						result.add(description);
					}
				} catch (Exception e) {
					ErrorUtils.report(logger, e);
				}
			}
			return result;
		}
		public JsonObject callDbmsQueryJmx() {
			JsonObject result = getForQuery(
					"call dbms.queryJmx('org.neo4j:*')"
					, false
					);
			return result;
		}

		/**
		 * Returns the attributes for the server configuration.
		 * @return
		 */
		public JsonObject getServerConfiguration() {
			JsonObject result = new JsonObject();
			try {
				result = getForQuery(
						"call dbms.queryJmx('org.neo4j:instance=kernel#0,name=Configuration')"
						, false
						);
				result = result.get("values").getAsJsonArray()
						.get(0).getAsJsonObject()
						.get("attributes").getAsJsonObject()
						;
			} catch (Exception e) {
				// ignore
			}
			return result;
		}
		
		/**
		 * Is the database read-only?
		 * @return true if read-only, false if it is updatable
		 */
		public boolean dbIsReadOnly() {
			boolean result = false;
			try {
				JsonObject config = getServerConfiguration();
				result = config.get("dbms.shell.read_only").getAsJsonObject().get("value").getAsBoolean();
			} catch (Exception e) {
				// ignore - should only occur if db not available
				result = true;
			}
			return result;
		}
		
		/**
		 * The returned Json object has three keys: admin, author, reader.
		 * The value of each key is a JsonArray.
		 * The values of the JsonArrays are domains.
		 * Each domain is stored as a JsonObject with a key and label.
		 * @param username
		 * @return
		 */
		public JsonObject getDomainDropdownsForUser(String username) {
			return internalManager.getDomainDropdownsForUser(username).getAsJsonObject();
		}
		
		/**
		 * Can the database be updated?
		 * @return true if it can be updated, false if it is read-only
		 */
		public boolean dbIsWritable() {
			return ! dbIsReadOnly();
		}
		
		/**
		 * For the specified label, create a constraint on the ID property.
		 * 
		 * @param label
		 * @return OK if successful, CONFLICT if already exists
		 */
		public RequestStatus createConstraintUniqueNodeId(
				String label
				) {
			String query = "CREATE constraint on (p:" + label + ") ASSERT p.id IS UNIQUE";
			return neo4jManager.processConstraintQuery(query);
		}

		public RequestStatus createConstraintUniqueNode(
				String label
				, String property
				) {
			String query = "CREATE constraint on (p:" + label + ") ASSERT p." + property + " IS UNIQUE";
			return neo4jManager.processConstraintQuery(query);
		}

		/**
		 * For the specified node label, drop the unique 
		 * constraint for the ID property.
		 * 
		 * @param label
		 * @return OK if successful, BAD_REQUEST if constraint does not exist
		 */
		public RequestStatus dropConstraintUniqueNodeid(
				String label
				) {
			String query = "DROP constraint on (p:" + label + ") ASSERT p.id IS UNIQUE";
			return neo4jManager.processConstraintQuery(query);
		}
		
		public RequestStatus dropConstraintUniqueNode(
				String label
				, String property
				) {
			String query = "DROP constraint on (p:" + label + ") ASSERT p." + property + " IS UNIQUE";
			return neo4jManager.processConstraintQuery(query);
		}
		
		public RequestStatus dropConstraintUniqueNodeId(
				String label
				) {
			String query = "DROP constraint on (p:" + label + ") ASSERT p.id IS UNIQUE";
			return neo4jManager.processConstraintQuery(query);
		}
		
		/**
		 * Returns a JsonObject for dropdowns to
		 * create new objects.  Since the library 
		 * will be a domain, it also returns dropdowns for
		 * domains the user is allowed to admin, author, and read.
		 * 
		 * Also returns ontology instance dropdowns, with one dropdown
		 * for each ontology type. 
		 * 
		 * {
		 * domains: {
		 *   	admin: [
		 *   		{ value: "", label: "" }
		 *   		, { value: "", label: "" }
		 *   	]
		 *   	, author: [
		 *   		{ value: "", label: "" }
		 *   		, { value: "", label: "" }
		 *   	]
		 *   	, reader: [
		 *   		{ value: "", label: "" }
		 *   		, { value: "", label: "" }
		 *   	]
		 * 		}
		 * , ontologyDropdowns {
		 *     Human: [
		 *     	   {value: "", label: ""}
		 *     , etc.
		 *     ] 
		 * }
		 * , newforms: {
		 *    dropdown: [
		 *    		{ value: "", label: "" }
		 *         , { value: "", label: ""  }
		 *         , ...
		 *    ]
		 *    , valueSchemas:
		 *    , values:  
		 * }

		 * }
		 * @param requestor
		 * @param query
		 * @return
		 */
		public JsonObject getNewDocForms(String requestor, String query) {
			ResultNewForms result = new ResultNewForms(true);
			result.setQuery(query);
			result.setDomains(internalManager.getDomainDropdownsForUser(requestor));
			result.setOntologyTypesDropdown(ONTOLOGY_TOPICS.keyNamesToDropdown());
			result.setOntologyDropdowns(getDropdownsForOntologyInstances());
			result.setBiblicalBooksDropdown(this.biblicalBookNamesDropdown);
			result.setBiblicalChaptersDropdown(this.biblicalChapterNumbersDropdown);
			result.setBiblicalVersesDropdown(this.biblicalVerseNumbersDropdown);
			result.setBiblicalSubversesDropdown(this.biblicalVerseSubVersesDropdown);
			List<JsonObject> dbResults = new ArrayList<JsonObject>();
			try {
				for (NEW_FORM_CLASSES_DB_API e : NEW_FORM_CLASSES_DB_API.values()) {
					if (internalManager.userAuthorizedForThisForm(requestor, e.restriction)) {
						dbResults.add(e.obj.toJsonObject());
					}
				}
				result.setValueSchemas(internalManager.getSchemas(dbResults, requestor));
				Map<String,JsonObject> formsMap = new TreeMap<String,JsonObject>();
				for (JsonObject form : dbResults) {
					formsMap.put(form.get(Constants.VALUE_SCHEMA_ID).getAsString(), form);
				}
				result.setResult(formsMap);
			} catch (Exception e) {
				result.setStatusCode(HTTP_RESPONSE_CODES.SERVER_ERROR.code);
				result.setStatusMessage(e.getMessage());
			}
			return result.toJsonObject();
		}

		
		private void loadTheophanyGrammar() {
			Set<String> result = new TreeSet<String>();
		    Set<String> tokens = getTopicUniqueTokens(
		    		"gr_gr_cog"
		    		, "me.m01.d06"
		    		, 201
		    		, 651
		    		);
			  logger.info("Initializing token analyses for Canons of Theophany in the external database.");
		    for (String token : tokens) {
		    		PerseusMorph pm = new PerseusMorph(token);
		    		PerseusAnalyses analyses = pm.getAnalyses();
		    		for (PerseusAnalysis analysis : analyses.analyses ) {
		    			RequestStatus status = this.addLTKDbObject("wsadmin", analysis.toJsonString());
		    			if (status.getCode() != 201) {
		    				System.out.print("");
		    			}
		    		}
		    	}
		}
		
		/**
		 * Run the utility specified by the paramter utilityName.
		 * At this time, we are not doing anything with the json,
		 * but it is available for future use.
		 * @param requestor
		 * @param utilityName
		 * @param json
		 * @return
		 */
		public RequestStatus runUtility(
				String requestor
				, String utilityName
				, String json
				) {
			RequestStatus result = new RequestStatus();
			Utility form = gson.fromJson(json, Utility.class);
			if (internalManager.isWsAdmin(requestor)) {
				switch (UTILITIES.valueOf(utilityName)) {
				case FetchPerseusParses:
					break;
				case Tokenize:
					result = runUtilityTokenize();
					break;
				default:
					break;
				}
			} else {
				result.setCode(HTTP_RESPONSE_CODES.UNAUTHORIZED.code);
				result.setMessage(HTTP_RESPONSE_CODES.UNAUTHORIZED.message);
			}
			return result;
		}
		
		public RequestStatus runUtilityTokenize() {
			RequestStatus status = new RequestStatus();
			StringBuffer sb = new StringBuffer();
			sb.append("MATCH (n:Liturgical) where n.id starts with \"gr_gr_cog\" ");
			sb.append("WITH split(n.value,\" \") as words ");
			sb.append("UNWIND range(0,size(words)-2) as idx ");
			sb.append("WITH distinct toLower(words[idx]) as word, count(words[idx]) as count ");
			sb.append("RETURN word, count order by count descending");
			JsonObject queryResult = this.getForQuery(sb.toString(), false);
			status.setCode(queryResult.get("status").getAsJsonObject().get("code").getAsInt());
//			ResultJsonObjectArray a = new ResultJsonObjectArray(true);
			status.setMessage(queryResult.get("valueCount").getAsLong() + " tokens created");
			return status;
		}
}