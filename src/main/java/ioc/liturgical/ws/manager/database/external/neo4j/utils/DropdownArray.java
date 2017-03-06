package ioc.liturgical.ws.manager.database.external.neo4j.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.annotations.Expose;

import net.ages.alwb.utils.core.datastores.json.models.AbstractModel;

public class DropdownArray extends AbstractModel {
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

}
