package ioc.liturgical.ws.models.options;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.ages.alwb.utils.core.datastores.json.models.AbstractModel;

public class DropdownOptions extends AbstractModel {
	
	public List<DropdownOptionEntry> options = new ArrayList<DropdownOptionEntry>();
	
	public DropdownOptions() {
		super();
	}

	public List<DropdownOptionEntry> getOptions() {
		return options;
	}

	public void setOptions(List<DropdownOptionEntry> options) {
		this.options = options;
	}
	
	public void addOption(DropdownOptionEntry entry) {
		this.options.add(entry);
	}
	
	/**
	 * Adds an option using the same string for both the value and label.
	 * @param s
	 */
	public void addSingleton(String s) {
		this.options.add(new DropdownOptionEntry(s,s));
	}
	
	/**
	 * Returns the Options as a JsonObject with:
	 * key = "options"
	 * value = JsonArray of DropdownOptionEntry
	 */
	@Override
	public JsonObject toJsonObject() {
		JsonObject result = new JsonObject();
		JsonArray array = new JsonArray();
		for (DropdownOptionEntry entry : options) {
			array.add(entry.toJsonObject());
		}
		result.add("options", array);
		return result;
	}
	
	/**
	 * Returns the options as a JsonArray.
	 * @return
	 */
	public JsonArray toJsonArray() {
		return this.toJsonObject().get("options").getAsJsonArray();
	}
}
