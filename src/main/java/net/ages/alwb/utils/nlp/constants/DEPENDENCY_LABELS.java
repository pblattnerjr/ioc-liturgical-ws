package net.ages.alwb.utils.nlp.constants;

public enum DEPENDENCY_LABELS {
	ADV("ADV","adverb","")
	, APOS("APOS","apposing element","PM")
	, ATR("ATR","attributive","")
	, ATV("ATV","complement","")
	, AuxC("AuxC","conjunction","CONJ")
	, AuxG("AuxG","bracketing punctuation","")
	, AuxK("AuxK","terminal punctuation","")
	, AuxP("AuxP","preposition","PREP")
	, AuxV("AuxV","auxiliary verb","")
	, AuxX("AuxX","comma","PM")
	, AuxY("AuxY","sentence adverbial","")
	, AuxZ("AuxZ","emphasizing particle","")
	, COORD("COORD","coordinator","CONJ")
	, ExD("ExD","ellipsis","")
	, OBJ("OBJ","object","")
	, OCOMP("OCOMP","object complement","")
	, PNOM("PNOM","predicate nominal","")
	, PRED("PRED","predicate","")
	, ROOT("ROOT","root of a dependency tree","")
	, SBJ("SBJ","subject","")
	, ST("ST","sub-tree","")
	, TBD("TBD","to-be-determined","tbd")
	;
	public String keyname = "";
	public String description = "";
	public String parse = "";
	
	private DEPENDENCY_LABELS(
			String keyname
			, String description
			, String parse
			) {
		this.keyname = keyname;
		this.description = description;
		this.parse = parse;
	}
		
	/**
	 * Find the Enum value for this keyname
	 * @param name
	 * @return Entry
	 */
	public static DEPENDENCY_LABELS forName(String name) {
		for (DEPENDENCY_LABELS v : DEPENDENCY_LABELS.values()) {
			if (v.keyname.equals(name)) {
				return v;
			}
		}
		return DEPENDENCY_LABELS.TBD;
	}
}
