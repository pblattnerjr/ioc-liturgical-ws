package net.ages.alwb.utils.nlp.models;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

import org.ocmc.ioc.liturgical.schemas.models.supers.AbstractModel;

public class Text extends AbstractModel {
	@Expose public String text = "";
	@Expose public List<Sentence> sentences = new ArrayList<Sentence>();
	
	public Text(String text) {
		super();
		this.text = text;
	}
}
