package net.ages.alwb.utils.nlp.utils;

import java.io.InputStream;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import opennlp.tools.lemmatizer.SimpleLemmatizer;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.tokenize.Tokenizer;

import net.ages.alwb.utils.core.generics.MultiMapWithList;
import net.ages.alwb.utils.core.misc.AlwbGeneralUtils;
import ioc.liturgical.ws.models.db.docs.nlp.ConcordanceLine;
import ioc.liturgical.ws.models.db.docs.nlp.WordInflected;
import net.ages.alwb.utils.core.error.handling.ErrorUtils;
import net.ages.alwb.utils.core.file.AlwbFileUtils;
import net.ages.alwb.utils.nlp.constants.BETA_CODES;
import net.ages.alwb.utils.nlp.models.CharacterInfo;


public class NlpUtils {
	private static final Logger logger = LoggerFactory.getLogger(NlpUtils.class);
	
	private static SimpleLemmatizer lemmatizer;
	
	private static String[] posTags = "CC,CD,DT,EX,FW,IN,JJ,JJR,JJS,MD,NN,NNN,NNS,PDT,POS,PRP,PRP$,RB,RBR,RBS,RP,TO,UH,VB,VBD,VBG,VBN,VBP,VBZ,WDT,WP,WP$,WRB".split(",");

	/**
	 * Creates a map of each character in the string.
	 * You can use the GreekCharacter to get
	 * its Unicode block and numeric value.
	 * @param s
	 * @return
	 */
	public static List<CharacterInfo> getCharacterInfo(String s) {
		List<CharacterInfo> result = new ArrayList<CharacterInfo>();
		for (char c : s.toCharArray()) {
			result.add(new CharacterInfo(c));
		}
		return result;
	}

	
	/**
	 * Gives the string index for points at which the two strings differ.
	 * The comparison is based on the Unicode numeric value for each char
	 * 
	 * This method calls its overloaded version that takes List<CharacterInfo> as the parameters.
	 * 
	 * @param s1
	 * @param s2
	 * @return
	 */
	public static List<Integer> getDiff(String s1, String s2) {
		List<Integer> result = new ArrayList<Integer>();
		if (BETA_CODES.toBetaCode(s1).equals(BETA_CODES.toBetaCode(s2))) {
			List<CharacterInfo> list1 = getCharacterInfo(s1);
			List<CharacterInfo> list2 = getCharacterInfo(s2);
			result = getDiff(list1, list2);
		}
		return result;
	}

	/**
	 * Gives the string index for points at which the two character lists differ.
	 * The comparison is based on the Unicode numeric value for each char
	 * @param s1
	 * @param s2
	 * @return
	 */
	public static List<Integer> getDiff(List<CharacterInfo> list1, List<CharacterInfo> list2) {
		List<Integer> result = new ArrayList<Integer>();
		if (list1.size() == list2.size()) {
			int j = list1.size();
			for (int i=0; i < j; i++) {
				if (list1.get(i).value() != list2.get(i).value()) {
					result.add(new Integer(i));
				}
			}
		}
		return result;
	}
	
