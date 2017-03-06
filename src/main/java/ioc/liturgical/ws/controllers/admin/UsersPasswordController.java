package ioc.liturgical.ws.controllers.admin;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;

import ioc.liturgical.ws.app.ServiceProvider;
import ioc.liturgical.ws.constants.Constants;
import ioc.liturgical.ws.constants.HTTP_RESPONSE_CODES;
import ioc.liturgical.ws.constants.ADMIN_ENDPOINTS;
import ioc.liturgical.ws.constants.NEW_FORM_CLASSES;
import ioc.liturgical.ws.manager.auth.AuthDecoder;
import ioc.liturgical.ws.manager.database.internal.InternalDbManager;
import ioc.liturgical.ws.models.RequestStatus;

public class UsersPasswordController {
	private static final Logger logger = LoggerFactory.getLogger(UsersPasswordController.class);
	
	public UsersPasswordController(InternalDbManager storeManager) {

		/**
		 * GET 
		 */
//		String path = ADMIN_ENDPOINTS.USERS_PASSWORD.toLibraryTopicKeyPath();
		String path = "/admin/api/v1/users/password";
		ControllerUtils.reportPath(logger, "GET", path);
		get(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
			String requestor = new AuthDecoder(request.headers("Authorization")).getUsername();
			String query = ServiceProvider.createStringFromSplat(request.splat(), Constants.ID_DELIMITER);
			JsonObject json = storeManager.getUserPasswordChangeForms(requestor, query);
//			JsonObject json = storeManager.getUserPasswordChangeForms(query);
			if (json.get("valueCount").getAsInt() > 0) {
				response.status(HTTP_RESPONSE_CODES.OK.code);
			} else {
				response.status(HTTP_RESPONSE_CODES.NOT_FOUND.code);
			}
			return json.toString();
		});

		/**
		 * PUT controllers
		 */
		path = ADMIN_ENDPOINTS.USERS_PASSWORD.toLibraryTopicPath();
		ControllerUtils.reportPath(logger, "PUT", path);
		put(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
			String query = ServiceProvider.createStringFromSplat(request.splat(), Constants.ID_DELIMITER);
			RequestStatus requestStatus = storeManager.updateUserPassword(query, request.body());
			response.status(requestStatus.getCode());
			return requestStatus.toJsonString();
		});

	}

}
