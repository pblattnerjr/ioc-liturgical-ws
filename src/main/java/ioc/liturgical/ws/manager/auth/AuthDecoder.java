package ioc.liturgical.ws.manager.auth;

import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ages.alwb.utils.core.error.handling.ErrorUtils;

public class AuthDecoder {
	private static final Logger logger = LoggerFactory.getLogger(AuthDecoder.class);

	private String username = "";
	private String password = "";
	
	public AuthDecoder(String auth) {
		try {
			if (auth != null) {
				String data = auth.substring(auth.indexOf(" ") +1 );
			    
				   // Decode the data back to original string
				   byte[] bytes = Base64.getDecoder().decode(data);
				  String decoded = new String(bytes);
				  String[] parts = decoded.split(":");
				  if (parts.length > 1) {
					  username = parts[0];
					  password = parts[1];
				  }
			}
		} catch (Exception e) {
			ErrorUtils.report(logger, e);
		}
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
