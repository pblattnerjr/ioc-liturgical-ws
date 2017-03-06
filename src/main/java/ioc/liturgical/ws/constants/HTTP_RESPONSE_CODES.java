package ioc.liturgical.ws.constants;

import com.google.gson.JsonObject;

public enum HTTP_RESPONSE_CODES {
	OK(200, "Request completed without any issues.")
	, BAD_REQUEST(400, "Malformed request") 
	, CONFLICT(409, "Resource already exists.") 
	, CREATED(201, "Resource created.")
	, FORBIDDEN (403, "User does not have authorization for requested resource.") 
	, LIBRARY_DOES_NOT_EXIST(400, "Library does not exist.") 
	, NO_CONTENT(204, "Body missing.")
	, NOT_FOUND (404, "Requested resource does not exist.")
	, NOT_ALLOWED(405, "User not allowed to use this method on requested resource.") 
	, SERVER_ERROR(500,"An internal server error occurred.")
	, UNAUTHORIZED(401, "Missing or invalid authentication credentials.")
	, USER_DOES_NOT_EXIST(400, "User does not exist.") 
	;
	
	public int code = 0;
	public String message = "";
	
	private HTTP_RESPONSE_CODES(int code, String message) {
		this.code = code;
		this.message = message;
	}
	
	/**
	 * Get the code and message as a Json string
	 * @param code enum
	 * @return json for the code value and message
	 */
	public String toJsonString(HTTP_RESPONSE_CODES code) {
		JsonObject body = new JsonObject();
		body.addProperty("code", code.code);
		body.addProperty("message", code.message);
		JsonObject response = new JsonObject();
		response.add("status", body);
		return response.toString();
	}
}
