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
@Attributes(title = "Human", description = "This is a doc that records information about a human being.")
public class HumanCreateForm extends LTKOntologyCreateFormEntry {

	public HumanCreateForm(
			String key
			) {
		super(
				ONTOLOGY_TOPICS.HUMAN
				, key
				, HumanCreateForm.class.getSimpleName()
				,  1.1
				);
	}
}
