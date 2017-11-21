package ioc.liturgical.ws.models.db.docs.nlp;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;

import ioc.liturgical.ws.constants.db.external.LIBRARIES;
import ioc.liturgical.ws.constants.db.external.TOPICS;
import ioc.liturgical.ws.models.db.supers.LTKDb;
import net.ages.alwb.gateway.utils.Tuple;
import net.ages.alwb.utils.nlp.constants.DEPENDENCY_LABELS;
import net.ages.alwb.utils.nlp.constants.DEPENDENCY_LABEL_MAPPER;
import net.ages.alwb.utils.nlp.parsers.TextParser;

import java.util.ArrayList;
import java.util.List;

import com.github.reinert.jjschema.Attributes;

/**
 * @author mac002
 *
 */
@Attributes(title = "DependencyTree", description = "Data for a Dependency Tree")
public class DependencyTree extends LTKDb {
	
	private static String schema = DependencyTree.class.getSimpleName();
	private static double serialVersion = 1.1;
	private static TOPICS ontologyTopic = TOPICS.DEPENDENCY_TREE;
	
	@Expose List<TokenAnalysis> nodes = new ArrayList<TokenAnalysis>();

	public DependencyTree(
			String key
			) {
		super(
				LIBRARIES.LINGUISTICS.toSystemDomain()
				, ontologyTopic.label
				, key
				, schema
				, serialVersion
				, ontologyTopic
				);
	}

	/**
	 * This constructor will convert the text into a 
	 * dependency tree, such that each word token 
	 * is dependent on the punctuation token that 
	 * is immediately to its right.  The punctuation
	 * tokens will be dependent on the Root.
	 * 
	 * This is useful for when the dependency
	 * information has not yet been determined.
	 * @param key
	 * @param text
	 */
	public DependencyTree(
			String key
			, String text
			) {
		super(
				LIBRARIES.LINGUISTICS.toSystemDomain()
				, ontologyTopic.label
				, key
				, schema
				, serialVersion
				, ontologyTopic
				);
		TextParser p = new TextParser(text);
		List<Tuple> punctuationLabels = new ArrayList<Tuple>();
		int i = 0;
		for (String token : p.getTokens()) {
			if (token.trim().length() > 0) {
				if (DEPENDENCY_LABEL_MAPPER.isPunctuation(token)) {
					Tuple tuple = new Tuple();
					tuple.setLeft(Integer.toString(i));
					tuple.setRight(DEPENDENCY_LABEL_MAPPER.getLabel(token).keyname);
					punctuationLabels.add(tuple);
				}
			}
			i++;
		}
		i = 0;
		int punctIndex = 0;
		for (String token : p.getTokens()) {
			if (token.trim().length() > 0) {
				String dependsOn = punctuationLabels.get(punctIndex).getLeft();
				String label = "label";
				String gloss = "gloss tbd";
				String parse = "parse tbd";
				if (i == Integer.parseInt(punctuationLabels.get(punctIndex).getLeft())) {
					dependsOn = "Root";
					label = punctuationLabels.get(punctIndex).getRight();
					parse = "PM";
					switch (token) {
					case ";":  {
						gloss = "?";
						break;
					}
					case "˙":  {
						gloss = ":";
						break;
					}
					case "·":  {
						gloss = ":";
						break;
					}
					default : {
						gloss = token;
					}
					}
					punctIndex++;
				} else {
					String keyname = DEPENDENCY_LABEL_MAPPER.getLabel(token).keyname;
					if (! keyname.equals(DEPENDENCY_LABELS.TBD.keyname)) {
						label = keyname;
						switch (keyname) {
						case "AuxP": {
							parse = "PREP";
							break;
						}
						  default: {
							  parse = "parse tbd";
						  }
						}
					}
				}
				TokenAnalysis treeNode = new TokenAnalysis(
						this.key
						, Integer.toString(i)
						);
				treeNode.setToken(token);
				treeNode.setDependsOn(dependsOn);
				treeNode.setLemma(token);
				treeNode.setGloss(gloss);
				treeNode.setLabel(label);
				treeNode.setGrammar(parse);
				this.nodes.add(treeNode);
			}
			i++;
		}

	}

	public List<TokenAnalysis> getNodes() {
		return nodes;
	}

	public void setNodes(List<TokenAnalysis> nodes) {
		this.nodes = nodes;
	}
	
	public List<JsonObject> nodesToJsonObjectList() {
		List<JsonObject> result = new ArrayList<JsonObject>();
		for (TokenAnalysis tokenAnalysis : this.nodes) {
			result.add(tokenAnalysis.toJsonObject());
		}
		return result;
	}
		
}
