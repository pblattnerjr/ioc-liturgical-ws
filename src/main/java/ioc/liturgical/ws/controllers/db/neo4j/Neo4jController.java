package ioc.liturgical.ws.controllers.db.neo4j;

import static spark.Spark.get;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ioc.liturgical.ws.constants.Constants;
import ioc.liturgical.ws.managers.databases.external.neo4j.ExternalDbManager;

public class Neo4jController {
	
    private Gson gson = new GsonBuilder().disableHtmlEscaping().create();
    
	/**
	 * returns a login form
	 * @param storeManager
	 */
	public Neo4jController(ExternalDbManager docService) {
		
		get(Constants.EXTERNAL_DATASTORE_API_PATH  + "/docs", (request, response) -> {
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

		get(Constants.EXTERNAL_DATASTORE_API_PATH  + "/dropdowns/domains", (request, response) -> {
			response.type(Constants.UTF_JSON);
        	return docService.getDropdownItems().toString();
		});

		get(Constants.EXTERNAL_DATASTORE_API_PATH  + "/dropdowns", (request, response) -> {
			response.type(Constants.UTF_JSON);
        	return docService.getDropdownItems().toString();
		});

	}

}
