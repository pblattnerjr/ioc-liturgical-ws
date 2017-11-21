package net.ages.alwb.utils.nlp.fetchers;

import static org.junit.Assert.*;

import org.junit.Test;

import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.tokenize.Tokenizer;

public class LexigramTest {

	@Test
	public void test() {
		String s = "ὅ Βυθοῦ ἀνεκάλυψε πυθμένα, καὶ διὰ ξηρᾶς οἰκείους ἕλκει, ἐν αὐτῷ κατακαλύψας ἀντιπάλους,  ὁ κραταιός, ἐν πολέμοις Κύριος˙ ὅτι δεδόξασται.";
		Tokenizer tokenizer = SimpleTokenizer.INSTANCE;
        String [] theTokens = tokenizer.tokenize(s);
        for (String token : theTokens) {
        	Lexigram l = new Lexigram(token);
        }
	}

}
