package ioc.liturgical.ws.controllers.db.neo4j;

import static spark.Spark.get;
import static spark.Spark.post;

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
	public Neo4jController(ExternalDbManager docService) {
		
		// GET docs for specified parameters
		String path = ENDPOINTS_DB_API.DOCS.pathname;
		ControllerUtils.reportPath(logger, "GET", path);
		get(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
        	return gson.toJson(docService.search(
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
        	return docService.getDropdownItems().toString();
		});

		// GET dropdowns
		path = Constants.EXTERNAL_DATASTORE_API_PATH  + "/dropdowns";
		ControllerUtils.reportPath(logger, "GET", path);
		get(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
        	return docService.getDropdownItems().toString();
		});

		// GET rels (relationships) for specified parameters
		path = ENDPOINTS_DB_API.LINKS.pathname;
		ControllerUtils.reportPath(logger, "GET", path);
		get(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
        	return gson.toJson(docService.searchRelationships(
        			request.queryParams("t")  // link type (e.g. REFERS_TO)
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
			RequestStatus requestStatus = docService.deleteForRelationshipId(requestor, id);
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
			RequestStatus requestStatus = docService.deleteForId(requestor, id);
			response.status(requestStatus.getCode());
			return requestStatus.toJsonString();
		});

}

}
