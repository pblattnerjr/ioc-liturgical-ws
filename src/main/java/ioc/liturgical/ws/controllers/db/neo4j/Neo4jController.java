package ioc.liturgical.ws.controllers.db.neo4j;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static spark.Spark.delete;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import ioc.liturgical.ws.app.ServiceProvider;
import ioc.liturgical.ws.constants.Constants;
import ioc.liturgical.ws.constants.ENDPOINTS_DB_API;
import ioc.liturgical.ws.constants.ENDPOINT_TYPES;
import ioc.liturgical.ws.constants.HTTP_RESPONSE_CODES;
import ioc.liturgical.ws.constants.db.external.SINGLETON_KEYS;
import ioc.liturgical.ws.controllers.admin.ControllerUtils;
import ioc.liturgical.ws.managers.auth.AuthDecoder;
import ioc.liturgical.ws.managers.databases.external.neo4j.ExternalDbManager;
import ioc.liturgical.ws.models.RequestStatus;
import ioc.liturgical.ws.models.ResultJsonObjectArray;

public class Neo4jController {
	private static final Logger logger = LoggerFactory.getLogger(Neo4jController.class);
	
    private Gson gson = new GsonBuilder().disableHtmlEscaping().create();
    
    
    
	/**
	 * returns a login form
	 * @param storeManager
	 */
	public Neo4jController(ExternalDbManager externalManager) {
	
		String path = ENDPOINTS_DB_API.DOCS.toLibraryTopicKeyPath();
		ControllerUtils.reportPath(logger, "GET", path);
		get(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
			String id = ServiceProvider.createStringFromSplat(request.splat(), Constants.ID_DELIMITER);
			ResultJsonObjectArray json = externalManager.getForId(id);
			if (json.valueCount > 0) {
				response.status(HTTP_RESPONSE_CODES.OK.code);
			} else {
				response.status(HTTP_RESPONSE_CODES.NOT_FOUND.code);
			}
			return json.toJsonString();
		});

		// GET docs for specified parameters
		path = ENDPOINTS_DB_API.DOCS.pathname;
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

