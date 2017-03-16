package ioc.liturgical.ws.managers.databases.external.neo4j.utils;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import ioc.liturgical.ws.managers.databases.external.neo4j.CypherQuery;
import ioc.liturgical.ws.managers.databases.external.neo4j.CypherQueryBuilder;
import ioc.liturgical.ws.managers.databases.external.neo4j.ExternalDbManager;

/**
 * Returns the topics available in the database
 * @author mac002
 *
 */
public class TopicsService {
	String liturgicalTopicsSplitter = "split(doc.id,'~')[1]";

	public List<String> getLiturgicalTopicsFor(String domain, String book) {
		List<String> theTopics = new ArrayList<String>();
		String startsWith = domain;
		if (book.length() > 0) {
			startsWith = startsWith + "~" + book;
		}
		CypherQuery q = new CypherQueryBuilder()
				.MATCH()
				.LABEL("Liturgical")
				.LABEL(domain)
				.WHERE("id")
				.STARTS_WITH(startsWith)
				.RETURN("id")
				.ORDER_BY("doc.seq")
				.build();
		
		JsonObject json = ExternalDbManager
				.neo4jManager
				.getForQuery(q.toString())
				.toJsonObject();
		JsonArray values = json.get("values").getAsJsonArray();
		for (int i=0; i < values.size(); i++) {
			JsonElement e = values.get(i);
			String id = e.getAsJsonObject().get("doc.id").getAsString();
	//		id = id.replaceAll("~", ".");
			theTopics.add(id);
		}
		return theTopics;
	}

	public List<String> getBiblicalTopicsFor(String domain, String book) {
		List<String> theTopics = new ArrayList<String>();
		String startsWith = domain;
		if (book.length() > 0) {
			startsWith = startsWith + "~" + book;
		}
		CypherQuery q = new CypherQueryBuilder()
				.MATCH()
				.LABEL("Biblical")
				.LABEL(domain)
				.WHERE("id")
				.STARTS_WITH(startsWith)
				.RETURN("id")
				.ORDER_BY("doc.seq")
				.build();
		
		JsonObject json = ExternalDbManager
				.neo4jManager
				.getForQuery(q.toString())
				.toJsonObject();
		JsonArray values = json.get("values").getAsJsonArray();
		for (int i=0; i < values.size(); i++) {
			JsonElement e = values.get(i);
			String id = e.getAsJsonObject().get("doc.id").getAsString();
	//		id = id.replaceAll("~", ".");
	//		id = id.replaceAll(":", ".");
			theTopics.add(id);
		}
		return theTopics;
	}

}
