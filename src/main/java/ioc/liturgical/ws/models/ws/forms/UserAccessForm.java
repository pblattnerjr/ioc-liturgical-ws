package ioc.liturgical.ws.models.ws.forms;

import com.google.gson.annotations.Expose;

import ioc.liturgical.ws.forms.manager.FormRegExConstants;
import net.ages.alwb.utils.core.datastores.json.models.AbstractModel;
import com.github.reinert.jjschema.Attributes;

@Attributes(title = "User Access", description = "Schema for a User Access doc")
public class UserAccessForm extends AbstractModel {
	@Attributes(required = true, description = "name of this form", readonly=true)
	@Expose String formName = "UserAccessForm";
	@Attributes(required = true, description = "version number of this form", readonly=true)
	@Expose String version = "v1";
	@Attributes(required = true, description = "id used to login to system", minLength=5)
	@Expose String username = "";
	@Attributes(required = true, description = "first name of the user")
	@Expose String firstname = "";
	@Attributes(required = true, description = "surname  of the user")
	@Expose String surname = "";
	@Attributes(required = true, description = "email to contact the user", pattern=FormRegExConstants.EMAIL)
	@Expose String email = "";
	@Attributes(required = true, description = FormRegExConstants.PASSWORD_DESCRIPTION, pattern=FormRegExConstants.PASSWORD_PATTERN)
	@Expose String password = "";
	@Attributes(required = true, description = "reenter user's password",pattern=FormRegExConstants.PASSWORD_PATTERN)
	@Expose String passwordReenter = "";
	@Attributes(required = true, description = "does the user need to change his/her password after first logon?")
	@Expose boolean passwordChangeRequired = true;
	@Attributes(required = true, description = "is the user's account active?")
	@Expose boolean accountActive = true;
	public String getFormName() {
		return formName;
	}
	public void setFormName(String formName) {
		this.formName = formName;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPasswordReenter() {
		return passwordReenter;
	}
	public void setPasswordReenter(String passwordReenter) {
		this.passwordReenter = passwordReenter;
	}
	public boolean isPasswordChangeRequired() {
		return passwordChangeRequired;
	}
	public void setPasswordChangeRequired(boolean passwordChangeRequired) {
		this.passwordChangeRequired = passwordChangeRequired;
	}
	public boolean isAccountActive() {
		return accountActive;
	}
	public void setAccountActive(boolean accountActive) {
		this.accountActive = accountActive;
	}
		
	
}
