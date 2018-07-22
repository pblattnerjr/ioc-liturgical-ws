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
import org.ocmc.ioc.liturgical.utils.FileUtils;
	
	import com.google.gson.Gson;
	import com.google.gson.GsonBuilder;
	import com.google.gson.JsonElement;
	import com.google.gson.JsonObject;
	import com.google.gson.JsonParser;

	/**
	 * A one-off utility for loading English and Modern Greek labels for OLW from
	 * a json file.  OLW initially used a Javascript object for the labels.  This
	 * utility was used to move the labels to a database.
	 * @author mac002
	 *
	 */
	public class JsonLabelsToNeo4j {
		private JsonObject json = null; // JsonObject for the Labels loaded from a Json string
		public Map<String,String> labelsEnglish = new TreeMap<String,String>();
		public Map<String,String> labelsGreek = new TreeMap<String,String>();
		public List<UiLabel> labelList = new ArrayList<UiLabel>();
		private Neo4jConnectionManager dbManager = null;
		
		public JsonLabelsToNeo4j(
				String path
				, String url
				, String uid
				, String pwd
				, String system
				) {
			this.json = this.getJsonObject(path);
			this.process(url, uid, pwd, system);
		}
		
		public JsonLabelsToNeo4j(
				JsonObject json
				, String url
				, String uid
				, String pwd
				, String system
				) {
			this.json = json;
			this.process(url, uid, pwd, system);
		}
		
		private void process(String url, String uid, String pwd, String system) {
			  this.dbManager = new Neo4jConnectionManager(
					  url
					  , uid
					  , pwd
					  , false
					  );
			this.loadMapsFromJson();
			this.loadUiLabelList("en_sys_" + system, this.labelsEnglish);
			this.loadUiLabelList("el_sys_" + system, this.labelsGreek);
			this.updateDatabase();
		}
		
		private void updateDatabase() {
			System.out.println("Loading database for " + this.labelList.size() + " records");
			int count = 1;
			int all = this.labelList.size();
			for (UiLabel label : this.labelList) {
				try {
					System.out.println(count + " of " + all + " " + label.getId());
					count++;
					this.dbManager.mergeWhereEqual(label);
//					label.setPrettyPrint(true);
//					System.out.println(label.toJsonString());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		private void loadUiLabelList(String library, Map<String,String> list) {
			for (Entry<String,String> entry : list.entrySet()) {
				String [] parts = entry.getKey().split("\\.");
				String topic = parts[0];
				String key = "";
				StringBuffer sb = new StringBuffer();
				int j = parts.length;
				for (int i = 1;  i < j; i++ ) {
					if (sb.length() > 0) {
						sb.append(StringUtils.capitalize(parts[i]));
					} else {
						sb.append(parts[i]);
					}
				}
				key = sb.toString();
				UiLabel label = new UiLabel(library, topic, key, entry.getValue());
				label.setVisibility(VISIBILITY.PUBLIC);
				label.setStatus(STATUS.FINALIZED);
				this.labelList.add(label);
			}
		}

		private void loadMapsFromJson() {
			JsonObject en = json.get("en").getAsJsonObject();
			this.processObject("en", "", en);
			JsonObject el = json.get("el").getAsJsonObject();
			this.processObject("el", "", el);
		}
		
		private JsonObject getJsonObject(String path) {
			JsonObject json = null;
			try {
				String jsonString = FileUtils.getFileContents(new File(path));
				json = new JsonParser().parse(jsonString).getAsJsonObject();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return json;
		}

		private void processObject(String language, String parentLabel, JsonObject json) {
			String label = "";
			for (Entry<String, JsonElement> entry : json.entrySet()) {
				if (parentLabel.length() > 0) {
					label = parentLabel + "." + entry.getKey();
				} else {
					label = entry.getKey();
				}
				if (entry.getValue().isJsonObject()) {
					this.processObject(language, label, entry.getValue().getAsJsonObject());
				} else {
					if (language.equals("en")) {
						this.labelsEnglish.put(label, entry.getValue().getAsString());
					} else {
						this.labelsGreek.put(label, entry.getValue().getAsString());
					}
				}
			}
		}
		
		private void compare() {
			System.out.println("=========== Missing from Greek ============");
			for (Entry<String,String> entry : this.labelsEnglish.entrySet()) {
				if (! this.labelsGreek.containsKey(entry.getKey())) {
					System.out.println(entry.getKey());
					this.labelsGreek.put(entry.getKey(), entry.getValue());
				}
			}
			System.out.println("=========== Missing from English ============");
			for (Entry<String,String> entry : this.labelsGreek.entrySet()) {
				if (! this.labelsEnglish.containsKey(entry.getKey())) {
					System.out.println(entry.getKey());
					this.labelsEnglish.put(entry.getKey(), entry.getValue());
				}
			}
		}
		
		private void verifyAndPrintResults() {
			for (Entry<String,String> entry : this.labelsEnglish.entrySet()) {
				String [] parts = entry.getKey().split("\\.");
				String topic = parts[0];
				String key = "";
				StringBuffer sb = new StringBuffer();
				int j = parts.length;
				for (int i = 1;  i < j; i++ ) {
					if (sb.length() > 0) {
						sb.append("."); 
					}
					sb.append(parts[i]);
				}
				key = sb.toString();
				IdManager idManager = new IdManager("en_uk_olw", topic, key);
				System.out.println(idManager.getId());
			}
			System.out.println(this.labelsEnglish.size());
			System.out.println(this.labelsGreek.size());
			this.compare();
			this.compare();
		}

		public static void main(String[] args) {
			Gson gson = new GsonBuilder().disableHtmlEscaping().create();
			String path = "/Users/mac002/Git/ocmc-translation-projects/ioc-liturgical-ws/src/test/java/delete/me/labels.json";
			String uid = System.getenv("uid");
			String pwd = System.getenv("pwd");
			String url= "localhost"; // "159.203.89.233";
			String system = "olw";
			JsonLabelsToNeo4j labels2Neo = new JsonLabelsToNeo4j(path, url, uid, pwd, system);
//				labels2Neo.verifyAndPrintResults();
		}
	}
