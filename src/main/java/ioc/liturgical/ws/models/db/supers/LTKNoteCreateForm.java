package ioc.liturgical.ws.models.db.supers;

import com.google.gson.annotations.Expose;

import ioc.liturgical.ws.annotations.UiWidget;
import ioc.liturgical.ws.constants.Constants;
import ioc.liturgical.ws.constants.db.external.SCHEMA_CLASSES;
import ioc.liturgical.ws.constants.db.external.TOPICS;

import com.github.reinert.jjschema.Attributes;

/**
 * @author mac002
 *
 */
@Attributes(title = "LTKDbNoteCreateForm", description = "Abstract Form to Create a Note Entry")
public class LTKNoteCreateForm extends LTK {
	
	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(id="top", required = false, description = "Your note(s)")
	@Expose public String value = "";

	@Attributes(id="bottom", readonly=true, required = false, description = "Sequence.  Used for sort order when listing instances of notes.")
	@Expose public String seq = "";

	public LTKNoteCreateForm(
			String library
			, String topic
			, String key
			, String schema
			, double serialVersion
			, TOPICS ontologyTopic
			) {
		super(
				library
				, topic
				, key
				, schema
				,  serialVersion
				, ontologyTopic
				);
		this.seq = super.getId();
	}

	public LTKNoteCreateForm(
			LTKNoteCreateForm form
			) {
		super(
				form.getLibrary()
				, form.getTopic()
				, form.getKey()
				, SCHEMA_CLASSES.classForSchemaName(form._valueSchemaId).ltkDb.schemaIdAsString()
				, SCHEMA_CLASSES.classForSchemaName(form._valueSchemaId).ltkDb.schemaIdAsNumber()
				, form.getOntologyTopic()
				);
		this.value = form.getValue();
		this.seq = this.getId();
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getSeq() {
		return seq;
	}

	public void setSeq(String seq) {
		this.seq = seq;
	}

}