	/**
	 * Takes a JsonArray of texts, and creates a unique set of tokens
	 * with frequency counts.
	 * 
	 * @param texts
	 * @param convertToLowerCase, if true converts each text to its lowercase form
	 * @param ignoreLatin - if true, will not include a token that contains a Latin character
	 * @param ignoreNumbers - if true, will not include a token that contains numbers
	 * @param removeDiacritics - if true, removes accent marks, etc.
	 * @param numberOfConcordanceEntries - the number of concordance entries you want
	 * @return
	 */
	public static MultiMapWithList<WordInflected, ConcordanceLine> getWordListWithFrequencies(
			JsonArray texts
			, boolean convertToLowerCase
			, boolean ignorePunctuation
			, boolean ignoreLatin
			, boolean ignoreNumbers
			, boolean removeDiacritics
			, int numberOfConcordanceEntries
			) {
		int concordSize = numberOfConcordanceEntries;
		if (concordSize == 0) {
			concordSize = 1;
		}
		MultiMapWithList<WordInflected, ConcordanceLine> result = 
				new MultiMapWithList<WordInflected, ConcordanceLine>(concordSize);
		
		logger.info("tokenizing " + texts.size() + " texts");

		for (JsonElement e : texts) {
			JsonObject o = e.getAsJsonObject();
			String id = o.get("n.id").getAsString();
			String value = o.get("n.value").getAsString();
 	       List<String> theTokens = getTokens(
 	  			value
 				, convertToLowerCase
 				, ignorePunctuation
 				, ignoreLatin
 				, ignoreNumbers
 				, removeDiacritics
 	    	);
	        for (String token : theTokens) {
	        	String rawToken = token;
	        	if (result.mapSimpleContainsValue(token)) {
	        		WordInflected  word = result.getValueFromMapSimple(token);
	        		word.setFrequency(word.getFrequency()+1);
	        		result.addValueToMapSimple(token, word);
	        	} else {
	        		WordInflected word = new WordInflected(token, 1);
	        		ConcordanceLine line = getConcordanceLine(
	        				rawToken
	        				, 1
	        				, value
	        				, id
	        				, 100
	        				, convertToLowerCase
	        				);
	        		word.setExampleId(id);
	        		word.setExampleLeftContext(line.getContextLeft());
	        		word.setExampleRightContext(line.getContextRight());
	        		result.addValueToMapSimple(token, word);
	        	}
	        	if (numberOfConcordanceEntries > 0) {
		        	int seq = result.listSize(token) + 1;
	        		ConcordanceLine line = getConcordanceLine(
	        				rawToken
	        				, seq
	        				, value
	        				, id
	        				, 100
	        				, convertToLowerCase
	        				);
	        		result.addValueToMapWithLists(token, line);
	        	}
	        }
		}
        logger.info("creating WordInflected for " + result.getMapSimple().size() + " tokens");
        
        logger.info("done");
		return result;
	}
	
	/**
	 * 
	 * @param text - the text within which the token occurs
	 * @param id - the ID of the text
	 * @param token - the token (appears in center of concordance line)
	 * @param width - number of characters to left and right
	 * @return
	 */
	public static ConcordanceLine getConcordanceLine(
			String rawToken
			, int seq
			, String text 
			, String id
			, int width
			, boolean lowerCase
			) {
		int halfWidth = width / 2;
		int rawTokenLength = rawToken.length();
    	int tokenStartIndex = 0;
    	if (lowerCase) {
    		tokenStartIndex = text.toLowerCase().indexOf(rawToken);
    	} else {
    		tokenStartIndex = text.indexOf(rawToken);
    	}
    	int tokenEndIndex = tokenStartIndex + rawTokenLength;
    	int leftStartIndex = tokenStartIndex - halfWidth;
    	String left = "";
    	if (leftStartIndex > 0) {
    		try {
        		left = text.substring(tokenStartIndex - halfWidth, tokenStartIndex);
    		} catch (Exception e) {
    			ErrorUtils.report(logger, e);
    		}
    	} else {
    		try {
            	left = text.substring(0, tokenStartIndex);
    		} catch (Exception e) {
    			ErrorUtils.report(logger, e);
    		}
        	int padding = halfWidth - left.length();
        	for (int i = 0; i < padding; i++) {
        		left = " " + left;
        	}
    	}
    	int rightEndIndex = tokenEndIndex  + halfWidth;
    	String right = "";
    	if (rightEndIndex <= text.length()) {
    		try {
            	right = text.substring(tokenEndIndex, rightEndIndex);
    		} catch (Exception e) {
    			ErrorUtils.report(logger, e);
    		}
    	} else {
    		try {
            	right = text.substring(tokenEndIndex, text.length());
    		} catch (Exception e) {
    			ErrorUtils.report(logger, e);
    		}
        	int padding = halfWidth - right.length();
        	for (int i = 0; i < padding; i++) {
        		right = right + " ";
        	}
    	}
    	return new ConcordanceLine(
    			rawToken
    			, seq
    			, width
    			, id
    			, left
    			, right
    			);
	}

	
	public static JsonArray getTokensAsJsonArray(
			String text
			, boolean convertToLowerCase
			, boolean ignorePunctuation
			, boolean ignoreLatin
			, boolean ignoreNumbers
			, boolean removeDiacritics
			) {
		JsonArray result = new JsonArray();
		for (String token : getTokens(
				text
				, convertToLowerCase
				, ignorePunctuation
				, ignoreLatin
				, ignoreNumbers
				, removeDiacritics
				)) {
			result.add(token);
		}
		return result;
	}

