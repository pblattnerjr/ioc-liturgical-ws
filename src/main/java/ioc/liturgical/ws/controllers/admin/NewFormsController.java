package ioc.liturgical.ws.controllers.admin;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import ioc.liturgical.ws.app.ServiceProvider;
import ioc.liturgical.ws.constants.Constants;
import ioc.liturgical.ws.constants.ADMIN_ENDPOINTS;
import ioc.liturgical.ws.constants.HTTP_RESPONSE_CODES;
import ioc.liturgical.ws.constants.NEW_FORM_CLASSES;
import ioc.liturgical.ws.constants.SYSTEM_LIBS;
import ioc.liturgical.ws.constants.USER_TOPICS;
import ioc.liturgical.ws.manager.auth.AuthDecoder;
import ioc.liturgical.ws.manager.database.internal.InternalDbManager;
import ioc.liturgical.ws.models.RequestStatus;

/**
 * This controller has a single GET handler.  It handles
 * requests for a list of what forms are available to
 * create a doc, i.e. available for a POST.
 * 
 * When an individual form is submitted, it must be handled in
 * the controller for that specific type of form.
 * 
 * @author mac002
 *
 */
public class NewFormsController {
	private static final Logger logger = LoggerFactory.getLogger(NewFormsController.class);
	
	public NewFormsController(InternalDbManager storeManager) {

		String path = ADMIN_ENDPOINTS.NEW.toLibraryPath();
		ControllerUtils.reportPath(logger, "GET", path);
		get(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
			String query = ServiceProvider.createStringFromSplat(request.splat(), Constants.ID_DELIMITER);
			String requestor = new AuthDecoder(request.headers("Authorization")).getUsername();
			JsonObject json = storeManager.getNewDocForms(requestor, query);
			if (json.get("valueCount").getAsInt() > 0) {
				response.status(HTTP_RESPONSE_CODES.OK.code);
			} else {
				response.status(HTTP_RESPONSE_CODES.NOT_FOUND.code);
			}
			return json.toString();
		});

	}
}
