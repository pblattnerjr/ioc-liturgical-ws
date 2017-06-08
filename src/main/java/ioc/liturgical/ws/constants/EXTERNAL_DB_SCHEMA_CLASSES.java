package ioc.liturgical.ws.constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import ioc.liturgical.ws.models.db.docs.nlp.ConcordanceLine;
import ioc.liturgical.ws.models.db.docs.nlp.PerseusAnalysis;
import ioc.liturgical.ws.models.db.docs.nlp.WordInflected;
import ioc.liturgical.ws.models.db.docs.ontology.Animal;
import ioc.liturgical.ws.models.db.docs.ontology.Being;
import ioc.liturgical.ws.models.db.docs.ontology.Concept;
import ioc.liturgical.ws.models.db.docs.ontology.Event;
import ioc.liturgical.ws.models.db.docs.ontology.Group;
import ioc.liturgical.ws.models.db.docs.ontology.Human;
import ioc.liturgical.ws.models.db.docs.ontology.Mystery;
import ioc.liturgical.ws.models.db.docs.ontology.Object;
import ioc.liturgical.ws.models.db.docs.ontology.Place;
import ioc.liturgical.ws.models.db.docs.ontology.Plant;
import ioc.liturgical.ws.models.db.docs.ontology.Role;
import ioc.liturgical.ws.models.db.docs.ontology.TextBiblical;
import ioc.liturgical.ws.models.db.docs.ontology.TextLiturgical;
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
import ioc.liturgical.ws.models.db.links.LinkRefersToAnimal;
import ioc.liturgical.ws.models.db.links.LinkRefersToBeing;
import ioc.liturgical.ws.models.db.links.LinkRefersToBiblicalText;
import ioc.liturgical.ws.models.db.links.LinkRefersToConcept;
import ioc.liturgical.ws.models.db.links.LinkRefersToEvent;
import ioc.liturgical.ws.models.db.links.LinkRefersToGroup;
import ioc.liturgical.ws.models.db.links.LinkRefersToHuman;
import ioc.liturgical.ws.models.db.links.LinkRefersToMystery;
import ioc.liturgical.ws.models.db.links.LinkRefersToObject;
import ioc.liturgical.ws.models.db.links.LinkRefersToPlace;
import ioc.liturgical.ws.models.db.links.LinkRefersToPlant;
import ioc.liturgical.ws.models.db.links.LinkRefersToRole;
import ioc.liturgical.ws.models.db.supers.LTK;
import ioc.liturgical.ws.models.db.supers.LTKDb;
import ioc.liturgical.ws.models.db.supers.LTKDbOntologyEntry;
import ioc.liturgical.ws.models.db.supers.LTKLink;
import net.ages.alwb.utils.core.datastores.json.models.DropdownItem;
import net.ages.alwb.utils.core.datastores.json.models.ModelHelpers;

/**
 * Enumerates classes that have schemas for storing in the database
 * @author mac002
 *
 */
