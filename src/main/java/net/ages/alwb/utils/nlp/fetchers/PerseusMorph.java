package net.ages.alwb.utils.nlp.fetchers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import ioc.liturgical.ws.models.db.docs.nlp.PerseusAnalyses;
import ioc.liturgical.ws.models.db.docs.nlp.PerseusAnalysis;
import net.ages.alwb.utils.nlp.constants.GRAMMAR_ABBREVIATIONS;


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
public class PerseusMorph {

	private static String url = "http://www.perseus.tufts.edu/hopper/morph";
	private String token = "";
	private PerseusAnalyses analyses = null;

	public PerseusMorph(String token) {
		this.token = token.trim();
		analyses = new PerseusAnalyses(token);
		this.fetchAnalyses();
	}
	
	/**
	 * Get the Perseus analyses for the Greek word.
	 * @param token
	 */
	private void fetchAnalyses() {
		if (token.length() == 1 && net.ages.alwb.utils.nlp.constants.Constants.PUNCTUATION.contains(token)) {
			PerseusAnalysis analysis = new PerseusAnalysis(
					token
					, token
					, token
					, token
					, GRAMMAR_ABBREVIATIONS.PM.keyname
					, token
					);
			analyses.addAnalysis(analysis);
		} else {
			Document doc = null;
			try {
				doc = Jsoup.connect(url)
						.data("l", token)
						.data("la", "greek") 
						.timeout(60000)
						.get();
				if (doc != null) {
					Elements elements = doc.select("div.analysis");
					for (Element e : elements) {
							String lemmaGreek = e.select("h4.greek").first().text().trim();
							String lemmaBetaCode = e.select("div.lemma").attr("id").trim();
							String glosses = e.select("span.lemma_definition").first().text();
							if (glosses.endsWith(",") || glosses.endsWith(";")) {
								glosses = glosses.substring(0, glosses.length()-1);
							}
							Elements rows = e.select("tr");
							for (Element r : rows) {
								String greek = r.select("td.greek").first().text().trim();
								String parse = r.child(1).text().trim();
								PerseusAnalysis analysis = new PerseusAnalysis(
										token
										, greek
										, lemmaGreek
										, lemmaBetaCode
										, parse
										, glosses
										);
								analyses.addAnalysis(analysis);
							}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
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
		PerseusMorph.url = url;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public PerseusAnalyses getAnalyses() {
		return analyses;
	}

	public void setAnalyses(PerseusAnalyses analyses) {
		this.analyses = analyses;
	}

}
