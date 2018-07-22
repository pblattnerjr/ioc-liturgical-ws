package ioc.liturgical.ws.controllers.admin;

import static spark.Spark.post;
import static spark.Spark.put;

import org.ocmc.ioc.liturgical.schemas.constants.ENDPOINTS_ADMIN_API;
import org.ocmc.ioc.liturgical.schemas.constants.NEW_FORM_CLASSES_ADMIN_API;
import org.ocmc.ioc.liturgical.schemas.models.ws.response.RequestStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import ioc.liturgical.ws.app.ServiceProvider;
import ioc.liturgical.ws.constants.Constants;
import ioc.liturgical.ws.managers.auth.AuthDecoder;
import ioc.liturgical.ws.managers.databases.internal.InternalDbManager;

public class UsersPreferencesController {
	private static final Logger logger = LoggerFactory.getLogger(UsersPreferencesController.class);
	
	public UsersPreferencesController(InternalDbManager storeManager) {

		/**
		 * GET is handled generically by ServiceProvider because users is a library.
		 */
		
		/**
		 * POST controllers
		 */
		// create (POST) UserPreferences
		String path = NEW_FORM_CLASSES_ADMIN_API.NEW_USER_PREFERENCES.toPostPath();
		ControllerUtils.reportPath(logger, "POST", path);
		post(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
			String requestor = new AuthDecoder(request.headers("Authorization")).getUsername();
			RequestStatus requestStatus = storeManager.addUserPreferences(requestor, request.body());
			response.status(requestStatus.getCode());
			return requestStatus.toJsonString();
		});

		/**
		 * PUT controllers
		 */
		path = ENDPOINTS_ADMIN_API.USERS_PREFERENCES.toLibraryTopicPath();
		ControllerUtils.reportPath(logger, "PUT", path);
		put(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
			String requestor = new AuthDecoder(request.headers("Authorization")).getUsername();
			String query = ServiceProvider.createStringFromSplat(request.splat(), Constants.ID_DELIMITER);
			RequestStatus requestStatus = storeManager.updateUserPreferences(
					requestor
					, query
					, request.body()
					);
			response.status(requestStatus.getCode());
			return requestStatus.toJsonString();
		});

	}

}
