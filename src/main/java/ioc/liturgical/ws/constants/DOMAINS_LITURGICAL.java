package ioc.liturgical.ws.constants;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class DOMAINS_LITURGICAL {
	// TODO: each AGES-Initiatives Github library project has a domain.json
	// file.  These should be used to populate the database with the information
	// about the versions.  The web service should read the database instead
	// of calling this.
    private static Map<String, String> map = new TreeMap<String,String>();
    static {
        map.put("en_uk_lash", "Translations by Fr. Ephrem Lash");
    	map.put("en_us_andronache", "Translations by Virgil Peter Andronache");
    	map.put("en_us_boyer", "Translations by John Michael Boyer");
    	map.put("en_us_constantinides", "Translations by Fr. Evagoras Constantinides");
        map.put("en_us_dedes", "Translations by Fr. Seraphim Dedes");
        map.put("en_us_goa", "Translations by the Greek Orthodox Archdiocese of America");
        map.put("en_us_holycross", "Translations by the Holy Cross Orthodox Press");
        map.put("en_us_oca", "Translations by the Orthodox Church in America");
        map.put("en_us_repass", "Translations by Fr. Juvenal Repass");
        map.put("gr_gr_cog", "Common Orthodox Greek text");
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
