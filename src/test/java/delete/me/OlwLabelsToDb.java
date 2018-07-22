package delete.me;

import java.io.File;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.ocmc.ioc.liturgical.schemas.id.managers.IdManager;
import org.ocmc.ioc.liturgical.utils.FileUtils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class OlwLabelsToDb {

	public Map<String,String> labelsEnglish = new TreeMap<String,String>();
	public Map<String,String> labelsGreek = new TreeMap<String,String>();

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
	public static void main(String[] args) {
		OlwLabelsToDb olwEn = new OlwLabelsToDb();
		String path = "/Users/mac002/Git/ocmc-translation-projects/ioc-liturgical-ws/src/test/java/delete/me/labels.json";
		String jsonString = FileUtils.getFileContents(new File(path));
		JsonObject json = new JsonParser().parse(jsonString).getAsJsonObject();
		JsonObject en = json.get("en").getAsJsonObject();
		olwEn.processObject("en", "", en);
		for (Entry<String,String> entry : olwEn.labelsEnglish.entrySet()) {
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
		System.out.println(olwEn.labelsEnglish.size());
		JsonObject el = json.get("el").getAsJsonObject();
		olwEn.processObject("el", "", el);
		System.out.println(olwEn.labelsGreek.size());
		olwEn.compare();
		olwEn.compare();
	}

}
