package net.ages.alwb.utils.nlp.constants;

/**
 * Maps various abbreviations from other sources to the
 * the ones used by the liturgical-ws.
 * 
 * Penn
 * 		Issue: they use IN for both preposition and subordinating conjunction
 * @see http://www.comp.leeds.ac.uk/amalgam/tagsets/upenn.html
 * @author mac002
 *
 */
public enum GRAMMAR_ABBREVIATIONS {
	P1("1", "", "", "", "", "first person ")
	,P2("2",  "", "", "", "", "second person ")
	,P3("3",  "", "", "", "", "third person ")
	,ACC("ACC",  "", "", "", "", "accusative ")
	,ACT("ACT",  "", "", "", "", "active ")
	,ADJ("ADJ",  "", "3", "a", "JJ", "adjective")
	,ADJ_COMP("ADJ.COMP",  "", "3", "a", "JJR", "adjective")
	,ADJ_SUPER("ADJ.SUP",  "", "3", "a", "JJS", "adjective")
	,ADV("ADV",  "", "4", "r", "RB", "adverb(ial)")
	,ADV_COMP("ADV.COMP",  "", "4", "r", "RBR", "adverb, comparative")
	,ADV_SUP("ADV.SUP",  "", "4", "r", "RBS", "adverb, superlative")
	,ADV_WH("ADV.WH",  "", "4", "r", "WRB", "WH-adverb")
	,AOR("AOR",  "", "", "", "", "aorist")
	,ART("ART",  "", "", "", "", "article")
	,COMP("COMP",  "", "", "", "", "comparative")
	,CONJ("CONJ",  "", "", "", "", "conjunction")
	,CONJ_CRD("CONJ.CRD",  "", "", "", "CC", "coordinating conjunction")
	,CRD("CRD",  "", "", "", "", "coordination/coordinating")
	,DAT("DAT",  "", "", "", "", "dative") 
	,DEF("DEF",  "", "", "", "", "definite") 
	,DEM("DEM",  "", "", "", "", "demonstrative") 
	,DET("DET",  "", "", "", "DT", "determiner")
	,DET_WH("DET.WH",  "", "", "", "WDT", "WH-determiner")
	,DU("DU",  "", "", "", "", "dual") 
	,EXCLAMATION("EXCLM",  "", "", "", "UH", "exclamation") 
	,FEMININE("F",  "", "", "", "", "feminine")
	,FUT("FUT",  "", "", "", "", "future")
	,GEN("GEN",  "", "", "", "", "genitive")
	,IMP("IMP",  "", "", "", "", "imperative")
	,IMPRF("IMPRF",  "", "", "", "", "imperfect")
	,IND("IND",  "", "", "", "", "indicative")
	,INDF("INDF",  "", "", "", "", "indefinite")
	,INF("INF",  "", "", "", "", "infinitive")
	,IPFV("IPFV",  "", "", "", "", "imperfective")
	,MASCULINE("M",  "", "", "", "", "masculine")
	, MIDDLE("MID",  "", "", "", "", "middle")
	,NEUTER("N",  "", "", "", "", "neuter")
	,NEG("NEG",  "", "", "", "", "negation, negative")
	,NOM("NOM",  "", "", "", "", "nominative")
	,NOUN("NOUN",  "", "1", "n", "NN", "noun ")
	,NOUN_PL("NOUN.PL",  "", "1", "n", "NNS", "noun, plural ")
	,NOUN_PROP_SG("NOUN.PROP.SG",  "", "1", "n", "NNP", "noun, proper, plural ")
	,NOUN_PROP_PL("NOUN.PROP.PL",  "", "1", "n", "NNPS", "noun, proper, plural ")
	,NUM("NUM",  "", "", "", "CD", "number")
	,OPTATIVE("OPT",  "", "", "", "", "optative")
	,PART("PART",  "", "", "", "RP", "particle")
	,PASS("PASS",  "", "", "", "", "passive")
	,PERS("PERS",  "", "", "", "", "personal")
	,PFV("PFV",  "", "", "", "", "perfective")
	,PL("PL",  "", "", "", "", "plural")
	,POSI("POSI",  "", "", "", "", "positive")
	,POSS("POSS",  "", "", "", "POS", "possessive")
	,PM("PM",  "", "", "", "", "punctuation mark")
	,PREP("PREP",  "", "", "", "", "preposition")
	,PREP_OR_SUB_CONJ("PREP.or.CONJ.SUB",  "", "", "", "IN", "preposition")
	,PRF("PRF",  "", "", "", "", "perfect")
	,PROC("PROC",  "", "", "", "", "proclitic")
	,PRON("PRON",  "", "", "", "", "pronoun")
	,PRON_PERS("PRON.PERS",  "", "", "", "PRP", "pronoun")
	,PRON_POSS("PRON.POSS",  "", "", "", "PRP$", "pronoun")
	,PRON_WH("PRON.WH",  "", "", "", "WP", "WH-pronoun")
	,PRON_WH_POSS("PRON.WH.POSS",  "", "", "", "WP$", "WH-pronoun, possessive")
	,PROP("PROP",  "", "", "", "", "proper")
	,PRS("PRS",  "", "", "", "", "present")
	,PST("PST",  "", "", "", "", "past")
	,PTCP("PTCP",  "", "", "", "", "participle")
	,Q("Q",  "", "", "", "", "question particle/marker")
	,REL("REL",  "", "", "", "", "relative")
	,SBJV("SBJV",  "", "", "", "", "subjunctive")
	,SG("SG",  "", "", "", "", "singular")
	,SUB("SUB",  "", "", "", "", "subordinating")
	,SUP("SUP",  "", "", "", "", "superlative")
	,SYM("SYM",  "", "", "", "SYM", "symbol")
	,VERB("VERB",  "", "2", "v", "VB", "verb")
	,VERB_PST("VERB",  "", "2", "v", "VBD", "verb, past tense")
	,VERB_PRS_PART("VERB.PRS.PART",  "", "2", "v", "VBG", "verb, present participle or gerund")
	,VERB_PST_PART("VERB.PST.PART",  "", "2", "v", "VBG", "verb, past participle")
	,VERB_PST_NOT_P3("VERB.PST",  "", "2", "v", "VBP", "verb, present tense, not 3rd person singular")
	,VERB_PST_P3("VERB.3.PST",  "", "2", "v", "VBZ", "verb, present tense, 3rd person singular")
	,VOC("VOC",  "", "", "", "", "vocative")
	, WH("WH",  "", "", "", "", "wh(o), wh(ich), wh(ere), etc.")
	, TBD("TBD", "", "", "", "", "to-be-determined")
	, EMPTY("",  "", "", "", "", "empty, not defined")
	;
	public String keyname = "";
	public String description = "";
	public String wordnetNbr = "";
	public String wordnetAbr = "";
	public String oxford = "";
	public String penn = "";
	
