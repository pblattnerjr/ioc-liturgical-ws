package net.ages.alwb.utils.nlp.models.wordnet;

import org.ocmc.ioc.liturgical.schemas.constants.LIBRARIES;
import org.ocmc.ioc.liturgical.schemas.constants.TOPICS;
import org.ocmc.ioc.liturgical.schemas.exceptions.BadIdException;
import org.ocmc.ioc.liturgical.schemas.models.db.internal.LTK;
import org.ocmc.ioc.liturgical.schemas.models.supers.LTKDb;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.github.reinert.jjschema.Attributes;
import com.google.gson.annotations.Expose;

/**
 * @author mac002
 *
 */
@Attributes(title = "Lemma Form of a Word in WordNet", description = "This is a doc that records information about the lemma form of a word used in the WordNet lexical database.")
public class WnLexicalSense extends LTK {
	private static TOPICS topic = TOPICS.WN_LEXICAL_SENSE;
	private static String schema = WnLexicalSense.class.getSimpleName();
	private static double version = 1.1;
	
	@Attributes(required = true, description = "WordNet ID", readonly=true)
	@Expose public String wnId = "";

	@Attributes(required = true, description = "Gloss", readonly=true)
	@Expose public String gloss = "";
	
	@Attributes(required = true, description = "Sense Number", readonly=true)
	@Expose public String senseNbr = "";
	
	@Attributes(required = true, description = "Lexical ID", readonly=true)
	@Expose public String lexId = "";
	
	@Attributes(required = true, description = "Tag Count", readonly=true)
	@Expose public int tagCount = 0;
	
	public WnLexicalSense(
			String wnId
			) throws BadIdException {
		super(
				LIBRARIES.WORDNET.toSystemDomain()
				, topic.label
				, wnId
				);
		super.disableHtmlEscaping();
			this.wnId = wnId;
	}

	public String getWnId() {
		return wnId;
	}

	public void setWnId(String wnId) {
		this.wnId = wnId;
	}

	public String getGloss() {
		return gloss;
	}

	public void setGloss(String gloss) {
		this.gloss = gloss;
	}

	public String getSenseNbr() {
		return senseNbr;
	}

	public void setSenseNbr(String senseNbr) {
		this.senseNbr = senseNbr;
	}

	public String getLexId() {
		return lexId;
	}

	public void setLexId(String lexId) {
		this.lexId = lexId;
	}

	public int getTagCount() {
		return tagCount;
	}

	public void setTagCount(int i) {
		this.tagCount = i;
	}

}
