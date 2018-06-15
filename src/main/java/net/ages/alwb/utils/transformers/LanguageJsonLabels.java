package net.ages.alwb.utils.transformers;

import java.util.Map;
import java.util.TreeMap;

import org.ocmc.ioc.liturgical.schemas.models.supers.AbstractModel;

import com.google.gson.annotations.Expose;

public class LanguageJsonLabels extends AbstractModel {

	@Expose public Map<String,JsonLabel> labels = new TreeMap<String,JsonLabel>();

	public LanguageJsonLabels() {
		super();
	}
	
	public void add(String topic, String key, String value) {
		this.labels.put(topic, new JsonLabel(key,value));
	}

	public Map<String, JsonLabel> getLabels() {
		return labels;
	}

	public void setLabels(Map<String, JsonLabel> labels) {
		this.labels = labels;
	}

}
