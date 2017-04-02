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
	DOCS(
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
			,"Endpoint for generic Relationships"
			)
	, LINK_REFERS_TO_BIBLICAL_TEXT(
			"refers_to_biblical_text"
			, ENDPOINT_TYPES.RELATIONSHIP
			, RELATIONSHIP_TYPES.REFERS_TO_BIBLICAL_TEXT.typename
			,"Endpoint for text A refers to text B"
			)
	, TEXTS(
			"texts"
			, ENDPOINT_TYPES.NODE
			, "Text"
			,"Endpoint for text docs"
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
	 * @param type - node or relationship
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
		if (type.equals(ENDPOINT_TYPES.NODE)) {
			pathname = pathname + Constants.EXTERNAL_DATASTORE_NODE_PATH;
		} else {
			pathname = pathname + Constants.EXTERNAL_DATASTORE_RELATIONSHIP_PATH;
		}
		pathname = pathname + this.name;
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
