package ioc.liturgical.ws.models.db.docs;

import com.google.gson.annotations.Expose;

import ioc.liturgical.ws.annotations.UiWidget;
import ioc.liturgical.ws.constants.Constants;
import ioc.liturgical.ws.constants.ONTOLOGY_TOPICS;
import ioc.liturgical.ws.models.db.forms.AnimalCreateForm;
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
	private static ONTOLOGY_TOPICS ontoTopic = ONTOLOGY_TOPICS.BEING;
	
	public Being(
			String key
			) {
		super(ontoTopic, schema, version, key);
	}

	public Being(
			BeingCreateForm form
			) {
		super(ontoTopic, schema,  version, form);
	}
}
