package ioc.liturgical.ws.models.db.forms;

import ioc.liturgical.ws.constants.ONTOLOGY_TOPICS;
import ioc.liturgical.ws.models.db.supers.LTKOntologyCreateFormEntry;

import com.github.reinert.jjschema.Attributes;

/**
 * @author mac002
 *
 */
@Attributes(title = "Event", description = "This is a doc that records information about an event, e.g. the birth of Christ.")
public class EventCreateForm extends LTKOntologyCreateFormEntry {

	public EventCreateForm(
			String key
			) {
		super(
				ONTOLOGY_TOPICS.EVENT
				, key
				, EventCreateForm.class.getSimpleName()
				,  1.1
				);
	}

}
