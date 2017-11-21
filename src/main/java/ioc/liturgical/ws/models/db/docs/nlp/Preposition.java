package ioc.liturgical.ws.models.db.docs.nlp;

import ioc.liturgical.ws.constants.nlp.PARTS_OF_SPEECH;
import ioc.liturgical.ws.models.db.supers.LTKDbGrammarAnalysis;

import com.github.reinert.jjschema.Attributes;

/**
 * @author mac002
 *
 */
@Attributes(title = "Preposition", description = "This is a doc that records information about a grammatical analysis of a preposition.")
public class Preposition extends LTKDbGrammarAnalysis {
	
	private static String schema = Preposition.class.getSimpleName();
	private static double version = 1.1;
	
	public Preposition(
			String form
			, String key
			) {
		super(form, key, schema, version);
		super.setPos(PARTS_OF_SPEECH.PREP);
	}

}
