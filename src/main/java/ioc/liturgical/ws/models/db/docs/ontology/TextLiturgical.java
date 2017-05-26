package ioc.liturgical.ws.models.db.docs.ontology;

import com.google.gson.annotations.Expose;

import ioc.liturgical.ws.annotations.UiWidget;
import ioc.liturgical.ws.constants.BIBLICAL_BOOK_NUMBERS;
import ioc.liturgical.ws.constants.Constants;
import ioc.liturgical.ws.constants.ID_PART_TYPES;
import ioc.liturgical.ws.constants.ONTOLOGY_TOPICS;
import ioc.liturgical.ws.models.db.forms.TextBiblicalTranslationCreateForm;
import ioc.liturgical.ws.models.db.forms.TextLiturgicalTranslationCreateForm;
import ioc.liturgical.ws.models.db.supers.LTKDb;
import net.ages.alwb.utils.core.error.handling.ErrorUtils;
import net.ages.alwb.utils.core.id.managers.IdManager;

import java.text.Normalizer;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.reinert.jjschema.Attributes;

/**
 * This class provides a POJO for use in web forms to create or update a domain
 * @author mac002
 *
 */
@Attributes(title = "Liturgical Text", description = "Properties for Liturgical Text.")
public class TextLiturgical extends LTKDb {
	private static final Logger logger = LoggerFactory.getLogger(TextLiturgical.class);

	private static double serialVersion = 1.1;
	private static String schema = TextLiturgical.class.getSimpleName();
	private static Pattern punctPattern = Pattern.compile("[˙·,.;!?(){}\\[\\]<>%]"); // punctuation

	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(required = true, description = "The value of the text.")
	@Expose  public String value = "";

	@Attributes(id = "bottom", required = false, readonly = true, description = "Normalized text with punctuation.")
	@Expose  public String nwp = "";

	@Attributes(id = "bottom", required = false, readonly = true, description = "Normalized text with no punctuation.")
	@Expose  public String nnp = "";

	@Attributes(id = "bottom", required = false, readonly = true, description = "Line sequence number for this text within its topic.")
	@Expose  public String seq = "";

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
		this.partTypeOfTopic = ID_PART_TYPES.TOPIC_FROM_ID_OF_SELECTED_LITURGICAL_TEXT;
		this.partTypeOfKey = ID_PART_TYPES.KEY_FROM_ID_OF_SELECTED_LITURGICAL_TEXT;
	}
	
	@Override
	public void setSubClassProperties (
			String json
			) {
		try {
			TextLiturgicalTranslationCreateForm form = gson.fromJson(json, TextLiturgicalTranslationCreateForm.class);
			this.setValue(form.getValue());
			this.setSeq(form.getSeq());
			if (! this.seq.startsWith(this.library)) {
				try {
					IdManager idManager = new IdManager(this.seq);
					this.setSeq(this.id + "~" + idManager.getTopic() + "~" + idManager.getKey());
				} catch (Exception e) {
					ErrorUtils.report(logger, e);
				}
			}
			this.tags = form.getTags();
		} catch (Exception e) {
			ErrorUtils.report(logger, e);
		}
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
		try {
			this.nwp = Normalizer.normalize(value, Normalizer.Form.NFD)
					.replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();
			this.nnp = punctPattern.matcher(this.nwp).replaceAll("");
		} catch (Exception e) {
			ErrorUtils.report(logger, e);
			throw e;
		}
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
