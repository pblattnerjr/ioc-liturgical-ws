package ioc.liturgical.ws.models.db.docs.ontology;

import ioc.liturgical.ws.constants.ID_PART_TYPES;
import ioc.liturgical.ws.constants.db.external.TOPICS;
import ioc.liturgical.ws.models.db.forms.ObjectCreateForm;
import ioc.liturgical.ws.models.db.supers.LTKDbOntologyEntry;

import com.github.reinert.jjschema.Attributes;

/**
 * @author mac002
 *
 */
@Attributes(title = "Object", description = "This is a doc that records information about an object.")
public class Object extends LTKDbOntologyEntry {
	
	private static String schema = Object.class.getSimpleName();
	private static double version = 1.1;
	private static TOPICS ontoTopic = TOPICS.OBJECT;
	
	public Object(
			String key
			) {
		super(ontoTopic, schema, version, key);
		this.partTypeOfTopic = ID_PART_TYPES.ONTOLOGY_TOPIC;
		this.partTypeOfKey = ID_PART_TYPES.USER_TEXT;
	}
	
	public Object(
			String key
			, String name
			) {
		super(ontoTopic, schema, version, key);
		this.partTypeOfTopic = ID_PART_TYPES.ONTOLOGY_TOPIC;
		this.partTypeOfKey = ID_PART_TYPES.USER_TEXT;
		this.name = name;
	}


	public Object(
			ObjectCreateForm form
			) {
		super(ontoTopic, schema,  version, form);
	}

}
