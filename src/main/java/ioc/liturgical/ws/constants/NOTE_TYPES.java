package ioc.liturgical.ws.constants;


import java.util.ArrayList;
import java.util.List;

import org.ocmc.ioc.liturgical.schemas.models.DropdownItem;

import com.google.gson.JsonArray;

/**
 *
 * Grammatical Parts of Speech used with Greek
 * 
 * @author mac002
 *
 */
public enum NOTE_TYPES {

ADVICE_FOR_TRANSLATORS("TNAFT","Advice for Translators")
, ADVICE_FOR_TRANSLATION_CHECKERS("TNAFC","Advice for Translation Checkers")
, ANCIENT_CULTURES("TNAC","Ancient Cultures")
, ANCIENT_TEXTS("TNAT","Ancient Texts")
, BIBLICAL_INTERTEXTUALITY("TNBI","Biblical Intertextuality")
, CHRISTIAN_TRADITION_PATRISTIC_COMMENTARTIES("TNCTPC","Christian Tradition - Patristic Commentaries")
, CHRISTIAN_TRADITION_THEOLOGICAL_WORKS("TNCTTW","Christian Tradition - Theological Works")
, CHRISTIAN_TRADITION_HAPGIOGRAPHA("TNCTH","Christian Tradition - Hagiographa")
, COMPARISON_OF_VERSIONS("TNCOV","Comparison of Versions")
, EXEGETICAL("TNEXE","Exegetical")
, GENERAL("TNGEN","General")
, GEOGRAPHY("TNGEO","Geography")
, LINGUISTICS_GRAMMAR("TNLINGGRAM","Linguistics - Grammar")	
, LINGUISTICS_GRAMMAR_DISCOURSE("TNLINGGRAMDIS","Linguistics - Grammar - Discourse")	
, LINGUISTICS_LEXICAL("TNLINGLEX","Linguistics - Lexical")
, LINGUISTICS_LEXICAL_ETYMOLOGY("TNLINGLEXETY","Linguistics - Lexical Etymology")
, LINGUISTICS_LEXICAL_MORPHOLOGY("TNLINGLEXMOR","Linguistics - Lexical Morphology")
, LINGUISTICS_LEXICAL_SEMANTICS("TNLINGLEXSEM","Linguistics - Lexical Semantics")
, LINGUISTICS_PRAGMATICS("TNLINGPRAG","Linguistics - Pragmatics")
, LINGUISTICS_GRAMMAR_SYNTAX("TNLINGGRAMSYN","Linguistics - Grammar - Syntax")
, HISTORY("TNH","History")
, ISLAM("TNI","Islam")
, JEWISH_TRADITION("TNJT","Jewish Tradition")
, LAW("TNL","Law")
, LITERARY_DEVICES("TNLD","Literary Devices")	
, LITERARY_GENRE("TNLG","Literary Genre")
, LITERATURE("TNLIT","Literature")
, LITURGICAL_RUBRIC("TNLITURGYRUBRIC","Liturgical - Rubric")
, LITURGICAL_USAGE("TNLITURGYUSE","Liturgical - Usage")
, LITURGIES("TNLITURGY","Liturgies")
, MUSIC("TNMU","Music")
, MYSTICISM("TNMY","Mysticism")
, ONTOLOGY("TNONTO", "Ontology")
, ONTOLOGY_GOD("TNONTOGOD", "Ontology - God")
, ONTOLOGY_ANIMAL("TNONTOANIMAL", "Ontology - Animal")
, ONTOLOGY_BEING("TNONTOBEING", "Ontology - Being")
, ONTOLOGY_CONCEPT("TNONTOCONC", "Ontology - Concept")
, ONTOLOGY_EVENT("TNONTOEVENT", "Ontology - Event")
, ONTOLOGY_GROUP("TNONTOGROUP", "Ontology - Group")
, ONTOLOGY_HUMAN("TNONTOHUM", "Ontology - Human")
, ONTOLOGY_MYSTERY("TNONTOMYS", "Ontology - Mystery")
, ONTOLOGY_OBJECT("TNONTOOBJ", "Ontology - Object")
, ONTOLOGY_PLACE("TNONTOPLACE", "Ontology - Place")
, ONTOLOGY_PLANT("TNONTOPLANT", "Ontology - Plant")
, ONTOLOGY_ROLE("TNONTOROLE", "Ontology - Role")
, PERITESTMENTAL_LITERATURE("TNPL","Peritestamental Literature")
, PHILOSOPHY("TNPHILO","Philosophy")
, MEANING_PLAIN("TNMP","Meaning - Plain")
, MEANING_ALLEGORICAL("TNMA","Meaning - Allegorical")
, MEANING_TYPOLOGICAL("TNMT","Meaning - Typological")
, MEANING_SPIRITUAL("TNMS","Meaning - Spiritual")
, PSYCHOLOGY("TNPSY","Psychology")
, SUGGESTIONS_FOR_READING("TNSFR","Suggestions for Reading") 
, SYNOPTIC_READING("TNSR","Synoptic Reading")
, TEXT_WITNESS("TNTC","Text Witness")
, TEXTUAL_CRITICISM("TNTC","Textual Criticism") 
, THEOLOGY("TNTHEO","Theology")
, TRANSLATORS_NOTE("TNTN","Translator's Note")
, VOCABULARY("TNV","Vocabulary")
	;

	public String keyname = "";
	public String fullname = "";
	
	private NOTE_TYPES(
			String keyname
			, String fullname
			) {
		this.keyname = keyname;
		this.fullname = fullname;
	}

	/**
	 * Converts the enumeration a JsonArray of DropdownItems
	 * where the dropdown item's
	 * value = enum.name
	 * label = enum.keyname
	 * 
	 * @return a JsonArray of Dropdowns
	 */
    public static JsonArray toDropdownJsonArray() {
    	JsonArray result = new JsonArray();
    	for (NOTE_TYPES e : NOTE_TYPES.values()) {
    		result.add(new DropdownItem(e.keyname, e.name()).toJsonObject());
    	}
    	return result;
    }

	/**
	 * Converts the enumeration a List of DropdownItems
	 * where the dropdown item's
	 * value = enum.name
	 * label = enum.keyname
	 * 
	 * @return a list of dropdowns
	 */
    public static List<DropdownItem> toDropdownList() {
    	List<DropdownItem> result = new ArrayList<DropdownItem>();
    	for (NOTE_TYPES e : NOTE_TYPES.values()) {
    		result.add(new DropdownItem(e.keyname, e.name()));
    	}
    	return result;
    }

		
}
