package ioc.liturgical.ws.models.db.docs.nlp;

import ioc.liturgical.ws.constants.nlp.PARTS_OF_SPEECH;
import ioc.liturgical.ws.models.db.supers.LTKDbGrammarAnalysisGenderNumberCase;

import com.github.reinert.jjschema.Attributes;
import com.google.gson.annotations.Expose;

/**
 * @author mac002
 *
 */
@Attributes(title = "Participle", description = "This is a doc that records information about a grammatical analysis of a participle.")
public class Participle extends LTKDbGrammarAnalysisGenderNumberCase {
	
	private static String schema = Participle.class.getSimpleName();
	private static double version = 1.1;
	
	@Attributes(required = true, description = "Grammatical number, i.e, singular, dual, or plural")
	@Expose public String number = "";

	@Attributes(required = true, description = "Grammatical tense, i.e., present")
	@Expose public String tense = "";

	@Attributes(required = true, description = "Grammatical voice, i.e., active, middle, passive ")
	@Expose public String voice = "";
	
	public Participle(
			String form
			, String key
			) {
		super(form, key, schema, version);
		super.setPos(PARTS_OF_SPEECH.PART);
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getTense() {
		return tense;
	}

	public void setTense(String tense) {
		this.tense = tense;
	}

	public String getVoice() {
		return voice;
	}

	public void setVoice(String voice) {
		this.voice = voice;
	}

}
