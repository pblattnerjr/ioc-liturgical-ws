package ioc.liturgical.ws.models.db.links;

import com.google.gson.annotations.Expose;

import ioc.liturgical.ws.annotations.UiWidget;
import ioc.liturgical.ws.constants.Constants;
import ioc.liturgical.ws.constants.ONTOLOGY_TOPICS;
import ioc.liturgical.ws.constants.RELATIONSHIP_TYPES;
import ioc.liturgical.ws.models.db.forms.LinkRefersToGroupCreateForm;
import ioc.liturgical.ws.models.db.supers.LTKLink;

import com.github.reinert.jjschema.Attributes;

/**
 * @author mac002
 *
 */
@Attributes(title = "Reference to a Group", description = "This is a doc that records information about a reference made in a text to a group of people.  For example, a liturgical text might be a hymn that refers to a country, or a kingdom, or a monastery.")
public class LinkRefersToGroup extends LTKLink {

	private static double serialVersion = 1.1;
	private static String schema = LinkRefersToGroup.class.getSimpleName();
	private static RELATIONSHIP_TYPES type = RELATIONSHIP_TYPES.REFERS_TO_GROUP;
	private static ONTOLOGY_TOPICS ontoTopic = ONTOLOGY_TOPICS.GROUP;

	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(required = false, description = "Word or phrase that makes the reference")
	@Expose public String referredByPhrase = "";

	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(required = false, description = "Comments about the reference")
	@Expose public String comments = "";

	public LinkRefersToGroup(
			String library
			, String topic
			, String key
			) {
		super(
				library
				, topic
				, key
				, LinkRefersToGroup.schema
				, LinkRefersToGroup.serialVersion
				, LinkRefersToGroup.type
				, LinkRefersToGroup.ontoTopic
				);
	}

	public LinkRefersToGroup(LinkRefersToGroupCreateForm form) {
		super(
				form.getLibrary()
				, form.getTopic()
				, form.getKey()
				, LinkRefersToGroup.schema
				, LinkRefersToGroup.serialVersion
				, LinkRefersToGroup.type
				, LinkRefersToGroup.ontoTopic
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
