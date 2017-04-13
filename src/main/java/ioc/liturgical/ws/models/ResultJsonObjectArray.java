package ioc.liturgical.ws.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
}
