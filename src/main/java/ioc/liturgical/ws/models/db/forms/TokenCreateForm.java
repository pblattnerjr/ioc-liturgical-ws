package ioc.liturgical.ws.models.db.forms;

import ioc.liturgical.ws.constants.db.external.TOPICS;
import ioc.liturgical.ws.models.db.supers.LTK;

/**
 * TBD: plan is to use this to receive the data from the user
 * in order to create the necessary nodes and relationships to
 * record:
 * 
 * (:text)-1[:first]1->(:token)
 * (:text)-1[:depRoot ]m->(:token)
 * (:root)-1[:depEdge]1->(:token)
 * (:token)-1[:depEdge]0->(:token)
 * (:token)-1[:next]0->(:token)
 * (:token)-1[:parse]0->(:analysis)
 * 
 * 
 * @author mac002
 *
 */
public class TokenCreateForm extends LTK {

	private static String schema = TokenCreateForm.class.getSimpleName();
	private static double serialVersion = 1.1;
	private static TOPICS ontologyTopic = TOPICS.WORD_INFLECTED;

	public String id = "";
    public String dependsOn = "";
    public  String token = "";
    public  String lemma = "";
    public  String gloss = "";
    public String label = "";
    public String grammar = "";

    public TokenCreateForm(
    		String textId
    		, String seq
    		) {
		super(
				"TBD" // library
				, textId // topic
				, seq // key
				, schema
				, serialVersion
				, ontologyTopic
				);
	}

}
