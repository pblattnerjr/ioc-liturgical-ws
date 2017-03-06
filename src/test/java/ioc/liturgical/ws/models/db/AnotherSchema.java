package ioc.liturgical.ws.models.db;

import com.google.gson.annotations.Expose;

import net.ages.alwb.utils.core.datastores.json.models.AbstractModel;

public class AnotherSchema extends AbstractModel {
	@Expose String _id;
	@Expose String dataSchema;
	@Expose String uiSchema;
	
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getDataSchema() {
		return dataSchema;
	}
	public void setDataSchema(String dataSchema) {
		this.dataSchema = dataSchema;
	}
	public String getUiSchema() {
		return uiSchema;
	}
	public void setUiSchema(String uiSchema) {
		this.uiSchema = uiSchema;
	}
}
