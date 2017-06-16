package ioc.liturgical.ws.models.db.docs.nlp;

import ioc.liturgical.ws.constants.db.external.LIBRARIES;
import ioc.liturgical.ws.constants.db.external.SINGLETON_KEYS;
import ioc.liturgical.ws.constants.db.external.TOPICS;
import ioc.liturgical.ws.models.db.supers.LTKDb;

import com.github.reinert.jjschema.Attributes;
import com.google.common.base.Strings;
import com.google.gson.annotations.Expose;

/**
 * @author mac002
 *
 */
@Attributes(title = "Concordance Line", description = "This is a doc that records information about a line of a concordance.  It only shows a part of the text it was taken from. It shows a specific number of characters that occur before and after the form.")
public class ConcordanceLine extends LTKDb {
	
	private static String schema = ConcordanceLine.class.getSimpleName();
	private static double version = 1.1;
	private static TOPICS topic = TOPICS.TEXT_CONCORDANCE;
	
	@Attributes(required = true, readonly=true, description = "The inflected word form that is the subject of the concordance")
	@Expose public String  form = "";
	
	@Attributes(required = true, readonly=true, description = "The sequence number of this line.")
	@Expose public int  seq = 0;

	@Attributes(required = true, readonly=true, description = "The number of characters shown in the context.")
	@Expose public int  width = 0;

	@Attributes(required = true, readonly=true, description = "The ID of the text from which this part of the line comes.")
	@Expose public String  textId = "";

	@Attributes(required = true, readonly=true, description = "A limited number of characters that occur to the left of the form in the text from which it is taken.  If the form is at the beginning of the text, this will be empty.")
	@Expose public String  contextLeft = "";

	@Attributes(required = true, readonly=true, description = "A limited number of characters that occur to the right of the form in the text from which it is taken.  If the form is at the end of the text, this will be empty.")
	@Expose public String  contextRight = "";

	public ConcordanceLine(
			String form
			, int seq
			, int width
			, String textId
			, String contextLeft
			, String contextRight
			) {
		super (
				LIBRARIES.LINGUISTICS.toSystemDomain()
				, topic.label
				, form + ":" + String.format("%03d", seq)
				, schema
				, version
				, topic
				);
		this.form = form;
		this.seq = seq;
		this.width = width;
		this.textId = id;
		this.contextLeft = contextLeft;
		this.contextRight = contextRight;
	}

	public String getForm() {
		return form;
	}

	public void setForm(String form) {
		this.form = form;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public String getTextId() {
		return textId;
	}

	public void setTextId(String textId) {
		this.textId = textId;
	}

	public String getContextLeft() {
		return contextLeft;
	}

	public void setContextLeft(String contextLeft) {
		this.contextLeft = contextLeft;
	}

	public String getContextRight() {
		return contextRight;
	}

	public void setContextRight(String contextRight) {
		this.contextRight = contextRight;
	}
	
	/**
	 * Returns left context, token, right context
	 * @param tokenDelimiter - character to mark the start and end of the token to make it easier to spot
	 * @return
	 */
	public String toPaddedString(String tokenDelimiter) {
		return Strings.padStart(
				this.contextLeft, width/2, ' ') 
				+" " 
				+ tokenDelimiter 
				+ tokenDelimiter 
				+ " " 
				+ this.form 
				+" " 
				+ tokenDelimiter 
				+ tokenDelimiter 
				+ " " 
				+ Strings.padEnd(this.contextRight, width/2, ' ')
				;
	}

	public String toString(String tokenDelimiter) {
		return this.contextLeft
				+" " 
				+ tokenDelimiter 
				+ tokenDelimiter 
				+ " " 
				+ this.form 
				+" " 
				+ tokenDelimiter 
				+ tokenDelimiter 
				+ " " 
				+ this.contextRight
				;
	}

	public String toString() {
		return this.contextLeft
				+ this.form 
				+ this.contextRight
				;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

}
