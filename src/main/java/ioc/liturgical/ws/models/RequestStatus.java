package ioc.liturgical.ws.models;

import com.google.gson.annotations.Expose;

import ioc.liturgical.ws.constants.HTTP_RESPONSE_CODES;
import net.ages.alwb.utils.core.datastores.json.models.AbstractModel;

/**
 * Holds the status of a Request.
 * 
 * The constructor sets the status code to 200 (OK).
 * 
 * If the code is different (i.e. there are errors), call the set methods.
 * 
 * @author mac002
 *
 */
public class RequestStatus extends AbstractModel {
	@Expose public int code = HTTP_RESPONSE_CODES.OK.code;
	@Expose public String developerMessage = HTTP_RESPONSE_CODES.OK.message;
	@Expose public String userMessage = HTTP_RESPONSE_CODES.OK.message;
	
	/**
	 * Defaults to code = 200 (OK)
	 */
	public RequestStatus() {
		super();
	}
	
	public RequestStatus(HTTP_RESPONSE_CODES code) {
		super();
		this.code = code.code;
		this.userMessage = code.message;
	}
	
	/**
	 * Both developerMessage and userMessage will be set to the value of message
	 * @param code
	 * @param message
	 */
	public RequestStatus(int code, String message) {
		super();
		this.code = code;
		this.developerMessage = message;
		this.userMessage = message;
	}
	
	public RequestStatus(int code, String developerMessage, String userMessage) {
		super();
		this.code = code;
		this.developerMessage = developerMessage;
		this.userMessage = userMessage;
	}

	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getDeveloperMessage() {
		return developerMessage;
	}
	public void setDeveloperMessage(String developerMessage) {
		this.developerMessage = developerMessage;
	}
	public String getUserMessage() {
		return userMessage;
	}
	public void setUserMessage(String userMessage) {
		this.userMessage = userMessage;
	}
	
	public void setMessage(String message) {
		setDeveloperMessage(message);
		setUserMessage(message);
	}
}