		public static String getLemma(String word) {
			String original = word.trim().toLowerCase();
			String result = original;
			try {
				   if (lemmatizer == null) {
					   InputStream is = NlpUtils.class.getResourceAsStream("/models/en-lemmatizer.dict");
				        lemmatizer = new SimpleLemmatizer(is);
				        is.close();
				    }
				   for (String tag : posTags) {
					    result = lemmatizer.lemmatize(original, tag);
					    if (! result.equals(original)) {
					    	result = result + "." + tag;
					    	break;
					    }
				   }
			} catch (Exception e) {
				ErrorUtils.report(logger, e);
			}
			return result;
		}
		
		public static List<String> getTokens(
			String text
			, boolean convertToLowerCase
			, boolean ignorePunctuation
			, boolean ignoreLatin
			, boolean ignoreNumbers
			, boolean removeDiacritics
			) {
		List<String> result = new ArrayList<String>();
		String regExAlpha = ".*[a-zA-Z].*";
		String regExAlphaNumeric = ".*[a-zA-Z0-9].*";
		String punct = "[;˙·,.;!?\\-(){}\\[\\]\\/:<>%͵·\"'`’_«»‘*•+…‧′|]";
		Pattern punctPattern = Pattern.compile(punct); // punctuation

		String regEx = regExAlpha;
		if (ignoreNumbers) {
			regEx = regExAlphaNumeric;
		}
		
		boolean include = true;
		Tokenizer tokenizer = SimpleTokenizer.INSTANCE;

	        for (String token : tokenizer.tokenize(AlwbGeneralUtils.toNfc(text))) {
	        	String a = token;
	           	if (convertToLowerCase) {
	        		token = token.toLowerCase();
	        	}
	        	if (removeDiacritics) {
	        		token = Normalizer.normalize(token, Normalizer.Form.NFD)
	    					.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
	        	}
	        	include = true;
	        	if (ignorePunctuation && punctPattern.matcher(token).matches()) {
	        		include = false;
	        	}
	        	if (include && (ignoreLatin || ignoreNumbers)) {
	        		if (token.matches(regEx)) {
	        			include = false;
	        		}
	        	}
	        	if (include) {
		        		result.add(token);
		        	}
	        	}
			return result;
	}
		
	public static void main(String[] args) {
		String test  = "(Εἰς τὰς καθημερινὰς ψάλλεται τό· \"ὁ ἐν ἁγίοις θαυμαστός\". Αἱ δεσποτικαὶ ἑορταὶ ἔχουν ἴδιον Εἰσοδικόν.)";
//		String test = "(Ψαλλομένου τοῦ Ἀπολυτικίου, γίνεται ὑπὸ τοῦ Ἱερέως ἡ Εἴσοδος μετὰ τοῦ Εὐαγγελίου. Ὁ Ἱερεὺς προσεύχεται χαμηλοφώνως τὴν ἑπομένην εὐχήν:)";
//		String test = "(τοῦ Ἀπο γίνεται ὑπὸ τοῦ Ἱερέως ἡ Εἴσοδος μετὰ τοῦ Εὐαγγελίου. Ὁ Ἱερεὺς προσεύχεται χαμηλοφώνως τὴν ἑπομένην εὐχήν:)";
		System.out.println(test);
			System.out.println("\nTokens:");
			for (String token : getTokens(
	 	 	  			test
	 	 				, true // convertToLowerCase
	 	 				, true //  ignorePunctuation
	 	 				, true //  ignoreLatin
	 	 				, true //  ignoreNumbers
	 	 				, true //  removeDiacritics
	 	 	    	)) {
				System.out.println(token);
			}
			String rawToken = "Εἰς";
			ConcordanceLine result = getConcordanceLine(
					rawToken
					,1
					, test
					, "gr_gr_cog~client~cl.eu.lichrysbasil.R005"
					, 80
					, true
					);
			System.out.println(result.toString("*"));
			rawToken = "ἁγίοις";
			 result = getConcordanceLine(
					rawToken
					, 2
					, test
					, "gr_gr_cog~client~cl.eu.lichrysbasil.R005"
					, 80
					, true
					);
				System.out.println(result.toString("*"));
				
	}
	
}
