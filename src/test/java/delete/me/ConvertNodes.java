package delete.me;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.ages.alwb.utils.core.file.AlwbFileUtils;

public class ConvertNodes {
	
	public static JsonArray convert(
			JsonArray nodes
			, Map<String,List<String>> map
			, String parent
			) {
		JsonArray rootChildren = new JsonArray();
			if (map.containsKey(parent)) {
				for (String key : map.get(parent)) {
					JsonObject node = nodes.get(Integer.parseInt(key)).getAsJsonObject();
					JsonObject nodeData = new JsonObject();
					nodeData.addProperty("key", key);
					nodeData.addProperty("token", node.get("token").getAsString());
					nodeData.addProperty("gloss", node.get("gloss").getAsString());
					nodeData.addProperty("grammar", node.get("grammar").getAsString());
					nodeData.addProperty("lemma", node.get("lemma").getAsString());
					JsonArray children = ConvertNodes.convert(nodes, map, key);
					if (children != null) {
						nodeData.add("nodes", children);
					}
					rootChildren.add(nodeData);
				}
		}
		JsonArray result = new JsonArray();
		JsonObject nodeData = new JsonObject();
		nodeData.addProperty("key", "");
		nodeData.addProperty("token", "");
		nodeData.addProperty("gloss", "");
		nodeData.addProperty("grammar", "");
		nodeData.addProperty("lemma", "");
		nodeData.add("nodes", rootChildren);
		result.add(nodeData);
		return result;
	}
	
	public static Map<String, List<String>> toDependencyList(JsonArray a) {
		Map<String,List<String>> result = new TreeMap<String, List<String>>();
		for (JsonElement e : a) {
			JsonObject o = e.getAsJsonObject();
			String dependsOn = o.get("dependsOn").getAsString();
			List<String> dependencies = new ArrayList<String>();
			if (result.containsKey(dependsOn)) {
				dependencies = result.get(dependsOn);
			}
			dependencies.add(o.get("key").getAsString());
			result.put(dependsOn, dependencies);
		}
		return result;
	}


	public static void main(String[] args) {
		String path = "/volumes/ssd2/canBeRemoved/nodes.json";;
		String json = AlwbFileUtils.fileAsString(new File(path));
		JsonObject jsonObject = (new JsonParser()).parse(json).getAsJsonObject();
		JsonArray values = jsonObject.get("values").getAsJsonArray();
		Map<String,List<String>> map = ConvertNodes.toDependencyList(values);
		for (Entry<String,List<String>> entry : map.entrySet()) {
			System.out.println(entry.getKey() + ": " + entry.getValue().toString());
		}
		JsonArray result = ConvertNodes.convert(values, map, "Root");
		System.out.println(result.toString());
	}

}
