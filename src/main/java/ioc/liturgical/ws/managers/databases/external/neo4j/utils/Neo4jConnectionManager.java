package ioc.liturgical.ws.managers.databases.external.neo4j.utils;

import java.util.List;
import java.util.Map;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.StatementResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import ioc.liturgical.ws.constants.HTTP_RESPONSE_CODES;
import ioc.liturgical.ws.constants.RELATIONSHIP_TYPES;
import ioc.liturgical.ws.managers.exceptions.DbException;
import ioc.liturgical.ws.managers.interfaces.LowLevelDataStoreInterface;
import net.ages.alwb.utils.core.datastores.json.models.ModelHelpers;
import ioc.liturgical.ws.models.RequestStatus;
import ioc.liturgical.ws.models.ResultJsonObjectArray;
import ioc.liturgical.ws.models.db.stats.QueryStatistics;
import ioc.liturgical.ws.models.db.supers.LTKDb;
import ioc.liturgical.ws.models.db.supers.LTKDbOntologyEntry;
import net.ages.alwb.utils.core.datastores.json.models.LTKVJsonObject;
import net.ages.alwb.utils.core.error.handling.ErrorUtils;
import net.ages.alwb.utils.core.misc.Constants;

/**
 * Methods for interacting with a Neo4j database through the bolt interface
 * @author mac002
 *
 */
public class Neo4jConnectionManager implements LowLevelDataStoreInterface {
	private static final Logger logger = LoggerFactory
			.getLogger(Neo4jConnectionManager.class);

	  private Gson gson = new GsonBuilder().disableHtmlEscaping().create();
	  private JsonParser parser = new JsonParser();
	  private String boltUrl = "bolt://127.0.0.1";
	  private String username = "";
	  private String password = "";
	  private Driver driver = null;
	  private boolean connectionOK = false;
	  private boolean readOnly = false;
	  private static String doc = "doc.";
	  private static String id = "id";

	  public Neo4jConnectionManager(
			  String username
			  ,String password
			  , boolean readOnly
			  ) {
		  this.username = username;
		  this.password = password;
		  this.readOnly  = readOnly;
		  initializeDriver();
		  }

	  public Neo4jConnectionManager(
			  String boltUrl
			  , String username
			  , String password
			  , boolean readOnly
			  ) {
		  this.boltUrl = "bolt://"+boltUrl;
		  this.username = username;
		  this.password = password;
		  this.readOnly  = readOnly;
		  initializeDriver();
	  }
	  
	  private void initializeDriver() {
		  logger.info("Using " + boltUrl + " for external database...");
		  driver = GraphDatabase.driver(boltUrl, AuthTokens.basic(username, password));
		  testConnection();
	  }
	  

	  private ResultJsonObjectArray testConnection() {
		  ResultJsonObjectArray result = getResultObjectForQuery("match (n) return count(n) limit 10");
		  if (result.getValueCount() > 0) {
			  logger.info("Connection to Neo4j database is OK.");
			  this.connectionOK = true;
		  } else {
			  this.connectionOK = false;
			  logger.error("Can't connect to the Neo4j database.");
			  logger.error(result.getStatus().getUserMessage());
		  }
		  return result;
	  }
	  

	public ResultJsonObjectArray getForQuery(String query) {
			return getResultObjectForQuery(query);
	}

	/**
	 * If the result of the query contains records with an id and value,
	 * they will be converted to either LTKVString or LTKVJsonObject
	 * depending on whether the value is simple or complex.
	 * 
	 * Otherwise, it will return a JsonObject based on the record map.
	 * 
	 * @param query
	 * @return
	 */
	  public ResultJsonObjectArray getResultObjectForQuery(
			  String query
			  ) {
			ResultJsonObjectArray result = new ResultJsonObjectArray(true);
			try (org.neo4j.driver.v1.Session session = driver.session()) {
				StatementResult neoResult = session.run(query);
				while (neoResult.hasNext()) {
					org.neo4j.driver.v1.Record record = neoResult.next();
						JsonObject o = parser.parse(gson.toJson(record.asMap())).getAsJsonObject();
						if (o.has("properties(link)")) {
							o = parser.parse(gson.toJson(record.get("properties(link)").asMap())).getAsJsonObject();
						} else if (o.has("properties(doc)")) {
							o = parser.parse(gson.toJson(record.get("properties(doc)").asMap())).getAsJsonObject();
						}
						result.addValue(o);
				}
			} catch (Exception e) {
				result.setStatusCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
				result.setStatusMessage(e.getMessage());
			}
			recordQuery(query, result.getStatus().getCode(), result.getCount());
			return result;
	}

