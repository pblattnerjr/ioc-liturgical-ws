package ioc.liturgical.ws.models.db.supers;

import com.google.gson.annotations.Expose;

import ioc.liturgical.ws.constants.db.external.LIBRARIES;
import ioc.liturgical.ws.constants.db.external.TOPICS;
import ioc.liturgical.ws.models.db.supers.LTK;

import com.github.reinert.jjschema.Attributes;

/**
 * Provides a means to store information to render a client side node of
 * a dependency diagram.
 * 
 * Note that the long-term solution is to store such information using the
 * NLP model  corresponding to the part-of-speech of the token.
 * 
 * So, this is just a temporary solution.
 * 
 * @author mac002
 *
 */
@Attributes(title = "Tree Node", description = "Information to render a node of a depenency diagram")
public class LTKTokenAnalysisCreateForm extends LTK {
	
    @Expose String dependsOn = "";
    @Expose String token = "";
    @Expose String lemma = "";
    @Expose String gloss = "";
    @Expose String label = "";
    @Expose String gCase = "";
    @Expose String gender = "";
    @Expose String mood = "";
    @Expose String number = "";
    @Expose String person = "";
    @Expose String pos = "";
    @Expose String tense = "";
    @Expose String voice = "";
    @Expose String grammar = "";
    @Expose String refersTo = "";

	public LTKTokenAnalysisCreateForm(
			String library
			, String topic
			, String key
			, String schema
			, double serialVersion
			, TOPICS ontologyTopic
			) {
		super(
				LIBRARIES.LINGUISTICS.toSystemDomain()
				, topic
				, key
				, schema
				,  serialVersion
				, ontologyTopic
				);
	}

	public String getDependsOn() {
		return dependsOn;
	}

	public void setDependsOn(String dependsOn) {
		this.dependsOn = dependsOn;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getLemma() {
		return lemma;
	}

	public void setLemma(String lemma) {
		this.lemma = lemma;
	}

	public String getGloss() {
		return gloss;
	}

	public void setGloss(String gloss) {
		this.gloss = gloss;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getgCase() {
		return gCase;
	}

	public void setgCase(String gCase) {
		this.gCase = gCase;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getMood() {
		return mood;
	}

	public void setMood(String mood) {
		this.mood = mood;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getPerson() {
		return person;
	}

	public void setPerson(String person) {
		this.person = person;
	}

	public String getPos() {
		return pos;
	}

	public void setPos(String pos) {
		this.pos = pos;
	}

	public String getTense() {
		return tense;
	}

	public void setTense(String tense) {
		this.tense = tense;
	}

	public String getVoice() {
		return voice;
	}

	public void setVoice(String voice) {
		this.voice = voice;
	}

	public String getGrammar() {
		return grammar;
	}

	public void setGrammar(String grammar) {
		this.grammar = grammar;
	}

	public String getRefersTo() {
		return refersTo;
	}

	public void setRefersTo(String refersTo) {
		this.refersTo = refersTo;
	}

}
