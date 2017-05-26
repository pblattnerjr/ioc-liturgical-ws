package ioc.liturgical.ws.models.ws.response;

import com.github.reinert.jjschema.Attributes;
import com.google.gson.annotations.Expose;

import ioc.liturgical.ws.annotations.UiWidget;
import ioc.liturgical.ws.constants.Constants;
import ioc.liturgical.ws.constants.DOMAIN_TYPES;
import ioc.liturgical.ws.constants.STATUS;
import net.ages.alwb.utils.core.datastores.json.models.AbstractModel;

public class DomainWorkflowInfo extends AbstractModel {
	
	@Expose public DOMAIN_TYPES type = DOMAIN_TYPES.USER;

	@Expose public boolean stateEnabled = false;

	@Expose public STATUS defaultStatusAfterEdit = STATUS.FINALIZED;

	@Expose public STATUS defaultStatusAfterFinalization = STATUS.FINALIZED;

	@Expose public boolean workflowEnabled = false;

	public DomainWorkflowInfo () {
		super();
	}

	public boolean isStateEnabled() {
		return stateEnabled;
	}

	public void setStateEnabled(boolean stateEnabled) {
		this.stateEnabled = stateEnabled;
	}

	public STATUS getDefaultStatusAfterEdit() {
		return defaultStatusAfterEdit;
	}

	public void setDefaultStatusAfterEdit(STATUS defaultStatusAfterEdit) {
		this.defaultStatusAfterEdit = defaultStatusAfterEdit;
	}

	public STATUS getDefaultStatusAfterFinalization() {
		return defaultStatusAfterFinalization;
	}

	public void setDefaultStatusAfterFinalization(STATUS defaultStatusAfterFinalization) {
		this.defaultStatusAfterFinalization = defaultStatusAfterFinalization;
	}

	public boolean isWorkflowEnabled() {
		return workflowEnabled;
	}

	public void setWorkflowEnabled(boolean workflowEnabled) {
		this.workflowEnabled = workflowEnabled;
	}

	public DOMAIN_TYPES getType() {
		return type;
	}

	public void setType(DOMAIN_TYPES type) {
		this.type = type;
	}

}
