package ioc.liturgical.ws.controllers.admin;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

import org.ocmc.ioc.liturgical.schemas.constants.ENDPOINTS_ADMIN_API;
import org.ocmc.ioc.liturgical.schemas.constants.HTTP_RESPONSE_CODES;
import org.ocmc.ioc.liturgical.schemas.constants.NEW_FORM_CLASSES_ADMIN_API;
import org.ocmc.ioc.liturgical.schemas.models.ws.response.RequestStatus;
import org.ocmc.ioc.liturgical.schemas.models.ws.response.ResultJsonObjectArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;

import ioc.liturgical.ws.app.ServiceProvider;
import ioc.liturgical.ws.constants.Constants;
import ioc.liturgical.ws.managers.auth.AuthDecoder;
import ioc.liturgical.ws.managers.databases.external.neo4j.ExternalDbManager;
import ioc.liturgical.ws.managers.databases.internal.InternalDbManager;

public class DomainsController {
	private static final Logger logger = LoggerFactory.getLogger(DomainsController.class);
	
	public DomainsController(
			InternalDbManager storeManager
			, ExternalDbManager externalDbManager
			) {

		/**
		 * GET controllers
		 */
		String path = ENDPOINTS_ADMIN_API.DOMAINS.toLibraryTopicKeyPath();
		ControllerUtils.reportPath(logger, "GET", path);
		get(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
			String query = ServiceProvider.createStringFromSplat(request.splat(), Constants.ID_DELIMITER);
			ResultJsonObjectArray json = storeManager.getForId(query);
			if (json.valueCount > 0) {
				response.status(HTTP_RESPONSE_CODES.OK.code);
			} else {
				response.status(HTTP_RESPONSE_CODES.NOT_FOUND.code);
			}
			return json.toJsonString();
		});

		path = ENDPOINTS_ADMIN_API.DOMAINS.toLibraryTopicPath();
		ControllerUtils.reportPath(logger, "GET", path);
		get(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
			String query = ServiceProvider.createStringFromSplat(request.splat(), Constants.ID_DELIMITER);
			ResultJsonObjectArray json = storeManager.getForIdStartsWith(query);
			if (json.getValueCount() > 0) {
				response.status(HTTP_RESPONSE_CODES.OK.code);
			} else {
				response.status(HTTP_RESPONSE_CODES.NOT_FOUND.code);
			}
			return json.toJsonString();
		});

		path = ENDPOINTS_ADMIN_API.DOMAINS.toLibraryPath();
		String db = ENDPOINTS_ADMIN_API.DOMAINS.toDbLibraryTopic(); // yes, this is correct.  domains is a topic.
		ControllerUtils.reportPath(logger, "GET", path + " maps to database id " + db);
		get(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
			ResultJsonObjectArray json = storeManager.getForIdStartsWith(db);
			if (json.getValueCount() > 0) {
				response.status(HTTP_RESPONSE_CODES.OK.code);
			} else {
				response.status(HTTP_RESPONSE_CODES.NOT_FOUND.code);
			}
			return json.toJsonString();
		});
		
		path = ENDPOINTS_ADMIN_API.DOMAINS_USER_ROLE_DROPDOWNS.toLibraryTopicPath();
		ControllerUtils.reportPath(logger, "GET", path + " gets user role dropdowns for specified path");
		get(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
			String domain = ServiceProvider.createStringFromSplat(
					request.splat()
					, Constants.ID_DELIMITER
					);
			JsonObject json = storeManager.getDropdownsForUsersWithRoleForDomain(domain);
			if (json.get("valueCount").getAsInt() > 0) {
				response.status(HTTP_RESPONSE_CODES.OK.code);
			} else {
				response.status(HTTP_RESPONSE_CODES.NOT_FOUND.code);
			}
			return json.toString();
		});
		
		path = ENDPOINTS_ADMIN_API.DOMAINS_COLLECTIVE.toLibraryPath();
		ControllerUtils.reportPath(logger, "GET", path + " gets domains that are collective and liturgical");
		get(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
			JsonObject json = storeManager.getDomainsThatAreCollectiveLiturgicalAsJson();
			if (json.get("valueCount").getAsInt() > 0) {
				response.status(HTTP_RESPONSE_CODES.OK.code);
			} else {
				response.status(HTTP_RESPONSE_CODES.NOT_FOUND.code);
			}
			return json.toString();
		});

		/**
		 * POST controllers
		 */
		path = NEW_FORM_CLASSES_ADMIN_API.NEW_DOMAIN.toPostPath();
		ControllerUtils.reportPath(logger, "POST", path);
		post(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
			String requestor = new AuthDecoder(request.headers("Authorization")).getUsername();
			RequestStatus requestStatus = storeManager.addDomain(requestor,request.body());
			response.status(requestStatus.getCode());
			externalDbManager.updateDropdownItemsForSearchingText();
			return requestStatus.toJsonString();
		});

		/**
		 * PUT controllers
		 */
		path = ENDPOINTS_ADMIN_API.DOMAINS.toLibraryTopicPath();
		ControllerUtils.reportPath(logger, "PUT", path);
		put(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
			String requestor = new AuthDecoder(request.headers("Authorization")).getUsername();
			String key = ServiceProvider.createStringFromSplat(request.splat(), Constants.ID_DELIMITER);
			RequestStatus requestStatus = storeManager.updateDomain(requestor, key, request.body());
			response.status(requestStatus.getCode());
			externalDbManager.updateDropdownItemsForSearchingText();
			return requestStatus.toJsonString();
		});

	}

}
