package ioc.liturgical.ws.constants;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class HIERATIKON_SECTIONS {
    private static Map<String, String> map = new TreeMap<String,String>();
    static {
        map.put("his00", "s00 - Introduction (Εισαγωγή)");
        map.put("his01", "s01 - Vespers (Ακολουθία του Εσπερινού)");
        map.put("his02", "s02 - Great Compline (Το Μέγα Απόδειπνο)");
        map.put("his03", "s03 - Midnight Office (Το Μεσονυκτικόν)");
        map.put("his04", "s04 - Matins (Ακολουθία του  Όρθρου)");
        map.put("his05", "s05 - The Order of the All-night Vigil (Τάξις της Ολονυκτίου Αγρυπνίας)");
        map.put("his06", "s06 - Liturgy - Preparation (Ακολουθία της Προθέσεως)");
        map.put("his07", "s07 - Liturgy - John Chrysostom (Η Θεία Λειτουργία του Ιωάννου του Χρυσοστόμου)");
        map.put("his08", "s08 - Liturgy - Entrance Hymns ()");
        map.put("his09", "s09 - Liturgy - Basil the Great (Η Θεία Λειτουργία του Μεγάλου Βασιλείου)");
        map.put("his10", "s10 - Liturgy - Presanctified (Η Θεία Λειτουργία των Προηγιασμένων)");
        map.put("his11", "s11 - Concelebration ()");
        map.put("his12", "s12 - Communion Prayers (Προσευχές της Θείας Κοινωνίας)");
        map.put("his13", "s13 - Cross Ceremonies ()");
        map.put("his14", "s14 - Great Blessing of Waters (Ακολουθία του Μεγάλου Αγιασμού των Θεοφανείων)");
        map.put("his15", "s15 - Pentecost Kneeling Vespers (Ακολουθία του Εσπερινού της Πεντηκοστής  )");
        map.put("his16", "s16 - Special Ceremonies ()");
        map.put("his17", "s17 - Various Prayers (Διάφορες Προσευχές)");
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
