package ioc.liturgical.ws.managers.databases.internal;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import ioc.liturgical.ws.managers.interfaces.HighLevelDataStoreInterface;
import ioc.liturgical.ws.models.RequestStatus;
import ioc.liturgical.ws.models.ResultJsonObjectArray;
import ioc.liturgical.ws.models.db.forms.LinkRefersToBiblicalTextCreateForm;
import ioc.liturgical.ws.models.db.links.LinkRefersToBiblicalText;
import ioc.liturgical.ws.models.ws.db.Domain;
import ioc.liturgical.ws.models.ws.db.Label;
import ioc.liturgical.ws.models.ws.db.User;
import ioc.liturgical.ws.models.ws.db.UserAuth;
import ioc.liturgical.ws.models.ws.db.UserContact;
import ioc.liturgical.ws.models.ws.db.UserHash;
import ioc.liturgical.ws.models.ws.db.UserStatistics;
import ioc.liturgical.ws.models.ws.db.ValueSchema;
import ioc.liturgical.ws.models.ws.forms.AuthorizationCreateForm;
import ioc.liturgical.ws.models.ws.forms.DomainCreateForm;
import ioc.liturgical.ws.models.ws.forms.LabelCreateForm;
import ioc.liturgical.ws.models.ws.forms.SelectionWidgetSchema;
import ioc.liturgical.ws.models.ws.forms.UserCreateForm;
import ioc.liturgical.ws.models.ws.forms.UserPasswordChangeForm;
import ioc.liturgical.ws.models.ws.forms.UserPasswordForm;
import ioc.liturgical.ws.app.ServiceProvider;
import ioc.liturgical.ws.constants.ENDPOINTS_ADMIN_API;
import ioc.liturgical.ws.constants.EXTERNAL_DB_SCHEMA_CLASSES;
import ioc.liturgical.ws.constants.Constants;
import ioc.liturgical.ws.constants.SYSTEM_MISC_LIBRARY_TOPICS;
import ioc.liturgical.ws.constants.HTTP_RESPONSE_CODES;
import ioc.liturgical.ws.constants.NEW_FORM_CLASSES_ADMIN_API;
import ioc.liturgical.ws.constants.RESTRICTION_FILTERS;
import ioc.liturgical.ws.constants.ROLES;
import ioc.liturgical.ws.constants.SCHEMA_CLASSES;
import ioc.liturgical.ws.constants.USER_TOPICS;
import ioc.liturgical.ws.constants.VERBS;
import ioc.liturgical.ws.managers.auth.UserStatus;
import ioc.liturgical.ws.managers.exceptions.DbException;
import net.ages.alwb.utils.core.auth.PasswordHasher;
import net.ages.alwb.utils.core.datastores.db.factory.DbConnectionFactory;
import net.ages.alwb.utils.core.datastores.db.h2.manager.H2ConnectionManager;
import net.ages.alwb.utils.core.datastores.json.exceptions.BadIdException;
import net.ages.alwb.utils.core.datastores.json.exceptions.MissingSchemaIdException;
import net.ages.alwb.utils.core.datastores.json.models.LTKVJsonObject;
import net.ages.alwb.utils.core.datastores.json.models.LTKVString;
import net.ages.alwb.utils.core.error.handling.ErrorUtils;
import net.ages.alwb.utils.core.id.managers.IdManager;

/**
 * 
 * @author mac002
 *
 */
public class InternalDbManager implements HighLevelDataStoreInterface {

	private static final Logger logger = LoggerFactory.getLogger(InternalDbManager.class);
	private boolean suppressAuth = false; // for debugging purposes, if true, causes authorized() to always return true
	private boolean prettyPrint = true;
	private String wsAdmin = "wsadmin";
	private boolean initialized = true;
	private Gson gson = new Gson();
	private String storename = null;
	private String tablename = null;
	private boolean deleteOldDb = false;
	public int getMaxInactiveMinutes() {
		return maxInactiveMinutes;
	}

	public void setMaxInactiveMinutes(int maxInactiveMinutes) {
		this.maxInactiveMinutes = maxInactiveMinutes;
	}
	private boolean deleteOldTableRows = false;
	private boolean createTestUsers = false;
	private String customerNumber = null;
	private int maxInactiveMinutes = 10;
	
	public H2ConnectionManager manager;
	
	public InternalDbManager(
			String storename
			, String tablename
			, boolean deleteOldDb
			, boolean deleteOldTableRows
			, boolean createTestUsers
			, String wsAdmin
			) {
 
		this.storename = storename;
		this.tablename = tablename;
		this.deleteOldDb = deleteOldDb;
		this.deleteOldTableRows = deleteOldTableRows;
		this.createTestUsers = createTestUsers;
		this.wsAdmin = wsAdmin;
		
		try {
			manager = 
					DbConnectionFactory.getH2Manager(
							storename
							, tablename
							, deleteOldDb
							, deleteOldTableRows
							);
		} catch (SQLException e) {
			ErrorUtils.report(logger, e);
		}
    	verify();
	}
		
	/**
	 * Get all docs whose id matches specified pattern
	 * @param pattern, e.g. users
	 * @return all matching docs
	 */
	public JsonObject getWhereLike(String pattern) {
		ResultJsonObjectArray result = new ResultJsonObjectArray(prettyPrint);
		result.setQuery(pattern.replaceAll("%", "*"));
		try {
			List<JsonObject> dbResults = filter(manager.queryForJsonWhereLike(pattern));
			result.setValueSchemas(getSchemas(dbResults, null));
			result.setResult(dbResults);
		} catch (SQLException e) {
			result.setStatusCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
			result.setStatusMessage(e.getMessage());
		}
		return result.toJsonObject();
	}
	
	public JsonObject getTopicsLike(String pattern) {
		ResultJsonObjectArray result = new ResultJsonObjectArray(prettyPrint);
		result.setQuery(pattern);
		try {
			result.setResult(
					manager.queryForJsonWhereIdRegEx(
							".*" 
					+ Constants.ID_SPLITTER
					+ pattern 
					+ Constants.ID_SPLITTER 
					+ ".*")
					);
		} catch (SQLException e) {
			result.setStatusCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
			result.setStatusMessage(e.getMessage());
		}
		return result.toJsonObject();
	}
	
	/**
	 * Get all docs whose id starts with specified pattern
	 * @param pattern, e.g. _users
	 * @return all matching docs
	 */
	public JsonObject getWhereStartsWith(String pattern) {
		ResultJsonObjectArray result = new ResultJsonObjectArray(prettyPrint);
		result.setQuery(pattern.replaceAll("%", "*"));
		try {
			List<JsonObject> dbResults = filter(manager.queryForJsonWhereStartsWith(pattern));
			result.setValueSchemas(getSchemas(dbResults, null));
			result.setResult(dbResults);
		} catch (SQLException e) {
			result.setStatusCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
			result.setStatusMessage(e.getMessage());
		}
		return result.toJsonObject();
	}
	
	/**
	 * Examines the list of JsonObjects.
	 * If the object contains a valueSchemaId,
	 * attempts to find the schema for it in the database.
	 * If found, it adds it to the map to be returned.
	 * @param list
	 * @param username - if not null, used to filter list of domains to ones authorized
	 * @return
	 */
	public Map<String, JsonObject> getSchemas(
			List<JsonObject> list
			, String username
			) {
		Map<String,JsonObject> result = new TreeMap<String,JsonObject>();
		for (JsonObject json : list) {
			if (json.has(Constants.VALUE_SCHEMA_ID)) {
				try {
					String id = json.get(Constants.VALUE_SCHEMA_ID).getAsString();
					JsonObject schema = getSchema(id);
					if (schema != null && ! result.containsKey(id)) {
						if (id.startsWith(AuthorizationCreateForm.class.getSimpleName())) {
							JsonObject schemaObject = schema.get("schema").getAsJsonObject();
							JsonObject propertiesObject = schemaObject.get("properties").getAsJsonObject();
							propertiesObject.add("username", getUserIdsSelectionWidgetSchema());
							propertiesObject.add("library", getDomainIdsSelectionWidgetSchema(username));
							schemaObject.add("properties", propertiesObject);
							schema.add("schema", schemaObject);
						}
						result.put(id, schema);
						logger.info("id: " + id + " schema: " + schema.toString());
					}
				} catch (Exception e) {
					ErrorUtils.report(logger, e);
				}
			} else {
				logger.info("missing schema id for " + json.toString());
			}
		}
		return result;
	}

