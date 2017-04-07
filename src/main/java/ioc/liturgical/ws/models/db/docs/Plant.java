package ioc.liturgical.ws.models.db.docs;

import com.google.gson.annotations.Expose;

import ioc.liturgical.ws.annotations.UiWidget;
import ioc.liturgical.ws.constants.Constants;
import ioc.liturgical.ws.constants.ONTOLOGY_TOPICS;
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
	private static ONTOLOGY_TOPICS ontoTopic = ONTOLOGY_TOPICS.PLANT;
	
	public Plant(
			String key
			) {
		super(ontoTopic, schema, version, key);
	}

	public Plant(
			PlantCreateForm form
			) {
		super(ontoTopic, schema,  version, form);
	}

}
