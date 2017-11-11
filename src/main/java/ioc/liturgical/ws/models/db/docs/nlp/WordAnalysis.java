package ioc.liturgical.ws.models.db.docs.nlp;

import java.time.Instant;

import com.github.reinert.jjschema.Attributes;
import com.google.gson.annotations.Expose;

import ioc.liturgical.ws.constants.db.external.LIBRARIES;
import ioc.liturgical.ws.constants.db.external.TOPICS;
import ioc.liturgical.ws.models.db.supers.LTKDb;
import net.ages.alwb.utils.nlp.constants.GRAMMAR_ABBREVIATIONS_MAPPER;

/**
 * Word Analysis
 * 
 * Usually these are analyses from the Perseus (Tuft University) morphological analysis tool.
 * But, they can also be analyses added by uses of this system.
 * 
 * Note the difference between a TokenAnalysis and a WordAnalysis.
 * A TokenAnalysis is the grammatical analysis of a specific token 
 * occurring in a specific text.  A WordAnalysis has no direct association
 * with any particular text.  It is just a potential analysis for a given inflected form.
 * 
 * @author mac002
 * @see TokenAnalysis
 *
 */
@Attributes(title = "Word Analysis", description = "Grammatical analysis of a word.")
public class WordAnalysis extends LTKDb {

	private static String schema = WordAnalysis.class.getSimpleName();
	private static double serialVersion = 1.1;
	private static TOPICS topic = TOPICS.WORD_GRAMMAR;

	@Attributes(required = true, description = "The token is the word as it appears in the text.")
	@Expose public String token = "";
	@Attributes(required = true, description = "The lemma is the dictionary form of the word.")
	@Expose public String lemmaGreek = "";
	@Attributes(required = true, description = "The BetaCode form of the lemma.")
	@Expose public String lemmaBetaCode = "";
	@Attributes(required = true, description = "The Greek form of the token.")
	@Expose public String greek = "";
	@Attributes(required = true, description = "The grammatical properties of the token.")
	@Expose public String parse = "";
	@Attributes(required = true, description = "The part of speech of the token.")
	@Expose public String partOfSpeech = "";
	@Attributes(required = true, description = "Words or phrases that can be used as glosses (translations).")
	@Expose public String glosses = "";
	@Attributes(required = true, description = "A conside statement of the token / glosses / parse / lemma")
	@Expose public String concise = "";
	@Attributes(required = true, description = "Where the information for this parse came from.")
	@Expose public String source = "";
	
	public WordAnalysis() {
		super(
				LIBRARIES.LINGUISTICS.toSystemDomain()
				, ""
				, ""
				, schema
				,  serialVersion
				, topic
				);
	}
	
	public WordAnalysis(
			String token
			, String greek
			, String lemmaGreek
			, String lemmaBetaCode
			, String parse
			, String glosses
			) {
		super(
				LIBRARIES.LINGUISTICS.toSystemDomain()
				, token
				, lemmaGreek + "/" + GRAMMAR_ABBREVIATIONS_MAPPER.convertToLeipzig(" ", parse, ".")
				, schema
				,  serialVersion
				, topic
				);
		this.token = token;
		this.greek = greek;
		this.lemmaBetaCode = lemmaBetaCode;
		this.lemmaGreek = lemmaGreek;
		this.parse = parse;
		this.partOfSpeech = GRAMMAR_ABBREVIATIONS_MAPPER.getLabelValue(parse.split(" ")[0]);
		this.glosses = glosses;
		this.concise = this.toExPexInterlinear(true);
		this.source = "Tufts University, Perseus,  Greek Word Study Tool";
		this.setCreatedBy("wsadmin");
		this.setModifiedBy("wsadmin");
		this.setCreatedWhen(Instant.now().toString());
		this.setModifiedWhen(this.getCreatedWhen());
	}

	public String getGreek() {
		return greek;
	}

	public void setGreek(String greek) {
		this.greek = greek;
	}

	public String getParse() {
		return parse;
	}

	public void setParse(String parse) {
		this.parse = parse;
	}
	
	public String getPartOfSpeech() {
		return partOfSpeech;
	}

	public void setPartOfSpeech(String partOfSpeech) {
		this.partOfSpeech = partOfSpeech;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getGlosses() {
		return glosses;
	}

	public void setGlosses(String glosses) {
		this.glosses = glosses;
	}

	public String getLemmaGreek() {
		return lemmaGreek;
	}

	public void setLemmaGreek(String lemmaGreek) {
		this.lemmaGreek = lemmaGreek;
	}

	public String getLemmaBeta() {
		return lemmaBetaCode;
	}

	public void setLemmaBeta(String lemmaBeta) {
		this.lemmaBetaCode = lemmaBeta;
	}
	
	public String toExPexInterlinear(boolean useLeipzig) {
		String parse = this.parse;
		if (useLeipzig) {
			parse = GRAMMAR_ABBREVIATIONS_MAPPER.convertToLeipzig(" ", parse, ".");
		}
		return "[" 
				+ this.token 
				+ "/" 
				+ this.glosses.replace("[definition unavailable]", "{[}definition unavailable{]}")
				+ "/" 
				+ parse 
				+ "/" 
				+ this.lemmaGreek
				+ "]";
	}

	public String getLemmaBetaCode() {
		return lemmaBetaCode;
	}

	public void setLemmaBetaCode(String lemmaBetaCode) {
		this.lemmaBetaCode = lemmaBetaCode;
	}

	public String getConcise() {
		return concise;
	}

	public void setConcise(String concise) {
		this.concise = concise;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
}
