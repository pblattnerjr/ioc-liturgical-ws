package net.ages.alwb.utils.nlp.fetchers;

public class Href {
	public String url = "";
	/**
	 * 
	 */
	public String lemma = "";

	public Href(String url, String lemma) {
		this.url = url;
		this.lemma = lemma;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getLemma() {
		return lemma;
	}

	public void setLemma(String lemma) {
		this.lemma = lemma;
	}
}
