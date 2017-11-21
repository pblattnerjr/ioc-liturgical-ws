package ioc.liturgical.ws.constants;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.ages.alwb.utils.core.datastores.json.models.DropdownItem;

/**
 * 
 * 
 * @author mac002
 *
 */
public enum STATUS {
	EDITING("Editing")  // the record has been assigned to someone to edit it
	, HOLDING("Holding") // the record is on hold. 
	, READY_TO_REVIEW("Ready for an Admin to assign someone to Review it") // the record is ready to be assigned to someone to review it
	, REVIEWING("Reviewing") // the record has been assigned to someone to review
	, READY_TO_EDIT("Ready for an Admin to assign someone to Edit it") // the record is ready to be assigned to someone to edit it
	, READY_TO_FINALIZE("Ready for an Admin to Finalize it") // the record is readly to finalize
	, FINALIZED("Finalized") // the record has been finalized.  
	, READY_TO_RELEASE("Ready for an Admin to release it (move it to the read-only database)") // the record is readly to finalize
	, RELEASED("Released") // the record has been finalized.  
	;

	public String keyname = "";
	
	private STATUS(
			String keyname
			) {
		this.keyname = keyname;
	}

	/**
	 * Converts the enumeration a JsonArray of DropdownItems
	 * where the dropdown item's
	 * value = enum.name
	 * label = enum.keyname
	 * 
	 * @return
	 */
    public static JsonArray toDropdownJsonArray() {
    	JsonArray result = new JsonArray();
    	for (STATUS e : STATUS.values()) {
    		result.add(new DropdownItem(e.keyname, e.name()).toJsonObject());
    	}
    	return result;
    }

	/**
	 * Converts the enumeration a List of DropdownItems
	 * where the dropdown item's
	 * value = enum.name
	 * label = enum.keyname
	 * 
	 * @return
	 */
    public static List<JsonObject> toDropdownList() {
    	List<JsonObject> result = new ArrayList<JsonObject>();
    	for (STATUS e : STATUS.values()) {
    		result.add(new DropdownItem(e.keyname, e.name()).toJsonObject());
    	}
    	return result;
    }

		
}
