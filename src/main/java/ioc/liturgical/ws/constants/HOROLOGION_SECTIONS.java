package ioc.liturgical.ws.constants;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class HOROLOGION_SECTIONS {
    private static Map<String, String> map = new TreeMap<String,String>();
    static {
    	map.put("s01", "s01 - Preliminary Prayers (Προοιμιακή Προσευχή)");
    	map.put("s02", "s02 - Midnight Office  (Το Μεσονυκτικόν)");
    	map.put("s03", "s03 - Matins  (Ακολουθία του Όρθρου)");
    	map.put("s04", "s04 - First Hour (Ωρα Αʹ.)");
    	map.put("s05", "s05 - Third Hour (Ωρα Γʹ.)");
    	map.put("s06", "s06 - Sixth Hour (Ωρα Ϛʹ.)");
    	map.put("s07", "s07 - Typika Service (Ακολουθία των Τυπικών)");
    	map.put("s08", "s08 - Lunch Prayers (Ακολουθία της Τραπέζης)");
    	map.put("s09", "s09 - Ninth Hour (Ωρα Θʹ.");
    	map.put("s10", "s10 - Vespers (Ακολουθία του Εσπερινού)");
    	map.put("s11", "s11 - Dinner Prayers (Ευχή της τραπέζης)");
    	map.put("s12", "s12 - Great Compline (Μέγα Απόδειπνον)");
    	map.put("s13", "s13 - Small Compline  (Μικρόν Απόδειπνον) ");
    	map.put("s14", "s14 - Paschal Service (Πασχάλιος Ημερονύκτιος Ακολουθία)");
    	map.put("s15", "s15 - Troparia by Month (Τροπάρια των Ακινήτων Κατά Μήνα)");
    	map.put("s16", "s16 - Troparia of Triodion (Τροπάρια του Τριωδίον)");
    	map.put("s17", "s17 - Troparia of Pentecostarion (Τροπάρια του Πεντηκοστάριον)");
    	map.put("s18", "s18 - Resurrectional Apolytikia (Αναστάσιμα Απολυτίκια)");
    	map.put("s19", "s19 - Weekday Apolytikia (Απολυτίκια της Όλης Εβδομάδος)");
    	map.put("s20", "s20 - Theotokia by Mode and Day (Θεοτοκία εις Έκαστον Ήχον)");
    	map.put("s21", "s21 - Communion Prayers (Προσευχές της Θείας Κοινωνίας)");
    	map.put("s22", "s22 - Salutations / Akathist to Theotokos (Ακολουθία του Ακαθίστου Ύμνου εις την Υπεραγίαν Θεοτόκον)");
    	map.put("s23", "s23 - Small Paraklesis (Ακολουθία του  Μικρού Παρακλητικού Κανόνας)");
    	map.put("s24", "s24 - Great Paraklesis (Ακολουθία του  Μεγάλου Παρακλητικού Κανόνας)");
    	map.put("s25", "s25 - Supplication Canon to Lord Jesus (Κανών εις τον Κύριον Ημών Ιησούν Χριστόν)");
    	map.put("s26", "s26 - Supplication Canon to Guardian Angel (Κανών εις τον Φύλακα Άγγελον)");
    	map.put("s27", "s27 - Supplication Canon to Angels and Saints (Κανών εις  τας Δυνάμεις και εις Πάντας τους Αγίους)");
    	map.put("s28", "s28 - Akathist to the Cross (Οίκοι εις τον Τιμίον Σταυρού)");
    	map.put("s29", "s29 - Canon, Oikoi, and Hymns to the Holy Trinity (Κανών, Οίκοι, και Ύμνοι εις την Αγίαν Τριάδα)");
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
