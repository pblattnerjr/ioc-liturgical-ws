package ioc.liturgical.ws.constants;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import com.google.common.base.Joiner;
import com.google.gson.JsonArray;

import net.ages.alwb.utils.core.datastores.json.models.DropdownItem;


public enum ONTOLOGY_TOPICS {
	ROOT("OntoRoot", "The root node of the ontology.", null)
	, BEING(
			"Being"
			, "A living entity such as God, an angel, a human being, a plant, or an animal."
			, ONTOLOGY_TOPICS.ROOT
			)
	, ANIMAL(
			"Animal"
			, "A being that is a living organizm capable of independent movement.",
			ONTOLOGY_TOPICS.BEING
			)
	, CONCEPT(
			"Concept"
			,"An abstract concept about something."
			, ONTOLOGY_TOPICS.ROOT
			)
	, EVENT(
			"Event"
			, "Something that happens or occurs."
			, ONTOLOGY_TOPICS.ROOT
			)
	, GRAMMAR(
			"Grammar"
			, "Linguistic information about a word or its relationship to other words"
			, ONTOLOGY_TOPICS.ROOT
			)
	, GROUP(
			"Group"
			, "A collection of people."
			, ONTOLOGY_TOPICS.ROOT
			)
	, HUMAN(
			"Human"
			, "A human being."
			, ONTOLOGY_TOPICS.BEING
			)
	, LINGUISTICS(
			"Linguistics"
			, "Information about a language: its lexicon, semantics, phonology, morphology, grammar."
			, ONTOLOGY_TOPICS.ROOT
			)
	, LEXICAL_ITEM(
			"LexicalItem"
			, "The lemmas and forms of a language"
			, ONTOLOGY_TOPICS.LINGUISTICS
			)
	, MYSTERY(
			"Mystery"
			, "Baptism, Chrismation, Confession, Marriage, Holy Communion, Holy Orders, or Unction."
			, ONTOLOGY_TOPICS.CONCEPT
			)
	, OBJECT(
			"Object"
			, "A non-living entity, e,g, an artifact or tool."
			, ONTOLOGY_TOPICS.ROOT
			)
	, PLANT(
			"Plant"
			, "A being that is a living organizm not capable of independent movement."
			, ONTOLOGY_TOPICS.BEING
			)
	, PLACE(
			"Place"
			, "A celestial or geographic location."
			, ONTOLOGY_TOPICS.ROOT
			)
	, ROLE(
			"Role"
			, "A role that entity can play or be."
			, ONTOLOGY_TOPICS.ROOT
			)
	, TEXT(
			"Text"
			, "A document containing written words."
			, ONTOLOGY_TOPICS.ROOT
			)
	, TEXT_CONCORDANCE(
			"TextConcordance"
			, "A document containing a substring from a text, used as a line in a concordance."
			, ONTOLOGY_TOPICS.LINGUISTICS
			)
	, TEXT_BIBLICAL(
			"Biblical"
			, "Biblical text."
			, ONTOLOGY_TOPICS.TEXT
			)
	, TEXT_LITURGICAL(
			"Liturgical"
			, "Liturgical text."
			, ONTOLOGY_TOPICS.TEXT
			)
	, TEXT_SPEECH(
			"Speech"
			, "Text that records a speech made by someone."
			, ONTOLOGY_TOPICS.TEXT
			)
	, WORD_INFLECTED(
			"WordInflected"
			, "The inflected form a lemma can take, e.g. the lemma λόγος can have the form λόγον "
			, ONTOLOGY_TOPICS.LEXICAL_ITEM
			)
	, WORD_GRAMMAR(
			"WordGrammar"
			, "Grammatical Information about a word"
			, ONTOLOGY_TOPICS.GRAMMAR
			)
	, WORD_LEMMA(
			"WordLemma"
			, "The form of a word that is used to look it up in a lexicon, e.g. the inflected form λόγον is found in a lexicon as the lemma λόγος."
			, ONTOLOGY_TOPICS.LEXICAL_ITEM
			)
	;

	public String keyname = "";
	public String description = "";
	public ONTOLOGY_TOPICS hyponym;
	
	private ONTOLOGY_TOPICS(
			String keyname
			, String description
			, ONTOLOGY_TOPICS hyponym
			) {
		this.keyname = keyname;
		this.description = description;
		this.hyponym = hyponym;
	}
		
	/**
	 * Find the Topic for this string
	 * @param topicname
	 * @return ONTOLOGY_TOPICS topic
	 */
	public static ONTOLOGY_TOPICS forName(String name) {
		for (ONTOLOGY_TOPICS t : ONTOLOGY_TOPICS.values()) {
			if (t.keyname.equals(name)) {
				return t;
			}
		}
		return null;
	}
	
	/**
	 * Creates a delimited string id for this role, library, and user 
	 * @param library
	 * @param user
	 * @return
	 */
	public String toId(String library, String user) {
		return Joiner.on(Constants.ID_DELIMITER).join(this.keyname, library, user);
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

	private String toLabels(ONTOLOGY_TOPICS topic) {
		StringBuffer result = new StringBuffer();
		if (topic == ONTOLOGY_TOPICS.ROOT) {
			result.append(topic.keyname);
		} else {
			result.append(toLabels(topic.hyponym));
			if (result.length() > 0) {
				result.append(":");
			}
			result.append(topic.keyname);
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
		for (ONTOLOGY_TOPICS t : ONTOLOGY_TOPICS.values()) {
			values.add(t.keyname);
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
	public static JsonArray keyNamesToJsonArrayDropdown() {
		JsonArray result = new JsonArray();
		TreeSet<String> values = new TreeSet<String>();
		for (ONTOLOGY_TOPICS t : ONTOLOGY_TOPICS.values()) {
			values.add(t.keyname);
		}
		for (String value : values) {
			result.add(new DropdownItem(value, value).toJsonObject());
		}
		return result;
	}

}
