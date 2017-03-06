package ioc.liturgical.ws.models.ws.response;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;

import ioc.liturgical.ws.models.ws.db.User;
import ioc.liturgical.ws.models.ws.forms.UserAccessForm;
import net.ages.alwb.utils.core.datastores.json.models.AbstractModel;

public class ResultUserArray extends AbstractModel {
	@Expose JsonObject uiSchema;
	@Expose JsonObject schema;
	@Expose List<UserAccessForm> items = new ArrayList<UserAccessForm>();
	
	public ResultUserArray() {
		super();
		this.uiSchema = this.toJsonUiSchemaObject();
		this.schema = this.toJsonSchemaObject();
		this.setPrettyPrint(true);
	}
	
	public void setItems(List<UserAccessForm> items) {
		this.items = items;
	}
	public List<UserAccessForm> getItems() {
		return items;
	}
	
	public void addItem(UserAccessForm item) {
		this.items.add(item);
	}
	
	public JsonObject toJsonSchemaObject() {
		return this.toJsonSchemaObject(UserAccessForm.class);
	}

	public JsonObject toJsonUiSchemaObject() {
		UserAccessForm form = new UserAccessForm();
		return form.toJsonUiSchemaObject();
	}
	
	public static void main(String[] args) {
		AbstractResponse<UserAccessForm> result = new AbstractResponse<UserAccessForm>(new UserAccessForm(),true);
		UserAccessForm item = new UserAccessForm();
		item.setEmail("mickeymouse@disney.com");
		item.setFirstname("Mickey");
		item.setSurname("Mouse");
		item.setUsername("mickeymouse");
		result.addItem(item);
		item = new UserAccessForm();
		item.setEmail("minniemouse@disney.com");
		item.setFirstname("Minney");
		item.setSurname("Mouse");
		item.setUsername("minniemouse");
		result.addItem(item);
		System.out.println(result.toJsonString());
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

}
