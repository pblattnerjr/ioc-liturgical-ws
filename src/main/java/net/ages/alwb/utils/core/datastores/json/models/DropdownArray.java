package net.ages.alwb.utils.core.datastores.json.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonArray;
import com.google.gson.annotations.Expose;

import ioc.liturgical.ws.controllers.db.neo4j.Neo4jController;
import net.ages.alwb.utils.core.error.handling.ErrorUtils;

public class DropdownArray extends AbstractModel {
	private static final Logger logger = LoggerFactory.getLogger(DropdownArray.class);
	private String arrayName = "items";
	
	@Expose List<DropdownItem> items = new ArrayList<DropdownItem>();
	
	public DropdownArray() {
		super();
	}
	
	public DropdownArray(String arrayName) {
		super();
		this.arrayName = arrayName;
	}

	public void add(DropdownItem item) {
		items.add(item);
	}

	/**
	 * Adds an option using the same string for both the value and label.
	 * @param s
	 */
	public void addSingleton(String s) {
		items.add(new DropdownItem(s,s));
	}
	
	public List<DropdownItem> getItems() {
		return items;
	}

	public void setItems(Map<String,DropdownItem> map) {
		for (DropdownItem item : map.values()) {
			this.add(item);
		}
	}
	
	public void setItems(List<DropdownItem> items) {
		this.items = items;
	}

	public String getArrayName() {
		return arrayName;
	}

	public void setArrayName(String arrayName) {
		this.arrayName = arrayName;
	}
	
	public String toRenamedJsonString() {
		return this.toJsonString().replaceFirst("items", arrayName);
	}

	public JsonArray toJsonArray() {
		try {
			return this.toJsonObject().get(arrayName).getAsJsonArray();
		} catch (Exception e) {
			ErrorUtils.report(logger, e);
		}
		return null;
	}
}
