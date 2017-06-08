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
import ioc.liturgical.ws.managers.databases.internal.InternalDbManager;
import ioc.liturgical.ws.models.RequestStatus;
import ioc.liturgical.ws.models.ResultJsonObjectArray;

public class LabelsController {
	private static final Logger logger = LoggerFactory.getLogger(LabelsController.class);
	
	public LabelsController(InternalDbManager storeManager) {

		/**
		 * GET controllers
		 */
		String path = ENDPOINTS_ADMIN_API.LABELS.toLibraryTopicKeyPath();
		ControllerUtils.reportPath(logger, "GET", path);
		get(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
			String query = ServiceProvider.createStringFromSplat(request.splat(), Constants.ID_DELIMITER);
			ResultJsonObjectArray json = storeManager.getForId(query);
			if (json.valueCount > 0) {
				response.status(HTTP_RESPONSE_CODES.OK.code);
			} else {
				response.status(HTTP_RESPONSE_CODES.NOT_FOUND.code);
			}
			return json.toString();
		});

		path = ENDPOINTS_ADMIN_API.LABELS.toLibraryTopicPath();
		ControllerUtils.reportPath(logger, "GET", path);
		get(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
			String query = ServiceProvider.createStringFromSplat(request.splat(), Constants.ID_DELIMITER);
			ResultJsonObjectArray json = storeManager.getForIdStartsWith(query);
			if (json.getValueCount() > 0) {
				response.status(HTTP_RESPONSE_CODES.OK.code);
			} else {
				response.status(HTTP_RESPONSE_CODES.NOT_FOUND.code);
			}
			return json.toString();
		});

		path = ENDPOINTS_ADMIN_API.LABELS.toLibraryPath();
		String db = ENDPOINTS_ADMIN_API.LABELS.toDbLibraryTopic(); // yes, this is correct.  labels is a topic.
		ControllerUtils.reportPath(logger, "GET", path + " maps to database id " + db);
		get(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
			ResultJsonObjectArray json = storeManager.getForIdStartsWith(db);
			if (json.getValueCount() > 0) {
				response.status(HTTP_RESPONSE_CODES.OK.code);
			} else {
				response.status(HTTP_RESPONSE_CODES.NOT_FOUND.code);
			}
			return json.toString();
		});
		
		/**
		 * POST controllers
		 */
		path = NEW_FORM_CLASSES_ADMIN_API.NEW_LABEL.toPostPath();
		ControllerUtils.reportPath(logger, "POST", path);
		post(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
			String requestor = new AuthDecoder(request.headers("Authorization")).getUsername();
			RequestStatus requestStatus = storeManager.addLabel(requestor,request.body());
			response.status(requestStatus.getCode());
			return requestStatus.toJsonString();
		});

		/**
		 * PUT controllers
		 */
		path = ENDPOINTS_ADMIN_API.LABELS.toLibraryTopicPath();
		ControllerUtils.reportPath(logger, "PUT", path);
		put(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
			String requestor = new AuthDecoder(request.headers("Authorization")).getUsername();
			String key = ServiceProvider.createStringFromSplat(request.splat(), Constants.ID_DELIMITER);
			RequestStatus requestStatus = storeManager.updateLabel(requestor, key, request.body());
			response.status(requestStatus.getCode());
			return requestStatus.toJsonString();
		});

	}

}
