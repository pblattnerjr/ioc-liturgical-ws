package net.ages.alwb.utils.nlp.fetchers;

import static org.junit.Assert.*;

import org.junit.Test;

import ioc.liturgical.ws.models.db.docs.nlp.WordAnalyses;
import ioc.liturgical.ws.models.db.docs.nlp.WordAnalysis;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.tokenize.Tokenizer;

public class PerseusMorphTest {

	@Test
	public void test() {
		String s = "ὅ Βυθοῦ ἀνεκάλυψε πυθμένα, καὶ διὰ ξηρᾶς οἰκείους ἕλκει, ἐν αὐτῷ κατακαλύψας ἀντιπάλους,  ὁ κραταιός, ἐν πολέμοις Κύριος˙ ὅτι δεδόξασται.";
		Tokenizer tokenizer = SimpleTokenizer.INSTANCE;
        String [] theTokens = tokenizer.tokenize(s);
        for (String token : theTokens) {
    		PerseusMorph pm = new PerseusMorph(token);
    		WordAnalyses analyses = pm.getAnalyses();
    		for (WordAnalysis analysis : analyses.analyses ) {
    			System.out.println(analysis.toExPexInterlinear(true));
    		}
        }
	}

}
