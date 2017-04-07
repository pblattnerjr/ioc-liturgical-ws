package ioc.liturgical.ws.models.db.forms;

import com.google.gson.annotations.Expose;

import ioc.liturgical.ws.annotations.UiWidget;
import ioc.liturgical.ws.constants.Constants;
import ioc.liturgical.ws.constants.ONTOLOGY_TOPICS;
import ioc.liturgical.ws.models.db.supers.LTK;
import ioc.liturgical.ws.models.db.supers.LTKOntologyCreateFormEntry;

import com.github.reinert.jjschema.Attributes;

/**
 * @author mac002
 *
 */
@Attributes(title = "Concept", description = "This is a doc that records information about a concept.")
public class ConceptCreateForm extends LTKOntologyCreateFormEntry {

	public ConceptCreateForm(
			String key
			) {
		super(
				ONTOLOGY_TOPICS.CONCEPT
				, key
				, ConceptCreateForm.class.getSimpleName()
				,  1.1
				);
	}
}
