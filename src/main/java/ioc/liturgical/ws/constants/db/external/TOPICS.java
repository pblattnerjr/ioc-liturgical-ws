package ioc.liturgical.ws.constants.db.external;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import com.google.common.base.Joiner;
import com.google.gson.JsonArray;

import ioc.liturgical.ws.constants.Constants;
import net.ages.alwb.utils.core.datastores.json.models.DropdownItem;

/**
 * Hierarchical specification of topics used in the database.
 * Each topic becomes a label if used with a node.
 * Each hyponymn also becomes a label.
 * The labels are added from the root down.
 * @author mac002
 *
 */
public enum TOPICS {
	ROOT("Root", "The root node of the topics.", null)
	, COMMENTS_ROOT(
			"CommentsRoot"
			, "The root node for comments about a text or a relationship."
			, TOPICS.ROOT
			)
	, LINGUISTICS_ROOT(
			"LinguisticsRoot"
			, "The root node for Information about a language: its lexicon, semantics, phonology, morphology, grammar."
			, TOPICS.ROOT
			)
	, NOTES_ROOT(
			"NoteRoot"
			, "The root node for personal notes made by a user."
			, TOPICS.ROOT
			)
	, ONTOLOGY_ROOT("OntologyRoot", "The root node of the text ontology.", ROOT)
	, TABLES_ROOT("TablesRoot", "The root node of the Tables.", ROOT)
	, GOD(
			"God"
			, "The creator of all that exists."
			, TOPICS.ONTOLOGY_ROOT
			)
	, BEING(
			"Being"
			, "A living, created entity such as an angel, a human being, a plant, or an animal."
			, TOPICS.ONTOLOGY_ROOT
			)
	, ANIMAL(
			"Animal"
			, "A being that is a living organizm capable of independent movement.",
			TOPICS.BEING
			)
	, CONCEPT(
			"Concept"
			,"An abstract concept about something."
			, TOPICS.ONTOLOGY_ROOT
			)
	, EVENT(
			"Event"
			, "Something that happens or occurs."
			, TOPICS.ONTOLOGY_ROOT
			)
	, GRAMMAR(
			"Grammar"
			, "Linguistic information about a word or its relationship to other words"
			, TOPICS.LINGUISTICS_ROOT
			)
	, GROUP(
			"Group"
			, "A collection of people."
			, TOPICS.ONTOLOGY_ROOT
			)
	, HUMAN(
			"Human"
			, "A human being."
			, TOPICS.BEING
			)
	, LEXICAL_ITEM(
			"LexicalItem"
			, "The lemmas and forms of a language"
			, TOPICS.LINGUISTICS_ROOT
			)
	, MYSTERY(
			"Mystery"
			, "Baptism, Chrismation, Confession, Marriage, Holy Communion, Holy Orders, or Unction."
			, TOPICS.CONCEPT
			)
	, OBJECT(
			"Object"
			, "A non-living entity, e,g, an artifact or tool."
			, TOPICS.ONTOLOGY_ROOT
			)
	, PLANT(
			"Plant"
			, "A being that is a living organizm not capable of independent movement."
			, TOPICS.BEING
			)
	, PLACE(
			"Place"
			, "A celestial or geographic location."
			, TOPICS.ONTOLOGY_ROOT
			)
	, ROLE(
			"Role"
			, "A role that entity can play or be."
			, TOPICS.ONTOLOGY_ROOT
			)
	, TABLE_LEXICON(
			"LexiconTable"
			, "A lexicon containing lemmas and their senses"
			, TOPICS.TABLES_ROOT
			)
	, TEXT(
			"Text"
			, "A document containing written words."
			, TOPICS.ONTOLOGY_ROOT
			)
	, TEXT_CONCORDANCE(
			"TextConcordance"
			, "A document containing a substring from a text, used as a line in a concordance."
			, TOPICS.LINGUISTICS_ROOT
			)
	, TEXT_BIBLICAL(
			"Biblical"
			, "Biblical text."
			, TOPICS.TEXT
			)
	, TEXT_LITURGICAL(
			"Liturgical"
			, "Liturgical text."
			, TOPICS.TEXT
			)
	, TEXT_SPEECH(
			"Speech"
			, "Text that records a speech made by someone."
			, TOPICS.TEXT
			)
	, WORDNET(
			"WordNet"
			, "Lexical database for English, developed by Princeton University."
			, TOPICS.LEXICAL_ITEM
			)
	, WN_LEXICAL_ENTRY(
			"WnLexicalEntry"
			, "The dictionary lookup form for a word used by WordNet. It is the lemma plus the part of speech abbreviation."
			, TOPICS.WORDNET
			)
	, WN_LEXICAL_LEMMA(
			"WnLexicalEntry"
			, "The dictionary lookup form for a word used by WordNet."
			, TOPICS.WORDNET
			)
	, WN_LEXICAL_SENSE(
			"WnLexicalSense"
			, "The sense of a word used by WordNet."
			, TOPICS.WORDNET
			)
	, WN_LEXICAL_SYNSET(
			"WnLexicalSynset"
			, "A WordNet synset."
			, TOPICS.WORDNET
			)
	, WORD_CONTEXT(
			"WordContext"
			, "The semantic context for the sense of a word "
			, TOPICS.LEXICAL_ITEM
			)
	, WORD_INFLECTED(
			"WordInflected"
			, "The inflected form a lemma can take, e.g. the lemma λόγος can have the form λόγον "
			, TOPICS.LEXICAL_ITEM
			)
	, WORD_GRAMMAR(
			"WordGrammar"
			, "Grammatical Information about a word"
			, TOPICS.GRAMMAR
			)
	, WORD_LEMMA(
			"WordLemma"
			, "The Greek form of a word that is used to look it up in a lexicon, e.g. the inflected form λόγον is found in a lexicon as the lemma λόγος."
			, TOPICS.LEXICAL_ITEM
			)
	, WORD_LEMMA_GEV(
			"WordLemmaGev"
			, "The dictionary lookup form for a word used by the Global English Version translation of the liturgical texts."
			, TOPICS.LEXICAL_ITEM
			)
	, WORD_SENSE(
			"WordSense"
			, "The Greek sense of a word "
			, TOPICS.LEXICAL_ITEM
			)
	, WORD_SENSE_GEV(
			"WordSenseGev"
			, "The  sense of a word used by the Global English Version translation of the liturgical texts."
			, TOPICS.LEXICAL_ITEM
			)
	;

