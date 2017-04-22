package ioc.liturgical.ws.constants;

import net.ages.alwb.utils.core.datastores.json.models.AbstractModel;

import ioc.liturgical.ws.models.db.forms.LinkRefersToAnimalCreateForm;
import ioc.liturgical.ws.models.db.forms.LinkRefersToBeingCreateForm;
import ioc.liturgical.ws.models.db.forms.LinkRefersToBiblicalTextCreateForm;
import ioc.liturgical.ws.models.db.forms.LinkRefersToConceptCreateForm;
import ioc.liturgical.ws.models.db.forms.LinkRefersToEventCreateForm;
import ioc.liturgical.ws.models.db.forms.LinkRefersToGroupCreateForm;
import ioc.liturgical.ws.models.db.forms.LinkRefersToHumanCreateForm;
import ioc.liturgical.ws.models.db.forms.LinkRefersToObjectCreateForm;
import ioc.liturgical.ws.models.db.forms.LinkRefersToPlaceCreateForm;
import ioc.liturgical.ws.models.db.forms.LinkRefersToPlantCreateForm;
import ioc.liturgical.ws.models.db.forms.LinkRefersToRoleCreateForm;

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
	NEW_LINK_REFERS_TO_ANIMAL(
			"doc refers animal"
			, new LinkRefersToAnimalCreateForm("","","")
			, ENDPOINTS_DB_API.LINK_REFERS_TO_ANIMAL
			, RESTRICTION_FILTERS.ALL_DOMAINS_ADMIN
			)
,	NEW_LINK_REFERS_TO_BEING(
			"doc refers to being"
			, new LinkRefersToBeingCreateForm("","","")
			, ENDPOINTS_DB_API.LINK_REFERS_TO_BEING
			, RESTRICTION_FILTERS.ALL_DOMAINS_ADMIN
			)
,	NEW_LINK_REFERS_TO_BIBLICAL_TEXT(
			"doc refers to biblical text"
			, new LinkRefersToBiblicalTextCreateForm("","","")
			, ENDPOINTS_DB_API.LINK_REFERS_TO_BIBLICAL_TEXT
			, RESTRICTION_FILTERS.ALL_DOMAINS_ADMIN
			)
, NEW_LINK_REFERS_TO_CONCEPT(
		"doc refers to concept"
		, new LinkRefersToConceptCreateForm("","","")
		, ENDPOINTS_DB_API.LINK_REFERS_TO_CONCEPT
		, RESTRICTION_FILTERS.ALL_DOMAINS_ADMIN
		)
, NEW_LINK_REFERS_TO_EVENT(
		"doc refers to event"
		, new LinkRefersToEventCreateForm("","","")
		, ENDPOINTS_DB_API.LINK_REFERS_TO_EVENT
		, RESTRICTION_FILTERS.ALL_DOMAINS_ADMIN
		)
, NEW_LINK_REFERS_TO_GROUP(
		"doc refers to group"
		, new LinkRefersToGroupCreateForm("","","")
		, ENDPOINTS_DB_API.LINK_REFERS_TO_GROUP
		, RESTRICTION_FILTERS.ALL_DOMAINS_ADMIN
		)
, NEW_LINK_REFERS_TO_HUMAN(
		"doc refers to human"
		, new LinkRefersToHumanCreateForm("","","")
		, ENDPOINTS_DB_API.LINK_REFERS_TO_HUMAN
		, RESTRICTION_FILTERS.ALL_DOMAINS_ADMIN
		)
, NEW_LINK_REFERS_TO_OBJECT(
		"doc refers to object"
		, new LinkRefersToObjectCreateForm("","","")
		, ENDPOINTS_DB_API.LINK_REFERS_TO_OBJECT
		, RESTRICTION_FILTERS.ALL_DOMAINS_ADMIN
		)
, NEW_LINK_REFERS_TO_PLACE(
		"doc refers to place"
		, new LinkRefersToPlaceCreateForm("","","")
		, ENDPOINTS_DB_API.LINK_REFERS_TO_PLACE
		, RESTRICTION_FILTERS.ALL_DOMAINS_ADMIN
		)
, NEW_LINK_REFERS_TO_PLANT(
		"doc refers to plant"
		, new LinkRefersToPlantCreateForm("","","")
		, ENDPOINTS_DB_API.LINK_REFERS_TO_PLANT
		, RESTRICTION_FILTERS.ALL_DOMAINS_ADMIN
		)
, NEW_LINK_REFERS_TO_ROLE(
		"doc refers to role"
		, new LinkRefersToRoleCreateForm("","","")
		, ENDPOINTS_DB_API.LINK_REFERS_TO_PLACE
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
