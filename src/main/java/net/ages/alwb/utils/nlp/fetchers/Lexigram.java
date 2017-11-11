package net.ages.alwb.utils.nlp.fetchers;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import ioc.liturgical.ws.models.db.docs.nlp.WordAnalyses;


/**
 * Provides a means to retrieve data from Tufts Perseus Morphology gateway
 * 
 * Problems:
 * 1. The default Unicode we have for the database is using the Greek Extended block.
 * 2. Perseus is using the Greek and Coptic Unicode block.
 * 
 * Example: ί is code 8055 for our database, but 943 for Perseus
 * 
 * http://www.perseus.tufts.edu/hopper/help/quickstart.jsp
 * 
 * @author mac002
 *
 */
public class Lexigram {

	private static String url = "http://www.lexigram.gr/lex/arch/";
	private String token = "";
	private WordAnalyses analyses = null;

	public Lexigram(String token) {
		this.token = token;
		analyses = new WordAnalyses(token);
		this.fetchAnalyses();
	}
	
	/**
	 * Get the Perseus analyses for the Greek word.
	 * @param token
	 */
	private void fetchAnalyses() {
		Document doc = null;
		try {
			doc = Jsoup.connect(url + token+"#Hist1")
					.timeout(3000)
					.get();
			if (doc != null) {
				Elements elements = doc.select("body");
				for (Element e : elements) {
					System.out.println(e.html());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getWord() {
		return token;
	}

	public void setWord(String word) {
		this.token = word;
	}

	public static String getUrl() {
		return url;
	}

	public static void setUrl(String url) {
		Lexigram.url = url;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public WordAnalyses getAnalyses() {
		return analyses;
	}

	public void setAnalyses(WordAnalyses analyses) {
		this.analyses = analyses;
	}

}
