package net.ages.alwb.utils.transformers;


	import ioc.liturgical.ws.managers.databases.external.neo4j.utils.Neo4jConnectionManager;

import java.util.ArrayList;
import java.util.List;

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
	public class LabelsNeoToJson {
		private Neo4jConnectionManager dbManager = null;
		
		public LabelsNeoToJson(
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
		
		private String getTopicsEnum(List<String> topics) {
			StringBuffer sb = new StringBuffer();
			sb.append("// run ioc-liturgical-ws net.ages.alwb.utils.transformers.LabelsNeoToJson to produce this file.\n\n");
			sb.append("class LabelTopics {};\n");
			for (String topic : topics) {
				sb.append("LabelTopics.");
				sb.append(topic);
				sb.append(" = '");
				sb.append(topic);
				sb.append("';\n");
			}
			sb.append("export default LabelTopics;");
			return sb.toString();
		}
		
		private void process(String system, String out) {
			JsonObject result = new JsonObject();
			String topicsEnum = "";
			String query = "match (n:UiLabel) return distinct split(n.library, \"_\")[0] as item";
			ResultJsonObjectArray queryResult = dbManager.getResultObjectForQuery(query);
			List<String> languages = new ArrayList<String>();
			for (JsonObject json : queryResult.values) {
				languages.add(json.get("item").getAsString());
			}
			for (String language : languages) {
				JsonObject langJson = new JsonObject();
				String library = language + "_sys_" + system; 
				query = "match (n:UiLabel) where n.library starts with '" + language + "' and n.library ends with '" +  system + "' return distinct n.topic as item";
				ResultJsonObjectArray langQueryResult = dbManager.getResultObjectForQuery(query);
				List<String> topics = new ArrayList<String>();
				for (JsonObject json : langQueryResult.values) {
					topics.add(json.get("item").getAsString());
				}
				topicsEnum = this.getTopicsEnum(topics);
				
				for (String topic : topics) {
					JsonObject topicsJson = new JsonObject();
					query = "match (n:UiLabel) where n.library = '" + library +"' and n.topic = '" + topic + "' return distinct n.key as label, n.value as value";
					ResultJsonObjectArray topicQueryResult = dbManager.getResultObjectForQuery(query);
					for (JsonObject json : topicQueryResult.values) {
						String label = json.get("label").getAsString();
						String value = json.get("value").getAsString();
						topicsJson.addProperty(label, value);
					}
					langJson.add(topic, topicsJson);
				}
				result.add(language, langJson);
			}
			FileUtils.writeFile(out + "/labels.json",result.toString());
			FileUtils.writeFile(out + "/LabelTopics.js", topicsEnum);
		}
		
		
		public static void main(String[] args) {
			Gson gson = new GsonBuilder().disableHtmlEscaping().create();
			String reactPath = "/Users/mac002/Git/ocmc-translation-projects/ioc-liturgical-react/src/classes";
			String olwPath = "/Users/mac002/Git/ocmc-translation-projects/ioc-tms-app/src/labels";
			String path = reactPath;
			String uid = System.getenv("uid");
			String pwd = System.getenv("pwd");
			String url= "localhost"; // "159.203.89.233";
			String system = "ilr";
			LabelsNeoToJson labels2Neo = new LabelsNeoToJson(url, uid, pwd);
			labels2Neo.process("ilr", path);
		}
	}
