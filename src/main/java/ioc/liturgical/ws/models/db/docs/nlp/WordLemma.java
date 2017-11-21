package ioc.liturgical.ws.models.db.docs.nlp;

import ioc.liturgical.ws.constants.db.external.TOPICS;
import ioc.liturgical.ws.models.db.supers.LTKDbLexicalItem;

import com.github.reinert.jjschema.Attributes;

/**
 * @author mac002
 *
 */
@Attributes(title = "Lemma Form of a Word", description = "This is a doc that records information about the lemma form of a word.  The inflected form is the word as it appears in text.  The lemma form is used to look up an inflected word in a dictionary.")
public class WordLemma extends LTKDbLexicalItem {
	private static TOPICS topic = TOPICS.WORD_LEMMA;
	private static String schema = WordLemma.class.getSimpleName();
	private static double version = 1.1;
	
	public WordLemma(
			String form
			, int frequency
			) {
		super(topic, form, frequency, schema, version);
	}

}
