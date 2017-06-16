package ioc.liturgical.ws.models.db.docs.ontology;

import com.google.gson.annotations.Expose;

import ioc.liturgical.ws.annotations.UiWidget;
import ioc.liturgical.ws.constants.Constants;
import ioc.liturgical.ws.constants.ID_PART_TYPES;
import ioc.liturgical.ws.constants.db.external.TOPICS;
import ioc.liturgical.ws.models.db.forms.PlantCreateForm;
import ioc.liturgical.ws.models.db.supers.LTKDbOntologyEntry;

import com.github.reinert.jjschema.Attributes;

/**
 * @author mac002
 *
 */
@Attributes(title = "Plant", description = "This is a doc that records information about an earthly being that is not capable of independent motion.")
public class Plant extends LTKDbOntologyEntry {
	
	private static String schema = Plant.class.getSimpleName();
	private static double version = 1.1;
	private static TOPICS ontoTopic = TOPICS.PLANT;
	
	public Plant(
			String key
			) {
		super(ontoTopic, schema, version, key);
		this.partTypeOfTopic = ID_PART_TYPES.ONTOLOGY_TOPIC;
		this.partTypeOfKey = ID_PART_TYPES.USER_TEXT;
	}
	
	public Plant(
			String key
			, String name
			) {
		super(ontoTopic, schema, version, key);
		this.name = name;
		this.partTypeOfTopic = ID_PART_TYPES.ONTOLOGY_TOPIC;
		this.partTypeOfKey = ID_PART_TYPES.USER_TEXT;
	}


	public Plant(
			PlantCreateForm form
			) {
		super(ontoTopic, schema,  version, form);
	}

}
