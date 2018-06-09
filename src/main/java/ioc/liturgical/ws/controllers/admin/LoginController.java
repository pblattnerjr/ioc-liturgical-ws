package ioc.liturgical.ws.controllers.admin;

import static spark.Spark.get;

import java.util.ArrayList;
import java.util.List;

import org.ocmc.ioc.liturgical.schemas.models.ws.response.AbstractResponse;
import org.ocmc.ioc.liturgical.schemas.models.ws.response.Login;
import org.ocmc.ioc.liturgical.schemas.models.ws.response.ResultJsonObjectArray;
import org.ocmc.ioc.liturgical.schemas.models.ws.db.UserPreferences;
import org.ocmc.ioc.liturgical.schemas.models.ws.db.UserContact;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;

import ioc.liturgical.ws.constants.Constants;
import ioc.liturgical.ws.managers.auth.AuthDecoder;
import ioc.liturgical.ws.managers.databases.internal.InternalDbManager;

public class LoginController {
	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

	/**
	 * returns a login form
	 * TODO: look at other places to see if it is useful to use AbstractReponse
	 * @param storeManager
	 */
	public LoginController(InternalDbManager storeManager) {
		
		String path = Constants.INTERNAL_DATASTORE_API_PATH  + Constants.LOGIN_PATH + "/form";
		ControllerUtils.reportPath(logger, "GET", path);
		get(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
			return new AbstractResponse<Login>(new Login(), true).toJsonString();
		});

		path = Constants.INTERNAL_DATASTORE_API_PATH  + Constants.LOGIN_PATH + "/user";
		ControllerUtils.reportPath(logger, "GET", path);
		get(path, (request, response) -> {
			response.type(Constants.UTF_JSON);
			AuthDecoder authDecoder = new AuthDecoder(request.headers("Authorization"));
			ResultJsonObjectArray json = new ResultJsonObjectArray(false);
			List<JsonObject> list = new ArrayList<JsonObject>();
			UserContact userContact = storeManager.getUserContact(authDecoder.getUsername());
			list.add(userContact.toJsonObject());
			UserPreferences prefs = storeManager.getUserPreferences(authDecoder.getUsername());
			list.add(prefs.toJsonObject());
			json.setResult(list);
			return json.toJsonString();
		});
	}

}
