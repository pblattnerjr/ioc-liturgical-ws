package ioc.liturgical.ws.models.db.docs.ontology;

import ioc.liturgical.ws.constants.ID_PART_TYPES;
import ioc.liturgical.ws.constants.db.external.TOPICS;
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
	private static TOPICS ontoTopic = TOPICS.GROUP;
	
	public Group(
			String key
			) {
		super(ontoTopic, schema, version, key);
	}

	public Group(
			String key
			, String name
			) {
		super(ontoTopic, schema, version, key);
		this.name = name;
		this.partTypeOfTopic = ID_PART_TYPES.ONTOLOGY_TOPIC;
		this.partTypeOfKey = ID_PART_TYPES.USER_TEXT;
	}

	public Group(
			GroupCreateForm form
			) {
		super(ontoTopic, schema,  version, form);
		this.partTypeOfTopic = ID_PART_TYPES.ONTOLOGY_TOPIC;
		this.partTypeOfKey = ID_PART_TYPES.USER_TEXT;
	}

}
