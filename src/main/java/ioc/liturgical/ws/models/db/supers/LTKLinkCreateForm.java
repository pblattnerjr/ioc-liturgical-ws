package ioc.liturgical.ws.models.db.supers;

import com.google.gson.annotations.Expose;

import ioc.liturgical.ws.constants.EXTERNAL_DB_SCHEMA_CLASSES;
import ioc.liturgical.ws.constants.ONTOLOGY_TOPICS;
import ioc.liturgical.ws.constants.RELATIONSHIP_TYPES;

import com.github.reinert.jjschema.Attributes;

/**
 * @author mac002
 *
 */
@Attributes(title = "LTKLinkCreateForm", description = "Abstract Class to create an LTKLink")
public class LTKLinkCreateForm extends LTK {
	
	@Expose RELATIONSHIP_TYPES type = null;
	
	public LTKLinkCreateForm(
			String library
			, String topic
			, String key
			, String schema
			, double serialVersion
			, RELATIONSHIP_TYPES type
			, ONTOLOGY_TOPICS ontoTopic
			) {
		super(
				library
				, topic
				, key
				, schema
				,  serialVersion
				, ontoTopic
				);
		this.type = type;
	}

	public LTKLinkCreateForm(
			LTKLinkCreateForm form
			) {
		super(
				form.getLibrary()
				, form.getTopic()
				, form.getKey()
				, EXTERNAL_DB_SCHEMA_CLASSES.classForSchemaName(form._valueSchemaId).ltkDb.schemaIdAsString()
				, EXTERNAL_DB_SCHEMA_CLASSES.classForSchemaName(form._valueSchemaId).ltkDb.schemaIdAsNumber()
				, form.getOntologyTopic()
				);
	}

	public RELATIONSHIP_TYPES getType() {
		return type;
	}

	public void setType(RELATIONSHIP_TYPES type) {
		this.type = type;
	}

}
