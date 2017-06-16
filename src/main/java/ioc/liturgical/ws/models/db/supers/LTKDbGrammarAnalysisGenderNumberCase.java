package ioc.liturgical.ws.models.db.supers;

import com.google.gson.annotations.Expose;

import ioc.liturgical.ws.constants.db.external.LIBRARIES;
import ioc.liturgical.ws.constants.db.external.SINGLETON_KEYS;
import ioc.liturgical.ws.constants.db.external.TOPICS;

import com.github.reinert.jjschema.Attributes;

/**
 * 
 * 
 * @author mac002
 *
 */
@Attributes(title = "LTKDb Grammar Analysis for Parts of Speech with Gender, Number, and Case", description = "Abstract Grammar Analysis for parts of speech that have gender, number, and case, e.g. adjectives and nouns.")
public class LTKDbGrammarAnalysisGenderNumberCase extends LTKDbGrammarAnalysis {
	
	private static TOPICS topic = TOPICS.GRAMMAR;

	@Attributes(required = true, description = "Gramatical gender, i.e. Masculine, Feminine, or Neuter")
	@Expose public String gender = "";

	@Attributes(required = true, description = "Grammatical number, i.e. singular, dual, or plural")
	@Expose public String number = "";

	@Attributes(required = true, description = "Grammatical case, i.e. Accusative, Dative, Genitive, Nominative, or Vocative")
	@Expose public String gCase = "";

	public LTKDbGrammarAnalysisGenderNumberCase(
			String form
			, String seq
			, String schema
			, double serialVersion
			) {
		super(
				form
				, seq
				, schema
				,  serialVersion
				);
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getgCase() {
		return gCase;
	}

	public void setgCase(String gCase) {
		this.gCase = gCase;
	}

}