	  /**
	   * Record in the database the results of running a query.
	   * The query can be a create, read, update, or delete.
	   * 
	   * To view the queries that have been recorded, open the Neo4j browser
	   * and run this query:
	   * 
	   * match (n:stats) 
	   * with n.dayOfYear as day 
	   * return distinct day, count(day)
	   * order by day
	   * 
	   * @param query
	   * @param statusCode
	   * @param resultCount
	   */
	 public void recordQuery(String query, int statusCode, long resultCount) {
		 try {
			  QueryStatistics stats = new QueryStatistics(
					  Constants.STATS_LIBRARY
					  , Constants.STATS_TOPIC
					  , query
					  , statusCode
					  , resultCount);
			  recordQuery(stats);
		 } catch (Exception e) {
			 ErrorUtils.report(logger, e);
		 }
	  }
	  
	  protected void finalize() throws Throwable {
		  super.finalize();
		  driver.close();
		}
	  
	  public static void main(String [] args) {
		  Neo4jConnectionManager q = new Neo4jConnectionManager(args[0], args[1], args[2], false);
		  System.out.println(q.isConnectionOK());
	  }

	public boolean isConnectionOK() {
		return connectionOK;
	}

	public void setConnectionOK(boolean connectionOK) {
		this.connectionOK = connectionOK;
	}
	
	/**
	 * The purpose of this method is to ensure that any node with 
	 * an id property has a unique constraint so that duplicate IDs 
	 * are not allowed.
	 * @param label
	 * @return
	 */
	private StatementResult setIdConstraint(String label) {
		StatementResult neoResult = null;
		String query = "create constraint on (p:" + label + ") assert p.id is unique"; 
		try (org.neo4j.driver.v1.Session session = driver.session()) {
			neoResult = session.run(query);
		} catch (Exception e) {
			ErrorUtils.report(logger, e);
		}
		return neoResult;
	}
	
	@Override
	public RequestStatus insert(LTKVJsonObject doc) throws DbException {
		RequestStatus result = new RequestStatus();
		int count = 0;
		setIdConstraint(doc.getSchemaAsLabel());
		String query = "create (n:" + doc.getDelimitedLabels(":") + ") set n = {props} return n";
		try (org.neo4j.driver.v1.Session session = driver.session()) {
			Map<String,Object> props = ModelHelpers.getAsPropertiesMap(doc);
			StatementResult neoResult = session.run(query, props);
			count = neoResult.consume().counters().nodesCreated();
			if (count > 0) {
		    	result.setCode(HTTP_RESPONSE_CODES.CREATED.code);
		    	result.setMessage(HTTP_RESPONSE_CODES.CREATED.message + ": created " + doc.get_id());
			} else {
		    	result.setCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
		    	result.setMessage(HTTP_RESPONSE_CODES.BAD_REQUEST.message + "  " + doc.get_id());
			}
		} catch (Exception e){
			result.setCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
			result.setMessage(HTTP_RESPONSE_CODES.BAD_REQUEST.message);
			result.setDeveloperMessage(e.getMessage());
		}
    	recordQuery(query, result.getCode(), count);
		return result;
	}

	public RequestStatus insert(LTKDb doc) throws DbException {
		RequestStatus result = new RequestStatus();
		int count = 0;
		setIdConstraint(doc.toSchemaAsLabel());
		String query = "create (n:" + doc.fetchOntologyLabels() + ") set n = {props} return n";
		try (org.neo4j.driver.v1.Session session = driver.session()) {
			Map<String,Object> props = ModelHelpers.getAsPropertiesMap(doc);
			StatementResult neoResult = session.run(query, props);
			count = neoResult.consume().counters().nodesCreated();
			if (count > 0) {
		    	result.setCode(HTTP_RESPONSE_CODES.CREATED.code);
		    	result.setMessage(HTTP_RESPONSE_CODES.CREATED.message + ": created " + doc.getId());
			} else {
		    	result.setCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
		    	result.setMessage(HTTP_RESPONSE_CODES.BAD_REQUEST.message + "  " + doc.getId());
			}
		} catch (Exception e){
			if (e.getMessage().contains("already exists")) {
				result.setCode(HTTP_RESPONSE_CODES.CONFLICT.code);
				result.setDeveloperMessage(HTTP_RESPONSE_CODES.CONFLICT.message);
			} else {
				result.setCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
				result.setDeveloperMessage(HTTP_RESPONSE_CODES.BAD_REQUEST.message);
			}
			result.setUserMessage(e.getMessage());
		}
    	recordQuery(query, result.getCode(), count);
		return result;
	}

