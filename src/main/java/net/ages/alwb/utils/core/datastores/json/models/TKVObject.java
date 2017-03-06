package net.ages.alwb.utils.core.datastores.json.models;

public class TKVObject extends TKVString {
	public TKVObject() {
		super();
	}
	
	public String getValue() {
		return this.toJsonString();
	}
	
	public void setValue() {
		super.setValue(this.getValue());
	}

}
