package net.ages.alwb.utils.nlp.parsers;

import java.util.Map;
import java.util.TreeMap;

import net.ages.alwb.utils.nlp.constants.DEPENDENCY_LABEL_MAPPER;
import net.ages.alwb.utils.nlp.fetchers.PerseusXmlMorph;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.tokenize.Tokenizer;

/**
 * Parses text into sentences, phrases, and words
 * @author mac002
 *
 */
public class TextParser {
	
	private String text = "";
	private String [] tokens = null;
	
	public TextParser(String text) {
		this.text = text;
        Tokenizer tokenizer = SimpleTokenizer.INSTANCE;
        this.tokens = tokenizer.tokenize(this.text);
	}

	public Map<String, PerseusXmlMorph> parse() {
		Map<String, PerseusXmlMorph> result = new TreeMap<String,PerseusXmlMorph>();
        for (String token : tokens) {
        	boolean analyze = true;
        	if (token.length() == 1) {
        		if (DEPENDENCY_LABEL_MAPPER.isPunctuation(token)) {
        			analyze = false;
        		}
        	}
        	if (analyze) {
            	result.put(token, new PerseusXmlMorph(token));
        	}
        }
        return result;
	}
	
	/**
	 * TODO:
	 * 1. Create a separate class that gets the parses
	 * 2. Create a hash of each analysis and store the analysis in a map with the hash as the key
	 *      in order to avoid duplicates.
	 * @param args
	 */
	public static void main(String[] args) {
		String s1 = "Ἅπαντες πιστοί,";  // πιστοί
		String s2 = " Ἅπαντες πιστοί, ἐν ᾧ τὴν τελείωσιν ἐλάβομεν, θεολογοῦντες ἀσιγήτως, σὺν Ἀγγέλοις δοξάσωμεν, Πατέρα Υἱὸν καὶ Πνεῦμα Ἅγιον· τοῦτο γὰρ Τριὰς ὑποστάσεσιν ὁμοούσιος, εἷς δὲ Θεός, ᾧ καὶ ψάλλομεν· Ὁ τῶν Πατέρων Κύριος, καὶ Θεὸς εὐλογητὸς εἶ.";

		TextParser p = new TextParser(s1);
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String[] getTokens() {
		return tokens;
	}

	public void setTokens(String[] tokens) {
		this.tokens = tokens;
	}

}
