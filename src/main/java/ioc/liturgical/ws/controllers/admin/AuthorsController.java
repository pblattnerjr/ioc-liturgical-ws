package ioc.liturgical.ws.controllers.admin;

import static spark.Spark.get;

import org.ocmc.ioc.liturgical.schemas.constants.ENDPOINTS_ADMIN_API;
import org.ocmc.ioc.liturgical.schemas.constants.HTTP_RESPONSE_CODES;
import org.ocmc.ioc.liturgical.schemas.constants.SYSTEM_LIBS;

import com.google.gson.JsonObject;

import ioc.liturgical.ws.app.ServiceProvider;
import ioc.liturgical.ws.constants.Constants;
import ioc.liturgical.ws.managers.databases.internal.InternalDbManager;

public class AuthorsController {
	
	public AuthorsController(InternalDbManager storeManager) {
		get(ENDPOINTS_ADMIN_API.AUTHORS.toLibraryTopicKeyPath(), (request, response) -> {
			response.type(Constants.UTF_JSON);
			String query = ServiceProvider.createStringFromSplat(request.splat(), Constants.ID_DELIMITER);
			JsonObject json = storeManager.getForId(
					SYSTEM_LIBS.AUTHORS 
					+ Constants.ID_DELIMITER 
					+ query
					).toJsonObject();
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
