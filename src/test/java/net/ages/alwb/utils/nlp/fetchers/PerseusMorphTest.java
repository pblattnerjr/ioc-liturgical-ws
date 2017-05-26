package net.ages.alwb.utils.nlp.fetchers;

import static org.junit.Assert.*;

import org.junit.Test;

import ioc.liturgical.ws.models.db.docs.grammar.PerseusAnalyses;
import ioc.liturgical.ws.models.db.docs.grammar.PerseusAnalysis;
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
    		PerseusAnalyses analyses = pm.getAnalyses();
    		for (PerseusAnalysis analysis : analyses.analyses ) {
    			System.out.println(analysis.toExPexInterlinear(true));
    		}
        }
	}

}
