package ioc.liturgical.ws.constants;

import ioc.liturgical.ws.constants.Constants;

/**
 * Enum for REST endpoints for the Admin api.
 * Used to give info to requestor about what endpoints are available.
 * The order in which they are listed below is the order in which they
 * will appear in the list in the UI.  So add new ones at the appropriate
 * position in the enum list!
 * 
 *               
 * @author mac002
 *
 */
public enum ENDPOINTS_ADMIN_API {
	ADMINS(
			"admins"
			, ""
			, "Users allowed to administer a given library."
			, SYSTEM_LIBS.ADMINS.libname
			,""
			, INCLUDE_IN_RESOURCE_LIST.YES.value
			)
	, AUTHORIZATION_NEW(
			"authorizations"
			, "new"
			,"Create a new authorization."
			, SYSTEM_LIBS.ADMINS.libname
			, ""
			, INCLUDE_IN_RESOURCE_LIST.NO.value
			)
	, AUTHORS(
			"authors"
			, ""
			, "Users allowed to author docs in a given library"
			, SYSTEM_LIBS.AUTHORS.libname
			,""
			, INCLUDE_IN_RESOURCE_LIST.YES.value
			)
	, DOMAINS(
			""
			, "domains"
			,"Docs for domains."
			, DB_TOPICS.DOMAINS.lib
			, DB_TOPICS.DOMAINS.topic
			, INCLUDE_IN_RESOURCE_LIST.YES.value
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
	, READERS(
			"readers"
			, ""
			, "Users allowed to read docs in a given library"
			, SYSTEM_LIBS.READERS.libname
			,""
			, INCLUDE_IN_RESOURCE_LIST.YES.value
			)
	, USERS(
			"users"
			, ""
			,"People who have access to the system."
			, SYSTEM_LIBS.USERS.libname
			, ""
			, INCLUDE_IN_RESOURCE_LIST.YES.value
			)
	, USERS_CONTACT(
			"users"
			, "contact"
			,"Contact information for people who have access to the system."
			, SYSTEM_LIBS.USERS.libname
			, USER_TOPICS.CONTACT.lib
			, INCLUDE_IN_RESOURCE_LIST.YES.value
			)
	, USERS_NEW(
			"users"
			, "new"
			,"Create a new user."
			, SYSTEM_LIBS.USERS.libname
			, ""
			, INCLUDE_IN_RESOURCE_LIST.NO.value
			)
	, USERS_PASSWORD(
			"users"
			, "password"
			,"change a user's password."
			, SYSTEM_LIBS.USERS.libname
			, USER_TOPICS.HASH.lib
			, INCLUDE_IN_RESOURCE_LIST.YES.value
			)
	, USERS_STATISTICS(
			"users"
			, "statistics"
			,"System access statistics for people who have access to the system."
			, SYSTEM_LIBS.USERS.libname
			, USER_TOPICS.STATISTICS.lib
			, INCLUDE_IN_RESOURCE_LIST.YES.value
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
	private ENDPOINTS_ADMIN_API(
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
	
	public String toDbLibrary() {
		return this.mapsToLibrary;
	}
	
	public String toDbLibraryTopic() {
		return this.toDbLibrary() + Constants.ID_DELIMITER + this.mapsToTopic;
	}
	
	public String toDbLibraryTopicKey(String key) {
		return this.toDbLibraryTopic() +  Constants.ID_DELIMITER + key;
	}
	
	/**
	 * Converts the REST path into a Database id
	 * @param path
	 * @return
	 */
	public static String pathToDbId(String path) {
		String result = path;
			for (ENDPOINTS_ADMIN_API e : ENDPOINTS_ADMIN_API.values()) {
				if (e.toLibraryTopicKeyPath().equals(path)) {
					result = e.mapsToLibrary +  Constants.ID_DELIMITER + e.mapsToTopic + Constants.ID_DELIMITER + "";
				} else if (e.toLibraryTopicPath().equals(path)) {
					result = e.mapsToLibrary +  Constants.ID_DELIMITER + e.mapsToTopic;
				} else if (e.toLibraryPath().equals(path)) {
					result = e.mapsToLibrary;
				} else if (path.length() == 0) {
					result = e.mapsToLibrary +  Constants.ID_DELIMITER + e.mapsToTopic;
				}
			}
			return result;
	}

}
