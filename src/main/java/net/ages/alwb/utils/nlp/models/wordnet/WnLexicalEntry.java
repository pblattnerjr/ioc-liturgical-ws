package net.ages.alwb.utils.nlp.models.wordnet;


import java.util.ArrayList;
import java.util.List;

import org.ocmc.ioc.liturgical.schemas.constants.LIBRARIES;
import org.ocmc.ioc.liturgical.schemas.constants.TOPICS;
import org.ocmc.ioc.liturgical.schemas.exceptions.BadIdException;
import org.ocmc.ioc.liturgical.schemas.models.db.internal.LTK;

import com.github.reinert.jjschema.Attributes;
import com.google.gson.annotations.Expose;

/**
 * @author mac002
 *
 */
@Attributes(title = "Lemma Form of a Word in WordNet", description = "This is a doc that records information about the lemma form of a word used in the WordNet lexical database.")
public class WnLexicalEntry extends LTK {
	private static TOPICS topic = TOPICS.WN_LEXICAL_ENTRY;
	private static String schema = WnLexicalEntry.class.getSimpleName();
	private static double version = 1.1;
	
	@Attributes(required = true, description = "Wordnet ID", readonly=true)
	@Expose String wnId = "";
	@Attributes(required = true, description = "Part of Speech", readonly=true)
	@Expose String pos = "";
	@Attributes(required = true, description = "Lexical forms of the Entry", readonly=true)
	@Expose List<String> forms = new ArrayList<String>();
	
	public WnLexicalEntry(
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

	public String getPos() {
		return pos;
	}

	public void setPos(String pos) {
		this.pos = pos;
	}

	public List<String> getForms() {
		return forms;
	}

	public void setForms(List<String> forms) {
		this.forms = forms;
	}
	
	public void addFormsFromDelimitedString(String forms) {
		String[] parts = forms.split(";");
		for (String form : parts) {
			this.forms.add(form);
		}
	}

}
