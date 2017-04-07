package ioc.liturgical.ws.models.db.docs;

import ioc.liturgical.ws.constants.ONTOLOGY_TOPICS;
import ioc.liturgical.ws.models.db.forms.GroupCreateForm;
import ioc.liturgical.ws.models.db.supers.LTKDbOntologyEntry;

import com.github.reinert.jjschema.Attributes;

/**
 * @author mac002
 *
 */
@Attributes(title = "Group", description = "This is a doc that records information about a group of people, e.g. a kingdom, or a monastery.")
public class Group extends LTKDbOntologyEntry {
	
	private static String schema = Group.class.getSimpleName();
	private static double version = 1.1;
	private static ONTOLOGY_TOPICS ontoTopic = ONTOLOGY_TOPICS.GROUP;
	
	public Group(
			String key
			) {
		super(ontoTopic, schema, version, key);
	}

	public Group(
			GroupCreateForm form
			) {
		super(ontoTopic, schema,  version, form);
	}

}
