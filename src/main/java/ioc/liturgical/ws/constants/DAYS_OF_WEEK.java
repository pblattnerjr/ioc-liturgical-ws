package ioc.liturgical.ws.constants;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class DAYS_OF_WEEK {
    private static Map<String, String> map = new TreeMap<String,String>();
    static {
        map.put("d1", "d1 - Sunday (Κυριακή)");
        map.put("d2", "d2 - Monday (Δευτέρα)");
        map.put("d3", "d3 - Tuesday (Τρίτη)");
        map.put("d4", "d4 - Wednesday (Τετάρτη)");
        map.put("d5", "d5 - Thursday (Πέμπτη)");
        map.put("d6", "d6 - Friday (Παρασκευή)");
        map.put("d7", "d7 - Saturday (Σάββατο)");
        map = Collections.unmodifiableMap(map);
	}
    
    public static Map<String,String> getMap() {
    	return map;
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

}
