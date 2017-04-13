package ioc.liturgical.ws.models.ws.db;

import java.util.ArrayList;
import java.util.List;

import com.github.reinert.jjschema.Attributes;
import com.google.gson.annotations.Expose;

import ioc.liturgical.ws.annotations.UiWidget;
import ioc.liturgical.ws.constants.Constants;
import net.ages.alwb.utils.core.datastores.json.models.AbstractModel;

@Attributes(title = "Domain", description = "A domain identifies the text of a doc as being in a specific language as spoken in a specific country for a specific realm.  A realm can be a version of a translation, e.g. the King James Version (KJV), or a particular translator, e.g. by Fr. Seraphim Dedes (dedes), or for a particular metropolis, e.g. the Orthodox Church of Kenya (oak)")
public class Domain extends AbstractModel {
	
	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(required = true, description = "Description of the library.")
	@Expose public String description = "";
	
	@UiWidget(Constants.UI_WIDGET_RADIO)
	@Attributes(required = true, description = "Is this domain active?")
	@Expose public boolean active = true;

	@UiWidget(Constants.UI_WIDGET_RADIO)
	@Attributes(required = true, description = "Is this domain public?")
	@Expose public boolean isPublic = true;

	@UiWidget(Constants.UI_WIDGET_CHECKBOXES)
	@Attributes(required = true, description = "Doc Types for this domain.")
	@Expose public List<String> labels = new ArrayList<String>();

	@Attributes(readonly=true, description="The user ID of the person who created it.")
	@Expose public String createdBy = "";

	@Attributes(readonly=true, description="The date/time when it was created.")
	@Expose public String createdWhen = "";
	
	@Attributes(readonly=true, description="The user ID of the person who last modified it.")
	@Expose public String modifiedBy = "";

	@Attributes(readonly=true, description="The date/time when it was last modified.")
	@Expose public String modifiedWhen = "";


	public Domain() {
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

	public boolean isPublic() {
		return isPublic;
	}

	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}

	public List<String> getLabels() {
		return labels;
	}

	public void setLabels(List<String> labels) {
		this.labels = labels;
	}

}
