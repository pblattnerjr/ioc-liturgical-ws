package ioc.liturgical.ws.models.db.forms;

import com.google.gson.annotations.Expose;

import ioc.liturgical.ws.constants.db.external.LIBRARIES;
import ioc.liturgical.ws.constants.db.external.TOPICS;
import ioc.liturgical.ws.models.db.supers.LTKTokenAnalysisCreateForm;

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
public class PtbWordCreateForm extends LTKTokenAnalysisCreateForm {
	
	private static String schema = PtbWordCreateForm.class.getSimpleName();
	private static double version = 1.1;
	private static TOPICS ontoTopic = TOPICS.PERSEUS_TREEBANK_WORD;

    @Expose String postag = "";

	public PtbWordCreateForm(
			String topic
			, String key
			) {
		super(
				LIBRARIES.LINGUISTICS.toSystemDomain()
				, topic
				, key
				, schema
				,  version
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
