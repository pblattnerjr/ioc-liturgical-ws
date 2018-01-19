package delete.me;

import java.io.UnsupportedEncodingException;

import com.google.gson.JsonObject;

import org.apache.commons.lang3.*;
import ioc.liturgical.ws.managers.databases.external.neo4j.utils.Neo4jConnectionManager;

import org.ocmc.ioc.liturgical.schemas.exceptions.BadIdException;
import org.ocmc.ioc.liturgical.schemas.models.ws.response.ResultJsonObjectArray;
import org.ocmc.ioc.liturgical.utils.FileUtils;
import net.ages.alwb.utils.nlp.models.wordnet.WnLexicalEntry;
import net.ages.alwb.utils.nlp.models.wordnet.WnLexicalSense;
import net.ages.alwb.utils.nlp.models.wordnet.WnSynset;
import net.ages.alwb.utils.nlp.models.wordnet.WordNet;

public class WordnetNeo4j {

	public static String unescape(String s) {
		String result = s;
		result = result.replaceAll("\\\u0027", "'");
		return result;
	}
	public static void main(String[] args) {
		Neo4jConnectionManager neo4jManager = null;
		String uid = System.getenv("uid");
		String pwd = System.getenv("pwd");
		  neo4jManager = new Neo4jConnectionManager(
				  "localhost"
				  , uid
				  , pwd
				  , false
				  );

		String query = "match (e:`lemon#LexicalEntry`) return properties(e) order by e.id";
		ResultJsonObjectArray s = neo4jManager.getForQuery(query);
		System.out.println(s.status.code + " " + s.getResultCount());
		WordNet wn = new WordNet();
		for (JsonObject j : s.getResult()) {
			try {
				JsonObject p = j.get("properties(e)").getAsJsonObject();
				String id = p.get("id").getAsString();
				WnLexicalEntry e = new WnLexicalEntry(id);
				e.setPrettyPrint(true);
				e.setPos(p.get("wdo#part_of_speech").getAsString());
				e.addFormsFromDelimitedString(p.get("Forms[]").getAsString());
				wn.addEntry(e);
			} catch (BadIdException e) {
				e.printStackTrace();
			}
		}
		query = "match (e:`lemon#LexicalSense`) return properties(e) order by e.id";
		s = neo4jManager.getForQuery(query);
		System.out.println(s.status.code + " " + s.getResultCount());
		for (JsonObject j : s.getResult()) {
			try {
				JsonObject p = j.get("properties(e)").getAsJsonObject();
				String id = p.get("id").getAsString();
				WnLexicalSense e = new WnLexicalSense(id);
				e.setGloss(p.get("wdo#gloss").getAsString());
				e.setSenseNbr(p.get("wdo#sense_number").getAsString());
				e.setLexId(p.get("wdo#lex_id").getAsString());
				try {
					e.setTagCount(p.get("wdo#tag_count").getAsInt());
				} catch (Exception z) {
					// ignore
				}
				e.setPrettyPrint(true);
				wn.addSense(e);
			} catch (BadIdException e) {
				e.printStackTrace();
			}
		}
		query = "match (e:`wdo#Synset`) return properties(e) order by e.id";
		s = neo4jManager.getForQuery(query);
		System.out.println(s.status.code + " " + s.getResultCount());
		for (JsonObject j : s.getResult()) {
			try {
				JsonObject p = j.get("properties(e)").getAsJsonObject();
				String id = p.get("id").getAsString();
				WnSynset e = new WnSynset(id);
				e.setGloss(p.get("wdo#gloss").getAsString());
				e.setPos(p.get("wdo#part_of_speech").getAsString());
				e.setLexicalDomain(p.get("wdo#lexical_domain").getAsString());
				if (p.has("w3#label[]")) {
					e.addLabelsFromDelimitedString(p.get("w3#label[]").getAsString());
				}
				if (p.has("wdo#sample[]")) {
					e.addSamplesFromDelimitedString(p.get("w3#label[]").getAsString());
				}
				if (p.has("wdo#phrase_type")) {
					e.setPhraseType(p.get("wdo#phrase_type").getAsString());
				}
				e.setPrettyPrint(true);
				wn.addSynset(e);
			} catch (BadIdException e) {
				e.printStackTrace();
			}
		}
		FileUtils.writeFile("/Volumes/ssd2/canBeRemoved/wordnet/wn.json", wn.toJsonString());
		System.out.println("Done");
	}

}