public enum EXTERNAL_DB_SCHEMA_CLASSES {
	ANIMAL(
			new AnimalCreateForm(" ")
			, new Animal(" ")
			)
	, BEING(
			new BeingCreateForm(" ")
			, new Being(" ")
			)
	, CONCORDANCE_LINE(
			new ConcordanceLine(" ",0,0," "," "," ")
			, new ConcordanceLine(" ",0,0," "," "," ")
			)
	, CONCEPT(
			new ConceptCreateForm(" ")
			, new Concept(" ")
			)
	, EVENT(
			new EventCreateForm(" ")
			, new Event(" ")
			)
	, GROUP(
			new GroupCreateForm(" ")
			, new Group(" ")
			)
	, HUMAN(
			new HumanCreateForm(" ")
			, new Human(" ")
			)
	, LEXICAL_FORM(
			new WordInflected("", 0)
			, new WordInflected("", 0)
			)
	, LINK_REFERS_TO_ANIMAL(
			new LinkRefersToAnimalCreateForm(" "," ","")
			, new LinkRefersToAnimal(" "," ","")
			)
	, LINK_REFERS_TO_BEING(
			new LinkRefersToBeingCreateForm(" "," ","")
			, new LinkRefersToBeing(" "," ","")
			)
	, LINK_REFERS_TO_BIBLICAL_TEXT(
			new LinkRefersToBiblicalTextCreateForm(" "," ","")
			, new LinkRefersToBiblicalText(" "," ","")
			)
	, LINK_REFERS_TO_CONCEPT(
			new LinkRefersToConceptCreateForm(" "," ","")
			, new LinkRefersToConcept(" "," ","")
			)
	, LINK_REFERS_TO_EVENT(
			new LinkRefersToEventCreateForm(" "," ","")
			, new LinkRefersToEvent(" "," ","")
			)
	, LINK_REFERS_TO_GROUP(
			new LinkRefersToGroupCreateForm(" "," ","")
			, new LinkRefersToGroup(" "," ","")
			)
	, LINK_REFERS_TO_HUMAN(
			new LinkRefersToHumanCreateForm(" "," ","")
			, new LinkRefersToHuman(" "," ","")
			)
	, LINK_REFERS_TO_MYSTERY(
			new LinkRefersToMysteryCreateForm(" "," ","")
			, new LinkRefersToMystery(" "," ","")
			)
	, LINK_REFERS_TO_OBJECT(
			new LinkRefersToObjectCreateForm(" "," ","")
			, new LinkRefersToObject(" "," ","")
			)
	, LINK_REFERS_TO_PLACE(
			new LinkRefersToPlaceCreateForm(" "," ","")
			, new LinkRefersToPlace(" "," ","")
			)
	, LINK_REFERS_TO_PLANT(
			new LinkRefersToPlantCreateForm(" "," ","")
			, new LinkRefersToPlant(" "," ","")
			)
	, LINK_REFERS_TO_ROLE(
			new LinkRefersToRoleCreateForm(" "," ","")
			, new LinkRefersToRole(" "," ","")
			)
	, MYSTERY(
			new MysteryCreateForm(" ")
			, new Mystery(" ")
			)
	, OBJECT(
			new ObjectCreateForm(" ")
			, new Object(" ")
			)
	, PERSEUS_ANALYSIS(
			new PerseusAnalysis()
			, new PerseusAnalysis()
			)
	, PLACE(
			new PlaceCreateForm(" ")
			, new Place(" ")
			)
	, PLANT(
			new PlantCreateForm(" ")
			, new Plant(" ")
			)
	, ROLE(
			new RoleCreateForm(" ")
			, new Role(" ")
			)
	, TEXT_BIBLICAL_SOURCE(
			new TextBiblicalSourceCreateForm(" "," ", " ")
			, new TextBiblical(" "," ", " ")
			)
	, TEXT_BIBLICAL_TRANSLATION(
			new TextBiblicalTranslationCreateForm(" "," ", " ")
			, new TextBiblical(" "," ", " ")
			)
	, TEXT_LITURGICAL_SOURCE(
			new TextLiturgicalSourceCreateForm(" "," ", " ")
			, new TextLiturgical(" "," ", " ")
			)
	, TEXT_LITURGICAL_TRANSLATION(
			new TextLiturgicalTranslationCreateForm(" "," ", " ")
			, new TextLiturgical(" "," ", " ")
			)
	;

	public LTK ltk;
	public LTKDb ltkDb;
	
	private static Gson gson = new Gson();
	
	/**
	 * 
	 * @param ltk  - used as the CreateForm supertype
	 * @param ltkDb - used as the database supertype
	 * @param referenceType
	 */
	private EXTERNAL_DB_SCHEMA_CLASSES(
			 LTK ltk
			 , LTKDb ltkDb
			) {
		this.ltk = ltk;
		this.ltkDb = ltkDb;
	}

	/**
	 * Gets the LTK for this schema, whether the schema name provided is for the LTK or the LTKDb
	 * @param name
	 * @return
	 */
	public static LTK ltkForSchemaName(String name) {
		for (EXTERNAL_DB_SCHEMA_CLASSES s : EXTERNAL_DB_SCHEMA_CLASSES.values()) {
			if (s.ltk.schemaIdAsString().equals(name) || s.ltkDb.schemaIdAsString().equals(name)) {
				return s.ltk;
			}
		}
		return null;
	}
	
	/**
	 * Gets the LTKDb for this schema, whether the schema name provided is for the LTK or the LTKDb
	 * @param name
	 * @return
	 */
	public static LTKDb ltkDbForSchemaName(String name) {
		for (EXTERNAL_DB_SCHEMA_CLASSES s : EXTERNAL_DB_SCHEMA_CLASSES.values()) {
			if (s.ltk.schemaIdAsString().equals(name) || s.ltkDb.schemaIdAsString().equals(name)) {
				return s.ltkDb;
			}
		}
		return null;
	}
	/**
	 * Validate the json using this schema.
	 * The method will test the schema to see if it is
	 * an LTK or LTKDb and use the appropriate class.
	 * @param json
	 * @return results of the validation.  It's length will be zero if there are no problems.
	 */
	public static String validate(String json) {
		String result = "";
		try {
			LTK ltk = gson.fromJson(json, LTK.class);
			EXTERNAL_DB_SCHEMA_CLASSES theClass = classForSchemaName(ltk.get_valueSchemaId());
			if (ltk.get_valueSchemaId().contains("CreateForm")) {
				result = theClass.ltk.validate(json);
			} else {
				result = theClass.ltkDb.validate(json);
			}
		} catch (Exception e) {
			result = e.getMessage();
		}
		return result;
	}
	
	/**
	 * Validate the json using this ltk schema
	 * @param json
	 * @return results of the validation.  It's length will be zero if there are no problems.
	 */
	public String validateLtk(String json) {
		this.ltk.validate(json);
		return "";
	}
	
	/**
	 * Validate the json using this ltkDb schema
	 * @param json
	 * @return results of the validation.  It's length will be zero if there are no problems.
	 */
	public String validateLtkDb(String json) {
		this.ltkDb.validate(json);
		return "";
	}