	/**
	 * Get all docs whose id starts with specified pattern
	 * @param pattern, e.g. _users
	 * @return all matching docs
	 */
	public JsonObject getForIdStartsWith(String id) {
		ResultJsonObjectArray result = new ResultJsonObjectArray(prettyPrint);
		result.setQuery(id);
		try {
			List<JsonObject> dbResults = filter(manager.queryForJsonWhereStartsWith(id));
			result.setValueSchemas(getSchemas(dbResults, null));
			result.setResult(dbResults);
		} catch (SQLException e) {
			result.setStatusCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
			result.setStatusMessage(e.getMessage());
		}
		return result.toJsonObject();
	}
	
	
	private List<JsonObject> filter(List<JsonObject> list) {
		try {
			return list.stream() 
			.filter(obj -> ! obj.get("_id").getAsString().startsWith("users|hash"))	// filter out the user hashes
			.collect(Collectors.toList());
		} catch (Exception e) {
			ErrorUtils.report(logger, e);
		}
		return list;
	}

	/**
	 * Get doc whose id matches
	 * @param pattern, e.g. _users/{id}
	 * @return matching doc
	 */
	public JsonObject getForId(String id) {
		ResultJsonObjectArray result = new ResultJsonObjectArray(prettyPrint);
		result.setQuery(id);
		try {
			List<JsonObject> dbResults = manager.queryForJsonWhereEqual(id);
			result.setValueSchemas(getSchemas(dbResults, null));
			result.setResult(dbResults);
		} catch (SQLException e) {
			result.setStatusCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
			result.setStatusMessage(e.getMessage());
		}
		return result.toJsonObject();
	}
	
	public JsonObject getNewUserForm(String query) {
		ResultJsonObjectArray result = new ResultJsonObjectArray(prettyPrint);
		result.setQuery(query);
		try {
			UserCreateForm form = new UserCreateForm();
			LTKVJsonObject record = 
					new LTKVJsonObject(
						USER_TOPICS.NEW.lib
						, USER_TOPICS.NEW.topic
						, "user"
						, form.schemaIdAsString()
						, form.toJsonObject()
						);

			List<JsonObject> dbResults = new ArrayList<JsonObject>();
			dbResults.add(record.toJsonObject());
			result.setValueSchemas(getSchemas(dbResults, null));
			result.setResult(dbResults);
		} catch (Exception e) {
			result.setStatusCode(HTTP_RESPONSE_CODES.SERVER_ERROR.code);
			result.setStatusMessage(e.getMessage());
		}
		return result.toJsonObject();
	}

	private String userFromQuery(String query) {
		return query;
	}

	public LTKVJsonObject getUserPasswordChangeForm(
			String requestor
			, String username
			) {
		LTKVJsonObject record = null;
		try {
			UserPasswordChangeForm form = new UserPasswordChangeForm();
			form.setUsername(username);
			// if user whose password we are changing is the requestor
			// they do not have to change the password after login.
			form.setRequiresChangeAfterLogin(!requestor.equals(username));
			record = 
					new LTKVJsonObject(
							"users|password|" + username
						, form.schemaIdAsString()
						, form.toJsonObject()
						);
		} catch (Exception e) {
			ErrorUtils.report(logger, e);
		}
		return record;
	}

	public JsonObject getUserPasswordChangeFormObject(
			String requestor
			, String query
			) {
		ResultJsonObjectArray result = new ResultJsonObjectArray(prettyPrint);
		result.setQuery(query);
		List<JsonObject> dbResults = new ArrayList<JsonObject>();
		try {
			String username = userFromQuery(query);
			dbResults.add(getUserPasswordChangeForm(requestor,username).toJsonObject());
			result.setValueSchemas(getSchemas(dbResults, null));
			result.setResult(dbResults);
		} catch (Exception e) {
			result.setStatusCode(HTTP_RESPONSE_CODES.SERVER_ERROR.code);
			result.setStatusMessage(e.getMessage());
		}
		return result.toJsonObject();
	}

	public JsonObject getUserPasswordChangeForms(String query) {
		ResultJsonObjectArray result = new ResultJsonObjectArray(prettyPrint);
		result.setQuery(query);
		List<JsonObject> dbResults = new ArrayList<JsonObject>();
		try {
			for (String id : getUserIDsForPasswordChange()) {
			UserPasswordChangeForm form = new UserPasswordChangeForm();
			form.setUsername(id);
			LTKVJsonObject record = 
					new LTKVJsonObject(
							"users|password|" + id
						, form.schemaIdAsString()
						, form.toJsonObject()
						);

			dbResults.add(record.toJsonObject());
			}
			result.setValueSchemas(getSchemas(dbResults, null));
			result.setResult(dbResults);
		} catch (Exception e) {
			result.setStatusCode(HTTP_RESPONSE_CODES.SERVER_ERROR.code);
			result.setStatusMessage(e.getMessage());
		}
		return result.toJsonObject();
	}
	
	public JsonObject getUserPasswordChangeForms(String requestor, String query) {
		ResultJsonObjectArray result = new ResultJsonObjectArray(prettyPrint);
		result.setQuery(query);
		List<JsonObject> dbResults = new ArrayList<JsonObject>();
		try {
			for (String id : getUserIDsForPasswordChange()) {
				dbResults.add(getUserPasswordChangeForm(requestor,id).toJsonObject());
			}
			result.setValueSchemas(getSchemas(dbResults, null));
			result.setResult(dbResults);
		} catch (Exception e) {
			result.setStatusCode(HTTP_RESPONSE_CODES.SERVER_ERROR.code);
			result.setStatusMessage(e.getMessage());
		}
		return result.toJsonObject();
	}
	
	/**
	 * Get a list of the IDs of all users
	 * @return
	 */
	public List<String> getUserIds() {
		return getIds(USER_TOPICS.CONTACT.toId(""));
	}

	/**
	 * Get a list of the users with a path for changing their password
	 * These can be listed in the user interface for selection by
	 * an administrator.
	 * @return
	 */
	public List<String> getUserIDsForPasswordChange() {
		List<String> result = new ArrayList<String>();
		List<String> userIds = getUserIds();
		for (String id: userIds) {
			try {
				String [] parts = id.split(Constants.ID_SPLITTER);
				result.add(parts[2]);
			} catch (Exception e) {
				ErrorUtils.report(logger, e);
			}
		}
		return result;
	}


	private List<String> getDomainIds() {
		return getIds(SYSTEM_MISC_LIBRARY_TOPICS.DOMAINS.toId(""));
	}

	private JsonObject getUserIdsSelectionWidgetSchema() {
		return getIdsAsSelectionWidgetSchema("Users", USER_TOPICS.CONTACT.toId(""), false, true);
	}

	/**
	 * Provides a list of all domains found in the database without filtering based on user's admin auth
	 * @return
	 */
	private JsonObject getDomainIdsSelectionWidgetSchema() {
		return getIdsAsSelectionWidgetSchema("Domains", SYSTEM_MISC_LIBRARY_TOPICS.DOMAINS.toId(""), false, true);
	}

