package ioc.liturgical.ws.app;

import static spark.Spark.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;

import spark.QueryParamsMap;
import spark.Request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;

import ioc.liturgical.ws.constants.Constants;
import ioc.liturgical.ws.constants.HTTP_RESPONSE_CODES;
import ioc.liturgical.ws.controllers.admin.AuthorizationsController;
import ioc.liturgical.ws.controllers.admin.DomainsController;
import ioc.liturgical.ws.controllers.admin.LabelsController;
import ioc.liturgical.ws.controllers.admin.LoginController;
import ioc.liturgical.ws.controllers.admin.NewFormsController;
import ioc.liturgical.ws.controllers.admin.ResourcesController;
import ioc.liturgical.ws.controllers.admin.UsersContactController;
import ioc.liturgical.ws.controllers.admin.UsersPasswordController;
import ioc.liturgical.ws.controllers.db.neo4j.Neo4jController;
import ioc.liturgical.ws.controllers.db.neo4j.ReferencesController;
import ioc.liturgical.ws.controllers.ldp.LdpController;
import ioc.liturgical.ws.managers.auth.AuthDecoder;
import ioc.liturgical.ws.managers.auth.UserStatus;
import ioc.liturgical.ws.managers.databases.external.neo4j.ExternalDbManager;
import ioc.liturgical.ws.managers.databases.internal.InternalDbManager;
import ioc.liturgical.ws.managers.ldp.LdpManager;
import net.ages.alwb.utils.core.datastores.json.manager.JsonObjectStoreManager;
import net.ages.alwb.utils.core.error.handling.ErrorUtils;

/**
 * Main Class for the IOC Liturgical Web Services. Provides the basic service.
 * 
 * The service can be started either by running main or calling the runService method.
 * 
 *  When you start the service, it is necessary to pass in the root (web service admin) password.
 *  
 *  This password must be the same one used in the external (backend) database.
 *
 *  If the *internal* database is empty, it will be initialized.  The wsadmin user will be
 *  added as the web service administrator using the password passed in when
 *  the service is started.  
 *  
 * The class reads properties set in the file named serviceProvider.config
 * 
 * 
 * Key concepts:
 * 
 * The IOC-Liturgical-WS protects and provides access to a back-end database.
 * A database contains libraries.
 * A library contains docs.
 * Docs are organized in the library by a topic.
 * A doc is uniquely identified by its ID.
 * A doc ID is library + topic + key, stored with a pipe delimiter, e.g. the | character
 * in the internal database, and with a tilde ~ character in the Neo4j (external) database.
 * 
 * There are three types of resource categories, and therefore there are three types of 
 * administrators:
 * 
 * 1. web service administrator (wsAdmin) manages the web service itself.
 * 2. database administrator (dbAdmin) manages the backend database.
 * 3. library administrator (libAdmin) manages a specific library in the database.
 * 
 * Supported application programming interfaces (api's) are defined as constants in ioc.liturgical.ws.contants.Constants.java:
 * 
 * 1. INTERNAL_DATASTORE_API_PATH is the api path for web services administration
 * 2. EXTERNAL_DATASTORE_API_PATH is the api path for the protected backend database
 * 3. INTERNAL_LITURGICAL_PROPERTIES_API_PATH is the api path for liturgical day properties

 * The backend database is protected using basic authentication.  
 * There is a single backend database user: wsadmin.
 * The hash of wsadmin's password from the backend database must be placed in the serviceProvider.config file.
 *
 * The web service provides authentication and authorization as a proxy to the external backend database.
 * User accounts and user authorizations are maintained using a ws admin app. 
 * 
 * @author mac002
 *
 */
public class ServiceProvider {

	private static final Logger logger = LoggerFactory.getLogger(ServiceProvider.class);
	public static String ws_pwd;
	public static String ws_usr;
	public static String keystorePath;

	private static int maxInactiveMinutes = 10 * 60; // default to 10 minutes

	public static InternalDbManager storeManager;
	
	public static ExternalDbManager docService;
	
	public static LdpManager ldpManager;
	
	public static boolean enableCors = false;
	static boolean externalDbIsReadOnly = false;
	static boolean externalDbAccessIsProtected = true;
	public static String externalDbDomain = "localhost";
	static boolean debug = false; // can be overridden by serviceProvider.config
	static boolean logAllQueries = false; // can be overridden by serviceProvider.config
	static boolean logQueriesWithNoMatches = false; // can be overridden by serviceProvider.config
	static boolean createTestUsers = false; // can be overridden by serviceProvider.config
	static boolean runningJUnitTests = false; // can be overridden by serviceProvider.config
	static boolean useExternalStaticFiles = true; // can be overridden by serviceProvider.config
	static String staticExternalFileLocation = null;
	
