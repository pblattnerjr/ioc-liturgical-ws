package ioc.liturgical.ws.managers.databases.external.neo4j.utils;

import java.util.ArrayList;
import java.util.HashMap;
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
import ioc.liturgical.ws.managers.exceptions.DbException;
import ioc.liturgical.ws.managers.interfaces.LowLevelDataStoreInterface;
import ioc.liturgical.ws.models.RequestStatus;
import ioc.liturgical.ws.models.ResultJsonObjectArray;
import net.ages.alwb.utils.core.datastores.json.models.LTKVJsonObject;
import net.ages.alwb.utils.core.error.handling.ErrorUtils;

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

	  public Neo4jConnectionManager(String username,String password) {
		  this.username = username;
		  this.password = password;
		  initializeDriver();
		  }

	  public Neo4jConnectionManager(String boltUrl, String username, String password) {
		  this.boltUrl = "bolt://"+boltUrl;
		  this.username = username;
		  this.password = password;
		  initializeDriver();
	  }
	  
	  private void initializeDriver() {
		  logger.info("Using " + boltUrl + " for external database...");
		  driver = GraphDatabase.driver(boltUrl, AuthTokens.basic(username, password));
		  testConnection();
	  }
	  

	  private void testConnection() {
		  ResultJsonObjectArray result = getResultObjectForQuery("match (n) return count(n)", false);

		  if (result.getValueCount() > 0) {
			  logger.info("Connection to Neo4j database is OK.");
			  this.connectionOK = true;
		  } else {
			  this.connectionOK = false;
			  logger.error("Can't connect to the Neo4j database.");
			  logger.error(result.getStatus().getUserMessage());
		  }
	  }

	public ResultJsonObjectArray getForQuery(String query) {
			return getResultObjectForQuery(query, true);
	}

	  public ResultJsonObjectArray getResultObjectForQuery(
			  String query
			  , boolean splitId
			  ) {
			ResultJsonObjectArray result = new ResultJsonObjectArray(true);
			try (org.neo4j.driver.v1.Session session = driver.session()) {
				StatementResult neoResult = session.run(query);
				while (neoResult.hasNext()) {
					org.neo4j.driver.v1.Record record = neoResult.next();
						JsonObject o = parser.parse(gson.toJson(record.asMap())).getAsJsonObject();
						if (o.has("value")) {
							try {
								String strValue = o.get("value").getAsString();
								JsonObject objValue = parser.parse(strValue).getAsJsonObject();
								o.add("value", objValue);
							} catch (Exception e) {
								// move on.  Forget it...
							}
						}
						if (splitId && record.containsKey("doc.id")) {
							String [] idParts = record.get("doc.id").asString().split("~");
							if (idParts.length == 3) {
								o.addProperty("doc.domain", idParts[0]);
								o.addProperty("doc.topic", idParts[1]);
								o.addProperty("doc.key", idParts[2]);
							}
						}
						result.addValue(o);
				}
				session.close();
			} catch (Exception e) {
				result.setStatusCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
				result.setStatusMessage(e.getMessage());
			}
			return result;
	}

	  protected void finalize() throws Throwable {
		  super.finalize();
		  driver.close();
		}
	  
	  public static void main(String [] args) {
		  Neo4jConnectionManager q = new Neo4jConnectionManager(args[0], args[1], args[2]);
		  System.out.println(q.isConnectionOK());
	  }

	public boolean isConnectionOK() {
		return connectionOK;
	}

	public void setConnectionOK(boolean connectionOK) {
		this.connectionOK = connectionOK;
	}

	private String getLabelForSchema(String schema) {
		String result = schema;
		String [] parts = schema.split(":");
		if (parts.length == 2) {
			result = parts[0];
		}
		return result;
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
		setIdConstraint(doc.getSchemaAsLabel());
		String query = "create (n:" + doc.getDelimitedLabels(":") + ") set n = {props} return n";
		try (org.neo4j.driver.v1.Session session = driver.session()) {
			StatementResult neoResult = session.run(query, doc.getAsPropertiesMap());
			int count = neoResult.consume().counters().nodesCreated();
	    	result.setCode(HTTP_RESPONSE_CODES.CREATED.code);
	    	result.setMessage(HTTP_RESPONSE_CODES.CREATED.message + ": created " + doc.get_id());
		} catch (Exception e){
			result.setCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
			result.setMessage(HTTP_RESPONSE_CODES.BAD_REQUEST.message);
			result.setDeveloperMessage(e.getMessage());
		}
		return result;
	}

	@Override
	public void insert(List<JsonObject> docs) throws DbException {
		// TODO Auto-generated method stub
		for (JsonObject doc : docs) {
			System.out.println(doc.toString());
		}
	}

	@Override
	public RequestStatus updateWhereEqual(LTKVJsonObject doc) throws DbException {
		RequestStatus result = new RequestStatus();
		setIdConstraint(doc.getSchemaAsLabel());
		String query = 
				"match (n) where n.id = \"" 
				+ doc.get_id() 
		        + "\" set n = {props} return count(n)";
		try (org.neo4j.driver.v1.Session session = driver.session()) {
			StatementResult neoResult = session.run(query, doc.getAsPropertiesMap());
			int count = neoResult.consume().counters().nodesCreated();
	    	result.setCode(HTTP_RESPONSE_CODES.OK.code);
	    	result.setMessage(HTTP_RESPONSE_CODES.OK.message + ": updated " + doc.get_id());
		} catch (Exception e){
			result.setCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
			result.setMessage(HTTP_RESPONSE_CODES.BAD_REQUEST.message);
			result.setDeveloperMessage(e.getMessage());
		}
		return result;
	}

	@Override
	public void insert(JsonObject doc) throws DbException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public RequestStatus deleteWhereEqual(String id) throws DbException {
		RequestStatus result = new RequestStatus();
		String query = 
				"match (n) where n.id = \"" 
				+ id 
		        + "\" delete n";
		try (org.neo4j.driver.v1.Session session = driver.session()) {
			StatementResult neoResult = session.run(query);
	    	result.setCode(HTTP_RESPONSE_CODES.OK.code);
	    	result.setMessage(HTTP_RESPONSE_CODES.OK.message + ": deleted " + id);
		} catch (Exception e){
			result.setCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
			result.setMessage(HTTP_RESPONSE_CODES.BAD_REQUEST.message);
			result.setDeveloperMessage(e.getMessage());
		}
		return result;
	}

}
