package ioc.liturgical.ws.models.db.supers;

import com.google.gson.annotations.Expose;

import ioc.liturgical.ws.constants.Constants;
import ioc.liturgical.ws.constants.db.external.LIBRARIES;
import ioc.liturgical.ws.constants.db.external.TOPICS;
import ioc.liturgical.ws.models.db.supers.LTKDb;
import net.ages.alwb.utils.core.misc.AlwbGeneralUtils;

import java.text.Normalizer;

import org.apache.commons.lang3.math.NumberUtils;

import com.github.reinert.jjschema.Attributes;

/**
 * Holds information about the grammatical analysis of a token from a text.
 * A token can be a word or a punctuation mark.
 * 
 * Note the difference between a TokenAnalysis and a WordAnalysis.
 * A TokenAnalysis is the grammatical analysis of a specific token 
 * occurring in a specific text.  A WordAnalysis has no direct association
 * with any particular text.  It is just a potential analysis for a given inflected form.
 * 
 * TODO: Note that the long-term solution is to store such information using the
 * NLP model  corresponding to the part-of-speech of the token.
 * 
 * So, this is just a temporary solution.
 * 
 * @author mac002
 * @see WordAnalysis
 *
 */
@Attributes(title = "Token Analysis", description = "Grammatical analysis of a token")
public class LTKDbTokenAnalysis extends LTKDb {
	
    @Expose String dependsOn = "root";
    @Expose String token = "";
    @Expose String lemma = "";
    @Expose String nnpToken = "";
    @Expose String nnpLemma = "";
    @Expose String gloss = "";
    @Expose String label = "";
    @Expose String gCase = "";
    @Expose String gender = "";
    @Expose String mood = "";
    @Expose String number = "";
    @Expose String person = "";
    @Expose String pos = "";
    @Expose String tense = "";
    @Expose String voice = "";
    @Expose String grammar = "";
    @Expose String refersTo = "";
    @Expose String seq = "";

	public LTKDbTokenAnalysis(
			String topic
			, String key
			, String schema
			, double serialVersion
			, TOPICS ontologyTopic
			) {
		super(
				LIBRARIES.LINGUISTICS.toSystemDomain()
				, topic
				, key
				, schema
				,  serialVersion
				, ontologyTopic
				);
		this.seq = this.toSeq();
	}

	public LTKDbTokenAnalysis(
			String schema
			, double serialVersion
			, TOPICS topic
			, LTKTokenAnalysisCreateForm form
			) {
		super(
				form.getLibrary()
				, form.getTopic()
				, form.getKey()
				, schema
				,  serialVersion
				, topic
				);
		this.dependsOn = form.getDependsOn();
		this.gCase = form.getgCase();
		this.gender = form.getGender();
		this.gloss = form.getGloss();
		this.refersTo = form.getRefersTo();
		this.label = form.getLabel();
		this.lemma = form.getLemma();
		this.mood = form.getMood();
		this.number = form.getNumber();
		this.person = form.getPerson();
		this.pos = form.getPos();
		this.tense = form.getTense();
		this.voice = form.getVoice();
		this.tags = form.getTags();
		this.seq = this.toSeq();
		this.grammar = this.toGrammarAbbreviations();
	}

	private String toSeq() {
		String key = this.key;
		if (NumberUtils.isNumber(key)) {
			key = AlwbGeneralUtils.padNumber(
					"0"
					, 3
					, Integer.parseInt(this.key)
					);
		} else if (key.equals("root")) {
			key = "000";
		}
		return 
				this.library 
				+ Constants.ID_DELIMITER 
				+ this.topic 
				+ Constants.ID_DELIMITER 
				+ key
				;
	}

	public String getDependsOn() {
		return dependsOn;
	}

	public void setDependsOn(String dependsOn) {
		this.dependsOn = dependsOn;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
		this.nnpToken = Normalizer.normalize(token, Normalizer.Form.NFD)
				.replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();
	}

	public String getLemma() {
		return lemma;
	}

