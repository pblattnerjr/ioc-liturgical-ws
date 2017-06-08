package net.ages.alwb.utils.nlp.fetchers;

import net.ages.alwb.utils.nlp.utils.NlpUtils;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import ioc.liturgical.ws.models.db.docs.nlp.Adjective;
import ioc.liturgical.ws.models.db.docs.nlp.Adverb;
import ioc.liturgical.ws.models.db.docs.nlp.Article;
import ioc.liturgical.ws.models.db.docs.nlp.Conjunction;
import ioc.liturgical.ws.models.db.docs.nlp.Exclamation;
import ioc.liturgical.ws.models.db.docs.nlp.Noun;
import ioc.liturgical.ws.models.db.docs.nlp.Participle;
import ioc.liturgical.ws.models.db.docs.nlp.Preposition;
import ioc.liturgical.ws.models.db.docs.nlp.Pronoun;
import ioc.liturgical.ws.models.db.docs.nlp.Verb;
import ioc.liturgical.ws.models.db.supers.LTKDbGrammarAnalysis;
import net.ages.alwb.utils.nlp.constants.BETA_CODES;
import net.ages.alwb.utils.nlp.constants.UnicodeGreekToExtendedGreek;

/**
 * Provides a means to retrieve data from Tufts Perseus XMLMorphology gateway
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
public class PerseusXmlMorph {

	private Charset utf8Charset = Charset.forName("UTF-8");
	private static String url = "http://www.perseus.tufts.edu/hopper/xmlmorph";
	private String word = "";
	private String betaCode = "";
	private List<LTKDbGrammarAnalysis> analyses = new ArrayList<LTKDbGrammarAnalysis>();

	public PerseusXmlMorph(String word) {
		this.word = word.toLowerCase();
		this.betaCode = BETA_CODES.toBetaCode(word);
		this.fetchAnalyses();
	}
	
	/**
	 * Get the Perseus analyses for the Greek word.
	 * @param word
	 */
	private void fetchAnalyses() {
		Document doc = null;
		int seq = 1;
		try {
			doc = Jsoup.connect(url)
					.data("lang", "greek")
					.data("lookup", betaCode) 
					.timeout(3000)
					.get();
			if (doc != null) {
				Elements elements = doc.select("analysis");
				for (Element e : elements) {
					String form = UnicodeGreekToExtendedGreek.normalize(e.select("form").first().text().trim());
//					List<Integer> diffIndex = Utils.getDiff(word, form);
//					if (diffIndex.size() > 0) {
//						System.out.println(word + " differs from " + form);
//						for (int i : diffIndex) {
//							System.out.println(word.charAt(i) + " : " + form.charAt(i));
//						}
//					}
					if (BETA_CODES.toBetaCode(form).equals(this.betaCode)) {
						String paddedSequence = "";
						if (seq < 10) {
							paddedSequence = "a00" + seq;
						} else if (seq < 100) {
							paddedSequence = "a0" + seq;
						} else {
							paddedSequence = "a" + seq;
						}
						seq++;
						String pos = e.select("pos").first().text().trim();
						String lemma = e.select("lemma").first().text().trim();
						String expandedForm = e.select("expandedForm").first().text().trim();
						String dialect = e.select("feature").first().text().trim();
						switch (pos) {
							case "adj" : {
								Adjective obj = new Adjective(form, paddedSequence);
								obj.setLemma(lemma);
								obj.setExpandedForm(expandedForm);
								obj.setDialect(dialect);
								obj.setNumber( e.select("number").first().text() );
								obj.setGender( e.select("gender").first().text() );
								obj.setgCase( e.select("case").first().text() );
								analyses.add(obj);
								break;
							}
							case "adv" : {
								Adverb obj = new Adverb(form, paddedSequence);
								obj.setLemma(lemma);
								obj.setExpandedForm(expandedForm);
								obj.setDialect(dialect);
								analyses.add(obj);
								break;
							}
							case "article" : {
								Article obj = new Article(form, paddedSequence);
								obj.setLemma(lemma);
								obj.setExpandedForm(expandedForm);
								obj.setDialect(dialect);
								obj.setNumber( e.select("number").first().text() );
								obj.setGender( e.select("gender").first().text() );
								obj.setgCase( e.select("case").first().text() );
								analyses.add(obj);
								break;
							}
							case "conj" : {
								Conjunction obj = new Conjunction(form, paddedSequence);
								obj.setLemma(lemma);
								obj.setExpandedForm(expandedForm);
								obj.setDialect(dialect);
								analyses.add(obj);
								break;
							}
							case "exclam": {
								Exclamation obj = new Exclamation(form, paddedSequence);
								obj.setLemma(lemma);
								obj.setExpandedForm(expandedForm);
								obj.setDialect(dialect);
								analyses.add(obj);
								break;
							}
							case "noun": {
								Noun obj = new Noun(form, paddedSequence);
								obj.setLemma(lemma);
								obj.setExpandedForm(expandedForm);
								obj.setDialect(dialect);
								obj.setNumber( e.select("number").first().text() );
								obj.setGender( e.select("gender").first().text() );
								obj.setgCase( e.select("case").first().text() );
								analyses.add(obj);
								break;
							}
							case "part": {
								Participle obj = new Participle(form, paddedSequence);
								obj.setLemma(lemma);
								obj.setExpandedForm(expandedForm);
								obj.setDialect(dialect);
								obj.setNumber( e.select("number").first().text() );
								obj.setGender( e.select("gender").first().text() );
								obj.setgCase( e.select("case").first().text() );
								obj.setNumber( e.select("number").first().text() );
								obj.setTense( e.select("tense").first().text() );
								obj.setVoice( e.select("voice").first().text() );
								analyses.add(obj);
								break;
							}
							case "prep" : {
								Preposition obj = new Preposition(form, paddedSequence);
								obj.setLemma(lemma);
								obj.setExpandedForm(expandedForm);
								obj.setDialect(dialect);
								analyses.add(obj);
								break;
							}
							case "pron": {
								Pronoun obj = new Pronoun(form, paddedSequence);
								obj.setLemma(lemma);
								obj.setExpandedForm(expandedForm);
								obj.setDialect(dialect);
								obj.setNumber( e.select("number").first().text() );
								obj.setGender( e.select("gender").first().text() );
								obj.setgCase( e.select("case").first().text() );
								analyses.add(obj);
								break;
							}
							case "verb": {
								Verb obj = new Verb(form, paddedSequence);
								obj.setLemma(lemma);
								obj.setExpandedForm(expandedForm);
								obj.setDialect(dialect);
								obj.setPerson( e.select("person").first().text() );
								obj.setNumber( e.select("number").first().text() );
								obj.setTense( e.select("tense").first().text() );
								obj.setMood( e.select("mood").first().text() );
								obj.setVoice( e.select("voice").first().text() );
								analyses.add(obj);
								break;
							}
							default: {
								throw new Exception("unknown part of speech: " + pos);
							}
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
				
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public List<LTKDbGrammarAnalysis> getAnalyses() {
		return analyses;
	}

	public void setAnalyses(List<LTKDbGrammarAnalysis> analyses) {
		this.analyses = analyses;
	}
	
	public int analysisCount() {
		return analyses.size();
	}
	
	private <T> List<T> getForType(Class<T> c) {
		return 
				analyses
				.stream()
				.filter(c::isInstance)
				.map(c::cast)
				.collect(Collectors.toList())
				;
	}

	public List<Adjective> getAdjectives() {
		return getForType(Adjective.class);
	}
	
	public List<Article> getArticles() {
		return getForType(Article.class);
	}
	
	public List<Conjunction> getConjunctions() {
		return getForType(Conjunction.class);
	}
	
	public List<Exclamation> getExclamations() {
		return getForType(Exclamation.class);
	}
	
	public List<Noun> getNouns() {
		return getForType(Noun.class);
	}
	
	public List<Preposition> getPrepositions() {
		return getForType(Preposition.class);
	}
	
	public List<Pronoun> getPronoun() {
		return getForType(Pronoun.class);
	}
	
	public List<Verb> getVerbs() {
		return getForType(Verb.class);
	}
}
