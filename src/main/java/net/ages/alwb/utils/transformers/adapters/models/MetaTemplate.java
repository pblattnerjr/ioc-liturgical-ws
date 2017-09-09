package net.ages.alwb.utils.transformers.adapters.models;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.google.gson.annotations.Expose;

import net.ages.alwb.utils.core.datastores.json.models.AbstractModel;

/**
 * Both AGES Liturgical Workbench (ALWB) and the OCMC ShareLatex Workbench (OSLW)
 * make use of templates to define the order and formatting of data for services and books.
 * 
 * This class is used to hold the meta data of a template from either source.
 * 
 * @author mac002
 *
 */
public class MetaTemplate extends AbstractModel {
	@Expose public String url = "";
	@Expose public String html = "";
	@Expose public String leftLibrary = "";
	@Expose public String leftLibraryFallback = "";
	@Expose public String centerLibrary = "";
	@Expose public String centerLibraryFallback = "";
	@Expose public String rightLibrary = "";
	@Expose public String rightLibraryFallback = "";
	@Expose public List<String> topicKeys = new ArrayList<String>();
	@Expose public Map<String,String> values = new TreeMap<String,String>();
	@Expose public TemplateElement topElement = new TemplateElement(true);
	
	public MetaTemplate(String url) {
		super();
		this.url = url;
	}

	public MetaTemplate(String url, boolean printPretty) {
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

	public TemplateElement getTopElement() {
		return topElement;
	}

	public void setTopElement(TemplateElement topElement) {
		this.topElement = topElement;
	}

	public String getLeftLibrary() {
		return leftLibrary;
	}

	public void setLeftLibrary(String leftLibrary) {
		this.leftLibrary = leftLibrary;
	}

	public String getLeftLibraryFallback() {
		return leftLibraryFallback;
	}

	public void setLeftLibraryFallback(String leftLibraryFallback) {
		this.leftLibraryFallback = leftLibraryFallback;
	}

	public String getCenterLibrary() {
		return centerLibrary;
	}

	public void setCenterLibrary(String centerLibrary) {
		this.centerLibrary = centerLibrary;
	}

	public String getCenterLibraryFallback() {
		return centerLibraryFallback;
	}

	public void setCenterLibraryFallback(String centerLibraryFallback) {
		this.centerLibraryFallback = centerLibraryFallback;
	}

	public String getRightLibrary() {
		return rightLibrary;
	}

	public void setRightLibrary(String rightLibrary) {
		this.rightLibrary = rightLibrary;
	}

	public String getRightLibraryFallback() {
		return rightLibraryFallback;
	}

	public void setRightLibraryFallback(String rightLibraryFallback) {
		this.rightLibraryFallback = rightLibraryFallback;
	}
	
	public void setLibraries(
			String leftLibrary
			, String centerLibrary
			, String rightLibrary
			, String leftLibraryFallback
			, String centerLibraryFallback
			, String rightLibraryFallback
			) {
		this.leftLibrary = leftLibrary;
		this.leftLibraryFallback = leftLibraryFallback;
		this.centerLibrary = centerLibrary;
		this.centerLibraryFallback = centerLibraryFallback;
		this.rightLibrary = rightLibrary;
		this.rightLibraryFallback = rightLibraryFallback;
	}

}
