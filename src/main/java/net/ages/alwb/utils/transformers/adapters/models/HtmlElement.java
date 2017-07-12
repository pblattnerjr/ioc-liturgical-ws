package net.ages.alwb.utils.transformers.adapters.models;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

import net.ages.alwb.utils.core.datastores.json.models.AbstractModel;

public class HtmlElement extends AbstractModel {
	@Expose public String key = ""; // required when iterating a Javascript array of HtmlElement
	@Expose public String tag = "";
	@Expose public String className = "";
	@Expose public String dataKey = ""; // the key we will use to retrieve values.  It might not be the original
	@Expose public String dataOriginal = ""; // the original key in case we need to use it instead
	@Expose public String topicKey = "";
	@Expose public List<HtmlElement> children = new ArrayList<HtmlElement>();
	

	public HtmlElement() {
		super();
	}
	public HtmlElement(boolean printPretty) {
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
	public List<HtmlElement> getChildren() {
		return children;
	}
	public void setChildren(List<HtmlElement> children) {
		this.children = children;
	}
	public void addChild(HtmlElement child) {
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
}
