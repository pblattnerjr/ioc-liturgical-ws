package ioc.liturgical.ws.models.db.supers;

import com.google.gson.annotations.Expose;

import ioc.liturgical.ws.annotations.UiWidget;
import ioc.liturgical.ws.constants.Constants;
import ioc.liturgical.ws.constants.db.external.TOPICS;

import java.util.List;

import com.github.reinert.jjschema.Attributes;

/**
 * Copy this code as the starting point for POJOs that have a Library, Topic, Key
 * as their ID and will be used to generate UI Schemas.  The issue is that JJSchema
 * does not support generation of attributes from supertypes.
 * @author mac002
 *
 */
@Attributes(title = "Class Name", description = "A {class name} {description}")
public class LTKDb extends LTK {
	
	@UiWidget(Constants.UI_WIDGET_RADIO)
	@Attributes(id="bottom", required = true, description = "Is this domain active?")
	@Expose public boolean active = true;

	@Attributes(id="bottom",readonly=true, description="The user ID of the person who created it.")
	@Expose public String createdBy = "";

	@Attributes(id="bottom",readonly=true, description="The date/time when it was created.")
	@Expose public String createdWhen = "";
	
	@Attributes(id="bottom",readonly=true, description="The user ID of the person who last modified it.")
	@Expose public String modifiedBy = "";

	@Attributes(id="bottom",readonly=true, description="The date/time when it was last modified.")
	@Expose public String modifiedWhen = "";

	public LTKDb(
			String library
			, String topic
			, String key
			, String schema
			, double serialVersion
			, TOPICS ontologyTopic
			) {
		super(library, topic, key, schema, serialVersion, ontologyTopic);
		this.active = true; 
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedWhen() {
		return createdWhen;
	}

	public void setCreatedWhen(String createdWhen) {
		this.createdWhen = createdWhen;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getModifiedWhen() {
		return modifiedWhen;
	}

	public void setModifiedWhen(String modifiedWhen) {
		this.modifiedWhen = modifiedWhen;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> labels) {
		this.tags = labels;
	}

	public String get_valueSchemaId() {
		return _valueSchemaId;
	}

	public void set_valueSchemaId(String _valueSchemaId) {
		this._valueSchemaId = _valueSchemaId;
	}
	
	/**
	 * subclasses should override this
	 * @param ltk
	 */
	public void setSubClassProperties(String json) {
		
	}

}
