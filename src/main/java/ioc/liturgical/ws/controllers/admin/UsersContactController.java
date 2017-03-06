package ioc.liturgical.ws.controllers.admin;

import static spark.Spark.post;
import static spark.Spark.put;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import ioc.liturgical.ws.app.ServiceProvider;
import ioc.liturgical.ws.constants.Constants;
import ioc.liturgical.ws.constants.ADMIN_ENDPOINTS;
import ioc.liturgical.ws.constants.NEW_FORM_CLASSES;
import ioc.liturgical.ws.manager.database.internal.InternalDbManager;
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
		String path = NEW_FORM_CLASSES.NEW_USER.toPostPath();
		ControllerUtils.reportPath(logger, "POST", path);
		post(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
			RequestStatus requestStatus = storeManager.addUser(request.body());
			response.status(requestStatus.getCode());
			return requestStatus.toJsonString();
		});

		/**
		 * PUT controllers
		 */
		path = ADMIN_ENDPOINTS.USERS_CONTACT.toLibraryTopicPath();
		ControllerUtils.reportPath(logger, "PUT", path);
		put(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
			String query = ServiceProvider.createStringFromSplat(request.splat(), Constants.ID_DELIMITER);
			RequestStatus requestStatus = storeManager.updateUserContact(query, request.body());
			response.status(requestStatus.getCode());
			return requestStatus.toJsonString();
		});

	}

}
