package ioc.liturgical.ws.controllers.admin;

import static spark.Spark.get;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;

import ioc.liturgical.ws.app.ServiceProvider;
import ioc.liturgical.ws.constants.Constants;
import ioc.liturgical.ws.constants.ENDPOINTS_ADMIN_API;
import ioc.liturgical.ws.constants.HTTP_RESPONSE_CODES;
import ioc.liturgical.ws.managers.auth.AuthDecoder;
import ioc.liturgical.ws.managers.databases.internal.InternalDbManager;

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

		String path = ENDPOINTS_ADMIN_API.NEW.toLibraryPath();
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
