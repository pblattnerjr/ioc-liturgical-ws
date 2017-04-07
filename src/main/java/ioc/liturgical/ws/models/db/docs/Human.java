package ioc.liturgical.ws.models.db.docs;

import ioc.liturgical.ws.constants.ONTOLOGY_TOPICS;
import ioc.liturgical.ws.models.db.forms.HumanCreateForm;
import ioc.liturgical.ws.models.db.supers.LTKDbOntologyEntry;

import com.github.reinert.jjschema.Attributes;

/**
 * @author mac002
 *
 */
@Attributes(title = "Human", description = "This is a doc that records information about a human being.")
public class Human extends LTKDbOntologyEntry {
	
	private static String schema = Human.class.getSimpleName();
	private static double version = 1.1;
	private static ONTOLOGY_TOPICS ontoTopic = ONTOLOGY_TOPICS.HUMAN;
	
	public Human(
			String key
			) {
		super(ontoTopic, schema, version, key);
	}

	public Human(
			HumanCreateForm form
			) {
		super(ontoTopic, schema,  version, form);
	}

}
