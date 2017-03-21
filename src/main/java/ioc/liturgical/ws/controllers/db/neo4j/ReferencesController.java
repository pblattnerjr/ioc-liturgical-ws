package ioc.liturgical.ws.controllers.db.neo4j;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;

import ioc.liturgical.ws.app.ServiceProvider;
import ioc.liturgical.ws.constants.Constants;
import ioc.liturgical.ws.constants.HTTP_RESPONSE_CODES;
import ioc.liturgical.ws.constants.ENDPOINTS_DB_API;
import ioc.liturgical.ws.constants.NEW_FORM_CLASSES_DB_API;
import ioc.liturgical.ws.controllers.admin.ControllerUtils;
import ioc.liturgical.ws.managers.auth.AuthDecoder;
import ioc.liturgical.ws.managers.databases.external.neo4j.ExternalDbManager;
import ioc.liturgical.ws.models.RequestStatus;

public class ReferencesController {
	private static final Logger logger = LoggerFactory.getLogger(ReferencesController.class);
	
	public ReferencesController(ExternalDbManager storeManager) {

		/**
		 * GET controllers
		 */
		String path = ENDPOINTS_DB_API.REFERENCES.toLibraryTopicKeyPath();
		ControllerUtils.reportPath(logger, "GET", path);
		get(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
			String query = ServiceProvider.createStringFromSplat(request.splat(), Constants.ID_DELIMITER);
			JsonObject json = storeManager.getForQuery(query);
			if (json.get("valueCount").getAsInt() > 0) {
				response.status(HTTP_RESPONSE_CODES.OK.code);
			} else {
				response.status(HTTP_RESPONSE_CODES.NOT_FOUND.code);
			}
			return json.toString();
		});

		path = ENDPOINTS_DB_API.REFERENCES.toLibraryTopicPath();
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

		path = ENDPOINTS_DB_API.REFERENCES.toLibraryPath();
		String db = ENDPOINTS_DB_API.REFERENCES.toDbLibraryTopic(); // yes, this is correct.  references is a topic.
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
		path = NEW_FORM_CLASSES_DB_API.NEW_REFERENCE.toPostPath();
		ControllerUtils.reportPath(logger, "POST", path);
		post(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
			String requestor = new AuthDecoder(request.headers("Authorization")).getUsername();
			String fromLibrary = "";
			String fromTopic = "";
			String fromKey = "";
			String toLibrary = "";
			String toTopic = "";
			String toKey = "";
			RequestStatus requestStatus = storeManager.addReference(
					requestor
					, fromLibrary
					, fromTopic
				    , fromKey
					, toLibrary
					, toTopic
					, toKey
					, request.body()
					);
			response.status(requestStatus.getCode());
			return requestStatus.toJsonString();
		});

		/**
		 * PUT controllers
		 */
		path = ENDPOINTS_DB_API.REFERENCES.toLibraryTopicPath();
		ControllerUtils.reportPath(logger, "PUT", path);
		put(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
			String requestor = new AuthDecoder(request.headers("Authorization")).getUsername();
			String id = ServiceProvider.createStringFromSplat(request.splat(), Constants.ID_DELIMITER);
			RequestStatus requestStatus = storeManager.updateReference(requestor, id, request.body());
			response.status(requestStatus.getCode());
			return requestStatus.toJsonString();
		});

	}

}
