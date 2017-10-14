package ioc.liturgical.ws.constants;

import java.util.ArrayList;
import java.util.List;

import ioc.liturgical.ws.constants.db.external.TOPICS;

/**
 * Types of Relationships in the Neo4j Database
 * @author mac002
 *
 */
public enum RELATIONSHIP_TYPES {
	REFERS_TO_ANIMAL(
			"REFERS_TO_ANIMAL"
			, "Doc makes reference to an animal."
			, TOPICS.ANIMAL
		)
	, REFERS_TO_BEING(
			"REFERS_TO_BEING"
			, "Doc makes reference to a being."
			, TOPICS.BEING
			)
	, REFERS_TO_BIBLICAL_TEXT(
			"REFERS_TO_BIBLICAL_TEXT"
			, "Doc makes reference to Biblical text."
			, TOPICS.TEXT_BIBLICAL
			)
	, REFERS_TO_CONCEPT(
			"REFERS_TO_CONCEPT"
			, "Doc makes reference to an abstract concept."
			, TOPICS.CONCEPT
			)
	, REFERS_TO_EVENT(
			"REFERS_TO_EVENT"
			, "Doc makes reference to an event."
			, TOPICS.EVENT
			)
	, REFERS_TO_GOD(
			"REFERS_TO_GOD"
			, "Doc makes reference to God."
			, TOPICS.GOD
			)
	, REFERS_TO_GROUP(
			"REFERS_TO_GROUP"
			, "Doc makes reference to a group of people."
			, TOPICS.GROUP
			)
	, REFERS_TO_OBJECT(
			"REFERS_TO_OBJECT"
			, "Doc makes reference to an object."
			, TOPICS.OBJECT
			)
	, REFERS_TO_HUMAN(
			"REFERS_TO_HUMAN"
			, "Doc makes reference to a human being."
			, TOPICS.HUMAN
			)
	, REFERS_TO_MYSTERY(
			"REFERS_TO_MYSTERY"
			, "Doc makes reference to a Mystery."
			, TOPICS.MYSTERY
			)
	, REFERS_TO_PLACE(
			"REFERS_TO_PLACE"
			, "Doc makes reference to a place."
			, TOPICS.PLACE
			)
	, REFERS_TO_PLANT(
			"REFERS_TO_PLANT"
			, "Doc makes reference to a plant."
			, TOPICS.PLANT
			)
	, REFERS_TO_ROLE(
			"REFERS_TO_ROLE"
			, "Doc makes reference to a role."
			, TOPICS.ROLE
	 )
	, REFERS_TO_WORD_ANALYSIS(
			"WORD_ANALYSIS"
			, "reference to a grammatical analysis of a word."
			, TOPICS.WORD_GRAMMAR
	 )
	, HAS_NOTE(
			"HAS_NOTE"
			, "note made about a node"
			, TOPICS.NOTES_ROOT
	 )
	, EXAMPLE(
			"EXAMPLE"
			, "has / is an example"
			, TOPICS.TEXT
	 )
	// from here down the relationships are for WordNet
	, SENSE_A(
			"SENSE_A"
			, "sense of an adjective"
			, TOPICS.WORDNET
	 )
	, SENSE_N(
			"SENSE_N"
			, "sense of a noun"
			, TOPICS.WORDNET
	 )
	, SENSE_R(
			"SENSE_R"
			, "sense of an adverb"
			, TOPICS.WORDNET
	 )
	, SENSE_V(
			"SENSE_V"
			, "sense of a verb"
			, TOPICS.WORDNET
	 )
	, SYNSET_MEMBER(
			"SYNSET_MEMBER"
			, "member of a synset"
			, TOPICS.WORDNET
	 )
	, HYPONYM(
			"HYPONYM"
			, "~" // for the WordNet relationships, the description is being set to the symbol value
			, TOPICS.WORDNET
		)
		, HYPERNYM(
			"HYPERNYM"
			, "@"
			, TOPICS.WORDNET
		)
		, PART_MERONYM(
			"PART_MERONYM"
			, "%p"
			, TOPICS.WORDNET
		)
		, MEMBER_OF_THIS_DOMAIN_TOPIC(
			"MEMBER_OF_THIS_DOMAIN_TOPIC"
			, "-c"
			, TOPICS.WORDNET
		)
		, SUBSTANCE_MERONYM(
			"SUBSTANCE_MERONYM"
			, "%s"
			, TOPICS.WORDNET
		)
		, DOMAIN_OF_SYNSET_TOPIC(
			"DOMAIN_OF_SYNSET_TOPIC"
			, ";c"
			, TOPICS.WORDNET
		)
		, PART_HOLONYM(
			"PART_HOLONYM"
			, "#p"
			, TOPICS.WORDNET
		)
		, MEMBER_HOLONYM(
			"MEMBER_HOLONYM"
			, "#m"
			, TOPICS.WORDNET
		)
		, INSTANCE_HYPONYM(
			"INSTANCE_HYPONYM"
			, "~i"
			, TOPICS.WORDNET
		)
		, ATTRIBUTE(
			"ATTRIBUTE"
			, "="
			, TOPICS.WORDNET
		)
		, DOMAIN_OF_SYNSET_USAGE(
			"DOMAIN_OF_SYNSET_USAGE"
			, ";u"
			, TOPICS.WORDNET
		)
		, INSTANCE_HYPERNYM(
			"INSTANCE_HYPERNYM"
			, "@i"
			, TOPICS.WORDNET
		)
		, DOMAIN_OF_SYNSET_REGION(
			"DOMAIN_OF_SYNSET_REGION"
			, ";r"
			, TOPICS.WORDNET
		)
		, MEMBER_MERONYM(
			"MEMBER_MERONYM"
			, "%m"
			, TOPICS.WORDNET
		)
		, MEMBER_OF_THIS_DOMAIN_USAGE(
			"MEMBER_OF_THIS_DOMAIN_USAGE"
			, "-u"
			, TOPICS.WORDNET
		)
		, SUBSTANCE_HOLONYM(
			"SUBSTANCE_HOLONYM"
			, "#s"
			, TOPICS.WORDNET
		)
		, MEMBER_OF_THIS_DOMAIN_REGION(
			"MEMBER_OF_THIS_DOMAIN_REGION"
			, "-r"
			, TOPICS.WORDNET
		)
		, ENTAILMENT(
			"ENTAILMENT"
			, "*"
			, TOPICS.WORDNET
		)
		, VERB_GROUP(
			"VERB_GROUP"
			, "$"
			, TOPICS.WORDNET
		)
		, CAUSE(
			"CAUSE"
			, ">"
			, TOPICS.WORDNET
		)
		, ALSO_SEE(
			"ALSO_SEE"
			, "^"
			, TOPICS.WORDNET
		)
		, SIMILAR_TO(
			"SIMILAR_TO"
			, "&"
			, TOPICS.WORDNET
		)
	;
	public String typename;
	public String description;
	public TOPICS topic;
	
