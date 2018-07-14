package net.ages.alwb.utils.nlp.utils;

import org.ocmc.ioc.liturgical.schemas.models.db.docs.nlp.UDtbWord;
import org.ocmc.ioc.liturgical.utils.ErrorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ages.alwb.tasks.UdTreebankDataCreateTask;

/**
 * Maps values from feats property of Universal Dependency analysis for a token
 * @author mac002
 *
 */
public class UDfeatsMapper {
	private static final Logger logger = LoggerFactory.getLogger(UDfeatsMapper.class);


	public static String partOfSpeech(String tag) {
		StringBuffer result = new StringBuffer();
		switch (tag) {
		case ("a"): {
			result.append("ADJ"); // adjective
			break;
		}
		case ("c"): {
			result.append("CONJ"); // conjunction
			break;
		}
		case ("d"): {
			result.append("ADV"); // adverb
			break;
		}
		case ("e"): {
			result.append("EXCLM"); // adverb
			break;
		}
		case ("g"): {
			result.append("PART"); // particle
			break;
		}
		case ("i"): {
			result.append("EXCLM"); // interjection (exclamation)
			break;
		}
		case ("l"): {
			result.append("ART"); // article
			break;
		}
		case ("m"): {
			result.append("NUM"); // numeral
			break;
		}
		case ("n"): {
			result.append("NOUN"); // noun
			break;
		}
		case ("p"): {
			result.append("PRON"); // pronoun
			break;
		}
		case ("r"): {
			result.append("PREP"); // preposition
			break;
		}
		case ("t"): {
			result.append("PTCP"); // participle
			break;
		}
		case ("u"): {
			result.append("PM"); // punctuation
			break;
		}
		case ("v"): {
			result.append("VERB"); // verb
			break;
		}
	}
		return result.toString();
	}
	
	public static String person(String tag) {
		StringBuffer result = new StringBuffer();
		switch (tag) {
		case ("1"): {
			result.append("1");
			break;
		}
		case ("2"): {
			result.append("2");
			break;
		}
		case ("3"): {
			result.append("3");
			break;
		}
	}
		return result.toString();
	}

	public static String number(String tag) {
		StringBuffer result = new StringBuffer();
		switch (tag) {
		case ("d"): {
			result.append("DU");
			break;
		}
		case ("p"): {
			result.append("PL");
			break;
		}
		case ("s"): {
			result.append("SG");
			break;
		}
	}
		return result.toString();
	}

	public static String tense(String tag) {
		StringBuffer result = new StringBuffer();
		switch (tag) {
		case ("a"): { // aorist
			result.append("AOR");
			break;
		}
		case ("f"): { // future
			result.append("FUT");
			break;
		}
		case ("i"): { // imperfect
			result.append("IMPRF");
			break;
		}
		case ("l"): { // pluperfect
			result.append("PLUP");
			break;
		}
		case ("p"): { // present
			result.append("PRS");
			break;
		}
		case ("r"): { // perfect
			result.append("PRF");
			break;
		}
		case ("t"): { // future perfect
			result.append("FUT.PRF");
			break;
		}
	}
		return result.toString();
	}
	
	public static String mood(String tag) {
		StringBuffer result = new StringBuffer();
		switch (tag) {
		case ("i"): { // indicative
			result.append("IND");
			break;
		}
		case ("m"): { // imperative
			result.append("IMP");
			break;
		}
		case ("n"): { // infinitive
			result.append("INF");
			break;
		}
		case ("o"): { // optative
			result.append("OPT");
			break;
		}
		case ("p"): { // participle
			result.append("PTCP");
			break;
		}
		case ("s"): { // subjunctive
			result.append("SBJV");
			break;
		}
	}
		return result.toString();
	}

