package net.ages.alwb.utils.transformers.adapters;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.ocmc.ioc.liturgical.utils.ErrorUtils;
import net.ages.alwb.utils.core.id.managers.IdManager;
import net.ages.alwb.utils.nlp.constants.DEPENDENCY_LABEL_MAPPER;
import net.ages.alwb.utils.nlp.fetchers.PerseusXmlMorph;
import net.ages.alwb.utils.nlp.parsers.TextParser;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.tokenize.Tokenizer;

/**
 * Converts a line of text to the format of a Latex ExPex interlinear entry.
 * 
 * The text is tokenized.  A token is a word, number, or punctuation mark.
 * 
 * The text will be broken into phrases and clauses.  Each token 
 * has a sequential numeric identifier.  There is an option to restart
 * the numbers for each new clause, or carry them over from the
 * previous.  The advantage of the latter is that it allows for adding
 * information about a reference made by a token in one clause to
 * a token in another clause.
 * 
 * Currently it is written only to handle keys that contain meMAOde.
 * TODO: write patterns and handlers for other types
 * 
 * @author mac002
 *
 */
public class TextToLatexExpexInterlinear {
	private static final Logger logger = LoggerFactory.getLogger(TextToLatexExpexInterlinear.class);

	private static Pattern odePattern = Pattern.compile("Ode([0-9])C([0-9])(.?)");
	public static final String newPage = "\\newpage\n";
	public static final String sectionGramParaText = "\\subsubsection{\\gramParaText}\n";
	public static final String sectionGramInterlinearText = "\\subsubsection{\\gramInterlinearText}\n";
	
	public static final String ExPexExampleTagOpen = "\\pex ";
	public static final String ExPexExampleClauseTagOpen = "\n\\a "; // "\n\\a\n\n"
	public static final String ExPexExampleTagClose = "\\xe\n\n";
	
	public static final String ExPexGlossTagOpen = "\n\n\\begingl[glstyle=nlevel]\n";
	public static final String ExPexFreeTranslationTag = "\\glft\n";
	public static final String ExPexGlossTagClose = "\\endgl\n";
	
	public static final String ExPexGlossLexemeOpen = "[";
	public static final String ExPexGlossLexemeClose = "///]\n";
	
	public static final String SourceTagOpen = "\n\\ltSource{";
	public static final String SourceTagClose = "}\n\n";

	public static final String underscore = "\\\\textunderscore ";
	
	// Dependency Diagram
	public static final String dependencyBegin = "\n\n\\begin{dependency}[theme=brazil]";
	public static final String dependencyEnd = "\n\\end{dependency}";
	
	public static final String depTextBegin = "\n\\begin{deptext}[column sep=0.2cm]";
	public static final String depTextEnd = "\n\\end{deptext}";
	
	// class properties
	private String id = "";
	private IdManager idManager = null;
	private String text = "";
	private boolean newClauseRestartsNumbers = false;
	private String library = "";
	private String topic = "";
	private String key = "";
	private String odeNbr = "";
	private String keyType = "";
	private String canonNbr = "";
	private String troparionNbr = "";
	private String lineNbr = "";
	private String textRole = "";
	private String[] translations = new String[0];
	private boolean isOde = false;
	private String firstWord = "";
	private Map<String,PerseusXmlMorph> parses = new TreeMap<String,PerseusXmlMorph>();

