package delete.me;

import com.github.reinert.jjschema.Attributes;

import net.ages.alwb.utils.core.datastores.json.models.AbstractModel;

public class UserSelfView extends AbstractModel {
	@Attributes(
			required = true
			, description = "verb for HTTP request"
			, enums = {"GET", "POST", "PUT", "DELETE"}
			)
	String verb = "";
	
	public UserSelfView() {
		super();
		super.setPrettyPrint(true);
	}
	
	public static void main(String[] args) {
		UserSelfView v = new UserSelfView();
		System.out.println(v.toJsonSchema());
	}

	public String getVerb() {
		return verb;
	}

	public void setVerb(String verb) {
		this.verb = verb;
	}
}
