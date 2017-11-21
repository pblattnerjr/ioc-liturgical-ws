package ioc.liturgical.ws.models.db.docs.nlp.wordnet;

import java.util.ArrayList;
import java.util.List;

import com.github.reinert.jjschema.Attributes;
import com.google.gson.annotations.Expose;

import ioc.liturgical.ws.constants.db.external.LIBRARIES;
import ioc.liturgical.ws.constants.db.external.TOPICS;
import ioc.liturgical.ws.models.db.supers.LTKDb;
import net.ages.alwb.utils.core.datastores.json.exceptions.BadIdException;

@Attributes(title = "Lemma Form of a Word in WordNet", description = "This is a doc that records information about the lemma form of a word used in the WordNet lexical database.")
public class WnLemma extends LTKDb {
	
		private static TOPICS topic = TOPICS.WN_LEXICAL_LEMMA;
		private static String schema = WnLemma.class.getSimpleName();
		private static double version = 1.1;
		
		@Attributes(required = true, description = "The lemma is the form of a word used to look it up in a dictionary.", readonly=true)
		@Expose String lemma = "";
		@Expose List<WnLexicalEntry> entries = new ArrayList<WnLexicalEntry>();

		public WnLemma(
				String lemma
				) throws BadIdException {
			super(
					LIBRARIES.WORDNET.toSystemDomain()
					, topic.label
					, lemma
					, schema
					, version
					, topic
					);
			this.lemma = lemma;
		}

		public String getLemma() {
			return lemma;
		}

		public void setLemma(String lemma) {
			this.lemma = lemma;
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
