package ioc.liturgical.ws.controllers.db.neo4j;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletResponse;

import org.ocmc.ioc.liturgical.schemas.constants.SINGLETON_KEYS;
import org.ocmc.ioc.liturgical.schemas.constants.TOPICS;
import org.ocmc.ioc.liturgical.schemas.constants.HTTP_RESPONSE_CODES;
import org.apache.commons.io.FileUtils;
import org.joda.time.Instant;
import org.ocmc.ioc.liturgical.schemas.constants.ENDPOINTS_DB_API;
import org.ocmc.ioc.liturgical.schemas.models.ws.response.RequestStatus;
import org.ocmc.ioc.liturgical.schemas.models.ws.response.ResultJsonObjectArray;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static spark.Spark.delete;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import ioc.liturgical.ws.app.ServiceProvider;
import ioc.liturgical.ws.constants.Constants;
import ioc.liturgical.ws.controllers.admin.ControllerUtils;
import ioc.liturgical.ws.managers.auth.AuthDecoder;
import ioc.liturgical.ws.managers.databases.external.neo4j.ExternalDbManager;

public class Neo4jController {
	private static final Logger logger = LoggerFactory.getLogger(Neo4jController.class);
	
    private Gson gson = new GsonBuilder().disableHtmlEscaping().create(); 
    
	/**
	 * returns a login form
	 * @param storeManager
	 */
	public Neo4jController(ExternalDbManager externalManager) {
	
		// GET analyses for treebank matching specified parameters
		String path = ENDPOINTS_DB_API.TREEBANKS.toLibraryTopicPath();
		ControllerUtils.reportPath(logger, "GET", path);
		get(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
			String requestor = new AuthDecoder(request.headers("Authorization")).getUsername();
			String idTopic = request.splat()[1];
        	return gson.toJson(externalManager.getDependencyDiagramData(
        			requestor
        			, idTopic
        			, request.queryParams("t")  // ontology type
        			));
		});

