package ioc.liturgical.ws.constants;

import com.google.common.base.Joiner;

/**
 * These are topics for the System DB library.
 * All docs are stored with an ID of library|topic|key.
 * All topics enumerated here belong to the same library, i.e. SYSTEM_LIBS.DB.
 * @author mac002
 *
 */
public enum DB_TOPICS {
	DOMAINS("domains", "Contains docs for the domains topic in the DB library.")
	, LABELS("labels", "Contains docs for the labels topic in the DB library.")
	, REFERENCES("references", "Contains docs for the references topic in the DB library.")
	, SCHEMAS("schemas", "Library topics and docs relating to schemas")
	,USERS("users", "Constains docs for the users topic in the DB library.");
	;
	public String lib = SYSTEM_LIBS.MISC.libname;
	public String topic;
	public String description;
	public String libTopic;
	
	private DB_TOPICS(
			String topic
			, String description
			) {
		this.topic = topic;
		this.description = description;
		this.libTopic = lib + Constants.ID_DELIMITER + topic;
	}
	
	/**
	 * Turns the topic and key into a delimited ID
	 * @param topic
	 * @param key
	 * @return
	 */
	public String toId(String key) {
		return Joiner.on(Constants.ID_DELIMITER).join(this.libTopic, key);
	}
}