	/**
	 * If the property is null, the method returns
	 * back the value of var, otherwise it checks
	 * prop to see if it starts with "true".  If so
	 * it returns true, else false.  
	 * 
	 * The var is passed in so that if the config file lacks 
	 * the specified property, the default value gets used.
	 * @param var
	 * @param prop
	 * @return
	 */
	public static boolean toBoolean(boolean var, String prop) {
		if (prop == null) {
			return var;
		} else {
			return prop.startsWith("true");
		}
	}

	public static void main(String[] args) {
		
		boolean allOk = true;
		
		int maxThreads = 8;
		int minThreads = 2;
		int timeOutMillis = 30000;
		
		ws_pwd = args[0];
		ws_usr = "wsadmin";
		boolean initializeExternalDb = true;
		if (args.length > 1) {
			initializeExternalDb =  !(args[1].equals("noexdb"));
			logger.info("External Db disabled by parameter passed to main: " + args[1]);
		}
		
		/**
		 * Values from Properties file
		 */
		Properties prop = new Properties();
		InputStream input = null;
		boolean useSsl = false;
		boolean debugStore = true;
		boolean deleteExistingDataStoreFile = false;
		boolean deleteExistingDataStoreRecords = false;
		String datastore_type = "db";
		JsonObjectStoreManager.STORE_TYPE store_type = JsonObjectStoreManager.STORE_TYPE.DB;

		try {
			ServiceProvider.class.getClassLoader();
			String location = getLocation();
			logger.info("Jar is executing from: " + location);
			URL keystore = ServiceProvider.class.getClassLoader().getResource("clientkeystore");
			keystorePath = keystore.getPath();
			try {
				input = new FileInputStream(new File(location+"/resources/serviceProvider.config"));
			} catch (Exception e) {
				// load from bundled config file
				input = ServiceProvider.class.getClassLoader().getResourceAsStream("serviceProvider.config");
			}
			prop.load(input);
			
			debug = toBoolean(debug, prop.getProperty("debug"));
			logger.info("debug: " + debug);

			logAllQueries = toBoolean(logAllQueries, prop.getProperty("logQueries"));
			logger.info("logQueries: " + logAllQueries);

			logQueriesWithNoMatches = toBoolean(logQueriesWithNoMatches, prop.getProperty("logQueriesWithNoMatches"));
			logger.info("logQueriesWithNoMatches: " + logQueriesWithNoMatches);

			createTestUsers = toBoolean(createTestUsers, prop.getProperty("createTestUsers"));
			logger.info("create test users: " + debug);

			runningJUnitTests = toBoolean(runningJUnitTests, prop.getProperty("runningJUnitTests"));
			logger.info("runningJUnitTests: " + runningJUnitTests);

			String ssl = prop.getProperty("use_ssl");
			logger.info("use_ssl: " + ssl);
			useSsl = ssl.toLowerCase().startsWith("true");
			deleteExistingDataStoreFile = toBoolean(deleteExistingDataStoreFile, prop.getProperty("datastore_delete_existing"));
			logger.info("datastore_delete_existing: " + deleteExistingDataStoreFile );

			deleteExistingDataStoreRecords = toBoolean(deleteExistingDataStoreRecords, prop.getProperty("datastore_truncate_existing"));
			logger.info("datastore_truncate_existing: " + deleteExistingDataStoreRecords );

			enableCors = toBoolean(enableCors, prop.getProperty("enable_cors"));
			logger.info("enable_cors: " + enableCors);

			externalDbIsReadOnly = toBoolean(externalDbIsReadOnly, prop.getProperty("external_db_is_read_only"));
			logger.info("external_db_is_read_only: " + externalDbIsReadOnly);
	
			externalDbAccessIsProtected = toBoolean(externalDbAccessIsProtected, prop.getProperty("external_db_access_is_protected"));
			logger.info("external_db_access_is_protected: " + externalDbAccessIsProtected);

			externalDbDomain = prop.getProperty("external_db_domain");
			logger.info("external_db_domain: " + externalDbDomain);

			useExternalStaticFiles = toBoolean(debug, prop.getProperty("useExternalStaticFiles"));
			logger.info("useExternalStaticFiles: " + useExternalStaticFiles);

			ServiceProvider.staticExternalFileLocation = prop.getProperty("staticExternalFileLocation");
			logger.info("staticExternalFileLocation: " + ServiceProvider.staticExternalFileLocation);

			datastore_type = prop.getProperty("datastore_type");
			debugStore = toBoolean(debugStore, prop.getProperty("datastore_debug"));
			if (datastore_type.toLowerCase().startsWith("file")) {
				store_type = JsonObjectStoreManager.STORE_TYPE.FILE;
			} else if (datastore_type.toLowerCase().startsWith("db")) {
				store_type = JsonObjectStoreManager.STORE_TYPE.DB;
			} else {
				allOk = false;
				logger.error("Valid datastore_type values are: file, db");
			}
			try {
				maxInactiveMinutes = 60 * Integer.parseInt(prop.getProperty("session_max_minutes"));
			} catch (Exception e) {
				logger.error("Property maxInactiveInterval missing or not a number.");
			}

		} catch (Exception e) {
			ErrorUtils.report(logger, e);
			allOk = false;
		}
//		port(8080);
		if (useSsl) {
			secure(keystorePath, ws_pwd, null, null);
		}
		/**
		 * Static file configuration:
		 * 
		 * If you want to bundle the static web apps with the jar, then put them
		 * into the src/main/resources/public folder, and set serviceProvider.config to:
		 * useExternalStaticFiles=false
		 * 
		 * If you want to keep the web apps separate, set the serviceProvider.config
		 * useExternalStaticFiles=true
		* staticExternalFileLocation=/some/path
		* 
		* where /some/path is the actual path you want to use.
		 */
		if (! runningJUnitTests) {
			if (useExternalStaticFiles) {
				externalStaticFileLocation(ServiceProvider.staticExternalFileLocation);
			} else {
				staticFileLocation("/public"); 
			}

			threadPool(maxThreads, minThreads, timeOutMillis);
		}

		if (allOk) {
			logger.info("Property file loaded OK");
			// Create the manager for operations involving the underlying datastore
			storeManager = new InternalDbManager(
					"ioc-liturgical-db"
					, "json"
					, deleteExistingDataStoreFile
					, deleteExistingDataStoreRecords
					, createTestUsers
					, ws_usr
					);
			storeManager.setMaxInactiveMinutes(maxInactiveMinutes);
			
			if (initializeExternalDb) {
				docService = new ExternalDbManager(
						externalDbDomain
						, logAllQueries
						, logQueriesWithNoMatches
						, storeManager
						);
			} else {
				docService = null;
			}
			
			ldpManager = new LdpManager(storeManager);
			
			if (enableCors) {
				enableCORS("*", "*", "*");
			}
			/**
			 * Make sure that api calls do not have a path that exceeds
			 * /api/library/topic/key Make sure the user is authenticated and
			 * authorized
			 */
			before((request, response) -> {
				if (debug) {
					dump(request);
				}
				if ((! externalDbAccessIsProtected && request.pathInfo().startsWith("/db/api"))
						|| (
								request.pathInfo().toLowerCase().startsWith("/ldp/api")
							)
						|| (
								request.requestMethod().toLowerCase().startsWith("options")
							)
					) {
				} else {
					request.session(true);
					AuthDecoder authDecoder = new AuthDecoder(request.headers("Authorization"));
					String method = request.requestMethod();
					String path = request.pathInfo();
					String library = libraryFrom(path);
					UserStatus status = storeManager.getUserStatus(
							authDecoder.getUsername()
							, authDecoder.getPassword()
							, method
							, library
							);
					if (status.isAuthenticated()) {
						if (status.isSessionExpired() && request.pathInfo().length() == 1
								&& request.pathInfo().startsWith("/")) {
							response.header("WWW-Authenticate", "Basic realm=\"Restricted\"");
							response.status(HTTP_RESPONSE_CODES.UNAUTHORIZED.code);
						} else {
							if (status.isAuthorized()) {
								// let the request pass through to the handler
							} else {
								response.type(Constants.UTF_JSON);
								halt(HTTP_RESPONSE_CODES.UNAUTHORIZED.code, HTTP_RESPONSE_CODES.UNAUTHORIZED.message);
							}
						}
					} else {
						// we do not require authorization to attempt to login, or to get the Liturgical Day Properties
						if (
								request.pathInfo().startsWith(Constants.INTERNAL_DATASTORE_API_PATH +"/login")
								|| request.pathInfo().startsWith(Constants.INTERNAL_DATASTORE_API_PATH +"/info")
								|| request.pathInfo().startsWith(Constants.INTERNAL_LITURGICAL_DAY_PROPERTIES_API_PATH)
								) {
							// pass through to handler
						} else {
							response.header("WWW-Authenticate", "Basic realm=\"Restricted\"");
							halt(HTTP_RESPONSE_CODES.UNAUTHORIZED.code, HTTP_RESPONSE_CODES.UNAUTHORIZED.message);
						}
					}
				}
			});

			/**
			 * Routes
			 * 
			 * Important: order the routes from most specific to least,
			 * Keep in mind that routes are being inject both from the
			 * injected controllers and the explicit routes in this class.
			 */

			/**
			 * Inject controllers
			 * 
			 * In some cases, injected controllers only need to handle post and put.
			 * In such cases, they will use the get handlers in this class. 
			 */
			new AuthorizationsController(storeManager);
			new DomainsController(storeManager);
			new LabelsController(storeManager);
			new LoginController(storeManager);
			new NewFormsController(storeManager);
			new Neo4jController(docService);
			new ReferencesController(docService);
			new ResourcesController(storeManager);
			new UsersContactController(storeManager);
			new UsersPasswordController(storeManager);
			new LdpController(ldpManager);

			get("api/sugar", (request, response) -> {
				response.type(Constants.UTF_JSON);

				if (request.queryParams().isEmpty()) {
					response.status(400);
					return "{'status': '400'}";
				} else {
					String query = request.queryParams("p").replaceAll("\\*", "%");
					response.status(200);
					return storeManager.getWhereLike(query).toString();
				}
			});
			
			/**
			 * Grant a right to a library Request Segments: api _rights role =
			 * libadmin or libauthor or libreader library, e.g. gr_GR_cog user =
			 * username
			 * 
			 * Preconditions: the role, library, and user must exist the
			 * requesting user must be authorized to grant this right against
			 * this library
			 */
			put("/api/v1/_rights/*/*/*", (request, response) -> {
				response.type(Constants.UTF_JSON);
				return HTTP_RESPONSE_CODES.NOT_FOUND;
			});

			/**
			 * Get by library and topic
			 */
			get("/api/v1/like/*/*/*", (request, response) -> {
				response.type(Constants.UTF_JSON);
				response.status(200);
				String query = ServiceProvider.createStringFromSplat(request.splat(), Constants.ID_DELIMITER);
				query = query.replaceAll("\\*", "%");
				return storeManager.getWhereLike(query).toString();
			});

			/**
			 * Get by library and topic
			 */
			get("/api/v1/like/*/*", (request, response) -> {
				response.type(Constants.UTF_JSON);
				response.status(200);
				String query = ServiceProvider.createStringFromSplat(request.splat(), Constants.ID_DELIMITER);
				query = query.replaceAll("\\*", "%");
				return storeManager.getWhereLike(query).toString();
			});

			/**
			 * Get by library
			 */
			get("/api/v1/like/*", (request, response) -> {
				response.type(Constants.UTF_JSON);
				response.status(200);
				String query = ServiceProvider.createStringFromSplat(request.splat(), Constants.ID_DELIMITER);
				query = query.replaceAll("\\*", "%");
				return storeManager.getWhereLike(query).toString();
			});			
			
			/**
			 * Return the version number for the overall web service.  This
			 * needs to be updated in Constants every time a new jar is created.
			 */
			get(Constants.INTERNAL_DATASTORE_API_PATH  + "/info", (request, response) -> {
				response.type(Constants.UTF_JSON);
				JsonObject json = new JsonObject();
				json.addProperty("wsVersion", Constants.VERSION);
				json.addProperty("dbServerDomain", externalDbDomain);
				json.addProperty("databaseReadOnly", externalDbIsReadOnly);
				return json.toString();
			});

			get("user", (request, response) -> {
				response.type(Constants.UTF_JSON);
				AuthDecoder authDecoder = new AuthDecoder(request.headers("Authorization"));
				JsonObject json = new JsonObject();
				if (storeManager.authenticated(authDecoder.getUsername(), authDecoder.getPassword())) {
					json.addProperty("authorized", true);
					response.status(HTTP_RESPONSE_CODES.OK.code);
				} else {
					json.addProperty("authorized", false);
					response.status(HTTP_RESPONSE_CODES.UNAUTHORIZED.code);
				}
				return json.toString();
			});

			/**
			 * Get ws resources by library and topic and key
			 */
			get(Constants.INTERNAL_DATASTORE_API_PATH + "/*/*/*", (request, response) -> {
				response.type(Constants.UTF_JSON);
				String query = ServiceProvider.createStringFromSplat(request.splat(), Constants.ID_DELIMITER);
				JsonObject json = storeManager.getForId(query);
				if (json.get("valueCount").getAsInt() > 0) {
					response.status(HTTP_RESPONSE_CODES.OK.code);
				} else {
					response.status(HTTP_RESPONSE_CODES.NOT_FOUND.code);
				}
				return json.toString();
			});

			/**
			 * Get ws resources by library and topic
			 */
			get(Constants.INTERNAL_DATASTORE_API_PATH + "/*/*", (request, response) -> {
				response.type(Constants.UTF_JSON);
				String query = ServiceProvider.createStringFromSplat(request.splat(), Constants.ID_DELIMITER);
				JsonObject json = storeManager.getForIdStartsWith(query);
				if (json.get("valueCount").getAsInt() > 0) {
					response.status(HTTP_RESPONSE_CODES.OK.code);
				} else {
					response.status(HTTP_RESPONSE_CODES.NOT_FOUND.code);
				}
				return json.toString();
			});

			/**
			 * Get ws resources by library
			 */
			get(Constants.INTERNAL_DATASTORE_API_PATH + "/*", (request, response) -> {
				response.type(Constants.UTF_JSON);
				String query = ServiceProvider.createStringFromSplat(request.splat(), Constants.ID_DELIMITER);
				JsonObject json = storeManager.getForIdStartsWith(query);
				if (json.get("valueCount").getAsInt() > 0) {
					response.status(HTTP_RESPONSE_CODES.OK.code);
				} else {
					response.status(HTTP_RESPONSE_CODES.NOT_FOUND.code);
				}
				return json.toString();
			});

			/**
			 * Get api version 1
			 */
			get("/api/v1", (request, response) -> {
				response.type(Constants.UTF_JSON);
				return HTTP_RESPONSE_CODES.NOT_FOUND.code;
			});

			/**
			 * Get api
			 */
			get("/api", (request, response) -> {
				response.type(Constants.UTF_JSON);
				response.status(404);
				return HTTP_RESPONSE_CODES.NOT_FOUND.code;
			});

		
			/**
			 * Post by library and topic and key
			 */
			post("/api/v1/*/*/*", (request, response) -> {
				response.type(Constants.UTF_JSON);
				String query = ServiceProvider.createStringFromSplat(request.splat(), Constants.ID_DELIMITER);
				JsonObject json = storeManager.getForId(query);
				if (json.get("valueCount").getAsInt() > 0) {
					response.status(HTTP_RESPONSE_CODES.OK.code);
				} else {
					response.status(HTTP_RESPONSE_CODES.NOT_FOUND.code);
				}
				return json.toString();
			});

			/**
			 * Post by library and topic
			 */
			post("/api/v1/*/*", (request, response) -> {
				response.type(Constants.UTF_JSON);
				String query = ServiceProvider.createStringFromSplat(request.splat(), Constants.ID_DELIMITER);
				JsonObject json = storeManager.getForIdStartsWith(query);
				if (json.get("valueCount").getAsInt() > 0) {
					response.status(HTTP_RESPONSE_CODES.OK.code);
				} else {
					response.status(HTTP_RESPONSE_CODES.NOT_FOUND.code);
				}
				return json.toString();
			});

			/**
			 * Post by library
			 */
			post("/api/v1/*", (request, response) -> {
				response.type(Constants.UTF_JSON);
				String query = ServiceProvider.createStringFromSplat(request.splat(), Constants.ID_DELIMITER);
				JsonObject json = storeManager.getForIdStartsWith(query);
				if (json.get("valueCount").getAsInt() > 0) {
					response.status(HTTP_RESPONSE_CODES.CREATED.code);
				} else {
					response.status(HTTP_RESPONSE_CODES.NOT_FOUND.code);
				}
				return json.toString();
			});
				
		}
		// this can greatly bog down your workstation, so use only when necessary.
		after((request, response) -> {
			if (debug) {
//				postDump(response);
			}
		});
	}


