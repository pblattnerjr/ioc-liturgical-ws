package ioc.liturgical.ws.controllers.admin;

import static spark.Spark.get;

import org.ocmc.ioc.liturgical.schemas.models.ws.forms.UserCreateForm;
import org.ocmc.ioc.liturgical.schemas.models.ws.forms.UserPasswordForm;
import org.ocmc.ioc.liturgical.schemas.models.ws.forms.UserUpdateForm;

import ioc.liturgical.ws.constants.Constants;

public class SchemasController {
	
	public SchemasController() {
		get(Constants.INTERNAL_DATASTORE_API_PATH + "/_schemas/post/_users", (request, response) -> {
			response.type(Constants.UTF_JSON);
			return new UserCreateForm().toJsonSchema();
		});

		get(Constants.INTERNAL_DATASTORE_API_PATH + "/_schemas/put/_users", (request, response) -> {
			response.type(Constants.UTF_JSON);
			return new UserUpdateForm().toJsonSchema();
		});

		get(Constants.INTERNAL_DATASTORE_API_PATH + "/_schemas/put/_users/password", (request, response) -> {
			response.type(Constants.UTF_JSON);
			return new UserPasswordForm().toJsonSchema();
		});
		
	}

}
