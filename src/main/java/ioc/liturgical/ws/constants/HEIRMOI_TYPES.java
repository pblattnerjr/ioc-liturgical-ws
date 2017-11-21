package ioc.liturgical.ws.constants;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class HEIRMOI_TYPES {
    private static Map<String, String> map = new TreeMap<String,String>();
    static {
        map.put("a", "a - Automela (Αυτόμελα)");
        map.put("h", "h - Heirmoi (Εἱρμοί)");
        map.put("ga", "ga - General Apolytikia (Απολυτίκια)");
        map.put("k", "k - Katavasias (Καταβασία)");
        map.put("o", "o - Other (Ἄλλα)");
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
