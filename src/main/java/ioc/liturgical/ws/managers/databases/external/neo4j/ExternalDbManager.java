package ioc.liturgical.ws.managers.databases.external.neo4j;

import java.text.Normalizer;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
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
import ioc.liturgical.ws.constants.HTTP_RESPONSE_CODES;
import ioc.liturgical.ws.constants.NEW_FORM_CLASSES_DB_API;
import ioc.liturgical.ws.constants.RELATIONSHIP_TYPES;
import ioc.liturgical.ws.constants.UTILITIES;
import ioc.liturgical.ws.constants.VERBS;
import ioc.liturgical.ws.constants.db.external.LIBRARIES;
import ioc.liturgical.ws.constants.db.external.SCHEMA_CLASSES;
import ioc.liturgical.ws.constants.db.external.SINGLETON_KEYS;
import ioc.liturgical.ws.constants.db.external.TOPICS;
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
import ioc.liturgical.ws.models.db.docs.nlp.ConcordanceLine;
import ioc.liturgical.ws.models.db.docs.nlp.PerseusAnalyses;
import ioc.liturgical.ws.models.db.docs.nlp.PerseusAnalysis;
import ioc.liturgical.ws.models.db.docs.nlp.WordInflected;
import ioc.liturgical.ws.models.db.docs.ontology.TextLiturgical;
import ioc.liturgical.ws.models.db.docs.tables.ReactBootstrapTableData;
import ioc.liturgical.ws.models.db.forms.TextLiturgicalTranslationCreateForm;
import ioc.liturgical.ws.models.db.links.LinkRefersToBiblicalText;
import ioc.liturgical.ws.models.db.returns.LinkRefersToTextToTextTableRow;
import ioc.liturgical.ws.models.db.returns.ResultNewForms;
import ioc.liturgical.ws.models.db.supers.LTK;
import ioc.liturgical.ws.models.db.supers.LTKDb;
import ioc.liturgical.ws.models.db.supers.LTKDbOntologyEntry;
import ioc.liturgical.ws.models.db.supers.LTKLink;
import ioc.liturgical.ws.models.ws.db.Utility;
import ioc.liturgical.ws.models.ws.response.column.editor.KeyArraysCollection;
import ioc.liturgical.ws.models.ws.response.column.editor.KeyArraysCollectionBuilder;
import ioc.liturgical.ws.models.ws.response.column.editor.LibraryTopicKey;
import ioc.liturgical.ws.models.ws.response.column.editor.LibraryTopicKeyValue;
import net.ages.alwb.utils.core.datastores.json.exceptions.BadIdException;
import net.ages.alwb.utils.core.datastores.json.exceptions.MissingSchemaIdException;
import net.ages.alwb.utils.core.datastores.json.models.DropdownArray;
import net.ages.alwb.utils.core.datastores.json.models.DropdownItem;
import net.ages.alwb.utils.core.datastores.json.models.LTKVJsonObject;
import net.ages.alwb.utils.core.error.handling.ErrorUtils;
import net.ages.alwb.utils.core.generics.MultiMapWithList;
import net.ages.alwb.utils.core.id.managers.IdManager;
import net.ages.alwb.utils.nlp.fetchers.Ox3kUtils;
import net.ages.alwb.utils.nlp.fetchers.PerseusMorph;
import net.ages.alwb.utils.nlp.models.GevLexicon;
import net.ages.alwb.utils.nlp.utils.NlpUtils;
import net.ages.alwb.utils.transformers.adapters.AgesHtmlToTemplateHtml;
import net.ages.alwb.utils.transformers.adapters.models.AgesReactTemplate;
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
	private boolean runningUtility = false;
	private boolean printPretty = true;
	private String runningUtilityName = "";
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
			  if (! this.existsWordAnalyses("ἀβλαβεῖς")) {
				  this.loadTheophanyGrammar();
			  }
		  }
	  }
	  
	  /**
	   * Checks to see if the database contains analyses for the given word
	   * @param word
	   * @return
	   */
	  public boolean existsWordAnalyses(String word) {
		  return getWordAnalyses(word).getResultCount() > 0;
	  }
	  
	  /**
	   * Query the database for word grammar analyses for the specified word
	   * @param word - case sensitive
	   * @return
	   */
	  public ResultJsonObjectArray getWordAnalyses(String word) {
		  String query = "match (n:" + TOPICS.WORD_GRAMMAR.label + ") where n.id starts with \"en_sys_linguistics~"
				+  word.toLowerCase() + "\" return properties(n)"
				 ;
		  ResultJsonObjectArray queryResult = this.getForQuery(
				  query
				  , false
				  , false
				  );
		  List<JsonObject> list = new ArrayList<JsonObject>();
		  JsonArray array = new JsonArray();
		  for (JsonObject obj : queryResult.getValues()) {
			  array.add(obj.get("properties(n)").getAsJsonObject());
		  }
		  JsonObject analyses = new JsonObject();
		  analyses.add(word, array);
		  list.add(analyses);
		  queryResult.setValues(list);
		  return queryResult;
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
				  this.createConstraintUniqueNodeId(TOPICS.ROOT.label);
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
		  relationshipTypesProperties = SCHEMA_CLASSES.relationshipPropertyJson();
		  tagOperatorsDropdown = getTagOperatorsArray();
	  }

	  public void buildOntologyDropdownMaps() {
		  ontologyTypesProperties = SCHEMA_CLASSES.ontologyPropertyJson();
		  ontologyTypesArray = SCHEMA_CLASSES.ontologyTypesJson();
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
				SCHEMA_CLASSES schema = SCHEMA_CLASSES.classForSchemaName(form.get_valueSchemaId());
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

	  /**
	   * @param query
	   * @param setValueSchemas
	   * @param logQuery - if set to true, pays attention to the global logAllQueries and logQueriesWithNoMatches flags
	   * @return
	   */
	  public ResultJsonObjectArray getForQuery(
			  String query
			  , boolean setValueSchemas
			  , boolean logQuery
			  ) {
			ResultJsonObjectArray result = neo4jManager.getResultObjectForQuery(query);
			result.setQuery(query);
			if (logQuery) {
				if (logAllQueries
						|| 
						(logQueriesWithNoMatches && result.getResultCount() == 0) 
						) {
					logger.info(query);
					logger.info("Result count: " + result.getResultCount());
				}
			}
			if (setValueSchemas) {
				result = setValueSchemas(result);
			}
			return result;
	}
	  /**
	   * 
	   * @param label - the node label to use
	   * @param idRegEx  - a regular expression for the ID, e.g. "gr_gr_cog~me.*text"
	   * @return
	   */
	  public ResultJsonObjectArray getWordListWithFrequencyCounts(
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
		  return this.getForQuery(query, false, false);
	  }
	  
		public ResultJsonObjectArray search(
				String type
				, String domain
				, String book
				, String chapter
				, String query
				, String property
				, String matcher
				) {
			ResultJsonObjectArray result = null;
			if (type == null) {
				type = TOPICS.TEXT.label;
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
			result = getForQuery(
					getCypherQueryForDocSearch(
							type
							, domain
							, book
							, chapter
							, query
							, property
							, matcher
							)
					, true
					, true
					);
			return result;
		}

		
		public ResultJsonObjectArray searchOntology(
				String type // to match
				, String genericType // generic type to match
				, String query
				, String property
				, String matcher
				, String tags // tags to match
				, String operator // for tags, e.g. AND, OR
				) {
			ResultJsonObjectArray result = null;

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
					, true
					);
			return result;
		}

		public ResultJsonObjectArray searchRelationships(
				String type // to match
				, String library // library to match
				, String query
				, String property
				, String matcher
				, String tags // tags to match
				, String operator // for tags, e.g. AND, OR
				) {
			ResultJsonObjectArray result = null;

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
			builder.RETURN("id, library, topic, key, value, seq, _valueSchemaId, ");
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
				theGenericType = TOPICS.ROOT.label;
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
				String validation = SCHEMA_CLASSES.validate(json);
				if (validation.length() == 0) {
				try {
						LTKDb record = 
								 gson.fromJson(
										json
										, SCHEMA_CLASSES
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

		/**
		 * Handles both a POST and PUT, i.e. updates a value if exists
		 * and creates it if not.
		 * @param id
		 * @param json
		 * @return
		 * @throws BadIdException
		 * @throws DbException
		 * @throws MissingSchemaIdException
		 */
		public RequestStatus updateValueOfLiturgicalText(
				String requestor
				, String id
				, String json
				) throws BadIdException, DbException {
			RequestStatus result = new RequestStatus();
			IdManager idManager = new IdManager(id);
			RequestStatus status = null;
			if (existsUnique(id)) {
				ResultJsonObjectArray queryResult = this.getForId(id, TOPICS.TEXT_LITURGICAL.label);
				TextLiturgical text = new TextLiturgical(
						idManager.getLibrary()
						, idManager.getTopic()
						, idManager.getKey()
						);
				text = gson.fromJson(queryResult.getFirstObject().toString(), TextLiturgical.class);
				text.setValue(this.parser.parse(json).getAsJsonObject().get("value").getAsString());
				status = this.updateLTKDbObject(requestor, text.toJsonString());
			} else {
				TextLiturgicalTranslationCreateForm text = new TextLiturgicalTranslationCreateForm(
						idManager.getLibrary()
						, idManager.getTopic()
						, idManager.getKey()
						);
				text.setValue(this.parser.parse(json).getAsJsonObject().get("value").getAsString());
				text.setSeq(this.parser.parse(json).getAsJsonObject().get("seq").getAsString());
				status = this.addLTKDbObject(requestor, text.toJsonString());
			}
			result.setCode(status.code);
			result.setMessage(result.getUserMessage());
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
									, SCHEMA_CLASSES
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
								, SCHEMA_CLASSES
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
				ResultJsonObjectArray json = this.getForId(id);
				return json.valueCount == 1;
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
		public ResultJsonObjectArray getForId(String id) {
			ResultJsonObjectArray result  = new ResultJsonObjectArray(true);
			try {
				CypherQueryBuilderForDocs builder = new CypherQueryBuilderForDocs(false)
						.MATCH()
						.LABEL(TOPICS.ROOT.label)
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
			return result;
		}

		public ResultJsonObjectArray getForId(
				String id
				, String label) {
			ResultJsonObjectArray result  = new ResultJsonObjectArray(true);
			try {
				CypherQueryBuilderForDocs builder = new CypherQueryBuilderForDocs(false)
						.MATCH()
						.LABEL(label)
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
			return result;
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
		public ResultJsonObjectArray getForIdStartsWith(String id) {
			CypherQueryBuilderForDocs builder = new CypherQueryBuilderForDocs()
					.MATCH()
					.LABEL(TOPICS.ROOT.label)
					.WHERE("id")
					.STARTS_WITH(id)
					;
			builder.RETURN("id, value, seq");
			builder.ORDER_BY("doc.seq");
			CypherQueryForDocs q = builder.build();
			ResultJsonObjectArray result = getForQuery(q.toString(), true, true);
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
		public ResultJsonObjectArray getContext(
				String id
				, int n
				) {
			ResultJsonObjectArray result = new ResultJsonObjectArray(true);
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
			ResultJsonObjectArray record = this.getForId(id);
			try {
				result = record.getFirstObject().get("seq").getAsString();
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
		public ResultJsonObjectArray getForSeqRange(
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
			ResultJsonObjectArray result = getForQuery(q.toString(), true, true);
			return result;
		}

		/**
		 * Get all records for the specified library and topic
		 * @param library
		 * @param topic
		 * @return
		 */
		public ResultJsonObjectArray getTopic(String library, String topic) {
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
			ResultJsonObjectArray queryResult = null;
			
			if (useSequenceNumbers) {
				queryResult = this.getForSeqRange(library, topic, startingSequence, endingSequence);
			} else {
				queryResult = this.getForIdStartsWith(library + Constants.ID_DELIMITER + topic);
			}
			StringBuffer sb = new StringBuffer();
			for (JsonElement e : queryResult.getValues()) {
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
		
		public ResultJsonObjectArray getTopicAsJson(
				String library
				, String topic
				, int startingSequence  
				, int endingSequence  
				) {
			ResultJsonObjectArray result = new ResultJsonObjectArray(true);
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
			ResultJsonObjectArray queryResult = getTopicAsJson(
					library
					, topic
					, startingSequence  
					, endingSequence  
					);
				for (JsonElement e : queryResult.getValues()) {
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

		/**
		 * Get analyses for the tokens in the test that matches the supplied ID
		 * @param requestor
		 * @param id
		 * @return
		 */
		public ResultJsonObjectArray getWordGrammarAnalyses(
				String requestor
				, String id
			) {
			ResultJsonObjectArray result  = new ResultJsonObjectArray(true);
			try {
				ResultJsonObjectArray queryResult  = getForId(id);
				// add the Text object so we have all its properties
				result.addValue("text", queryResult.getFirstObject());
				
				// get the tokens for the text
				JsonArray theTokens = NlpUtils.getTokensAsJsonArray(
	 	 	  			queryResult.getFirstObjectValueAsString()
	 	 				, false // convertToLowerCase
	 	 				, false // ignorePunctuation
	 	 				, false // ignoreLatin
	 	 				, false // ignoreNumbers
	 	 				, false // removeDiacritics
	 	 	    	);
		 	       
		 	    // add the tokens to the result
		 	    JsonObject tokens = new JsonObject();
				tokens.add("tokens", theTokens);
				result.addValue(tokens);
				
				// add the analyses to the result
				JsonObject analyses = new JsonObject();
				analyses.add(
						"analyses"
						, getWordGrammarAnalyses(
								requestor
								, theTokens
							)
						.getFirstObject()
				);
				result.addValue(analyses);
				result.setQuery("get word grammar analyses for text with id =  " + id);
			} catch (Exception e) {
				result.setStatusCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
				result.setStatusMessage(e.getMessage());
			}
			return result;
		}

		public ResultJsonObjectArray getWordGrammarAnalyses(
				String requestor
				, JsonArray tokens
			) {
			ResultJsonObjectArray result  = new ResultJsonObjectArray(true);
			result.setQuery("get word grammar analyses for tokens");
			JsonObject resultAnalyses = new JsonObject();
			try {
				for (JsonElement e : tokens) {
					ResultJsonObjectArray analyses = this.getWordAnalyses(e.getAsString()); //   getTokenGrammarAnalyses(requestor, e.getAsString());
					if (! resultAnalyses.has(e.getAsString())) { // the same token value can occur more than one time in a text
						resultAnalyses.add(e.getAsString(), analyses.getFirstObject().get(e.getAsString()).getAsJsonArray());
					}
				}
				result.addValue(resultAnalyses);
			} catch (Exception e) {
				result.setStatusCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
				result.setStatusMessage(e.getMessage());
			}
			return result;
		}

		public ResultJsonObjectArray getTokenGrammarAnalyses(
				String requestor
				, String token
			) {
			ResultJsonObjectArray result  = new ResultJsonObjectArray(true);
			try {
				ResultJsonObjectArray queryResult = getForQuery(
						"MATCH (n:WordGrammar) where n.topic = \"" 
								+ token.toLowerCase() 
								+ "\" return n.concise"
								, false
								, false
			  );
				result.setQuery("get grammar analyses for token " + token);
				JsonObject analyses = new JsonObject();
				JsonArray array = new JsonArray();
				for (JsonObject o : queryResult.getValues()) {
					String concise = o.get("n.concise").getAsString();
					array.add(concise);
				}
				analyses.add(token, array);
				result.addValue(analyses);
			} catch (Exception e) {
				result.setStatusCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
				result.setStatusMessage(e.getMessage());
			}
			return result;
		}

		/**
		 * Return the requested doc text as tokens
		 * @param id
		 * @return
		 */
		public ResultJsonObjectArray getTokensForText(String id) {
			ResultJsonObjectArray result  = new ResultJsonObjectArray(true);
			try {
				JsonObject values = new JsonObject();
				ResultJsonObjectArray theText = this.getForId(id);
				
				values.add("tokens", getOntologyTagsForAllTypes());
				JsonObject jsonDropdown = new JsonObject();
				jsonDropdown.add("dropdown", values);

				List<JsonObject> list = new ArrayList<JsonObject>();
				list.add(jsonDropdown);

				result.setResult(list);
				result.setQuery("get word grammar analyses for text with id =  " + id);

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
				Map<String,List<String>> map = SCHEMA_CLASSES.relationshipPropertyMap();
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
				String q = "match (:Root)-[link:" + type + "]->(:Root) return distinct link.tags as " + type;
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

		public JsonObject getOntologyTagsForAllTypes() {
			JsonObject result  = new JsonObject();
			try {
				for (TOPICS t : TOPICS.values()) {
					if (t == TOPICS.ROOT || t == TOPICS.LINGUISTICS_ROOT || t == TOPICS.TABLES_ROOT) {
						// ignore
					} else {
						JsonArray value = getOntologyTags(t.label);
						result.add(t.label, value);
					}
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
				String q = "match (:Text)-[link:" + type + "]->(:Root) return distinct link.library  order by link.library ascending";
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

		// TODO: getRelationshipLibrarysAsDropdownItems runs very slow.  Figure out how to speed it up.
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
		
		public ResultJsonObjectArray getTopicsDropdown() {
			ResultJsonObjectArray result  = new ResultJsonObjectArray(true);
			try {
				String query = "match (n:gr_gr_cog) return distinct n.topic order by n.topic";
				ResultJsonObjectArray queryResult = this.getForQuery(query, false, false);
				DropdownArray array = new DropdownArray();
				for (JsonObject o : queryResult.getValues()) {
					String topic = o.get("n.topic").getAsString();
					array.add(new DropdownItem(topic,topic));
				}
				List<JsonObject> list = new ArrayList<JsonObject>();
				list.add(array.toJsonObject());
				result.setResult(list);
				result.setQuery("get Topics dropdown ");
			} catch (Exception e) {
				result.setStatusCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
				result.setStatusMessage(e.getMessage());
			}
			return result;
		}

		/**
		 * Extracts hierarchically all the elements of an AGES html file,
		 * starting with the content div.  It provides information 
		 * sufficient to render the equivalent HTML using a Javascript library
		 * @param url
		 * @return
		 */
		public ResultJsonObjectArray getAgesTemplateMetadata(
				String url
				) {
			ResultJsonObjectArray result  = new ResultJsonObjectArray(true);
			try {
				AgesHtmlToTemplateHtml ages = new AgesHtmlToTemplateHtml(url, true);
				AgesReactTemplate template = ages.toReactTemplateMetaData();
				List<JsonObject> list = new ArrayList<JsonObject>();
				list.add(template.toJsonObject());
				result.setResult(list);
				result.setQuery("get AGES template metadata for " + url);
			} catch (Exception e) {
				result.setStatusCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
				result.setStatusMessage(e.getMessage());
			}
			return result;
		}
		/**
		 * For the specified library and topic, add values to the collection.
		 * This is used by the client side ParaColTextEditor component
		 * @param collection
		 * @param library
		 * @param topic
		 * @return
		 */
		public Collection<LibraryTopicKeyValue> getLibraryTopicValues(
				Map<String,LibraryTopicKeyValue> keymap
				, String library
				, String topic
				) {
			String query = "match (n:" +library + ") where n.id starts with '" 
					+ library 
					+ Constants.ID_DELIMITER 
					+ topic 
					+ "' return n.key, n.value order by n.key "
					;
			ResultJsonObjectArray queryResult = this.getForQuery(query, false, false);
			int i = 0;
			for (JsonObject o : queryResult.getValues()) {
				String topicKey = topic + Constants.ID_DELIMITER + o.get("n.key").getAsString();
				String value = o.get("n.value").getAsString();
				if (keymap.containsKey(topicKey)) {
					LibraryTopicKeyValue ltkv = keymap.get(topicKey);
					ltkv.setValue(value);
					keymap.put(topicKey, ltkv);
				}
			}
			return keymap.values();
		}
		/**
		 * Returns a json object that has the topic keys for the specified library
		 * and the values for corresponding topic/keys in the specified valuesLibrary
		 * @param library
		 * @param topic
		 * @param valuesLibrary
		 * @return topicKeys, libraryKeys, and values for the specified library and the valuesLibrary
		 */
		public ResultJsonObjectArray getTopicValuesForParaColTextEditor(
				String library
				, String topic
				, String [] valueLibraries
				) {
			ResultJsonObjectArray result  = new ResultJsonObjectArray(true);
			try {
				KeyArraysCollection collection = this.getTopicCollection(library, topic.trim());
				for (String valuesLibrary : valueLibraries) {
					if (! valuesLibrary.equals(library)) {
						Collection<LibraryTopicKeyValue> ltkvCol = getLibraryTopicValues(
								collection.getEmptyLtkvMap()
								, valuesLibrary
								, topic
								);
						collection.addLibraryKeyValues(
								valuesLibrary
								, ltkvCol
								);
					}
				}
				List<JsonObject> list = new ArrayList<JsonObject>();
				list.add(collection.toJsonObject());
				result.setResult(list);
				result.setQuery("get Topic Values for Topic " + topic + " from library " + StringUtils.join(valueLibraries) + " for ParaColTextEditor");
			} catch (Exception e) {
				result.setStatusCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
				result.setStatusMessage(e.getMessage());
			}
			return result;
		}

		/**
		 * Get the records for each id specified in the list of topicKeys from the specified library 
		 * If a record is not found, a LibraryKeyValue will still be created, but with an empty value
		 * @param library
		 * @param topicKeys
		 * @return result array, with values of type LibraryKeyValue
		 */
		public ResultJsonObjectArray getRecordsForIds(String library, List<String> topicKeys) {
			ResultJsonObjectArray result  = new ResultJsonObjectArray(true);
			try {
				List<JsonObject> list = new ArrayList<JsonObject>();
				for (String topicKey : topicKeys) {
					LibraryTopicKeyValue lkv = new LibraryTopicKeyValue(printPretty);
					ResultJsonObjectArray record = this.getForId(library + Constants.ID_DELIMITER + topicKey);
					lkv.set_id(topicKey);
					if (record.status.code == HTTP_RESPONSE_CODES.OK.code && record.valueCount == 1) {
						lkv.setValue(record.getFirstObject().get("value").getAsString());
					}
					list.add(lkv.toJsonObject());
				}
				result.setResult(list);
				result.setQuery("get records for Ids in library " + library);
			} catch (Exception e) {
				result.setStatusCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
				result.setStatusMessage(e.getMessage());
			}
			return result;
		}
		
		public KeyArraysCollection getTopicCollection(
				String library
				, String topic 
				) throws Exception {
			KeyArraysCollectionBuilder b = new KeyArraysCollectionBuilder(
					library,
					topic
					, printPretty
					);
			try {
				String query = "match (n:Liturgical) where n.id starts with '" 
						+ library 
						+ "~" 
						+ topic 
						+ "' return n.key, n.value, n.seq order by n.key"
				;
				ResultJsonObjectArray queryResult = this.getForQuery(query, false, false);
				for (JsonObject o : queryResult.getValues()) {
					String key = o.get("n.key").getAsString();
					String value = o.get("n.value").getAsString();
					String seq = o.get("n.seq").getAsString();
					b.addTemplateKey(topic, key, value, seq);
				}
			} catch (Exception e){
				throw e;
			}
			return b.getCollection();
		}
		public ResultJsonObjectArray getTopicForParaColTextEditor(
				String library
				, String topic
				) {
			ResultJsonObjectArray result  = new ResultJsonObjectArray(true);
			try {
				List<JsonObject> list = new ArrayList<JsonObject>();
				list.add(this.getTopicCollection(library, topic).toJsonObject());
				result.setResult(list);
				result.setQuery("get Topic " + topic + " for ParaColTextEditor");
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
				ResultJsonObjectArray entries = getForQuery("match (n:" + TOPICS.ONTOLOGY_ROOT.label  + ") return count(n)", false, false);
				Long count = entries.getValueCount();
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
				String query  = "match (n:" + TOPICS.ONTOLOGY_ROOT.label + ") where n.id starts with = \"en_sys_ontology~" + type + "~\" return n.id as id, n.name as name";
				ResultJsonObjectArray entries = getForQuery(query, false, false);
				for (JsonElement entry : entries.getValues()) {
					try {
						if (entry.getAsJsonObject().has("name")) { // exclude items with no name
							String value = entry.getAsJsonObject().get("id").getAsString();
							String label = entry.getAsJsonObject().get("name").getAsString();
							result.add(new DropdownItem(label, value).toJsonObject());
						}
					} catch (Exception e) {
						ErrorUtils.report(logger, e);
					}
				}
			return result;
		}

		/**
		 * Get dropdowns of instances of each type of ontology
		 * @return
		 */
		public Map<String, JsonArray> getDropdownsForOntologyInstances() {
			Map<String,JsonArray> result = new TreeMap<String,JsonArray>();
			for (TOPICS t : TOPICS.values()) {
				try {
					JsonArray array = this.getDropdownInstancesForOntologyType(t.label);
					if (array != null && array.size() > 0) {
						result.put(t.label, array);
					}
				} catch (Exception e) {
					ErrorUtils.report(logger, e);
				}
			}
			return result;
		}

		/**
		 * Get a list of users known by the database
		 * @return
		 */
		public ResultJsonObjectArray callDbmsSecurityListUsers() {
			ResultJsonObjectArray result = getForQuery(
					"call dbms.security.listUsers"
					, false
					, false
					);
			return result;
		}

		/**
		 * "values":[{"description":"CONSTRAINT ON ( animal:Animal ) ASSERT animal.id IS UNIQUE"},{"desc...etc
		 * @return
		 */
		public JsonObject callDbConstraints() {
			JsonObject result = new JsonObject();
			ResultJsonObjectArray queryResult = getForQuery(
					"call db.constraints"
					, false
					, false
					);
			if (queryResult.valueCount > 0) {
				result.add("constraints", queryResult.getValuesAsJsonArray());
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
		public ResultJsonObjectArray callDbmsQueryJmx() {
			ResultJsonObjectArray result = getForQuery(
					"call dbms.queryJmx('org.neo4j:*')"
					, false
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
				ResultJsonObjectArray queryResult = getForQuery(
						"call dbms.queryJmx('org.neo4j:instance=kernel#0,name=Configuration')"
						, false
						, false
						);
				result = queryResult.getFirstObject()
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
			result.setOntologyTypesDropdown(TOPICS.keyNamesToDropdown());
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
				if (runningUtility) {
					result.setCode(HTTP_RESPONSE_CODES.UNAUTHORIZED.code);
					result.setMessage("Utility " + this.runningUtilityName + " is still running.");
				} else {
					
					runningUtility = true;
					this.runningUtilityName = utilityName;

					switch (UTILITIES.valueOf(utilityName)) {
					case EngSensesOne: {
						boolean deleteFirst = false;
						result = runUtilityCreateTableForEnglishOaldSenses(requestor, deleteFirst);
						break;
					}
					case FetchPerseusParses: {
						boolean deleteFirst = false;
						result = runUtilityFetchPerseus(requestor, deleteFirst);
						break;
					}
					case Tokenize: {
						result = runUtilityTokenize(requestor, "gr_gr_cog", 0);
						break;
					}	
					default:
						break;
					}
				}
			} else {
				result.setCode(HTTP_RESPONSE_CODES.UNAUTHORIZED.code);
				result.setMessage(HTTP_RESPONSE_CODES.UNAUTHORIZED.message);
			}
			runningUtility = false;
			return result;
		}
		
		/**
		 * Runs the tokenizer for the specified library.
		 * 
		 * The results are stored in the database:
		 * (s:WordInflected)-[r:EXAMPLE]->(e:TextConcordance)
		 * 
		 * If the numberOfConcordanceEntries is set to zero, 
		 * the result is simply:
		 * (s:WordInflected)
		 * 
		 * Note that WordInflected stores the context of the first example it finds,
		 * even if numberOfConcordanceEntries is set to zero.  The difference 
		 * being that it is a property of WordInflected, not a separate TextConcordance
		 * node with an relationship.  This is useful for spotting typos, where the misspelled
		 * word only occurs once.
		 * 
		 *
		 * @param requestor
		 * @param library
		 * @param numberOfConcordanceEntries  zero means don't do it at all, > 0 is a specific number
		 * @return
		 */
		public RequestStatus runUtilityTokenize(
				String requestor
				, String library
				, int numberOfConcordanceEntries
				) {
			RequestStatus status = new RequestStatus();
			
			StringBuffer sb = new StringBuffer();
			sb.append("MATCH (n:Liturgical) where n.id starts with \"");
			sb.append(library);
			sb.append("\" and not n.value contains \"gr_GR_cog\"") ;
			sb.append("RETURN n.id, n.value");
			try {
				ResultJsonObjectArray queryResult = this.getForQuery(
						"MATCH ()-[r:" 
								+ RELATIONSHIP_TYPES.EXAMPLE.typename 
								+ "]->() delete r return count(r)"
								, false
								, false
								);
				queryResult = this.getForQuery("MATCH (n:WordInflected) delete n return count(n)", false, false);
				queryResult = this.getForQuery("MATCH (n:TextConcordance) delete n return count(n)", false, false);
				queryResult = this.getForQuery(sb.toString(), false, false);
				MultiMapWithList<WordInflected, ConcordanceLine> forms = NlpUtils.getWordListWithFrequencies(
						queryResult.getValuesAsJsonArray()
						, true // true = convert to lower case
						, true // true = ignore punctuation
						, true // true = ignore tokens with a Latin character
						, true // true = ignore tokens with a number
						, false // true = remove diacritics
						, numberOfConcordanceEntries    // number of concordance entries
						);
				logger.info("saving tokens and frequency counts to the database");
				int size = forms.size();
				int count = 0;
				int interval = 1000;
				int intervalCounter  = 0;
				for (WordInflected form : forms.getMapSimpleValues()) {
					RequestStatus addStatus = this.addLTKDbObject(requestor, form.toJsonString());
					if (addStatus.getCode() != 201) {
						throw new Exception(addStatus.getUserMessage());
					}
					if (numberOfConcordanceEntries > 0) {
						List<ConcordanceLine> lines = forms.getMapWithLists().get(form.key);
						if (lines != null) {
							for (ConcordanceLine line : lines) {
								addStatus = this.addLTKDbObject(requestor, line.toJsonString());
								if (addStatus.getCode() != 201) {
									throw new Exception(addStatus.getUserMessage());
								}
								this.createRelationship(
										requestor
										, form.getId()
										, RELATIONSHIP_TYPES.EXAMPLE
										, line.getId()
										);
							}
						}
					}
					count++;
					intervalCounter++;
					if (intervalCounter == interval) {
						logger.info("saved " + count + " out of " + size);
						intervalCounter = 0;
					}
				}
				status.setCode(queryResult.getStatus().getCode());
				status.setMessage(forms.size() + " tokens created");
			} catch (Exception e) {
				ErrorUtils.report(logger, e);
				status.setCode(HTTP_RESPONSE_CODES.SERVER_ERROR.code);
				status.setMessage(e.getMessage());
			}
			return status;
		}
		
		public RequestStatus runUtilityCreateTableForEnglishOaldSenses(
				String requestor
				, boolean deleteFirst
				) {
			RequestStatus status = new RequestStatus();
			
			try {
				ResultJsonObjectArray queryResult = null;
				if (deleteFirst) {
					queryResult = this.getForQuery(
							"MATCH (n:WordSenseGev) delete n return count(n)"
							, false
							, false
							);
				}
				GevLexicon lexicon = new GevLexicon(
						Ox3kUtils.DOC_SOURCE.DISK
						, Ox3kUtils.DOC_SOURCE.DISK
						, true // save to disk
						, "/json" // save to this path in the resources folder
						, false // pretty print
				);
				lexicon.load();
				
				ReactBootstrapTableData data = new ReactBootstrapTableData(
						TOPICS.TABLE_LEXICON
						, SINGLETON_KEYS.TABLE_OALD_SENSES.keyname
				);
				data.setData(lexicon.toJsonString());
				RequestStatus addStatus = this.addLTKDbObject(
						requestor
						, data.toJsonString()
						);
				status.setCode(addStatus.code);
				status.setMessage(addStatus.userMessage);
			} catch (Exception e) {
				ErrorUtils.report(logger, e);
				status.setCode(HTTP_RESPONSE_CODES.SERVER_ERROR.code);
				status.setMessage(e.getMessage());
			}
			return status;
		}

		public RequestStatus runUtilityFetchPerseus(
				String requestor
				, boolean deleteFirst
				) {
			RequestStatus status = new RequestStatus();
			
			try {
				ResultJsonObjectArray queryResult = null;
				if (deleteFirst) {
					queryResult = this.getForQuery(
							"MATCH (n:WordGrammar) delete n return count(n)"
							, false
							, false
							);
				}
				queryResult = this.getForQuery(
						"MATCH (n:WordInflected) return n.key"
						, false
						, false
				);
				Long tokenCount = queryResult.getValueCount();
				int count = 0;
			    for (JsonElement e : queryResult.getValuesAsJsonArray()) {
			    	count++;
			    	boolean process = true;
			    	String token = e.getAsJsonObject().get("n.key").getAsString();
			    	if (! deleteFirst) {
			    		ResultJsonObjectArray exists = this.getForQuery(
								"MATCH (n:WordGrammar) where n.topic = \"" 
										+ token 
											+ "\" return n.topic limit 1"
								, false
								, false
						);
						process = ! (exists.getValueCount() > 0);
					}
			    	if (process) {
						logger.info("fetching analyses from Perseus for " + token + " (" + count + " of " + tokenCount + ")");
			    		PerseusMorph pm = new PerseusMorph(token);
			    		PerseusAnalyses analyses = pm.getAnalyses();
			    		for (PerseusAnalysis analysis : analyses.analyses ) {
			    			try {
				    			RequestStatus addStatus = this.addLTKDbObject("wsadmin", analysis.toJsonString());
				    			if (addStatus.getCode() != 201) {
				    				logger.error(token + " already has analysis: " + addStatus.getUserMessage());
				    			}
			    			} catch (Exception eAdd) {
			    				ErrorUtils.report(logger, eAdd);
			    			}
			    		}
			    	}
		    	}
				status.setCode(queryResult.getStatus().getCode());
				status.setMessage(queryResult.getStatus().getUserMessage());
			} catch (Exception e) {
				ErrorUtils.report(logger, e);
				status.setCode(HTTP_RESPONSE_CODES.SERVER_ERROR.code);
				status.setMessage(e.getMessage());
			}
			return status;
		}

		/**
		 * Create a relationship between the two specified nodes
		 * @param requestor
		 * @param idOfStartNode
		 * @param relationshipType
		 * @param idOfEndNode
		 * @return
		 */
		public RequestStatus createRelationship(
				String requestor,
				String idOfStartNode
				, RELATIONSHIP_TYPES relationshipType
				, String idOfEndNode
				) {
			RequestStatus status = new RequestStatus();
			StringBuffer sb = new StringBuffer();
			try {
				sb.append("MATCH (s:Root {id: \"");
				sb.append(idOfStartNode);
				sb.append("\"}), (e:Root {id: \"");
				sb.append(idOfEndNode);
				sb.append("\"}) merge (s)-[:");
				sb.append(relationshipType.typename);
				sb.append("]->(e)");
				ResultJsonObjectArray queryResult = this.getForQuery(sb.toString(),false, false);
				status.setCode(queryResult.getStatus().getCode());
				status.setMessage(queryResult.getStatus().getUserMessage());
			} catch (Exception e) {
				ErrorUtils.report(logger, e);
				status.setCode(HTTP_RESPONSE_CODES.SERVER_ERROR.code);
				status.setMessage(e.getMessage());
			}
			return status;
		}

}