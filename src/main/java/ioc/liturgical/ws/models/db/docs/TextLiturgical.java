package ioc.liturgical.ws.models.db.docs;

import com.google.gson.annotations.Expose;

import ioc.liturgical.ws.annotations.UiWidget;
import ioc.liturgical.ws.constants.Constants;
import ioc.liturgical.ws.constants.ONTOLOGY_TOPICS;
import ioc.liturgical.ws.models.db.forms.LinkRefersToBiblicalTextCreateForm;
import ioc.liturgical.ws.models.db.supers.LTKDb;


import com.github.reinert.jjschema.Attributes;

/**
 * This class provides a POJO for use in web forms to create or update a domain
 * @author mac002
 *
 */
@Attributes(title = "TextLiturgical", description = "Properties for a Text that is of type Liturgical.")
public class TextLiturgical extends LTKDb {

	private static double serialVersion = 1.1;
	private static String schema = TextLiturgical.class.getSimpleName();

	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(required = true, description = "The value of the text.")
	@Expose String value = "";

	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(required = false, readonly = true, description = "Normalized text with punctuation.")
	@Expose String nwp = "";

	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(required = false, readonly = true, description = "Normalized text with no punctuation.")
	@Expose String nnp = "";

	@Attributes(required = false, readonly = true, description = "Line sequence number for this text within its topic.")
	@Expose String seq = "";

	public TextLiturgical(
			String library
			, String topic
			, String key
			) {
		super(
				library
				, topic
				, key
				, schema
				, serialVersion
				, ONTOLOGY_TOPICS.TEXT_LITURGICAL
				);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getNwp() {
		return nwp;
	}

	public void setNwp(String nwp) {
		this.nwp = nwp;
	}

	public String getNnp() {
		return nnp;
	}

	public void setNnp(String nnp) {
		this.nnp = nnp;
	}

	public String getSeq() {
		return seq;
	}

	public void setSeq(String seq) {
		this.seq = seq;
	}

}
