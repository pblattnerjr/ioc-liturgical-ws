package ioc.liturgical.ws.constants;

import com.google.common.base.Joiner;

public enum USER_TOPICS {
	CONTACT("contact", "docs for the topic Contact.")
	, HASH("hash", "docs for the topic Hash, i.e. the hashed user password.")
	, NEW("new","place holder for a new user")
	, STATISTICS("statistics", "docs for the topic Stats, i.e. statistics about the user's access to the system.")
	, STATUS("status", "docs for the topic Status, i.e. whether the user's accout is active/inactive.")
	;
	public String lib = SYSTEM_LIBS.USERS.libname;
	public String topic;
	public String description;
	public String libTopic;
	
	private USER_TOPICS(
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
