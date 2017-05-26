package ioc.liturgical.ws.models.db.docs.grammar;

import ioc.liturgical.ws.constants.PARTS_OF_SPEECH;
import ioc.liturgical.ws.models.db.supers.LTKDbGrammarAnalysis;
import ioc.liturgical.ws.models.db.supers.LTKDbLexicalItem;

import com.github.reinert.jjschema.Attributes;

/**
 * @author mac002
 *
 */
@Attributes(title = "Lexical Form", description = "This is a doc that records information about a Greek lexical form.  The form is the word as it appears in text.  A form maps to a lemma.")
public class LexicalForm extends LTKDbLexicalItem {
	
	private static String schema = LexicalForm.class.getSimpleName();
	private static double version = 1.1;
	
	public LexicalForm(
			String form
			, int frequency
			) {
		super(form, frequency, schema, version);
	}

}
