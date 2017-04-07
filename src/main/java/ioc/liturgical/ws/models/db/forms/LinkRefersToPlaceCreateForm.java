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
@Attributes(title = "Reference to Place", description = "This is a doc that records information about a reference made in a text to some place.  For example, a liturgical text might be a hymn that refers to the Jordan River.")
public class LinkRefersToPlaceCreateForm extends LTKLinkCreateForm {
	
	private static double serialVersion = 1.1;
	private static String schema = LinkRefersToPlaceCreateForm.class.getSimpleName();
	private static RELATIONSHIP_TYPES type = RELATIONSHIP_TYPES.REFERS_TO_PLACE;
	private static ONTOLOGY_TOPICS ontoTopic = ONTOLOGY_TOPICS.PLACE;

	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(required = false, description = "Word or phrase that makes the reference")
	@Expose String referredByPhrase = "";

	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(required = false, description = "Comments on the reference")
	@Expose String comments = "";

	public LinkRefersToPlaceCreateForm(
			String library
			, String topic
			, String key
			) {
		super(
				library
				, topic
				, key
				, LinkRefersToPlaceCreateForm.schema
				,  LinkRefersToPlaceCreateForm.serialVersion
				, LinkRefersToPlaceCreateForm.type
				, LinkRefersToPlaceCreateForm.ontoTopic
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
