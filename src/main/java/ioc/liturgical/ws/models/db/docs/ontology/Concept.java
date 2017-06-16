package ioc.liturgical.ws.models.db.docs.ontology;

import ioc.liturgical.ws.constants.ID_PART_TYPES;
import ioc.liturgical.ws.constants.db.external.TOPICS;
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
	private static TOPICS ontoTopic = TOPICS.CONCEPT;
	
	public Concept(
			String key
			) {
		super(ontoTopic, schema, version, key);
	}

	public Concept(
			String key
			, String name
			) {
		super(ontoTopic, schema, version, key);
		this.name = name;
		this.partTypeOfTopic = ID_PART_TYPES.ONTOLOGY_TOPIC;
		this.partTypeOfKey = ID_PART_TYPES.USER_TEXT;
	}

	public Concept(
			ConceptCreateForm form
			) {
		super(ontoTopic, schema,  version, form);
		this.partTypeOfTopic = ID_PART_TYPES.ONTOLOGY_TOPIC;
		this.partTypeOfKey = ID_PART_TYPES.USER_TEXT;
	}

}
