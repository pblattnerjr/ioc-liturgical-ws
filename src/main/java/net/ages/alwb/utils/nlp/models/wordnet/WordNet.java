package net.ages.alwb.utils.nlp.models.wordnet;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

import org.ocmc.ioc.liturgical.schemas.models.supers.AbstractModel;

public class WordNet extends AbstractModel {
	@Expose public List<WnLexicalEntry> entries = new ArrayList<WnLexicalEntry>();
	@Expose public List<WnLexicalSense> senses = new ArrayList<WnLexicalSense>();
	@Expose public List<WnSynset> synsets = new ArrayList<WnSynset>();
	
	public WordNet() {
		super();
		super.disableHtmlEscaping();
	}
	public List<WnLexicalEntry> getEntries() {
		return entries;
	}
	public void setEntries(List<WnLexicalEntry> entries) {
		this.entries = entries;
	}
	public List<WnLexicalSense> getSenses() {
		return senses;
	}
	public void setSenses(List<WnLexicalSense> senses) {
		this.senses = senses;
	}
	public List<WnSynset> getSynsets() {
		return synsets;
	}
	public void setSynsets(List<WnSynset> synsets) {
		this.synsets = synsets;
	}
	public void addEntry(WnLexicalEntry entry) {
		this.entries.add(entry);
	}
	public void addSense(WnLexicalSense sense) {
		this.senses.add(sense);
	}
	public void addSynset(WnSynset synset) {
		this.synsets.add(synset);
	}

}
