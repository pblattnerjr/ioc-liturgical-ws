package ioc.liturgical.ws.models.db.docs.nlp;

import ioc.liturgical.ws.constants.PARTS_OF_SPEECH;
import ioc.liturgical.ws.models.db.supers.LTKDbGrammarAnalysisGenderNumberCase;

import com.github.reinert.jjschema.Attributes;

/**
 * @author mac002
 *
 */
@Attributes(title = "Noun", description = "This is a doc that records information about a grammatical analysis of a noun.")
public class Noun extends LTKDbGrammarAnalysisGenderNumberCase {
	
	private static String schema = Noun.class.getSimpleName();
	private static double version = 1.1;
	
	public Noun(
			String form
			, String key
			) {
		super(form, key, schema, version);
		super.setPos(PARTS_OF_SPEECH.NOUN);
	}

}
