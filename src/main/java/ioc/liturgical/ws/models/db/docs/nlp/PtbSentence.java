package ioc.liturgical.ws.models.db.docs.nlp;

import com.google.gson.annotations.Expose;

import ioc.liturgical.ws.annotations.UiWidget;
import ioc.liturgical.ws.constants.Constants;
import ioc.liturgical.ws.constants.ID_PART_TYPES;
import ioc.liturgical.ws.constants.db.external.LIBRARIES;
import ioc.liturgical.ws.constants.db.external.TOPICS;
import ioc.liturgical.ws.models.db.supers.LTKDb;
import net.ages.alwb.utils.core.error.handling.ErrorUtils;
import net.ages.alwb.utils.core.misc.AlwbGeneralUtils;

import java.text.Normalizer;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.reinert.jjschema.Attributes;

/**
 * This class provides a POJO for the text of a sentence from the Perseus Treebank.
 * @author mac002
 *
 */
@Attributes(title = "Perseus Treebank Sentence Text", description = "Properties for the text of a sentence from the Perseus Treebank.")
public class PtbSentence extends LTKDb {
	private static final Logger logger = LoggerFactory.getLogger(PtbSentence.class);

	private static double serialVersion = 1.1;
	private static String schema = PtbSentence.class.getSimpleName();
	private static TOPICS ontoTopic = TOPICS.PERSEUS_TREEBANK_SENTENCE;
	private static Pattern punctPattern = Pattern.compile("[;˙·,.;!?\\-(){}\\[\\]/:<>%͵·\"'`’_«»‘*•+…‧′|]"); // punctuation

	@Attributes(id = "top", required = false, readonly = true, description = "Author of the sentence.")
	@Expose  public String author = "";

	@Attributes(id = "top", required = false, readonly = true, description = "Title of the text that contains the sentence.")
	@Expose  public String title = "";

	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(required = true, description = "The value of the text.")
	@Expose  public String value = "";

	@Attributes(id = "bottom", required = false, readonly = true, description = "Normalized text with punctuation.")
	@Expose  public String nwp = "";

	@Attributes(id = "bottom", required = false, readonly = true, description = "Normalized text with no punctuation.")
	@Expose  public String nnp = "";

	@Attributes(id = "bottom", required = false, readonly = true, description = "Value without punctuation.")
	@Expose  public String vnp = "";

	@Attributes(id = "bottom", required = false, readonly = true, description = "Line sequence number for this text within its topic.")
	@Expose  public String seq = "";

	public PtbSentence(
			String topic
			, String key
			) {
		super(
				LIBRARIES.LINGUISTICS.toSystemDomain()
				, topic
				, "s" + key
				, schema
				, serialVersion
				, ontoTopic
				);
		this.partTypeOfTopic = ID_PART_TYPES.USER_TEXT;
		this.partTypeOfKey = ID_PART_TYPES.USER_TEXT;
	}
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = AlwbGeneralUtils.toNfc(value);
		try {
			this.nwp = Normalizer.normalize(value, Normalizer.Form.NFD)
					.replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();
			this.nnp = punctPattern.matcher(this.nwp).replaceAll("");
			this.vnp = punctPattern.matcher(this.value).replaceAll("").toLowerCase();
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

	public String getVnp() {
		return vnp;
	}

	public void setVnp(String vnp) {
		this.vnp = vnp;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
}
