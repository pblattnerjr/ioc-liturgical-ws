package ioc.liturgical.ws.constants;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class BIBLICAL_BOOK_NUMBERS {
    private static Map<String, String> map = new TreeMap<String,String>();
    static {
        map.put("GEN", "101");
        map.put("EXO", "102");
        map.put("LEV", "103");
        map.put("NUM", "104");
        map.put("DEU", "105");
        map.put("JOS", "106");
        map.put("JDG", "107");
        map.put("RUT", "108");
        map.put("SA1", "109");
        map.put("SA2", "110");
        map.put("KI1", "111");
        map.put("KI2", "112");
        map.put("CH1", "113");
        map.put("CH2", "114");
        map.put("ES1", "115");
        map.put("ES2", "116");
        map.put("TOB", "117");
        map.put("JDT", "118");
        map.put("EST", "119");
        map.put("MA1", "120");
        map.put("MA2", "121");
        map.put("MA3", "122");
        map.put("MA4", "123");
        map.put("PSA", "124");
        map.put("ODE", "125");
        map.put("PRO", "126");
        map.put("ECC", "127");
        map.put("SOS", "128");
        map.put("JOB", "129");
        map.put("WIS", "130");
        map.put("POS", "131");
        map.put("SIR", "132");
        map.put("HOS", "133");
        map.put("AMO", "134");
        map.put("MIC", "135");
        map.put("JOE", "136");
        map.put("OBA", "137");
        map.put("JON", "138");
        map.put("NAH", "139");
        map.put("HAB", "140");
        map.put("ZEP", "141");
        map.put("HAG", "142");
        map.put("ZEC", "143");
        map.put("MAL", "144");
        map.put("ISA", "145");
        map.put("JER", "146");
        map.put("BAR", "147");
        map.put("LAM", "148");
        map.put("LJE", "148");
        map.put("EZE", "150");
        map.put("SUS", "151");
        map.put("DAN", "152");
        map.put("BEL", "153");
        map.put("MAT", "201");
        map.put("MAR", "202");
        map.put("LUK", "203");
        map.put("JOH", "204");
        map.put("ACT", "205");
        map.put("ROM", "206");
        map.put("CO1", "207");
        map.put("CO2", "208");
        map.put("GAL", "209");
        map.put("EPH", "210");
        map.put("PHP", "211");
        map.put("COL", "212");
        map.put("TH1", "213");
        map.put("TH2", "214");
        map.put("TI1", "215");
        map.put("TI2", "216");
        map.put("TIT", "217");
        map.put("PHM", "218");
        map.put("HEB", "219");
        map.put("JAM", "220");
        map.put("PE1", "221");
        map.put("PE2", "222");
        map.put("JO1", "223");
        map.put("JO2", "224");
        map.put("JO3", "225");
        map.put("JDE", "226");
        map.put("REV", "227");
        map = Collections.unmodifiableMap(map);
	}
    
    /**
     * Get the sequence number for the specified abbreviation
     * @param abbreviation
     * @return
     */
    public static String getBookNumber(String abbreviation) {
    	return map.get(abbreviation);
    }

    /**
     * Get the sequence string for the specified abbreviation,
     * built using the chapter, verse, and library parms.
     * 
     * This is useful for sorting text in the proper order.
     * 
     * @param abbreviation
     * @param chapter
     * @param verse
     * @param library
     * @return e.g. 120~MA1~C06~50~en_uk_kjv
     */
    public static String getSequence(
    		String abbreviation
    		, String chapter
    		, String verse
    		, String library
    		) {
    	return map.get(abbreviation) + "~" + abbreviation + "~" + chapter + "~" + verse + "~" + library;
    }
    
    public static Map<String,String> getMap() {
    	return map;
    }
    
    public static boolean containsKey(String key) {
    	return map.containsKey(key);
    }
    
    public static String get(String key) {
    	return key + " - " + map.get(key);
    }
    
    /**
     * Convenience method for when you don't want to bother to
     * see if the key exists in the map.
     * 
     * If it does not, the label will be set to the value of the key.
     * If it does exist, the label will have the value from the map.
     * @param key
     * @return
     */
    public static String getLabel(String key) {
    	if (map.containsKey(key)) {
    		return get(key);
    	} else {
    		return key;
    	}
    }

}
