package net.ages.alwb.utils.nlp.utils;

public class PerseusPostagMapper {

	public static String postagPartOfSpeech(String postag) {
		String tag = String.valueOf(postag.charAt(0));
		return PerseusPostagMapper.partOfSpeech(tag);
	}

	public static String postagPerson(String postag) {
		String tag = String.valueOf(postag.charAt(1));
		return PerseusPostagMapper.person(tag);
	}

	public static String postagNumber(String postag) {
		String tag = String.valueOf(postag.charAt(2));
		return PerseusPostagMapper.number(tag);
	}
	
	public static String postagTense(String postag) {
		String tag = String.valueOf(postag.charAt(3));
		return PerseusPostagMapper.tense(tag);
	}

	public static String postagMood(String postag) {
		String tag = String.valueOf(postag.charAt(4));
		return PerseusPostagMapper.mood(tag);
	}

	public static String postagVoice(String postag) {
		String tag = String.valueOf(postag.charAt(5));
		return PerseusPostagMapper.voice(tag);
	}

	public static String postagGender(String postag) {
		String tag = String.valueOf(postag.charAt(6));
		return PerseusPostagMapper.gender(tag);
	}

	public static String postagCase(String postag) {
		String tag = String.valueOf(postag.charAt(7));
		return PerseusPostagMapper.gCase(tag);
	}

	public static String postagDegree(String postag) {
		String tag = String.valueOf(postag.charAt(8));
		return PerseusPostagMapper.degree(tag);
	}



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

	/**
		 * Issues:
		 * 1. Infinitive shows up as a mood
		 * 2. Participle shows up as both part of speech and mood
		 * @param postag
		 * @return
		 */
		public static String postagToGrammar(String postag) {
			StringBuffer result = new StringBuffer();
			if (postag.length() == 9) { // well formed
				for (int i=0; i < 9; i++) {
					String s = String.valueOf(postag.charAt(i));
					switch (i) {
						case 0: { // part of speech
							result.append(partOfSpeech(s));
							break;
					}
					case 1: { // person
						String tag = person(s);
						if (tag.length() > 0) {
							result.append(".");
							result.append(tag);
						}
						break;
					}
					case 2: { // number
						String tag = number(s);
						if (tag.length() > 0) {
							result.append(".");
							result.append(tag);
						}
						break;
					}
					case 3: { // tense
						String tag = tense(s);
						if (tag.length() > 0) {
							result.append(".");
							result.append(tag);
						}
						break;
					}
					case 4: { // mood
						String tag = mood(s);
						if (tag.length() > 0) {
							result.append(".");
							result.append(tag);
						}
						break;
					}
					case 5: { // voice
						String tag = voice(s);
						if (tag.length() > 0) {
							result.append(".");
							result.append(tag);
						}
						break;
					}
					case 6: { // gender
						String tag = gender(s);
						if (tag.length() > 0) {
							result.append(".");
							result.append(tag);
						}
						break;
					}
					case 7: { // case
						String tag = gCase(s);
						if (tag.length() > 0) {
							result.append(".");
							result.append(tag);
						}
						break;
					}
					case 8: { // degree
						String tag = degree(s);
						if (tag.length() > 0) {
							result.append(".");
							result.append(tag);
						}
						break;
					}
					}
				}
			} else {
				// TODO: report as error.
			}
			return result.toString();
		}

	}
