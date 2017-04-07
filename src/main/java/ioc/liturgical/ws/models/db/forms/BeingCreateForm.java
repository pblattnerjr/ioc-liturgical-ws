package ioc.liturgical.ws.models.db.forms;

import ioc.liturgical.ws.constants.ONTOLOGY_TOPICS;
import ioc.liturgical.ws.models.db.supers.LTKOntologyCreateFormEntry;

import com.github.reinert.jjschema.Attributes;

/**
 * @author mac002
 *
 */
@Attributes(title = "Being", description = "This is a doc that records information about a being, e.g. that is neither a human, animal, or plant.")
public class BeingCreateForm extends LTKOntologyCreateFormEntry {

	public BeingCreateForm(
			String key
			) {
		super(
				ONTOLOGY_TOPICS.BEING
				, key
				, BeingCreateForm.class.getSimpleName()
				,  1.1
				);
	}
}
