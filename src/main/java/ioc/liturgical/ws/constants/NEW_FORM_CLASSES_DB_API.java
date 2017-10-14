package ioc.liturgical.ws.constants;

import net.ages.alwb.utils.core.datastores.json.models.AbstractModel;
import ioc.liturgical.ws.models.db.docs.nlp.ConcordanceLine;
import ioc.liturgical.ws.models.db.docs.nlp.PerseusAnalysis;
import ioc.liturgical.ws.models.db.forms.AnimalCreateForm;
import ioc.liturgical.ws.models.db.forms.BeingCreateForm;
import ioc.liturgical.ws.models.db.forms.ConceptCreateForm;
import ioc.liturgical.ws.models.db.forms.EventCreateForm;
import ioc.liturgical.ws.models.db.forms.GroupCreateForm;
import ioc.liturgical.ws.models.db.forms.HumanCreateForm;
import ioc.liturgical.ws.models.db.forms.LinkRefersToAnimalCreateForm;
import ioc.liturgical.ws.models.db.forms.LinkRefersToBeingCreateForm;
import ioc.liturgical.ws.models.db.forms.LinkRefersToBiblicalTextCreateForm;
import ioc.liturgical.ws.models.db.forms.LinkRefersToConceptCreateForm;
import ioc.liturgical.ws.models.db.forms.LinkRefersToEventCreateForm;
import ioc.liturgical.ws.models.db.forms.LinkRefersToGroupCreateForm;
import ioc.liturgical.ws.models.db.forms.LinkRefersToHumanCreateForm;
import ioc.liturgical.ws.models.db.forms.LinkRefersToMysteryCreateForm;
import ioc.liturgical.ws.models.db.forms.LinkRefersToObjectCreateForm;
import ioc.liturgical.ws.models.db.forms.LinkRefersToPlaceCreateForm;
import ioc.liturgical.ws.models.db.forms.LinkRefersToPlantCreateForm;
import ioc.liturgical.ws.models.db.forms.LinkRefersToRoleCreateForm;
import ioc.liturgical.ws.models.db.forms.MysteryCreateForm;
import ioc.liturgical.ws.models.db.forms.ObjectCreateForm;
import ioc.liturgical.ws.models.db.forms.PlaceCreateForm;
import ioc.liturgical.ws.models.db.forms.PlantCreateForm;
import ioc.liturgical.ws.models.db.forms.RoleCreateForm;
import ioc.liturgical.ws.models.db.forms.TextBiblicalSourceCreateForm;
import ioc.liturgical.ws.models.db.forms.TextBiblicalTranslationCreateForm;
import ioc.liturgical.ws.models.db.forms.TextLiturgicalSourceCreateForm;
import ioc.liturgical.ws.models.db.forms.TextLiturgicalTranslationCreateForm;
import ioc.liturgical.ws.models.db.forms.UserNoteCreateForm;
import ioc.liturgical.ws.models.ws.db.WsPaths;

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
	NEW_ANIMAL(
			"Animal"
			, new AnimalCreateForm("")
			, ENDPOINTS_DB_API.DOCS
			, RESTRICTION_FILTERS.ALL_DOMAINS_ADMIN
			)
,	NEW_LINK_REFERS_TO_ANIMAL(
			"doc refers animal"
			, new LinkRefersToAnimalCreateForm("","","")
			, ENDPOINTS_DB_API.LINKS
			, RESTRICTION_FILTERS.ALL_DOMAINS_ADMIN
			)
,	NEW_BEING(
		"Being"
		, new BeingCreateForm("")
		, ENDPOINTS_DB_API.DOCS
		, RESTRICTION_FILTERS.ALL_DOMAINS_ADMIN
		)
,	NEW_LINK_REFERS_TO_BEING(
			"doc refers to being"
			, new LinkRefersToBeingCreateForm("","","")
			, ENDPOINTS_DB_API.LINKS
			, RESTRICTION_FILTERS.ALL_DOMAINS_ADMIN
			)
