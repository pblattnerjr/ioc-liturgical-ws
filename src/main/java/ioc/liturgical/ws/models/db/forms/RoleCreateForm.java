package ioc.liturgical.ws.models.db.forms;

import ioc.liturgical.ws.constants.ONTOLOGY_TOPICS;
import ioc.liturgical.ws.models.db.supers.LTKOntologyCreateFormEntry;

import com.github.reinert.jjschema.Attributes;

/**
 * @author mac002
 *
 */
@Attributes(title = "Role", description = "This is a doc that records information about a role, e.g. King, Father, Mother, Saint.")
public class RoleCreateForm extends LTKOntologyCreateFormEntry {

	public RoleCreateForm(
			String key
			) {
		super(
				ONTOLOGY_TOPICS.ROLE
				, key
				, RoleCreateForm.class.getSimpleName()
				,  1.1
				);
	}
	
}
