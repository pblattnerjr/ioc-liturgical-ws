package net.ages.alwb.utils.core.datastores.json.models;

import com.google.gson.annotations.Expose;

/**
 * Used to build a JsonObject to represent an item in a dropdown list.
 * @author mac002
 *
 */
public class DropdownItem extends AbstractModel {
	@Expose String label = "";
	@Expose String value = "";
	
	public DropdownItem() {
		super();
	}
	
	/**
	 * Shortcut constructor that sets both label and value to value parm
	 * @param value
	 */
	public DropdownItem(String value) {
		super();
		this.label = value;
		this.value = value;
	}

	public DropdownItem(String label, String value) {
		super();
		this.label = label;
		this.value = value;
	}
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

}
