package ioc.liturgical.ws.controllers.admin;

import static spark.Spark.get;

import ioc.liturgical.ws.constants.Constants;
import ioc.liturgical.ws.manager.database.internal.InternalDbManager;
import ioc.liturgical.ws.models.ws.response.AbstractResponse;
import ioc.liturgical.ws.models.ws.response.Login;

public class LoginController {
	
	/**
	 * returns a login form
	 * @param storeManager
	 */
	public LoginController(InternalDbManager storeManager) {
		get(Constants.INTERNAL_DATASTORE_API_PATH  + "/login", (request, response) -> {
			response.type(Constants.UTF_JSON);
			return new AbstractResponse<Login>(new Login(), true).toJsonString();
		});
	}

}
