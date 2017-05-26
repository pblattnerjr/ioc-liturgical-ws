package ioc.liturgical.ws.models.db.docs.grammar;

import ioc.liturgical.ws.constants.PARTS_OF_SPEECH;
import ioc.liturgical.ws.models.db.supers.LTKDbGrammarAnalysis;

import com.github.reinert.jjschema.Attributes;

/**
 * @author mac002
 *
 */
@Attributes(title = "Conjunction", description = "This is a doc that records information about a grammatical analysis of a conjunction.")
public class Conjunction extends LTKDbGrammarAnalysis {
	
	private static String schema = Conjunction.class.getSimpleName();
	private static double version = 1.1;
	
	public Conjunction(
			String form
			, String key
			) {
		super(form, key, schema, version);
		super.setPos(PARTS_OF_SPEECH.CONJ);
	}

}
