package net.ages.alwb.utils.nlp.wordnet.models;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

import edu.mit.jwi.item.ISynset;
import org.ocmc.ioc.liturgical.schemas.models.supers.AbstractModel;

public class Synset extends AbstractModel {
	@Expose String id = ""; // id
	@Expose String gloss = ""; // wdo#gloss
	@Expose String domain = ""; // wdo#lexical_domain
	@Expose String pos = ""; // wdo#part_of_speech
//	@Expose Integer type = 0; // Returns the type of the synset, encoded as follows: 1=Noun, 2=Verb, 3=Adjective, 4=Adverb, 5=Adjective Satellite
	@Expose String words = "";


	public Synset(ISynset s) {
		super();
		super.setPrettyPrint(false);
		this.setValues(s);
	}

	public Synset(ISynset s, boolean printPretty) {
		super();
		super.setPrettyPrint(printPretty);
		this.setValues(s);
	}
	
	private void setValues(ISynset s) {
		this.id = s.getID().toString();
		this.gloss = s.getGloss();
		this.pos = String.valueOf(s.getPOS().getTag());
		this.domain = s.getLexicalFile().getName();
//		this.type = s.getType();  // it is just the numeric code for the POS, so it is redundant
	}

	public String getWords() {
		return words;
	}

	public void setWords(String words) {
		this.words = words;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGloss() {
		return gloss;
	}

	public void setGloss(String gloss) {
		this.gloss = gloss;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getPos() {
		return pos;
	}

	public void setPos(String pos) {
		this.pos = pos;
	}

	
}