,	NEW_LINK_REFERS_TO_BIBLICAL_TEXT(
			"doc refers to biblical text"
			, new LinkRefersToBiblicalTextCreateForm("","","")
			, ENDPOINTS_DB_API.LINKS
			, RESTRICTION_FILTERS.ALL_DOMAINS_ADMIN
			)
, NEW_CONCEPT(
		"Concept"
		, new ConceptCreateForm("")
		, ENDPOINTS_DB_API.DOCS
		, RESTRICTION_FILTERS.ALL_DOMAINS_ADMIN
		)
, NEW_CONCORDANCE_LINE(
		"ConcordanceLine"
		, new ConcordanceLine(" ",0,0," "," "," ")
		, ENDPOINTS_DB_API.DOCS
		, RESTRICTION_FILTERS.WS_ADMIN
		)
, NEW_LINK_REFERS_TO_CONCEPT(
		"doc refers to concept"
		, new LinkRefersToConceptCreateForm("","","")
		, ENDPOINTS_DB_API.LINKS
		, RESTRICTION_FILTERS.ALL_DOMAINS_ADMIN
		)
, NEW_EVENT(
		"Event"
		, new EventCreateForm("")
		, ENDPOINTS_DB_API.DOCS
		, RESTRICTION_FILTERS.ALL_DOMAINS_ADMIN
		)
, NEW_LINK_REFERS_TO_EVENT(
		"doc refers to event"
		, new LinkRefersToEventCreateForm("","","")
		, ENDPOINTS_DB_API.LINKS
		, RESTRICTION_FILTERS.ALL_DOMAINS_ADMIN
		)
, NEW_GROUP(
		"Group"
		, new GroupCreateForm("")
		, ENDPOINTS_DB_API.DOCS
		, RESTRICTION_FILTERS.ALL_DOMAINS_ADMIN
		)
, NEW_LINK_REFERS_TO_GROUP(
		"doc refers to group"
		, new LinkRefersToGroupCreateForm("","","")
		, ENDPOINTS_DB_API.LINKS
		, RESTRICTION_FILTERS.ALL_DOMAINS_ADMIN
		)
, NEW_HUMAN(
		"Human"
		, new HumanCreateForm("")
		, ENDPOINTS_DB_API.DOCS
		, RESTRICTION_FILTERS.ALL_DOMAINS_ADMIN
		)
, NEW_LINK_REFERS_TO_HUMAN(
		"doc refers to human"
		, new LinkRefersToHumanCreateForm("","","")
		, ENDPOINTS_DB_API.LINKS
		, RESTRICTION_FILTERS.ALL_DOMAINS_ADMIN
		)
, NEW_MYSTERY(
		"Mystery"
		, new MysteryCreateForm("")
		, ENDPOINTS_DB_API.DOCS
		, RESTRICTION_FILTERS.ALL_DOMAINS_ADMIN
		)
, NEW_LINK_REFERS_TO_MYSTERY(
		"doc refers to mystery"
		, new LinkRefersToMysteryCreateForm("","","")
		, ENDPOINTS_DB_API.LINKS
		, RESTRICTION_FILTERS.ALL_DOMAINS_ADMIN
		)
, NEW_NOTE_USER(
		"Note (private)"
		, new UserNoteCreateForm(" ", " ", " ")
		, ENDPOINTS_DB_API.NOTES
		, RESTRICTION_FILTERS.NONE
		)
, NEW_OBJECT(
		"Object"
		, new ObjectCreateForm("")
		, ENDPOINTS_DB_API.DOCS
		, RESTRICTION_FILTERS.ALL_DOMAINS_ADMIN
		)
, NEW_LINK_REFERS_TO_OBJECT(
		"doc refers to object"
		, new LinkRefersToObjectCreateForm("","","")
		, ENDPOINTS_DB_API.LINKS
		, RESTRICTION_FILTERS.ALL_DOMAINS_ADMIN
		)
, NEW_PERSEUS_ANALYSIS(
		"Perseus Analysis"
		, new PerseusAnalysis()
		, ENDPOINTS_DB_API.WORD_ANALYSIS
		, RESTRICTION_FILTERS.WS_ADMIN
		)
