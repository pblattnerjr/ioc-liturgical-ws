package net.ages.alwb.utils.transformers;
import java.util.Map;
import java.util.TreeMap;

import org.ocmc.ioc.liturgical.schemas.models.supers.AbstractModel;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;

public class JsonLabel extends AbstractModel {

	@Expose public String label = "";
	@Expose public String value = "";
	
	public JsonLabel(String label, String value) {
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
	
	public String toJsonString() {
		JsonObject json = new JsonObject();
		json.addProperty(this.label, this.value);
		return json.toString();
	}
}
