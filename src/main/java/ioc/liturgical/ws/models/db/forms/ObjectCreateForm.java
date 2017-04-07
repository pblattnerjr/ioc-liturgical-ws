package ioc.liturgical.ws.models.db.forms;

import ioc.liturgical.ws.constants.ONTOLOGY_TOPICS;
import ioc.liturgical.ws.models.db.supers.LTKOntologyCreateFormEntry;

import com.github.reinert.jjschema.Attributes;

/**
 * @author mac002
 *
 */
@Attributes(title = "Object", description = "This is a doc that records information about an object.")
public class ObjectCreateForm extends LTKOntologyCreateFormEntry {

	public ObjectCreateForm(
			String key
			) {
		super(
				ONTOLOGY_TOPICS.OBJECT
				, key
				, ObjectCreateForm.class.getSimpleName()
				,  1.1
				);
	}
}
