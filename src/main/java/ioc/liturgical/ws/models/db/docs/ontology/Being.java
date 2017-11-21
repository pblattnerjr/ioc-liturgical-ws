package ioc.liturgical.ws.models.db.docs.ontology;

import ioc.liturgical.ws.constants.ID_PART_TYPES;
import ioc.liturgical.ws.constants.db.external.TOPICS;
import ioc.liturgical.ws.models.db.forms.BeingCreateForm;
import ioc.liturgical.ws.models.db.supers.LTKDbOntologyEntry;

import com.github.reinert.jjschema.Attributes;

/**
 * @author mac002
 *
 */
@Attributes(title = "Being", description = "This is a doc that records information about a being, e.g. that is neither a human, animal, or plant.")
public class Being extends LTKDbOntologyEntry {
	
	private static String schema = Being.class.getSimpleName();
	private static double version = 1.1;
	private static TOPICS ontoTopic = TOPICS.BEING;
	
	public Being(
			String key
			) {
		super(ontoTopic, schema, version, key);
		this.partTypeOfTopic = ID_PART_TYPES.ONTOLOGY_TOPIC;
		this.partTypeOfKey = ID_PART_TYPES.USER_TEXT;
	}

	public Being(
			String key
			, String name
			) {
		super(ontoTopic, schema, version, key);
		this.name = name;
		this.partTypeOfTopic = ID_PART_TYPES.ONTOLOGY_TOPIC;
		this.partTypeOfKey = ID_PART_TYPES.USER_TEXT;
	}

	public Being(
			BeingCreateForm form
			) {
		super(ontoTopic, schema,  version, form);
	}
}
