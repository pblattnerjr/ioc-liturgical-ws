package ioc.liturgical.ws.models.db.docs;

import ioc.liturgical.ws.constants.ONTOLOGY_TOPICS;
import ioc.liturgical.ws.models.db.supers.LTKDbOntologyEntry;
import ioc.liturgical.ws.models.db.forms.ConceptCreateForm;
import com.github.reinert.jjschema.Attributes;

/**
 * @author mac002
 *
 */
@Attributes(title = "Concept", description = "This is a doc that records information about a concept.")
public class Concept extends LTKDbOntologyEntry {
	
	private static String schema = Concept.class.getSimpleName();
	private static double version = 1.1;
	private static ONTOLOGY_TOPICS ontoTopic = ONTOLOGY_TOPICS.CONCEPT;
	
	public Concept(
			String key
			) {
		super(ontoTopic, schema, version, key);
	}

	public Concept(
			ConceptCreateForm form
			) {
		super(ontoTopic, schema,  version, form);
	}

}
