package ioc.liturgical.ws.managers.databases.external.neo4j.utils;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import ioc.liturgical.ws.managers.databases.external.neo4j.ExternalDbManager;

/**
 * Returns the topics available in the database
 * @author mac002
 *
 */
public class LabelsService {

	public List<String> getLabelsFor(String type) {
		List<String> result = new ArrayList<String>();
		JsonObject json = ExternalDbManager.queryToDb.getForQuery("match (doc:" + type + ") return distinct labels(doc)");
		JsonArray values = json.get("values").getAsJsonArray();
		for (int i=0; i < values.size(); i++) {
			JsonElement e = values.get(i);
			JsonArray labelsArray = e.getAsJsonObject().get("labels(doc)").getAsJsonArray();
			StringBuilder labels = new StringBuilder();
			for (int j=0; j < labelsArray.size(); j++) {
				String label = labelsArray.get(j).getAsString();
				if (label.startsWith("Text") || label.startsWith(type)) {
					// ignore
				} else {
					if (labels.length() > 0) {
						labels.append(",");
					}
					labels.append(label);
				}
			}
			result.add(labels.toString());
		}
		return result;
	}

}
