package delete.me;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import ioc.liturgical.ws.managers.databases.external.neo4j.utils.Neo4jConnectionManager;

import java.util.ArrayList;
import java.util.List;

import org.ocmc.ioc.liturgical.schemas.models.db.docs.ontology.TextBiblical;
import org.ocmc.ioc.liturgical.schemas.models.ws.response.RequestStatus;
import org.ocmc.ioc.liturgical.schemas.models.ws.response.ResultJsonObjectArray;

public class Neo4jQuickAndDirty {

	public static List<String> oddBalls = new ArrayList<String>();

	public static void addOddBall(String s) {
		if (oddBalls.contains(s)) {
			// ignore
		} else {
			oddBalls.add(s);
			System.out.println("\t\t\t" + s);
		}
	}

	public static String pad(String prefix, String s) {
		StringBuffer sb = new StringBuffer();
		String subVerse = "";
		if (s.length() < 1) {
			s = "0";
		}
		if (s.contains("-")) {
			sb.append(s);
			addOddBall(s);
		} else if (s.length() < 1) {
			sb.append("000");
		} else {
			if (!s.matches("^.+?\\d$")) {
				subVerse = s.substring(s.length() - 1);
				s = s.substring(0, s.length() - 1);
			}
			int intVerse = 0;
			try {
				intVerse = Integer.parseInt(s);
			} catch (Exception e) {
				throw e;
			}
			if (intVerse < 10) {
				sb.append("00" + intVerse);
			} else if (intVerse < 100) {
				sb.append("0" + intVerse);
			} else {
				sb.append(String.valueOf(intVerse));
			}
			sb.append(subVerse);
			if (subVerse.length() > 0) {
				addOddBall(sb.toString());
			}
		}
		return prefix + sb.toString();
	}

	public static void main(String[] args) {

		String uid = "";
		String pwd = "";
		Gson gson = new Gson();

		uid = System.getenv("uid");
		pwd = System.getenv("pwd");
		Neo4jConnectionManager neo4jManager = new Neo4jConnectionManager("localhost", uid, pwd, false);

		ResultJsonObjectArray booksList = neo4jManager.getForQuery("match (n:Biblical) return distinct n.topic");
		for (JsonObject v : booksList.getValues()) {
			String topic = v.get("n.topic").getAsString();
			System.out.println("Updating verses for " + topic);
			ResultJsonObjectArray books = neo4jManager
					.getForQuery("match (n:Biblical:" + topic + ") return properties(n) as props");
			for (JsonObject b : books.getValues()) {
				JsonObject props = b.get("props").getAsJsonObject();
				String json = props.toString();
				TextBiblical textOriginal = gson.fromJson(json, TextBiblical.class);
				TextBiblical textNew = gson.fromJson(json, TextBiblical.class);
				String originalId = textOriginal.getId();
				String[] parts = textOriginal.getKey().split(":");
				String chapter = parts[0];
				String verse = parts[1];
				if (chapter.length() < 4) {
					if (!verse.contains("-")) {
						String originalChapter = chapter;
						chapter = pad("C", chapter.substring(1));
						if (textOriginal.getId().equals("gr_eg_lxxupccatog~BEL~C01:0")) {
							System.out.print("");
						}
						if (verse.length() == 1 && verse.startsWith("0")) {
							verse = "000";
						} else {
							verse = pad("", verse);
						}
						textNew.setKey(chapter + ":" + verse);
						textNew.setId(textOriginal.getLibrary() + "~" + textNew.getTopic() + "~" + textNew.getKey());
						String seq = textOriginal.getSeq(); // 120~MA1~C06~50~en_uk_kjv
						String[] seqParts = seq.split("~");
						StringBuffer seqSb = new StringBuffer();
						seqSb.append(seqParts[0]);
						seqSb.append("~");
						seqSb.append(seqParts[1]);
						seqSb.append("~");
						seqSb.append(chapter);
						seqSb.append("~");
						seqSb.append(verse);
						seqSb.append("~");
						seqSb.append(textOriginal.getLibrary());
						textNew.setSeq(seqSb.toString());
						String labelQuery = "MATCH (n:Biblical) WHERE n.id = '" + originalId + "' REMOVE n:"
								+ originalChapter + "  SET n:" + chapter;
						String idQuery = "MATCH (n:Biblical) WHERE n.id = '" + originalId + "' SET n.id = '"
								+ textNew.getId() + "'";
						// textOriginal.setPrettyPrint(true);
						// textNew.setPrettyPrint(true);
						// System.out.println(textOriginal.toJsonString());
						// System.out.println(textNew.toJsonString());
						// System.out.println(labelQuery);
						try {
							// System.out.println(textOriginal.getId() + " => "
							// + textNew.getId());
							ResultJsonObjectArray labelUpdateResult = neo4jManager.getForQuery(labelQuery);
							// System.out.println(labelUpdateResult.getStatus().code);
							ResultJsonObjectArray idUpdateResult = neo4jManager.getForQuery(idQuery);
							// System.out.println(idUpdateResult.getStatus().code);
							RequestStatus status = neo4jManager.updateWhereEqual(textNew,false);
							// System.out.println(status.code);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
}
