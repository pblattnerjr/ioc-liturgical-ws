package ioc.liturgical.ws.constants.db.external;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import com.google.common.base.Joiner;
import com.google.gson.JsonArray;

import ioc.liturgical.ws.constants.Constants;
import net.ages.alwb.utils.core.datastores.json.models.DropdownItem;

/**
 * Keys used for a single instance of something.
 * @author mac002
 *
 */
public enum SINGLETON_KEYS {
	TABLE_OALD_SENSES(
			"OALD"
			, "Word senses from OALD"
			, LIBRARIES.TABLES
			, TOPICS.TABLE_LEXICON
			)
	;

	public String keyname = "";
	public String description = "";
	public LIBRARIES library = null;
	public TOPICS topic = null;
	
	private SINGLETON_KEYS(
			String keyname
			, String description
			, LIBRARIES library
			, TOPICS topic
			) {
		this.keyname = keyname;
		this.description = description;
		this.library = library;
		this.topic = topic;
	}
		
	/**
	 * Find the Topic for this string
	 * @param topicname
	 * @return ONTOLOGY_TOPICS topic
	 */
	public static SINGLETON_KEYS forName(String name) {
		for (SINGLETON_KEYS t : SINGLETON_KEYS.values()) {
			if (t.keyname.equals(name)) {
				return t;
			}
		}
		return null;
	}
	
	/**
	 * Get the keynames as a sorted list of DropDownItem
	 * @return
	 */
	public static List<DropdownItem> keyNamesToDropdown() {
		List<DropdownItem> result = new ArrayList<DropdownItem>();
		TreeSet<String> values = new TreeSet<String>();
		for (SINGLETON_KEYS t : SINGLETON_KEYS.values()) {
			values.add(t.keyname);
		}
		for (String value : values) {
			result.add(new DropdownItem(value, value));
		}
		return result;
	}

	/**
	 * Get the keynames as a JsonArray of DropDownItem
	 * @return
	 */
	public static JsonArray keyNamesToJsonArrayDropdown() {
		JsonArray result = new JsonArray();
		TreeSet<String> values = new TreeSet<String>();
		for (SINGLETON_KEYS t : SINGLETON_KEYS.values()) {
			values.add(t.keyname);
		}
		for (String value : values) {
			result.add(new DropdownItem(value, value).toJsonObject());
		}
		return result;
	}

	/**
	 * Returns an id, i.e. library_topic_key
	 * where library = the library for this singleton
	 * and topic = the topic for this singleton
	 * and key is the key for this singleton
	 * @return
	 */
	public String toId() {
		return Joiner.on(Constants.ID_DELIMITER).join(
				this.library.toSystemDomain()
				, this.topic.label
				, this.keyname
		);
	}
	
	public String getLibrary() {
		return this.library.toSystemDomain();
	}

}