	public void setLemma(String lemma) {
		this.lemma = lemma;
		this.nnpLemma = Normalizer.normalize(lemma, Normalizer.Form.NFD)
				.replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();
	}

	public String getGloss() {
		return gloss;
	}

	public void setGloss(String gloss) {
		this.gloss = gloss;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getgCase() {
		return gCase;
	}

	public void setgCase(String gCase) {
		this.gCase = gCase;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getMood() {
		return mood;
	}

	public void setMood(String mood) {
		this.mood = mood;
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

	public String getPos() {
		return pos;
	}

	public void setPos(String pos) {
		this.pos = pos;
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

	public String getGrammar() {
		return grammar;
	}

	public void setGrammar(String grammar) {
		this.grammar = grammar;
	}

	public String getSeq() {
		return seq;
	}

	public void setSeq(String seq) {
		this.seq = seq;
	}
	
	public String toGrammarAbbreviations() {
		switch (this.pos) {
	      case ("ADJ"): {
	          return this.getGrammarForNounLikeWords();
	        }
	        case ("ADJ.COMP"): {
	          return this.getGrammarForNounLikeWords();
	        }
	        case ("ADJ.SUP.ABS"): {
	          return this.getGrammarForNounLikeWords();
	        }
	        case ("ADJ.SUP.REL"): {
	          return this.getGrammarForNounLikeWords();
	        }
	        case ("ART"): {
	          return this.getGrammarForNounLikeWords();
	        }
	        case ("ART.DEF"): {
	          return this.getGrammarForNounLikeWords();
	        }
	        case ("ART.INDF"): {
	          return this.getGrammarForNounLikeWords();
	        }
	        case ("INF"): {
	          return this.getGrammarForInfinitive();
	        }
	        case ("PART"): {
	          return this.getGrammarForParticiple();
	        }
	        case ("PRON"): {
	          return this.getGrammarForNounLikeWords();
	        }
	        case ("PRON.COR"): {
		          return this.getGrammarForNounLikeWords();
		        }
	        case ("PRON.DEF"): {
		          return this.getGrammarForNounLikeWords();
		        }
	        case ("PRON.DEM"): {
		          return this.getGrammarForNounLikeWords();
		        }
	        case ("PRON.INDF"): {
		          return this.getGrammarForNounLikeWords();
		        }
	        case ("PRON.PERS"): {
		          return this.getGrammarForNounLikeWords();
		        }
	        case ("PRON.POSS"): {
	          return this.getGrammarForNounLikeWords();
	        }
	        case ("PRON.Q"): {
		          return this.getGrammarForNounLikeWords();
		        }
	        case ("PRON.REFL"): {
		          return this.getGrammarForNounLikeWords();
		        }
	        case ("PRON.REL"): {
	          return this.getGrammarForNounLikeWords();
	        }
	        case ("NOUN"): {
	          return this.getGrammarForNounLikeWords();
	        }
	        case ("VERB"): {
	          return this.getGrammarForVerb();
	        }
	        default: {
	          return this.pos;
	        }
		}
	}
	
	private String getGrammarForInfinitive() {
	    return this.pos
	            + "."
	            + this.tense
	            + "."
	            + this.voice
	            ;
	}
	private String getGrammarForNounLikeWords() {
	    return this.pos
	            + "."
	            + this.number
	            + "."
	            + this.gender
	            + "."
	            + this.gCase
	            ;
	}
	private String getGrammarForParticiple() {
	    return this.pos
	            + "."
	            + this.voice
	            + "."
	            + this.tense
	            + "."
	            + this.gender
	            + "."
	            + this.number
	            + "."
	            + this.gCase
	            ;
	}
	private String getGrammarForVerb() {
	    return this.pos
	            + "."
	            + this.person
	            + "."
	            + this.number
	            + "."
	            + this.tense
	            + "."
	            + this.voice
	            + "."
	            + this.mood
	            ;
	    }

	public String getRefersTo() {
		return refersTo;
	}

	public void setRefersTo(String refersTo) {
		this.refersTo = refersTo;
	}

}
