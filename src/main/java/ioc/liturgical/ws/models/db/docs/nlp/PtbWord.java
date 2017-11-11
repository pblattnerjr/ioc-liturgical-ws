package ioc.liturgical.ws.models.db.docs.nlp;

import com.google.gson.annotations.Expose;

import ioc.liturgical.ws.constants.db.external.TOPICS;
import ioc.liturgical.ws.models.db.supers.LTKDbTokenAnalysis;

import com.github.reinert.jjschema.Attributes;

/**
 * Holds information about the grammatical analysis of a token from a text.
 * A token can be a word or a punctuation mark.
 * 
 * Note the difference between a TokenAnalysis and a WordAnalysis.
 * A TokenAnalysis is the grammatical analysis of a specific token 
 * occurring in a specific text.  A WordAnalysis has no direct association
 * with any particular text.  It is just a potential analysis for a given inflected form.
 * 
 * TODO: Note that the long-term solution is to store such information using the
 * NLP model  corresponding to the part-of-speech of the token.
 * 
 * So, this is just a temporary solution.
 * 
 * @author mac002
 * @see WordAnalysis
 *
 */
@Attributes(title = "Perseus Treebank Word Analysis", description = "Grammatical analysis of a token from the Perseus Treebank")
public class PtbWord extends LTKDbTokenAnalysis {
	
	private static String schema = PtbWord.class.getSimpleName();
	private static double version = 1.1;
	private static TOPICS ontoTopic = TOPICS.PERSEUS_TREEBANK_WORD;

    @Expose String postag = "";

	public PtbWord(
			String topic
			, String key
			) {
		super(
				topic
				, key
				, schema
				, version
				, ontoTopic
				);
	}

	public String getPostag() {
		return postag;
	}

	public void setPostag(String postag) {
		this.postag = postag;
	}

}
