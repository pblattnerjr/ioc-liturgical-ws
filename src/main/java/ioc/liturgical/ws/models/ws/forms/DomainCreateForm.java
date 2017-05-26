package ioc.liturgical.ws.models.ws.forms;

import com.google.gson.annotations.Expose;

import ioc.liturgical.ws.forms.manager.FormRegExConstants;
import ioc.liturgical.ws.annotations.UiWidget;
import ioc.liturgical.ws.constants.Constants;
import ioc.liturgical.ws.constants.DOMAIN_TYPES;
import ioc.liturgical.ws.constants.STATUS;
import net.ages.alwb.utils.core.datastores.json.models.AbstractModel;

import java.util.ArrayList;
import java.util.List;

import com.github.reinert.jjschema.Attributes;

/**
 * This class provides a POJO for use in web forms to create or update a domain
 * @author mac002
 *
 */
@Attributes(title = "Domain", description = "A domain identifies the text of a doc as being in a specific language as spoken in a specific country for a specific realm.  A realm can be a version of a translation, e.g. the King James Version (KJV), or a particular translator, e.g. by Fr. Seraphim Dedes (dedes), or for a particular metropolis, e.g. the Orthodox Church of Kenya (oak)")
public class DomainCreateForm extends AbstractModel {
	
	@Attributes(required = true, description = "ISO code for the language, e.g. en.  Must be 2 to 3 characters, with no spaces.", minLength=2, maxLength=3, pattern=FormRegExConstants.NOSPACES)
	@Expose public String languageCode = "";

	@Attributes(required = true, description = "ISO code for the country, e.g. uk.  Must 2 to 3 characters, with no spaces.", minLength=2, maxLength=3, pattern=FormRegExConstants.NOSPACES)
	@Expose public String countryCode = "";

	@Attributes(required = true, description = "Realm, e.g. kjv.  Must be 3 to 20 characters, with no spaces.", minLength=3, maxLength=20, pattern=FormRegExConstants.NOSPACES)
	@Expose public String realm = "";

	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(required = true, description = "Description of the library.")
	@Expose public String description = "";

	@UiWidget(Constants.UI_WIDGET_RADIO)
	@Attributes(required = true, description = "The type of domain this is")
	@Expose public DOMAIN_TYPES type = DOMAIN_TYPES.USER;

	@UiWidget(Constants.UI_WIDGET_RADIO)
	@Attributes(required = true, description = "Is state enabled for this domain? If it is, then admins and authors may set the status of each record for this domain.")
	@Expose public boolean stateEnabled = false;

	@Attributes(required = true, description = "What is the default status of a record after an edit?")
	@Expose public STATUS defaultStatusAfterEdit = STATUS.FINALIZED;

	@Attributes(required = true, description = "What is the default status of a record after it is finalized?")
	@Expose public STATUS defaultStatusAfterFinalization = STATUS.FINALIZED;

	@UiWidget(Constants.UI_WIDGET_RADIO)
	@Attributes(required = true, description = "Is workflow enabled for this domain? If it is, a user can be assigned to do the work for the next step in the workflow.")
	@Expose public boolean workflowEnabled = false;

	@UiWidget(Constants.UI_WIDGET_CHECKBOXES)
	@Attributes(required = true, description = "Labels to Use as Doc Types for this domain.")
	@Expose public List<String> labels = new ArrayList<String>();

	public DomainCreateForm() {
		super();
		this.serialVersionUID = 1.1;
	}

	public String getLanguageCode() {
		return languageCode;
	}

	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getRealm() {
		return realm;
	}

	public void setRealm(String realm) {
		this.realm = realm;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<String> getLabels() {
		return labels;
	}

	public void setLabels(List<String> labels) {
		this.labels = labels;
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
