package ioc.liturgical.ws.models.db.forms;

import ioc.liturgical.ws.constants.ONTOLOGY_TOPICS;
import ioc.liturgical.ws.models.db.supers.LTKOntologyCreateFormEntry;

import com.github.reinert.jjschema.Attributes;

/**
 * @author mac002
 *
 */
@Attributes(title = "Place", description = "This is a doc that records information about a place.")
public class PlaceCreateForm extends LTKOntologyCreateFormEntry {

	public PlaceCreateForm(
			String key
			) {
		super(
				ONTOLOGY_TOPICS.PLACE
				, key
				, PlaceCreateForm.class.getSimpleName()
				,  1.1
				);
	}
}
