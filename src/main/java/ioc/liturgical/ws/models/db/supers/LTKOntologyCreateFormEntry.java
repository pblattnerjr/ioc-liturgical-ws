package ioc.liturgical.ws.models.db.supers;

import com.google.gson.annotations.Expose;

import ioc.liturgical.ws.annotations.UiWidget;
import ioc.liturgical.ws.constants.Constants;
import ioc.liturgical.ws.constants.EXTERNAL_DB_LIBS;
import ioc.liturgical.ws.constants.EXTERNAL_DB_SCHEMA_CLASSES;
import ioc.liturgical.ws.constants.ONTOLOGY_TOPICS;

import com.github.reinert.jjschema.Attributes;

/**
 * @author mac002
 *
 */
@Attributes(title = "LTKDbOntology Entry", description = "Abstract ontology Entry")
public class LTKOntologyCreateFormEntry extends LTK {
	
	@Attributes(required = true, description = "Name")
	@Expose String name = "";

	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(required = false, description = "Description")
	@Expose String description = "";

	public LTKOntologyCreateFormEntry(
			ONTOLOGY_TOPICS topic
			, String key
			, String schema
			, double serialVersion
			) {
		super(
				EXTERNAL_DB_LIBS.ONTOLOGY.libname
				, topic.keyname
				, key
				, schema
				,  serialVersion
				, topic
				);
		this.name = key;
	}

	public LTKOntologyCreateFormEntry(
			LTKOntologyCreateFormEntry form
			) {
		super(
				EXTERNAL_DB_LIBS.ONTOLOGY.libname
				, form.getTopic()
				, form.getKey()
				, EXTERNAL_DB_SCHEMA_CLASSES.classForSchemaName(form._valueSchemaId).ltkDb.schemaIdAsString()
				, EXTERNAL_DB_SCHEMA_CLASSES.classForSchemaName(form._valueSchemaId).ltkDb.schemaIdAsNumber()
				, form.getOntologyTopic()
				);
		this.description = form.getDescription();
		this.name = form.getName();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
