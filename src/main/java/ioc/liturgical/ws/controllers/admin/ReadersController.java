package ioc.liturgical.ws.controllers.admin;

import static spark.Spark.get;

import org.ocmc.ioc.liturgical.schemas.constants.ENDPOINTS_ADMIN_API;
import org.ocmc.ioc.liturgical.schemas.constants.HTTP_RESPONSE_CODES;
import org.ocmc.ioc.liturgical.schemas.constants.SYSTEM_LIBS;
import org.ocmc.ioc.liturgical.schemas.models.ws.response.ResultJsonObjectArray;

import com.google.gson.JsonObject;

import ioc.liturgical.ws.app.ServiceProvider;
import ioc.liturgical.ws.constants.Constants;
import ioc.liturgical.ws.managers.databases.internal.InternalDbManager;

public class ReadersController {
	
	public ReadersController(InternalDbManager storeManager) {
		get(ENDPOINTS_ADMIN_API.READERS.toLibraryTopicKeyPath(), (request, response) -> {
			response.type(Constants.UTF_JSON);
			String query = ServiceProvider.createStringFromSplat(request.splat(), Constants.ID_DELIMITER);
			ResultJsonObjectArray json = storeManager.getForId(SYSTEM_LIBS.READERS + Constants.ID_DELIMITER  + query);
			if (json.valueCount > 0) {
				response.status(HTTP_RESPONSE_CODES.OK.code);
			} else {
				response.status(HTTP_RESPONSE_CODES.NOT_FOUND.code);
			}
			return json.toJsonString();
		});

		get(ENDPOINTS_ADMIN_API.READERS.toLibraryTopicPath(), (request, response) -> {
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


		get(ENDPOINTS_ADMIN_API.READERS.toLibraryPath(), (request, response) -> {
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