, NEW_PLACE(
		"Place"
		, new PlaceCreateForm("")
		, ENDPOINTS_DB_API.DOCS
		, RESTRICTION_FILTERS.ALL_DOMAINS_ADMIN
		)
, NEW_LINK_REFERS_TO_PLACE(
		"doc refers to place"
		, new LinkRefersToPlaceCreateForm("","","")
		, ENDPOINTS_DB_API.LINKS
		, RESTRICTION_FILTERS.ALL_DOMAINS_ADMIN
		)
, NEW_PLANT(
		"Plant"
		, new PlantCreateForm("")
		, ENDPOINTS_DB_API.DOCS
		, RESTRICTION_FILTERS.ALL_DOMAINS_ADMIN
		)
, NEW_LINK_REFERS_TO_PLANT(
		"doc refers to plant"
		, new LinkRefersToPlantCreateForm("","","")
		, ENDPOINTS_DB_API.LINKS
		, RESTRICTION_FILTERS.ALL_DOMAINS_ADMIN
		)
, NEW_ROLE(
		"Role"
		, new RoleCreateForm("")
		, ENDPOINTS_DB_API.DOCS
		, RESTRICTION_FILTERS.ALL_DOMAINS_ADMIN
		)
, NEW_LINK_REFERS_TO_ROLE(
		"doc refers to role"
		, new LinkRefersToRoleCreateForm("","","")
		, ENDPOINTS_DB_API.LINKS
		, RESTRICTION_FILTERS.ALL_DOMAINS_ADMIN
		)
, NEW_TEXT_BIBLICAL_SOURCE(
		"Biblical Text (Source)"
		, new TextBiblicalSourceCreateForm("","","")
		, ENDPOINTS_DB_API.DOCS
		, RESTRICTION_FILTERS.ALL_DOMAINS_ADMIN
		)
, NEW_TEXT_BIBLICAL_TRANSLATION(
		"Biblical Text (Translation)"
		, new TextBiblicalTranslationCreateForm("","","")
		, ENDPOINTS_DB_API.DOCS
		, RESTRICTION_FILTERS.ALL_DOMAINS_ADMIN
		)
, NEW_TEXT_LITURGICAL_TRANSLATION(
		"Liturgical Text (Translation)"
		, new TextLiturgicalTranslationCreateForm("","","")
		, ENDPOINTS_DB_API.DOCS
		, RESTRICTION_FILTERS.ALL_DOMAINS_ADMIN
		)
, NEW_TEXT_LITURGICAL_SOURCE(
		"Liturgical Text (Source)"
		, new TextLiturgicalSourceCreateForm("","","")
		, ENDPOINTS_DB_API.DOCS
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
	
	public WsPaths toEndpointPaths() {
		WsPaths result = new WsPaths();
		result.setGet(this.toGetPath());
		result.setPost(this.toPostPath());
		result.setPut(this.toPutPath());
		return result;
	}
	
	/**
	 * Get the path to use for handling an http get
	 * @return
	 */
	public String toGetPath() {
		return this.endpoint.pathname;
	}
	/**
	 * Get the path to use for handling an http post
	 * @return
	 */
	public String toPostPath() {
		return this.endpoint.pathname;
	}
	/**
	 * Get the path to use for handling an http put
	 * @return
	 */
	public String toPutPath() {
		return this.endpoint.toLibraryTopicKeyPath();
	}
	
	/**
	 * Finds the enum whose object matches the parameter
	 * and returns the post path for that object.
	 * @param m
	 * @return
	 */
	public static WsPaths getEndpointPathsForAbstractModel(Class<? extends AbstractModel> class1) {
		WsPaths result = new WsPaths();
		for (NEW_FORM_CLASSES_DB_API f : NEW_FORM_CLASSES_DB_API.values()) {
			if (f.obj.getClass().equals(class1)) {
				result = f.toEndpointPaths();
				break;
			}
		}
		return result;
	}
	
}
