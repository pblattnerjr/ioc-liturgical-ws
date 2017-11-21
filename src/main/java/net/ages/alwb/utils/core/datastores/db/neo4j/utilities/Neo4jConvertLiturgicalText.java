package net.ages.alwb.utils.core.datastores.db.neo4j.utilities;

import com.google.gson.JsonObject;

import ioc.liturgical.ws.managers.databases.external.neo4j.utils.Neo4jConnectionManager;
import ioc.liturgical.ws.managers.exceptions.DbException;
import ioc.liturgical.ws.models.ResultJsonObjectArray;
import ioc.liturgical.ws.models.db.docs.ontology.TextBiblical;
import ioc.liturgical.ws.models.db.docs.ontology.TextLiturgical;
import net.ages.alwb.utils.core.misc.AlwbGeneralUtils;

/**
 * Converts the original Liturgical text schema to the new one
 * @author mac002
 *
 */
public class Neo4jConvertLiturgicalText {
	public static void main(String[] args) {
		Neo4jConnectionManager m = null;
		String uid = System.getenv("uid");
		String pwd = System.getenv("pwd");
		  m = new Neo4jConnectionManager(
				  "localhost"
				  , uid
				  , pwd
				  , false
				  );
		  String query = "match (n:Liturgical) return properties(n)";
		  ResultJsonObjectArray s = m.getForQuery(query);
		  System.out.println(s.getStatus().getCode() + s.getValueCount());
		  int toProcess = Integer.valueOf(String.valueOf(s.getResultCount()));
		  int block = (int) (toProcess * .1);
		  int processed = 0;
		  int blockCounter = 0;
		  for (JsonObject o : s.getValues()) {
			  try {
				  JsonObject p = o.get("properties(n)").getAsJsonObject();
				  TextLiturgical t = new TextLiturgical(
						  p.get("library").getAsString()
						  , p.get("topic").getAsString()
						  , p.get("key").getAsString()
						  );
				  t.setSeq(p.get("seq").getAsString());
				  t.setValue(p.get("value").getAsString()); 
				  if (p.has("comment")) {
					  t.setComment(p.get("comment").getAsString());
				  }
				  m.updateWhereEqual(t);
				  blockCounter++;
				  processed++;
				  if (blockCounter == block) {
					  System.out.println(AlwbGeneralUtils.done(processed, toProcess));
					  blockCounter = 0;
				  }
//				  System.out.println(t.toJsonString());
			} catch (DbException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  }
	}

}
