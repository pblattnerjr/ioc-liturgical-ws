package net.ages.alwb.utils.nlp.constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

import net.ages.alwb.utils.core.misc.AlwbGeneralUtils;


/**
 * Maps Perseus Grammatical abbreviations to Leipzig
 * 
 * @see https://www.eva.mpg.de/lingua/resources/glossing-rules.php
 * @author mac002
 *
 */
public class GRAMMAR_ABBREVIATIONS_MAPPER {
    private static Map<String, GRAMMAR_ABBREVIATIONS> map = new TreeMap<String,GRAMMAR_ABBREVIATIONS>();
    static {
    	map.put("TBD",GRAMMAR_ABBREVIATIONS.TBD);
    	map.put("1st", GRAMMAR_ABBREVIATIONS.P1);
    	map.put("2nd", GRAMMAR_ABBREVIATIONS.P2);
    	map.put("3rd", GRAMMAR_ABBREVIATIONS.P3);
    	map.put("aeolic", GRAMMAR_ABBREVIATIONS.EMPTY);
    	map.put("attic", GRAMMAR_ABBREVIATIONS.EMPTY);
    	map.put("contr", GRAMMAR_ABBREVIATIONS.EMPTY);
    	map.put("doric", GRAMMAR_ABBREVIATIONS.EMPTY);
    	map.put("dual", GRAMMAR_ABBREVIATIONS.DU);
    	map.put("enclitic", GRAMMAR_ABBREVIATIONS.EMPTY);
    	map.put("epic", GRAMMAR_ABBREVIATIONS.EMPTY);
    	map.put("exclam", GRAMMAR_ABBREVIATIONS.EXCLAMATION);
    	map.put("fem", GRAMMAR_ABBREVIATIONS.FEMININE);
    	map.put("geog_name", GRAMMAR_ABBREVIATIONS.EMPTY);
    	map.put("homeric", GRAMMAR_ABBREVIATIONS.EMPTY);
    	map.put("imperat", GRAMMAR_ABBREVIATIONS.IMP);
    	map.put("imperf", GRAMMAR_ABBREVIATIONS.IMPRF);
    	map.put("indeclform", GRAMMAR_ABBREVIATIONS.EMPTY);
    	map.put("ionic", GRAMMAR_ABBREVIATIONS.EMPTY);
    	map.put("irreg_superl", GRAMMAR_ABBREVIATIONS.EMPTY);
    	map.put("masc", GRAMMAR_ABBREVIATIONS.MASCULINE);
    	map.put("mp", GRAMMAR_ABBREVIATIONS.PASS);
    	map.put("neut", GRAMMAR_ABBREVIATIONS.NEUTER);
    	map.put("nu_movable", GRAMMAR_ABBREVIATIONS.EMPTY);
    	map.put("partic", GRAMMAR_ABBREVIATIONS.PART);
    	map.put("poetic", GRAMMAR_ABBREVIATIONS.EMPTY);
    	map.put("proclitic", GRAMMAR_ABBREVIATIONS.EMPTY);
    	map.put("pres_redupl", GRAMMAR_ABBREVIATIONS.EMPTY);
    	map.put("prs", GRAMMAR_ABBREVIATIONS.PRS);
    	map.put("redupl", GRAMMAR_ABBREVIATIONS.EMPTY);
    	map.put("short_subj", GRAMMAR_ABBREVIATIONS.EMPTY);
    	map.put("syll_augment", GRAMMAR_ABBREVIATIONS.EMPTY);
    	map.put("unaugmented", GRAMMAR_ABBREVIATIONS.EMPTY);
	}


    /**
     * Get the abbreviation label
     * @param s
     * @return
     */
    public static GRAMMAR_ABBREVIATIONS getLabel(String s) {
    	String n = AlwbGeneralUtils.normalize(s);
    	if (map.containsKey(n)) {
    		return map.get(n);
    	} else {
    		return GRAMMAR_ABBREVIATIONS.TBD;
    	}
    }
    
    public static String getLabelValue(String s) {
    	if (map.containsKey(s)) {
    		return map.get(s).keyname.trim();
    	} else {
    		return s.toUpperCase();
    	}
    }

    /**
     * Converts a string that contains Perseus grammatical abbreviations into Leipzig ones
     * @param splitter
     * @param parse
     * @param delimiter
     * @return
     */
    public static String convertToLeipzig(String splitter, String parse, String delimiter) {
    	if (parse.contains("1") || parse.contains("2") || parse.contains("3")) {
    		String why = parse;
    	}
    	List<String> result = new ArrayList<String>();
    	String[] parts = parse.trim().toLowerCase().split(splitter);
    	for (String part : parts) {
    		String label = getLabelValue(part.trim());
    		if (label.length() > 0) {
        		result.add(label);
    		}
    	}
    	return StringUtils.join(result,delimiter);
    }
    
}
