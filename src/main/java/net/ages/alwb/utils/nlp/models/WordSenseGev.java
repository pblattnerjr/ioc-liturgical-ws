package net.ages.alwb.utils.nlp.models;

import com.google.gson.annotations.Expose;

import ioc.liturgical.ws.constants.Constants;
import ioc.liturgical.ws.constants.db.external.LIBRARIES;
import ioc.liturgical.ws.constants.db.external.TOPICS;
import net.ages.alwb.utils.core.datastores.json.exceptions.BadIdException;
import net.ages.alwb.utils.core.datastores.json.models.LTK;

/**
 * Word sense used by the Global English Version translations of the liturgical texts
 * @author mac002
 *
 */
public class WordSenseGev extends LTK {

	private static String schema = WordSenseGev.class.getSimpleName();
	private static double serialVersion = 1.1;
	private static TOPICS topic = TOPICS.WORD_SENSE_GEV;

	@Expose String lemma = ""; 
	@Expose String pos = "";
	@Expose String context = "tbd";
	@Expose String def = "";
	@Expose String senseNbr = "";
	@Expose String source = "";
	
	public WordSenseGev(
			String lemma
			, String pos
			, String senseNbr
			, boolean prettyprint
			) throws BadIdException {
			super(
					LIBRARIES.LINGUISTICS.toSystemDomain()
					, lemma
					, pos + Constants.DOMAIN_DELIMITER + senseNbr
					);
		this.setPrettyPrint(prettyprint);
		this.lemma = lemma;
		this.pos = getPosAbbreviation(pos);
		this.senseNbr = senseNbr;
	}

	/**
	 * Convert the full name of the part of speech to the appropriate abbreviation
	 * If not found, the POS will be set to TBD
	 * @return
	 */
	private String getPosAbbreviation(String pos) {
		String result = "";
		switch (pos) {
		case ("abbreviation"):  {
			result = "ABRV";
			break;
		}
		case ("adjective"):  {
			result = "ADJ";
			break;
		}
		case ("adverb"):  {
			result = "ADV";
			break;
		}
		case ("auxiliary verb"):  {
			result = "VERB.AUX";
			break;
		}		
		case ("combining form"):  { // A combining form is a form of a word that only appears as part of another word
			result = "COMF";
			break;
		}
		case ("conjunction"):  {
			result = "CONJ";
			break;
		}
		case ("definite article"):  {
			result = "ART.DEF";
			break;
		}
		case ("determiner"):  {
			result = "DET";
			break;
		}
		case ("determiner, pronoun"):  {
			result = "DET.PRON";
			break;
		}
		case ("exclamation"):  {
			result = "EXCLM";
			break;
		}
		case ("indefinite article"):  {
			result = "ART.INDF";
			break;
		}
		case ("infinitive marker"):  {
			result = "INF.MRK";
			break;
		}
		case ("linking verb"):  {
			result = "VERB.LNKG";
			break;
		}
		case ("modal verb"):  {
			result = "VERB.MOD";
			break;
		}
		case ("noun"):  {
			result = "NOUN";
			break;
		}
		case ("number"):  {
			result = "NUM";
			break;
		}
		case ("ordinal number"):  {
			result = "NUM.ORD";
			break;
		}
		case ("phrasal verb"):  {
			result = "VERB.";
			break;
		}
		case ("prefix"):  {
			result = "PRFX";
			break;
		}
		case ("preposition"):  {
			result = "PREP";
			break;
		}
		case ("pronoun"):  {
			result = "PRON";
			break;
		}
		case ("pronoun, determiner"):  {
			result = "PRON.DET";
			break;
		}
		case ("suffix"):  {
			result = "SUF";
			break;
		}
		case ("symbol"):  {
			result = "SYMB";
			break;
		}
		case ("verb"):  {
			result = "VERB";
			break;
		}
		default: {
			result = "TBD";
		}
		}
		return result;
	}

	public String getDef() {
		return def;
	}

	public void setDef(String def) {
		this.def = def;
	}

	public String getLemma() {
		return lemma;
	}

	public void setLemma(String lemma) {
		this.lemma = lemma;
	}

	public String getPos() {
		return pos;
	}

	public void setPos(String pos) {
		this.pos = pos;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getSenseNbr() {
		return senseNbr;
	}

	public void setSenseNbr(String senseNbr) {
		this.senseNbr = senseNbr;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
}
