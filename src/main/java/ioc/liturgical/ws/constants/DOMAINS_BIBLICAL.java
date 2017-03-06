package ioc.liturgical.ws.constants;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class DOMAINS_BIBLICAL {
    private static  Map<String, String> map = new TreeMap<String,String>();
    static {
		map.put("en_uk_kjv", "Bible - English - KJV");
		map.put("en_uk_lxxbrenton", "LXX - English - Brenton");
		map.put("en_uk_lxx2012", "LXX - English - Brenton revision 2012");
		map.put("en_uk_webbe", "Bible - English - WEB British Edition");
		map.put("gr_eg_lxxupccata", "LXX - Greek - Codex Alexandrinus (UPC-CAT)");
		map.put("gr_eg_lxxupccatba", "LXX - Greek - Codices Vaticanus and Alexandrinus (UPC-CAT)");
		map.put("gr_eg_lxxupccatb", "LXX - Greek - Codex Vaticanus (UPC-CAT)");
		map.put("gr_eg_lxxupccatog", "LXX - Greek - Old Greek (UPC-CAT)");
		map.put("gr_eg_lxxupccats", "LXX - Greek - Codex Sinaiticus (UPC-CAT)");
		map.put("gr_eg_lxxupccatth", "LXX - Greek - Theodotion (UPC-CAT)");
		map.put("gr_eg_lxxupccat", "LXX - Greek - Rahlfs (UPC-CAT)");
        map.put("gr_gr_ntpt", "NT - Greek - Patriarcal");
//        map = Collections.unmodifiableMap(map);
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
