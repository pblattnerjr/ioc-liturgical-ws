package net.ages.alwb.utils.core.datastores.json.models;


import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import ioc.liturgical.ws.annotations.UiWidget;
import ioc.liturgical.ws.constants.Constants;
import ioc.liturgical.ws.models.SchemaException;
import ioc.liturgical.ws.models.SchemaExceptionDescription;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.reinert.jjschema.Attributes;
import com.github.reinert.jjschema.v1.JsonSchemaFactory;
import com.github.reinert.jjschema.v1.JsonSchemaV4Factory;

import net.ages.alwb.utils.core.error.handling.ErrorUtils;

/**
 * Important! Any class that implements this must 
 * 1. have a parameterless contructor that calls super().  
 * Otherwise, gson will be null for the subclass.
 * 
 * 2. set serialVersionUID.
 * 
 * @author mac002
 *
 */
public class AbstractModel {
    private static final Logger logger = LoggerFactory.getLogger(AbstractModel.class);
	protected double serialVersionUID = 1.1;

	protected static Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
	
	public AbstractModel() {
	}

	/**
	 * Used to get an instance from a json string.
	 * You must first instantiate some subclass, then
	 * call this method on it.
	 * 
	 * For example,
	 * 
	 * String json = "{\"name\":\"S2\",\"url\":\"http://www.somewhere.com\",\"_id\":\"servers|S2\"}"
	 * Server entry = new Server(); // where server is some subclass of AbstractModel
	 * entry = (Server) entry.fromJsonString(json);
	 * @param json
	 * @return
	 */
	public AbstractModel fromJsonString(String json) {
		try {
			return gson.fromJson(json, this.getClass());
		} catch (Exception e) {
			return null;
		}
	}
	
	public String toJsonString() {
		String result = null;
		try {
			result = this.gson.toJson(this);
		} catch (Exception e) {
			ErrorUtils.report(logger, e);
			result = null;
		}
		return result;
	}
	
	public JsonObject toJsonObject() {
		JsonObject result = null;
		try {
			result = (new JsonParser()).parse(toJsonString()).getAsJsonObject();
		} catch (Exception e) {
			ErrorUtils.report(logger, e);
			result = null;
		}
		return result;
	}

	public String toJsonSchema() {
		return toJsonSchema(this.getClass());
	}
	
	public String toJsonSchema(@SuppressWarnings("rawtypes") Class theClass) {
		JsonSchemaFactory schemaFactory = new JsonSchemaV4Factory();
		schemaFactory.setAutoPutDollarSchema(true);
		JsonNode productSchema = schemaFactory.createSchema(theClass);
		return productSchema.toString();
	}

	public JsonObject toJsonSchemaObject() {
		return toJsonSchemaObject(this.getClass());
	}

	public JsonObject toJsonSchemaObject(@SuppressWarnings("rawtypes") Class theClass) {
		JsonSchemaFactory schemaFactory = new JsonSchemaV4Factory();
		schemaFactory.setAutoPutDollarSchema(true);
		JsonNode productSchema = schemaFactory.createSchema(theClass);
		return new JsonParser().parse(productSchema.toString()).getAsJsonObject();
	}
		
	public boolean valid(String json) {
		return validate(json).length() == 0;
	}
	
	public String validate(String json) {
		String result = "";
		try {
			JSONObject rawSchema = new JSONObject(toJsonSchema());
			Schema schema = SchemaLoader.load(rawSchema);
			schema.validate(new JSONObject(json));
		} catch (ValidationException e) {
			result = e.toJSON().toString();
		} catch (JSONException e) {
			SchemaExceptionDescription sem = new SchemaExceptionDescription(
					"json"
					, ""
					, "valid json"
					, "missing or invalid json"
					);
			SchemaException se = new SchemaException(
					"/"
					, "valid json"
					, sem.toString()
					);
			result = se.toJsonString();
		}
		return result;
	}
	
	/**
	 * Get a json string for react-jsonschema-form that is the ui-schema.
	 * 
	 * This will include the uiOrder.  The order in the form is based on the order
	 * in which the fields occur in the Java class.  Only fields that have the Expose
	 * annotation will be included.
	 * 
	 * @return
	 */
	public String toUiSchema() {
		return toJsonUiSchemaObject().toString();
	}
	
	/**
	 * To be included in the UiSchema, the following conditions must be met
	 * for a subclass:
	 * - it must use the @Attributes annotation
	 * - it must use the @Expose attribute
	 * - it must be a public property
	 * 
	 * Example:
	 * @Attributes(required = true, description = "The color of the item.")
	 * @Expose public String color = "";
	 * 
	 * The order in which attributes appear on the form is determined as follows:
	 * 
	 * this.class.getFields() presents the properties back using the following principles:
	 * 		Fields are presented in the order in which they are declared
	 * 		Fields are presented from the class, then each successive supertype up the hierarchy
	 * 		Fields that have @Attributes(id="top".... will appear first.
	 *     Fields that have @Attributes(id="bottom".... will appear last
	 *     Fields that lack an id attribute will appear in the middle.
	 *     
	 * @return
	 */
	public JsonObject toJsonUiSchemaObject() {
		JsonObject json = new JsonObject();
		List<String> topFieldNames = new ArrayList<String>();
		List<String> middleFieldNames = new ArrayList<String>();
		List<String> bottomFieldNames = new ArrayList<String>();
		if (this.getClass().isAnnotationPresent(com.github.reinert.jjschema.Attributes.class)) {
			Attributes attributes = this.getClass().getAnnotation(com.github.reinert.jjschema.Attributes.class);
			if (attributes.readonly()) {
				json.addProperty("ui:readonly", true);
			}
		}
		for (Field field : this.getClass().getFields()) {
				if (field.isAnnotationPresent(com.google.gson.annotations.Expose.class)) {
					if (field.isAnnotationPresent(com.github.reinert.jjschema.Attributes.class)) {
						Attributes attributes = field.getAnnotation(Attributes.class);
						if (attributes.readonly()) {
							JsonObject widget = new JsonObject();
							widget.addProperty("ui:readonly", true);
							json.add(field.getName(), widget);
						}
						if (attributes.id() == null) {
							middleFieldNames.add(field.getName());
						} else {
							if (attributes.id().equals("top")) {
								topFieldNames.add(field.getName());
							} else {
								bottomFieldNames.add(field.getName());
							}

						}
					} else {
						middleFieldNames.add(field.getName());
					}
					if (field.isAnnotationPresent(UiWidget.class)) {
						UiWidget uiWidget = field.getAnnotation(UiWidget.class);
						if (! uiWidget.value().startsWith(Constants.UI_WIDGET_DOMAINS )&& 
								! uiWidget.value().startsWith(Constants.UI_WIDGET_USERS)) {
							JsonObject widget = new JsonObject();
							widget.addProperty("ui:widget", uiWidget.value());
							json.add(field.getName(), widget);
						}
					}
				}
		}
		JsonArray fieldNames = new JsonArray();
		for (String name : topFieldNames) {
			fieldNames.add(name);
		}
		for (String name : middleFieldNames) {
			fieldNames.add(name);
		}
		for (String name : bottomFieldNames) {
			fieldNames.add(name);
		}
		json.add("ui:order", fieldNames);
		return json;
	}

	public double schemaIdAsNumber() {
		return serialVersionUID;
	}
	
	public String schemaIdAsString() {
		return this.getClass().getSimpleName() + ":" + String.valueOf(serialVersionUID);
	}


	public void setPrettyPrint(boolean printPretty) {
	    	if (printPretty) {
	        	gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
	    	} else {
	        	gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();        		
	    	}
	}
	

}
