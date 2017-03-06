package ioc.liturgical.ws.constants;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class DAYS_OF_MONTH {
    private static Map<String, String> map = new TreeMap<String,String>();
    static {
        map.put("d01", "1");
        map.put("d02", "2");
        map.put("d03", "3");
        map.put("d04", "4");
        map.put("d05", "5");
        map.put("d06", "6");
        map.put("d07", "7");
        map.put("d08", "8");
        map.put("d09", "9");
        map.put("d10", "10");
        map.put("d11", "11");
        map.put("d12", "12");
        map.put("d13", "13");
        map.put("d14", "14");
        map.put("d15", "15");
        map.put("d16", "16");
        map.put("d17", "17");
        map.put("d18", "18");
        map.put("d19", "19");
        map.put("d20", "20");
        map.put("d21", "21");
        map.put("d22", "22");
        map.put("d23", "23");
        map.put("d24", "24");
        map.put("d25", "25");
        map.put("d26", "26");
        map.put("d27", "27");
        map.put("d28", "28");
        map.put("d29", "29");
        map.put("d30", "30");
        map.put("d31", "31");
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
