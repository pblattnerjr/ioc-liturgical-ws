package net.ages.alwb.utils.transformers;


	import ioc.liturgical.ws.managers.databases.external.neo4j.utils.Neo4jConnectionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.ocmc.ioc.liturgical.schemas.models.ws.response.ResultJsonObjectArray;
import org.ocmc.ioc.liturgical.utils.FileUtils;

	
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

	/**
	 * Reads the database for system labels and creates a json representation.
	 * Also creates a javascript enum file for the label topics.
	 * @author mac002
	 *
	 */
	public class NeoHeirmologion {
		private Neo4jConnectionManager dbManager = null;
		
		public NeoHeirmologion(
				String url
				, String uid
				, String pwd
				) {
			this.dbManager = new Neo4jConnectionManager(
					  url
					  , uid
					  , pwd
					  , false
					  );
		}
		
		private void process(String out) {
			String query = "match (n:Liturgical) where n.library = 'en_us_dedes' and n.topic starts with 'he.' and n.key ends with '.text' return n.id, n.library, n.topic, n.key, n.value order by n.seq";
			ResultJsonObjectArray queryResult = dbManager.getResultObjectForQuery(query);
			for (JsonObject json : queryResult.values) {
				String id = json.get("n.id").getAsString();
				String library = json.get("n.library").getAsString();
				String topic = json.get("n.topic").getAsString();
				String key = json.get("n.key").getAsString();
				String value = json.get("n.value").getAsString().trim();
				if (value == null || value.length() == 0) {
					String q2 = "match (n:Liturgical) where n.library starts with 'en_' and n.id ends with '" + topic + "~" + key + "'  return n.id, n.library, n.topic, n.key, n.value order by n.seq";
					ResultJsonObjectArray q2Result = dbManager.getResultObjectForQuery(q2);
					for (JsonObject j2 : q2Result.values) {
						String j2Value = j2.get("n.value").getAsString().trim();
						if (j2Value != null && j2Value.length() > 0) {
							id = j2.get("n.id").getAsString();
							library = j2.get("n.library").getAsString();
							topic = j2.get("n.topic").getAsString();
							key = j2.get("n.key").getAsString();
							value = j2Value;
							break;
						}
					}
				}
				if (value != null && value.length() > 0) {
					String newId = id.substring(0, id.length() - 4);
					String q3 = "match (n:Liturgical) where n.id starts with '" + newId + "'  return n.id, n.library, n.topic, n.key, n.value order by n.seq";
					ResultJsonObjectArray q3Result = dbManager.getResultObjectForQuery(q3);
					String mode = "";
					String notMetered = "";
					String name = "";
					for (JsonObject j3 : q3Result.values) {
						String j3Key = j3.get("n.key").getAsString();
						if (j3Key.endsWith(".mode")) {
							mode = j3.get("n.value").getAsString();
						} else if (j3Key.endsWith(".notmetered")) {
							notMetered = j3.get("n.value").getAsString();
						} else if (j3Key.endsWith(".name")) {
							name = j3.get("n.value").getAsString();
						}
					}
					System.out.println(mode);
					System.out.println(notMetered);
					System.out.println(name);
					System.out.println(id + " " + value);
				}
			}
		}
		
		public static void main(String[] args) {
			String path = "";
			String uid = System.getenv("user");
			String pwd = System.getenv("pwd");
			String url= "localhost"; // "159.203.89.233";
			NeoHeirmologion doIt = new NeoHeirmologion(url, uid, pwd);
			doIt.process(path);
		}
	}
