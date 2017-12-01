package net.ages.alwb.utils.nlp.models.wordnet;

import org.ocmc.ioc.liturgical.schemas.constants.LIBRARIES;
import org.ocmc.ioc.liturgical.schemas.constants.TOPICS;
import org.ocmc.ioc.liturgical.schemas.exceptions.BadIdException;
import org.ocmc.ioc.liturgical.schemas.models.db.internal.LTK;
import org.ocmc.ioc.liturgical.schemas.models.supers.LTKDb;

import java.util.ArrayList;
import java.util.List;

import com.github.reinert.jjschema.Attributes;
import com.google.gson.annotations.Expose;

/**
 * @author mac002
 *
 */
@Attributes(title = "Lemma Form of a Word in WordNet", description = "This is a doc that records information about the lemma form of a word used in the WordNet lexical database.")
public class WnSynset extends LTK {
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

	@Attributes(required = true, description = "Phrase Type", readonly=true)
	@Expose String phraseType = "";

	@Attributes(required = true, description = "Synset Labels", readonly=true)
	@Expose List<String> labels = new ArrayList<String>();
	
	@Attributes(required = true, description = "Samples", readonly=true)
	@Expose List<String> samples = new ArrayList<String>();
	
	public WnSynset(
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

	public List<String> getLabels() {
		return labels;
	}

	public void setLabels(List<String> labels) {
		this.labels = labels;
	}

	public String getGloss() {
		return gloss;
	}

	public void setGloss(String gloss) {
		this.gloss = gloss;
	}

	public String getLexicalDomain() {
		return lexicalDomain;
	}

	public void setLexicalDomain(String lexicalDomain) {
		this.lexicalDomain = lexicalDomain;
	}

	public void addLabelsFromDelimitedString(String labels) {
		String[] parts = labels.split(";");
		for (String label : parts) {
			this.labels.add(label);
		}
	}

	public void addSamplesFromDelimitedString(String samples) {
		String[] parts = samples.split(";");
		for (String sample : parts) {
			this.samples.add(sample);
		}
	}
	public String getPhraseType() {
		return phraseType;
	}

	public void setPhraseType(String phraseType) {
		this.phraseType = phraseType;
	}

	public List<String> getSamples() {
		return samples;
	}

	public void setSamples(List<String> samples) {
		this.samples = samples;
	}


}
