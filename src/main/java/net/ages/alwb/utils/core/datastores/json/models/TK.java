package net.ages.alwb.utils.core.datastores.json.models;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;

import net.ages.alwb.utils.core.error.handling.ErrorUtils;

/**
 * 
 * Provides a means to have topic|Key as _id of a json object.
 * The _id can be domain|topic|Key, e.g. gr_gr_cog|actors|Priest
 * but it would be better for you to use the LTK classes instead.
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
public class TK extends AbstractModel {
	@Expose String _id;
	String topic = null;
	String key = null;
	Object value = null;

	public TK() {
		super();
	}
	
	/**
	 * 
	 * @param id must be topic|key in format.
	 */
	public TK(String id) {
		set_id(id);
	}
	
	public TK(String topic, String key) {
		super();
		this.topic = topic;
		this.key = key;
		set_Id();
	}

	public String get_id() {
		return _id;
	}
	
	public void set_Id() {
		if (this.topic != null && this.key != null) {
			this._id = topic + "|" + key;
		}
	}

	public void set_id(String _id) {
		this._id = _id;
		parseId();
	}

	public String getTopic() {
		if (this.topic == null) {
			parseId();
		}
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
		set_Id();
	}

	public String getKey() {
		if (this.key == null) {
			parseId();
		}
		return key;
	}

	public void setKey(String key) {
		this.key = key;
		set_Id();
	}

	private void parseId() {
		if (this._id == null) {
			if (this.topic != null && this.key != null) {
				this._id = topic + "|" + key;
			}
		} else {
			if (_id.contains("|")) {
				try {
					String[] parts = _id.split("\\|");
					if (parts.length == 2) {
						this.topic = parts[0];
						this.key = parts[1];
					} else if (parts.length == 3) { // e.g. id = gr_gr_cog|actors|Priest.text, where gr_gr_cog|actors is the topic
						this.topic = parts[0] + "|" + parts[1];
						this.key = parts[2];
					} else {
						this.topic = _id;
						this.key = _id;
					}
				} catch (Exception e) {
					this.topic = _id;
					this.key = _id;
				}
			}
		}
	}
	public String toJsonString() {
		this.set_Id();
		return super.toJsonString();
	}
	
	public JsonObject toJsonObject() {
		this.set_Id();
		return super.toJsonObject();
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
}