	private GRAMMAR_ABBREVIATIONS(
			String keyname
			, String oxford // Oxford Learner's dictionary pos abbreviations
			, String wordnetNbr // numeric code for WordNet pos 
			, String wordnetAbr // string code for WordNet pos
			, String penn // Penn tree bank abbreviations
			, String description
			) {
		this.keyname = keyname;
		this.oxford = oxford;
		this.wordnetAbr = wordnetAbr;
		this.wordnetNbr = wordnetNbr;
		this.penn = penn;
		this.description = description;
	}
		
	/**
	 * Find the Enum value for this keyname
	 * @param name
	 * @return Entry
	 */
	public static GRAMMAR_ABBREVIATIONS forName(String name) {
		for (GRAMMAR_ABBREVIATIONS v : GRAMMAR_ABBREVIATIONS.values()) {
			if (v.keyname.equals(name)) {
				return v;
			}
		}
		return GRAMMAR_ABBREVIATIONS.TBD;
	}
	
	/**
	 * Find the Enum value for this penn code
	 * @param name
	 * @return Entry
	 */
	public static GRAMMAR_ABBREVIATIONS forNamePenn(String name) {
		for (GRAMMAR_ABBREVIATIONS v : GRAMMAR_ABBREVIATIONS.values()) {
			if (v.penn.equals(name)) {
				return v;
			}
		}
		return GRAMMAR_ABBREVIATIONS.EMPTY;
	}

	/**
	 * Find the Enum value for this penn code
	 * @param name
	 * @return Entry
	 */
	public static GRAMMAR_ABBREVIATIONS forNameWnCode(String name) {
		for (GRAMMAR_ABBREVIATIONS v : GRAMMAR_ABBREVIATIONS.values()) {
			if (v.wordnetAbr.equals(name)) {
				return v;
			}
		}
		return GRAMMAR_ABBREVIATIONS.EMPTY;
	}
	/**
	 * Find the Enum value for this WordNet int code (stored as a string, though)
	 * @param name
	 * @return Entry
	 */
	public static GRAMMAR_ABBREVIATIONS forNameWnNbr(String name) {
		for (GRAMMAR_ABBREVIATIONS v : GRAMMAR_ABBREVIATIONS.values()) {
			if (v.wordnetNbr.equals(name)) {
				return v;
			}
		}
		return GRAMMAR_ABBREVIATIONS.EMPTY;
	}
}
