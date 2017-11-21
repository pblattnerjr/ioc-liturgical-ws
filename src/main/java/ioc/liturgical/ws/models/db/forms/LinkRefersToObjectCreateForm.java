package ioc.liturgical.ws.models.db.forms;

import com.google.gson.annotations.Expose;

import ioc.liturgical.ws.annotations.UiWidget;
import ioc.liturgical.ws.constants.Constants;
import ioc.liturgical.ws.constants.ID_PART_TYPES;
import ioc.liturgical.ws.constants.RELATIONSHIP_TYPES;
import ioc.liturgical.ws.constants.db.external.TOPICS;
import ioc.liturgical.ws.models.db.supers.LTKLinkCreateForm;

import com.github.reinert.jjschema.Attributes;

/**
 * @author mac002
 *
 */
@Attributes(title = "Reference to Object", description = "This is a doc that records information about a reference made in a text to some object.  For example, a liturgical text might be a hymn that refers to the Cross upon which Our Lord was crucified.")
public class LinkRefersToObjectCreateForm extends LTKLinkCreateForm {
	
	private static double serialVersion = 1.1;
	private static String schema = LinkRefersToObjectCreateForm.class.getSimpleName();
	private static RELATIONSHIP_TYPES type = RELATIONSHIP_TYPES.REFERS_TO_OBJECT;
	private static TOPICS ontoTopic = TOPICS.OBJECT;

	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(required = false, description = "Word or phrase that makes the reference")
	@Expose public String referredByPhrase = "";

	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(required = false, description = "Comments on the reference")
	@Expose public String comments = "";

	public LinkRefersToObjectCreateForm(
			String library
			, String topic
			, String key
			) {
		super(
				library
				, topic
				, key
				, LinkRefersToObjectCreateForm.schema
				,  LinkRefersToObjectCreateForm.serialVersion
				, LinkRefersToObjectCreateForm.type
				, LinkRefersToObjectCreateForm.ontoTopic
				);
		this.partTypeOfTopic = ID_PART_TYPES.ID_OF_SELECTED_LITURGICAL_TEXT;
		this.partTypeOfKey = ID_PART_TYPES.ID_OF_SELECTED_ONTOLOGY_INSTANCE;
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
