package ioc.liturgical.ws.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;

import net.ages.alwb.utils.core.datastores.json.models.AbstractModel;

public class ResultJsonObjectArray extends AbstractModel {
	@Expose public String query;
	@Expose public RequestStatus status;
	@Expose public Long valueCount = Long.parseLong("0");
	@Expose public Map<String,JsonObject> valueSchemas = new TreeMap<String,JsonObject>();
	@Expose public List<JsonObject> values;
	
	public ResultJsonObjectArray(boolean prettyPrint) {
		super();
		super.setPrettyPrint(prettyPrint);
		values = new ArrayList<JsonObject>();
		status = new RequestStatus();
	}
	
	public void addSchema(String id, JsonObject schema) {
		if (! valueSchemas.containsKey(id)) {
			valueSchemas.put(id, schema);
		}
	}
	
	/**
	 * Add a JsonObject to the list of  values
	 * @param value
	 */
	public void addValue(JsonObject value) {
		this.values.add(value);
		valueCount++;
	}
		
	/**
	 * Add a JsonObject to the list of values, storing it with the key
	 * This actually creates a new JsonObject with key = key
	 * and value = value, then adds the new object to the list.
	 * @param key
	 * @param value
	 */
	public void addValue(String key, JsonObject value) {
		JsonObject o = new JsonObject();
		o.add(key, value);
		this.values.add(o);
		valueCount++;
	}

	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}

	public List<JsonObject> getResult() {
		return values;
	}

	public void setResult(List<JsonObject> result) {
		this.values = result;
		if (result != null) {
			this.valueCount = (long) result.size();
		}
	}

	public Long getCount() {
		return valueCount;
	}

	public void setCount(Long count) {
		this.valueCount = count;
	}

	public Long getResultCount() {
		return valueCount;
	}

	public void setResultCount(Long resultCount) {
		this.valueCount = resultCount;
	}

	public RequestStatus getStatus() {
		return status;
	}

	public void setStatus(RequestStatus status) {
		this.status = status;
	}

	public Long getValueCount() {
		return valueCount;
	}

	public void setValueCount(Long valueCount) {
		this.valueCount = valueCount;
	}

	public List<JsonObject> getValues() {
		return values;
	}
	
	public JsonArray getValuesAsJsonArray() {
		JsonArray array = new JsonArray();
		for (JsonObject o : this.values) {
			array.add(o);
		}
		return array;
	}

	public void setValues(List<JsonObject> values) {
		this.values = values;
	}
	
	public void setStatusCode(int code) {
		this.status.setCode(code);
	}

	/**
	 * Sets status developer message and user message to the same message
	 * @param message
	 */
	public void setStatusMessage(String message) {
		this.status.developerMessage = message;
		this.status.userMessage = message;
	}

	public void setStatusDeveloperMessage(String message) {
		this.status.developerMessage = message;
	}

	public void setStatusUserMessage(String message) {
		this.status.userMessage = message;
	}

	public Map<String, JsonObject> getValueSchemas() {
		return valueSchemas;
	}

	public void setValueSchemas(Map<String, JsonObject> valueSchemas) {
		this.valueSchemas = valueSchemas;
	}
	
	/**
	 * Gets the value property from the first JsonObject in the list
	 * as a String.  In other words, the value store there is expected to be a string.
	 * If there are none, will return null
	 * @return
	 */
	public String getFirstObjectValueAsString() {
		if (this.valueCount > 0) {
			return this.getValues()
					.get(0)
					.getAsJsonObject()
					.get("value")
					.getAsString();
		} else {
			return null;
		}
	}
	
	/**
	 * Gets the value property from the first JsonObject in the list
	 * as a JsonObject.  
	 * In other words, the value stored there is expected to be a JsonObject.
	 * If there are none, will return null
	 * @return
	 */
	public JsonObject getFirstObjectValueAsObject() {
		if (this.valueCount > 0) {
			return this.getValues()
					.get(0)
					.getAsJsonObject()
					.get("value")
					.getAsJsonObject();
		} else {
			return null;
		}
	}

	/**
	 * Gets the first object in the values list
	 * @return
	 */
	public JsonObject getFirstObject() {
		if (this.valueCount > 0) {
			return this.getValues()
					.get(0)
					.getAsJsonObject();
		} else {
			return null;
		}
	}
}
