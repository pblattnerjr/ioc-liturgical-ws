package ioc.liturgical.ws.models.db.docs.nlp.wordnetx;

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
public class WnSynset extends LTKDb {
	private static TOPICS topic = TOPICS.WN_LEXICAL_SYNSET;
	private static String schema = WnSynset.class.getSimpleName();
	private static double version = 1.1;
	
	@Attributes(required = true, description = "WordNet ID", readonly=true)
	@Expose String wnId = "";

	@Attributes(required = true, description = "Part of Speech", readonly=true)
	@Expose String pos = "";

	@Attributes(required = true, description = "Gloss", readonly=true)
	@Expose String gloss = "";

	@Attributes(required = true, description = "Lexical Domain", readonly=true)
	@Expose String lexicalDomain = "";

	@Attributes(required = true, description = "Synset Labels", readonly=true)
	@Expose List<String> labels = new ArrayList<String>();
	
	public WnSynset(
			String wnId
			) throws BadIdException {
		super(
				LIBRARIES.WORDNET.toSystemDomain()
				, topic.label
				, wnId
				, schema
				, version
				, topic
				);
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

	public List<String> getLabels() {
		return labels;
	}

	public void setLabels(List<String> labels) {
		this.labels = labels;
	}

}
