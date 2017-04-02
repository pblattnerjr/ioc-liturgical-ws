package ioc.liturgical.ws.constants;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.google.gson.JsonObject;

import ioc.liturgical.ws.models.db.links.LinkRefersToBiblicalText;
import ioc.liturgical.ws.models.db.supers.LTKDb;
import net.ages.alwb.utils.core.datastores.json.models.AbstractModel;
import net.ages.alwb.utils.core.datastores.json.models.ModelHelpers;

/**
 * Types of Relationships in the Neo4j Database
 * @author mac002
 *
 */
public enum RELATIONSHIP_TYPES {
	REFERS_TO_BIBLICAL_TEXT(
			"REFERS_TO_BIBLICAL_TEXT"
			, "Doc makes reference to Biblical text."
			, new LinkRefersToBiblicalText("","",""))
	;
	public String typename;
	public String description;
	public LTKDb schema;
	
	private RELATIONSHIP_TYPES(
			String typename
			, String description
			, LTKDb schema
			) {
		this.typename = typename;
		this.description = description;
		this.schema = schema;
	}

	/**
	 * Get a map where:
	 *    relationship typename is the key
	 *    value is a list of properties used for that relationship type
	 * @return
	 */
	public static Map<String,List<String>> propertyMap() {
		Map<String, List<String>> result = new TreeMap<String,List<String>>();
		for (RELATIONSHIP_TYPES t : RELATIONSHIP_TYPES.values()) {
			result.put(t.typename, ModelHelpers.getPropertiesList(t.schema));
		}
		return result;
	}

	/**
	 * Creates a Json Object with keys that are the typename and
	 * values that are a JsonArray of the property names for the
	 * schema assocated with that typename.
	 * @return
	 */
	public static JsonObject propertyJson() {
		JsonObject result = new JsonObject();
		for (RELATIONSHIP_TYPES t : RELATIONSHIP_TYPES.values()) {
			result.add(t.typename, ModelHelpers.getPropertiesJsonArray(t.schema));
		}
		return result;
	}

}
