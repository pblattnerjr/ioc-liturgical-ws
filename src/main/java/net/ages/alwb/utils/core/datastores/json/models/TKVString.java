package net.ages.alwb.utils.core.datastores.json.models;


import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;

/**
 * Provides an entry whose value is a String.
 * 
 * @author mac002
 *
 */
public class TKVString extends TK {
	@Expose String value;
	
	public TKVString() {
		super();
	}
		
	public TKVString(String _id, String value) {
		super(_id);
		this.value = value;
	}
	
	public TKVString(String topic, String key, String value) {
		super(topic, key);
		this.value =  value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}
