package ioc.liturgical.ws.constants;

import ioc.liturgical.ws.models.db.forms.LinkRefersToBiblicalTextCreateForm;
import net.ages.alwb.utils.core.datastores.json.models.AbstractModel;

/**
 * Enumerates classes that are a form for creating a new doc.
 * 
 *  The restriction filters are applied by the WsDatastoreManager.getNewDocForms() method
 *  when a forms list is requested by a user.  
 *  
 *  If the form restriction is WS_ADMIN,
 * then only a user that is a web service admin is allowed to use it.
 * 
 * If it is restricted to ALL_DOMAINS_ADMIN, then only someone with
 * that authorization or higher (i.e. a WS_ADMIN) can use it.
 * 
 * If it is restricted to DOMAIN_ADMIN, then only someone with that
 * authorization or higher (i.e. ALL_DOMAINS_ADMIN, WS_ADMIN) can use it.
 * 
 * 
 * @author mac002
 *
 */
public enum NEW_FORM_CLASSES_DB_API {
	NEW_LINK_REFERS_TO_BIBLICAL_TEXT(
			"reference"
			, new LinkRefersToBiblicalTextCreateForm("","","")
			, ENDPOINTS_DB_API.LINK_REFERS_TO_BIBLICAL_TEXT
			, RESTRICTION_FILTERS.ALL_DOMAINS_ADMIN
			)
	;

	public AbstractModel obj;
	public ENDPOINTS_DB_API endpoint;
	public String name;
	public RESTRICTION_FILTERS restriction;
	
	/**
	 * 
	 * @param name of the form
	 * @param obj an instance of the form
	 * @param endpoint that is used with this form
	 * @param restriction what type of role is allowed to use this form
	 */
	private NEW_FORM_CLASSES_DB_API(
			String name
			 , AbstractModel obj
			 , ENDPOINTS_DB_API endpoint
			 , RESTRICTION_FILTERS restriction
			) {
		this.name = name;
		this.obj = obj;
		this.endpoint = endpoint;
		this.restriction = restriction;
	}
	
	/**
	 * Get the path to use for handling an http post
	 * @return
	 */
	public String toPostPath() {
		return this.endpoint.toLibraryPath() + "/" + this.name;
	}
	
}
