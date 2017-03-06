package ioc.liturgical.ws.constants;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class EOTHINON_WEEKS {
    private static Map<String, String> map = new TreeMap<String,String>();
    static {
        map.put("e01", "e01 - The First Eothinon (Τὸ Αʹ Ἑωθινόν)");
        map.put("e02", "e02 - The Second Eothinon (Τὸ Βʹ Ἑωθινόν)");
        map.put("e03", "e03 - The Third Eothinon (Τὸ Γʹ Ἑωθινόν)");
        map.put("e04", "e04 - The Fourth Eothinon (Τὸ Δʹ Ἑωθινόν)");
        map.put("e05", "e05 - The Fifth Eothinon (Τὸ Εʹ Ἑωθινόν)");
        map.put("e06", "e06 - The Sixth Eothinon (Τὸ Ϛʹ Ἑωθινόν)");
        map.put("e07", "e07 - The Seventh Eothinon (Τὸ Ζʹ Ἑωθινόν)");
        map.put("e08", "e08 - The Eighth Eothinon (Τὸ Ηʹ Ἑωθινόν)");
        map.put("e09", "e09 - The Ninth Eothinon (Τὸ Θʹ Ἑωθινόν)");
        map.put("e10", "e10 - The Tenth Eothinon (Τὸ Ιʹ Ἑωθινόν)");
        map.put("e11", "e11 - The Eleventh Eothinon (Τὸ ΙΑʹ Ἑωθινόν)");
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
