package ioc.liturgical.ws.controllers.admin;

import static spark.Spark.get;

import com.google.gson.JsonObject;

import ioc.liturgical.ws.app.ServiceProvider;
import ioc.liturgical.ws.constants.ADMIN_ENDPOINTS;
import ioc.liturgical.ws.constants.Constants;
import ioc.liturgical.ws.constants.HTTP_RESPONSE_CODES;
import ioc.liturgical.ws.constants.SYSTEM_LIBS;
import ioc.liturgical.ws.managers.databases.internal.InternalDbManager;

public class ReadersController {
	
	public ReadersController(InternalDbManager storeManager) {
		get(ADMIN_ENDPOINTS.READERS.toLibraryTopicKeyPath(), (request, response) -> {
			response.type(Constants.UTF_JSON);
			String query = ServiceProvider.createStringFromSplat(request.splat(), Constants.ID_DELIMITER);
			JsonObject json = storeManager.getForId(SYSTEM_LIBS.READERS + Constants.ID_DELIMITER  + query);
			if (json.get("valueCount").getAsInt() > 0) {
				response.status(HTTP_RESPONSE_CODES.OK.code);
			} else {
				response.status(HTTP_RESPONSE_CODES.NOT_FOUND.code);
			}
			return json.toString();
		});

		get(ADMIN_ENDPOINTS.READERS.toLibraryTopicPath(), (request, response) -> {
			response.type(Constants.UTF_JSON);
			String query = ServiceProvider.createStringFromSplat(request.splat(), Constants.ID_DELIMITER);
			JsonObject json = storeManager.getWhereStartsWith(SYSTEM_LIBS.READERS + Constants.ID_DELIMITER  + query);
			if (json.get("valueCount").getAsInt() > 0) {
				response.status(HTTP_RESPONSE_CODES.OK.code);
			} else {
				response.status(HTTP_RESPONSE_CODES.NOT_FOUND.code);
			}
			return json.toString();
		});


		get(ADMIN_ENDPOINTS.READERS.toLibraryPath(), (request, response) -> {
			response.type(Constants.UTF_JSON);
			String query = ServiceProvider.createStringFromSplat(request.splat(), Constants.ID_DELIMITER);
			JsonObject json = storeManager.getWhereStartsWith(SYSTEM_LIBS.READERS + Constants.ID_DELIMITER  + query);
			if (json.get("valueCount").getAsInt() > 0) {
				response.status(HTTP_RESPONSE_CODES.OK.code);
			} else {
				response.status(HTTP_RESPONSE_CODES.NOT_FOUND.code);
			}
			return json.toString();
		});
		
	}

}
