package ioc.liturgical.ws.models.db.links;

import com.google.gson.annotations.Expose;

import ioc.liturgical.ws.annotations.UiWidget;
import ioc.liturgical.ws.constants.Constants;
import ioc.liturgical.ws.constants.ONTOLOGY_TOPICS;
import ioc.liturgical.ws.constants.RELATIONSHIP_TYPES;
import ioc.liturgical.ws.models.db.forms.LinkRefersToRoleCreateForm;
import ioc.liturgical.ws.models.db.supers.LTKLink;

import com.github.reinert.jjschema.Attributes;

/**
 * This class provides a POJO for use in web forms to create or update a domain
 * @author mac002
 *
 */
@Attributes(title = "Reference to a Role", description = "This is a doc that records information about a reference made in a text to the role played by something or someone.")
public class LinkRefersToRole extends LTKLink {

	private static double serialVersion = 1.1;
	private static String schema = LinkRefersToRole.class.getSimpleName();
	private static RELATIONSHIP_TYPES type = RELATIONSHIP_TYPES.REFERS_TO_ROLE;
	private static ONTOLOGY_TOPICS ontoTopic = ONTOLOGY_TOPICS.ROLE;

	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(required = false, description = "Word or phrase that makes the reference")
	@Expose String referredByPhrase = "";

	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(required = false, description = "Comments about the reference")
	@Expose String comments = "";

	public LinkRefersToRole(
			String library
			, String topic
			, String key
			) {
		super(
				library
				, topic
				, key
				, LinkRefersToRole.schema
				, LinkRefersToRole.serialVersion
				, LinkRefersToRole.type
				, LinkRefersToRole.ontoTopic
				);
	}

	public LinkRefersToRole(LinkRefersToRoleCreateForm form) {
		super(
				form.getLibrary()
				, form.getTopic()
				, form.getKey()
				, LinkRefersToRole.schema
				, LinkRefersToRole.serialVersion
				, LinkRefersToRole.type
				, LinkRefersToRole.ontoTopic
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