	public static String createPathFromSplat(String[] splat) {
		StringBuilder sb = new StringBuilder();
		int size = splat.length;
		for (int i = 0; i < size; i++) {
			sb.append(splat[i] + "/");
		}
		return deSlash(sb.toString());
	}

	public static String deSlash(String s) {
		if (s.endsWith("/")) {
			return s.substring(0, s.length() - 1);
		} else {
			return s;
		}
	}

	/**
	 * Converts parameters into the format of a database ID
	 * 
	 * @param splat
	 * @param delimiter
	 * @return
	 */
	public static String createStringFromSplat(String[] splat, String delimiter) {
		StringBuilder sb = new StringBuilder();
		int size = splat.length;
		for (int i = 0; i < size; i++) {
			sb.append(splat[i] + delimiter);
		}
		return deDelimit(sb.toString(), delimiter);
	}

	public static String deDelimit(String s, String delimiter) {
		if (s.endsWith(delimiter)) {
			return s.substring(0, s.length() - 1);
		} else {
			return s;
		}
	}

	public static Map<String, Object> toObjectMap(Set<String> keys, QueryParamsMap map) {
		if (map == null) {
			return null;
		} else {
			Map<String, Object> result = new TreeMap<String, Object>();
			try {
				for (String key : keys) {
					String value = map.get(key).value();
					result.put(key, value);
				}
			} catch (Exception e) {
				ErrorUtils.report(logger, e);
			}
			return result;
		}
	}

