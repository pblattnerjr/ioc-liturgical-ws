package net.ages.alwb.utils.core.datastores.json.models;

import java.sql.SQLException;

import net.ages.alwb.utils.core.datastores.json.manager.JsonStoreManager;

/**
 * Provides methods for classes that will contain JsonProperty members.
 * and use JsonManager as the persistence datastore for the bean.
 * 
 * Note that JsonManager uses the TKVJsonPrimitive that has been wrapped
 * by JsonProperty.  
 * 
 * @author mac002
 *
 */
public abstract class JsonBean {
	public  String TOPIC;
	protected JsonStoreManager storeManager;
	
	public JsonBean(String topic, JsonStoreManager storeManager) {
		this.TOPIC = topic;
		this.storeManager = storeManager;
	}
	
	@SuppressWarnings("rawtypes")
	protected void addToStore(JsonProperty value) throws SQLException {
		storeManager.add(value.getTkvJsonPrimitive());
	}
	
	protected void setFromBooleanPropertyStore(JsonProperty<Boolean> p) {
		TKVJsonPrimitive test = (TKVJsonPrimitive) storeManager.getEntry(TOPIC, p.getKey());
		if (test != null && test.valueHasContent()) {
			p.setValue(test.getValue().getAsBoolean());
		}
	}

	protected void setFromDoublePropertyStore(JsonProperty<Double> p) {
		TKVJsonPrimitive test = (TKVJsonPrimitive) storeManager.getEntry(TOPIC, p.getKey());
		if (test != null && test.valueHasContent()) {
			p.setValue(test.getValue().getAsDouble());
		}
	}

	protected void setFromIntegerPropertyStore(JsonProperty<Integer> p) {
		TKVJsonPrimitive test = (TKVJsonPrimitive) storeManager.getEntry(TOPIC, p.getKey());
		if (test != null && test.valueHasContent()) {
			p.setValue(test.getValue().getAsInt());
		}
	}

	protected void setFromStringPropertyStore(JsonProperty<String> p) {
		TKVJsonPrimitive test = (TKVJsonPrimitive) storeManager.getEntry(TOPIC, p.getKey());
		if (test != null && test.valueHasContent()) {
			p.setValue(test.getValue().getAsString());
		}
	}

}
