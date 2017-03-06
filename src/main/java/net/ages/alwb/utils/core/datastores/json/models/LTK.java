package net.ages.alwb.utils.core.datastores.json.models;

import com.google.common.base.Joiner;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;

import net.ages.alwb.utils.core.datastores.json.exceptions.BadIdException;
import net.ages.alwb.utils.core.error.handling.ErrorUtils;

/**
 * 
 * Provides a means to have library|topic|Key as _id of a json object.
 * 
 * Note: this class must be extended by another class
 * that adds
 * 	@Expose <t> value member
 * and appropriate set and get methods.
 * 
 * Important! Any class that implements this must have a
 * parameterless contructor that calls super().  
 * Otherwise, gson will be null for the subclass.
 * @author mac002
 *
 */
public class LTK extends AbstractModel {
	@Expose String _id;
	String library = null;
	String topic = null;
	String key = null;
	Object value = null;
	String delimiter = "|";

	/**
	 * 
	 * @param id format must be library|topic|key 
	 */
	public LTK(String id) throws BadIdException {
		super();
		this._id = id;
		parseId();
	}
	/**
	 * Constructs an id in the format of library|topic|key
	 * @param library
	 * @param topic
	 * @param key
	 * @throws BadIdException
	 */
	public LTK(
			String library
			, String topic
			, String key) 
					throws BadIdException {
		super();
		this.library = library;
		this.topic = topic;
		this.key = key;
		set_id();
	}

	public String get_id() {
		return _id;
	}
	
	public void set_id() throws BadIdException {
		this._id = this.library + delimiter + this.topic + delimiter + this.key;
		parseId();
	}
	
	/**
	 * For working with Guava tables,
	 * call this to get the rowKey
	 * @return library|topic
	 */
	public String getRowKey() {
		return this.library + delimiter + this.topic;
	}

	/**
	 * For working with Guava tables,
	 * call this to get the columnKey
	 * @return key
	 */
	public String getColumnKey() {
		return this.key;
	}

	/**
	 * Returns the delimiter used for the id parts
	 * @return
	 */
	public String getDelimiter() {
		return delimiter;
	}
	
	public String getLibrary()  {
		return library;
	}

	public void setLibrary(String library)  throws BadIdException  {
		this.library = library;
		set_id();
	}


	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) throws BadIdException {
		this.topic = topic;
		set_id();
	}

	public String getKey()  {
		return key;
	}

	public void setKey(String key) throws BadIdException {
		this.key = key;
		set_id();
	}

	private void parseId() throws BadIdException {
		boolean ok = false;
			if (_id.contains("|")) {
				try {
					String[] parts = _id.split("\\|");
					if (parts.length == 3) { // e.g. id = gr_gr_cog|actors|Priest.text, where gr_gr_cog|actors is the topic
						this.library = parts[0];
						this.topic = parts[1];
						this.key = parts[2];
						ok = true;
					} else {
					}
				} catch (Exception e) {
					ok = false;
				}
			} else {
				ok = false;
			}
		if (!ok) {
			throw new BadIdException("_id must have exactly three parts, delimited by the pipe symbol | e.g. gr_gr_cog|actors|priest ");
		}
	}
	public String toJsonString() {
		return super.toJsonString();
	}
	
	public JsonObject toJsonObject() {
		return super.toJsonObject();
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
}
