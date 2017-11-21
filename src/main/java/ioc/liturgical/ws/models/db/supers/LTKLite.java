package ioc.liturgical.ws.models.db.supers;

import com.google.gson.annotations.Expose;

import ioc.liturgical.ws.constants.Constants;
import ioc.liturgical.ws.constants.db.external.TOPICS;
import ioc.liturgical.ws.constants.STATUS;
import ioc.liturgical.ws.constants.VISIBILITY;
import net.ages.alwb.utils.core.datastores.json.models.AbstractModel;

import com.github.reinert.jjschema.Attributes;

/**
 * A stripped down version of LTK.
 *  
 * @author mac002
 *
 */
@Attributes(title = "Class Name", description = "A {class name} {description}")
public class LTKLite extends AbstractModel {
	
	@Attributes(id = "bottom", required = true, readonly = true, minLength = 1, description = "The unique identifier, that is, the ID.")
	@Expose public String id = "";

	@Attributes(id = "bottom", required = true, readonly = true, minLength = 1,  description = "The library part of the ID, that is, typically the domain.")
	@Expose public String library = "";

	@Attributes(id = "bottom", required = true, readonly = true, minLength = 1, description = "The topic part of the ID.")
	@Expose public String topic = "";

	@Attributes(id = "bottom", required = true, readonly = true, minLength = 1, description = "The key part of the ID")
	@Expose public String key = "";

	
	@Attributes(id = "bottom", required = true, readonly = true, minLength = 1, description = "The unique identifier for the schema.")
	@Expose public String _valueSchemaId = "";
	
	/**
	 * Visibility is at the record level.  It works in conjunction with the domain of the record ID.
	 * 
	 */
	@Attributes(id="bottom", readonly=false, required=true, description="If the library is your user library, you can set the visibility to private.  The record will then only be visible to you personally.  If you set it to private, anyone to whom you have granted read or write access to your user library will be able to see it.  If you set visibility to public, it means it is eligible to be moved to the public, read-only database.")
	@Expose public VISIBILITY visibility = VISIBILITY.PUBLIC;

	@Attributes(id="bottom", readonly=false, required=true, description="Status of this record.  'Editing' means it is either a draft or undergoing revision.  'Reviewing' means it is being reviewed for someone's approval.  'Finalized' means work has finished on this record.  It is not being edited or reviewed at this time. 'Ready for...' means it is ready for someone to be assigned to the next step.")
	@Expose public STATUS status = STATUS.EDITING;

	public LTKLite(
			String library
			, String topic
			, String key
			, String schema
			, double serialVersion
			) {
		super();
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
	
	public String toTopicKey() {
		return this.topic + Constants.ID_DELIMITER + this.key;
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

	public String get_valueSchemaId() {
		return _valueSchemaId;
	}

	public void set_valueSchemaId(String _valueSchemaId) {
		this._valueSchemaId = _valueSchemaId;
	}
	
	
	public String toIdAsPath() {
		return this.library + "/" + this.topic + "/" + this.key;
	}

	public String toSchemaAsLabel() {
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

	
	public VISIBILITY getVisibility() {
		return visibility;
	}

	public void setVisibility(VISIBILITY visibility) {
		this.visibility = visibility;
	}

	public STATUS getStatus() {
		return status;
	}

	public void setStatus(STATUS status) {
		this.status = status;
	}

}
