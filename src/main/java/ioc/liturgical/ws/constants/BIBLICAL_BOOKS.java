package ioc.liturgical.ws.constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.google.gson.JsonArray;

import net.ages.alwb.utils.core.datastores.json.models.DropdownItem;

public class BIBLICAL_BOOKS {
    private static Map<String, String> map = new TreeMap<String,String>();
    static {
        map.put("OT", "OT - Old Testament (Παλαιά Διαθήκη)");
        map.put("GEN", "GEN - Genesis (Γένεσις)");
        map.put("EXO", "EXO - Exodus ( Ἔξοδος)");
        map.put("LEV", "LEV - Leviticus (Λευϊτικόν)");
        map.put("NUM", "NUM - Numbers (Ἀριθμοί)");
        map.put("DEU", "DEU - Deuteronomy (Δευτερονόμιον)");
        map.put("JOS", "JOS - Joshua (Ἰησοῦς Nαυῆ)");
        map.put("JDG", "JDG - Judges (Κριταί)");
        map.put("RUT", "RUT - Ruth (Ῥούθ)");
        map.put("SA1", "SA1 - I Samuel (Βασιλειῶν)");
        map.put("SA2", "SA2 - II Samuel (Βασιλειῶν)");
        map.put("KI1", "KI1 - I Kings (Βασιλειῶν Γʹ)");
        map.put("KI2", "KI2 - II Kings (Βασιλειῶν Δʹ)");
        map.put("CH1", "CH1 - I Chronicles (Παραλειπομένων Αʹ)");
        map.put("CH2", "CH2 - II Chronicles (Παραλειπομένων Βʹ)");
        map.put("ES1", "ES1 - 1 Esdras ( Ἔσδρας Αʹ)");
        map.put("ES2", "ES2 - Ezra-Nehemiah ( Ἔσδρας Βʹ)");
        map.put("ES4", "ES4 - 4 Esdras ( Ἔσδρας Δʹ)");
        map.put("EZR", "EZR - Ezra ( Ἔσδρας)");
        map.put("NEH", "NEH - Nehemiah ( Νεεμίας)");
        map.put("PRA", "PRA - Prayer of Azariah (Προσευχή του Αζαρίου)");
        map.put("PRM", "PRM - Prayer of Manasseh (Προσευχή του Μανασσή)");
        map.put("TOB", "TOB - Tobit or Tobias (Τωβίτ)");
        map.put("JDT", "JDT - Judith (Ἰουδίθ)");
        map.put("EST", "EST - Esther (Ἐσθήρ)");
        map.put("MA1", "MA1 - 1 Maccabees (Μακκαβαίων Αʹ)");
        map.put("MA2", "MA2 - 2 Maccabees (Μακκαβαίων)");
        map.put("MA3", "MA3 - 3 Maccabees (Μακκαβαίων Γʹ)");
        map.put("PSA", "PSA - Psalms (Ψαλμοί)");
        map.put("PSX", "PSX - Psalm 151 (Ψαλμός 151)");
        map.put("ODE", "ODE - Biblical Odes (Εννέα Ωδαί)");
        map.put("PMA", "PMA - Prayer of Manasseh (Προσευχὴ Μανασσῆ)");
        map.put("JOB", "JOB - Job (Ἰώβ)");
        map.put("PRO", "PRO - Proverbs (Παροιμίαι)");
        map.put("ECC", "ECC - Ecclesiastes (Ἐκκλησιαστής)");
        map.put("SOS", "SOS - Song of Solomon or Canticles (Ἆσμα Ἀσμάτων)");
        map.put("WIS", "WIS - Wisdom (Σοφία Σαλoμῶντος)");
        map.put("SIR", "SIR - Sirach or Ecclesiasticus (Σοφία Ἰησοῦ Σειράχ)");
        map.put("POS", "POS - Psalms of Solomon (Ψαλμοί Σαλoμῶντος)");
        map.put("HOS", "HOS - Hosea (Ὡσηέ Αʹ)");
        map.put("AMO", "AMO - Amos (Ἀμώς Βʹ)");
        map.put("MIC", "MIC - Micah (Μιχαίας Γʹ)");
        map.put("JOE", "JOE - Joel (Ἰωήλ Δʹ)");
        map.put("OBA", "OBA - Obadiah (Ὀβδιού Εʹ)");
        map.put("JON", "JON - Jonah (Ἰωνᾶς Ϛ')");
        map.put("NAH", "NAH - Nahum (Ναούμ Ζʹ)");
        map.put("HAB", "HAB - Habbakuk (Ἀμβακούμ Ηʹ)");
        map.put("ZEP", "ZEP - Zephaniah (Σοφονίας Θʹ)");
        map.put("HAG", "HAG - Haggai (Ἀγγαῖος Ιʹ)");
        map.put("ZEC", "ZEC - Zechariah (Ζαχαρίας ΙΑʹ)");
        map.put("MAL", "MAL - Malachi (Μαλαχίας ΙΒʹ)");
        map.put("ISA", "ISA - Isaiah (Ἠσαΐας)");
        map.put("JER", "JER - Jeremiah (Ἱερεμίας)");
        map.put("BAR", "BAR - Baruch (Βαρούχ)");
        map.put("LAM", "LAM - Lamentations (Θρῆνοι)");
        map.put("LJE", "LJE - Letter of Jeremiah (Ἐπιστολὴ Ἰερεμίου)");
        map.put("EZE", "EZE - Ezekiel (Ἰεζεκιήλ)");
        map.put("DAN", "DAN - Daniel (Δανιήλ)");
        map.put("MA4", "MA4 - 4 Maccabees (Μακκαβαίων Δ' Παράρτημα)");
        map.put("STY", "STY - Song of Three Youths (Οι Άγιοι Τρεις Παίδες)");
        map.put("SUS", "SUS - Susanna (Σουσάννα)");
        map.put("BEL", "BEL - Bel and the Dragon (Βὴλ καὶ Δράκων)");
        map.put("NT", "NT - New Testament (H KAINH DIAθHKH)");
        map.put("Gospel", "Gospels (ΕΥΑΓΓΕΛΙΟΝ)");
        map.put("Apostle", "Apostle (ὁ Ἀπόστολος)");
        map.put("MAT", "MAT - Matthew (Κατά Ματθαίον Ευαγγέλιον)");
        map.put("MAR", "MAR - Mark (Κατά Μάρκον Ευαγγέλιον)");
        map.put("LUK", "LUK - Luke (Κατά Λουκάν Ευαγγέλιον)");
        map.put("JOH", "JOH - John (Κατά Ιωάννην Ευαγγέλιον)");
        map.put("ACT", "ACT - Acts (Πράξεις των Αποστόλων)");
        map.put("ROM", "ROM - Romans (Επιστολή προς Ρωμαίους)");
        map.put("CO1", "CO1 - 1 Corinthians (Α΄ Επιστολή προς Κορινθίους)");
        map.put("CO2", "CO2 - 2 Corinthians (Β΄ Επιστολή προς Κορινθίους)");
        map.put("GAL", "GAL - Galatians (Επιστολή προς Γαλάτες)");
        map.put("EPH", "EPH - Ephesians (Επιστολή προς Εφεσίους)");
        map.put("PHP", "PHP - Philippians (Επιστολή προς Φιλιππησίους)");
        map.put("COL", "COL - Colossians (Επιστολή προς Κολοσσαείς)");
        map.put("TH1", "TH1 - 1 Thessalonians (Α΄ Επιστολή προς Θεσσαλονικείς)");
        map.put("TH2", "TH2 - 2 Thessalonians (Β΄ Επιστολή προς Θεσσαλονικείς)");
        map.put("TI1", "TI1 - 1 Timothy (Α΄ Επιστολή προς Τιμόθεο)");
        map.put("TI2", "TI2 - 2 Timothy (Β΄ Επιστολή προς Τιμόθεο)");
        map.put("TIT", "TIT - Titus (Επιστολή προς Τίτο)");
        map.put("PHM", "PHM - Philemon (Επιστολή προς Φιλήμονα)");
        map.put("HEB", "HEB - Hebrews (Επιστολή προς Εβραίους)");
        map.put("JAM", "JAM - James (Επιστολή Ιακώβου)");
        map.put("PE1", "PE1 - 1 Peter (Α΄ Επιστολή Πέτρου)");
        map.put("PE2", "PE2 - 2 Peter (Β΄ Επιστολή Πέτρου)");
        map.put("JO1", "JO1 - 1 John (Α΄ Επιστολή Ιωάννη)");
        map.put("JO2", "JO2 - 2 John (Β΄ Επιστολή Ιωάννη)");
        map.put("JO3", "JO3 - 3 John (Γ΄ Επιστολή Ιωάννη)");
        map.put("JDE", "JDE - Jude (Επιστολή Ιούδα)");
        map.put("REV", "REV - Revelation (Αποκάλυψη του Ιωάννη)");
        map = Collections.unmodifiableMap(map);
	}
    
    public static Map<String,String> getMap() {
    	return map;
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

    /**
     * Converts the map to a JsonArray containing
     * dropdown items to use in a user interface.
     * A map key becomes the dropdown value
     * and a map value becomes the dropdown label.
     * @return
     */
    public static JsonArray toDropdownJsonArray() {
    	JsonArray array = new JsonArray();
    	for (Entry<String,String> entry : map.entrySet()) {
    		array.add(new DropdownItem(entry.getValue(), entry.getKey()).toJsonObject());
    	}
    	return array;
    }

    /**
     * Converts the map to a JsonArray containing
     * dropdown items to use in a user interface.
     * A map key becomes the dropdown value
     * and a map value becomes the dropdown label.
     * @return
     */
    public static List<DropdownItem> toDropdownList() {
    	List<DropdownItem> result = new ArrayList<DropdownItem>();
    	for (Entry<String,String> entry : map.entrySet()) {
    		result.add(new DropdownItem(entry.getValue(), entry.getKey()));
    	}
    	return result;
    }

}
