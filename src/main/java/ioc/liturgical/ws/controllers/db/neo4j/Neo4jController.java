package ioc.liturgical.ws.controllers.db.neo4j;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static spark.Spark.delete;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ioc.liturgical.ws.app.ServiceProvider;
import ioc.liturgical.ws.constants.Constants;
import ioc.liturgical.ws.constants.ENDPOINTS_DB_API;
import ioc.liturgical.ws.controllers.admin.ControllerUtils;
import ioc.liturgical.ws.managers.auth.AuthDecoder;
import ioc.liturgical.ws.managers.databases.external.neo4j.ExternalDbManager;
import ioc.liturgical.ws.models.RequestStatus;

public class Neo4jController {
	private static final Logger logger = LoggerFactory.getLogger(Neo4jController.class);
	
    private Gson gson = new GsonBuilder().disableHtmlEscaping().create();
    
	/**
	 * returns a login form
	 * @param storeManager
	 */
	public Neo4jController(ExternalDbManager externalManager) {
		
		// GET docs for specified parameters
		String path = ENDPOINTS_DB_API.DOCS.pathname;
		ControllerUtils.reportPath(logger, "GET", path);
		get(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
        	return gson.toJson(externalManager.search(
        			request.queryParams("t")  // doc type (e.g. Liturgical, Biblical)
        			, request.queryParams("d")  // domain
        			, request.queryParams("b") // book
        			, request.queryParams("c") // chapter or other major part of book
        			, request.queryParams("q")   // query
        			, request.queryParams("p") // property of the doc (e.g. the ID, the value)
        			, request.queryParams("m") // matcher (e.g. contains, starts with, regex)
        			));
		});

		// GET domains as a dropdown list
		path = Constants.EXTERNAL_DATASTORE_API_PATH  + "/dropdowns/domains";
		ControllerUtils.reportPath(logger, "GET", path);
		get(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
        	return externalManager.getDropdownItemsForSearchingText().toString();
		});

		// GET dropdowns for searching docs of type text
		path = ENDPOINTS_DB_API.DROPDOWNS_TEXTS.pathname;
		ControllerUtils.reportPath(logger, "GET", path);
		get(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
        	return externalManager.getDropdownItemsForSearchingText().toString();
		});

		// GET dropdowns for searching relationship properties
		path = ENDPOINTS_DB_API.DROPDOWNS_RELATIONSHIPS.pathname;
		ControllerUtils.reportPath(logger, "GET", path);
		get(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
        	return externalManager.getRelationshipSearchDropdown().toJsonString();
		});

		// GET rels (relationships) for specified parameters
		path = ENDPOINTS_DB_API.LINKS.pathname;
		ControllerUtils.reportPath(logger, "GET", path);
		get(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
        	return gson.toJson(externalManager.searchRelationships(
        			request.queryParams("t")  // link type (e.g. REFERS_TO_BIBLICAL_TEXT)
        			, request.queryParams("d")  // domain
        			, request.queryParams("l") // labels
        			, request.queryParams("o") // operator
        			));
		});

		// DELETE relationship for parameter id, where id is the id of the properties in the relationship itself.
		path = ENDPOINTS_DB_API.LINKS.toLibraryTopicKeyPath();
		ControllerUtils.reportPath(logger, "DELETE", path);
		delete(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
			String requestor = new AuthDecoder(request.headers("Authorization")).getUsername();
			String id = ServiceProvider.createStringFromSplat(request.splat(), Constants.ID_DELIMITER);
			RequestStatus requestStatus = externalManager.deleteForRelationshipId(requestor, id);
			response.status(requestStatus.getCode());
			return requestStatus.toJsonString();
		});
		
	   // TODO: add a delete for a relationship between two nodes where there is no specific ID in the properties of the relationship

		// DELETE doc for parameter id
		path = Constants.EXTERNAL_DATASTORE_API_PATH 	+ Constants.PATH_LIBRARY_TOPIC_KEY_WILDCARD;
		ControllerUtils.reportPath(logger, "DELETE", path);
		delete(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
			String requestor = new AuthDecoder(request.headers("Authorization")).getUsername();
			String id = ServiceProvider.createStringFromSplat(request.splat(), Constants.ID_DELIMITER);
			RequestStatus requestStatus = externalManager.deleteForId(requestor, id);
			response.status(requestStatus.getCode());
			return requestStatus.toJsonString();
		});
		
		/**
		 * POST controllers
		 */
		
		path = ENDPOINTS_DB_API.LINKS.pathname;
		ControllerUtils.reportPath(logger, "POST", path);
		post(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
			String requestor = new AuthDecoder(request.headers("Authorization")).getUsername();
			RequestStatus requestStatus = externalManager.addReference(
					requestor
					, request.body()
					);
			response.status(requestStatus.getCode());
			return requestStatus.toJsonString();
		});

		/**
		 * PUT controllers
		 */
		path = ENDPOINTS_DB_API.LINKS.pathname;
		ControllerUtils.reportPath(logger, "PUT", path);
		put(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
			String requestor = new AuthDecoder(request.headers("Authorization")).getUsername();
			String id = ServiceProvider.createStringFromSplat(request.splat(), Constants.ID_DELIMITER);
			RequestStatus requestStatus = externalManager.updateReference(requestor, id, request.body());
			response.status(requestStatus.getCode());
			return requestStatus.toJsonString();
		});


}

}
