package delete.me;

import ioc.liturgical.ws.managers.databases.external.neo4j.utils.Neo4jConnectionManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import org.apache.commons.io.FileUtils;
import org.ocmc.ioc.liturgical.schemas.constants.SCHEMA_CLASSES;
import org.ocmc.ioc.liturgical.schemas.models.supers.LTK;
import org.ocmc.ioc.liturgical.schemas.models.supers.LTKDb;
import org.ocmc.ioc.liturgical.schemas.models.supers.LTKLink;
import org.ocmc.ioc.liturgical.schemas.models.ws.response.ResultJsonObjectArray;
import org.ocmc.ioc.liturgical.utils.ErrorUtils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class Neo4jToJson {
	
	 private static String executeGitProcessor(String command, String file, String dir) {
		StringBuffer result = new StringBuffer();
		try {
				ProcessBuilder  ps = null;
				if (file.length() > 0) {
					ps = new ProcessBuilder("git", command, file);
				} else {
					ps = new ProcessBuilder("git", command);
				}
				ps.directory(new File(dir));
				ps.redirectErrorStream(true);
				Process pr = ps.start();  
				BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
				String line;
				while ((line = in.readLine()) != null) {
					result.append(line);
				}
				pr.waitFor();
				in.close();
				result.append("OK");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result.toString();
	}


	 /**
	  * Exclude
	  * en_sys_linguistics
	  * en_sys_tables
	  * en_sys_wsadmin
	  * 
	  * 
	  */

	 
	 
	 
	 public static void main(String[] args) {
		String out = "/volumes/ssd2/canBeRemoved/ocmc/gitlab/";
		Neo4jConnectionManager neo4jManager = null;
		Gson gson = new Gson();
		try {
			String user = System.getenv("user");
			String pwd = System.getenv("pwd");
			  neo4jManager = new Neo4jConnectionManager(
					  "localhost"
					  , user
					  , pwd
					  , false
					  );
			String query = "match (n:Root) return distinct n.library order by n.library";
			ResultJsonObjectArray libraries = neo4jManager.getForQuery(query);
			for (JsonObject library : libraries.getValues()) {
				String lib = library.get("n.library").getAsString();
				if (! lib.startsWith("en_sys_linguistics")) {
					continue;
				}
				query = "match (n:Root) where n.id starts with '" + lib + "' return distinct n.topic order by n.topic";
				ResultJsonObjectArray topics = neo4jManager.getForQuery(query);
				String libPath = out + lib;
//				executeGitProcessor("init", "", libPath);
				for (JsonObject t : topics.getValues()) {
					String topic = t.get("n.topic").getAsString();
					String topicPath = topic.replaceAll("\\.", "/");
					System.out.print(lib + " " + topic);
					query = "match (n:Root) where n.id starts with '" + lib + "~" + topic + "~' return properties(n)";
					ResultJsonObjectArray values = neo4jManager.getForQuery(query);
					System.out.println(": " + values.getValueCount());
					for (JsonObject v : values.getValues()) {
						String json = v.get("properties(n)").getAsJsonObject().toString();
						LTK form = gson.fromJson(json, LTK.class);
						SCHEMA_CLASSES schema = SCHEMA_CLASSES.classForSchemaName(form.get_valueSchemaId());
						form = 
								gson.fromJson(
										json
										, schema.ltk.getClass()
							);
						LTKDb record = 
								 gson.fromJson(
										json
										, SCHEMA_CLASSES
											.classForSchemaName(
													form.get_valueSchemaId())
											.ltkDb.getClass()
							);
						record.setPrettyPrint(true);
						StringBuffer sb = new StringBuffer();
						sb.append(out);
						sb.append(lib);
						sb.append("/");
						sb.append(topicPath);
						sb.append("/");
						sb.append(record.getId());
						sb.append(".json");
//						executeGitProcessor("add", sb.toString(), libPath + "/" + topic);
						org.ocmc.ioc.liturgical.utils.FileUtils.writeFile(sb.toString(), record.toJsonString());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
