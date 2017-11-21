package ioc.liturgical.ws.constants;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;

import net.ages.alwb.utils.core.datastores.json.models.DropdownItem;

/**
 * Used to set the visibility of a record in the external database.
 * 
 * Personal: viewable / updatable only by the person who created it
 * Private: viewable / updatable only to those to whom access has been granted for the library
 * Public: eligible for being moved to the public read-only database
 * 
 * Prerequisites:
 * 
 * each user must have their own personal domain that uses their userid.  For example,
 * for the user mcolburn, there must be a personal domain 
 * ending with mcolburn, e.g. en_us_mcolburn
 * 
 * 
 * @author mac002
 *
 */
public enum VISIBILITY {
	PERSONAL("personal - only the library owner can see / edit it") 
	, PRIVATE("private - only those granted access can see /edit it") 
	, PUBLIC("public - anyone can see it, but not edit it")
	;

	public String keyname = "";
	
	private VISIBILITY(
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
    	for (VISIBILITY e : VISIBILITY.values()) {
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
    public static List<DropdownItem> toDropdownList() {
    	List<DropdownItem> result = new ArrayList<DropdownItem>();
    	for (VISIBILITY e : VISIBILITY.values()) {
    		result.add(new DropdownItem(e.keyname, e.name()));
    	}
    	return result;
    }

		
}
