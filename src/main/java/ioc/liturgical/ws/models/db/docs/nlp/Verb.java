package ioc.liturgical.ws.models.db.docs.nlp;

import com.google.gson.annotations.Expose;
import com.github.reinert.jjschema.Attributes;

import ioc.liturgical.ws.constants.nlp.PARTS_OF_SPEECH;
import ioc.liturgical.ws.models.db.supers.LTKDbGrammarAnalysis;

/**
 * 
 * 
 * @author mac002
 *
 */
@Attributes(title = "LTKDb Grammar Analysis for Parts of Speech with Gender, Number, and Case", description = "Abstract Grammar Analysis for parts of speech that have gender, number, and case, e.g. adjectives and nouns.")
public class Verb extends LTKDbGrammarAnalysis {

	private static String schema = Verb.class.getSimpleName();
	private static double version = 1.1;

	@Attributes(required = true, description = "Grammatical number, i.e, singular, dual, or plural")
	@Expose public String number = "";

	@Attributes(required = true, description = "Grammatical person, i.e., 1st, 2nd, 3rd")
	@Expose public String person = "";

	@Attributes(required = true, description = "Grammatical tense, i.e., present")
	@Expose public String tense = "";

	@Attributes(required = true, description = "Grammatical mood, i.e.,  subjunctive")
	@Expose public String mood = "";

	@Attributes(required = true, description = "Grammatical voice, i.e., active, middle, passive ")
	@Expose public String voice = "";

	public Verb(
			String form
			, String seq
			) {
		super(
				form
				, seq
				, schema
				,  version
		);
		super.setPos(PARTS_OF_SPEECH.VERB);
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getPerson() {
		return person;
	}

	public void setPerson(String person) {
		this.person = person;
	}

	public String getTense() {
		return tense;
	}

	public void setTense(String tense) {
		this.tense = tense;
	}

	public String getMood() {
		return mood;
	}

	public void setMood(String mood) {
		this.mood = mood;
	}

	public String getVoice() {
		return voice;
	}

	public void setVoice(String voice) {
		this.voice = voice;
	}


}
