package net.ages.alwb.utils.nlp.models;

import com.google.gson.annotations.Expose;

import net.ages.alwb.utils.core.datastores.json.models.AbstractModel;

public class Token extends AbstractModel {
	@Expose public String text = "";
	
	public Token(String text) {
		super();
		this.text = text;
	}
}
