package net.ages.alwb.utils.core.datastores.json.models;


import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;

import net.ages.alwb.utils.core.datastores.json.exceptions.BadIdException;

/**
 * Provides an entry whose value is a JsonObject.
 * If you want the value to just be a String, use LTKVString instead!
 * 
 * @author mac002
 *
 */
public class LTKVJsonObject extends LTK {
	@Expose String _valueSchemaId = "";
	@Expose JsonObject value;
	
	/**
	 * Constructor
	 * The JsonObject value must include a property _id
	 * whose value is of the form library|topic|key
	 * @param value
	 */
	public LTKVJsonObject(JsonObject value) throws BadIdException {
		super(value.get("_id").getAsString());
		if (value.has("value")) {
			this.value = value.get("value").getAsJsonObject();
		} else {
			this.value = value;
		}
	}
	
	/**
	 * Constructor
	 * The JsonObject value must include a property _id
	 * whose value is of the form library|topic|key
	 * @param _id
	 * @param schema id
	 * @param value
	 * @throws BadIdException
	 */
	public LTKVJsonObject(
			String _id
			, String schema
			, JsonObject value
			) throws BadIdException {
		super(_id);
		this._valueSchemaId = schema;
		this.value = value;
	}
	
	/**
	 * Constructor
	 * @param library
	 * @param topic
	 * @param key
	 * @param schema id
	 * @param value
	 * @throws BadIdException
	 */
	public LTKVJsonObject(
			String library
			, String topic
			, String key
			, String schema
			, JsonObject value
			) 
		throws BadIdException {
		super(library,topic, key);
		this._valueSchemaId = schema;
		this.value =  value;
	}
	
	
	public String get_valueSchemaId() {
		return _valueSchemaId;
	}

	public void set_valueSchemaId(String _valueSchemaId) {
		this._valueSchemaId = _valueSchemaId;
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
