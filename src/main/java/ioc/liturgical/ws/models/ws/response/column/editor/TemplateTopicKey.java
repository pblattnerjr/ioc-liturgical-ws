package ioc.liturgical.ws.models.ws.response.column.editor;

import com.google.gson.annotations.Expose;

import net.ages.alwb.utils.core.datastores.json.models.AbstractModel;

/**
 * Holds the value for a row in the templateKeys array
 * @author mac002
 *
 */
public class TemplateTopicKey extends AbstractModel {
	@Expose String _id = "";
	@Expose String key = "";
	@Expose int libKeysIndex = 0; // index into the array of libraryKeys
	
	public TemplateTopicKey() {
		super();
	}
	public TemplateTopicKey(boolean printPretty) {
		super();
		super.setPrettyPrint(printPretty);
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public int getLibKeysIndex() {
		return libKeysIndex;
	}

	public void setLibKeysIndex(int libKeysIndex) {
		this.libKeysIndex = libKeysIndex;
	}
}
