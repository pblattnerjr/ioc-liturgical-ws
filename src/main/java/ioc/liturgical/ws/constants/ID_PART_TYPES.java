package ioc.liturgical.ws.constants;

/**
 * Used to identify the content of a part of an ID.
 * IDs are composed of a library + a topic + a key.
 * 
 * The forms for Biblical Text, Liturgical Text, Ontology instances,
 * and references (links) indicate the type of the ID part. 
 * 
 * @author mac002
 *
 */
public enum ID_PART_TYPES {
	BIBLICAL_BOOK_ABBREVIATION(
			"BiblicalBookAbbreviation"
			, "The abbreviation of the name of the biblical book containing the verse."
			)
,	BIBLICAL_CHAPTER_VERSE(
			"BiblicalChapterVerse"
			, "The chapter and verse number of the biblical verse."
			)
	, ID_OF_SELECTED_BIBLICAL_VERSE(
			"IdOfSelectedBiblicalVerse"
			, "The ID from a user selected biblical verse."
			)
	,ID_OF_SELECTED_LITURGICAL_TEXT(
			"IdOfSelectedLiturgicalText"
			, "The ID from a user selected liturgical text."
			)
	,ID_OF_SELECTED_ONTOLOGY_INSTANCE(
			"IdOfSelectedOntologyInstance"
			, "The ID from a user selected liturgical text."
			)
	, KEY_FROM_ID_OF_SELECTED_BIBLICAL_VERSE(
			"KeyFromIdOfSelectedBiblicalText"
			, "The key part of the ID from a user selected biblical verse."
			)
	, KEY_FROM_ID_OF_SELECTED_LITURGICAL_TEXT(
			"KeyFromIdOfSelectedLiturgicalText"
			, "The key  part of the ID from a user selected liturgical text."
			)
	, ONTOLOGY_TOPIC(
			"OntologyTopic"
			, "An ontology topic"
			)
	, TOPIC_FROM_ID_OF_SELECTED_BIBLICAL_VERSE(
			"TopicFromIdOfSelectedBiblicalVerse"
			, "The topic part of the ID from a user selected biblical verse."
			)
	, TOPIC_FROM_ID_OF_SELECTED_LITURGICAL_TEXT(
			"TopicFromIdOfSelectedLiturgicalText"
			, "The topic part of the ID from a user selected liturgical text."
			)
	, USER_TEXT(
			"UserText"
			, "Text entered by the user"
			)
	;

	public String code = "";
	public String description = "";
	
	private ID_PART_TYPES(
			String code
			, String description
			) {
		this.code = code;
		this.description = description;
	}
		
	/**
	 * Find the Role for this string
	 * @param rolename
	 * @return Role
	 */
	public static ID_PART_TYPES forWsname(String code) {
		for (ID_PART_TYPES e : ID_PART_TYPES.values()) {
			if (e.code.equals(code)) {
				return e;
			}
		}
		return null;
	}
	
}
