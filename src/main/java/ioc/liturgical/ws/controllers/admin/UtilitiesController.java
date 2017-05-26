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
import ioc.liturgical.ws.constants.ENDPOINTS_ADMIN_API;
import ioc.liturgical.ws.constants.NEW_FORM_CLASSES_ADMIN_API;
import ioc.liturgical.ws.managers.auth.AuthDecoder;
import ioc.liturgical.ws.managers.databases.external.neo4j.ExternalDbManager;
import ioc.liturgical.ws.managers.databases.internal.InternalDbManager;
import ioc.liturgical.ws.models.RequestStatus;

/**
 * Handles post for calling a utility
 * @author mac002
 *
 */
public class UtilitiesController {
	private static final Logger logger = LoggerFactory.getLogger(UtilitiesController.class);
	
	public UtilitiesController(ExternalDbManager externalManager) {

		/**
		 * PUT controllers
		 */
		String path = ENDPOINTS_ADMIN_API.UTILITIES.toLibraryTopicPath();
		ControllerUtils.reportPath(logger, "PUT", path);
		put(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
			String requestor = new AuthDecoder(request.headers("Authorization")).getUsername();
			String key = ServiceProvider.createStringFromSplat(request.splat(), Constants.ID_DELIMITER);
			RequestStatus requestStatus = externalManager.runUtility(requestor, key, request.body());
			response.status(requestStatus.getCode());
			return requestStatus.toJsonString();
		});

	}

}
