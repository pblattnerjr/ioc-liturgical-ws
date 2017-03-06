package ioc.liturgical.ws.models;

import com.google.gson.annotations.Expose;

import net.ages.alwb.utils.core.datastores.json.models.AbstractModel;

/**
 * {"pointerToViolation":"#/username","causingExceptions":[],"keyword":"minLength","message":"expected minLength: 3, actual: 0"}
 * @author mac002
 *
 */
public class SchemaException extends AbstractModel {
	@Expose String pointerToViolation = "";
	@Expose String [] causingExceptions = new String[0];
	@Expose String keyword = "";
	@Expose String message = "";
	
	public SchemaException () {
		super();
	}
	
	public SchemaException(String pointerToViolation, String keyword, String message) {
		this.pointerToViolation = pointerToViolation;
		this.keyword = keyword;
		this.message = message;
	}

	public String getPointerToViolation() {
		return pointerToViolation;
	}

	public void setPointerToViolation(String pointerToViolation) {
		this.pointerToViolation = pointerToViolation;
	}

	public String[] getCausingExceptions() {
		return causingExceptions;
	}

	public void setCausingExceptions(String[] causingExceptions) {
		this.causingExceptions = causingExceptions;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
