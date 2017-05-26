package ioc.liturgical.ws.models.db.forms;

import ioc.liturgical.ws.constants.ID_PART_TYPES;
import ioc.liturgical.ws.constants.ONTOLOGY_TOPICS;
import ioc.liturgical.ws.models.db.supers.LTKOntologyCreateFormEntry;

import com.github.reinert.jjschema.Attributes;

/**
 * @author mac002
 *
 */
@Attributes(title = "Plant", description = "This is a doc that records information about a being that is incapable of independent locomotion.")
public class PlantCreateForm extends LTKOntologyCreateFormEntry {

	public PlantCreateForm(
			String key
			) {
		super(
				ONTOLOGY_TOPICS.PLANT
				, key
				, PlantCreateForm.class.getSimpleName()
				,  1.1
				);
		this.partTypeOfTopic = ID_PART_TYPES.ONTOLOGY_TOPIC;
		this.partTypeOfKey = ID_PART_TYPES.USER_TEXT;
	}
}