	@Override
	public void insert(List<JsonObject> docs) throws DbException {
		// TODO Need to implement
		for (JsonObject doc : docs) {
			System.out.println(doc.toString());
		}
	}

	/**
	 * @see recordQuery(String, int, long)
	 * @param doc
	 * @throws DbException
	 */
	public void recordQuery(QueryStatistics doc) throws DbException {
		if (! readOnly) {
			String query = "create (n:stats) set n = {props} return n";
			try (org.neo4j.driver.v1.Session session = driver.session()) {
				StatementResult neoResult = session.run(
						query
						, ModelHelpers.getAsPropertiesMap(doc)
						);
			} catch (Exception e){
				// ignore
			}
		}
	}


	public RequestStatus createRelationship(
			String fromId
			, LTKDb doc
			, String toId
			, RELATIONSHIP_TYPES type
			) throws DbException {
		RequestStatus result = new RequestStatus();
		setIdConstraint(doc.toSchemaAsLabel());
		String matchFrom = "MATCH (f) where f.id = \'" 
				+ fromId 
				+ "\' match (t) where t.id = \'" 
				+ toId 
				+ "\'"
				;
        String queryCreate = " CREATE (f)-[r:" 
        		+ type.typename 
        		+ "]->(t) set r = {props} return r";
		String query = matchFrom + queryCreate;
		int count = 0;
		try (org.neo4j.driver.v1.Session session = driver.session()) {
			Map<String,Object> props = ModelHelpers.getAsPropertiesMap(doc);
			StatementResult neoResult = session.run(query, props);
			count = neoResult.consume().counters().relationshipsCreated();
			if (count == 0) {
		    	result.setCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
		    	result.setMessage(HTTP_RESPONSE_CODES.BAD_REQUEST.message + "   " + doc.getId());
			} else {
		    	result.setCode(HTTP_RESPONSE_CODES.CREATED.code);
		    	result.setMessage(HTTP_RESPONSE_CODES.CREATED.message + ": created " + doc.getId());
			}
		} catch (Exception e){
			result.setCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
			result.setMessage(HTTP_RESPONSE_CODES.BAD_REQUEST.message);
			result.setDeveloperMessage(e.getMessage());
		}
		recordQuery(query, result.getCode(), count);
		return result;
	}

	@Override
	public RequestStatus updateWhereEqual(LTKVJsonObject doc) throws DbException {
		RequestStatus result = new RequestStatus();
		int count = 0;
		setIdConstraint(doc.getSchemaAsLabel());
		String query = 
				"match (n) where n.id = \"" 
				+ doc.get_id() 
		        + "\" set n = {props} return count(n)";
		try (org.neo4j.driver.v1.Session session = driver.session()) {
			Map<String,Object> props = ModelHelpers.getAsPropertiesMap(doc);
			StatementResult neoResult = session.run(query, props);
			count = neoResult.consume().counters().propertiesSet();
			if (count > 0) {
		    	result.setCode(HTTP_RESPONSE_CODES.OK.code);
		    	result.setMessage(HTTP_RESPONSE_CODES.OK.message + ": updated " + doc.get_id());
			} else {
		    	result.setCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
		    	result.setMessage(HTTP_RESPONSE_CODES.BAD_REQUEST.message + " " + doc.get_id());
			}
		} catch (Exception e){
			result.setCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
			result.setMessage(HTTP_RESPONSE_CODES.BAD_REQUEST.message);
			result.setDeveloperMessage(e.getMessage());
		}
    	recordQuery(query, result.getCode(), count);
		return result;
	}

	public RequestStatus updateWhereEqual(LTKDb doc) throws DbException {
		RequestStatus result = new RequestStatus();
		int count = 0;
		setIdConstraint(doc.toSchemaAsLabel());
		String query = 
				"match (n) where n.id = \"" 
				+ doc.getId() 
		        + "\" set n = {props} return count(n)";
		try (org.neo4j.driver.v1.Session session = driver.session()) {
			Map<String,Object> props = ModelHelpers.getAsPropertiesMap(doc);
			StatementResult neoResult = session.run(query, props);
			count = neoResult.consume().counters().propertiesSet();
			if (count > 0) {
		    	result.setCode(HTTP_RESPONSE_CODES.OK.code);
		    	result.setMessage(HTTP_RESPONSE_CODES.OK.message + ": updated " + doc.getId());
			} else {
		    	result.setCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
		    	result.setMessage(HTTP_RESPONSE_CODES.BAD_REQUEST.message + " " + doc.getId());
			}
		} catch (Exception e){
			result.setCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
			result.setMessage(HTTP_RESPONSE_CODES.BAD_REQUEST.message);
			result.setDeveloperMessage(e.getMessage());
		}
    	recordQuery(query, result.getCode(), count);
		return result;
	}

