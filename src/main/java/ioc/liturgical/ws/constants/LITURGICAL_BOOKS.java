package ioc.liturgical.ws.constants;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class LITURGICAL_BOOKS {
    private static Map<String, String> map = new TreeMap<String,String>();
    static {
        map.put("le.ep", "Lectionary - Apostolos (ὁ Ἀπόστολος)");
        map.put("da", "Daily Hymns of the Week (Της Όλης Εβδομάδος)");
        map.put("eo", "Eothina (τά ἑωθινά)");
        map.put("eu", "Euchologion (τό Εὐχολόγιον)");
        map.put("le.go", "Lectionary - Evangelion (τό εὐαγγέλιον)");
        map.put("he", "Heirmologion (τό Εἱρμολόγιον)");
        map.put("hi", "Hieratikon (τό Ἱερατικὸν)");
        map.put("ho", "Horologion (τό ῾Ωρλόγιον)");
        map.put("me", "Menaion (τά Μηναῖα)");
        map.put("oc", "Octoechos (ἡ Παρακλητική)");
        map.put("pe", "Pentecostarion (τό Πεντηκοστάριον)");
        map.put("le.pr", "Lectionary - Prophetologion (τό Προφητολόγιον)");
        map.put("ps", "Psalterion (τό Ψαλτήριον)");
        map.put("sy", "Synaxarion (τό Συναξάριον)");
        map.put("tr", "Triodion (τό Τριῴδιον)");
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
