package ioc.liturgical.ws.models.db.forms;

import com.google.gson.annotations.Expose;

import ioc.liturgical.ws.annotations.UiWidget;
import ioc.liturgical.ws.constants.Constants;
import ioc.liturgical.ws.constants.ID_PART_TYPES;
import ioc.liturgical.ws.constants.db.external.TOPICS;
import ioc.liturgical.ws.models.db.supers.LTK;
import net.ages.alwb.utils.core.error.handling.ErrorUtils;
import net.ages.alwb.utils.core.id.managers.IdManager;

import com.github.reinert.jjschema.Attributes;

/**
 * This class provides a POJO for use in web forms to create or update a domain
 * @author mac002
 *
 */
@Attributes(title = "Liturgical Text (Translation)", description = "Form to create a translation of a Liturgical text.")
public class TextLiturgicalTranslationCreateForm extends LTK {

	private static double serialVersion = 1.1;
	private static String schema = TextLiturgicalTranslationCreateForm.class.getSimpleName();

	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(required = true, description = "Translation of the source text.")
	@Expose  public String value = "";

	@Attributes(required = false, readonly = true, description = "Line sequence number for this text within its topic.")
	@Expose  public String seq = "";

	public TextLiturgicalTranslationCreateForm(
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
				, TOPICS.TEXT_LITURGICAL
				);
		this.partTypeOfTopic = ID_PART_TYPES.TOPIC_FROM_ID_OF_SELECTED_LITURGICAL_TEXT;
		this.partTypeOfKey = ID_PART_TYPES.KEY_FROM_ID_OF_SELECTED_LITURGICAL_TEXT;
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
	
	public void convertSeq(String from) {
		String[] parts = from.split(Constants.ID_DELIMITER);
		if (parts.length == 3) {
			IdManager idManager = new IdManager(this.getLibrary(), this.getTopic(), parts[2]);
			this.setSeq(idManager.getId());
		}
	}

}
