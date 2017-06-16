package ioc.liturgical.ws.models.db.forms;

import ioc.liturgical.ws.constants.ID_PART_TYPES;
import ioc.liturgical.ws.constants.db.external.TOPICS;
import ioc.liturgical.ws.models.db.supers.LTKOntologyCreateFormEntry;

import com.github.reinert.jjschema.Attributes;

/**
 * @author mac002
 *
 */
@Attributes(title = "Mystery", description = "This is a form to record information about one of the main mysteries of the Church, i.e., Baptism, Chrismation, Confession, Holy Communion, Holy Matrimony, Holy Orders, and Unction.")
public class MysteryCreateForm extends LTKOntologyCreateFormEntry {

	public MysteryCreateForm(
			String key
			) {
		super(
				TOPICS.MYSTERY
				, key
				, MysteryCreateForm.class.getSimpleName()
				,  1.1
				);
		this.partTypeOfTopic = ID_PART_TYPES.ONTOLOGY_TOPIC;
		this.partTypeOfKey = ID_PART_TYPES.USER_TEXT;
	}
	
}
