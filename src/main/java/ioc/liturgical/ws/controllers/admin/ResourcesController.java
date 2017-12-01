package ioc.liturgical.ws.controllers.admin;

import static spark.Spark.get;

import org.ocmc.ioc.liturgical.schemas.constants.ENDPOINTS_ADMIN_API;
import org.ocmc.ioc.liturgical.schemas.constants.HTTP_RESPONSE_CODES;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import ioc.liturgical.ws.constants.Constants;
import ioc.liturgical.ws.managers.databases.internal.InternalDbManager;

public class ResourcesController {
	private static final Logger logger = LoggerFactory.getLogger(ResourcesController.class);
	
	/**
	 * provides a list of available REST endpoints (i.e. resources)
	 * @param storeManager
	 */
	public ResourcesController(InternalDbManager storeManager) {

		String path = Constants.INTERNAL_DATASTORE_API_PATH + "/" + Constants.RESOURCES_PATH;
		ControllerUtils.reportPath(logger, "GET", path);
		get(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
			response.status(HTTP_RESPONSE_CODES.OK.code);
			JsonObject json = new JsonObject();
			JsonArray endpoints = new JsonArray();
			
			for (ENDPOINTS_ADMIN_API e : ENDPOINTS_ADMIN_API.values()) {
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
