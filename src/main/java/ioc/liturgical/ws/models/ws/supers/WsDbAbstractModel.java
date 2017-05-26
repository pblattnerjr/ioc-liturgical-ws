package ioc.liturgical.ws.models.ws.supers;

import com.google.gson.annotations.Expose;

import ioc.liturgical.ws.annotations.UiWidget;
import ioc.liturgical.ws.constants.Constants;
import net.ages.alwb.utils.core.datastores.json.models.AbstractModel;

import com.github.reinert.jjschema.Attributes;

/**
 * Copy this code as the starting point for POJOs that have a Library, Topic, Key
 * as their ID and will be used to generate UI Schemas.  The issue is that JJSchema
 * does not support generation of attributes from supertypes.
 * @author mac002
 *
 */
@Attributes(title = "Class Name", description = "A {class name} {description}")
public class WsDbAbstractModel extends AbstractModel {
	
	@UiWidget(Constants.UI_WIDGET_RADIO)
	@Attributes(id="bottom", required = true, description = "Is this doc active?")
	@Expose public boolean active = true;

	@Attributes(id="bottom",readonly=true, description="The user ID of the person who created it.")
	@Expose public String createdBy = "";

	@Attributes(id="bottom",readonly=true, description="The date/time when it was created. The time is Zulu (i.e., Universal Coordinated Time)")
	@Expose public String createdWhen = "";
	
	@Attributes(id="bottom",readonly=true, description="The user ID of the person who last modified it.")
	@Expose public String modifiedBy = "";

	@Attributes(id="bottom",readonly=true, description="The date/time when it was last modified. The time is Zulu (i.e., Universal Coordinated Time)")
	@Expose public String modifiedWhen = "";

	public WsDbAbstractModel(
			) {
		super();
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

}
