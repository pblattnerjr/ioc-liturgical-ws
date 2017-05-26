package ioc.liturgical.ws.constants;

import com.google.gson.Gson;

import ioc.liturgical.ws.models.db.returns.LTKVString;
import ioc.liturgical.ws.models.ws.db.Domain;
import ioc.liturgical.ws.models.ws.db.Label;
import ioc.liturgical.ws.models.ws.db.User;
import ioc.liturgical.ws.models.ws.db.UserAuth;
import ioc.liturgical.ws.models.ws.db.UserContact;
import ioc.liturgical.ws.models.ws.db.UserHash;
import ioc.liturgical.ws.models.ws.db.UserStatistics;
import ioc.liturgical.ws.models.ws.db.Utility;
import ioc.liturgical.ws.models.ws.forms.AuthorizationCreateForm;
import ioc.liturgical.ws.models.ws.forms.DomainCreateForm;
import ioc.liturgical.ws.models.ws.forms.LabelCreateForm;
import ioc.liturgical.ws.models.ws.forms.UserCreateForm;
import ioc.liturgical.ws.models.ws.forms.UserPasswordChangeForm;
import ioc.liturgical.ws.models.ws.response.LiturgicalDayPropertiesForm;
import ioc.liturgical.ws.models.ws.response.Login;
import net.ages.alwb.utils.core.datastores.json.models.AbstractModel;

/**
 * Enumerates classes that have schemas for storing in the database
 * @author mac002
 *
 */
public enum INTERNAL_DB_SCHEMA_CLASSES {
	ADMIN_NEW(new AuthorizationCreateForm())
	, DOMAIN_NEW(new DomainCreateForm())
	, DOMAIN(new Domain())
	, LABEL(new Label())
	, LABEL_NEW(new LabelCreateForm())
	, LITURGICAL_DAY_PROPERTIES_FORM(new LiturgicalDayPropertiesForm())
	, LOGIN(new Login())
	, LTKVSTRING(new LTKVString())
	, USER(new User())
	, USER_AUTH(new UserAuth())
	, USER_CONTACT(new UserContact())
	, USER_HASH(new UserHash())
	, USER_PASSWORD_CHANGE(new UserPasswordChangeForm())
	, USER_NEW(new UserCreateForm())
	, USER_STATISTICS(new UserStatistics())
	, UTILITY(new Utility())
	;

	public AbstractModel obj;
	
	private INTERNAL_DB_SCHEMA_CLASSES(
			 AbstractModel obj
			) {
		this.obj = obj;
	}

	public static AbstractModel modelForSchemaName(String name) {
		for (INTERNAL_DB_SCHEMA_CLASSES s : INTERNAL_DB_SCHEMA_CLASSES.values()) {
			if (s.obj.schemaIdAsString().equals(name)) {
				return s.obj;
			}
		}
		return null;
	}
	
	public static INTERNAL_DB_SCHEMA_CLASSES classForSchemaName(String name) {
		for (INTERNAL_DB_SCHEMA_CLASSES s : INTERNAL_DB_SCHEMA_CLASSES.values()) {
			if (s.obj.schemaIdAsString().equals(name)) {
				return s;
			}
		}
		return null;
	}

}
