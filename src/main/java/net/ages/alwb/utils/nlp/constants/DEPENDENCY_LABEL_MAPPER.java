package net.ages.alwb.utils.nlp.constants;

import java.util.Map;
import java.util.TreeMap;

import org.ocmc.ioc.liturgical.utils.GeneralUtils;


/**
 * Maps punctuation marks and words to their dependency tree label
 * if possible to do so out of context
 * 
 * @see https://ufal.mff.cuni.cz/pdt2.0/doc/manuals/en/a-layer/html/ch03s02x08.html
 * @author mac002
 *
 */
public class DEPENDENCY_LABEL_MAPPER {
    private static Map<String, DEPENDENCY_LABELS> map = new TreeMap<String,DEPENDENCY_LABELS>();
    static {
    	// punctuation
    	map.put(".",DEPENDENCY_LABELS.AuxK);
    	map.put(",",DEPENDENCY_LABELS.AuxX);
    	map.put("˙",DEPENDENCY_LABELS.APOS); // greek colon
    	map.put("·",DEPENDENCY_LABELS.APOS); // greek colon (alternate version)
    	map.put(";",DEPENDENCY_LABELS.AuxK); // greek question mark
    	map.put("?",DEPENDENCY_LABELS.AuxK); 
    	map.put("\"",DEPENDENCY_LABELS.AuxG);
    	map.put("'",DEPENDENCY_LABELS.AuxG);
    	map.put(":",DEPENDENCY_LABELS.APOS);
    	map.put("`",DEPENDENCY_LABELS.AuxG);
    	map.put("!",DEPENDENCY_LABELS.AuxK);
    	map.put("(", DEPENDENCY_LABELS.AuxG);
    	map.put(")", DEPENDENCY_LABELS.AuxG);
    	// conjunctions
    	map.put("και", DEPENDENCY_LABELS.COORD);
    	map.put("οτι", DEPENDENCY_LABELS.AuxC);
    	// prepositions
    	map.put("αμφι", DEPENDENCY_LABELS.AuxP);
    	map.put("ανα", DEPENDENCY_LABELS.AuxP);
    	map.put("αντι", DEPENDENCY_LABELS.AuxP);
    	map.put("απο", DEPENDENCY_LABELS.AuxP);
    	map.put("δια", DEPENDENCY_LABELS.AuxP);
    	map.put("εις", DEPENDENCY_LABELS.AuxP);
    	map.put("ες", DEPENDENCY_LABELS.AuxP);
       	map.put("εν", DEPENDENCY_LABELS.AuxP);
    	map.put("εξ", DEPENDENCY_LABELS.AuxP);
    	map.put("εκ", DEPENDENCY_LABELS.AuxP);
    	map.put("επι", DEPENDENCY_LABELS.AuxP);
    	map.put("κατα", DEPENDENCY_LABELS.AuxP);
    	map.put("μετα", DEPENDENCY_LABELS.AuxP);
    	map.put("παρα", DEPENDENCY_LABELS.AuxP);
    	map.put("περι", DEPENDENCY_LABELS.AuxP);
    	map.put("προ", DEPENDENCY_LABELS.AuxP);
    	map.put("προς", DEPENDENCY_LABELS.AuxP);
    	map.put("συν", DEPENDENCY_LABELS.AuxP);
    	map.put("υπερ", DEPENDENCY_LABELS.AuxP);
    	map.put("υπο", DEPENDENCY_LABELS.AuxP);
       // definite articles
        map.put("ο", DEPENDENCY_LABELS.ATR);
        map.put("του", DEPENDENCY_LABELS.ATR);
        map.put("τω", DEPENDENCY_LABELS.ATR);
        map.put("τον", DEPENDENCY_LABELS.ATR);
        map.put("ω", DEPENDENCY_LABELS.ATR);
        map.put("οι", DEPENDENCY_LABELS.ATR);
        map.put("των", DEPENDENCY_LABELS.ATR);
        map.put("τοις", DEPENDENCY_LABELS.ATR);
        map.put("τους", DEPENDENCY_LABELS.ATR);
        map.put("η", DEPENDENCY_LABELS.ATR);
        map.put("της", DEPENDENCY_LABELS.ATR);
        map.put("τη", DEPENDENCY_LABELS.ATR);
        map.put("την", DEPENDENCY_LABELS.ATR);
        map.put("τις", DEPENDENCY_LABELS.ATR);
        map.put("το", DEPENDENCY_LABELS.ATR);
        map.put("τα", DEPENDENCY_LABELS.ATR);
	}


    /**
     * Get the dependency label for this punctuation mark
     * @param s
     * @return
     */
    public static DEPENDENCY_LABELS getLabel(String s) {
    	String n = GeneralUtils.normalize(s);
    	if (map.containsKey(n)) {
    		return map.get(n);
    	} else {
    		if (s.equals("·")) {
    			return DEPENDENCY_LABELS.APOS;
    		} else {
        		return DEPENDENCY_LABELS.TBD;
    		}
    	}
    }
    
    public static boolean isPunctuation(String s) {
    	boolean result = false;
    	if (s.equals("·")) {
    		System.out.print("");
    	}
    	String n = GeneralUtils.normalize(s);
    	DEPENDENCY_LABELS label = getLabel(n);
    	result = n.length() == 1
    			&& (
    					label.equals(DEPENDENCY_LABELS.AuxG)
    					|| label.equals(DEPENDENCY_LABELS.AuxK)
    					|| label.equals(DEPENDENCY_LABELS.AuxX)
    					|| label.equals(DEPENDENCY_LABELS.APOS)
    			)
		;
    	return result;
    }
}
