package net.ages.alwb.utils.nlp.models;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

import net.ages.alwb.utils.core.datastores.json.models.AbstractModel;

public class Sentence extends AbstractModel {
	@Expose public String text = "";
	@Expose List<Phrase> phrases = new ArrayList<Phrase>();
	
	public Sentence(String text) {
		super();
		this.text = text;
	}
}
