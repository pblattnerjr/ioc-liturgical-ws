package ioc.liturgical.ws.models.db.supers;

import com.google.gson.annotations.Expose;

import ioc.liturgical.ws.constants.Constants;
import net.ages.alwb.utils.core.datastores.json.models.AbstractModel;
import net.ages.alwb.utils.core.error.handling.ErrorUtils;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.reinert.jjschema.Attributes;

/**
 * Copy this code as the starting point for POJOs that have a Library, Topic, Key
 * as their ID and will be used to generate UI Schemas.  The issue is that JJSchema
 * does not support generation of attributes from supertypes.
 * @author mac002
 *
 */
@Attributes(title = "Class Name", description = "A {class name} {description}")
public class LTK extends AbstractModel {
	private static final Logger logger = LoggerFactory.getLogger(LTK.class);
	
	@Attributes(required = true, readonly = true, description = "The unique identifier for the schema.")
	@Expose String _valueSchemaId = "";

	@Attributes(required = true, readonly = true, description = "The unique identifier, that is, the ID.")
	@Expose String id = "";

	@Attributes(required = true, readonly = false,  description = "The library part of the ID, that is, typically the domain.")
	@Expose String library = "";

	@Attributes(required = true, readonly = false, description = "The topic part of the ID.")
	@Expose String topic = "";

	@Attributes(required = true, readonly = false, description = "The key part of the ID")
	@Expose String key = "";

	@Attributes(readonly=true, description="Labels to use when searching for this Reference.")
	@Expose List<String> labels = new ArrayList<String>();
	

	public LTK(
			String library
			, String topic
			, String key
			, String schema
			, double serialVersion
			) {
		super();
		this.serialVersionUID = serialVersion;
		this.library = library;
		this.topic = topic;
		this.key = key;
		this.joinIdParts();
		this._valueSchemaId = schema + ":" + this.serialVersionUID;
	}

	public String getLibrary() {
		return library;
	}

	public void setLibrary(String library) {
		this.library = library;
		joinIdParts();
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
		joinIdParts();
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
		joinIdParts();
	}

	
	private void joinIdParts() {
		this.id = this.library + Constants.ID_DELIMITER + this.topic + Constants.ID_DELIMITER + this.key;
	}

	public List<String> getLabels() {
		return labels;
	}

	public void setLabels(List<String> labels) {
		this.labels = labels;
	}

	public String get_valueSchemaId() {
		return _valueSchemaId;
	}

	public void set_valueSchemaId(String _valueSchemaId) {
		this._valueSchemaId = _valueSchemaId;
	}
	
	public void addLabel(String label) {
		this.labels.add(label);
	}
	
	public boolean hasLabel(String label) {
		return this.labels.contains(label);
	}
	
	public void removeLabel(String label) {
		try {
			this.labels.remove(label);
		} catch (Exception e) {
			ErrorUtils.report(logger, e);
		}
	}
	
	public String getIdAsPath() {
		return this.library + "/" + this.topic + "/" + this.key;
	}

	public String getSchemaAsLabel() {
		String result = this._valueSchemaId;
		try {
			String [] parts = this._valueSchemaId.split(":");
			if (parts.length > 1) {
				result = parts[0];
			}
		} catch (Exception e) {
			// ignore
		}
		return result;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
