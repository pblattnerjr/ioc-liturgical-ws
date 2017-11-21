package ioc.liturgical.ws.models.db.links;

import com.google.gson.annotations.Expose;

import ioc.liturgical.ws.annotations.UiWidget;
import ioc.liturgical.ws.constants.Constants;
import ioc.liturgical.ws.constants.RELATIONSHIP_TYPES;
import ioc.liturgical.ws.constants.db.external.TOPICS;
import ioc.liturgical.ws.models.db.forms.LinkRefersToConceptCreateForm;
import ioc.liturgical.ws.models.db.supers.LTKLink;

import com.github.reinert.jjschema.Attributes;

/**
 * @author mac002
 *
 */
@Attributes(title = "Reference to Concept", description = "This is a doc that records information about a reference made in a text to some concept.  For example, a liturgical text might be a hymn that refers to the doctrine of the Trinity.")
public class LinkRefersToConcept extends LTKLink {

	private static double serialVersion = 1.1;
	private static String schema = LinkRefersToConcept.class.getSimpleName();
	private static RELATIONSHIP_TYPES type = RELATIONSHIP_TYPES.REFERS_TO_CONCEPT;
	private static TOPICS ontoTopic = TOPICS.CONCEPT;

	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(required = false, description = "Word or phrase that makes the reference")
	@Expose public String referredByPhrase = "";

	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(required = false, description = "Comments about the reference")
	@Expose public String comments = "";

	public LinkRefersToConcept(
			String library
			, String topic
			, String key
			) {
		super(
				library
				, topic
				, key
				, LinkRefersToConcept.schema
				, LinkRefersToConcept.serialVersion
				, LinkRefersToConcept.type
				, LinkRefersToConcept.ontoTopic
				);
	}

	public LinkRefersToConcept(LinkRefersToConceptCreateForm form) {
		super(
				form.getLibrary()
				, form.getTopic()
				, form.getKey()
				, LinkRefersToConcept.schema
				, LinkRefersToConcept.serialVersion
				, LinkRefersToConcept.type
				, LinkRefersToConcept.ontoTopic
				);
		this.setReferredByPhrase(form.getReferredByPhrase());
		this.setComments(form.getComments());
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
