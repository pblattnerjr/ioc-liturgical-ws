package net.ages.alwb.utils.core.datastores.json.models;

import java.time.LocalDateTime;

import com.google.gson.annotations.Expose;

import net.ages.alwb.utils.core.datastores.json.models.AbstractModel;

public class TransactionHistory extends AbstractModel {
	@Expose private String _id;
	@Expose private String _rev;
	@Expose private String userId;
	@Expose private String date;
	@Expose private String path;
	@Expose private String body;
	@Expose private int resultStatusCode;
	@Expose private String resultStatusText;
	
	public TransactionHistory() {
		super();
	}
	
	public TransactionHistory(
			String userId
			, String path
			, String body
			, int resultStatusCode
			, String resultStatusText
			) {
		this.userId = userId;
		this.date = LocalDateTime.now().toString();
		this.path = path;
		this.body = body;
		this.resultStatusCode = resultStatusCode;
		this.resultStatusText = resultStatusText;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String get_rev() {
		return _rev;
	}

	public void set_rev(String _rev) {
		this._rev = _rev;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public int getResultStatusCode() {
		return resultStatusCode;
	}

	public void setResultStatusCode(int resultStatusCode) {
		this.resultStatusCode = resultStatusCode;
	}

	public String getResultStatusText() {
		return resultStatusText;
	}

	public void setResultStatusText(String resultStatusText) {
		this.resultStatusText = resultStatusText;
	}
}
