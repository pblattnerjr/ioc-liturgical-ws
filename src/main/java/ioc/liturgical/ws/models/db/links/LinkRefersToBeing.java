package ioc.liturgical.ws.models.db.links;

import com.google.gson.annotations.Expose;

import ioc.liturgical.ws.annotations.UiWidget;
import ioc.liturgical.ws.constants.Constants;
import ioc.liturgical.ws.constants.ONTOLOGY_TOPICS;
import ioc.liturgical.ws.constants.RELATIONSHIP_TYPES;
import ioc.liturgical.ws.models.db.forms.LinkRefersToBeingCreateForm;
import ioc.liturgical.ws.models.db.supers.LTKLink;

import com.github.reinert.jjschema.Attributes;

/**
 * This class provides a POJO for use in web forms to create or update a domain
 * @author mac002
 *
 */
@Attributes(title = "Reference to a being", description = "This is a doc that records information about a reference made in a text to a being.  For example, a liturgical text might be a hymn that refers to God, or an angel.  Note that there are some subtypes of being: animal, human, plant.  Use the subytype if applicable.")
public class LinkRefersToBeing extends LTKLink {

	private static double serialVersion = 1.1;
	private static String schema = LinkRefersToBeing.class.getSimpleName();
	private static RELATIONSHIP_TYPES type = RELATIONSHIP_TYPES.REFERS_TO_BEING;
	private static ONTOLOGY_TOPICS ontoTopic = ONTOLOGY_TOPICS.BEING;

	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(required = false, description = "Word or phrase that makes the reference")
	@Expose String referredByPhrase = "";

	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(required = false, description = "Comments about the reference")
	@Expose String comments = "";

	public LinkRefersToBeing(
			String library
			, String topic
			, String key
			) {
		super(
				library
				, topic
				, key
				, LinkRefersToBeing.schema
				, LinkRefersToBeing.serialVersion
				, LinkRefersToBeing.type
				, LinkRefersToBeing.ontoTopic
				);
	}

	public LinkRefersToBeing(LinkRefersToBeingCreateForm form) {
		super(form.getLibrary()
				, form.getTopic()
				, form.getKey()
				, LinkRefersToBeing.schema
				, LinkRefersToBeing.serialVersion
				, LinkRefersToBeing.type
				, LinkRefersToBeing.ontoTopic
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
