package ioc.liturgical.ws.constants;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class MONTHS_OF_YEAR_MAP {
    private static Map<String, String> map = new TreeMap<String,String>();
    static {
        map.put("m01", "m01 - January (Ιανουάριος)");
        map.put("m02", "m02 - February (Φεβρουάριος)");
        map.put("m03", "m03 - March (Μάρτιος)");
        map.put("m04", "m04 - April (Απρίλιος)");
        map.put("m05", "m05 - May (Μάιος)");
        map.put("m06", "m06 - June (Ιούνιος)");
        map.put("m07", "m07 - July (Ιούλιος)");
        map.put("m08", "m08 - August (Αύγουστος)");
        map.put("m09", "m09 - September (Σεπτέμβριος)");
        map.put("m10", "m10 - October (Οκτώβριος)");
        map.put("m11", "m11 - November (Νοέμβριος )");
        map.put("m12", "m12 - December (Δεκέμβριος)");
        map = Collections.unmodifiableMap(map);
	}
    
    public static boolean containsKey(String key) {
    	return map.containsKey(key);
    }
    
    public static String get(String key) {
    	return map.get(key);
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
    
    public static Map<String,String> getMap() {
    	return map;
    }

}
