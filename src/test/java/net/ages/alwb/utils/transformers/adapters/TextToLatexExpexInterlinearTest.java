package net.ages.alwb.utils.transformers.adapters;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class TextToLatexExpexInterlinearTest {

	@Test
	public void test() {
		String s = "Βυθοῦ ἀνεκάλυψε πυθμένα, καὶ διὰ ξηρᾶς οἰκείους ἕλκει, ἐν αὐτῷ κατακαλύψας ἀντιπάλους,  ὁ κραταιός, ἐν πολέμοις Κύριος˙ ὅτι δεδόξασται.";
		List<String> translations = new ArrayList<String>();
		translations.add("T 1");
		TextToLatexExpexInterlinear t = new TextToLatexExpexInterlinear(
				"gr_gr_cog~me.m01.d06~meMA.Ode1C1H.text"
				, s
				, translations
				, false
				);
		String result = t.convert();
		System.out.println(result);
		assertNotNull(result);
	}

}
