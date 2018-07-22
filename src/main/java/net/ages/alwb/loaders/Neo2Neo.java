package net.ages.alwb.loaders;


	import ioc.liturgical.ws.managers.databases.external.neo4j.utils.Neo4jConnectionManager;
import ioc.liturgical.ws.managers.exceptions.DbException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
	import java.util.TreeMap;
	import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.ocmc.ioc.liturgical.schemas.constants.STATUS;
import org.ocmc.ioc.liturgical.schemas.constants.VISIBILITY;
import org.ocmc.ioc.liturgical.schemas.id.managers.IdManager;
import org.ocmc.ioc.liturgical.schemas.models.labels.UiLabel;
import org.ocmc.ioc.liturgical.schemas.models.ws.response.ResultJsonObjectArray;
import org.ocmc.ioc.liturgical.utils.FileUtils;
	
	import com.google.gson.Gson;
	import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
	import com.google.gson.JsonObject;
	import com.google.gson.JsonParser;

	/**
	 * A utility to copy records from one Neo4j database to another
	 * @author mac002
	 *
	 */
	public class Neo2Neo {
		private Neo4jConnectionManager fromDbManager = null;
		private Neo4jConnectionManager toDbManager = null;
		
		public Neo2Neo(
				String fromUrl
				, String toUrl
				, String uid
				, String pwd
				, String queryWhereClause
				, String label
				) {
			this.process(fromUrl, toUrl, uid, pwd, queryWhereClause, label);
		}
		
		private void process(
				String fromUrl
				, String toUrl
				, String uid
				, String pwd
				, String queryWhereClause
				, String label
				) {
			  this.fromDbManager = new Neo4jConnectionManager(
					  fromUrl
					  , uid
					  , pwd
					  , false
					  );
			  
			  this.toDbManager = new Neo4jConnectionManager(
					  toUrl
					  , uid
					  , pwd
					  , false
					  );

			   String query = "match (n:" + label + ") " + queryWhereClause + " return properties(n) as properties, labels(n) as labels";
				 ResultJsonObjectArray queryResults = fromDbManager.getForQuery(query);
				 long size = queryResults.getValueCount();
				 long count = 1;
				 System.out.println("Match count = " + size);
				 for (JsonObject value : queryResults.getValues()) {
					 JsonObject doc = new JsonObject();
					 doc.add("doc", value);
					 try {
						String id = value.get("properties").getAsJsonObject().get("id").getAsString();
						System.out.println("Sending " + count + " of " + size + " - " + id);
						count++;
						this.toDbManager.insert(doc);
					} catch (DbException e) {
						e.printStackTrace();
					}
				 }

		}

		public static void main(String[] args) {
			String uid = System.getenv("uid");
			String pwd = System.getenv("pwd");
			String fromUrl= "localhost"; 
			String toUrl = "159.203.89.233";
			String queryWhereClause = ""; // 
			String label = "UiLabel";
			Neo2Neo labels2Neo = new Neo2Neo(fromUrl, toUrl, uid, pwd, queryWhereClause, label);
		}
	}
