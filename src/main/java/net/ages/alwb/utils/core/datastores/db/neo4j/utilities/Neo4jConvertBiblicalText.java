package net.ages.alwb.utils.core.datastores.db.neo4j.utilities;

import com.google.gson.JsonObject;

import ioc.liturgical.ws.managers.databases.external.neo4j.utils.Neo4jConnectionManager;
import ioc.liturgical.ws.managers.exceptions.DbException;

import org.ocmc.ioc.liturgical.schemas.models.db.docs.ontology.TextBiblical;
import org.ocmc.ioc.liturgical.schemas.models.ws.response.ResultJsonObjectArray;
import org.ocmc.ioc.liturgical.utils.GeneralUtils;

/**
 * Converts the original Biblical text schema to the new one
 * @author mac002
 *
 */
public class Neo4jConvertBiblicalText {
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
		  String query = "match (n:Biblical) return properties(n)";
		  ResultJsonObjectArray s = m.getForQuery(query);
		  System.out.println(s.getStatus().getCode() + s.getValueCount());
		  int toProcess = Integer.valueOf(String.valueOf(s.getResultCount()));
		  int block = (int) (toProcess * .1);
		  int processed = 0;
		  int blockCounter = 0;
		  for (JsonObject o : s.getValues()) {
			  try {
				  JsonObject p = o.get("properties(n)").getAsJsonObject();
				  TextBiblical t = new TextBiblical(
						  p.get("library").getAsString()
						  , p.get("topic").getAsString()
						  , p.get("key").getAsString()
						  );
				  t.setBookNbr(p.get("bookNbr").getAsString());
				  t.setChapter(p.get("chapter").getAsString());
				  t.setNnp(p.get("nnp").getAsString());
				  t.setNwp(p.get("nwp").getAsString());
				  t.setSeq(p.get("seq").getAsString());
				  t.setValue(p.get("value").getAsString());
				  t.setVerse(p.get("verse").getAsString());
				  m.updateWhereEqual(t,false);
				  blockCounter++;
				  processed++;
				  if (blockCounter == block) {
					  System.out.println(GeneralUtils.done(processed, toProcess));
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
