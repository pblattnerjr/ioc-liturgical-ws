package net.ages.alwb.utils.core.datastores.json.models;

import com.google.gson.JsonPrimitive;
import com.google.gson.annotations.Expose;

/**
 * Provides an entry whose value is a String.
 * 
 * @author mac002
 *
 */
public class TKVJsonPrimitive extends TK {
	@Expose
	JsonPrimitive value;

	public TKVJsonPrimitive() {
		super();
	}

	public TKVJsonPrimitive(String _id, String value) {
		super(_id);
		this.value = new JsonPrimitive(value);
	}

	public TKVJsonPrimitive(String _id, boolean value) {
		super(_id);
		this.value = new JsonPrimitive(value);
	}

	public TKVJsonPrimitive(String _id, long value) {
		super(_id);
		this.value = new JsonPrimitive(value);
	}

	public TKVJsonPrimitive(String _id, int value) {
		super(_id);
		this.value = new JsonPrimitive(value);
	}

	public TKVJsonPrimitive(String topic, String key, String value) {
		super(topic, key);
		this.value = new JsonPrimitive(value);
	}

	public TKVJsonPrimitive(String topic, String key, boolean value) {
		super(topic, key);
		this.value = new JsonPrimitive(value);
	}

	public TKVJsonPrimitive(String topic, String key, long value) {
		super(topic, key);
		this.value = new JsonPrimitive(value);
	}

	public TKVJsonPrimitive(String topic, String key, int value) {
		super(topic, key);
		this.value = new JsonPrimitive(value);
	}

	public TKVJsonPrimitive(String topic, String key, JsonPrimitive value) {
		super(topic, key);
		this.value = value;
	}

	public JsonPrimitive getValue() {
		return value;
	}

	public void setValue(JsonPrimitive value) {
		this.value = value;
	}

	public void setValue(String value) {
		if (value != null && (value.toLowerCase().equals("true") || value.toLowerCase().equals("false"))) {
			boolean testVal = Boolean.parseBoolean(value);
			this.value = new JsonPrimitive(testVal);
		} else {
			try {
				long testVal = Long.parseLong(value);
				this.value = new JsonPrimitive(testVal);
			} catch (Exception e2) {
				try {
					int testVal = Integer.parseInt(value);
					this.value = new JsonPrimitive(testVal);
				} catch (Exception e) {
					this.value = new JsonPrimitive(value);
				}
			}
		}
	}

	/**
	 * Test the value.
	 * @return true if value is not null and has content
	 */
	public boolean valueHasContent() {
		return (this.value != null) && (this.value.toString().length() > 0);
	}
}