	public static void dump(Request request) {
		StringBuffer sb = new StringBuffer();
		sb.append(request.requestMethod() + ": " + request.pathInfo());
		Set<String> headers = request.headers();
		if (headers.size() > 0) {
			sb.append("\nHeaders:\n");
			for (String header : headers) {
				sb.append("\t" + header + ": " + request.headers(header) + "\n");
			}
		}
		Set<String> params = request.queryParams();
		if (params.size() > 0) {
			sb.append("\nParms:\n");
			for (String parm : params) {
				sb.append("\t" + parm + "=" + request.queryParams(parm) + "\n");
			}
		}
		sb.append("body: \n\t");
		sb.append(request.body());
		print(sb.toString());
	}

	public static void postDump(spark.Response response) {
		StringBuffer sb = new StringBuffer();
		sb.append(response.body());
		sb.append(response.status());
		print(sb.toString());
	}

	public static void print(String message) {
		System.out.println(message);
	}

	/**
	 * Safely try to get the first value from splat
	 * 
	 * @param splat
	 * @return
	 */
	private static String libraryFrom(String path) {
		String result = path;
		try {
			int apiIndex = 0;
			if (path.startsWith(Constants.INTERNAL_DATASTORE_API_PATH)) {
				apiIndex = Constants.INTERNAL_DATASTORE_API_PATH.split("/").length;
			} else if (path.startsWith(Constants.EXTERNAL_DATASTORE_API_PATH)) {
				apiIndex = Constants.EXTERNAL_DATASTORE_API_PATH.split("/").length;
			}
			String[] parts = path.split("/");
			result = parts[apiIndex];
		} catch (Exception e) {
			ErrorUtils.report(logger, e);
		}
		return result;
	}

