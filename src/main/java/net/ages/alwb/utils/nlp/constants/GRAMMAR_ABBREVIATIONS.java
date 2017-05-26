package net.ages.alwb.utils.nlp.constants;

public enum GRAMMAR_ABBREVIATIONS {
	P1("1", "first person ")
	,P2("2", "second person ")
	,P3("3", "third person ")
	,ACC("ACC", "accusative ")
	,ACT("ACT", "active ")
	,ADJ("ADJ", "adjective")
	,ADV("ADV", "adverb(ial)")
	,AOR("AOR", "aorist")
	,ART("ART", "article")
	,COMP("COMP", "comparative")
	,CONJ("CONJ", "conjunction")
	,DAT("DAT", "dative") 
	,DEF("DEF", "definite") 
	,DEM("DEM", "demonstrative") 
	,DET("DET", "determiner")
	,DU("DU", "dual") 
	,EXCLAMATION("EXCLM", "exclamation") 
	,FEMININE("F", "feminine")
	,FUT("FUT", "future")
	,GEN("GEN", "genitive")
	,IMP("IMP", "imperative")
	,IMPRF("IMPRF", "imperfect")
	,IND("IND", "indicative")
	,INDF("INDF", "indefinite")
	,INF("INF", "infinitive")
	,IPFV("IPFV", "imperfective")
	,MASCULINE("M", "masculine")
	, MIDDLE("MID", "middle")
	,NEUTER("N", "neuter")
	,NEG("NEG", "negation, negative")
	,NOM("NOM", "nominative")
	,NOUN("NOUN", "noun ")
	,NUM("NUM", "number")
	,OPTATIVE("OPT", "optative")
	,PART("PART", "particle")
	,PASS("PASS", "passive")
	,PFV("PFV", "perfective")
	,PL("PL", "plural")
	,POSI("POSI", "positive")
	,POSS("POSS", "possessive")
	,PM("PM", "punctuation mark")
	,PREP("PREP", "preposition")
	,PRF("PRF", "perfect")
	,PROC("PROC", "proclitic")
	,PRON("PRON", "pronoun")
	,PRS("PRS", "present")
	,PST("PST", "past")
	,PTCP("PTCP", "participle")
	,Q("Q", "question particle/marker")
	,REL("REL", "relative")
	,SBJV("SBJV", "subjunctive")
	,SG("SG", "singular")
	,SUP("SUP", "superlative")
	,VERB("VERB", "verb")
	,VOC("VOC", "vocative ")
	, TBD("TBD","to-be-determined")
	, EMPTY("", "not used")
	;
	public String keyname = "";
	public String description = "";
	
	private GRAMMAR_ABBREVIATIONS(
			String keyname
			, String description
			) {
		this.keyname = keyname;
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
}
