package ioc.liturgical.ws.controllers.admin;

import static spark.Spark.post;
import static spark.Spark.put;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import ioc.liturgical.ws.app.ServiceProvider;
import ioc.liturgical.ws.constants.Constants;
import ioc.liturgical.ws.constants.ENDPOINTS_ADMIN_API;
import ioc.liturgical.ws.constants.NEW_FORM_CLASSES_ADMIN_API;
import ioc.liturgical.ws.managers.auth.AuthDecoder;
import ioc.liturgical.ws.managers.databases.internal.InternalDbManager;
import ioc.liturgical.ws.models.RequestStatus;

public class UsersContactController {
	private static final Logger logger = LoggerFactory.getLogger(UsersContactController.class);
	
	public UsersContactController(InternalDbManager storeManager) {

		/**
		 * GET is handled generically by ServiceProvider because users is a library.
		 */
		
		/**
		 * POST controllers
		 */
		// combines create (POST) UserContact and UserHash
		String path = NEW_FORM_CLASSES_ADMIN_API.NEW_USER.toPostPath();
		ControllerUtils.reportPath(logger, "POST", path);
		post(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
			String requestor = new AuthDecoder(request.headers("Authorization")).getUsername();
			RequestStatus requestStatus = storeManager.addUser(requestor, request.body());
			response.status(requestStatus.getCode());
			return requestStatus.toJsonString();
		});

		/**
		 * PUT controllers
		 */
		path = ENDPOINTS_ADMIN_API.USERS_CONTACT.toLibraryTopicPath();
		ControllerUtils.reportPath(logger, "PUT", path);
		put(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
			String requestor = new AuthDecoder(request.headers("Authorization")).getUsername();
			String query = ServiceProvider.createStringFromSplat(request.splat(), Constants.ID_DELIMITER);
			RequestStatus requestStatus = storeManager.updateUserContact(
					requestor
					, query
					, request.body()
					);
			response.status(requestStatus.getCode());
			return requestStatus.toJsonString();
		});

	}

}