	/**
	 * 
	* @param id - the domain + topic + key of the text
	 * @param text - the source text value
	 * @param translations - an array of translations of the source text
	 * @param newClauseRestartsNumbers - if true, the first word of each clause will be set to 1.  If false, the first number will be a continuation from the previous clause.
	 */
	public TextToLatexExpexInterlinear ( 
		String id
		, String text
		, List<String> translations
		, boolean newClauseRestartsNumbers
		, boolean getPerseusParses
		) {
		this.id = id;
		this.idManager = new IdManager(id);
		try {
			this.library = idManager.getLibrary();
			this.topic = idManager.getTopic();
			this.key = idManager.getKey();
			parseKey();
			this.text = text;
			this.translations = translations.toArray(new String[0]);
			this.newClauseRestartsNumbers = newClauseRestartsNumbers;
			if (getPerseusParses) {
				TextParser tp = new TextParser(text);
				parses = tp.parse();
			}
		} catch (Exception e) {
			ErrorUtils.report(logger, e);
		}
	}
	
	
	/**
	 * Pick out the meaningful parts of the key
	 * and figure out what they mean.
	 * @throws Exception 
	 */
	private void parseKey() {
		if (key.toLowerCase().contains("ode")) {
			isOde = true;
				String[] parts = key.split("\\.");
				if (parts.length == 3) {
					keyType = parts[2];
					Matcher matcher = odePattern.matcher(parts[1]);
					if (matcher.matches()) {
						String group3 = matcher.group(3).toLowerCase();
						odeNbr = matcher.group(1);
						canonNbr = matcher.group(2);
						if (group3.startsWith("acrostic")) {
							textRole = "\\canonPartAcrostic";
						} else if (group3.startsWith("h")) {
							textRole = "\\canonPartHeirmos";
						} else if (StringUtils.isNumeric(group3)) {
							textRole = "\\canonPartTroparion";
							troparionNbr = group3.substring(0, 1);
						} else {
							switch (keyType) {
							case "ode" : {
								textRole = "\\canonPartOde";
								break;
							}
							case "melody" : {
								textRole = "\\canonPartMelody";
								break;
							}
							case "mode" : {
								textRole = "\\canonPartMode";
								break;
							}
							case "title" : {
								textRole = "\\canonPartTitle";
								break;
							}
							default: {
								textRole = "";
							}
							}
						}
						if (group3.length() > 1) {
							lineNbr = group3.substring(1, group3.length()-1);
						}
					}
				} else {
					System.out.println("Not a well formed ode key: " + key);
				}
		}
	}
	
	public String getDocOdeSubSection(
			) {
		StringBuffer result = new StringBuffer();
		result.append("\\ltDocOdeSubSection{");
		result.append(odeNbr);
		result.append("}{");
		result.append(canonNbr);
		result.append("}{");
		result.append(textRole);
		result.append("}{");
		result.append(firstWord);
		result.append("}\n");
		return result.toString();
	}
	
	public String getParaText() {
		StringBuffer result = new StringBuffer();
		result.append("\\ltParaText{");
		result.append(topic);
		result.append("}{");
		result.append(key);
		result.append("}\n");
		return result.toString();
	}
	
	public static String getDepTree(List<String> tokens) {
		StringBuffer result = new StringBuffer();

		// open it
		result.append(dependencyBegin);
		// add the Greek text and stub out the English 
		// by using the Greek as placeholders
		result.append(depTextBegin);
		result.append(getDepText(tokens, 0, "0.5"));
		result.append(getDepText(tokens, -1, ""));
		result.append(depTextEnd);

		// stub out the graph
		result.append(getDepRoot(tokens.get(0)));
		result.append(getDepEdges(tokens));
		
		// close it
		result.append(dependencyEnd);
		
		return result.toString();
	}
	public static String getDepRoot(String token) {
		StringBuffer result = new StringBuffer();
		result.append("\n\\deproot[edge unit distance=4ex]{1}{ST-ROOT / ");
		result.append(DEPENDENCY_LABEL_MAPPER.getLabel(token).keyname);
		result.append("}");
		return result.toString();
	}

	public static String getDepEdges(List<String> tokens) {
		StringBuffer result = new StringBuffer();
		int size = tokens.size();
		for (int i=1; i < size; i++) {
			result.append(
					getDepEdge(
							"1"
							, Integer.toString(i+1)
							, DEPENDENCY_LABEL_MAPPER.getLabel(tokens.get(i)).keyname
							)
					);
		}
		return result.toString();
	}
	public static String getDepEdge(
			String root
			, String seq
			, String label
			) {
		StringBuffer result = new StringBuffer();
		result.append("\n\\depedge{");
		result.append(root);
		result.append("}{");
		result.append(seq);
		result.append("}{");
		result.append(label);
		result.append("}");
		return result.toString();
	}

	public static String getDepText(List<String> tokens, int padIndex, String padCm) {
		StringBuffer result = new StringBuffer();
		int size = tokens.size() - 1;
		for (int i=0; i < size; i++) {
			result.append("\n");
			result.append(tokens.get(i));
			result.append(" \\&");
			if (i == padIndex) {
				result.append("[");
				result.append(padCm);
				result.append("cm]");
			}
		}
		result.append("\n");
		result.append(tokens.get(size));
		result.append("\n\\\\");
		return result.toString();
	}
	
