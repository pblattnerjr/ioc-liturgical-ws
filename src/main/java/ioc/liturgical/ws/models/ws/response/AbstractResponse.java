package ioc.liturgical.ws.models.ws.response;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;

import net.ages.alwb.utils.core.datastores.json.models.AbstractModel;

/**
 * Provides a means to return back to the REST caller not just the requested
 * data, but the JSON schema to render the objects and the UI Schema.
 * 
 * These can be used by react-jsonschema-form on the client side.
 * 
 * @author mac002
 *
 * @param <T>
 */
public class AbstractResponse <T extends AbstractModel> extends AbstractModel {
	@Expose JsonObject uiSchema;
	@Expose JsonObject schema;
	@Expose List<T> items = new ArrayList<T>();
	private T instanceOfT = null;
	
	/**
	 * 
	 * @param instanceOfT, e.g. new User().  If this will be an empty singleton, 
	 *                            just pass in a new object.  If it will have data, pass in the first item.
	 * @param prettyPrint, true if want the json to be pretty printed.
	 */
	public AbstractResponse(
			T instanceOfT
			, boolean prettyPrint) {
		super();
		this.instanceOfT = instanceOfT;
		this.items.add(instanceOfT);
		this.uiSchema = this.toJsonUiSchemaObject();
		this.schema = this.toJsonSchemaObject();
		this.setPrettyPrint(true);
	}

	public JsonObject getUiSchema() {
		return uiSchema;
	}

	public void setUiSchema(JsonObject uiSchema) {
		this.uiSchema = uiSchema;
	}

	public JsonObject getSchema() {
		return schema;
	}

	public void setSchema(JsonObject schema) {
		this.schema = schema;
	}

	public void addItem(T item) {
		this.items.add(item);
	}
	
	public List<T> getItems() {
		return items;
	}

	public void setItems(List<T> items) {
		this.items = items;
	}
	
	public JsonObject toJsonSchemaObject() {
		return this.instanceOfT.toJsonSchemaObject();
	}

	public JsonObject toJsonUiSchemaObject() {
		return this.instanceOfT.toJsonUiSchemaObject();
	}
	
}
