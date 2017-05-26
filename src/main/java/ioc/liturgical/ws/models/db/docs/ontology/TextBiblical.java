package ioc.liturgical.ws.models.db.docs.ontology;

import com.google.gson.annotations.Expose;

import ioc.liturgical.ws.annotations.UiWidget;
import ioc.liturgical.ws.constants.BIBLICAL_BOOK_NUMBERS;
import ioc.liturgical.ws.constants.Constants;
import ioc.liturgical.ws.constants.ID_PART_TYPES;
import ioc.liturgical.ws.constants.ONTOLOGY_TOPICS;
import ioc.liturgical.ws.models.db.forms.TextBiblicalTranslationCreateForm;
import ioc.liturgical.ws.models.db.supers.LTKDb;
import net.ages.alwb.utils.core.error.handling.ErrorUtils;

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
@Attributes(title = "Biblical Text", description = "Properties for a Biblical Text, i.e., a verse.")
public class TextBiblical extends LTKDb {
	private static final Logger logger = LoggerFactory.getLogger(TextBiblical.class);

	private static double serialVersion = 1.1;
	private static String schema = TextBiblical.class.getSimpleName();
	private static Pattern punctPattern = Pattern.compile("[˙·,.;!?(){}\\[\\]<>%]"); // punctuation

	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(required = true, description = "The value of the text.")
	@Expose public String value = "";

	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(required = false, readonly = true, description = "Normalized text with punctuation.")
	@Expose  public String nwp = "";

	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(required = false, readonly = true, description = "Normalized text with no punctuation.")
	@Expose  public String nnp = "";

	@Attributes(required = false, readonly = true, description = "Line sequence number for this text within its topic.")
	@Expose  public String seq = "";

	@Attributes(required = false, readonly = true, description = "Chapter number of the text.")
	@Expose  public String chapter = "";

	@Attributes(required = false, readonly = true, description = "Verse number of the text.")
	@Expose  public String verse = "";

	@Attributes(required = false, readonly = true, description = "Sequence number for the book this text occurs in.")
	@Expose  public String bookNbr = "";

	public TextBiblical(
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
				, ONTOLOGY_TOPICS.TEXT_BIBLICAL
				);
		this.partTypeOfTopic = ID_PART_TYPES.TOPIC_FROM_ID_OF_SELECTED_BIBLICAL_VERSE;
		this.partTypeOfKey = ID_PART_TYPES.KEY_FROM_ID_OF_SELECTED_BIBLICAL_VERSE;
	}

	@Override
	public void setSubClassProperties (
			String json
			) {
		try {
			TextBiblicalTranslationCreateForm form = gson.fromJson(json, TextBiblicalTranslationCreateForm.class);
			String [] chapterVerse = form.key.split(":");
			
			this.bookNbr = BIBLICAL_BOOK_NUMBERS.getBookNumber(this.topic);
			this.chapter = chapterVerse[0];
			this.verse = chapterVerse[1];
			this.seq = BIBLICAL_BOOK_NUMBERS.getSequence(
					this.topic
					, this.chapter
					, this.verse
					, this.library
					);

			this.setValue(form.getValue());
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

	/**
	 * Do not call this directly.  Instead, call setValue().
	 * It will set this for you.
	 * @param nwp
	 */
	public void setNwp(String nwp) {
		this.nwp = nwp;
	}

	public String getNnp() {
		return nnp;
	}

	/**
	 * Do not call this directly.  Instead, call setValue().
	 * It will set this for you.
	 * @param nnp
	 */
	public void setNnp(String nnp) {
		this.nnp = nnp;
	}

	public String getSeq() {
		return seq;
	}

	public void setSeq(String seq) {
		this.seq = seq;
	}

	public String getChapter() {
		return chapter;
	}

	public void setChapter(String chapter) {
		this.chapter = chapter;
	}

	public String getVerse() {
		return verse;
	}

	public void setVerse(String verse) {
		this.verse = verse;
	}

	public String getBookNbr() {
		return bookNbr;
	}

	public void setBookNbr(String bookNbr) {
		this.bookNbr = bookNbr;
	}

}
