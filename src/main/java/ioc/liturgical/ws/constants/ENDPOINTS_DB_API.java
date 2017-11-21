package ioc.liturgical.ws.constants;

import ioc.liturgical.ws.constants.Constants;

/**
 * Enum for REST endpoints for the Database api.
 * Used to give info to requestor about what endpoints are available.
 * The order in which they are listed below is the order in which they
 * will appear in the list in the UI.  So add new ones at the appropriate
 * position in the enum list!
 * 
 *               
 * @author mac002
 *
 */
public enum ENDPOINTS_DB_API {
	AGES_INDEX(
			"agesindex"
			, ENDPOINT_TYPES.TABLE
			, "AgesIndex"
			,"Endpoint for an index of available AGES HTML files"
			)
	, AGES_PDF(
			"agespdf"
			, ENDPOINT_TYPES.PDF
			, "AgesPdf"
			,"Endpoint for an AGES html file converted to a PDF file"
			)
	, AGES_READ_ONLY_TEMPLATE(
			"agesreadonlytemplate"
			, ENDPOINT_TYPES.HTML
			, "AgesReadOnlyTemplate"
			,"Endpoint for an AGES HTML file converted to a Read-Only React template"
			)
	, AGES_REACT_TEMPLATE(
			"agesreacttemplate"
			, ENDPOINT_TYPES.HTML
			, "AgesReactTemplate"
			,"Endpoint for an AGES HTML file converted to a React template"
			)
	, ANIMALS(
			"animals"
			, ENDPOINT_TYPES.ONTOLOGY
			, "Animals"
			,"Endpoint for animal docs"
			)
	, BEINGS(
			"beings"
			, ENDPOINT_TYPES.ONTOLOGY
			, "Beings"
			,"Endpoint for being docs"
			)
	, CLONE(
			"clone"
			, ENDPOINT_TYPES.NODE
			, "CloneLibrary"
			,"Endpoint for cloning a library"
			)
	, CONCEPTS(
			"concepts"
			, ENDPOINT_TYPES.ONTOLOGY
			, "Concepts"
			,"Endpoint for abstract concept docs"
			)
	, DEPENDENCY_TREE(
			"dependencyTree"
			, ENDPOINT_TYPES.NODE
			, "DependencyTree"
			,"Endpoint for dependency tree information"
			)
	, DOCS(
			""
			, ENDPOINT_TYPES.NODE
			, ""
			,"Endpoint for generic docs"
			)
	, DOMAINS(
			"domains"
			, ENDPOINT_TYPES.NODE
			, "domain"
			,"Endpoint for domains."
			)
	, DROPDOWNS_DOMAINS(
			"domains"
			, ENDPOINT_TYPES.DROPDOWN
			, "domain"
			,"Endpoint for dropdown values for domains."
			)
	, DROPDOWNS_NOTES(
			"notes"
			, ENDPOINT_TYPES.DROPDOWN
			, "notes"
			,"Endpoint for dropdown values to search notes."
			)
	, DROPDOWNS_ONTOLOGY(
			"ontology"
			, ENDPOINT_TYPES.DROPDOWN
			, "ontology"
			,"Endpoint for dropdown values to search properties of ontology entries."
			)
	, DROPDOWNS_RELATIONSHIPS(
			"relationships"
			, ENDPOINT_TYPES.DROPDOWN
			, "domain"
			,"Endpoint for dropdown values to search properties of relationships."
			)
	, DROPDOWNS_TEXTS(
			"texts"
			, ENDPOINT_TYPES.DROPDOWN
			, "domain"
			,"Endpoint for dropdown values to search docs of type text."
			)
	, DROPDOWNS_GR_LIB_TOPICS(
			"grlibtopics"
			, ENDPOINT_TYPES.DROPDOWN
			, "domain"
			,"Endpoint for dropdown values for the topics in the gr_gr_cog library."
			)
	, DROPDOWNS_TREEBANKS(
			"treebanks"
			, ENDPOINT_TYPES.DROPDOWN
			, "treebanks"
			,"Endpoint for dropdown values to search treebanks."
			)
	, GROUPS(
			"groups"
			, ENDPOINT_TYPES.ONTOLOGY
			, "Groups"
			,"Endpoint for group docs"
			)
	, GOD(
			"God"
			, ENDPOINT_TYPES.ONTOLOGY
			, "God"
			,"Endpoint for docs about God"
			)
	, HUMANS(
			"humans"
			, ENDPOINT_TYPES.ONTOLOGY
			, "Humans"
			,"Endpoint for human docs"
			)
	, LABELS(
			"labels"
			, ENDPOINT_TYPES.NODE
			, "label"
			,"Endpoint for labels."
			)
	, LINKS(
			""
			, ENDPOINT_TYPES.RELATIONSHIP
			, ""
			,"Generic Endpoint for Relationships with properties"
			)
	, LINK_REFERS_TO_ANIMAL(
			RELATIONSHIP_TYPES.REFERS_TO_ANIMAL.typename.toLowerCase()
			, ENDPOINT_TYPES.RELATIONSHIP
			, RELATIONSHIP_TYPES.REFERS_TO_ANIMAL.typename
			,"Endpoint for text refers to an animal"
			)
	, LINK_REFERS_TO_BEING(
			RELATIONSHIP_TYPES.REFERS_TO_BEING.typename.toLowerCase()
			, ENDPOINT_TYPES.RELATIONSHIP
			, RELATIONSHIP_TYPES.REFERS_TO_BEING.typename
			,"Endpoint for text refers to a being"
			)
	, LINK_REFERS_TO_BIBLICAL_TEXT(
			"refers_to_biblical_text"
			, ENDPOINT_TYPES.RELATIONSHIP
			, RELATIONSHIP_TYPES.REFERS_TO_BIBLICAL_TEXT.typename
			,"Endpoint for text refers to a biblical text"
			)
	, LINK_REFERS_TO_CONCEPT(
			RELATIONSHIP_TYPES.REFERS_TO_CONCEPT.typename.toLowerCase()
			, ENDPOINT_TYPES.RELATIONSHIP
			, RELATIONSHIP_TYPES.REFERS_TO_CONCEPT.typename
			,"Endpoint for text refers to a concept"
			)
	, LINK_REFERS_TO_EVENT(
			RELATIONSHIP_TYPES.REFERS_TO_EVENT.typename.toLowerCase()
			, ENDPOINT_TYPES.RELATIONSHIP
			, RELATIONSHIP_TYPES.REFERS_TO_EVENT.typename
			,"Endpoint for text refers to an event"
			)
	, LINK_REFERS_TO_GROUP(
			RELATIONSHIP_TYPES.REFERS_TO_GROUP.typename.toLowerCase()
			, ENDPOINT_TYPES.RELATIONSHIP
			, RELATIONSHIP_TYPES.REFERS_TO_GROUP.typename
			,"Endpoint for text refers to a group of something."
			)
	, LINK_REFERS_TO_HUMAN(
			RELATIONSHIP_TYPES.REFERS_TO_HUMAN.typename.toLowerCase()
			, ENDPOINT_TYPES.RELATIONSHIP
			, RELATIONSHIP_TYPES.REFERS_TO_HUMAN.typename
			,"Endpoint for text refers to a human being"
			)
	, LINK_REFERS_TO_MYSTERY(
			RELATIONSHIP_TYPES.REFERS_TO_MYSTERY.typename.toLowerCase()
			, ENDPOINT_TYPES.RELATIONSHIP
			, RELATIONSHIP_TYPES.REFERS_TO_MYSTERY.typename
			,"Endpoint for text refers to a mystery"
			)
	, LINK_REFERS_TO_OBJECT(
			RELATIONSHIP_TYPES.REFERS_TO_OBJECT.typename.toLowerCase()
			, ENDPOINT_TYPES.RELATIONSHIP
			, RELATIONSHIP_TYPES.REFERS_TO_OBJECT.typename
			,"Endpoint for text refers to a human object"
			)
	, LINK_REFERS_TO_PLACE(
			RELATIONSHIP_TYPES.REFERS_TO_PLACE.typename.toLowerCase()
			, ENDPOINT_TYPES.RELATIONSHIP
			, RELATIONSHIP_TYPES.REFERS_TO_PLACE.typename
			,"Endpoint for text refers to a place"
			)
	, LINK_REFERS_TO_PLANT(
			RELATIONSHIP_TYPES.REFERS_TO_PLANT.typename.toLowerCase()
			, ENDPOINT_TYPES.RELATIONSHIP
			, RELATIONSHIP_TYPES.REFERS_TO_PLANT.typename
			,"Endpoint for text refers to a plant"
			)
	, LINK_REFERS_TO_ROLE(
			RELATIONSHIP_TYPES.REFERS_TO_ROLE.typename.toLowerCase()
			, ENDPOINT_TYPES.RELATIONSHIP
			, RELATIONSHIP_TYPES.REFERS_TO_ROLE.typename
			,"Endpoint for text refers to a plant"
			)
	, NEW(
			"new"
			, ENDPOINT_TYPES.NODE
			,"Forms for creating new docs."
			, ""
			)
	, NOTES(
			"notes"
			, ENDPOINT_TYPES.NODE
			, "notes"
			,"Endpoint for notes about texts"
			)
	, OBJECTS(
			"objects"
			, ENDPOINT_TYPES.ONTOLOGY
			, "objects"
			,"Endpoint for object docs"
			)
	, ONTOLOGY(
			"ontology"
			, ENDPOINT_TYPES.ONTOLOGY
			, "ontology"
			,"Endpoint for ontology entries"
			)
	, PLACES(
			"places"
			, ENDPOINT_TYPES.ONTOLOGY
			, "Places"
			,"Endpoint for place docs"
			)
	, PLANTS(
			"plants"
			, ENDPOINT_TYPES.ONTOLOGY
			, "Plants"
			,"Endpoint for plant docs"
			)
	, ROLES(
			"roles"
			, ENDPOINT_TYPES.ONTOLOGY
			, "Roles"
			,"Endpoint for role docs"
			)
	, TABLES(
			"tables"
			, ENDPOINT_TYPES.TABLE
			, "Table"
			,"Endpoint for tables"
			)
	, TEXTS(
			"texts"
			, ENDPOINT_TYPES.NODE
			, "Text"
			,"Endpoint for text docs"
			)
	, TEXT_ANALYSIS(
			"text/analysis"
			, ENDPOINT_TYPES.NLP
			, "Text Analysis"
			,"Endpoint for a grammatical analysis of words in a text"
			)
	, TREEBANKS(
			"treebanks"
			, ENDPOINT_TYPES.NODE
			, "treebanks"
			,"Endpoint for treebanks"
			)
	, VALUE(
			"value"
			, ENDPOINT_TYPES.NODE
			, "Value"
			,"Endpoint for simple value create or update of a text doc"
			)
	, VIEW_TEMPLATE(
			"viewtemplate"
			, ENDPOINT_TYPES.VIEW
			, "viewtemplate"
			,"Endpoint for text topic keys / values as a template"
			)
	, VIEW_TOPIC(
			"viewtopic"
			, ENDPOINT_TYPES.VIEW
			, "viewtopic"
			,"Endpoint for text topic keys / values as a view"
			)
	, TOKENS(
			"tokens"
			, ENDPOINT_TYPES.NLP
			, "Token"
			,"Endpoint for text tokens"
			)
	, TOKEN_ANALYSIS(
			"token/analysis"
			, ENDPOINT_TYPES.NLP
			, "Token Analysis"
			,"Endpoint for a grammatical analysis of tokens"
			)
	, WORD_ANALYSIS(
			"word/analysis"
			, ENDPOINT_TYPES.NLP
			, "Word Analysis"
			,"Endpoint for a grammatical analysis of words"
			)
	;

