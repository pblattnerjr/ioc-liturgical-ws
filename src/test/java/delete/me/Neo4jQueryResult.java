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

public class Neo4jQueryResult {

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
		  String query = "match (n:DELETE) where n.id = 'en_sys_notes~en_sys_nouns~en_uk_lash~actors~Priest~newkey~n1' delete n return count(n)";
//		String query = "match (n:Root) where n.id = 'en_uk_lash~tr.d069~trMA.Synaxarion.text' remove n:Root set n :DELETED_Root";
		ResultJsonObjectArray s = neo4jManager.getForQuery(query);
		System.out.println(s.status.code + " " + s.getResultCount());
		System.out.println(s.getStatus().toJsonString());
	}
}
