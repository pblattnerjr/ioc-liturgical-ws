package ioc.liturgical.ws.manager.database.external.neo4j.utils;

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

import ioc.liturgical.ws.app.ServiceProvider;
import ioc.liturgical.ws.constants.HTTP_RESPONSE_CODES;
import ioc.liturgical.ws.models.ResultJsonObjectArray;
import net.ages.alwb.utils.core.error.handling.ErrorUtils;

/**
 * Generic means to submit a query to Neo4j through the bolt interface
 * @author mac002
 *
 */
public class QueryToDb {
	private static final Logger logger = LoggerFactory
			.getLogger(QueryToDb.class);

	  private Gson gson = new GsonBuilder().disableHtmlEscaping().create();
	  private JsonParser parser = new JsonParser();
	  private String boltUrl = "bolt://127.0.0.1";
	  private String username = "";
	  private String password = "";
	  private Driver driver = null;
	  private boolean connectionOK = false;

	  public QueryToDb(String username,String password) {
		  this.username = username;
		  this.password = password;
		  initializeDriver();
		  }

	  public QueryToDb(String boltUrl, String username, String password) {
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

	  public JsonObject getForQuery(String query) {
			return getResultObjectForQuery(query, true).toJsonObject();
	}

	  public ResultJsonObjectArray getResultObjectForQuery(String query, boolean splitId) {
			ResultJsonObjectArray result = new ResultJsonObjectArray(true);
			try (org.neo4j.driver.v1.Session session = driver.session()) {
				StatementResult neoResult = session.run(query);
				while (neoResult.hasNext()) {
					org.neo4j.driver.v1.Record record = neoResult.next();
						JsonObject o = parser.parse(gson.toJson(record.asMap())).getAsJsonObject();
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
		  QueryToDb q = new QueryToDb(args[0], args[1], args[2]);
		  System.out.println(q.isConnectionOK());
	  }

	public boolean isConnectionOK() {
		return connectionOK;
	}

	public void setConnectionOK(boolean connectionOK) {
		this.connectionOK = connectionOK;
	}

}
