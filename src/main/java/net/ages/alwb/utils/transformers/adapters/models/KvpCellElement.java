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
public class KvpCellElement extends AbstractModel {
	@Expose public String tag = "";
	@Expose public String parentClassName = "";
	@Expose public String dataDomain = "";
	@Expose public String dataKey = ""; // the key we will use to retrieve values.  It might not be the original
	@Expose public String dataOriginal = ""; // the original key in case we need to use it instead
	@Expose public String topicKey = "";
	

	public KvpCellElement() {
		super();
	}
	
	public KvpCellElement(TemplateElement template) {
		super();
		this.tag = template.getTag();
		this.parentClassName = template.getParentClassName();
		this.dataDomain = template.getDataDomain();
		this.dataKey = template.getDataKey();
		this.dataOriginal = template.getDataOriginal();
		this.topicKey = template.getTopicKey();
	}
	
	public KvpCellElement(boolean printPretty) {
		super();
		super.setPrettyPrint(printPretty);
	}

	public KvpCellElement(TemplateElement template, boolean printPretty) {
		super();
		super.setPrettyPrint(printPretty);
		this.tag = template.getTag();
		this.parentClassName = template.getParentClassName();
		this.dataDomain = template.getDataDomain();
		this.dataKey = template.getDataKey();
		this.dataOriginal = template.getDataOriginal();
		this.topicKey = template.getTopicKey();
}

	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
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
