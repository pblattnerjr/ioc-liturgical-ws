package ioc.liturgical.ws.models.db.forms;

import com.google.gson.annotations.Expose;

import ioc.liturgical.ws.forms.manager.FormRegExConstants;
import ioc.liturgical.ws.models.SchemaException;
import ioc.liturgical.ws.annotations.UiWidget;
import ioc.liturgical.ws.constants.Constants;
import ioc.liturgical.ws.forms.manager.FormFieldLengths;
import net.ages.alwb.utils.core.datastores.json.models.AbstractModel;

import com.github.reinert.jjschema.Attributes;

/**
 * This class provides a POJO for use in web forms to create or update a domain
 * @author mac002
 *
 */
@Attributes(title = "Reference", description = "A reference is a doc that records information about a reference made in a text to something else.  For example, a liturgical text might be a hymn that refers to a person, place, or event, e.g. in the Bible.")
public class ReferenceCreateForm extends AbstractModel {
	
	@Attributes(required = true, description = "The database ID of the doc containing the text that makes a reference to another text.")
	@Expose String idReferredByText = "";

	@Attributes(required = true, description = "The database ID of the doc containing the text that is referred to by another text.")
	@Expose String idReferredToText = "";

	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(required = true, description = "Biblical Material (Theme < Figures, Hints, etc).")
	@Expose String bibMaterial = "";

	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(required = true, description = "Exegetical notes")
	@Expose String exenote = "";

	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(required = true, description = "Lexical notes")
	@Expose String lexnote = "";

	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(required = true, description = "Targeted terms")
	@Expose String targetedTerms = "";

	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(required = true, description = "Text witness")
	@Expose String textWitness = "";

	public ReferenceCreateForm() {
		super();
		this.serialVersionUID = 1.1;
	}


	public String getIdReferredByText() {
		return idReferredByText;
	}


	public void setIdReferredByText(String idReferredByText) {
		this.idReferredByText = idReferredByText;
	}


	public String getIdReferredToText() {
		return idReferredToText;
	}


	public void setIdReferredToText(String idReferredToText) {
		this.idReferredToText = idReferredToText;
	}


	public String getBibMaterial() {
		return bibMaterial;
	}


	public void setBibMaterial(String bibMaterial) {
		this.bibMaterial = bibMaterial;
	}


	public String getExenote() {
		return exenote;
	}


	public void setExenote(String exenote) {
		this.exenote = exenote;
	}


	public String getLexnote() {
		return lexnote;
	}


	public void setLexnote(String lexnote) {
		this.lexnote = lexnote;
	}


	public String getTargetedTerms() {
		return targetedTerms;
	}


	public void setTargetedTerms(String targetedTerms) {
		this.targetedTerms = targetedTerms;
	}


	public String getTextWitness() {
		return textWitness;
	}


	public void setTextWitness(String textWitness) {
		this.textWitness = textWitness;
	}
	
	public static void main(String[] args) {
		ReferenceCreateForm form = new ReferenceCreateForm();
		System.out.println(form.toJsonUiSchemaObject().toString());
		System.out.println(form.toJsonSchemaObject().toString());
		System.out.println(form.toJsonObject().toString());
	}

}
