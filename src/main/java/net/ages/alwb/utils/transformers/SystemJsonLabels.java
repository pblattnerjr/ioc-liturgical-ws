package net.ages.alwb.utils.transformers;

import java.util.Map;
import java.util.TreeMap;

import org.ocmc.ioc.liturgical.schemas.models.supers.AbstractModel;

import com.google.gson.annotations.Expose;

public class SystemJsonLabels extends AbstractModel {

	@Expose public Map<String, LanguageJsonLabels> labels = new TreeMap<String, LanguageJsonLabels>();

	public SystemJsonLabels() {
		super();
	}
	
	public void add(String language, String topic, String key, String value) {
		LanguageJsonLabels map = new LanguageJsonLabels();
		if (this.labels.containsKey(language)) {
			map = this.labels.get(language);
		}
		map.add(topic, key,value);
		this.labels.put(language, map);
	}

	public Map<String, LanguageJsonLabels> getLabels() {
		return labels;
	}

	public void setLabels(Map<String, LanguageJsonLabels> labels) {
		this.labels = labels;
	}

}
