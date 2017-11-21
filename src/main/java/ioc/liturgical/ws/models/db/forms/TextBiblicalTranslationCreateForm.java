package ioc.liturgical.ws.models.db.forms;

import com.google.gson.annotations.Expose;

import ioc.liturgical.ws.annotations.UiWidget;
import ioc.liturgical.ws.constants.Constants;
import ioc.liturgical.ws.constants.ID_PART_TYPES;
import ioc.liturgical.ws.constants.db.external.TOPICS;
import ioc.liturgical.ws.models.db.supers.LTK;


import com.github.reinert.jjschema.Attributes;

/**
 * This class provides a POJO for use in web forms to create or update a domain
 * @author mac002
 *
 */
@Attributes(title = "Biblical Text (Translation)", description = "Form to create a translation of a Biblical text, i.e., a verse.")
public class TextBiblicalTranslationCreateForm extends LTK {

	private static double serialVersion = 1.1;
	private static String schema = TextBiblicalTranslationCreateForm.class.getSimpleName();

	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(required = true, description = "Translation of the source text.")
	@Expose public String value = "";

	public TextBiblicalTranslationCreateForm(
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
				, TOPICS.TEXT_BIBLICAL
				);
		this.partTypeOfTopic = ID_PART_TYPES.TOPIC_FROM_ID_OF_SELECTED_BIBLICAL_VERSE;
		this.partTypeOfKey = ID_PART_TYPES.KEY_FROM_ID_OF_SELECTED_BIBLICAL_VERSE;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
