package ioc.liturgical.ws.controllers.admin;

import static spark.Spark.get;

import com.google.gson.JsonObject;

import ioc.liturgical.ws.app.ServiceProvider;
import ioc.liturgical.ws.constants.ENDPOINTS_ADMIN_API;
import ioc.liturgical.ws.constants.Constants;
import ioc.liturgical.ws.constants.HTTP_RESPONSE_CODES;
import ioc.liturgical.ws.constants.SYSTEM_LIBS;
import ioc.liturgical.ws.managers.databases.internal.InternalDbManager;

public class AuthorsController {
	
	public AuthorsController(InternalDbManager storeManager) {
		get(ENDPOINTS_ADMIN_API.AUTHORS.toLibraryTopicKeyPath(), (request, response) -> {
			response.type(Constants.UTF_JSON);
			String query = ServiceProvider.createStringFromSplat(request.splat(), Constants.ID_DELIMITER);
			JsonObject json = storeManager.getForId(SYSTEM_LIBS.AUTHORS + Constants.ID_DELIMITER + query);
			if (json.get("valueCount").getAsInt() > 0) {
				response.status(HTTP_RESPONSE_CODES.OK.code);
			} else {
				response.status(HTTP_RESPONSE_CODES.NOT_FOUND.code);
			}
			return json.toString();
		});

		get(ENDPOINTS_ADMIN_API.AUTHORS.toLibraryTopicPath(), (request, response) -> {
			response.type(Constants.UTF_JSON);
			String query = ServiceProvider.createStringFromSplat(request.splat(), Constants.ID_DELIMITER);
			JsonObject json = storeManager.getWhereStartsWith(SYSTEM_LIBS.AUTHORS + Constants.ID_DELIMITER + query);
			if (json.get("valueCount").getAsInt() > 0) {
				response.status(HTTP_RESPONSE_CODES.OK.code);
			} else {
				response.status(HTTP_RESPONSE_CODES.NOT_FOUND.code);
			}
			return json.toString();
		});


		get(ENDPOINTS_ADMIN_API.AUTHORS.toLibraryTopicKeyPath(), (request, response) -> {
			response.type(Constants.UTF_JSON);
			String query = ServiceProvider.createStringFromSplat(request.splat(), Constants.ID_DELIMITER);
			JsonObject json = storeManager.getWhereStartsWith(SYSTEM_LIBS.AUTHORS + Constants.ID_DELIMITER + query);
			if (json.get("valueCount").getAsInt() > 0) {
				response.status(HTTP_RESPONSE_CODES.OK.code);
			} else {
				response.status(HTTP_RESPONSE_CODES.NOT_FOUND.code);
			}
			return json.toString();
		});
		
	}

}