	private RELATIONSHIP_TYPES(
			String typename
			, String description
			, TOPICS topic
			) {
		this.typename = typename;
		this.description = description;
		this.topic = topic;
	}
	
	/**
	 * Get RELATIONSHIP_TYPES values that are Links, i.e. "REFERS_TO"
	 * @return
	 */
	public static List<RELATIONSHIP_TYPES> Links() {
		return RELATIONSHIP_TYPES.filterByTypeName("REFERS_TO");
	}

	/**
	 * Returns a filtered list of RELATIONSHIP_TYPES whose typename starts with the supplied parameter
	 * @param startsWith
	 * @return
	 */
	public static List<RELATIONSHIP_TYPES> filterByTypeName(String startsWith) {
		List<RELATIONSHIP_TYPES> result = new ArrayList<RELATIONSHIP_TYPES>();
		for (RELATIONSHIP_TYPES t : RELATIONSHIP_TYPES.values()) {
			if (t.typename.startsWith(startsWith)) {
				result.add(t);
			}
		}
		return result;
	}

	/**
	 * Returns a filtered list of RELATIONSHIP_TYPES whose topic matches the supplied parameter
	 * @param topic
	 * @return
	 */
	public static List<RELATIONSHIP_TYPES> filterByTopic(TOPICS topic) {
		List<RELATIONSHIP_TYPES> result = new ArrayList<RELATIONSHIP_TYPES>();
		for (RELATIONSHIP_TYPES t : RELATIONSHIP_TYPES.values()) {
			if (t.topic.equals(topic)) {
				result.add(t);
			}
		}
		return result;
	}

	/**
	 * Returns a filtered list of RELATIONSHIP_TYPES whose topic hyponymn matches the supplied parameter
	 * @param hyponymn
	 * @return
	 */
	public static List<RELATIONSHIP_TYPES> filterByTopicHyponymn(TOPICS hyponymn) {
		List<RELATIONSHIP_TYPES> result = new ArrayList<RELATIONSHIP_TYPES>();
		for (RELATIONSHIP_TYPES t : RELATIONSHIP_TYPES.values()) {
			if (t.topic.hyponym.equals(hyponymn)) {
				result.add(t);
			}
		}
		return result;
	}

}
