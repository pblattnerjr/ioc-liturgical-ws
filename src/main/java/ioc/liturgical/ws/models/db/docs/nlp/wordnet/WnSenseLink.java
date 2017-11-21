package ioc.liturgical.ws.models.db.docs.nlp.wordnet;

import com.github.reinert.jjschema.Attributes;
import com.google.gson.annotations.Expose;

import ioc.liturgical.ws.constants.RELATIONSHIP_TYPES;
import ioc.liturgical.ws.constants.db.external.LIBRARIES;
import ioc.liturgical.ws.constants.db.external.TOPICS;
import ioc.liturgical.ws.models.db.docs.nlp.wordnetx.WnLexicalSense;
import ioc.liturgical.ws.models.db.supers.LTKLink;

@Attributes(title = "The sense of a word in WordNet", description = "This is a link that records information about the sense of a word used in the WordNet lexical database.")
public class WnSenseLink extends LTKLink {
	
	private static TOPICS topic = TOPICS.WN_LEXICAL_SENSE;
	private static RELATIONSHIP_TYPES type = null;
	private static String schema = WnLexicalSense.class.getSimpleName();
	private static double version = 1.1;
	
	@Attributes(required = true, description = "WordNet Word ID (the ID of its sense)", readonly=true)
	@Expose public String wid = "";

	@Attributes(required = true, description = "Sense Number", readonly=true)
	@Expose public int senseNbr = 0;
	
	@Attributes(required = true, description = "WordNet Synset ID", readonly=true)
	@Expose public String sid = "";

	public WnSenseLink(
			String wid
			, int senseNbr
			, String sid
			, RELATIONSHIP_TYPES type
			) {
		super(
				LIBRARIES.WORDNET.toSystemDomain()
				, topic.label
				, wid
				, schema
				, version
				, type
				, topic
		);
		this.wid = wid;
		this.senseNbr = senseNbr;
		this.sid = sid;
		this.type = type;
	}

}
