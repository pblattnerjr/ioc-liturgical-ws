package ioc.liturgical.ws.models.ws.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;

import net.ages.alwb.utils.core.datastores.json.models.AbstractModel;

public class ValueSchema extends AbstractModel {
	private static final Logger logger = LoggerFactory.getLogger(ValueSchema.class);

	@Expose public JsonObject schema;
	@Expose public JsonObject uiSchema;
	@Expose public WsPaths paths = null;
	
	public ValueSchema(AbstractModel obj) {
		super();
		this.schema = obj.toJsonSchemaObject();
		this.uiSchema = obj.toJsonUiSchemaObject();
		this.paths = obj.resolveEndpointPaths();
	}

	public JsonObject getSchema() {
		return schema;
	}

	public void setSchema(JsonObject schema) {
		this.schema = schema;
	}

	public JsonObject getUiSchema() {
		return uiSchema;
	}

	public void setUiSchema(JsonObject uiSchema) {
		this.uiSchema = uiSchema;
	}

	public WsPaths getPaths() {
		return paths;
	}

	public void setPaths(WsPaths paths) {
		this.paths = paths;
	}

}