	public String pathPrefix = Constants.EXTERNAL_DATASTORE_API_PATH;
	public String name = "";
	public String label = "";
	public String pathname = "";
	public ENDPOINT_TYPES type = null;
	public String description = "";
	
	/**
	 * 
	 * @param name - endpoint name as appears in the REST API
	 * @param type - node or relationship, etc
	 * @param label - name used in database, e.g. node label or relationship type name
	 * @param description
	 */
	private ENDPOINTS_DB_API(
			String name
			, ENDPOINT_TYPES type
			, String label
			, String description
			) {
		this.name = name;
		this.type = type;
		this.label = label;
		this.description = description;
		pathname = pathPrefix;
		switch (type) {
		case FORM:
			pathname = pathname + Constants.EXTERNAL_DATASTORE_DROPDOWNS_PATH;
			break;
		case DROPDOWN:
			pathname = pathname + Constants.EXTERNAL_DATASTORE_DROPDOWNS_PATH;
			break;
		case NLP:
			pathname = pathname + Constants.EXTERNAL_DATASTORE_NATURAL_LANGUAGE_PROCESSING_PATH;
			break;
		case NODE:
			pathname = pathname + Constants.EXTERNAL_DATASTORE_NODE_PATH;
			break;
		case ONTOLOGY:
			pathname = pathname + Constants.EXTERNAL_DATASTORE_ONTOLOGY_PATH;
			break;
		case RELATIONSHIP:
			pathname = pathname + Constants.EXTERNAL_DATASTORE_RELATIONSHIP_PATH;
			break;
		default: // node
			pathname = pathname + Constants.EXTERNAL_DATASTORE_NODE_PATH;
			break;
		}
		pathname = pathname + (this.name.length() > 0 ? "/" : "" ) + this.name;
	}

	/**
	 * Returns a REST path that expects a specific key
	 * @return
	 */
	public String toLibraryTopicKeyPath() {
		return this.pathname + Constants.PATH_LIBRARY_TOPIC_KEY_WILDCARD;
	}
	
	/**
	 * 
	 * @return
	 */
	public String toLibraryTopicPath() {
		return this.pathname + Constants.PATH_LIBRARY_TOPIC_WILDCARD;
	}
	/**
	 * 
	 * @return
	 */
	public String toLibraryPath() {
		return this.pathname + Constants.PATH_LIBRARY_WILDCARD;
	}
	
}
