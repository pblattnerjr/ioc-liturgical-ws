package ioc.liturgical.ws.constants;

import com.google.common.base.Joiner;

/**
 * These are topics for the SYSTEM_LIBS.MISC library.
 * All docs are stored with an ID of library|topic|key.
 * @author mac002
 *
 */
public enum SYSTEM_MISC_LIBRARY_TOPICS {
	DOMAINS("domains", "Contains docs for the domains topic in the DB library.")
	, LABELS("labels", "Contains docs for the labels topic in the DB library.")
	, SCHEMAS("schemas", "Library topics and docs relating to schemas")
	,USERS("users", "Contains docs for the users topic in the DB library.")
	, UTILITIES("utilities", "Contains docs describing utilities");
	;
	public String lib = SYSTEM_LIBS.MISC.libname;
	public String topic;
	public String description;
	public String libTopic;
	
	private SYSTEM_MISC_LIBRARY_TOPICS(
			String topic
			, String description
			) {
		this.topic = topic;
		this.description = description;
		this.libTopic = lib + Constants.ID_DELIMITER + topic;
	}
	
	/**
	 * Turns the topic and key into a delimited ID
	 * @param name
	 * @param key
	 * @return
	 */
	public String toId(String key) {
		return Joiner.on(Constants.ID_DELIMITER).join(this.libTopic, key);
	}
}
