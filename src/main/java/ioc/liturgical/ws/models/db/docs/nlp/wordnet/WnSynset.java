package ioc.liturgical.ws.models.db.docs.nlp.wordnet;

import java.util.ArrayList;
import java.util.List;

import com.github.reinert.jjschema.Attributes;
import com.google.gson.annotations.Expose;

import ioc.liturgical.ws.constants.db.external.LIBRARIES;
import ioc.liturgical.ws.constants.db.external.TOPICS;
import ioc.liturgical.ws.models.db.supers.LTKDb;
import net.ages.alwb.utils.core.datastores.json.exceptions.BadIdException;

@Attributes(title = "A WordNet Synset", description = "This is a doc that records information about synset in the WordNet lexical database.")
public class WnSynset extends LTKDb {
	
		private static TOPICS topic = TOPICS.WN_LEXICAL_LEMMA;
		private static String schema = WnSynset.class.getSimpleName();
		private static double version = 1.1;
		
		@Attributes(required = true, description = "The WordNet ID for a synset,", readonly=true)
		@Expose String sid = "";

		@Attributes(required = true, description = "The gloss in a WordNet synset is a definition for a group of words that share the same sense.", readonly=true)
		@Expose String gloss = "";

		// 1=Noun, 2=Verb, 3=Adjective, 4=Adverb, 5=Adjective Satellite.
		@Attributes(required = true, description = "The type of the synset, 1=Noun, 2=Verb, 3=Adjective, 4=Adverb, 5=Adjective Satellite", readonly=true)
		@Expose int type = 0;
		
		@Expose List<WnLexicalEntry> entries = new ArrayList<WnLexicalEntry>();

		public WnSynset(
				String sid
				) throws BadIdException {
			super(
					LIBRARIES.WORDNET.toSystemDomain()
					, topic.label
					, sid
					, schema
					, version
					, topic
					);
			this.sid = sid;
		}

		public String getLemma() {
			return gloss;
		}

		public void setLemma(String lemma) {
			this.gloss = lemma;
		}

		public List<WnLexicalEntry> getEntries() {
			return entries;
		}

		public void setEntries(List<WnLexicalEntry> entries) {
			this.entries = entries;
		}
		
		public void addEntry(WnLexicalEntry entry) {
			this.entries.add(entry);
		}

}
