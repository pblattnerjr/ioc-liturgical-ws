package ioc.liturgical.ws.constants;

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
	DOMAINS(
			""
			, "domains"
			,"Docs for domains."
			, DB_TOPICS.DOMAINS.lib
			, DB_TOPICS.DOMAINS.topic
			, INCLUDE_IN_RESOURCE_LIST.YES.value
			)
	, DELETE(
			"doc"
			, "delete"
			,"Delete a doc."
			, ""
			, ""
			, INCLUDE_IN_RESOURCE_LIST.NO.value
			)
	, DOMAINS_NEW(
			"domains"
			, "new"
			,"Create a new domain."
			, DB_TOPICS.DOMAINS.lib
			, ""
			, INCLUDE_IN_RESOURCE_LIST.NO.value
			)
	, LABELS(
			""
			, "labels"
			,"Docs for labels."
			, DB_TOPICS.LABELS.lib
			, DB_TOPICS.LABELS.topic
			, INCLUDE_IN_RESOURCE_LIST.YES.value
			)
	, LABELS_NEW(
			"labels"
			, "new"
			,"Create a new label."
			, DB_TOPICS.LABELS.lib
			, ""
			, INCLUDE_IN_RESOURCE_LIST.NO.value
			)
	, NEW(
			"new"
			, "forms"
			,"Forms for creating new docs."
			, ""
			, ""
			, INCLUDE_IN_RESOURCE_LIST.YES.value
			)
	, REFERENCES(
			"references"
			, "references"
			,"Docs for references."
			, DB_TOPICS.REFERENCES.lib
			, DB_TOPICS.REFERENCES.topic
			, INCLUDE_IN_RESOURCE_LIST.YES.value
			)
	, REFERENCES_NEW(
			"references"
			, "new"
			,"Create a new reference."
			, DB_TOPICS.REFERENCES.lib
			, ""
			, INCLUDE_IN_RESOURCE_LIST.NO.value
			)
	, WILDCARD_LIBRARY(
			"*"
			, "/*/*"
			,"Wildcard Library."
			, "*"
			, "/*/*"
			, INCLUDE_IN_RESOURCE_LIST.NO.value
			)
	;

	public String pathPrefix = Constants.INTERNAL_DATASTORE_API_PATH;
	public String pathname = "";
	public String label = "";
	public String library = "";
	public String topic = "";
	public String mapsToLibrary = "";
	public String mapsToTopic = "";
	public String description = "";
	public boolean includeInResourcesList = true;
	
	/**
	 * 
	 * @param library - as it will display in the client
	 * @param topic - as it will display in the client
	 * @param description - of the endpoint
	 * @param mapsToLibrary - which DB library to map to
	 * @param mapsToTopic - which DB topic to map to
	 * @param includeInResourcesList - include as an endpoint when user asks for Resources?
	 */
	private ENDPOINTS_DB_API(
			String library
			, String topic
			, String description
			, String mapsToLibrary
			, String mapsToTopic
			, boolean includeInResourcesList
			) {
		this.library = library;
		this.topic = topic;
		this.pathname = (library.length() > 0 ? library : SYSTEM_LIBS.MISC.libname) + (topic.length() > 0 ? "/" + topic: "" ) ;

		this.description = description;
		this.mapsToLibrary = mapsToLibrary;
		this.mapsToTopic = mapsToTopic;
		this.includeInResourcesList = includeInResourcesList;

		/**
		 * Set up how the endpoint will display in a droplist to a user
		 */
		this.label = library;
		if (label.length() > 0) {
			if (topic.length() > 0) {
				this.label = this.label + " / ";
			}
		}
		this.label = this.label + topic;

	}

	/**
	 * Returns a REST path that expects a specific key
	 * @return
	 */
	public String toLibraryTopicKeyPath() {
		return this.pathPrefix + "/" + this.pathname + "/*/*";
	}
	
	/**
	 * 
	 * @return
	 */
	public String toLibraryTopicPath() {
		return this.pathPrefix + "/" + this.pathname + "/*";
	}
	/**
	 * 
	 * @return
	 */
	public String toLibraryPath() {
		return this.pathPrefix + "/" + this.pathname;
	}

	/**
	 * Can be used to insert a specific library into the wildcard library,
	 * leaving the topic and key as wildcards.
	 * @param library
	 * @return
	 */
	public String toParameterizedWildcardLibrary(String library) {
		return this.pathPrefix 
				+ "/" 
				+ library 
				+ ENDPOINTS_DB_API.WILDCARD_LIBRARY.topic;
	}

	public String toDbLibrary() {
		return this.mapsToLibrary;
	}
	
	public String toDbLibraryTopic() {
		return this.toDbLibrary() + "|" + this.mapsToTopic;
	}
	
	public String toDbLibraryTopicKey(String key) {
		return this.toDbLibraryTopic() + "|" + key;
	}
	
	/**
	 * Converts the REST path into a Database id
	 * @param path
	 * @return
	 */
	public static String pathToDbId(String path) {
		String result = path;
			for (ENDPOINTS_DB_API e : ENDPOINTS_DB_API.values()) {
				if (e.toLibraryTopicKeyPath().equals(path)) {
					result = e.mapsToLibrary + "|" + e.mapsToTopic + "|" + "";
				} else if (e.toLibraryTopicPath().equals(path)) {
					result = e.mapsToLibrary + "|" + e.mapsToTopic;
				} else if (e.toLibraryPath().equals(path)) {
					result = e.mapsToLibrary;
				} else if (path.length() == 0) {
					result = e.mapsToLibrary + "|" + e.mapsToTopic;
				}
			}
			return result;
	}

}
