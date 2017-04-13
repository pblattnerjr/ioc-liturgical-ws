package ioc.liturgical.ws.models.ws.forms;

import com.google.gson.annotations.Expose;

import ioc.liturgical.ws.forms.manager.FormRegExConstants;
import ioc.liturgical.ws.models.SchemaException;
import ioc.liturgical.ws.annotations.UiWidget;
import ioc.liturgical.ws.constants.Constants;
import ioc.liturgical.ws.forms.manager.FormFieldLengths;
import net.ages.alwb.utils.core.datastores.json.models.AbstractModel;

import com.github.reinert.jjschema.Attributes;

/**
 * This class provides a POJO for use in web forms to create or update a user
 * @author mac002
 *
 */
@Attributes(title = "User", description = "Schema for a User")
public class UserCreateForm extends AbstractModel {
	@Attributes(required = true, description = "id used to login to system", minLength=FormFieldLengths.USERNAME_MIN)
	@Expose public String username = "";

	@Attributes(required = true, description = "first name of the user", minLength=FormFieldLengths.NAME_FIRST_MIN)
	@Expose public String firstname = "";
	
	@Attributes(required = true, description = "last name (i.e., family name, or surname)  of the user", minLength=FormFieldLengths.NAME_LAST_MIN)
	@Expose public String lastname = "";
	
	@Attributes(required = true, description = "email to contact the user", pattern=FormRegExConstants.EMAIL)
	@Expose public String email = "";
	
	@Attributes(required = true, description = "reenter email to contact the user", pattern=FormRegExConstants.EMAIL)
	@Expose public String emailReenter = "";
	
	@UiWidget(Constants.UI_WIDGET_PASSWORD)
	@Attributes(required = true, description = FormRegExConstants.PASSWORD_DESCRIPTION, pattern=FormRegExConstants.PASSWORD_PATTERN)
	@Expose public String password = "";
	
	@UiWidget(Constants.UI_WIDGET_PASSWORD)
	@Attributes(required = true, description = "reenter password", pattern=FormRegExConstants.PASSWORD_PATTERN)
	@Expose public String passwordReenter = "";
		
	public UserCreateForm() {
		super();
		this.serialVersionUID = 1.1;
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
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmailReenter() {
		return emailReenter;
	}

	public void setEmailReenter(String emailReenter) {
		this.emailReenter = emailReenter;
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

	public boolean valid(String json) {
		return this.validate(json).length() == 0;
	}
	
	public String validate(String json) {
		if (this.passwordsMatch()) {
			if (this.emailsMatch()) {
				return super.validate(json);
			} else {
				return new SchemaException("#/email","matches","Expected email to match emailReenter but does not.").toJsonString();
			}
		} else {
			return new SchemaException("#/password","matches","Expected password to match passwordReenter but does not.").toJsonString();
		}
	}
	
	private boolean passwordsMatch() {
		return (this.password.length() == this.passwordReenter.length() && this.password.startsWith(this.passwordReenter));
	}
	
	private boolean emailsMatch() {
		return (this.email.length() == this.emailReenter.length() && this.email.startsWith(this.emailReenter));
	}
}
