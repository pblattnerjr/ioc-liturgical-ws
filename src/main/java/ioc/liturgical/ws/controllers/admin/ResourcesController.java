package ioc.liturgical.ws.controllers.admin;

import static spark.Spark.get;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import ioc.liturgical.ws.constants.Constants;
import ioc.liturgical.ws.constants.ADMIN_ENDPOINTS;
import ioc.liturgical.ws.constants.HTTP_RESPONSE_CODES;
import ioc.liturgical.ws.manager.database.internal.InternalDbManager;

public class ResourcesController {
	
	/**
	 * provides a list of available REST endpoints (i.e. resources)
	 * @param storeManager
	 */
	public ResourcesController(InternalDbManager storeManager) {
		
		get(Constants.INTERNAL_DATASTORE_API_PATH + "/" + Constants.RESOURCES_PATH, (request, response) -> {

			response.type(Constants.UTF_JSON);
			response.status(HTTP_RESPONSE_CODES.OK.code);
			JsonObject json = new JsonObject();
			JsonArray endpoints = new JsonArray();
			
			for (ADMIN_ENDPOINTS e : ADMIN_ENDPOINTS.values()) {
				if (e.includeInResourcesList) {
					JsonObject endpoint = new JsonObject();
					endpoint.addProperty("value", e.pathname);
					endpoint.addProperty("label", e.label);
					endpoints.add(endpoint);
				}
			}
			
			json.add("resources", endpoints);

			return json.toString();
		});
	}

}
