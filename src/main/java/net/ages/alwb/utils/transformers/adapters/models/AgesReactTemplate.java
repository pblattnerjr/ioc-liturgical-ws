package net.ages.alwb.utils.transformers.adapters.models;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.google.gson.annotations.Expose;

import net.ages.alwb.utils.core.datastores.json.models.AbstractModel;

public class AgesReactTemplate extends AbstractModel {
	@Expose public String url = "";
	@Expose public String html = "";
	@Expose public List<String> topicKeys = new ArrayList<String>();
	@Expose public Map<String,String> values = new TreeMap<String,String>();
	@Expose public HtmlElement topElement = new HtmlElement(true);
	
	public AgesReactTemplate(String url) {
		super();
		this.url = url;
	}

	public AgesReactTemplate(String url, boolean printPretty) {
		super();
		super.setPrettyPrint(printPretty);
		this.url = url;
	}

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public List<String> getTopicKeys() {
		return topicKeys;
	}

	public void setTopicKeys(List<String> topicKeys) {
		this.topicKeys = topicKeys;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Map<String, String> getValues() {
		return values;
	}

	public void setValues(Map<String, String> values) {
		this.values = values;
	}
	
	/**
	 * Add a value to the value map
	 * @param key
	 * @param value
	 */
	public void addValue(String key, String value) {
		this.values.put(key, value);
	}
	
	public void addTopicKey(String topicKey) {
		if (! topicKeys.contains(topicKey)) {
			topicKeys.add(topicKey);
		}
	}
	
	/**
	 * Get the set of IDs used in this template
	 * @return
	 */
	public Set<String> getIds() {
		return this.values.keySet();
	}

	public HtmlElement getTopElement() {
		return topElement;
	}

	public void setTopElement(HtmlElement topElement) {
		this.topElement = topElement;
	}

}
