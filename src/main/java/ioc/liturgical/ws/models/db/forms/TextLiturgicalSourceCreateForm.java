package ioc.liturgical.ws.models.db.forms;

import com.google.gson.annotations.Expose;

import ioc.liturgical.ws.annotations.UiWidget;
import ioc.liturgical.ws.constants.Constants;
import ioc.liturgical.ws.constants.ID_PART_TYPES;
import ioc.liturgical.ws.constants.ONTOLOGY_TOPICS;
import ioc.liturgical.ws.models.db.supers.LTK;

import com.github.reinert.jjschema.Attributes;

/**
 * This class provides a POJO for use in web forms to create or update a domain
 * @author mac002
 *
 */
@Attributes(title = "Liturgical Text (Source)", description = "Form to create a Liturgical source text.")
public class TextLiturgicalSourceCreateForm extends LTK {

	private static double serialVersion = 1.1;
	private static String schema = TextLiturgicalSourceCreateForm.class.getSimpleName();

	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(required = true, description = "The value of the text.")
	@Expose  public String value = "";

	@Attributes(required = false, readonly = false, description = "Line sequence number for this text within its topic.")
	@Expose  public String seq = "";

	public TextLiturgicalSourceCreateForm(
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
		this.partTypeOfTopic = ID_PART_TYPES.USER_TEXT;
		this.partTypeOfKey = ID_PART_TYPES.USER_TEXT;
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