		// GET generate Text downloads for specified parameters
		path = ENDPOINTS_DB_API.LITURGICAL_TEXT_DOWNLOADS.toLibraryTopicPath();
		ControllerUtils.reportPath(logger, "GET", path);
		get(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
			String requestor = new AuthDecoder(request.headers("Authorization")).getUsername();
			String id = ServiceProvider.createStringFromSplat(request.splat(), Constants.ID_DELIMITER);
			String includePersonalNotes = request.queryParams("ip")  ;
        	return gson.toJson(
        			externalManager.createDownloads(
        					requestor
        					, id
        					, includePersonalNotes
        					)
        			);
		});

		// GET template by ID
		path = ENDPOINTS_DB_API.TEMPLATES.toLibraryTopicKeyPath();
		ControllerUtils.reportPath(logger, "GET", path);
		get(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
			String requestor = new AuthDecoder(request.headers("Authorization")).getUsername();
			String id = ServiceProvider.createStringFromSplat(request.splat(), Constants.ID_DELIMITER);
			ResultJsonObjectArray json = externalManager.getTemplateForId(requestor, id);
			if (json.valueCount > 0) {
				response.status(HTTP_RESPONSE_CODES.OK.code);
			} else {
				response.status(HTTP_RESPONSE_CODES.NOT_FOUND.code);
			}
			return json.toJsonString();
		});

		path = ENDPOINTS_DB_API.DOCS.toLibraryTopicKeyPath();
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
			String requestor = "*";
			try {
				requestor = new AuthDecoder(request.headers("Authorization")).getUsername();
			} catch (Exception e) {
				requestor = "*";
			}
	//		JsonObject test = externalManager.getTextInformation(requestor, "gr_gr_cog" + request.queryParams("q"), true);
        	return gson.toJson(externalManager.search(
        			requestor
        			, request.queryParams("t")  // doc type (e.g. Liturgical, Biblical)
        			, request.queryParams("d")  // domain
        			, request.queryParams("b") // book
        			, request.queryParams("c") // chapter or other major part of book
        			, request.queryParams("q")  // query
        			, request.queryParams("p") // property of the doc (e.g. the ID, the value)
        			, request.queryParams("m") // matcher (e.g. contains, starts with, regex)
        			));
		});

		// GET ontology entries for specified parameters
		path = ENDPOINTS_DB_API.ONTOLOGY.pathname;
		ControllerUtils.reportPath(logger, "GET", path);
		get(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
			String requestor = "*";
			try {
				requestor = new AuthDecoder(request.headers("Authorization")).getUsername();
			} catch (Exception e) {
				requestor = "*";
			}
        	return gson.toJson(externalManager.searchOntology(
        			requestor
        			, request.queryParams("t")  // ontology type (e.g. Animal)
        			, request.queryParams("g")  // generic type
        			, request.queryParams("q")   // query
        			, request.queryParams("p") // property of the doc (e.g. the ID, the value)
        			, request.queryParams("m") // matcher (e.g. contains, starts with, regex)
        			, request.queryParams("l") // tags (~labels)
        			, request.queryParams("o") // operator
        			));
		});

		// GET text analysis for specified ID
		path = ENDPOINTS_DB_API.TEXT_ANALYSIS.toLibraryPath();
		ControllerUtils.reportPath(logger, "GET", path);
		get(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
			String requestor = new AuthDecoder(request.headers("Authorization")).getUsername();
			String id = request.splat()[0];
			return externalManager.getWordGrammarAnalyses(requestor, id).toJsonString();
		});

		// GET all notes for a user
		path = ENDPOINTS_DB_API.NOTES.toLibraryPath();
		ControllerUtils.reportPath(logger, "GET", path);
		get(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
			String query = ServiceProvider.createStringFromSplat(request.splat(), Constants.ID_DELIMITER);
			ResultJsonObjectArray json = externalManager.getForIdStartsWith(query, TOPICS.NOTE_USER);
			if (json.getValueCount() > 0) {
				response.status(HTTP_RESPONSE_CODES.OK.code);
			} else {
				response.status(HTTP_RESPONSE_CODES.NOT_FOUND.code);
			}
			return json.toString();
		});

		// GET notes matching specified parameters
		path = ENDPOINTS_DB_API.NOTES.pathname;
		ControllerUtils.reportPath(logger, "GET", path);
		get(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
			String requestor = new AuthDecoder(request.headers("Authorization")).getUsername();
        	return gson.toJson(externalManager.searchNotes(
        			requestor
        			, request.queryParams("t")  // note type (e.g. NoteUser)
        			, request.queryParams("q")   // query
        			, request.queryParams("p") // property of the doc (e.g. the ID, the value)
        			, request.queryParams("m") // matcher (e.g. contains, starts with, regex)
        			, request.queryParams("l") // tags (~labels)
        			, request.queryParams("o") // operator
        			, false // do not return all properties
        			));
		});

		// GET template or template sections matching specified parameters
		path = ENDPOINTS_DB_API.TEMPLATES.pathname;
		ControllerUtils.reportPath(logger, "GET", path);
		get(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
			String requestor = new AuthDecoder(request.headers("Authorization")).getUsername();
        	return gson.toJson(externalManager.searchTemplates(
        			requestor
        			, request.queryParams("t")  // type (Template or Section)
        			, request.queryParams("q")   // query
        			, request.queryParams("p") // property of the doc (e.g. the ID, the value)
        			, request.queryParams("m") // matcher (e.g. contains, starts with, regex)
        			, request.queryParams("l") // tags (~labels)
        			, request.queryParams("o") // operator
        			));
		});

		// GET analyses for treebank matching specified parameters
		path = ENDPOINTS_DB_API.TREEBANKS.pathname;
		ControllerUtils.reportPath(logger, "GET", path);
		get(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
			String requestor = new AuthDecoder(request.headers("Authorization")).getUsername();
        	return gson.toJson(externalManager.searchTreebanks(
        			requestor
        			, request.queryParams("t")  // type (e.g. PtbWord)
        			, request.queryParams("q")   // query
        			, request.queryParams("p") // property of the doc (e.g. the ID, the value)
        			, request.queryParams("m") // matcher (e.g. contains, starts with, regex)
        			, request.queryParams("l") // tags (~labels)
        			, request.queryParams("o") // operator
        			));
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

		// GET dropdowns for searching ontology properties
		path = ENDPOINTS_DB_API.DROPDOWNS_ONTOLOGY_ENTITIES.pathname;
		ControllerUtils.reportPath(logger, "GET", path);
		get(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
        	return externalManager.getOntologyEntitiesDropdown(
        			request.queryParams("t")  // link type (e.g. REFERS_TO_HUMAN)
        			).toJsonString();
		});

		// GET dropdowns for searching notes
		path = ENDPOINTS_DB_API.DROPDOWNS_NOTES.pathname;
		ControllerUtils.reportPath(logger, "GET", path);
		get(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
			String requestor = new AuthDecoder(request.headers("Authorization")).getUsername();
        	return externalManager.getNotesSearchDropdown(requestor).toJsonString();
		});

		// GET dropdowns for searching templates
		path = ENDPOINTS_DB_API.DROPDOWNS_TEMPLATES.pathname;
		ControllerUtils.reportPath(logger, "GET", path);
		get(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
        	return externalManager.getTemplatesSearchDropdown().toJsonString();
		});

		// GET dropdowns for searching treebank
		path = ENDPOINTS_DB_API.DROPDOWNS_TREEBANKS.pathname;
		ControllerUtils.reportPath(logger, "GET", path);
		get(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
        	return externalManager.getTreebanksSearchDropdown().toJsonString();
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
			String requestor = "*";
			try {
				requestor = new AuthDecoder(request.headers("Authorization")).getUsername();
			} catch (Exception e) {
				requestor = "*";
			}
        	return gson.toJson(externalManager.searchRelationships(
        			requestor
        			, request.queryParams("t")  // link type (e.g. REFERS_TO_BIBLICAL_TEXT)
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
        	return externalManager.getAgesLDOM(
        			request.queryParams("u")  // url
        			, request.queryParams("t")  // translation library
        			).toJsonString();
		});

		// GET AGES read only template created from the specified url parameter
		path = ENDPOINTS_DB_API.AGES_READ_ONLY_TEMPLATE.toLibraryPath();
		ControllerUtils.reportPath(logger, "GET", path);
		get(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
			String requestor = new AuthDecoder(request.headers("Authorization")).getUsername();
        	return externalManager.getAgesService(
        			request.queryParams("u")  // url
        			, request.queryParams("l")  // left library
        			, request.queryParams("c")  // center library
        			, request.queryParams("r")  // right library
        			, request.queryParams("lf")  // left fallback library
        			, request.queryParams("cf")  // center fallback library
        			, request.queryParams("rf")  // right fallback library
        			, requestor
        			).toJsonString();
		});

		// GET PDF generated from an AGES HTML file using the specified url parameter
		path = ENDPOINTS_DB_API.PDF.pathname;
		ControllerUtils.reportPath(logger, "GET", path);
		get(path, (request, response) -> {
			  try {
      				String id = request.queryParams("id");
			        Path filePath = Paths.get(Constants.PDF_FOLDER + "/" + id + ".pdf");
			        File file = new File(Constants.PDF_FOLDER + "/" + id + ".pdf");
			        if (! file.exists()) { // wait because the pdf still might be generating
			        	long millis =  15000; // 60000 = 1 minute
			        	for (int i = 0; i < 5; i++) {
			        		Thread.sleep(millis);
			        		if (file.exists()) {
			        			break;
			        		}
			        	}
			        }
			        
			        File newFile = new File(
			        		ServiceProvider.staticExternalFileLocation 
			        		+ "/pdf/" 
			        		+ id
			        		+ ".pdf"
			        );
			        byte[] data = Files.readAllBytes(filePath);
			        HttpServletResponse httpServletResponse = response.raw();
			        httpServletResponse.setContentType("application/pdf");
			        httpServletResponse.setContentLength(data.length);
			        httpServletResponse.addHeader("Content-Disposition", "inline; filename=" + id + ".pdf");
			        httpServletResponse.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			        httpServletResponse.addHeader("Pragma", "no-cache");
			        httpServletResponse.addHeader("Expires", "0");
			        httpServletResponse.getOutputStream().write(data);
			        httpServletResponse.getOutputStream().flush();
			        httpServletResponse.getOutputStream().close();
			        
			        /**
    						headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
			         */
			    } catch (IOException e) {
			        e.printStackTrace();
			    }
			 
			    return "";
		});

		// GET TEX generated from an AGES HTML file using the specified url parameter
		path = ENDPOINTS_DB_API.TEX.pathname;
		ControllerUtils.reportPath(logger, "GET", path);
		get(path, (request, response) -> {
			  try {
      				String id = request.queryParams("id");
			        Path filePath = Paths.get(Constants.PDF_FOLDER + "/" + id + ".tex");
			        File file = new File(Constants.PDF_FOLDER + "/" + id + ".tex");
			        if (! file.exists()) { // wait because the pdf still might be generating
			        	long millis =  15000; // 60000 = 1 minute
			        	for (int i = 0; i < 5; i++) {
			        		Thread.sleep(millis);
			        		if (file.exists()) {
			        			break;
			        		}
			        	}
			        }
			        byte[] data = Files.readAllBytes(filePath);
			        HttpServletResponse httpServletResponse = response.raw();
			        httpServletResponse.setContentType("application/x-latex");
			        httpServletResponse.addHeader("Content-Disposition", "inline; filename=" + id + ".tex");
			        httpServletResponse.getOutputStream().write(data);
			        httpServletResponse.getOutputStream().close();
			    } catch (IOException e) {
			        e.printStackTrace();
			    }
			 
			    return "";
		});

		// GET the user's personal library as a json string
		path = ENDPOINTS_DB_API.USER_DOCS.pathname;
		ControllerUtils.reportPath(logger, "GET", path);
		get(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
			String requestor = new AuthDecoder(request.headers("Authorization")).getUsername();
			return externalManager.getUserPersonalDocs(requestor);
		});

		// GET LDOM
		path = ENDPOINTS_DB_API.LITURGICAL_DOCUMENT_OBJECT_MODEL.toLibraryPath();
		ControllerUtils.reportPath(logger, "GET", path);
		get(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
			String requestor = new AuthDecoder(request.headers("Authorization")).getUsername();
        	return externalManager.getLdomForTemplate(
        			request.queryParams("id")  // template ID
        			, request.queryParams("y")  // year (if a dated service)
        			, request.queryParams("l")  // left library
        			, request.queryParams("c")  // center library
        			, request.queryParams("r")  // right library
        			, request.queryParams("lf")  // left fallback library
        			, request.queryParams("cf")  // center fallback library
        			, request.queryParams("rf")  // right fallback library
        			, requestor
        			).toJsonString();
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
		
		// post a template
		path = ENDPOINTS_DB_API.TEMPLATES.pathname;
		ControllerUtils.reportPath(logger, "POST", path);
		post(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
			String requestor = new AuthDecoder(request.headers("Authorization")).getUsername();
			RequestStatus requestStatus = externalManager.addTemplate(
					requestor
					, request.body()
					);
			response.status(requestStatus.getCode());
			return requestStatus.toJsonString();
		});
		
		// post a note
		path = ENDPOINTS_DB_API.NOTES.pathname;
		ControllerUtils.reportPath(logger, "POST", path);
		post(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
			String requestor = new AuthDecoder(request.headers("Authorization")).getUsername();
			RequestStatus requestStatus = externalManager.addNote(
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

		// put (update) a template 
		path = ENDPOINTS_DB_API.TEMPLATES.toLibraryTopicKeyPath();
		ControllerUtils.reportPath(logger, "PUT", path);
		put(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
			String requestor = new AuthDecoder(request.headers("Authorization")).getUsername();
			RequestStatus requestStatus = externalManager.updateTemplate(
					requestor
					, request.body()
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

		// put (update) a note 
		path = ENDPOINTS_DB_API.NOTES.toLibraryTopicKeyPath();
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

		// put (update) a token analysis 
		path = ENDPOINTS_DB_API.TOKEN_ANALYSIS.toLibraryTopicKeyPath();
		ControllerUtils.reportPath(logger, "PUT", path);
		put(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
			String requestor = new AuthDecoder(request.headers("Authorization")).getUsername();
			RequestStatus requestStatus = externalManager.updateTokenAnalysis(
					requestor
					, request.body()
					);
			response.status(requestStatus.getCode());
			return requestStatus.toJsonString();
		});

}

}
