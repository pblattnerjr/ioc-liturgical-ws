package ioc.liturgical.ws.models.ws.db;

import java.util.ArrayList;
import java.util.List;

import com.github.reinert.jjschema.Attributes;
import com.google.gson.annotations.Expose;

import ioc.liturgical.ws.annotations.UiWidget;
import ioc.liturgical.ws.constants.Constants;
import ioc.liturgical.ws.constants.DOMAIN_TYPES;
import ioc.liturgical.ws.constants.STATUS;
import ioc.liturgical.ws.forms.manager.FormRegExConstants;
import ioc.liturgical.ws.models.ws.supers.WsDbAbstractModel;

@Attributes(title = "Domain", description = "A domain identifies the text of a doc as being in a specific language as spoken in a specific country for a specific realm.  A realm can be a version of a translation, e.g. the King James Version (KJV), or a particular translator, e.g. by Fr. Seraphim Dedes (dedes), or for a particular metropolis, e.g. the Orthodox Church of Kenya (oak)")
public class Domain extends WsDbAbstractModel {
	
	@Attributes(required = true, readonly=true, description = "The combination of language and country code and realm, separated by the underscore character, e.g. en_us_dedes", minLength=7)
	@Expose public String domain = "";

	@Attributes(required = true, readonly=true, description = "ISO code for the language, e.g. en.  Must be 2 to 3 characters, with no spaces.", minLength=2, maxLength=3, pattern=FormRegExConstants.NOSPACES)
	@Expose public String languageCode = "";

	@Attributes(required = true, readonly=true, description = "ISO code for the country, e.g. uk.  Must 2 to 3 characters, with no spaces.", minLength=2, maxLength=3, pattern=FormRegExConstants.NOSPACES)
	@Expose public String countryCode = "";

	@Attributes(required = true, readonly=true, description = "Realm, e.g. kjv.  Must be 3 to 20 characters, with no spaces.", minLength=3, maxLength=20, pattern=FormRegExConstants.NOSPACES)
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
	@Attributes(required = true, description = "Doc Types for this domain.")
	@Expose public List<String> labels = new ArrayList<String>();


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

	public List<String> getLabels() {
		return labels;
	}

	public void setLabels(List<String> labels) {
		this.labels = labels;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getLanguageCode() {
		return languageCode;
	}

	private void setDomain() {
		this.setDomain(
				languageCode 
				+ Constants.DOMAIN_DELIMITER 
				+ this.countryCode 
				+ Constants.DOMAIN_DELIMITER 
				+ this.realm
				);
	}

	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
		setDomain();
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
		setDomain();
	}

	public String getRealm() {
		return realm;
	}

	public void setRealm(String realm) {
		this.realm = realm;
		setDomain();
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
