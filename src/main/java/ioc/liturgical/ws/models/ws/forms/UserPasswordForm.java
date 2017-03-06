package ioc.liturgical.ws.models.ws.forms;

import com.google.gson.annotations.Expose;

import ioc.liturgical.ws.forms.manager.FormFieldLengths;
import ioc.liturgical.ws.forms.manager.FormRegExConstants;
import ioc.liturgical.ws.models.SchemaException;
import net.ages.alwb.utils.core.datastores.json.models.AbstractModel;

import com.github.reinert.jjschema.Attributes;

/**
 * This class provides a POJO for use in web forms to create or update a user password
 * @author mac002
 *
 */
@Attributes(title = "User Password Entry", description = "Schema for Setting a Password")
public class UserPasswordForm extends AbstractModel {
	@Attributes(required = true, description = "id used to login to system", minLength=FormFieldLengths.USERNAME_MIN)
	@Expose
	public String username = "";
	@Attributes(required = true, description = FormRegExConstants.PASSWORD_DESCRIPTION, pattern=FormRegExConstants.PASSWORD_PATTERN)
	@Expose
	public String password = "";
	@Attributes(required = true, description = "reenter password", pattern=FormRegExConstants.PASSWORD_PATTERN)
	@Expose
	public String passwordReenter = "";
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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
			return super.validate(json);
		} else {
			return new SchemaException("#/password","matches","Expected password to match passwordReenter but does not.").toJsonString();
		}
	}
	
	private boolean passwordsMatch() {
		return (this.password.length() == this.passwordReenter.length() && this.password.startsWith(this.passwordReenter));
	}
}
