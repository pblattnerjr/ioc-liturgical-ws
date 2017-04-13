package ioc.liturgical.ws.models.ws.response;

import com.google.gson.annotations.Expose;

import ioc.liturgical.ws.annotations.UiWidget;
import ioc.liturgical.ws.constants.Constants;
import net.ages.alwb.utils.core.datastores.json.models.AbstractModel;

public class Login extends AbstractModel {
	@Expose public String username = "";
	
	@UiWidget(Constants.UI_WIDGET_PASSWORD)
	@Expose public String password = "";

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
	
	public static void main(String[] args) {
		AbstractResponse<Login> result = new AbstractResponse<Login>(new Login(), true);
		System.out.println(result.toJsonString());
		
	}

}
