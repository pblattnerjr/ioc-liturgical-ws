package ioc.liturgical.ws.constants;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Joiner;


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

}
