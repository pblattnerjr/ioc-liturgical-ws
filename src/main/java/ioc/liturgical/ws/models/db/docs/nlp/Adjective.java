package ioc.liturgical.ws.models.db.docs.nlp;

import ioc.liturgical.ws.constants.nlp.PARTS_OF_SPEECH;
import ioc.liturgical.ws.models.db.supers.LTKDbGrammarAnalysisGenderNumberCase;

import com.github.reinert.jjschema.Attributes;

/**
 * @author mac002
 *
 */
@Attributes(title = "Adjective", description = "This is a doc that records information about a grammatical analysis of an adjective.")
public class Adjective extends LTKDbGrammarAnalysisGenderNumberCase {
	
	private static String schema = Adjective.class.getSimpleName();
	private static double version = 1.1;
	
	public Adjective(
			String form
			, String key
			) {
		super(form, key, schema, version);
		super.setPos(PARTS_OF_SPEECH.ADJ);
	}

}