	public static String voice(String tag) {
		StringBuffer result = new StringBuffer();
		if (tag.length() > 0) {
			switch (tag) {
			case ("a"): {
				result.append("ACT");
				break;
			}
			case ("p"): {
				result.append("PASS");
				break;
			}
			case ("m"): {
				result.append("MID");
				break;
			}
			case ("e"): {
				result.append("MP");
				break;
			}
			default: {
				result.append(tag);
			}
			}
		} else {
			result.append(tag);
		}
		return result.toString();
	}

	public static String gender(String tag) {
		StringBuffer result = new StringBuffer();
		switch (tag) {
		case ("f"): { // feminine
			result.append("F");
			break;
		}
		case ("m"): { // masculine
			result.append("M");
			break;
		}
		case ("n"): { // neuter
			result.append("N");
			break;
		}
		}
		return result.toString();
	}
	
	public static String gCase(String tag) {
		StringBuffer result = new StringBuffer();
		switch (tag) {
		case ("a"): { // accusative
			result.append("ACC");
			break;
		}
		case ("d"): { // dative
			result.append("DAT");
			break;
		}
		case ("g"): { // genitive
			result.append("GEN");
			break;
		}
		case ("l"): { // locative
			result.append("LOC");
			break;
		}
		case ("n"): { // nominative
			result.append("NOM");
			break;
		}
		case ("v"): { // vocative
			result.append("VOC");
			break;
		}
	}
		return result.toString();
	}

	public static String degree(String tag) {
		StringBuffer result = new StringBuffer();
		switch (tag) {
		case ("c"): { // comparative
			result.append("COMP");
			break;
		}
		case ("s"): { // superlative
			result.append("SUP");
			break;
		}
	}
		return result.toString();
	}

	public static String featsToGrammar(UDtbWord word) {
		StringBuffer sb = new StringBuffer();
		if (word.getFeats().length() > 0 && ! word.getFeats().equals("_")) {
			sb.append(word.getPos());
			String [] features = word.getFeats().split("\\|");
			for (String feat : features) {
				String [] featKv = feat.split("="); 
				try {
					String value = featKv[1];
					sb.append(".");
					sb.append(value.toUpperCase());
				} catch (Exception e) {
					ErrorUtils.report(logger, e);
				}
			}
		}
		return sb.toString();
	}
	public static UDtbWord featsToGrammarWithSwitch(UDtbWord word) {
		/**
		 * TODO:
		 * 1. map the value to a normalized form.
		 * 2. set the property in the UDtbWord.
		 * 3. set the grammar based on the values in UDtbWord.
		 */
			String [] features = word.getFeats().split("|");
			for (String feat : features) {
				String [] featKv = feat.split("="); 
				try {
					String feature = featKv[0];
					String value = featKv[1];
					switch (feature) {
					case("Aspect"): {
						/**
						 * Imp, Perf
						 */
						break;
					}
					case("Case"): {
						// Acc, Dat, Gen, Nom, Voc
						word.setgCase(value.toUpperCase());
						break;
					}
					case ("Definite") : {
						break;
					}
/**
"Definite=Def"
"Definite=Ind"
"Degree=Cmp"
"Degree=Pos"
"Degree=Sup"
"Foreign=Yes"
"Gender=Fem"
"Gender=Masc"
"Gender=Neut"
"Mood=Imp"
"Mood=Ind"
"Mood=Opt"
"Mood=Sub"
"NumType=Card"
"NumType=Ord"
"Number=Dual"
"Number=Plur"
"Number=Sing"
"Person=1"
"Person=2"
"Person=3"
"Poss=Yes"
"PronType=Art"
"PronType=Dem"
"PronType=Ind"
"PronType=Prs"
"PronType=Rel"
"Tense=Fut"
"Tense=Past"
"Tense=Pqp"
"Tense=Pres"
"VerbForm=Conv"
"VerbForm=Fin"
"VerbForm=Ger"
"VerbForm=Inf"
"VerbForm=Part"
"Voice=Act"
"Voice=Mid"
"Voice=Pass"
"_"
						 */
					}
				} catch (Exception e) {
					ErrorUtils.report(logger, e);
				}
			}
			return word;
		}

	}
