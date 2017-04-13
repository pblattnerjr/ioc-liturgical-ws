package ioc.liturgical.ws.models.db.docs;

import ioc.liturgical.ws.constants.ONTOLOGY_TOPICS;
import ioc.liturgical.ws.models.db.forms.AnimalCreateForm;
import ioc.liturgical.ws.models.db.supers.LTKDbOntologyEntry;

import com.github.reinert.jjschema.Attributes;

/**
 * @author mac002
 *
 */
@Attributes(title = "Animal", description = "This is a doc that records information about an earthly being that is capable of independent motion.")
public class Animal extends LTKDbOntologyEntry {
	
	private static String schema = Animal.class.getSimpleName();
	private static double version = 1.1;
	private static ONTOLOGY_TOPICS ontoTopic = ONTOLOGY_TOPICS.ANIMAL;
	
	public Animal(
			String key
			) {
		super(ontoTopic, schema, version, key);
	}

	public Animal(
			AnimalCreateForm form
			) {
		super(ontoTopic, schema,  version, form);
	}

}
