package net.ages.alwb.utils.nlp.models;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

import org.ocmc.ioc.liturgical.schemas.models.supers.AbstractModel;

public class Sentence extends AbstractModel {
	@Expose public String text = "";
	@Expose List<Phrase> phrases = new ArrayList<Phrase>();
	
	public Sentence(String text) {
		super();
		this.text = text;
	}
}
