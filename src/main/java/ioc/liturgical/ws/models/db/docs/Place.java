package ioc.liturgical.ws.models.db.docs;

import ioc.liturgical.ws.constants.ONTOLOGY_TOPICS;
import ioc.liturgical.ws.models.db.forms.PlaceCreateForm;
import ioc.liturgical.ws.models.db.supers.LTKDbOntologyEntry;

import com.github.reinert.jjschema.Attributes;

/**
 * @author mac002
 *
 */
@Attributes(title = "Place", description = "This is a doc that records information about a place.")
public class Place extends LTKDbOntologyEntry {
	
	private static String schema = Place.class.getSimpleName();
	private static double version = 1.1;
	private static ONTOLOGY_TOPICS ontoTopic = ONTOLOGY_TOPICS.PLACE;
	
	public Place(
			String key
			) {
		super(ontoTopic, schema, version, key);
	}

	public Place(
			PlaceCreateForm form
			) {
		super(ontoTopic, schema,  version, form);
	}

}
