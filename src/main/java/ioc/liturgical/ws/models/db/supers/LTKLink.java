package ioc.liturgical.ws.models.db.supers;

import com.google.gson.annotations.Expose;

import ioc.liturgical.ws.constants.RELATIONSHIP_TYPES;
import ioc.liturgical.ws.constants.db.external.LIBRARIES;
import ioc.liturgical.ws.constants.db.external.TOPICS;

import com.github.reinert.jjschema.Attributes;

/**
 * @author mac002
 *
 */
@Attributes(title = "LTK Link", description = "Abstract Link with Properties")
public class LTKLink extends LTKDb {

	@Attributes(id="bottom",required = true, readonly = true, minLength = 1, description = "The type of link.")
	@Expose public RELATIONSHIP_TYPES type = null;
	
	public LTKLink(
			String library
			, String topic
			, String key
			, String schema
			, double serialVersion
			, RELATIONSHIP_TYPES type
			, TOPICS ontoTopic
			) {
		super(
				library
				, topic
				, key
				, schema
				,  serialVersion
				, ontoTopic
				);
		this.type = type;
	}

	public LTKLink(
			TOPICS topic
			, String schema
			, double serialVersion
			, LTKOntologyCreateFormEntry form
			) {
		super(
				LIBRARIES.ONTOLOGY.toSystemDomain()
				, topic.label
				, form.getKey()
				, schema
				,  serialVersion
				, topic
				);
	}

	public RELATIONSHIP_TYPES getType() {
		return type;
	}

	public void setType(RELATIONSHIP_TYPES type) {
		this.type = type;
	}

}
