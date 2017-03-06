package ioc.liturgical.ws.constants;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

/**
 * Note that there are annotations that use DOC_TYPES keys but not explicitly.
 * You have to hard code them.  So making a change here won't change them there.
 * Here is the list:
 * Domain.java
 * DomainCreateForm.java
 * 
 * @author mac002
 *
 */
public class DOC_TYPES {
    private static Map<String, String> map = new TreeMap<String,String>();
    static {
        map.put("Biblical", "Biblical Texts");
        map.put("Link", "Link Texts");
        map.put("Liturgical", "Liturgical Texts");
        map.put("Note", "Note Texts");
        map = Collections.unmodifiableMap(map);
	}
    
    public static String[] getKeysAsArray() {
    	return (String[]) map.keySet().toArray();
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
