package net.ages.alwb.utils.core.datastores.json.models;


import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;

import net.ages.alwb.utils.core.datastores.json.exceptions.BadIdException;

/**
 * Provides an entry whose value is a String.
 * 
* This class can be used to represent AGES ares data.
* 
* Consider the file actors_gr_GR_cog.ares
* with the key
* Priest = "PRIEST"
* 
* This maps to a LTKVString as:
* 
 * Library = AGES domain, e.g. gr_gr_cog
 * Topic = AGES topic, e.g. actors
 * Key = AGES key, e.g. Priest
 * Value = Priest
 * 
 * When storing in a Guava table:
 * 
 * RowKey = Library|topic
 * ColumnKey = key 
 * 
 * @author mac002
 *
 */
public class LTKVString extends LTK {
	@Expose String value;
	
	
	/**
	 * Constructor
	 * The JsonObject value must include a property _id
	 * whose value is of the form topic|key
	 * @param value
	 */
	public LTKVString(JsonObject value) throws BadIdException {
		super(value.get("_id").getAsString());
		if (value.has("value")) {
			this.value = value.get("value").getAsString();
		} else {
			this.value = value.toString();
		}
	}

	public LTKVString(
			String _id
			, String value
			) throws BadIdException {
		super(_id);
		this.value = value;
	}
	
	public LTKVString(
			String library
			, String topic
			, String key
			, String value)
		throws BadIdException {
		super(library, topic, key);
		this.value =  value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}
