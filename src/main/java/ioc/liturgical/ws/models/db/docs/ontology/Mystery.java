package ioc.liturgical.ws.models.db.docs.ontology;

import ioc.liturgical.ws.constants.ID_PART_TYPES;
import ioc.liturgical.ws.constants.db.external.TOPICS;
import ioc.liturgical.ws.models.db.forms.MysteryCreateForm;
import ioc.liturgical.ws.models.db.supers.LTKDbOntologyEntry;

import com.github.reinert.jjschema.Attributes;

/**
 * @author mac002
 *
 */
@Attributes(title = "Mystery", description = "This is a form to record information about one of the main mysteries of the Church, i.e., Baptism, Chrismation, Confession, Holy Communion, Holy Matrimony, Holy Orders, and Unction.")
public class Mystery extends LTKDbOntologyEntry {
	
	private static String schema = Mystery.class.getSimpleName();
	private static double version = 1.1;
	private static TOPICS ontoTopic = TOPICS.MYSTERY;
	
	public Mystery(
			String key
			) {
		super(ontoTopic, schema, version, key);
		this.partTypeOfTopic = ID_PART_TYPES.ONTOLOGY_TOPIC;
		this.partTypeOfKey = ID_PART_TYPES.USER_TEXT;
	}

	public Mystery(
			String key
			, String name
			) {
		super(ontoTopic, schema, version, key);
		this.name = name;
		this.partTypeOfTopic = ID_PART_TYPES.ONTOLOGY_TOPIC;
		this.partTypeOfKey = ID_PART_TYPES.USER_TEXT;
	}

	public Mystery(
			MysteryCreateForm form
			) {
		super(ontoTopic, schema,  version, form);
	}

}