	/**
	 * Provides a list of Domains for which the user has admin authority,
	 * set up as a schema widget
	 * @param username
	 * @return
	 */
	private JsonObject getDomainIdsSelectionWidgetSchema(String username) {
		JsonObject result = new JsonObject();

		List<String> idsList = new ArrayList<String>();
		
		if (isWsAdmin(username)) { // add in the web_service as a library and all_domains as a library
			idsList.add(Constants.ID_DELIMITER+Constants.SYSTEM_LIB+Constants.ID_DELIMITER);
			idsList.add(Constants.ID_DELIMITER+Constants.DOMAINS_LIB+Constants.ID_DELIMITER);
		} else if (isDbAdmin(username)) { // add in all_domains as a library
			idsList.add(Constants.ID_DELIMITER+Constants.DOMAINS_LIB+Constants.ID_DELIMITER);
		}

		if (isDbAdmin(username)) { // get all domains
			idsList.addAll(getIds(SYSTEM_MISC_LIBRARY_TOPICS.DOMAINS.toId("")));
		} else { // only get domains for which the user is authorized to be an administrator
			idsList.addAll(getIds(ROLES.ADMIN.keyname + "%" + username));
		}
		
		Collections.sort(idsList);
		
		String[] idsArray = new String[idsList.size()];
		String[] labelsArray = new String[idsList.size()];
		
		for (int i=0; i < idsList.size(); i++) {
			String x = "";
			IdManager idManager = new IdManager(idsList.get(i));
			if (idManager.get(0).startsWith(SYSTEM_MISC_LIBRARY_TOPICS.DOMAINS.lib)) {
				idsArray[i] = idManager.get(2);
				labelsArray[i] = idManager.get(2);
			} else {
				idsArray[i] = idManager.get(1);
				labelsArray[i] = "* " + idManager.get(1);
			}
		}
		SelectionWidgetSchema widget = new SelectionWidgetSchema(
				"Domains"
				, idsArray
				, labelsArray
				);
		JsonParser parser = new JsonParser();
		result = parser.parse(widget.toWidgetJsonString()).getAsJsonObject();
		return result;
	}

	private JsonObject getIdsAsSelectionWidgetSchema(
			String title
			, String like
			, boolean keyOnlyForValue
			, boolean keyOnlyForLabel
			) {
		List<String> idsList = getIds(like);
		String[] idsArray = new String[idsList.size()];
		String[] labelsArray = new String[idsList.size()];
		idsArray = idsList.toArray(idsArray);
		labelsArray = idsArray;
		
		if (keyOnlyForValue) {
			for (int i=0; i < idsList.size(); i++) {
				IdManager idManager = new IdManager(idsArray[i]);
				idsArray[i] = idManager.get(2);
			}
		}
		if (keyOnlyForValue && keyOnlyForLabel) {
			labelsArray = idsArray;
		} else if (keyOnlyForLabel) {
			for (int i=0; i < idsList.size(); i++) {
				IdManager idManager = new IdManager(labelsArray[i]);
				labelsArray[i] = idManager.get(2);
			}
		}
		
		SelectionWidgetSchema widget = new SelectionWidgetSchema(
				title
				, idsArray
				, labelsArray
				);
		JsonParser parser = new JsonParser();
		JsonObject json = parser.parse(widget.toWidgetJsonString()).getAsJsonObject();
		return json;
	}
	
	private List<String> getIds(String like) {
		List<String> list = new ArrayList<String>();
		for (JsonElement user : getWhereLike(like).get("values").getAsJsonArray()) {
			list.add(user.getAsJsonObject().get("_id").getAsString());
		}
		return list;
	}

	/**
	 * 
	 * @param requestor
	 * @param restriction
	 * @return
	 */
	private boolean userAuthorizedForThisForm(
			String requestor
			, RESTRICTION_FILTERS restriction
			) {
		boolean result = false;
			switch (restriction) {
			case ALL_DOMAINS_ADMIN:
				result = isDbAdmin(requestor);
				break;
			case DOMAIN_ADMIN:
				result = isAdminForAnyLib(requestor);
				break;
			case NONE:
				result = true;
				break;
			case WS_ADMIN:
				result = isWsAdmin(requestor);
				break;
			default:
				result = false;
				break;
			}
		return result;
	}
	public JsonObject getNewDocForms(String requestor, String query) {
		ResultJsonObjectArray result = new ResultJsonObjectArray(prettyPrint);
		result.setQuery(query);
		List<JsonObject> dbResults = new ArrayList<JsonObject>();
		try {
			for (NEW_FORM_CLASSES_ADMIN_API e : NEW_FORM_CLASSES_ADMIN_API.values()) {
				if (userAuthorizedForThisForm(requestor, e.restriction)) {
					LTKVJsonObject record = 
							new LTKVJsonObject(
								e.endpoint.library
								, "new"
								, e.name
								, e.obj.schemaIdAsString()
								, e.obj.toJsonObject()
								);
				dbResults.add(record.toJsonObject());
				}
			}
			result.setValueSchemas(getSchemas(dbResults, requestor));
			result.setResult(dbResults);
		} catch (Exception e) {
			result.setStatusCode(HTTP_RESPONSE_CODES.SERVER_ERROR.code);
			result.setStatusMessage(e.getMessage());
		}
		return result.toJsonObject();
	}

	private void verify() {
		try {
	       	// create the table.  Will only happen if doesn't already exist.
        	manager.createTable();
        	
        	if (deleteOldTableRows) {
            	manager.truncateTable();
        	}
        	
        	// check to see if table has values
           	List<JsonObject> jsonList = manager.queryForJson();  
           	if (jsonList.isEmpty()) {
           		initializeTable(); // will also add schemas
           	} else {
    			addSchemas(); // this adds the schemas used by the system
           	}
		} catch (Exception e) {
			ErrorUtils.report(logger, e);
		}
	}
	
	
	private void initializeUser(
			String username
			, String firstname
			, String lastname
			, ROLES role
			, String lib
			) throws BadIdException {
		UserContact userContact = new UserContact();
		userContact.setFirstname(firstname);
		userContact.setLastname(lastname);
		userContact.setEmail(username + "@ioc-liturgical-ws.org");
		addUserContact(username, userContact);
		addUserHash(wsAdmin,username, customerNumber);
		UserStatistics stats = new UserStatistics();
		addUserStats(username,stats);
		if (role != null) {
			grantRole(wsAdmin,role, lib, username);
		}
	}
	private void initializeTable() {
		logger.info("Initializing table " + tablename + " for database " + storename);
		initialized = false;
		
		/***
		 * TODO: change authentication to use /users/hash/username
		 */
		try {
			addRoles();
   			addSchemas(); 
   			
			// add the ws admin user
			UserCreateForm user = new UserCreateForm();
			user.setFirstname("IOC Liturgical");
			user.setLastname("Web Services Admin");
			user.setEmail("admin@ioc-liturgical-db.org");
			user.setPassword(ServiceProvider.ws_pwd);
			user.setEmailReenter(user.getEmail());
			user.setPasswordReenter(user.getPassword());
			user.setUsername(wsAdmin);
			addUser(user.toJsonString());

			logger.info("ws admin user added");
			bootstrapSystemAdminRole(wsAdmin,ROLES.ADMIN, Constants.SYSTEM_LIB, wsAdmin);
			logger.info("system admin role added");
			UserStatistics stats = new UserStatistics();
			addUserStats(user.getUsername(),stats);

			// TODO delete the following

			DomainCreateForm domain = new DomainCreateForm();
			domain.setLanguageCode("gr");
			domain.setCountryCode("gr");
			domain.setRealm("cog");
			domain.setDescription("Commonly used Orthodox Greek text");
			List<String> labels = new ArrayList<String>();
			labels.add("Liturgical");
			domain.setLabels(labels);
			addDomain(wsAdmin, domain.toJsonString());

			domain = new DomainCreateForm();
			domain.setLanguageCode("en");
			domain.setCountryCode("us");
			domain.setRealm("dedes");
			domain.setDescription("Translations by Fr. Seraphim Dedes");
			domain.setLabels(labels);
			addDomain(wsAdmin, domain.toJsonString());
			logger.info("domains added");
			
			if (createTestUsers) {
				// add a test user who can administer all domains
				initializeUser(
						"adminForAllDomains"
						, "all"
						, "Domains"
						, ROLES.ADMIN
						, Constants.DOMAINS_LIB
						);
	
				// add a test user who can administer one domain
				initializeUser(
						"adminForEnUsDedes"
						, "one"
						, "Domain"
						, ROLES.ADMIN
						, "en_us_dedes"
						);
	
				initializeUser(
						"notAnAdmin"
						, "can't"
						, "DoAnything"
						, null
						, null
						);
				logger.info("Test users added");
			}
		} catch (Exception e) {
			ErrorUtils.report(logger, e);
		}

		logger.info("The database has been initialized.");
		logger.info(
				"The web services administrator user has been created: " 
				+ wsAdmin
		);
		logger.info(
				"The password for " 
				+ wsAdmin 
				+ " is the one you passed in as a parameter when you started the jar."
		);
		logger.info("Full rights have been granted to: " + ROLES.ADMIN.keyname + " on " + Constants.SYSTEM_LIB);
		logger.info("The user " + wsAdmin + " has been granted the role of " + ROLES.ADMIN.keyname + " on " + Constants.SYSTEM_LIB);
		logger.info("Use this first user and first role to add more users and more rights.");
		
		initialized = true;
	}

