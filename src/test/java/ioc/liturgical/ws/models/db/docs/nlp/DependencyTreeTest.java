package ioc.liturgical.ws.models.db.docs.nlp;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import net.ages.alwb.utils.nlp.fetchers.PerseusXmlMorph;
import net.ages.alwb.utils.nlp.parsers.TextParser;

public class DependencyTreeTest {

	/**
	 *     let nodeData =  [
        ["RootNode","", "", "", "", "", ""]
        , ["0","4", "ὁ", "ὁ", "the","ATR","DET.M.SG.NOM"]
        , ["1","4", "κραταιός", "κραταιός","mighty","ATR","ADJ.M.SG.NOM"]
        , ["2","1", "ἐν", "ἐν", "in", "AuxP","PREP"]
        , ["3","2", "πολέμοις", "πόλεμος", "wars","ATR","NOUN.M.PL.DAT"]
        , ["4","Root", "Κύριος", "κύριος", "Lord", "ST-ROOT-SUBJ","NOUN.M.SG.NOM"]
        , ["5","", "˙", "˙",  ":","APOS","PM"]
    ];

	 */
	@Test

	public void test() {
		String id = "gr_gr_cog~me.m01.d10~meMA.Kathisma11.text";
		String text = "Ὁ Δεσπότης σήμερον, ἐν Ἰορδάνῃ ἐπέστη, βαπτισθεὶς ἐν ὕδασιν, ὑπὸ τοῦ θείου Προδρόμου, ἄνωθεν ὁ Πατὴρ δὲ προσεμαρτύρει· Οὗτός ἐστιν ὁ Υἱὸς ὁ ἀγαπητός μου, καὶ τὸ Πνεῦμα ἐπεφάνη, ἐν ξένῃ θέᾳ, περιστερᾶς ἐπ' αὐτόν.";
		DependencyTree tree = new DependencyTree(id, text);
//		tree.addNode("RootNode","", "", "", "", "", "");
//		tree.addNode("0","4", "ὁ", "ὁ", "the","ATR","DET.M.SG.NOM");
//		tree.addNode("1","4", "κραταιός", "κραταιός","mighty","ATR","ADJ.M.SG.NOM");
//		tree.addNode("2","1", "ἐν", "ἐν", "in", "AuxP","PREP");
//		tree.addNode("3","2", "πολέμοις", "πόλεμος", "wars","ATR","NOUN.M.PL.DAT");
//		tree.addNode("4","Root", "Κύριος", "κύριος", "Lord", "ST-ROOT-SUBJ","NOUN.M.SG.NOM");
//		tree.addNode("5","Root", "˙", "˙",  ":","APOS","PM");
		tree.setPrettyPrint(true);
		System.out.println(tree.toJsonString());
		assertTrue(tree.getNodes().size() == 7);
	}

}
