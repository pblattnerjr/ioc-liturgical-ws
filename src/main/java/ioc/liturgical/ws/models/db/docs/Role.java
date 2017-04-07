package ioc.liturgical.ws.models.db.docs;

import ioc.liturgical.ws.constants.ONTOLOGY_TOPICS;
import ioc.liturgical.ws.models.db.forms.AnimalCreateForm;
import ioc.liturgical.ws.models.db.supers.LTKDbOntologyEntry;

import com.github.reinert.jjschema.Attributes;

/**
 * @author mac002
 *
 */
@Attributes(title = "Role", description = "This is a doc that records information about a role or title, e.g. King, Father, Mother, Priest, Bishop.")
public class Role extends LTKDbOntologyEntry {
	
	private static String schema = Role.class.getSimpleName();
	private static double version = 1.1;
	private static ONTOLOGY_TOPICS ontoTopic = ONTOLOGY_TOPICS.ROLE;
	
	public Role(
			String key
			) {
		super(ontoTopic, schema, version, key);
	}

	public Role(
			AnimalCreateForm form
			) {
		super(ontoTopic, schema,  version, form);
	}

}
