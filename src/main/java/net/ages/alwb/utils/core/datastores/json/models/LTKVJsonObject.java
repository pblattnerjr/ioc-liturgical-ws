package net.ages.alwb.utils.core.datastores.json.models;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;

import net.ages.alwb.utils.core.datastores.json.exceptions.BadIdException;

/**
 * Provides an entry whose value is a JsonObject.
 * If you want the value to just be a String, use LTKVString instead!
 * 
 * Notes:
 * 1. If valueSchemaId is set, it will add the schema name as the 
 *     first label in the list of labels.
 * 
 * @author mac002
 *
 */
public class LTKVJsonObject extends LTK {
	@Expose String _valueSchemaId = "";
	@Expose JsonObject value; 
	@Expose List<String> labels = new ArrayList<String>();
	
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
		addLabels();
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
		addLabels();
	}
	
	/**
	 * Adds a set of labels to the outer part of the LTKVJsonObject.
	 * If the Object uses a schema, the schema name becomes
	 * the first label of the LTKVJsonObject.
	 * 
	 * If the value (which is itself a JsonObject) also has labels,
	 * these will be externalized.  That is, they will be added to the
	 * outer set of labels as well.  The purpose of externalizing labels
	 * is to provide a database field that can explicitly be searched.
	 * 
	 * TODO: why would the value labels sometimes be a delimited string
	 * and other times be a JsonArray?  There is an inconsitency here
	 * that needs to be solved lest it bite us later.
	 */
	private void addLabels() {
		addLabel(getSchemaAsLabel());
		if (this.value.has("labels")) {
			if (value.get("labels").isJsonArray()) {
				JsonArray labelsArray = value.get("labels").getAsJsonArray();
				for (JsonElement e : labelsArray) {
					addLabel(e.getAsString());
				}
			} else {
				String labelString = value.get("labels").getAsString();
				String [] labels = labelString.split(":");
				for (String label : labels) {
					addLabel(label);
				}
			}
		}
	}

	public String get_valueSchemaId() {
		return _valueSchemaId;
	}

	public void set_valueSchemaId(String _valueSchemaId) {
		this._valueSchemaId = _valueSchemaId;
		addLabel(getSchemaAsLabel());
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

	public List<String> getLabels() {
		return labels;
	}

	public void setLabels(List<String> labels) {
		this.labels = labels;
	}
	
	public void addLabel(String label) {
		if (! this.labels.contains(label)) {
			this.labels.add(label);
		}
	}

	public String getSchemaAsLabel() {
		String result = this._valueSchemaId;
		try {
			String [] parts = this._valueSchemaId.split(":");
			if (parts.length > 1) {
				result = parts[0];
			}
		} catch (Exception e) {
			// ignore
		}
		return result;
	}
	
	/**
	 * Returns the labels as a delimited path.
	 * For Neo4j, the delimiter should be a colon :
	 * @param delimiter
	 * @return
	 */
	public String getDelimitedLabels(String delimiter) {
		StringBuffer sb = new StringBuffer();
		for (int i=0; i < labels.size()-1; i++) {
			sb.append(labels.get(i));
			sb.append(delimiter);
		}
		sb.append(labels.get(labels.size()-1));
		return sb.toString();
	}

	/**
	 * Turns the POJO into a map with a value for:
	 * id
	 * library
	 * topic
	 * key
	 * _valueSchemaId
	 * value
	 * labels
	 * 
	 * which can then be passed as parameters to a consuming method,
	 * e.g. the Neo4j bolt interface.
	 * 
	 * @return
	 */
	public Map<String, Object> getAsPropertiesMap() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		 parameters.put("id", this.get_id());
		 parameters.put("domain", this.getLibrary());
		 parameters.put("topic", this.getTopic());
		 parameters.put("key", this.getKey());
		 parameters.put("_valueSchemaId", this.get_valueSchemaId());
		 parameters.put("value", this.getValue().toString());
		 parameters.put("labels", getDelimitedLabels(":"));
		Map<String, Object> props = new HashMap<String, Object>();
		props.put("props", parameters);
		return props;
	}
}
