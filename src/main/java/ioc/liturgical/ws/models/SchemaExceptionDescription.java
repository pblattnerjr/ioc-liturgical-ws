package ioc.liturgical.ws.models;

import com.google.gson.annotations.Expose;

import net.ages.alwb.utils.core.datastores.json.models.AbstractModel;

public class SchemaExceptionDescription extends AbstractModel {
	@Expose String keyword;
	@Expose String field;
	@Expose String expected;
	@Expose String got;
	
	public SchemaExceptionDescription(
			String keyword
			, String field
			, String expected
			, String got
			) {
		super();
		this.keyword = keyword;
		this.field = field;
		this.expected = expected;
		this.got = got;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getExpected() {
		return expected;
	}

	public void setExpected(String expected) {
		this.expected = expected;
	}

	public String getGot() {
		return got;
	}

	public void setGot(String got) {
		this.got = got;
	}
	
	public String toString() {
		return "expected " + field + " " + keyword + ":" + " " + got; 
	}
}