	public RequestStatus updateWhereRelationshipEqual(LTKDb doc) throws DbException {
		RequestStatus result = new RequestStatus();
		int count = 0;
		setIdConstraint(doc.toSchemaAsLabel());
		String query = 
				"match ()-[r]->() where r.id = \"" 
				+ doc.getId() 
		        + "\" set r = {props} return count(r)";
		try (org.neo4j.driver.v1.Session session = driver.session()) {
			Map<String,Object> props = ModelHelpers.getAsPropertiesMap(doc);
			StatementResult neoResult = session.run(query, props);
			count = neoResult.consume().counters().propertiesSet();
			if (count > 0) {
		    	result.setCode(HTTP_RESPONSE_CODES.OK.code);
		    	result.setMessage(HTTP_RESPONSE_CODES.OK.message + ": updated " + doc.getId());
			} else {
		    	result.setCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
		    	result.setMessage(HTTP_RESPONSE_CODES.BAD_REQUEST.message + " " + doc.getId());
			}
		} catch (Exception e){
			result.setCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
			result.setMessage(HTTP_RESPONSE_CODES.BAD_REQUEST.message);
			result.setDeveloperMessage(e.getMessage());
		}
    	recordQuery(query, result.getCode(), count);
		return result;
	}

	@Override
	public void insert(JsonObject doc) throws DbException {
		// TODO need to implement?
	}

	@Override
	public RequestStatus deleteNodeWhereEqual(String id) throws DbException {
		RequestStatus result = new RequestStatus();
		int count = 0;
		String query = 
				"match (n) where n.id = \"" 
				+ id 
		        + "\" delete n";
		try (org.neo4j.driver.v1.Session session = driver.session()) {
			StatementResult neoResult = session.run(query);
			count = neoResult.consume().counters().nodesDeleted();
			if (count > 0) {
		    	result.setCode(HTTP_RESPONSE_CODES.OK.code);
		    	result.setMessage(HTTP_RESPONSE_CODES.OK.message + ": deleted " + id);
			} else {
		    	result.setCode(HTTP_RESPONSE_CODES.NOT_FOUND.code);
		    	result.setMessage(HTTP_RESPONSE_CODES.NOT_FOUND.message + " " + id);
			}
		} catch (Exception e){
			result.setCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
			result.setMessage(HTTP_RESPONSE_CODES.BAD_REQUEST.message);
			result.setDeveloperMessage(e.getMessage());
		}
		recordQuery(query, result.getCode(), count);
		return result;
	}

	public RequestStatus deleteRelationshipWhereEqual(String id) throws DbException {
		RequestStatus result = new RequestStatus();
		int count = 0;
		String query = 
				"match ()-[r]->() where r.id = \"" 
				+ id 
		        + "\" delete r";
		try (org.neo4j.driver.v1.Session session = driver.session()) {
			StatementResult neoResult = session.run(query);
			count = neoResult.consume().counters().relationshipsDeleted();
			if (count > 0) {
		    	result.setCode(HTTP_RESPONSE_CODES.OK.code);
		    	result.setMessage(HTTP_RESPONSE_CODES.OK.message + ": deleted " + id);
			} else {
		    	result.setCode(HTTP_RESPONSE_CODES.NOT_FOUND.code);
		    	result.setMessage(HTTP_RESPONSE_CODES.NOT_FOUND.message + " " + id);
			}
		} catch (Exception e){
			result.setCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
			result.setMessage(HTTP_RESPONSE_CODES.BAD_REQUEST.message);
			result.setDeveloperMessage(e.getMessage());
		}
		recordQuery(query, result.getCode(), count);
		return result;
	}
	
	public RequestStatus processConstraintQuery(String query) {
		RequestStatus result = new RequestStatus();
		try (org.neo4j.driver.v1.Session session = driver.session()) {
			StatementResult neoResult = session.run(query);
			int count = 0;
			if (query.toLowerCase().contains("create constraint")) {
				count = neoResult.consume().counters().constraintsAdded();
			} else {
				count = neoResult.consume().counters().constraintsRemoved();
			}
			if (count < 1) {
		    	result.setCode(HTTP_RESPONSE_CODES.CONFLICT.code);
		    	result.setMessage(HTTP_RESPONSE_CODES.CONFLICT.message);
			}
		} catch (Exception e){
			result.setCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
			result.setMessage(HTTP_RESPONSE_CODES.BAD_REQUEST.message);
			result.setDeveloperMessage(e.getMessage());
		}
		logger.info(query + ": " + result.getCode());
		return result;
	}

}
