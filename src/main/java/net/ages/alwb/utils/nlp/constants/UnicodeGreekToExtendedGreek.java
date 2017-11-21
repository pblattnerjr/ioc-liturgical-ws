package net.ages.alwb.utils.nlp.constants;
import java.util.Map;
import java.util.TreeMap;

public class UnicodeGreekToExtendedGreek {
	private static Map<String,String> map = new TreeMap<String,String>();
	static {
		map.put("ί",Character.toString((char) 8055));
	}
	
	/**
	 * Where necessary, converts Unicode Greek
	 * to Unicode Extended Greek.
	 * @param s
	 * @return
	 */
	public static String normalize(String word) {
		StringBuffer sb  = new StringBuffer();
		for (char c : word.toCharArray()) {
			String s = String.valueOf(c);
			if (map.containsKey(s)) {
				sb.append(map.get(s));
			} else {
				sb.append(s);
			}
		}
		return sb.toString();
	}

	
	public static void main(String[] args) {
		for (String s : map.keySet()) {
			System.out.println(s + ":" + (int) s.charAt(0) + ":"+ (int) map.get(s).charAt(0) );
		}
	}
}
