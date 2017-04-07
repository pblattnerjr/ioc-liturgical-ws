package ioc.liturgical.ws.models.db.docs;

import ioc.liturgical.ws.constants.ONTOLOGY_TOPICS;
import ioc.liturgical.ws.models.db.forms.EventCreateForm;
import ioc.liturgical.ws.models.db.supers.LTKDbOntologyEntry;

import com.github.reinert.jjschema.Attributes;

/**
 * @author mac002
 *
 */
@Attributes(title = "Event", description = "This is a doc that records information about an event, e.g. the birth of Christ.")
public class Event extends LTKDbOntologyEntry {
	
	private static String schema = Event.class.getSimpleName();
	private static double version = 1.1;
	private static ONTOLOGY_TOPICS ontoTopic = ONTOLOGY_TOPICS.EVENT;
	
	public Event(
			String key
			) {
		super(ontoTopic, schema, version, key);
	}

	public Event(
			EventCreateForm form
			) {
		super(ontoTopic, schema,  version, form);
	}

}
