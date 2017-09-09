package ioc.liturgical.ws.controllers.admin;

import static spark.Spark.get;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ioc.liturgical.ws.constants.Constants;
import ioc.liturgical.ws.managers.auth.AuthDecoder;
import ioc.liturgical.ws.managers.databases.internal.InternalDbManager;
import ioc.liturgical.ws.models.ws.response.AbstractResponse;
import ioc.liturgical.ws.models.ws.response.Login;

public class LoginController {
	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

	/**
	 * returns a login form
	 * TODO: look at other places to see if it is useful to use AbstractReponse
	 * @param storeManager
	 */
	public LoginController(InternalDbManager storeManager) {
		
		String path = Constants.INTERNAL_DATASTORE_API_PATH  + Constants.LOGIN_PATH + "/form";
		ControllerUtils.reportPath(logger, "GET", path);
		get(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
			return new AbstractResponse<Login>(new Login(), true).toJsonString();
		});

		path = Constants.INTERNAL_DATASTORE_API_PATH  + Constants.LOGIN_PATH + "/user";
		ControllerUtils.reportPath(logger, "GET", path);
		get(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
			AuthDecoder authDecoder = new AuthDecoder(request.headers("Authorization"));
			return storeManager.getUserContact(authDecoder.getUsername()).toJsonString();
		});

	}

}
