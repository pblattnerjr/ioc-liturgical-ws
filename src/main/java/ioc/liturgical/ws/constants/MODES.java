package ioc.liturgical.ws.constants;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class MODES {
    private static Map<String, String> map = new TreeMap<String,String>();
    static {
        map.put("m1", "Mode 1 (Ἦχος αʹ)");
        map.put("m2", "Mode 2 (Ἦχος βʹ)");
        map.put("m3", "Mode 3 (Ἦχος γʹ)");
        map.put("m4", "Mode 4 (Ἦχος δʹ)");
        map.put("m5", "Mode 5 (Ἦχος πλ. αʹ)");
        map.put("m6", "Mode 6 (Ἦχος πλ. βʹ)");
        map.put("m7", "Mode 7 (Ἦχος βαρύς)");
        map.put("m8", "Mode 8 (Ἦχος πλ. δʹ)");
        
        map = Collections.unmodifiableMap(map);
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
    
    public static Map<String,String> getMap() {
    	return map;
    }

}
