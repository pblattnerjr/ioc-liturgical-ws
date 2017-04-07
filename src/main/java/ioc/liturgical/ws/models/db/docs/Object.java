package ioc.liturgical.ws.models.db.docs;

import ioc.liturgical.ws.constants.ONTOLOGY_TOPICS;
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
	private static ONTOLOGY_TOPICS ontoTopic = ONTOLOGY_TOPICS.OBJECT;
	
	public Object(
			String key
			) {
		super(ontoTopic, schema, version, key);
	}

	public Object(
			ObjectCreateForm form
			) {
		super(ontoTopic, schema,  version, form);
	}

}
