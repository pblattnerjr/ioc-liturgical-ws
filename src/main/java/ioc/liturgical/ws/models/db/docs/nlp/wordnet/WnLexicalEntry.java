package ioc.liturgical.ws.models.db.docs.nlp.wordnet;

import ioc.liturgical.ws.constants.db.external.LIBRARIES;
import ioc.liturgical.ws.constants.db.external.TOPICS;
import ioc.liturgical.ws.models.db.supers.LTKDb;
import net.ages.alwb.utils.core.datastores.json.exceptions.BadIdException;

import java.util.ArrayList;
import java.util.List;

import com.github.reinert.jjschema.Attributes;
import com.google.gson.annotations.Expose;

/**
 * @author mac002
 *
 */
@Attributes(title = "Lemma Form of a Word in WordNet", description = "This is a doc that records information about the lemma form of a word used in the WordNet lexical database.")
public class WnLexicalEntry extends LTKDb {
	private static TOPICS topic = TOPICS.WN_LEXICAL_ENTRY;
	private static String schema = WnLexicalEntry.class.getSimpleName();
	private static double version = 1.1;
	
	@Attributes(required = true, description = "Entry", readonly=true)
	@Expose String entry = "";
	
	@Attributes(required = true, description = "Part of Speech", readonly=true)
	@Expose String pos = "";
	
	@Attributes(required = true, description = "An entry in the dictionary. A lemma will have an entry for each part of speech usage.", readonly=true)
	@Expose List<WnSenseLink> senses = new ArrayList<WnSenseLink>();
	
	public WnLexicalEntry(
			String entry
			, String pos
			) throws BadIdException {
		super(
				LIBRARIES.WORDNET.toSystemDomain()
				, topic.label
				, entry
				, schema
				, version
				, topic
				);
		this.entry = entry;
		this.pos = pos;
	}

	public void add(WnSenseLink sense) {
		senses.add(sense);
	}

	public String getEntry() {
		return entry;
	}

	public void setEntry(String entry) {
		this.entry = entry;
	}

	public List<WnSenseLink> getSenses() {
		return senses;
	}

	public void setSenses(List<WnSenseLink> senses) {
		this.senses = senses;
	}

	public String getPos() {
		return pos;
	}

	public void setPos(String pos) {
		this.pos = pos;
	}

}