	public String label = "";
	public String description = "";
	public TOPICS hyponym;
	
	private TOPICS(
			String label
			, String description
			, TOPICS hyponym
			) {
		this.label = label;
		this.description = description;
		this.hyponym = hyponym;
	}
		
	/**
	 * Find the Topic for this string
	 * @param topicname
	 * @return ONTOLOGY_TOPICS topic
	 */
	public static TOPICS forName(String name) {
		for (TOPICS t : TOPICS.values()) {
			if (t.label.equals(name)) {
				return t;
			}
		}
		return null;
	}
	
	/**
	 * Creates a delimited string id for this topic, using the supplied library and key 
	 * @param library
	 * @param user
	 * @return
	 */
	public String toId(String library, String key) {
		return Joiner.on(Constants.ID_DELIMITER).join(library, this.label, key);
	}

	/**
	 * Gets a colon delimited set of strings that represent
	 * the ontology hierarchy for this entry,
	 * from more generic to less.
	 * @return
	 */
	public String toDelimitedLabels() {
		return toLabels(this);
	}

	/**
	 * Returns the ontology hierarchy for this entry,
	 * from more generic to less.
	 * @return
	 */
	public List<String> toLabelsList() {
		List<String> result = new ArrayList<String>();
		String [] labels = toLabels(this).split(":");
		for (String label : labels) {
			result.add(label);
		}
		return result;
	}

	private String toLabels(TOPICS topic) {
		StringBuffer result = new StringBuffer();
		if (topic == TOPICS.ROOT) {
			result.append(topic.label);
		} else {
			result.append(toLabels(topic.hyponym));
			if (result.length() > 0) {
				result.append(":");
			}
			result.append(topic.label);
		}
		return result.toString();
	}
	
	/**
	 * Get the keynames as a sorted list of DropDownItem
	 * @return
	 */
	public static List<DropdownItem> keyNamesToDropdown() {
		List<DropdownItem> result = new ArrayList<DropdownItem>();
		TreeSet<String> values = new TreeSet<String>();
		for (TOPICS t : TOPICS.values()) {
			values.add(t.label);
		}
		for (String value : values) {
			result.add(new DropdownItem(value, value));
		}
		return result;
	}

	/**
	 * Get the keynames as a JsonArray of DropDownItem
	 * @return
	 */
	public static JsonArray topicLabelsToJsonArrayDropdown() {
		JsonArray result = new JsonArray();
		TreeSet<String> values = new TreeSet<String>();
		for (TOPICS t : TOPICS.values()) {
			values.add(t.label);
		}
		for (String value : values) {
			result.add(new DropdownItem(value, value).toJsonObject());
		}
		return result;
	}
	
	public static TOPICS getHyponym(TOPICS topic) {
		return topic.hyponym;
	}
	
	/**
	 * Walk the hierarchy to get the subroot (the one before
	 * the topmost ROOT), e.g. ONTOLOGY_ROOT
	 * @param topic
	 * @return
	 */
	public static TOPICS getSubRoot(TOPICS topic) {
		TOPICS result = TOPICS.ROOT;
		if (topic.hyponym == TOPICS.ROOT) {
			result = topic;
		} else {
			return getHyponym(topic.hyponym);
		}
		return result;
	}

}
