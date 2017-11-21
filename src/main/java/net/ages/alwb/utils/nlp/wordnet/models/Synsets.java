package net.ages.alwb.utils.nlp.wordnet.models;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;

import net.ages.alwb.utils.core.datastores.json.models.AbstractModel;

public class Synsets extends AbstractModel {
	private boolean printPretty = false;
	private String lemma = "";
	
	@Expose List<Synset> list = new ArrayList<Synset>();
	
	public Synsets(String lemma) {
		super();
		this.lemma = lemma;
	}

	public Synsets(boolean printPretty) {
		super();
		super.setPrettyPrint(printPretty);
	}
	
	public List<Synset> getList() {
		return list;
	}

	public void setList(List<Synset> list) {
		this.list = list;
	}
	
	public void add(Synset synset) {
		this.list.add(synset);
	}
	
}
