package ioc.liturgical.ws.models.db.supers;

import com.google.gson.annotations.Expose;

import ioc.liturgical.ws.constants.EXTERNAL_DB_LIBS;
import ioc.liturgical.ws.constants.ONTOLOGY_TOPICS;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.github.reinert.jjschema.Attributes;

/**
 * @author mac002
 *
 */
@Attributes(title = "LTKDb Grammar Lexicon", description = "Abstract Lexicon")
public class LTKDbLexicalItem extends LTKDb {
	
	private static final String punct = "[;˙·,.;!?\\-(){}\\[\\]\\/:<>%͵·\"'`’_«»‘*•+…‧′|]";
	private static final Pattern punctPattern = Pattern.compile(punct); // punctuation

	
	@Attributes(id="bottom", readonly=true, description="The normalized form (lowercase, no accents or punctuation")
	@Expose public String nnp;

	@Attributes(id="top", readonly=true, description="The number of times it occurs in the database")
	@Expose public int frequency = 0;

	@Attributes(id="top", readonly=true, description="The ID of an example of where this word occurs")
	@Expose public String exampleId;

	@Attributes(id="top", readonly=true, description="The letters occurring to the immediate left of the word in an example")
	@Expose public String exampleLeftContext;

	@Attributes(id="top", readonly=true, description="The letters occurring to the immediate right of the word in an example")
	@Expose public String exampleRightContext;

	/**
	 * 
	 * @param key the lemma or one of its forms
	 * @param frequency the number of times it occurs in the database
	 * @param schema the schema for this record type
	 * @param serialVersion the version number for this record type
	 */
	public LTKDbLexicalItem(
			ONTOLOGY_TOPICS topic // becomes the topic
			, String form // becomes the key
			, int frequency
			, String schema
			, double serialVersion
			) {
		super (
				EXTERNAL_DB_LIBS.LINGUISTICS.toSystemDomain()
				, topic.keyname
				, form
				, schema
				, serialVersion
				, topic
				);
		this.frequency = frequency;
		this.setNnp(form);
	}
	
	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	public String getNnp() {
		return nnp;
	}

	public void setNnp(String value) {
		this.nnp = Normalizer.normalize(value, Normalizer.Form.NFD)
				.replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();
    	this.nnp = punctPattern.matcher(this.nnp).replaceAll("");
	}

	public String getExampleId() {
		return exampleId;
	}

	public void setExampleId(String exampleId) {
		this.exampleId = exampleId;
	}

	public String getExampleLeftContext() {
		return exampleLeftContext;
	}

	public void setExampleLeftContext(String exampleLeftContext) {
		this.exampleLeftContext = exampleLeftContext;
	}

	public String getExampleRightContext() {
		return exampleRightContext;
	}

	public void setExampleRightContext(String exampleRightContext) {
		this.exampleRightContext = exampleRightContext;
	}

}
