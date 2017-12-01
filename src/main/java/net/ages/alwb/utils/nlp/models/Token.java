package net.ages.alwb.utils.nlp.models;

import com.google.gson.annotations.Expose;

import org.ocmc.ioc.liturgical.schemas.models.supers.AbstractModel;

public class Token extends AbstractModel {
	@Expose public String text = "";
	
	public Token(String text) {
		super();
		this.text = text;
	}
}
