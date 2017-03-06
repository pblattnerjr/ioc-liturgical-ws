package ioc.liturgical.ws.constants;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class DROPDOWN_VALUES {
    private static Map<String, String> map = new TreeMap<String,String>();
    static {
    	map.putAll(BIBLICAL_BOOKS.getMap());
    	map.putAll(LITURGICAL_BOOKS.getMap());
    	map.putAll(DAYS_OF_MONTH.getMap());
    	map.putAll(DAYS_OF_WEEK.getMap());
    	map.putAll(DAYS_OF_TRIODION.getMap());
    	map.putAll(DAYS_OF_PENTECOSTARION.getMap());
    	map.putAll(DOMAINS_BIBLICAL.getMap());
    	map.putAll(DOMAINS_LITURGICAL.getMap());
    	map.putAll(MODES.getMap());
    	map.putAll(HIERATIKON_SECTIONS.getMap());
    	map.putAll(HOROLOGION_SECTIONS.getMap());
    	map.putAll(HEIRMOI_TYPES.getMap());
    	map.putAll(EOTHINON_WEEKS.getMap());
    	map.putAll(MONTHS_OF_YEAR.getMap());
    	
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

}
