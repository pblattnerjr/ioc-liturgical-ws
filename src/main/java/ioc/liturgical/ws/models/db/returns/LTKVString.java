package ioc.liturgical.ws.models.db.returns;

import com.github.reinert.jjschema.Attributes;
import com.google.gson.annotations.Expose;

import ioc.liturgical.ws.annotations.UiWidget;
import ioc.liturgical.ws.constants.Constants;
import net.ages.alwb.utils.core.datastores.json.models.AbstractModel;
import net.ages.alwb.utils.core.id.managers.IdManager;

@Attributes(title = "LTKVString", description = "A document that has text as its value.")
public class LTKVString extends AbstractModel {

	private IdManager idManager = null;
	
	@Attributes(required = true, readonly = true, description = "The unique identifier of schema for this doc.")
	@Expose String _valueSchemaId = "LTKVString";
	
	@Attributes(required = true, readonly = true, description = "The unique identifier of this doc.")
	@Expose String _id = "";

	@Attributes(required = true, description = "The library (domain) part of the ID.")
	@Expose String library = "";

	@Attributes(required = true, description = "The topic part of the ID.")
	@Expose String topic = "";

	@Attributes(required = true, description = "The key part of the ID.")
	@Expose String key = "";

	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(required = true, description = "Description of the library.")
	@Expose String value = "";
	
	public LTKVString() {
		super();
		this.serialVersionUID = 1.1;
	}

	public LTKVString(String id, String value) {
		super();
		this.serialVersionUID = 1.1;
		this._id = id;
		idManager = new IdManager(id);
		this.library = idManager.getLibrary();
		this.topic = idManager.getTopic();
		this.key = idManager.getKey();
		this.value = value;
	}

	public LTKVString(
			String library
			, String topic
			, String key
			, String value) {
		super();
		this.serialVersionUID = 1.1;
		idManager = new IdManager(library, topic, key);
		this._id = idManager.getId();
		this.library = library;
		this.topic = topic;
		this.key = key;
		this.value = value;
	}
	
	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
		updateId();
	}

	private void updateId() {
		idManager = new IdManager(this.library, this.topic, this.key);
		this._id = idManager.getId();
	}
	public String getKey() {
		return key;
	}


	public void setKey(String key) {
		this.key = key;
		updateId();
	}


	public String getValue() {
		return value;
	}


	public void setValue(String value) {
		this.value = value;
	}

	public String getLibrary() {
		return library;
	}

	public void setLibrary(String library) {
		this.library = library;
		updateId();
	}

	public String get_valueSchemaId() {
		return _valueSchemaId;
	}

	public void set_valueSchemaId(String _valueSchemaId) {
		this._valueSchemaId = _valueSchemaId;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

}
