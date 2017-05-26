package ioc.liturgical.ws.models.db.docs.ontology;

import ioc.liturgical.ws.constants.GENDERS;
import ioc.liturgical.ws.constants.ID_PART_TYPES;
import ioc.liturgical.ws.constants.ONTOLOGY_TOPICS;
import ioc.liturgical.ws.models.db.forms.HumanCreateForm;
import ioc.liturgical.ws.models.db.supers.LTKDbOntologyEntry;

import com.github.reinert.jjschema.Attributes;
import com.google.gson.annotations.Expose;

/**
 * @author mac002
 *
 */
@Attributes(title = "Human", description = "This is a doc that records information about a human being.")
public class Human extends LTKDbOntologyEntry {
	
	private static String schema = Human.class.getSimpleName();
	private static double version = 1.1;
	private static ONTOLOGY_TOPICS ontoTopic = ONTOLOGY_TOPICS.HUMAN;
	
	@Attributes(required = true, description = "Gender")
	@Expose public GENDERS gender = GENDERS.unknown;

	public Human(
			String key
			) {
		super(ontoTopic, schema, version, key);
	}
	
	public Human(
			String key
			, String name
			) {
		super(ontoTopic, schema, version, key);
		this.name = name;
		this.partTypeOfTopic = ID_PART_TYPES.ONTOLOGY_TOPIC;
		this.partTypeOfKey = ID_PART_TYPES.USER_TEXT;
	}


	public Human(
			HumanCreateForm form
			) {
		super(ontoTopic, schema,  version, form);
		this.partTypeOfTopic = ID_PART_TYPES.ONTOLOGY_TOPIC;
		this.partTypeOfKey = ID_PART_TYPES.USER_TEXT;
	}

	public GENDERS getGender() {
		return gender;
	}

	public void setGender(GENDERS gender) {
		this.gender = gender;
	}

}
