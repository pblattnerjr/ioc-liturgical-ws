package ioc.liturgical.ws.models.db.supers;

import com.google.gson.annotations.Expose;

import ioc.liturgical.ws.constants.RELATIONSHIP_TYPES;
import ioc.liturgical.ws.constants.db.external.SCHEMA_CLASSES;
import ioc.liturgical.ws.constants.db.external.TOPICS;

import com.github.reinert.jjschema.Attributes;

/**
 * @author mac002
 *
 */
@Attributes(title = "LTKLinkCreateForm", description = "Abstract Class to create an LTKLink")
public class LTKLinkCreateForm extends LTK {
	
	@Attributes(id="bottom", required = true, readonly = true, description = "The type of link")
	@Expose public RELATIONSHIP_TYPES type = null;
	
	public LTKLinkCreateForm(
			String library
			, String topic
			, String key
			, String schema
			, double serialVersion
			, RELATIONSHIP_TYPES type
			, TOPICS ontoTopic
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
				, SCHEMA_CLASSES.classForSchemaName(form._valueSchemaId).ltkDb.schemaIdAsString()
				, SCHEMA_CLASSES.classForSchemaName(form._valueSchemaId).ltkDb.schemaIdAsNumber()
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
