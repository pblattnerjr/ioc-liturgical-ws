package ioc.liturgical.ws.models.db.docs.nlp;

import static org.junit.Assert.*;

import org.junit.Test;

import ioc.liturgical.ws.nlp.Utils;


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
		DependencyTree tree = new DependencyTree(id);
		tree.setNodes(Utils.initializeTokenAnalysisList(id, text));
		tree.setPrettyPrint(true);
		System.out.println(tree.toJsonString());
		assertTrue(tree.getNodes().size() == 7);
	}

}
