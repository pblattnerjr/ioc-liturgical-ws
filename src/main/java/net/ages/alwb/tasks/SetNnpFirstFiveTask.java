package net.ages.alwb.tasks;
import org.ocmc.ioc.liturgical.schemas.models.db.docs.ontology.TextLiturgical;
import org.ocmc.ioc.liturgical.schemas.models.ws.response.ResultJsonObjectArray;

import com.google.gson.JsonObject;

import ioc.liturgical.ws.managers.databases.external.neo4j.ExternalDbManager;
import ioc.liturgical.ws.managers.exceptions.DbException;

/**
 * Runs a task (separate thread) to set the first five tokens of each Liturgical text
 * @author mac002
 *
 */
public class SetNnpFirstFiveTask implements Runnable {
	ExternalDbManager manager = null;
	public SetNnpFirstFiveTask (
			ExternalDbManager manager
			) {
		this.manager = manager;
	}
	
	@Override
	public void run() {
		synchronized(this) {
			String query = "match (n:Root:Liturgical) where size(n. value) > 0  and not exists(n.nnpFirstFive)";
			ResultJsonObjectArray docs = this.manager.getForQuery(query + " return count(n)", false, false);
			JsonObject o = docs.getFirstObject();
			long nbr = o.get("count(n)").getAsLong();
			System.out.println(nbr);
			long batches = (nbr / 5000) + 1;
			for (int i = 0; i < batches; i++) {
				docs = this.manager.getForQuery(query + " return properties(n) limit 5000", false, false);
				String lastId = "";
				for (JsonObject doc : docs.getValues()) {
					String json = doc.get("properties(n)").getAsJsonObject().toString();
					TextLiturgical text = this.manager.gson.fromJson(json, TextLiturgical.class);
					try {
						text.setValue(text.getValue());
						ExternalDbManager.neo4jManager.updateWhereEqual(text, false);
						lastId = text.getId();
						System.out.println(lastId);
					} catch (DbException e) {
						e.printStackTrace();
					}
				}
				System.out.println(i + " of " + batches + ": " + lastId);
			}
		}
	}
	
}
