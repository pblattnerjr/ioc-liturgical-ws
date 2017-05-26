package net.ages.alwb.utils.nlp.constants;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import net.ages.alwb.utils.core.misc.AlwbGeneralUtils;

public class BETA_CODES {
    private static Map<String, String> map = new TreeMap<String,String>();
    static {
        map.put("α", "a");
        map.put("β", "b");
        map.put("γ", "g");
        map.put("δ", "d");
        map.put("ε", "e");
        map.put("ζ", "z");
        map.put("η", "h");
        map.put("θ", "q");
        map.put("ι", "i");
        map.put("κ", "k");
        map.put("λ", "l");
        map.put("μ", "m");
        map.put("ν", "n");
        map.put("ξ", "c");
        map.put("ο", "o");
        map.put("π", "p");
        map.put("ρ", "r");
        map.put("σ", "s");
        map.put("ς", "s");
        map.put("τ", "t");
        map.put("υ", "u");
        map.put("φ", "f");
        map.put("χ", "x");
        map.put("Ψ", "y");
        map.put("ω", "w");
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
    
    /**
     * Converts UTF-8 Greek into lowercase beta code without diacritics
     * @param s
     * @return
     */
    public static String toBetaCode(String word) {
    	StringBuffer sb = new StringBuffer();
    	String normalized = AlwbGeneralUtils.normalize(word.trim()).toLowerCase();
    	int l = normalized.length();
    	for (int i = 0; i < l; i++) {
    		String c = String.valueOf(normalized.charAt(i));
    		if (map.containsKey(c)) {
        		sb.append(map.get(c));
    		}
    	}
    	return sb.toString();
    }

}
