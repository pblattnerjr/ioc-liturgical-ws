package ioc.liturgical.ws.controllers.admin;

import static spark.Spark.post;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ioc.liturgical.ws.constants.Constants;
import ioc.liturgical.ws.constants.NEW_FORM_CLASSES;
import ioc.liturgical.ws.managers.auth.AuthDecoder;
import ioc.liturgical.ws.managers.databases.internal.InternalDbManager;
import ioc.liturgical.ws.models.RequestStatus;

public class AuthorizationsController {
	private static final Logger logger = LoggerFactory.getLogger(AuthorizationsController.class);
	
	public AuthorizationsController(InternalDbManager storeManager) {

		/**
		 * POST controllers
		 */
		String path = NEW_FORM_CLASSES.NEW_AUTHORIZATION.toPostPath();
		ControllerUtils.reportPath(logger, "POST", path);
		post(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
			String requestor = new AuthDecoder(request.headers("Authorization")).getUsername();
			RequestStatus requestStatus = storeManager.addAuthorization(requestor, request.body());
			response.status(requestStatus.getCode());
			return requestStatus.toJsonString();
		});

		
	}

}
