package ioc.liturgical.ws.models.ws.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.annotations.Expose;

import net.ages.alwb.utils.core.datastores.json.models.AbstractModel;

/**
 * Used to create Json that provides the webservice paths (endpoints)
 * for an HTTP GET, POST, and PUT
 * 
 * @author mac002
 *
 */
public class WsPaths extends AbstractModel {
	private static final Logger logger = LoggerFactory.getLogger(WsPaths.class);

	@Expose public String get;
	@Expose public String post;
	@Expose public String put;
	
	public WsPaths() {
		super();
	}

	public WsPaths(
			String get
			, String post
			, String put
			) {
		super();
		this.get = get;
		this.post = post;
		this.put = put;
	}

	public String getGet() {
		return get;
	}

	public void setGet(String get) {
		this.get = get;
	}

	public String getPost() {
		return post;
	}

	public void setPost(String post) {
		this.post = post;
	}

	public String getPut() {
		return put;
	}

	public void setPut(String put) {
		this.put = put;
	}

}
