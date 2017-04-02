package net.ages.alwb.utils.core.datastores.json.models;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;

import net.ages.alwb.utils.core.datastores.json.exceptions.BadIdException;
import net.ages.alwb.utils.core.id.managers.LTKIdManager;
import net.ages.alwb.utils.core.misc.Constants;

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
	@Expose String library = null;
	@Expose String topic = null;
	@Expose String key = null;
	LTKIdManager ltkIdManager = null;
	Object value = null;
	String delimiter = Constants.ID_DELIMITER;

	/**
	 * 
	 * @param id format must be library + delimiter + topic + delimiter + key and cannot have an embedded ID 
	 */
	public LTK(String id) throws BadIdException {
		super();
		this._id = id;
		parseId();
	}
	/**
	 * Constructs an id in the format of library + delimiter + topic + delimiter + key.
	 * Any of the parts (library, topic, or key) may be an embedded ID.
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
		this.ltkIdManager = new LTKIdManager(library, topic, key);
		joinPartsIntoId();
	}

	public String get_id() {
		return _id;
	}
	
	public void joinPartsIntoId() throws BadIdException {
		this._id = this.library + delimiter + this.topic + delimiter + this.key;
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
		joinPartsIntoId();
	}


	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) throws BadIdException {
		this.topic = topic;
		joinPartsIntoId();
	}

	public String getKey()  {
		return key;
	}

	public void setKey(String key) throws BadIdException {
		this.key = key;
		joinPartsIntoId();
	}

	/**
	 * @throws BadIdException
	 */
	private void parseId() throws BadIdException {
		boolean ok = false;
			if (_id.contains(Constants.ID_DELIMITER)) {
				try {
					String[] parts = _id.split(Constants.ID_SPLITTER);
					if (parts.length == 3) { // e.g. id = gr_gr_cog|actors|Priest.text, where gr_gr_cog|actors is the topic
						this.library = parts[0];
						this.topic = parts[1];
						this.key = parts[2];
						ok = true;
					}
				} catch (Exception e) {
					ok = false;
				}
			} else {
				ok = false;
			}
		if (!ok) {
			throw new BadIdException(
					"_id must have exactly three parts, delimited by " 
			+ Constants.ID_DELIMITER 
			+  " gr_gr_cog" + Constants.ID_DELIMITER  
			+ "actors" + Constants.ID_DELIMITER  
			+ "priest "
			+ " If you are embedding an ID into the Library or topic or key, you must use the constructor that takes library, topic, and key as parameters."
			);
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

	public LTKIdManager getLtkIdManager() {
		return ltkIdManager;
	}
	public void setLtkIdManager(LTKIdManager ltkIdManager) {
		this.ltkIdManager = ltkIdManager;
	}
	public void set_id(String _id) {
		this._id = _id;
	}


}
