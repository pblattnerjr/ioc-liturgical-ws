package ioc.liturgical.ws.models.db.supers;

import com.google.gson.annotations.Expose;

import ioc.liturgical.ws.annotations.UiWidget;
import ioc.liturgical.ws.constants.Constants;
import ioc.liturgical.ws.constants.EXTERNAL_DB_LIBS;
import ioc.liturgical.ws.constants.ONTOLOGY_TOPICS;

import com.github.reinert.jjschema.Attributes;

/**
 * @author mac002
 *
 */
@Attributes(title = "LTKDbOntology Entry", description = "Abstract ontology Entry")
public class LTKDbOntologyEntry extends LTKDb {

	@Attributes(required = true, description = "Name")
	@Expose public String name = "";

	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(required = false, description = "Description")
	@Expose public String description = "";

	public LTKDbOntologyEntry(
			ONTOLOGY_TOPICS topic
			, String schema
			, double serialVersion
			, String key
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

	public LTKDbOntologyEntry(
			ONTOLOGY_TOPICS topic
			, String schema
			, double serialVersion
			, LTKOntologyCreateFormEntry form
			) {
		super(
				EXTERNAL_DB_LIBS.ONTOLOGY.libname
				, topic.keyname
				, form.getKey()
				, schema
				,  serialVersion
				, topic
				);
		this.description = form.getDescription();
		this.name = form.getName();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
