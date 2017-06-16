package ioc.liturgical.ws.constants;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;

import ioc.liturgical.ws.models.ws.db.Utility;
import net.ages.alwb.utils.core.datastores.json.models.DropdownItem;
import static java.util.Arrays.asList;

/**
 * The utilities that can be run via the web service interface
 * 
 * @author mac002
 *
 */
public enum UTILITIES {
	EngSensesOne(
			"EngSensesOne"
			, 	"Gets English One senses"
			, new ArrayList<UTILITIES>()
			, new ArrayList<UTILITIES>()
			) 
	, Tokenize(
			"Tokenize"
			, 	"Tokenizes each word in the value property, removing punctuation, but preserving the diacritics. For each token that does not yet exist, creates a Token record in the external database. Also records the number of occurrences. If it already exists, it upates the number of occurrences."
			, new ArrayList<UTILITIES>()
			, new ArrayList<UTILITIES>()
			) 
	, FetchPerseusParses(
			"FetchPerseusParses"
			, "For each token from gr_gr_cog, gets the possible parses from Perseus and stores them."
			, new ArrayList<UTILITIES>(asList(UTILITIES.Tokenize))
			, new ArrayList<UTILITIES>()
			) 
	;

	public String keyname = "";
	public String description = "";
	public List<UTILITIES> preProcessors = new ArrayList<UTILITIES>();
	public List<UTILITIES> postProcessors = new ArrayList<UTILITIES>();
	
	private UTILITIES(
			String keyname
			, String description
			, List<UTILITIES> preProcessors
			, List<UTILITIES> postProcessors
			) {
		this.keyname = keyname;
		this.description = description;
		this.preProcessors = preProcessors;
		this.postProcessors = postProcessors;
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
    	for (UTILITIES e : UTILITIES.values()) {
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
    	for (UTILITIES e : UTILITIES.values()) {
    		result.add(new DropdownItem(e.keyname, e.name()));
    	}
    	return result;
    }

    /**
     * Creates a set of instances of Utility using
     * the information from the enum values
     * @return
     */
    public static List<Utility> toUtilityList() {
    	List<Utility> result = new ArrayList<Utility>();
    	for (UTILITIES e : UTILITIES.values()) {
    		Utility u = new Utility();
    		u.setName(e.keyname);
    		u.setDescription(e.description);
    		for (UTILITIES p : e.preProcessors) {
    			u.putPreProcessor(p);
    		}
    		for (UTILITIES p : e.postProcessors) {
    			u.putPostProcessor(p);
    		}
    		result.add(u);
    	}
    	return result;
    }
		
}
