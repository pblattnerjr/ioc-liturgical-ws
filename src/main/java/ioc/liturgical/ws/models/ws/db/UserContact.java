package ioc.liturgical.ws.models.ws.db;

import com.github.reinert.jjschema.Attributes;
import com.google.gson.annotations.Expose;

import ioc.liturgical.ws.forms.manager.FormRegExConstants;
import net.ages.alwb.utils.core.datastores.json.models.AbstractModel;

public class UserContact extends AbstractModel {
	@Attributes(required = true, description = "First name")
	@Expose public String firstname = "";
	@Attributes(required = true, description = "Last name, i.e. family name")
	@Expose public String lastname = "";
	@Attributes(required = true, description = "Email to contact the user", pattern=FormRegExConstants.EMAIL)
	@Expose public String email = "";
	
	public UserContact() {
		super();
		this.serialVersionUID = 1.1;
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
		
		
}
