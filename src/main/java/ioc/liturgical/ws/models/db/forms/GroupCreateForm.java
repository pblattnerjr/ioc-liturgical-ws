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
@Attributes(title = "Group", description = "This is a doc that records information about a group of people, e.g. a kingdom, or a monastery.")
public class GroupCreateForm extends LTKOntologyCreateFormEntry {

	public GroupCreateForm(
			String key
			) {
		super(
				ONTOLOGY_TOPICS.GROUP
				, key
				, GroupCreateForm.class.getSimpleName()
				,  1.1
				);
	}
}
