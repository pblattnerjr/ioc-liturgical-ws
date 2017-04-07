package ioc.liturgical.ws.models.db.forms;

import com.google.gson.annotations.Expose;

import ioc.liturgical.ws.annotations.UiWidget;
import ioc.liturgical.ws.constants.Constants;
import ioc.liturgical.ws.constants.ONTOLOGY_TOPICS;
import ioc.liturgical.ws.constants.RELATIONSHIP_TYPES;
import ioc.liturgical.ws.models.db.supers.LTKLinkCreateForm;

import com.github.reinert.jjschema.Attributes;

/**
 * @author mac002
 *
 */
@Attributes(title = "Reference to Concept", description = "This is a doc that records information about a reference made in a text to some concept.  For example, a liturgical text might be a hymn that refers to the doctrine of the Trinity.")
public class LinkRefersToConceptCreateForm extends LTKLinkCreateForm {
	
	private static double serialVersion = 1.1;
	private static String schema = LinkRefersToConceptCreateForm.class.getSimpleName();
	private static RELATIONSHIP_TYPES type = RELATIONSHIP_TYPES.REFERS_TO_CONCEPT;
	private static ONTOLOGY_TOPICS ontoTopic = ONTOLOGY_TOPICS.CONCEPT;

	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(required = false, description = "Word or phrase that makes the reference")
	@Expose String referredByPhrase = "";

	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(required = false, description = "Comments on the reference")
	@Expose String comments = "";

	public LinkRefersToConceptCreateForm(
			String library
			, String topic
			, String key
			) {
		super(
				library
				, topic
				, key
				, LinkRefersToConceptCreateForm.schema
				, LinkRefersToConceptCreateForm.serialVersion
				, LinkRefersToConceptCreateForm.type
				, LinkRefersToConceptCreateForm.ontoTopic
				);
	}
	
	public String getReferredByPhrase() {
		return referredByPhrase;
	}

	public void setReferredByPhrase(String referredByPhrase) {
		this.referredByPhrase = referredByPhrase;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

}
