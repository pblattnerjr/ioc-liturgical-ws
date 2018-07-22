package delete.me;

import ioc.liturgical.ws.managers.databases.external.neo4j.utils.Neo4jConnectionManager;
import ioc.liturgical.ws.managers.exceptions.DbException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.ocmc.ioc.liturgical.schemas.constants.VISIBILITY;
import org.ocmc.ioc.liturgical.schemas.models.db.docs.nlp.WordAnalysis;
import org.ocmc.ioc.liturgical.schemas.models.db.docs.ontology.TextBiblical;
import org.ocmc.ioc.liturgical.schemas.models.ws.response.RequestStatus;
import org.ocmc.ioc.liturgical.schemas.models.ws.response.ResultJsonObjectArray;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class Neo4jBibleKeyNormalizer {

	public static String getPaddedChapterNbr(String chapterNbr) {
		int i = 0;
		if (chapterNbr.startsWith("C")) {
			i = Integer.parseInt(chapterNbr.substring(1, chapterNbr.length()));
		} else {
			i = Integer.parseInt(chapterNbr);
		}
		return "C" + getPaddedNbr(i);
	}
	
	public static String getPaddedNbr(int i) {
		return String.format("%03d", i);
	}

	public static String getPaddedNbr(String s) {
		int i = 0;
		String last  = "";
		try {
			i = Integer.parseInt(s);
		} catch (Exception e) {
			String n = s.substring(0, s.length()-1);
			last = s.substring(s.length()-1);
			i = Integer.parseInt(n);
		}
		return String.format("%03d", i) + last;
	}

	public static void main(String[] args) {
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		Neo4jConnectionManager neo4jManager = null;
		List<String> libraries = new ArrayList<String>();
		libraries.add("gr_eg_LxxUpccatBA");
//		libraries.add("gr_gr_ntpt");
//		libraries.add( "gr_eg_LxxUpccat");
		libraries.add( "gr_eg_LxxUpccatTh");
		libraries.add( "gr_eg_LxxUpccatOG");
//		libraries.add( "en_uk_kjv");
//		libraries.add( "en_uk_lxxbrenton");
		libraries.add( "gr_eg_LxxUpccatB");
//		libraries.add( "en_uk_webbe");
		libraries.add( "gr_eg_LxxUpccatS");
		String uid = System.getenv("uid");
		String pwd = System.getenv("pwd");
		String url= "159.203.89.233"; // "localhost";
		  neo4jManager = new Neo4jConnectionManager(
				  url
				  , uid
				  , pwd
				  , false
				  );
		  for (String library : libraries) {
			System.out.print("Processing " + library);
			String query = "match (n:Biblical) where n.library = '" + library + "' return properties(n)";
			ResultJsonObjectArray s = neo4jManager.getForQuery(query);
			System.out.println(" = " + s.getResultCount());
			for (JsonObject o : s.getValues()) {
				TextBiblical text = gson.fromJson(o.get("properties(n)").getAsJsonObject().toString(), TextBiblical.class);
				String originalId = text.getId();
				if (text.getKey().length() < 8) {
					String chapter = text.getChapter();
					chapter = getPaddedChapterNbr(chapter);
					String verse = text.getVerse();
					if (verse.contains("-") || verse.contains("/")) {
						// ignored
					} else {
						verse = getPaddedNbr(verse);
					}
					text.setKey(chapter + ":" + verse);
					text.setId(text.getLibrary() + "~" + text.getTopic() + "~" + text.getKey());
					TextBiblical newText = new TextBiblical(
							text.getLibrary()
							, text.getTopic()
							,text.getKey()
							);
					newText.setActive(true);
					newText.setCreatedBy("wsadmin");
					newText.setModifiedBy("wsadmin");
					newText.setCreatedWhen(Instant.now().toString());
					newText.setModifiedWhen(text.getCreatedWhen());
					newText.setVisibility(VISIBILITY.PUBLIC);
					System.out.println(
					newText.validate(newText.toJsonString())
					);
					try {
						RequestStatus status = neo4jManager.updateWhereEqual(originalId, newText);
						if (status.code == 400) {
							System.out.println(status.developerMessage);
						}
						System.out.println(text.getId() + " = " + status.getCode());
					} catch (DbException e) {
						e.printStackTrace();
					}
				}
			}
		  }
	}
}
