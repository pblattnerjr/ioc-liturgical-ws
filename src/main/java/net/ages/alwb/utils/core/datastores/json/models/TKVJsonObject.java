package net.ages.alwb.utils.core.datastores.json.models;


import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.annotations.Expose;

/**
 * Provides an entry whose value is a JsonObject.
 * 
 * @author mac002
 *
 */
public class TKVJsonObject extends TK {
	@Expose JsonObject value;
	
	public TKVJsonObject() {
		super();
	}

	/**
	 * The JsonObject value must include a property _id
	 * whose value is of the form topic|key
	 * @param value
	 */
	public TKVJsonObject(JsonObject value) {
		super(value.get("_id").getAsString());
		if (value.has("value")) {
			this.value = value.get("value").getAsJsonObject();
		} else {
			this.value = value;
		}
	}
	public TKVJsonObject(String _id, JsonObject value) {
		super(_id);
		this.value = value;
	}
	
	public TKVJsonObject(String topic, String key, JsonObject value) {
		super(topic, key);
		this.value =  value;
	}

	public JsonObject getValue() {
		return value;
	}

	public void setValue(JsonObject value) {
		this.value = value;
	}
	
	@SuppressWarnings("unchecked")
	public Object getInstance(@SuppressWarnings("rawtypes") Class c) {
		String json = null;
		if (this.getValue().has("value")) {
			json = this.getValue().get("value").toString();
		} else {
			json = this.getValue().toString();
		}
		return gson.fromJson(json, c);
	}
	
}
