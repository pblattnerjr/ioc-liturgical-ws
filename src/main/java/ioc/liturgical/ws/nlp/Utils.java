package ioc.liturgical.ws.nlp;

import java.util.ArrayList;
import java.util.List;

import org.ocmc.ioc.liturgical.schemas.constants.nlp.UD_DEPENDENCY_LABELS;
import org.ocmc.ioc.liturgical.schemas.constants.nlp.UD_DEPENDENCY_LABEL_MAPPER;
import org.ocmc.ioc.liturgical.schemas.models.db.docs.nlp.TokenAnalysis;
import net.ages.alwb.gateway.utils.Tuple;
import net.ages.alwb.utils.nlp.constants.DEPENDENCY_LABELS;
import net.ages.alwb.utils.nlp.constants.DEPENDENCY_LABEL_MAPPER;
import net.ages.alwb.utils.nlp.parsers.TextParser;

public class Utils {

	/**
	 * 	Converts the text into a 
	 * dependency tree, such that each word token 
	 * is dependent on the punctuation token that 
	 * is immediately to its right.  The punctuation
	 * tokens will be dependent on the Root.
	 * 
	 * This is useful for when the dependency
	 * information has not yet been determined.

	 * @param key the key that is the ID for the text
	 * @param text the words of the text
	 * @return
	 */
	public static List<TokenAnalysis> initializeTokenAnalysisList(String key, String text) {
		List<TokenAnalysis> nodes = new ArrayList<TokenAnalysis>();
		TextParser p = new TextParser(text);
		int i = 0;
		String dependsOn = "Root";
		for (String token : p.getTokens()) {
			if (token.trim().length() > 0) {
				String label = "label";
				String gloss = "gloss tbd";
				String parse = "parse tbd";
				switch (token.trim()) {
				case ";":  {
					gloss = "?";
					parse = "PM";
					break;
				}
				case "˙":  {
					gloss = ":";
					parse = "PM";
					break;
				}
				case "·":  {
					gloss = ":";
					parse = "PM";
					break;
				}
				case "*":  {
					gloss = "*";
					parse = "PM";
					break;
				}
				default : {
				}
				}
				String keyname = UD_DEPENDENCY_LABEL_MAPPER.getLabel(token).keyname;
				if (! keyname.equals(UD_DEPENDENCY_LABELS.USP.keyname)) {
					label = keyname;
				}
				TokenAnalysis treeNode = new TokenAnalysis(
						key
						, Integer.toString(i)
						);
				treeNode.setToken(token);
				treeNode.setDependsOn(dependsOn);
				treeNode.setLemma(token);
				treeNode.setGloss(gloss);
				treeNode.setLabel(label);
				treeNode.setGrammar(parse);
				nodes.add(treeNode);
			}
			i++;
		}
		return nodes;
	}
}