	// Enables CORS on requests. This method is an initialization method and should be called once.
	private static void enableCORS(
			final String origin
			, final String methods
			, final String headers
			) {

	    options("/*", (request, response) -> {

	        String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
	        if (accessControlRequestHeaders != null) {
	            response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
	        }

	        String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
	        if (accessControlRequestMethod != null) {
	            response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
	        }

	        return "OK";
	    });

	    before((request, response) -> {
	        response.header("Access-Control-Allow-Origin", origin);
	        response.header("Access-Control-Request-Method", methods);
	        response.header("Access-Control-Allow-Headers", headers);
	        // Note: this may or may not be necessary in your particular application
	        response.type("application/json");
	    });
	}
	
	 
	  public static String getNeo4jUrl() {
	        String urlVar = System.getenv("NEO4J_URL_VAR");
	        if (urlVar==null) urlVar = "NEO4J_URL";
	        String url =  System.getenv(urlVar);
	        if(url == null || url.isEmpty()) {
	            return Constants.DEFAULT_URL;
	        }
	        return url;
	    }
	  
	  public static String getLocation() {
		  try {
			return new File(ServiceProvider.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParent();
		} catch (URISyntaxException e) {
			ErrorUtils.report(logger, e);
			return null;
		}
	  }
	  
}
