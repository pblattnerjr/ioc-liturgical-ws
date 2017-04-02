package ioc.liturgical.ws.models.options;

import com.google.gson.annotations.Expose;

import net.ages.alwb.utils.core.datastores.json.models.AbstractModel;

/**
 * Used to create the json for a single option entry in a dropdown list.
 * @author mac002
 *
 */
public class DropdownOptionEntry extends AbstractModel {
	@Expose String value = "";
	@Expose String label = "";
	
	/**
	 * 
	 * @param value - this is the identifier used in the code
	 * @param label - this is what the user will see in the dropdown list
	 */
	public DropdownOptionEntry(String value, String label) {
		super();
		this.value = value;
		this.label = label;
	}

	/**
	 * 
	 * @param value - this is the identifier used in the code and what the user will see in the dropdown list
	 */
	public DropdownOptionEntry(String value) {
		super();
		this.value = value;
		this.label = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	

}
