package net.ages.alwb.tasks;
import org.ocmc.ioc.liturgical.schemas.models.db.docs.ontology.TextLiturgical;
import org.ocmc.ioc.liturgical.schemas.models.ws.response.ResultJsonObjectArray;

import com.google.gson.JsonObject;

import ioc.liturgical.ws.managers.databases.external.neo4j.ExternalDbManager;
import ioc.liturgical.ws.managers.exceptions.DbException;

/**
 * Runs a task (separate thread) to set fix the day number for Guatemala calendar
 * @author mac002
 *
 */
public class FixCalDays implements Runnable {
	ExternalDbManager manager = null;
	public FixCalDays (
			ExternalDbManager manager
			) {
		this.manager = manager;
	}
	
	@Override
	public void run() {
		synchronized(this) {
			String query = "match (n:Root:Liturgical) where n.topic = 'calendar' and n.key starts with 'y20' and n.key ends with '.md' return properties(n);";
			ResultJsonObjectArray docs = this.manager.getForQuery(query, false, false);
			for (JsonObject doc : docs.getValues()) {
				String json = doc.get("properties(n)").getAsJsonObject().toString();
				TextLiturgical text = this.manager.gson.fromJson(json, TextLiturgical.class);
				try {
					String value = text.getValue(); // 7 abril (domingo)
					String [] valueParts = value.split(" ");
					String key = text.getKey(); // y2018.m04.d15.md
					String [] keyParts = key.split("\\.");
					String dayNbr = keyParts[2].substring(1);
					if (dayNbr.startsWith("0")) {
						dayNbr = dayNbr.substring(1);
					}
					String newValue = dayNbr + " " + valueParts[1] + " " + valueParts[2];
					System.out.println(key + ": " + value + " => " + newValue);
					text.setValue(newValue);
				ExternalDbManager.neo4jManager.updateWhereEqual(text, false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
	}
	
}
