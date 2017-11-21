package net.ages.alwb.utils.transformers.adapters.models;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

import net.ages.alwb.utils.core.datastores.json.models.AbstractModel;

/**
 * Represents an element found in a MetaTemplate.
 * 
 * @author mac002
 *
 */
public class TemplateElement extends AbstractModel {
	@Expose public String key = ""; // required when iterating a Javascript array of HtmlElement
	@Expose public String tag = "";
	@Expose public String className = "";
	@Expose public String parentClassName = "";
	@Expose public String dataDomain = "";
	@Expose public String dataKey = ""; // the key we will use to retrieve values.  It might not be the original
	@Expose public String dataOriginal = ""; // the original key in case we need to use it instead
	@Expose public String topicKey = "";
	@Expose public List<TemplateElement> children = new ArrayList<TemplateElement>();
	

	public TemplateElement() {
		super();
	}
	public TemplateElement(boolean printPretty) {
		super();
		super.setPrettyPrint(printPretty);
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public List<TemplateElement> getChildren() {
		return children;
	}
	public void setChildren(List<TemplateElement> children) {
		this.children = children;
	}
	public void addChild(TemplateElement child) {
		this.children.add(child);
	}
	public String getDataKey() {
		return dataKey;
	}
	public void setDataKey(String dataKey) {
		this.dataKey = dataKey;
	}
	public String getTopicKey() {
		return topicKey;
	}
	public void setTopicKey(String topicKey) {
		this.topicKey = topicKey;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getDataOriginal() {
		return dataOriginal;
	}
	public void setDataOriginal(String dataOriginal) {
		this.dataOriginal = dataOriginal;
	}
	public String getDataDomain() {
		return dataDomain;
	}
	public void setDataDomain(String dataDomain) {
		this.dataDomain = dataDomain;
	}
	public String getParentClassName() {
		return parentClassName;
	}
	public void setParentClassName(String parentClassName) {
		this.parentClassName = parentClassName;
	}
}
