package ioc.liturgical.ws.models.ws.forms;

import com.google.gson.annotations.Expose;

import ioc.liturgical.ws.forms.manager.FormRegExConstants;
import ioc.liturgical.ws.models.SchemaException;
import ioc.liturgical.ws.annotations.UiWidget;
import ioc.liturgical.ws.constants.Constants;
import ioc.liturgical.ws.forms.manager.FormFieldLengths;
import net.ages.alwb.utils.core.datastores.json.models.AbstractModel;

import java.util.List;

import com.github.reinert.jjschema.Attributes;

/**
 * This class provides a POJO for use in web forms to create an authorization for a user
 * @author mac002
 *
 */
@Attributes(title = "Authorization", description = "Use this form to grant a user an authorization for a specific library.  You will only see domains (libraries) for which you have administrative authority.  If you have authority for all domains, you will see * all_domains listed as a domain.  Granting a role to a user for this will give them that role for all domains.  If you are a web service admin, you will see * ws_service as an option and you can assign roles to users for the entire web service.")
public class AuthorizationCreateForm extends AbstractModel {
	
	@Attributes(required = true, description = "The role you are granting this person.", enums={"admin", "author", "reader"})
	@Expose String role;

	@Attributes(required = true, description = "The library for which you are granting this role.")
	@Expose String library;

	@Attributes(required = true, description = "The user to whom you are granting this role.")
	@Expose String username;
	
	public AuthorizationCreateForm() {
		super();
		this.serialVersionUID = 1.1;
	}

	public String getLibrary() {
		return library;
	}

	public void setLibrary(String library) {
		this.library = library;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public static void main (String args[]) {
		AuthorizationCreateForm form = new AuthorizationCreateForm();
		System.out.println(form.toJsonUiSchemaObject());
		System.out.println(form.toJsonSchemaObject());
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

}
