package ioc.liturgical.ws.models.db.forms;

import ioc.liturgical.ws.constants.ID_PART_TYPES;
import ioc.liturgical.ws.constants.db.external.TOPICS;
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
				TOPICS.PLACE
				, key
				, PlaceCreateForm.class.getSimpleName()
				,  1.1
				);
		this.partTypeOfTopic = ID_PART_TYPES.ONTOLOGY_TOPIC;
		this.partTypeOfKey = ID_PART_TYPES.USER_TEXT;
	}
}
