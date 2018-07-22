package net.ages.alwb.tasks;

import java.util.ArrayList;
import java.util.List;

import org.ocmc.ioc.liturgical.schemas.models.ws.response.ResultJsonObjectArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;

import ioc.liturgical.ws.managers.databases.external.neo4j.ExternalDbManager;

/**
 * Runs a task (separate thread) to count the number of tokens in a specified
 * liturgical book
 * .
 * @author mac002
 *
 */
public class UdTokenCountTask implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(UdTokenCountTask.class);

	private ExternalDbManager manager = null;
	private String requestor = "";
	private String idTopicStartsWith = "";
	private String out = "";
	
	public UdTokenCountTask (
			ExternalDbManager manager
			, String requestor
			, String idTopicStartsWith
			, String out
			) {
		this.manager = manager;
		this.requestor = requestor;
		this.idTopicStartsWith = idTopicStartsWith;
		this.out = out;
		
	}
	private List<String> getIds(String libraryStartswith, String topicStartsWith) {
		List<String> list = new ArrayList<String>();
		System.out.println("Running query ");
		String query = "match (n:Liturgical) where n.library = '" + libraryStartswith  + "' and n.topic starts with '" 
				+ topicStartsWith 
				+ "' and n.key ends with '.text' and size(n.value) > 0 return  n.topic as topic, n.key as key";
		System.out.println(query);
		ResultJsonObjectArray result = this.manager.getForQuery(query, false, false);
		for (JsonObject o : result.getValues()) {
			String topic = o.get("topic").getAsString();
			String key = o.get("key").getAsString();
			String topicKey = topic + "~" + key;
			if (! list.contains(topicKey)) {
				list.add(topicKey);
			}
		}
		query = "match (n:Liturgical)-[:VALUE_FROM]-(v:Liturgical) where n.library starts with '" + libraryStartswith + "' and n.topic starts with '" 
				+ topicStartsWith 
				+ "' and n.key ends with '.text' and size(v.value) > 0 and not v.key ends with 'media.key' return v.topic as topic, v.key as key";
		System.out.println(query);
		result = this.manager.getForQuery(query, false, false);
		for (JsonObject o : result.getValues()) {
			String topic = o.get("topic").getAsString();
			String key = o.get("key").getAsString();
			String topicKey = topic + "~" + key;
			if (! list.contains(topicKey)) {
				list.add(topicKey);
			}
		}
		return list;
	}

	@Override
	public void run() {
		int missing = 0;
		StringBuffer sb = new StringBuffer();
		List<String> grkIds = this.getIds("gr_gr_cog", this.idTopicStartsWith);
		sb.append("Analyzing " + this.idTopicStartsWith);
		sb.append("\n");
		sb.append("No English translations found for: ");
		sb.append("\n");
		List<String> engIds = this.getIds("en_", this.idTopicStartsWith);
		for (String topicKey : grkIds) {
			if (! engIds.contains(topicKey)) {
				missing++;
				sb.append(topicKey);
				sb.append("\n");
			}
		}
		sb.append(grkIds.size() + " - Number of Greek .text values found");
		sb.append("\n");
		sb.append(missing + " - Number needing to be translated into English ");
		sb.append("\n");
		org.ocmc.ioc.liturgical.utils.FileUtils.writeFile(this.out, sb.toString());
		System.out.println("Results written to " + out);
	}
}