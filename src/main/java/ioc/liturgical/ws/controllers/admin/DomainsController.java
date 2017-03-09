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
import ioc.liturgical.ws.managers.auth.AuthDecoder;
import ioc.liturgical.ws.managers.databases.internal.InternalDbManager;
import ioc.liturgical.ws.models.RequestStatus;

public class DomainsController {
	private static final Logger logger = LoggerFactory.getLogger(DomainsController.class);
	
	public DomainsController(InternalDbManager storeManager) {

		/**
		 * GET controllers
		 */
		String path = ADMIN_ENDPOINTS.DOMAINS.toLibraryTopicKeyPath();
		ControllerUtils.reportPath(logger, "GET", path);
		get(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
			String query = ServiceProvider.createStringFromSplat(request.splat(), Constants.ID_DELIMITER);
			JsonObject json = storeManager.getForId(query);
			if (json.get("valueCount").getAsInt() > 0) {
				response.status(HTTP_RESPONSE_CODES.OK.code);
			} else {
				response.status(HTTP_RESPONSE_CODES.NOT_FOUND.code);
			}
			return json.toString();
		});

		path = ADMIN_ENDPOINTS.DOMAINS.toLibraryTopicPath();
		ControllerUtils.reportPath(logger, "GET", path);
		get(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
			String query = ServiceProvider.createStringFromSplat(request.splat(), Constants.ID_DELIMITER);
			JsonObject json = storeManager.getForIdStartsWith(query);
			if (json.get("valueCount").getAsInt() > 0) {
				response.status(HTTP_RESPONSE_CODES.OK.code);
			} else {
				response.status(HTTP_RESPONSE_CODES.NOT_FOUND.code);
			}
			return json.toString();
		});

		path = ADMIN_ENDPOINTS.DOMAINS.toLibraryPath();
		String db = ADMIN_ENDPOINTS.DOMAINS.toDbLibraryTopic(); // yes, this is correct.  domains is a topic.
		ControllerUtils.reportPath(logger, "GET", path + " maps to database id " + db);
		get(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
			JsonObject json = storeManager.getForIdStartsWith(db);
			if (json.get("valueCount").getAsInt() > 0) {
				response.status(HTTP_RESPONSE_CODES.OK.code);
			} else {
				response.status(HTTP_RESPONSE_CODES.NOT_FOUND.code);
			}
			return json.toString();
		});
		
		/**
		 * POST controllers
		 */
		path = NEW_FORM_CLASSES.NEW_DOMAIN.toPostPath();
		ControllerUtils.reportPath(logger, "POST", path);
		post(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
			String requestor = new AuthDecoder(request.headers("Authorization")).getUsername();
			RequestStatus requestStatus = storeManager.addDomain(requestor,request.body());
			response.status(requestStatus.getCode());
			return requestStatus.toJsonString();
		});

		/**
		 * PUT controllers
		 */
		path = ADMIN_ENDPOINTS.DOMAINS.toLibraryTopicPath();
		ControllerUtils.reportPath(logger, "PUT", path);
		put(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
			String requestor = new AuthDecoder(request.headers("Authorization")).getUsername();
			String key = ServiceProvider.createStringFromSplat(request.splat(), Constants.ID_DELIMITER);
			RequestStatus requestStatus = storeManager.updateDomain(requestor, key, request.body());
			response.status(requestStatus.getCode());
			return requestStatus.toJsonString();
		});

	}

}
