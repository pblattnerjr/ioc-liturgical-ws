package ioc.liturgical.ws.models.db.forms;

import ioc.liturgical.ws.constants.ID_PART_TYPES;
import ioc.liturgical.ws.constants.db.external.TOPICS;
import ioc.liturgical.ws.models.db.supers.LTKOntologyCreateFormEntry;

import com.github.reinert.jjschema.Attributes;

/**
 * @author mac002
 *
 */
@Attributes(title = "Animal", description = "This is a doc that records information about an animal.")
public class AnimalCreateForm extends LTKOntologyCreateFormEntry {

	public AnimalCreateForm(
			String key
			) {
		super(
				TOPICS.ANIMAL
				, key
				, AnimalCreateForm.class.getSimpleName()
				,  1.1
				);
		this.partTypeOfTopic = ID_PART_TYPES.ONTOLOGY_TOPIC;
		this.partTypeOfKey = ID_PART_TYPES.USER_TEXT;
	}

}
