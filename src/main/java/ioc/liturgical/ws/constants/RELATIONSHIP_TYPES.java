package ioc.liturgical.ws.constants;

/**
 * Types of Relationships in the Neo4j Database
 * @author mac002
 *
 */
public enum RELATIONSHIP_TYPES {
	REFERS_TO_ANIMAL(
			"REFERS_TO_ANIMAL"
			, "Doc makes reference to an animal."
		)
	, REFERS_TO_BEING(
			"REFERS_TO_BEING"
			, "Doc makes reference to a being."
			)
	, REFERS_TO_BIBLICAL_TEXT(
			"REFERS_TO_BIBLICAL_TEXT"
			, "Doc makes reference to Biblical text."
			)
	, REFERS_TO_CONCEPT(
			"REFERS_TO_CONCEPT"
			, "Doc makes reference to an abstract concept."
			)
	, REFERS_TO_EVENT(
			"REFERS_TO_EVENT"
			, "Doc makes reference to an event."
			)
	, REFERS_TO_GROUP(
			"REFERS_TO_GROUP"
			, "Doc makes reference to a group of people."
			)
	, REFERS_TO_OBJECT(
			"REFERS_TO_OBJECT"
			, "Doc makes reference to an object."
			)
	, REFERS_TO_HUMAN(
			"REFERS_TO_HUMAN"
			, "Doc makes reference to a human being."
			)
	, REFERS_TO_MYSTERY(
			"REFERS_TO_MYSTERY"
			, "Doc makes reference to a Mystery."
			)
	, REFERS_TO_PLACE(
			"REFERS_TO_PLACE"
			, "Doc makes reference to a place."
			)
	, REFERS_TO_PLANT(
			"REFERS_TO_PLANT"
			, "Doc makes reference to a plant."
			)
	, REFERS_TO_ROLE(
			"REFERS_TO_ROLE"
			, "Doc makes reference to a role."
	 )
	, REFERS_TO_WORD_ANALYSIS(
			"WORD_ANALYSIS"
			, "reference to a grammatical analysis of a word."
	 )
	, EXAMPLE(
			"EXAMPLE"
			, "has / is an example"
	 )
	// from here down the relationships are for WordNet
	, SENSE_A(
			"SENSE_A"
			, "sense of an adjective"
	 )
	, SENSE_N(
			"SENSE_N"
			, "sense of a noun"
	 )
	, SENSE_R(
			"SENSE_R"
			, "sense of an adverb"
	 )
	, SENSE_V(
			"SENSE_V"
			, "sense of a verb"
	 )
	, SYNSET_MEMBER(
			"SYNSET_MEMBER"
			, "member of a synset"
	 )
	, HYPONYM(
			"HYPONYM"
			, "~" // for the WordNet relationships, the description is being set to the symbol value
		)
		, HYPERNYM(
			"HYPERNYM"
			, "@"
		)
		, PART_MERONYM(
			"PART_MERONYM"
			, "%p"
		)
		, MEMBER_OF_THIS_DOMAIN_TOPIC(
			"MEMBER_OF_THIS_DOMAIN_TOPIC"
			, "-c"
		)
		, SUBSTANCE_MERONYM(
			"SUBSTANCE_MERONYM"
			, "%s"
		)
		, DOMAIN_OF_SYNSET_TOPIC(
			"DOMAIN_OF_SYNSET_TOPIC"
			, ";c"
		)
		, PART_HOLONYM(
			"PART_HOLONYM"
			, "#p"
		)
		, MEMBER_HOLONYM(
			"MEMBER_HOLONYM"
			, "#m"
		)
		, INSTANCE_HYPONYM(
			"INSTANCE_HYPONYM"
			, "~i"
		)
		, ATTRIBUTE(
			"ATTRIBUTE"
			, "="
		)
		, DOMAIN_OF_SYNSET_USAGE(
			"DOMAIN_OF_SYNSET_USAGE"
			, ";u"
		)
		, INSTANCE_HYPERNYM(
			"INSTANCE_HYPERNYM"
			, "@i"
		)
		, DOMAIN_OF_SYNSET_REGION(
			"DOMAIN_OF_SYNSET_REGION"
			, ";r"
		)
		, MEMBER_MERONYM(
			"MEMBER_MERONYM"
			, "%m"
		)
		, MEMBER_OF_THIS_DOMAIN_USAGE(
			"MEMBER_OF_THIS_DOMAIN_USAGE"
			, "-u"
		)
		, SUBSTANCE_HOLONYM(
			"SUBSTANCE_HOLONYM"
			, "#s"
		)
		, MEMBER_OF_THIS_DOMAIN_REGION(
			"MEMBER_OF_THIS_DOMAIN_REGION"
			, "-r"
		)
		, ENTAILMENT(
			"ENTAILMENT"
			, "*"
		)
		, VERB_GROUP(
			"VERB_GROUP"
			, "$"
		)
		, CAUSE(
			"CAUSE"
			, ">"
		)
		, ALSO_SEE(
			"ALSO_SEE"
			, "^"
		)
		, SIMILAR_TO(
			"SIMILAR_TO"
			, "&"
		)
	;
	public String typename;
	public String description;
	
	private RELATIONSHIP_TYPES(
			String typename
			, String description
			) {
		this.typename = typename;
		this.description = description;
	}

}