	/**
	 * Is the json for this class for a CreateForm?
	 * @param json
	 * @return
	 */
	public static boolean classIsCreateForm(String json) {
		boolean result = false;
		try {
			LTK ltk = gson.fromJson(json, LTK.class);
			result = ltk.get_valueSchemaId().toLowerCase().contains("createform");
		} catch (Exception e) {
			result = false;
		}
		return result;
	}
	
	public static EXTERNAL_DB_SCHEMA_CLASSES classForSchemaName(String name) {
		for (EXTERNAL_DB_SCHEMA_CLASSES s : EXTERNAL_DB_SCHEMA_CLASSES.values()) {
			if (s.ltk.schemaIdAsString().equals(name) || s.ltkDb.schemaIdAsString().equals(name)) {
				return s;
			}
		}
		return null;
	}
	
	public LTK getLtkFromJson(String json) {
		LTK result = null;
		try {
			result = gson.fromJson(
					json
					, this.ltk.getClass())
			;
		} catch (Exception e) {
			result = null;
		}
		return result;
	}
	
	/**
	 * Get a list of the relationship types used by the Schema classes
	 * @return
	 */
	public static List<RELATIONSHIP_TYPES> getRelationshipTypes() {
		List<RELATIONSHIP_TYPES> result = new ArrayList<RELATIONSHIP_TYPES>();
		for (EXTERNAL_DB_SCHEMA_CLASSES s : EXTERNAL_DB_SCHEMA_CLASSES.values()) {
			if (s.ltkDb instanceof ioc.liturgical.ws.models.db.supers.LTKLink) {
				LTKLink link = (LTKLink) s.ltkDb;
				if (! result.contains(link.getType())) {
					result.add(link.getType());
				}
			}
		}
		return result;
	}

	/**
	 * Get a map where:
	 *    relationship typename is the key
	 *    value is a list of properties used for that relationship type
	 * @return
	 */
	public static Map<String,List<String>> relationshipPropertyMap() {
		Map<String, List<String>> result = new TreeMap<String,List<String>>();
		for (EXTERNAL_DB_SCHEMA_CLASSES s : EXTERNAL_DB_SCHEMA_CLASSES.values()) {
			if (s.ltkDb instanceof ioc.liturgical.ws.models.db.supers.LTKLink) {
				LTKLink link = (LTKLink) s.ltkDb;
					result.put(
							link.getType().typename
							, ModelHelpers.getPropertiesList(link)
							);
			}
		}
		return result;
	}

	/**
	 * Creates a Json Object with keys that are the typename and
	 * values that are a JsonArray of the property names for the
	 * schema assocated with that typename.
	 * @return
	 */
	public static JsonObject ontologyPropertyJson() {
		JsonObject result = new JsonObject();
		JsonArray anyProps = new JsonArray();
		anyProps.add(new DropdownItem("Any","*").toJsonObject());
		anyProps.add(new DropdownItem("id","id").toJsonObject());
		result.add("*", anyProps);
		for (EXTERNAL_DB_SCHEMA_CLASSES s : EXTERNAL_DB_SCHEMA_CLASSES.values()) {
			if (s.ltkDb instanceof ioc.liturgical.ws.models.db.supers.LTKDbOntologyEntry) {
				LTKDbOntologyEntry entry = (LTKDbOntologyEntry) s.ltkDb;
				result.add(
						entry.getTopic()
						, ModelHelpers.getPropertiesAsDropdownItems(entry)
				);
			}
		}
		return result;
	}

	public static JsonArray ontologyTypesJson() {
		JsonArray result = new JsonArray();
		result.add(new DropdownItem("Any","*").toJsonObject());
		for (EXTERNAL_DB_SCHEMA_CLASSES s : EXTERNAL_DB_SCHEMA_CLASSES.values()) {
			if (s.ltkDb instanceof ioc.liturgical.ws.models.db.supers.LTKDbOntologyEntry) {
				LTKDbOntologyEntry entry = (LTKDbOntologyEntry) s.ltkDb;
				result.add(new DropdownItem(entry.getTopic()).toJsonObject());
			}
		}
		return result;
	}

	/**
	 * Creates a Json Object with keys that are the typename and
	 * values that are a JsonArray of the property names for the
	 * schema assocated with that typename.
	 * @return
	 */
	public static JsonObject relationshipPropertyJson() {
		JsonObject result = new JsonObject();
		JsonArray anyProps = new JsonArray();
		anyProps.add(new DropdownItem("Any","*").toJsonObject());
		anyProps.add(new DropdownItem("id","id").toJsonObject());
		result.add("*", anyProps);
		for (EXTERNAL_DB_SCHEMA_CLASSES s : EXTERNAL_DB_SCHEMA_CLASSES.values()) {
			if (s.ltkDb instanceof ioc.liturgical.ws.models.db.supers.LTKLink) {
				LTKLink link = (LTKLink) s.ltkDb;
				result.add(
						link.getType().typename
						, ModelHelpers.getPropertiesAsDropdownItems(link)
				);
			}
		}
		return result;
	}


}
