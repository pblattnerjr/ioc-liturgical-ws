package net.ages.alwb.utils.core.datastores.json.models;


import com.google.gson.annotations.Expose;

/**
 * Provides an entry whose value is a String.
 * 
 * @author mac002
 *
 */
public class TKVBoolean extends TK {
	@Expose Boolean value;
	
	public TKVBoolean() {
		super();
	}
		
	public TKVBoolean(String _id, Boolean value) {
		super(_id);
		this.value = value;
	}
	
	public TKVBoolean(String topic, String key, Boolean value) {
		super(topic, key);
		this.value =  value;
	}

	public Boolean getValue() {
		return value;
	}

	public void setValue(Boolean value) {
		this.value = value;
	}
	
}
