package ioc.liturgical.ws.models.ws.db;

import com.github.reinert.jjschema.Attributes;
import com.google.gson.annotations.Expose;

import ioc.liturgical.ws.annotations.UiWidget;
import ioc.liturgical.ws.constants.Constants;
import net.ages.alwb.utils.core.datastores.json.models.AbstractModel;

@Attributes(readonly=true)
public class UserAuth extends AbstractModel {
	
	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(description="The description of this authorization.")
	@Expose public String description = "";

	@Attributes(description="The user ID of the person who granted this authorization.")
	@Expose public String grantedBy = "";

	@Attributes(description="The date/time when this authorization was granted.")
	@Expose public String grantedWhen = "";
	
	
	public UserAuth() {
		super();
		this.serialVersionUID = 1.1;
	}


	public String getGrantedBy() {
		return grantedBy;
	}


	public void setGrantedBy(String grantedBy) {
		this.grantedBy = grantedBy;
	}


	public String getGrantedWhen() {
		return grantedWhen;
	}


	public void setGrantedWhen(String grantedWhen) {
		this.grantedWhen = grantedWhen;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}
		
}
