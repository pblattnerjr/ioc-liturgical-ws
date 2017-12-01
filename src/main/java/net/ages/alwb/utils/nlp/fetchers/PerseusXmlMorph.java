package net.ages.alwb.utils.nlp.fetchers;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.ocmc.ioc.liturgical.schemas.models.db.docs.nlp.Adjective;
import org.ocmc.ioc.liturgical.schemas.models.db.docs.nlp.Adverb;
import org.ocmc.ioc.liturgical.schemas.models.db.docs.nlp.Article;
import org.ocmc.ioc.liturgical.schemas.models.db.docs.nlp.Conjunction;
import org.ocmc.ioc.liturgical.schemas.models.db.docs.nlp.Exclamation;
import org.ocmc.ioc.liturgical.schemas.models.db.docs.nlp.Noun;
import org.ocmc.ioc.liturgical.schemas.models.db.docs.nlp.Participle;
import org.ocmc.ioc.liturgical.schemas.models.db.docs.nlp.Preposition;
import org.ocmc.ioc.liturgical.schemas.models.db.docs.nlp.Pronoun;
import org.ocmc.ioc.liturgical.schemas.models.db.docs.nlp.Verb;
import org.ocmc.ioc.liturgical.schemas.models.supers.LTKDbGrammarAnalysis;
import org.ocmc.ioc.liturgical.utils.ErrorUtils;
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
	private static final Logger logger = LoggerFactory.getLogger(PerseusXmlMorph.class);

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
					.timeout(300000)
					.get();
			if (doc != null) {
				Elements elements = doc.select("analysis");
				for (Element e : elements) {
					String form = UnicodeGreekToExtendedGreek.normalize(this.get(e,"form"));
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
						try {
						String pos = this.get(e,"pos");
						String lemma = this.get(e,"lemma");
						String expandedForm = this.get(e,"expandedForm");
						String dialect = this.get(e,"feature");
						switch (pos) {
							case "adj" : {
								Adjective obj = new Adjective(form, paddedSequence);
								obj.setLemma(lemma);
								obj.setExpandedForm(expandedForm);
								obj.setDialect(dialect);
								obj.setNumber( this.get(e,"number") );
								obj.setGender( this.get(e,"gender") );
								obj.setgCase( this.get(e,"case") );
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
								obj.setNumber( this.get(e,"number") );
								obj.setGender( this.get(e,"gender") );
								obj.setgCase( this.get(e,"case") );
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
								obj.setNumber( this.get(e,"number") );
								obj.setGender( this.get(e,"gender") );
								obj.setgCase( this.get(e,"case") );
								analyses.add(obj);
								break;
							}
							case "part": {
								Participle obj = new Participle(form, paddedSequence);
								obj.setLemma(lemma);
								obj.setExpandedForm(expandedForm);
								obj.setDialect(dialect);
								obj.setNumber( this.get(e,"number") );
								obj.setGender( this.get(e,"gender") );
								obj.setgCase( this.get(e,"case") );
								obj.setNumber( this.get(e,"number") );
								obj.setTense( this.get(e,"tense") );
								obj.setVoice( this.get(e,"voice") );
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
								obj.setNumber( this.get(e,"number") );
								obj.setGender( this.get(e,"gender") );
								obj.setgCase( this.get(e,"case") );
								analyses.add(obj);
								break;
							}
							case "verb": {
								Verb obj = new Verb(form, paddedSequence);
								obj.setLemma(lemma);
								obj.setExpandedForm(expandedForm);
								obj.setDialect(dialect);
								obj.setMood( this.get(e,"mood") );
								if (obj.getMood().startsWith("inf")) {
									// ignore
								} else {
									obj.setPerson( this.get(e,"person") );
									obj.setNumber( this.get(e,"number") );
									obj.setTense( this.get(e,"tense") );
								}
								obj.setVoice( this.get(e,"voice") );
								analyses.add(obj);
								break;
							}
							default: {
								throw new Exception("unknown part of speech: " + pos);
							}
						}
					} catch (Exception innerE) {
						ErrorUtils.report(logger, innerE);
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
	
	/**
	 * Isolates the attempted read of a property into a try/catch block
	 * @param element
	 * @param property
	 * @return value of the requested property
	 */
	public String get(Element element, String property) {
		String result = "";
		try {
			if (element.hasAttr(property)) {
				result = element.select(property).first().text().trim();
			} 
		} catch (Exception e) {
			ErrorUtils.report(logger, e);
		}
		return result;
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
