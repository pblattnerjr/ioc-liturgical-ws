package ioc.liturgical.ws.constants;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class DAYS_OF_TRIODION {
    private static Map<String, String> map = new TreeMap<String,String>();
    static {
    	map.put("d001", "d001 - Publican and Pharisee (Τελώνου καὶ Φαρισαίου)");
    	map.put("d002", "d002 - ");
    	map.put("d003", "d003 - ");
    	map.put("d004", "d004 - ");
    	map.put("d005", "d005 - ");
    	map.put("d006", "d006 - ");
    	map.put("d007", "d007 - ");
    	map.put("d008", "d008 - Prodigal Son (Τοῦ Ἀσώτου)");
    	map.put("d009", "d009 - ");
    	map.put("d010", "d010 - ");
    	map.put("d011", "d011 - ");
    	map.put("d012", "d012 - ");
    	map.put("d013", "d013 - ");
    	map.put("d014", "d014 - Saturday of Souls (Ψυχοσάββατον)");
    	map.put("d015", "d015 - Meatfare (Ἀποκρέω)");
    	map.put("d016", "d016 - ");
    	map.put("d017", "d017 - ");
    	map.put("d018", "d018 - ");
    	map.put("d019", "d019 - ");
    	map.put("d020", "d020 - ");
    	map.put("d021", "d021 - Triodion: Holy Ascetics (Τῶν ἐν Ἀσκήσει Λαμψάντων.)");
    	map.put("d022", "d022 - Cheesefare (Τυρινῆς)");
    	map.put("d023", "d023 - Clean Monday ()");
    	map.put("d024", "d024 - ");
    	map.put("d025", "d025 - ");
    	map.put("d026", "d026 - Lent 1: Wednesday Evening ()");
    	map.put("d027", "d027 - ");
    	map.put("d028", "d028 - St Theodore Tyro and Kolyva Miracle (Τὸ διὰ Κολλύβων Θαῦμα τοῦ Ἁγίου Θεοδώρου τοῦ Τήρωνος)");
    	map.put("d029", "d029 - Sunday of Orthodoxy (Κυριακὴ τῆς Ὀρθοδοξίας.)");
    	map.put("d030", "d030 - Pan-Orthodox Vespers (Πανορθόδοξος Ἑσπερινός)");
    	map.put("d031", "d031 - ");
    	map.put("d032", "d032 - ");
    	map.put("d033", "d033 - ");
    	map.put("d034", "d034 - ");
    	map.put("d035", "d035 - ");
    	map.put("d036", "d036 - St. Gregory Palamas (Ἁγίου Γρηγορίου Παλαμᾶ)");
    	map.put("d037", "d037 - ");
    	map.put("d038", "d038 - ");
    	map.put("d039", "d039 - ");
    	map.put("d040", "d040 - ");
    	map.put("d041", "d041 - ");
    	map.put("d042", "d042 - ");
    	map.put("d043", "d043 - Veneration of the Cross (Σταυροπροσκύνησις)");
    	map.put("d044", "d044 - ");
    	map.put("d045", "d045 - ");
    	map.put("d046", "d046 - ");
    	map.put("d047", "d047 - ");
    	map.put("d048", "d048 - ");
    	map.put("d049", "d049 - ");
    	map.put("d050", "d050 - St John Climacus (Ἅγ. Ἰωάννης τῆς Κλίμακος)");
    	map.put("d051", "d051 - ");
    	map.put("d052", "d052 - ");
    	map.put("d053", "d053 - ");
    	map.put("d054", "d054 - The Great Canon (Ὁ Μέγας Κανών.)");
    	map.put("d055", "d055 - ");
    	map.put("d056", "d056 - Akathist Hymn (Σάββατον τοῦ Ἀκαθίστου)");
    	map.put("d057", "d057 - St. Mary of Egypt ()");
    	map.put("d058", "d058 - ");
    	map.put("d059", "d059 - ");
    	map.put("d060", "d060 - ");
    	map.put("d061", "d061 - ");
    	map.put("d062", "d062 - ");
    	map.put("d063", "d063 - Saturday of Lazarus (Σάββατον τοῦ Λαζάρου)");
    	map.put("d064", "d064 - Palm Sunday (Κυριακὴ τῶν Βαΐων)");
    	map.put("d065", "d065 - Holy Monday (Ἁγία καὶ Μεγάλη Δευτέρα)");
    	map.put("d066", "d066 - Holy Tuesday (Ἁγίας καὶ Μεγάλης Τρίτης)");
    	map.put("d067", "d067 - Holy Wednesday (Ἁγίας καὶ Μεγάλης Τετάρτης)");
    	map.put("d068", "d068 - Holy Thursday (Ἁγίας καὶ Μεγάλης Πέμπητς)");
    	map.put("d069", "d069 - Good Friday (Ἁγίας καὶ Μεγάλης Παρασκευῆς)");
    	map.put("d070", "d070 - Holy Saturday (Μεγάλου Σαββάτου)");
    	map.put("d071", "d071 - Holy Saturday (Τῷ Ἁγίῳ καὶ Μεγάλῳ Σαββάτῳ)");
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
