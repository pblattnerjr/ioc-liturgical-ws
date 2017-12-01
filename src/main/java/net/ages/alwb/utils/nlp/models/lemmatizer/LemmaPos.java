package net.ages.alwb.utils.nlp.models.lemmatizer;

import com.google.gson.annotations.Expose;

import org.ocmc.ioc.liturgical.schemas.models.supers.AbstractModel;

public class LemmaPos extends AbstractModel {
	@Expose String lemma = "";
	@Expose String pos = "";
	@Expose String posPenn = ""; // U of Penn tree bank code
	@Expose String posWn = ""; // Wordnet string code
	@Expose String posWnNbr = ""; // Wordnet numeric code
	@Expose boolean isOxford3k = false;

	public LemmaPos(String lemma, String pos) {
		super();
		super.setPrettyPrint(false);
		this.lemma = lemma;
		this.pos = pos;
	}

	public String getLemma() {
		return lemma;
	}

	public void setLemma(String lemma) {
		this.lemma = lemma;
	}

	public String getPos() {
		return pos;
	}

	public void setPos(String pos) {
		this.pos = pos;
	}

	public String getPosPenn() {
		return posPenn;
	}

	public void setPosPenn(String posPenn) {
		this.posPenn = posPenn;
	}

	public String getPosWn() {
		return posWn;
	}

	public void setPosWn(String posWn) {
		this.posWn = posWn;
	}

	public String getPosWnNbr() {
		return posWnNbr;
	}

	public void setPosWnNbr(String posWnNbr) {
		this.posWnNbr = posWnNbr;
	}

	public boolean isOxford3k() {
		return isOxford3k;
	}

	public void setOxford3k(boolean isOxford3k) {
		this.isOxford3k = isOxford3k;
	}
}
