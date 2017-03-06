package ioc.liturgical.ws.models.ws.db;

import com.github.reinert.jjschema.Attributes;
import com.google.gson.annotations.Expose;

import ioc.liturgical.ws.annotations.UiWidget;
import ioc.liturgical.ws.constants.Constants;
import net.ages.alwb.utils.core.datastores.json.models.AbstractModel;

@Attributes(title = "Label", description = "One or more labels can be attached to a doc in the backend database.  Labels are used for finding docs that meet certain criteria.")
public class Label extends AbstractModel {
	
	@Attributes(required = true, description = "Label (code used in database)")
	@Expose String label = "";

	@Attributes(required = true, description = "Title (meaning of the label code)")
	@Expose String title = "";

	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(required = true, description = "Description of the library.")
	@Expose String description = "";
	
	@UiWidget(Constants.UI_WIDGET_RADIO)
	@Attributes(required = true, description = "Is this label active?")
	@Expose boolean active = true;

	@Attributes(readonly=true, description="The user ID of the person who created it.")
	@Expose String createdBy = "";

	@Attributes(readonly=true, description="The date/time when it was created.")
	@Expose String createdWhen = "";
	
	@Attributes(readonly=true, description="The user ID of the person who last modified it.")
	@Expose String modifiedBy = "";

	@Attributes(readonly=true, description="The date/time when it was last modified.")
	@Expose String modifiedWhen = "";
	
	public Label() {
		super();
		this.serialVersionUID = 1.1;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