	public String convert() {
		return this.convert(true,true,true,true);
	}
	public String convert(
			boolean newPage
			, boolean subSection
			, boolean gramParaText
			, boolean includeDependencyTree
			) {
		StringBuffer result = new StringBuffer();
		Tokenizer tokenizer = SimpleTokenizer.INSTANCE;
        String [] theTokens = tokenizer.tokenize(text);
        firstWord = theTokens[0];

        if (newPage) {
    		result.append(newPage);
        }
        if (subSection) {
    		result.append(this.getDocOdeSubSection());
        }
        if (gramParaText) {
    		result.append(sectionGramParaText);
    		result.append(this.getParaText());
    		result.append(sectionGramInterlinearText);
        }
		
        int count = 0;
        int size = theTokens.length;
        
        StringBuffer clauseSb = new StringBuffer();
        StringBuffer innerSb = new StringBuffer();
        List<String> subTextList = new ArrayList<String>();
        
        for (int i=0; i < size; i++) {
        	String s = theTokens[i];
        	s = s.trim();
			subTextList.add(s);
        	count++;
        	innerSb.append(count);
        	innerSb.append(ExPexGlossLexemeOpen);
        	if (
        			s.startsWith(".") 
        			|| s.startsWith("·") 
        			|| s.startsWith("˙")
        			|| s.startsWith(":") 
        			|| s.startsWith(";") 
        			|| s.startsWith("?")
        			|| s.startsWith(",") 
        			|| s.startsWith("!")
        			|| s.startsWith("(")
        			|| s.startsWith(")")
        			|| s.startsWith("-")
        			|| s.startsWith("[")
        			|| s.startsWith("]")
        			) {
	        		if (s.startsWith("[") | s.startsWith("]")){
		        		innerSb.append("{" + s + "}/{" + s + "}/" + "PM/{" + s + "}]\n");
	        		} else {
		        		innerSb.append(s + "/" + s + "/" + "PM/" + s + "]\n");
	        		}
	        		if (s.startsWith(",")) {
        				clauseSb.append(wrapInner(innerSb.toString(), subTextList, includeDependencyTree));
        				innerSb = new StringBuffer();
          				subTextList = new ArrayList<String>();
          			} else if (
	            			s.startsWith(".") 
	            			|| s.startsWith("·") 
	            			|| s.startsWith("˙")
	            			|| s.startsWith(":") 
	            			|| s.startsWith(";") 
	            			|| s.startsWith("?")
	            			|| s.startsWith("!")
	            			|| s.startsWith(")")
	            			|| s.startsWith("-")
	            			|| s.startsWith("]")
	        				) {
          				if (this.newClauseRestartsNumbers) {
            				count = 0;
          				}
        				clauseSb.append(wrapInner(innerSb.toString(), subTextList, includeDependencyTree));
        				innerSb = new StringBuffer();
        				result.append(wrapOuter(clauseSb.toString()));
           				clauseSb = new StringBuffer();
          				subTextList = new ArrayList<String>();
	        		}
        		} else if(StringUtils.isNotEmpty(s) && StringUtils.isNumeric(s)) {
	        		innerSb.append(s + "/" + s + "/" + "NUM/" + s + "]\n");
        		} else {
                	innerSb.append(s);
                	innerSb.append(ExPexGlossLexemeClose);
        		}
        }
        result.append(clauseSb.toString());
		return result.toString();
	}
	
	private String wrapInner(
			String s
			, List<String> subtext
			, boolean includeDependencyTree
			) {
		StringBuffer result = new StringBuffer();
		// open
		result.append(ExPexExampleClauseTagOpen);
		result.append(StringUtils.join(subtext, " "));

		// stub out the dependency tree
		if (includeDependencyTree) {
			result.append(getDepTree(subtext));
		}
		
		// add the interlinear
		result.append(ExPexGlossTagOpen);

		// add the string
		result.append(s);
		// close
		result.append(ExPexFreeTranslationTag);
		int nbrTranslations = this.translations.length;
		for (int i=0; i < nbrTranslations; i++) {
			result.append(this.translations[i]);
			if (i < nbrTranslations -1) {
				result.append("\\\\");
			}
			result.append("\n");
		}
		result.append(ExPexGlossTagClose);
		return result.toString();
	}
	
	private String wrapOuter(String s) {
		StringBuffer result = new StringBuffer();
		// open
		result.append(ExPexExampleTagOpen + " " + this.idManager.getOslwPexId());
		// add the string
		result.append(s);
		// close
		result.append(ExPexExampleTagClose);
		return result.toString();
	}

	/**
	 * Checks the text to see if it contains underscores.
	 * If it does, the returned string will be have a 
	 * Latex underscore command instead of the character.
	 * @param text
	 */
	public String replaceUnderscores(String text) {
		if (text.contains("_")) {
			return text.replaceAll("_", underscore);
		} else {
			return text;
		}
	}
	
}

