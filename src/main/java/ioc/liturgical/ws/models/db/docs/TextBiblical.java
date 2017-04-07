package ioc.liturgical.ws.models.db.docs;

import com.google.gson.annotations.Expose;

import ioc.liturgical.ws.annotations.UiWidget;
import ioc.liturgical.ws.constants.Constants;
import ioc.liturgical.ws.constants.ONTOLOGY_TOPICS;
import ioc.liturgical.ws.models.db.supers.LTKDb;


import com.github.reinert.jjschema.Attributes;

/**
 * This class provides a POJO for use in web forms to create or update a domain
 * @author mac002
 *
 */
@Attributes(title = "TextLiturgical", description = "Properties for a Text that is of type Liturgical.")
public class TextBiblical extends LTKDb {

	private static double serialVersion = 1.1;
	private static String schema = TextBiblical.class.getSimpleName();

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

	@Attributes(required = false, readonly = true, description = "Chapter number of the text.")
	@Expose String chapter = "";

	@Attributes(required = false, readonly = true, description = "Verse number of the text.")
	@Expose String verse = "";

	@Attributes(required = false, readonly = true, description = "Sequence number for the book this text occurs in.")
	@Expose String bookNbr = "";

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
