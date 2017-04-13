package net.ages.alwb.utils.core.datastores.json.models;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import net.ages.alwb.utils.core.datastores.json.models.AbstractModel;
import net.ages.alwb.utils.core.error.handling.ErrorUtils;

/**
 * The purpose of this class is to provide static methods to supplement those
 * found in the model classes.
 * 
 * The reason they are not methods in the model classes is that Jjschema throws
 * an error if there is a method that has a complex return value.
 * 
 * @author mac002
 *
 */
public class ModelHelpers {
	private static final Logger logger = LoggerFactory.getLogger(ModelHelpers.class);

	protected static Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

	/**
	 * Converts the POJO to a Map using Gson
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> toHashMap(AbstractModel model) {
		try {
			Type type = new TypeToken<Map<String, Object>>() {
			}.getType();
			Map<String, Object> map = new HashMap<String, Object>();
			map = (Map<String, Object>) gson.fromJson(model.toJsonString(), type);
			for (Entry<String, Object> entry : map.entrySet()) {
				if (entry.getValue() instanceof com.google.gson.internal.LinkedTreeMap) {
					String value = gson.toJson(entry.getValue());
					map.put(entry.getKey(), value);
				}
			}
			return map;
		} catch (Exception e) {
			ErrorUtils.report(logger, e);
			return null;
		}
	}

	/**
	 * Gets a properties map using 'props' as the key This is useful when
	 * passing a properties parameter, e.g. to Neo4j
	 * 
	 * @return
	 */
	public static Map<String, Object> getAsPropertiesMap(AbstractModel model) {
		return getAsPropertiesMap("props", model);
	}

	/**
	 * Gets a properties map with the parameter key as its key. This is useful
	 * when passing a properties parameter, e.g. to Neo4j
	 * 
	 * @return
	 */
	public static Map<String, Object> getAsPropertiesMap(String key, AbstractModel model) {
		Map<String, Object> props = new HashMap<String, Object>();
		props.put(key, toHashMap(model));
		return props;
	}
	
	/**
	 * Get the properties of the model as a String list.
	 * They will not be sorted.
	 * @param model
	 * @return
	 */
	public static List<String> getPropertiesList(AbstractModel model) {
		List<String> result = new ArrayList<String>();
		List<String> props = new ArrayList<String>(toHashMap(model).keySet());
		for (String prop : props) {
			result.add(prop);
		}
		return result;
	}

	/**
	 * Get the properties as a list of Dropdown Items in a JsonArray.
	 * They will be sorted.
	 * @param model
	 * @return
	 */
	public static JsonArray getPropertiesAsDropdownItems(AbstractModel model) {
		JsonArray result = new JsonArray();
		result.add(new DropdownItem("Any","*").toJsonObject());
		List<String> props = getPropertiesList(model);
		java.util.Collections.sort(props);
		for (String prop : props) {
			result.add(new DropdownItem(prop).toJsonObject());
		}
		return result;
	}
}