	public boolean isWildcardLib(String library) {
		if (isSystemWildcard(library) || isDomainsWildcard(library)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isSystemWildcard(String library) {
		if (library.startsWith(Constants.SYSTEM_LIB) && library.endsWith(Constants.SYSTEM_LIB)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isDomainsWildcard(String library) {
		if (library.startsWith(Constants.DOMAINS_LIB) && library.endsWith(Constants.DOMAINS_LIB)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 
	 * @param library
	 * @return true if is a wildcard library or a domain library
	 */
	public boolean existsLibrary(String library) {
		if (isWildcardLib(library)) {
			return true;
		} else {
			return isDomainLibrary(library);
		}
	}
	
	public boolean isDomainLibrary(String library) {
		JsonObject json = getForId(SYSTEM_MISC_LIBRARY_TOPICS.DOMAINS.toId(library));
		return json.get("valueCount").getAsInt() > 0;
	}

	/**
	 * Checks to see if the user has a UserContact record
	 * @param user
	 * @return
	 */
	public boolean existsUser(String user) {	
		try {
			JsonObject json = getForId(USER_TOPICS.CONTACT.toId(user));
			return json.get("valueCount").getAsInt() > 0;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean authorizedToGrantRole(
			String requestor
			, ROLES role
			, String library
			) {
		boolean authorized = false;
		if (isWsAdmin(requestor)) {
			authorized = true;
		} else if (isDbAdmin(requestor) &&  isDomainLibrary(library)){
			authorized = true;
		} else if (isLibAdmin(library, requestor)) {
			authorized = true;
		}
		return authorized;
	}
	
	
	public RequestStatus grantRole(
			String requestor
			, ROLES role
			, String library
			, String user
			) {
		RequestStatus result = new RequestStatus();
		if (authorizedToGrantRole(requestor,role,library)) {
			if (existsUser(user)) {
				if (existsLibrary(library)) {
					UserAuth auth = new UserAuth();
					auth.setGrantedBy(requestor);
					auth.setGrantedWhen(getTimestamp());
					String description = " For the library " + library + ", is " + role.description;
					if (role.equals(ROLES.ADMIN)) {
						if ( isSystemWildcard(library)) {
							description = "The user has administrative authorization for the entire web service system and databases it protects.";
						} else if (isDomainsWildcard(library)) {
							description = "The user has administrative authorization for all domain libraries.";
						}
					}
					auth.setDescription(description);
					try {
						result = addLTKVJsonObject(
								role.keyname
								, library
								, user
								, auth.schemaIdAsString()
								,auth.toJsonObject()
								);
					} catch (MissingSchemaIdException e) {
						ErrorUtils.report(logger, e);
						result.setCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
						result.setMessage(HTTP_RESPONSE_CODES.BAD_REQUEST.message);
					} catch (Exception e) {
						result.setCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
						result.setMessage(HTTP_RESPONSE_CODES.BAD_REQUEST.message);
					}
				} else {
					result.setCode(HTTP_RESPONSE_CODES.LIBRARY_DOES_NOT_EXIST.code);
					result.setMessage(HTTP_RESPONSE_CODES.LIBRARY_DOES_NOT_EXIST.message);
				}
			} else {
				result.setCode(HTTP_RESPONSE_CODES.USER_DOES_NOT_EXIST.code);
				result.setMessage(HTTP_RESPONSE_CODES.USER_DOES_NOT_EXIST.message);
			}
		} else {
			result.setCode(HTTP_RESPONSE_CODES.FORBIDDEN.code);
			result.setMessage(HTTP_RESPONSE_CODES.FORBIDDEN.message);
		}
    	return result;
	}

	/**
	 * The public grant method checks to see if the user is authorized.
	 * This bootstrap method is private and only used to initialize the
	 * authority of the web service root user account.
	 * @param requestor
	 * @param role
	 * @param library
	 * @param user
	 * @return
	 */
	private RequestStatus bootstrapSystemAdminRole(
			String requestor
			, ROLES role
			, String library
			, String user
			) {
		RequestStatus result = new RequestStatus();
		UserAuth auth = new UserAuth();
		auth.setGrantedBy(requestor);
		auth.setGrantedWhen(getTimestamp());
		String description = "The user has administrative authorization for the entire web service system and databases it protects.";
		auth.setDescription(description);
			try {
				result = addLTKVJsonObject(
						role.keyname
						, library
						, user
						, auth.schemaIdAsString()
						,auth.toJsonObject()
						);
			} catch (Exception e) {
				ErrorUtils.report(logger, e);
			}
		return result;
	}

		/**
	 * TODO Do we need this?
	 * @param role
	 * @param user
	 */
	private RequestStatus revokeRole(ROLES role, String user) {
		RequestStatus result = new RequestStatus();
		LTKVString tkv;
		try {
			tkv = new LTKVString(
					new IdManager(
							"rights"
							, role.keyname
							, user
							).getId()
					, role.description
					);
			manager.delete(tkv.toJsonObject());
		} catch (Exception e) {
			result = new RequestStatus(HTTP_RESPONSE_CODES.BAD_REQUEST);
		}
		return result;
	}

	/**
	 * 
	 * This method will create an instance of
	 * UserContact and UserHash
	 * 
	 * @param json from a UserCreateForm
	 * @return
	 */
	public RequestStatus addUser(String json) {
	RequestStatus result = new RequestStatus();
	UserCreateForm userForm = new UserCreateForm();
	userForm = (UserCreateForm) userForm.fromJsonString(json);
	String validation = userForm.validate(json);
	if (validation.length() == 0) {
		try {
			// first create a UserContact
			UserContact user = new UserContact();
			user.setFirstname(userForm.getFirstname());
			user.setLastname(userForm.getLastname());
			user.setEmail(userForm.getEmail());
			result = addUserContact(userForm.getUsername(), user);
			
			if (result.getCode() == HTTP_RESPONSE_CODES.CREATED.code) {
				// now create a UserHash
				UserHash hash = new UserHash();
				hash.setPassword(userForm.getPassword());
				// we will overwrite the original response code
				result = addUserHash(userForm.getUsername(), hash);
			}
		} catch (Exception e) {
			result.setCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
			result.setMessage(HTTP_RESPONSE_CODES.BAD_REQUEST.message);
		}
	} else {
		result.setCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
		JsonObject message = stringToJson(validation);
		if (message == null) {
			result.setMessage(validation);
		} else {
			result.setMessage(message.get("message").getAsString());
		}
	}
	return result;
}

	/**
	 * 
	 * This method will update an instance of UserHash
	 * 
	 * @param json from a UserChangePasswordForm
	 * @return
	 */
	public RequestStatus updateUserPassword(String query, String json) {
	RequestStatus result = new RequestStatus();
	UserPasswordChangeForm userForm = new UserPasswordChangeForm();
	try {
		userForm = (UserPasswordChangeForm) userForm.fromJsonString(json);
		String validation = userForm.validate(json);
		if (validation.length() == 0) {
			try {
					// create a UserHash
					UserHash hash = new UserHash();
					hash.setPassword(userForm.getPassword());
					// we will overwrite the original response code
					result = updateUserHash(userForm.getUsername(), hash);
			} catch (Exception e) {
				result.setCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
				result.setMessage(HTTP_RESPONSE_CODES.BAD_REQUEST.message);
			}
		} else {
			result.setCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
			JsonObject message = stringToJson(validation);
			if (message == null) {
				result.setMessage(validation);
			} else {
				result.setMessage(message.get("message").getAsString());
			}
		}
	} catch (Exception e) {
		result.setCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
		result.setMessage(HTTP_RESPONSE_CODES.BAD_REQUEST.message);
	}
	return result;
}

	/**
	 * Adds a new domain
	 * @param requestor - username of the requestor
	 * @param json - json string representation of a new domain form
	 * @return the status of the request
	 */
	public RequestStatus addDomain(String requestor, String json) {
		RequestStatus result = new RequestStatus();
		DomainCreateForm form = new DomainCreateForm();
		form = (DomainCreateForm) form.fromJsonString(json);
		String validation = form.validate(json);
		if (validation.length() == 0) {
			try {
				Domain domain = new Domain();
				domain.setDescription(form.getDescription());
				domain.setLabels(form.getLabels());
				domain.setPublic(form.isPublic());
				domain.setCreatedBy(requestor);
				domain.setModifiedBy(requestor);
				domain.setCreatedWhen(getTimestamp());
				domain.setModifiedWhen(domain.getCreatedWhen());
				String key = Joiner.on("_").join(form.getLanguageCode(), form.getCountryCode(), form.getRealm());
				result = addLTKVJsonObject(
						SYSTEM_MISC_LIBRARY_TOPICS.DOMAINS.lib
						, SYSTEM_MISC_LIBRARY_TOPICS.DOMAINS.topic
						, key
						, domain.schemaIdAsString()
						, domain.toJsonObject()
						);
			} catch (Exception e) {
				result.setCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
				result.setMessage(HTTP_RESPONSE_CODES.BAD_REQUEST.message);
			}
		} else {
			result.setCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
			JsonObject message = stringToJson(validation);
			if (message == null) {
				result.setMessage(validation);
			} else {
				result.setMessage(message.get("message").getAsString());
			}
		}
		return result;
	}

	public RequestStatus addLabel(String requestor, String json) {
		RequestStatus result = new RequestStatus();
		LabelCreateForm form = new LabelCreateForm();
		form = (LabelCreateForm) form.fromJsonString(json);
		String validation = form.validate(json);
		if (validation.length() == 0) {
			try {
				Label label = new Label();
				label.setLabel(form.getLabel());
				label.setTitle(form.getTitle());
				label.setDescription(form.getDescription());
				label.setCreatedBy(requestor);
				label.setModifiedBy(requestor);
				label.setCreatedWhen(getTimestamp());
				label.setModifiedWhen(label.getCreatedWhen());
				result = addLTKVJsonObject(
						SYSTEM_MISC_LIBRARY_TOPICS.LABELS.lib
						, SYSTEM_MISC_LIBRARY_TOPICS.LABELS.topic
						, label.getLabel()
						, label.schemaIdAsString()
						, label.toJsonObject()
						);
			} catch (Exception e) {
				result.setCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
				result.setMessage(HTTP_RESPONSE_CODES.BAD_REQUEST.message);
			}
		} else {
			result.setCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
			JsonObject message = stringToJson(validation);
			if (message == null) {
				result.setMessage(validation);
			} else {
				result.setMessage(message.get("message").getAsString());
			}
		}
		return result;
	}


	public RequestStatus addAuthorization(String requestor, String json) {
		RequestStatus result = new RequestStatus();
		AuthorizationCreateForm form = new AuthorizationCreateForm();
		form = (AuthorizationCreateForm) form.fromJsonString(json);
		String validation = form.validate(json);
		if (validation.length() == 0) {
			try {
				ROLES role = ROLES.forWsname(form.getRole()+"s");
				result = grantRole(requestor, role, form.getLibrary(), form.getUsername());
			} catch (Exception e) {
				result.setCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
				result.setMessage(HTTP_RESPONSE_CODES.BAD_REQUEST.message);
			}
		} else {
			result.setCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
			JsonObject message = stringToJson(validation);
			if (message == null) {
				result.setMessage(validation);
			} else {
				result.setMessage(message.get("message").getAsString());
			}
		}
		return result;
	}

	private JsonObject stringToJson(String s) {
		try {
			return new JsonParser().parse(s).getAsJsonObject();
		} catch (Exception e) {
			ErrorUtils.report(logger, e);
		}
		return null;
	}
	
	/**
	 * Add a user based on a json string.
	 * @param key username for the user
	 * @param json string of UserContact properties
	 * @return
	 */
	public RequestStatus addUserContact(String key, String json) {
		RequestStatus result = new RequestStatus();
		UserContact user = new UserContact();
		user = (UserContact) user.fromJsonString(json);
		String validation = user.validate(json);
		if (validation.length() == 0) {
			try {
				result = addUserContact(key, user);
			} catch (Exception e) {
				result.setCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
				result.setMessage(HTTP_RESPONSE_CODES.BAD_REQUEST.message);
			}
		} else {
			result.setCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
			result.setMessage(validation);
		}
		return result;
	}

	public RequestStatus addUserContact(
			String key
			, UserContact user
			) throws BadIdException {
		RequestStatus result = new RequestStatus();
		try {
			result = addLTKVJsonObject(
					USER_TOPICS.CONTACT.lib
					, USER_TOPICS.CONTACT.topic
					, key
					, user.schemaIdAsString()
					,user.toJsonObject()
					);
			UserStatistics stats = new UserStatistics();
			addUserStats(key,stats);
		} catch (MissingSchemaIdException e) {
			ErrorUtils.report(logger, e);
			result.setCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
			result.setMessage(HTTP_RESPONSE_CODES.BAD_REQUEST.message);
		} catch (Exception e) {
			result.setCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
			result.setMessage(HTTP_RESPONSE_CODES.BAD_REQUEST.message);
		}
    	return result;
    }

	public void addUserHash(
			String requestor
			, String key
			, String hashedPassword
			) throws BadIdException {
		UserHash userHash = new UserHash();
		userHash.setHashedPassword(hashedPassword);
		addUserHash(key, userHash);
    }
	
	public RequestStatus addUserHash(
			String key
			, UserHash userHash
			) throws BadIdException {
		RequestStatus result = new RequestStatus();
		try {
			result = addLTKVJsonObject(
					USER_TOPICS.HASH.lib
					, USER_TOPICS.HASH.topic
					, key
					, userHash.schemaIdAsString()
					,userHash.toJsonObject()
					);
		} catch (MissingSchemaIdException e) {
			ErrorUtils.report(logger, e);
			result.setCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
			result.setMessage(HTTP_RESPONSE_CODES.BAD_REQUEST.message);
		} catch (Exception e) {
			result.setCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
			result.setMessage(HTTP_RESPONSE_CODES.BAD_REQUEST.message);
		}
    	return result;
    }

	public RequestStatus updateUserHash(
			String key
			, UserHash userHash
			) throws BadIdException {
		RequestStatus result = new RequestStatus();
		try {
			result = updateLTKVJsonObject(
					USER_TOPICS.HASH.lib
					, USER_TOPICS.HASH.topic
					, key
					, userHash.schemaIdAsString()
					,userHash.toJsonObject()
					);
		} catch (MissingSchemaIdException e) {
			ErrorUtils.report(logger, e);
			result.setCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
			result.setMessage(HTTP_RESPONSE_CODES.BAD_REQUEST.message);
		} catch (Exception e) {
			result.setCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
			result.setMessage(HTTP_RESPONSE_CODES.BAD_REQUEST.message);
		}
    	return result;
    }

	public RequestStatus addUserStats(
			String key
			, UserStatistics userStats
			) throws BadIdException {
		RequestStatus result = new RequestStatus();
		try {
			result = addLTKVJsonObject(
					USER_TOPICS.STATISTICS.lib
					, USER_TOPICS.STATISTICS.topic
					, key
					, userStats.schemaIdAsString()
					,userStats.toJsonObject()
					);
		} catch (MissingSchemaIdException e) {
			ErrorUtils.report(logger, e);
			result.setCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
			result.setMessage(HTTP_RESPONSE_CODES.BAD_REQUEST.message);
		} catch (Exception e) {
			result.setCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
			result.setMessage(HTTP_RESPONSE_CODES.BAD_REQUEST.message);
		}
    	return result;
    }
	
	public RequestStatus updateDomain(String requestor, String key, String json) {
		RequestStatus result = new RequestStatus();
		Domain obj = new Domain();
		String validation = obj.validate(json);
		if (validation.length() == 0) {
			try {
				obj = (Domain) obj.fromJsonString(json);
				obj.setModifiedBy(requestor);
				obj.setModifiedWhen(getTimestamp());
				result = updateDomain(key, obj);
			} catch (Exception e) {
				result.setCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
				result.setMessage(HTTP_RESPONSE_CODES.BAD_REQUEST.message);
			}
		} else {
			result.setCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
			result.setMessage(validation);
		}
		return result;
	}
	
	private RequestStatus updateDomain(String key, Domain obj) {
		RequestStatus result = new RequestStatus();
		try {
	    	result = updateLTKVJsonObject(
	    			SYSTEM_MISC_LIBRARY_TOPICS.DOMAINS.lib
	    			, SYSTEM_MISC_LIBRARY_TOPICS.DOMAINS.topic
	    			, key
	    			, obj.schemaIdAsString()
	    			, obj.toJsonObject()
	    			);
		} catch (MissingSchemaIdException e) {
			ErrorUtils.report(logger, e);
			result.setCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
			result.setMessage(HTTP_RESPONSE_CODES.BAD_REQUEST.message);
		} catch (Exception e) {
			result.setCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
			result.setMessage(e.getMessage());
		}
		return result;

	}

	public RequestStatus updateLabel(String requestor, String key, String json) {
		RequestStatus result = new RequestStatus();
		Label obj = new Label();
		String validation = obj.validate(json);
		if (validation.length() == 0) {
			try {
				obj = (Label) obj.fromJsonString(json);
				obj.setModifiedBy(requestor);
				obj.setModifiedWhen(getTimestamp());
				result = updateLabel(key, obj);
			} catch (Exception e) {
				result.setCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
				result.setMessage(HTTP_RESPONSE_CODES.BAD_REQUEST.message);
			}
		} else {
			result.setCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
			result.setMessage(validation);
		}
		return result;
	}
	
	private RequestStatus updateLabel(String key, Label obj) {
		RequestStatus result = new RequestStatus();
		try {
	    	result = updateLTKVJsonObject(
	    			SYSTEM_MISC_LIBRARY_TOPICS.LABELS.lib
	    			, SYSTEM_MISC_LIBRARY_TOPICS.LABELS.topic
	    			, key
	    			, obj.schemaIdAsString()
	    			, obj.toJsonObject()
	    			);
		} catch (MissingSchemaIdException e) {
			ErrorUtils.report(logger, e);
			result.setCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
			result.setMessage(HTTP_RESPONSE_CODES.BAD_REQUEST.message);
		} catch (Exception e) {
			result.setCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
			result.setMessage(e.getMessage());
		}
		return result;

	}


	public RequestStatus updateUserContact(String key, String json) {
		RequestStatus result = new RequestStatus();
		UserContact user = new UserContact();
		String validation = user.validate(json);
		if (validation.length() == 0) {
			try {
				user = (UserContact) user.fromJsonString(json);
				result = updateUserContact(key, user);
			} catch (Exception e) {
				result.setCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
				result.setMessage(HTTP_RESPONSE_CODES.BAD_REQUEST.message);
			}
		} else {
			result.setCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
			result.setMessage(validation);
		}
		return result;
	}
	
	private RequestStatus updateUserContact(String key, UserContact user) {
		RequestStatus result = new RequestStatus();
		try {
	    	result = updateLTKVJsonObject(
	    			USER_TOPICS.CONTACT.lib
	    			, USER_TOPICS.CONTACT.topic
	    			, key
	    			, user.schemaIdAsString()
	    			, user.toJsonObject()
	    			);
		} catch (MissingSchemaIdException e) {
			ErrorUtils.report(logger, e);
			result.setCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
			result.setMessage(HTTP_RESPONSE_CODES.BAD_REQUEST.message);
		} catch (Exception e) {
			result.setCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
			result.setMessage(e.getMessage());
		}
		return result;

	}
	
	private RequestStatus updateUserStats(String key, UserStatistics user) {
		RequestStatus result = new RequestStatus();
		try {
	    	result = updateLTKVJsonObject(
	    			USER_TOPICS.STATISTICS.lib
	    			, USER_TOPICS.STATISTICS.topic
	    			, key
	    			, user.schemaIdAsString()
	    			, user.toJsonObject()
	    			);
		} catch (MissingSchemaIdException e) {
			ErrorUtils.report(logger, e);
			result.setCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
			result.setMessage(HTTP_RESPONSE_CODES.BAD_REQUEST.message);
		} catch (Exception e) {
			result.setCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
			result.setMessage(e.getMessage());
		}
		return result;

	}

	/**
	 * Initializes schemas for both the internal and external databases
	 */
	private void addSchemas() {
		try {
			for (SCHEMA_CLASSES s :SCHEMA_CLASSES.values()) {
				try {
					ValueSchema schema = new ValueSchema(s.obj);
					String id = new IdManager(
							SYSTEM_MISC_LIBRARY_TOPICS.SCHEMAS.lib
							, SYSTEM_MISC_LIBRARY_TOPICS.SCHEMAS.topic
							, s.obj.schemaIdAsString()
							).getId();
					if (existsUnique(id)) {
						updateSchema(s.obj.schemaIdAsString(), schema.toJsonObject());
					} else {
						addSchema(s.obj.schemaIdAsString(), schema.toJsonObject());
					}
				} catch (Exception e) {
					ErrorUtils.report(logger, e);
				}
			}
			for (EXTERNAL_DB_SCHEMA_CLASSES s : EXTERNAL_DB_SCHEMA_CLASSES.values()) {
				try {
					ValueSchema schema = new ValueSchema(s.ltk);
					String id = new IdManager(
							SYSTEM_MISC_LIBRARY_TOPICS.SCHEMAS.lib
							, SYSTEM_MISC_LIBRARY_TOPICS.SCHEMAS.topic
							, s.ltk.schemaIdAsString()
							).getId();
					if (existsUnique(id)) {
						updateSchema(s.ltk.schemaIdAsString(), schema.toJsonObject());
					} else {
						addSchema(s.ltk.schemaIdAsString(), schema.toJsonObject());
					}
					schema = new ValueSchema(s.ltkDb);
					id = new IdManager(
							SYSTEM_MISC_LIBRARY_TOPICS.SCHEMAS.lib
							, SYSTEM_MISC_LIBRARY_TOPICS.SCHEMAS.topic
							, s.ltkDb.schemaIdAsString()
							).getId();
					if (existsUnique(id)) {
						updateSchema(s.ltkDb.schemaIdAsString(), schema.toJsonObject());
					} else {
						addSchema(s.ltkDb.schemaIdAsString(), schema.toJsonObject());
					}
				} catch (Exception e) {
					ErrorUtils.report(logger, e);
				}
			}
			logger.info("Schemas added");
		} catch (Exception e) {
			ErrorUtils.report(logger, e);
		}
	}
	
	private void addRoles() throws BadIdException, SQLException {
		for (ROLES r : ROLES.values()) {
			addTKVString(Constants.SYSTEM_LIB,"roles", r.keyname, r.description);
		}
		logger.info("Roles added");
	}
	
	/**
	 * Add a doc whose value is a string and has an _id made of topic and key
	 * @param topic
	 * @param key
	 * @param value
	 * @throws BadIdException 
	 * @throws SQLException 
	 */
	private RequestStatus addTKVString(String library, String topic, String key, String value) throws BadIdException, SQLException {
		RequestStatus result = new RequestStatus();
		String id = new IdManager(library,topic,key).getId();
		if (existsUnique(id)) {
			result.setCode(HTTP_RESPONSE_CODES.CONFLICT.code);
			result.setMessage(HTTP_RESPONSE_CODES.CONFLICT.message + ": " + id);
		} else {
			LTKVString tkv;
			tkv = new LTKVString(
					library
					, topic
					, key
					, value
					);
	    	manager.insert(tkv.toJsonObject());		
	    	result.setCode(HTTP_RESPONSE_CODES.CREATED.code);
	    	result.setMessage(HTTP_RESPONSE_CODES.CREATED.message + ": " + id);
		}
		return result;
	}
	
	/**
	 * Add a doc whose value is a JsonObject and whose _id is a library|topic|key
	 * @param library
	 * @param topic
	 * @param key
	 * @param json
	 * @param MissingSchemaIdException
	 * @throws SQLException 
	 * @throws BadIdException 
	 */
	public RequestStatus addLTKVJsonObject(
			String library
			, String topic
			, String key
			, String schemaId
			, JsonObject json
			) throws DbException, MissingSchemaIdException, BadIdException {
		RequestStatus result = new RequestStatus();
		if (existsSchema(schemaId)) {
			String id = new IdManager(library,topic,key).getId();
			if (existsUnique(id)) {
				result.setCode(HTTP_RESPONSE_CODES.CONFLICT.code);
				result.setMessage(HTTP_RESPONSE_CODES.CONFLICT.message + ": " + id);
			} else {
				LTKVJsonObject record = 
						new LTKVJsonObject(
							library
							, topic
							, key
							, schemaId
							, json
							);
				   try {
				    	manager.insert(record.toJsonObject());		
				   } catch (SQLException e) {
					   throw new DbException(
							   "Error adding " 
							   + library
							   +":" + topic 
							   +":" + key 
							   , e
							   );
				   }
			    	result.setCode(HTTP_RESPONSE_CODES.CREATED.code);
			    	result.setMessage(HTTP_RESPONSE_CODES.CREATED.message + ": " + id);
			}
		} else {
			throw new MissingSchemaIdException(schemaId);
		}
		return result;
	}

	/**
	 * Does this schemaId exist in the database?
	 * @param schemaId
	 * @return
	 */
	public boolean existsSchema(String schemaId) {
		return existsUnique(SYSTEM_MISC_LIBRARY_TOPICS.SCHEMAS.toId(schemaId));
	}
	
	public JsonObject getSchema(String key) {
		JsonObject result = null;
		try {
			List<JsonObject> schemas = manager.queryForJsonWhereEqual(SYSTEM_MISC_LIBRARY_TOPICS.SCHEMAS.toId(key));
			if (schemas.size() > 0) {
				result = manager.queryForJsonWhereEqual(SYSTEM_MISC_LIBRARY_TOPICS.SCHEMAS.toId(key)).get(0);
			}
			if (result != null) {
				result = result.get("value").getAsJsonObject();
			}
		} catch (Exception e) {
			ErrorUtils.report(logger, e);
			return null;
		}
		return result;
	}

	public RequestStatus updateLTKVJsonObject(
			String library
			, String topic
			, String key
			, String schemaId
			, JsonObject json
			) throws BadIdException, MissingSchemaIdException, DbException {
		RequestStatus result = new RequestStatus();
		if (existsSchema(schemaId)) {
			String id = new IdManager(library,topic,key).getId();
			if (existsUnique(id)) {
				LTKVJsonObject record;
				record = new LTKVJsonObject(
						library
						, topic
						, key
						, schemaId
						, json
						);
				   try {
						manager.updateWhereEqual(record.toJsonObject());		
				   } catch (SQLException e) {
					   throw new DbException(
							   "Error updating " 
							   + library
							   +":" + topic 
							   +":" + key 
							   , e
							   );
				   }
			} else {
				result.setCode(HTTP_RESPONSE_CODES.NOT_FOUND.code);
				result.setMessage(HTTP_RESPONSE_CODES.NOT_FOUND.message + ": " + id);
			}
		} else {
			throw new MissingSchemaIdException(schemaId);
		}
		return result;
	}
	
	
	public UserStatus getUserStatus(
			String username
			, String password
			, String verb
			, String library
			) {
		UserStatus  status = new UserStatus();
		if (existsUser(username)) {
			UserStatistics userStats = getUserStats(username);
			status.setKnownUser(true);
			status.setAuthenticated(authenticated(username,password));
			status.setAuthorized(authorized(username,VERBS.forWsname(verb),library));
			long currentNano = System.nanoTime();
			long lastNano = userStats.getLastAccessNanos();
			if (lastNano == 0) {
				status.setSessionExpired(true);
			} else {
				long elapsedNano = currentNano - lastNano;
				long elapsedMinutes = TimeUnit.MINUTES.convert(elapsedNano, TimeUnit.NANOSECONDS);
				status.setSessionExpired(elapsedMinutes > maxInactiveMinutes);
			}
			userStats.setLastAccessNanos(currentNano);
			userStats.setLastSuccessfulAccessDateTime(getTimestamp());
			if (status.isAuthenticated()) {
				userStats.setAccessCount(userStats.getAccessCount() + 1);
			} else {
				userStats.setFailedLoginCount(userStats.getFailedLoginCount() + 1);
				userStats.setLastFailedAccessDateTime(Instant.now().toString());
			}
			updateUserStats(username, userStats);
		}
		return status;
	}
	
	private String getTimestamp() {
		return Instant.now().toString();
	}

	public User getUser(String username) {
		try {			
			JsonObject obj = getForId(SYSTEM_MISC_LIBRARY_TOPICS.USERS.toId(username));
			int count = obj.getAsJsonPrimitive("valueCount").getAsInt();
			if (count != 1) {
				return null;
			} else {
				User user = (User) gson.fromJson(
						obj.get("values")
						.getAsJsonArray()
						.get(0)
						.getAsJsonObject()
						.get("value")
						.getAsJsonObject()
						, User.class
				);
				return user;
			}
		} catch (Exception e) {
			ErrorUtils.report(logger, e);
			return null;
		}
	}

	public UserContact getUserContact(String username) {
		try {			
			JsonObject obj = getForId(USER_TOPICS.CONTACT.toId(username));
			int count = obj.getAsJsonPrimitive("valueCount").getAsInt();
			if (count != 1) {
				return null;
			} else {
				UserContact user = (UserContact) gson.fromJson(
						obj.get("values")
						.getAsJsonArray()
						.get(0)
						.getAsJsonObject()
						.get("value")
						.getAsJsonObject()
						, UserContact.class
				);
				return user;
			}
		} catch (Exception e) {
			ErrorUtils.report(logger, e);
			return null;
		}
	}
	
	public UserHash getUserHash(String username) {
		try {			
			JsonObject obj = getForId(USER_TOPICS.HASH.toId(username));
			int count = obj.getAsJsonPrimitive("valueCount").getAsInt();
			if (count != 1) {
				return null;
			} else {
				UserHash user = (UserHash) gson.fromJson(
						obj.get("values")
						.getAsJsonArray()
						.get(0)
						.getAsJsonObject()
						.get("value")
						.getAsJsonObject()
						, UserHash.class
				);
				return user;
			}
		} catch (Exception e) {
			ErrorUtils.report(logger, e);
			return null;
		}
	}
	
	public UserStatistics getUserStats(String username) {
		try {			
			JsonObject obj = getForId(USER_TOPICS.STATISTICS.toId(username));
			int count = obj.getAsJsonPrimitive("valueCount").getAsInt();
			if (count != 1) {
				return new UserStatistics();
			} else {
				UserStatistics user = (UserStatistics) gson.fromJson(
						obj.get("values")
						.getAsJsonArray()
						.get(0)
						.getAsJsonObject()
						.get("value")
						.getAsJsonObject()
						, UserStatistics.class
				);
				return user;
			}
		} catch (Exception e) {
			ErrorUtils.report(logger, e);
			return null;
		}
	}

	public String hashPassword(String password) {
		try {
			return PasswordHasher.createHash(password);
		} catch (Exception e) {
			ErrorUtils.report(logger, e);
		}
		return null;
	}
	
	public boolean authenticated(String username, String password) {
		try {
			UserHash user = getUserHash(username);
			if (user == null) {
				return false;
			} else {
				if (PasswordHasher.checkPassword(password, user.getHashedPassword())) {
					return true;
				} else {
					return false;
				}
			}
		} catch (NoSuchAlgorithmException e) {
			ErrorUtils.report(logger, e);
		} catch (InvalidKeySpecException e) {
			ErrorUtils.report(logger, e);
		}
		return false;
	}
	
	/**
	 * forms
	 * api/_sys
	 * api/_app
	 * api/lib
	 * 
	 * sysadmin
	 * 		if exists _rights/_sys/sysadmin/mcolburn
	 * 		put _sys/_rights/sysadmin/hjones
	 * 		put _app/_rights/appadmin/cbrown
	 * appadmin
	 * 		put lib/_rights/libadmin/gr_gr_cog/mbarnes
	 * 		put lib/_rights/admin/gr_gr_cog/mbarnes
	 * libadmin
	 * 		put lib/_rights/libauthor/gr_gr_cog/frraphael
	 * 		put lib/_rights/libreader/gr_gr_cog/public   <-- reserved user
	 * 		put lib/_rights/author/gr_gr_cog/frraphael
	 * 		put lib/_rights/reader/gr_gr_cog/public   <-- reserved user
	 * libauthor
	 * 		write lib if exists _rights/lib/gr_gr_cog/frraphael
	 * libreader
	 * 		read lib if exists _rights/lib/gr_gr_cog/public
	 * 
	 * 
	 * username
	 * library
	 * method
	 * 
	 * method/library/username
	 * put/gr_gr_cog/mcolburn
	 * put/_sys/mcolburn <- by person
	 * put/_sys/sysadmin <- by role
	 * 
	 * When create a new library create these rights:
	 * _rights/post/{library}/{role} <- sysadmin, appadim, libadmin, libauthor 
	 * _rights/get/{library}/{role} <- sysadmin, appadim, libadmin, libauthor
	 * _rights/put/{library}/{role} <- sysadmin, appadim, libadmin, libauthor
	 * _rights/delete/{library}/{role} <- sysadmin, appadim, libadmin, libauthor
	 * 
	 * When grant role to user:
	 * 
	 * {library}/{role}/{username}
	 * 
	 * Issues
	 * 	1. a library is like a table.  It should exist before granting rights to it or posting to it.
	 * 2. a library has meta-data such as the language, country, realm, who it is for, etc.
	 * 
	 * 
	 * admin/_sys
	 * admin/_app
	 * admin/gr_gr_cog
	 * author/gr_gr_cog/sjones
	 * reader/gr_gr_cog/all
	 * 
	 * if role based,
	 * 1. get all the roles for the person
	 * 2. check to see if any of the roles match the action and lib
	 * 
	 */
	
	
	/**
	 * A user who is an admin for _sys has the power to do anything to the system or any database library. 
	 * A _sys admin can make grant _sys or _db admin to any other user.
	 * 
	 * A user who is an admin for the _db has the power to do anything to the docs in the database, and
	 * to add users to the system, and to grant their authorizations for _db.  But, he/she does not have
	 * the ability to make give another person the authority to by an admin of the _sys or the _db.
	 * 
	 * A user who has authorization for any other library can be either an author or reader.
	 * An author can create, read, update, and delete docs in that library.
	 * A reader can only read docs in that library.
	 * 
	 * @param username 
	 * @param verb
	 * @param library
	 * @return
	 */
	public boolean authorized(
			String username
			, VERBS verb
			, String library
			) {
		boolean isAuthorized = false;
		if (isWsAdmin(username)) {
			isAuthorized = true;
		} else if (isDbAdmin(username)) {
			isAuthorized = true;
		} else if (isAdminForAnyLib(username) && isAdminPath(library)) {
			isAuthorized = true;
 		} else {
			if (isLibAdmin(library, username)) {
				isAuthorized = true;
			} else {
		    	switch (verb) {
		    	case GET: {
					if (isLibAuthor(library, username) 
							|| isLibReader(library, username)
							) {
						isAuthorized = true;
					}
		    		break;
		    	}
		    	case POST: {
					if (isLibAuthor(library, username) ) {
						isAuthorized = true;
					}
		    		break;
		    	}
			case PUT:
				if (isLibAuthor(library, username) ) {
					isAuthorized = true;
				}
				break;
			case DELETE:
				if (isLibAuthor(library, username) ) {
					isAuthorized = true;
				}
				break;
			default:
				break;
	    	}
			}
		}
		if (suppressAuth) { // used for debugging purposes
			return true;
		} else {
			return isAuthorized;
		}
	}
	
	/**
	 * Is this path one of the web service admin paths?
	 * @param path
	 * @return
	 */
	private boolean isAdminPath(String path) {
		if (path.matches(Constants.RESOURCES_PATH)) {
			return true;
		} else {
			for (ENDPOINTS_ADMIN_API e : ENDPOINTS_ADMIN_API.values()) {
				if (e.library.equals(path)) {
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * Is this person an administrator of the Web Service?
	 * 
	 * @param username
	 * @return
	 */
	public boolean isWsAdmin(String username) {
		return hasRole(ROLES.ADMIN, Constants.SYSTEM_LIB, username);
	}
	

	/**
	 * Is this person an administrator of the backend database?
	 * @param username
	 * @return
	 */
	public boolean isDbAdmin(String username) {
		return isWsAdmin(username) || hasRole(ROLES.ADMIN,Constants.DOMAINS_LIB,username);
	}
	
	public boolean isAdminForAnyLib(String username) {
		return isDbAdmin(username) ||
				getWhereLike(ROLES.ADMIN.keyname + "%" + username).get("valueCount").getAsInt() > 0
				;
	}
	
	public List<String> getIdsOfDomainsUserAdministers(String username) {
		List<String> result = new ArrayList<String>();
		if (isDbAdmin(username)) {
			return getDomainIds();
		} else {
			JsonObject json = getWhereLike(ROLES.ADMIN.keyname + "%" + username);
			if (json.get("valueCount").getAsInt() > 0) {
				
			}
			return result;
		}
	}

	/**
	 * Is this person an administrator for the specified library?
	 * @param library
	 * @param username
	 * @return
	 */
	public boolean isLibAdmin(String library, String username) {
		return isDbAdmin(username) || hasRole(ROLES.ADMIN,library,username);
	}
	
	/**
	 * Is this person an authorized author for the specified library?
	 * @param library
	 * @param username
	 * @return
	 */
	public boolean isLibAuthor(String library, String username) {
		return hasRole(ROLES.AUTHOR,library,username);
	}

	/**
	 * Is this person an authorized reader for the specified library?
	 * @param library
	 * @param username
	 * @return
	 */
	public boolean isLibReader(String library, String username) {
		return hasRole(ROLES.READER, library,username);
	}

	/**
	 * Does the specified user have this role for this library?
	 * @param role
	 * @param library
	 * @param username
	 * @return true if that is the case
	 */
	public boolean hasRole(ROLES role, String library, String username) {
		return existsUnique(role.toId(library, username));
	}
	/**
	 * Is there a single doc that matches this id?
	 * @param _id
	 * @return true if there is only one doc that matches
	 */
	public boolean existsUnique(String _id) {
		JsonObject json = getForId(_id);
		return json.get("valueCount").getAsInt() == 1;
	}
	
	public RequestStatus addSchema(String schemaId, JsonObject json) {
		RequestStatus result = new RequestStatus();
			try {
				LTKVJsonObject record;
				record = new LTKVJsonObject(
						SYSTEM_MISC_LIBRARY_TOPICS.SCHEMAS.lib
						, SYSTEM_MISC_LIBRARY_TOPICS.SCHEMAS.topic
						, schemaId
						, schemaId
						, json
						);
		    	manager.insert(record.toJsonObject());		
		    	result.setCode(HTTP_RESPONSE_CODES.CREATED.code);
		    	result.setMessage(HTTP_RESPONSE_CODES.CREATED.message + ": " + schemaId);
			} catch (Exception e) {
				result.setCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
				result.setMessage(HTTP_RESPONSE_CODES.BAD_REQUEST.message);
			}
		return result;
	}
	
	public RequestStatus updateSchema(String schemaId, JsonObject json) {
		RequestStatus result = new RequestStatus();
		String id = new IdManager(SYSTEM_MISC_LIBRARY_TOPICS.SCHEMAS.lib,SYSTEM_MISC_LIBRARY_TOPICS.SCHEMAS.topic,schemaId).getId();
		if (existsUnique(id)) {
			try {
				LTKVJsonObject record;
				record = new LTKVJsonObject(
						SYSTEM_MISC_LIBRARY_TOPICS.SCHEMAS.lib
						, SYSTEM_MISC_LIBRARY_TOPICS.SCHEMAS.topic
						, schemaId
						, schemaId
						, json
						);
				manager.updateWhereEqual(record.toJsonObject());		
			} catch (Exception e) {
				result.setCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
				result.setMessage(HTTP_RESPONSE_CODES.BAD_REQUEST.message);
			}
		} else {
			result.setCode(HTTP_RESPONSE_CODES.NOT_FOUND.code);
			result.setMessage(HTTP_RESPONSE_CODES.NOT_FOUND.message + ": " + id);
		}
		return result;
	}

	public boolean isPrettyPrint() {
		return prettyPrint;
	}

	public void setPrettyPrint(boolean prettyPrint) {
		this.prettyPrint = prettyPrint;
	}

	@Override
	public RequestStatus deleteForId(String id) {
		// TODO Auto-generated method stub
		return null;
	}

}