		// GET ontology entries for specified parameters
		path = ENDPOINTS_DB_API.ONTOLOGY.pathname;
		ControllerUtils.reportPath(logger, "GET", path);
		get(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
        	return gson.toJson(externalManager.searchOntology(
        			request.queryParams("t")  // ontology type (e.g. Animal)
        			, request.queryParams("g")  // generic type
        			, request.queryParams("q")   // query
        			, request.queryParams("p") // property of the doc (e.g. the ID, the value)
        			, request.queryParams("m") // matcher (e.g. contains, starts with, regex)
        			, request.queryParams("l") // tags (~labels)
        			, request.queryParams("o") // operator
        			));
		});

		// GET ontology entries for specified parameters
		path = ENDPOINTS_DB_API.TEXT_ANALYSIS.toLibraryPath();
		ControllerUtils.reportPath(logger, "GET", path);
		get(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
			String requestor = new AuthDecoder(request.headers("Authorization")).getUsername();
			String id = request.splat()[0];
			return externalManager.getWordGrammarAnalyses(requestor, id).toJsonString();
		});

		/**
		 * provides a list of available REST endpoints (i.e. resources)
		 * @param storeManager
		 */
		path = ENDPOINTS_DB_API.NEW.toLibraryPath();
		ControllerUtils.reportPath(logger, "GET", path);
		get(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
			String query = ServiceProvider.createStringFromSplat(request.splat(), Constants.ID_DELIMITER);
			String requestor = new AuthDecoder(request.headers("Authorization")).getUsername();
			JsonObject json = externalManager.getNewDocForms(requestor, query);
			if (json.get("valueCount").getAsInt() > 0) {
				response.status(HTTP_RESPONSE_CODES.OK.code);
			} else {
				response.status(HTTP_RESPONSE_CODES.NOT_FOUND.code);
			}
			return json.toString();
		});

		// GET domain dropdown lists for specified user
		path = Constants.EXTERNAL_DATASTORE_API_PATH  + "/dropdowns/domains/*";
		ControllerUtils.reportPath(logger, "GET", path);
		get(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
			String user = request.splat()[0];
        	return externalManager.getDomainDropdownsForUser(user).toString();
		});

		// GET domains as a dropdown list
		path = Constants.EXTERNAL_DATASTORE_API_PATH  + "/dropdowns/domains";
		ControllerUtils.reportPath(logger, "GET", path);
		get(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
        	return externalManager.getDropdownItemsForSearchingText().toString();
		});

		// GET gr_gr_cog topics as a dropdown list
		path = ENDPOINTS_DB_API.DROPDOWNS_GR_LIB_TOPICS.pathname;
		ControllerUtils.reportPath(logger, "GET", path);
		get(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
        	return externalManager.getTopicsDropdown().toJsonString();
		});

		// GET keys and values for specified topic and specified libraries
		// Used by client side Parallel Column Text Editor (ParaColTextEditor)
		path = ENDPOINTS_DB_API.VIEW_TOPIC.toLibraryPath();
		ControllerUtils.reportPath(logger, "GET", path);
		get(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
			String[] libraries = null;
			try {
				libraries = request.queryParams("l").split(",");
			} catch (Exception e) {
				
			}
        	return externalManager.getTopicValuesForParaColTextEditor(
        			"gr_gr_cog" // for now.  In future, support other source texts
        			, request.queryParams("t")  // topic
        			, libraries
        			).toJsonString();
		});

		// GET dropdowns for searching docs of type text
		path = ENDPOINTS_DB_API.DROPDOWNS_TEXTS.pathname;
		ControllerUtils.reportPath(logger, "GET", path);
		get(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
        	return externalManager.getDropdownItemsForSearchingText().toString();
		});

		// GET dropdowns for searching ontology properties
		path = ENDPOINTS_DB_API.DROPDOWNS_ONTOLOGY.pathname;
		ControllerUtils.reportPath(logger, "GET", path);
		get(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
        	return externalManager.getOntologySearchDropdown().toJsonString();
		});

		// GET data for a react-bootstrap-table
		path = ENDPOINTS_DB_API.TABLES.toLibraryPath();
		ControllerUtils.reportPath(logger, "GET", path);
		get(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
        	return externalManager.getForId(SINGLETON_KEYS.TABLE_OALD_SENSES.toId()).toJsonString();
		});

		// GET dropdowns for searching relationship properties
		path = ENDPOINTS_DB_API.DROPDOWNS_RELATIONSHIPS.pathname;
		ControllerUtils.reportPath(logger, "GET", path);
		get(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
        	return externalManager.getRelationshipSearchDropdown().toJsonString();
		});

		// GET link (relationships) for specified /library/topic/key
		path = ENDPOINTS_DB_API.LINKS.toLibraryTopicKeyPath();
		ControllerUtils.reportPath(logger, "GET", path);
		get(path, (request, response) -> {
			String requestor = new AuthDecoder(request.headers("Authorization")).getUsername();
			String library = request.splat()[0];
			String id = ServiceProvider.createStringFromSplat(request.splat(), Constants.ID_DELIMITER);
			response.type(Constants.UTF_JSON);
        	return gson.toJson(
        			externalManager.getRelationshipById(
        			requestor
        			, library
        			, id
        			)
        	);
		});

		// GET links (relationships) for specified parameters
		path = ENDPOINTS_DB_API.LINKS.pathname;
		ControllerUtils.reportPath(logger, "GET", path);
		get(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
        	return gson.toJson(externalManager.searchRelationships(
        			request.queryParams("t")  // link type (e.g. REFERS_TO_BIBLICAL_TEXT)
        			, request.queryParams("d")  // domain
        			, request.queryParams("q")   // query
        			, request.queryParams("p") // property of the doc (e.g. the ID, the value)
        			, request.queryParams("m") // matcher (e.g. contains, starts with, regex)
        			, request.queryParams("l") // tags (~labels)
        			, request.queryParams("o") // operator
        			, request.queryParams("x") // exclude biblical texts
        			));
		});

		// GET AGES index table data
		path = ENDPOINTS_DB_API.AGES_INDEX.pathname;
		ControllerUtils.reportPath(logger, "GET", path);
		get(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
        	return externalManager.getAgesIndexTableData(
        			).toJsonString();
		});

		// GET AGES template created from the specified url parameter
		path = ENDPOINTS_DB_API.AGES_REACT_TEMPLATE.toLibraryPath();
		ControllerUtils.reportPath(logger, "GET", path);
		get(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
        	return externalManager.getAgesTemplateMetadata(
        			request.queryParams("u")  // url
        			, request.queryParams("t")  // translation library
        			).toJsonString();
		});

		// GET AGES read only template created from the specified url parameter
		path = ENDPOINTS_DB_API.AGES_READ_ONLY_TEMPLATE.toLibraryPath();
		ControllerUtils.reportPath(logger, "GET", path);
		get(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
        	return externalManager.getAgesService(
        			request.queryParams("u")  // url
        			, request.queryParams("l")  // left library
        			, request.queryParams("c")  // center library
        			, request.queryParams("r")  // right library
        			, request.queryParams("lf")  // left fallback library
        			, request.queryParams("cf")  // center fallback library
        			, request.queryParams("rf")  // right fallback library
        			).toJsonString();
		});

		// /Users/mac002/Downloads/priestsServiceBook.pdf
		// GET PDF genrated from an AGES HTML file using the specified url parameter
		path = ENDPOINTS_DB_API.AGES_PDF.pathname;
		ControllerUtils.reportPath(logger, "GET", path);
		get(path, (request, response) -> {
			  try {
			        Path filePath = Paths.get("/Users/mac002/Downloads/priestsServiceBook.pdf");
			        byte[] data = Files.readAllBytes(filePath);
			 /**
			  * Options:
			  * 1. Regenerate the json, then create the PDF
			  * 2. Pass in the json and use it to create the PDF
			  * 3. Generate a PDF whenever the json is generated.
			  */
			        HttpServletResponse httpServletResponse = response.raw();
			        httpServletResponse.setContentType("application/pdf");
			        httpServletResponse.addHeader("Content-Disposition", "inline; filename=priestsservicebook.pdf");
			        httpServletResponse.getOutputStream().write(data);
			        httpServletResponse.getOutputStream().close();
			    } catch (IOException e) {
			        e.printStackTrace();
			    }
			 
			    return "";
		});

		// Get forms for creating new instances of nodes and relationships
		path = ENDPOINTS_DB_API.NEW.toLibraryPath();
		ControllerUtils.reportPath(logger, "GET", path);
		get(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
			String query = ServiceProvider.createStringFromSplat(request.splat(), Constants.ID_DELIMITER);
			String requestor = new AuthDecoder(request.headers("Authorization")).getUsername();
			JsonObject json = externalManager.getNewDocForms(requestor, query);
			if (json.get("valueCount").getAsInt() > 0) {
				response.status(HTTP_RESPONSE_CODES.OK.code);
			} else {
				response.status(HTTP_RESPONSE_CODES.NOT_FOUND.code);
			}
			return json.toString();
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
		
		// post a doc
		path = ENDPOINTS_DB_API.DOCS.pathname;
		ControllerUtils.reportPath(logger, "POST", path);
		post(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
			String requestor = new AuthDecoder(request.headers("Authorization")).getUsername();
			RequestStatus requestStatus = externalManager.addLTKDbObject(
					requestor
					, request.body()
					);
			response.status(requestStatus.getCode());
			return requestStatus.toJsonString();
		});

		// post a reference
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
		 * PUT controllers - updates
		 */
		// put (update) the value of a doc 
		path = ENDPOINTS_DB_API.VALUE.toLibraryPath();
		ControllerUtils.reportPath(logger, "PUT", path);
		put(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
			String requestor = new AuthDecoder(request.headers("Authorization")).getUsername();
			String id = request.queryParams("i");
			String body = request.body();

			RequestStatus requestStatus = externalManager.updateValueOfLiturgicalText(
					requestor
					, id
					, body
			);
			response.status(requestStatus.getCode());
			return requestStatus.toJsonString();
		});

		// put (update) a doc 
		path = ENDPOINTS_DB_API.DOCS.toLibraryTopicKeyPath();
		ControllerUtils.reportPath(logger, "PUT", path);
		put(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
			String requestor = new AuthDecoder(request.headers("Authorization")).getUsername();
			RequestStatus requestStatus = externalManager.updateLTKDbObject(
					requestor
					, request.body()
					);
			response.status(requestStatus.getCode());
			return requestStatus.toJsonString();
		});

		// put (update) a reference
		path = ENDPOINTS_DB_API.LINKS.toLibraryTopicKeyPath();
		ControllerUtils.reportPath(logger, "PUT", path);
		put(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
			String requestor = new AuthDecoder(request.headers("Authorization")).getUsername();
			String id = ServiceProvider.createStringFromSplat(request.splat(), Constants.ID_DELIMITER);
			RequestStatus requestStatus = externalManager.updateReference(
					requestor
					, id
					, request.body()
					);
			response.status(requestStatus.getCode());
			return requestStatus.toJsonString();
		});


}

}
