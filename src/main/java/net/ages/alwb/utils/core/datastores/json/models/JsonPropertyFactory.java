package net.ages.alwb.utils.core.datastores.json.models;

/**
 * Static methods for instantiating JsonProperty from TKVJsonPrimitive.
 * There is a method for each of: Boolean, Long, Integer, and String.
 * @author mac002
 *
 */
public class JsonPropertyFactory {
	
	public static JsonProperty<Boolean> getAsBoolean(Object bean, TKVJsonPrimitive json) {
			return new JsonProperty<Boolean>(bean, json.getTopic(), json.getKey(), json.getValue().getAsBoolean());
	}
	public static JsonProperty<Double> getAsDouble(Object bean, TKVJsonPrimitive json) {
		return new JsonProperty<Double>(bean, json.getTopic(), json.getKey(), json.getValue().getAsDouble());
	}
	public static JsonProperty<Long> getAsLong(Object bean, TKVJsonPrimitive json) {
		return new JsonProperty<Long>(bean, json.getTopic(), json.getKey(), json.getValue().getAsLong());
	}
	public static JsonProperty<Integer> getAsInteger(Object bean, TKVJsonPrimitive json) {
		return new JsonProperty<Integer>(bean, json.getTopic(), json.getKey(), json.getValue().getAsInt());
	}
	public static JsonProperty<String> getAsString(Object bean, TKVJsonPrimitive json) {
		return new JsonProperty<String>(bean, json.getTopic(), json.getKey(), json.getValue().getAsString());
	}

}
