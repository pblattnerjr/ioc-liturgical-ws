package ioc.liturgical.ws.models.db.returns;

import com.github.reinert.jjschema.Attributes;
import com.google.gson.annotations.Expose;

import ioc.liturgical.ws.annotations.UiWidget;
import ioc.liturgical.ws.constants.Constants;
import net.ages.alwb.utils.core.datastores.json.models.AbstractModel;

/**
 * Used for a query result to display a relationship and the nodes
 * on either side as a table row, with the id and value of the
 * left, link, and right displayed as columns.
 * 
 * @author mac002
 *
 */

@Attributes(title = "Table Row for Text Refers To Text", description = "")
public class LinkRefersToTextToTextTableRow extends AbstractModel {

	private static String fromHandle = "from";
	private static String linkHandle = "link";
	private static String toHandle = "to";
	
	@Attributes(required = true, readonly = true, description = "The unique identifier of schema for this doc.")
	@Expose String _valueSchemaId = "LinkRefersToTextToTextTableRow";
	
	@Attributes(required = true, readonly = true, description = "The unique identifier of the 'from' doc.")
	@Expose String fromId = "";

	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(required = true, description = "The value of the 'from' doc.")
	@Expose String fromValue = "";
	
	@Attributes(required = true, readonly = true, description = "The unique identifier of the 'link' doc.")
	@Expose String linkId = "";

	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(required = true, description = "The value of the 'link' doc.")
	@Expose String linkValue = "";
	
	@Attributes(required = true, readonly = true, description = "The unique identifier of the 'to' doc.")
	@Expose String toId = "";

	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(required = true, description = "The value of the 'to' doc.")
	@Expose String toValue = "";
	
	public LinkRefersToTextToTextTableRow() {
		super();
		this.serialVersionUID = 1.1;
	}

	public String get_valueSchemaId() {
		return _valueSchemaId;
	}

	public void set_valueSchemaId(String _valueSchemaId) {
		this._valueSchemaId = _valueSchemaId;
	}


	public String getFromHandle() {
		return fromHandle;
	}

	public void setFromHandle(String fromHandle) {
		this.fromHandle = fromHandle;
	}

	public String getLinkHandle() {
		return linkHandle;
	}

	public void setLinkHandle(String linkHandle) {
		this.linkHandle = linkHandle;
	}

	public String getToHandle() {
		return toHandle;
	}

	public void setToHandle(String toHandle) {
		this.toHandle = toHandle;
	}

	public String getFromId() {
		return fromId;
	}

	public void setFromId(String fromId) {
		this.fromId = fromId;
	}

	public String getFromValue() {
		return fromValue;
	}

	public void setFromValue(String fromValue) {
		this.fromValue = fromValue;
	}

	public String getLinkId() {
		return linkId;
	}

	public void setLinkId(String linkId) {
		this.linkId = linkId;
	}

	public String getLinkValue() {
		return linkValue;
	}

	public void setLinkValue(String linkValue) {
		this.linkValue = linkValue;
	}

	public String getToId() {
		return toId;
	}

	public void setToId(String toId) {
		this.toId = toId;
	}

	public String getToValue() {
		return toValue;
	}

	public void setToValue(String toValue) {
		this.toValue = toValue;
	}

	public static String getReturnClause() {
		StringBuffer result = new StringBuffer();
		result.append(" return ");
		result.append(linkHandle + ".id as id, ");
		result.append(linkHandle + ".library as library, ");
		result.append(linkHandle + ".topic as fromId, ");
		result.append("type(" + linkHandle + ") as type, ");
		result.append(linkHandle + ".key as toId ");
		result.append("order by fromId + type + toId ascending");
		return result.toString();
	}

}
