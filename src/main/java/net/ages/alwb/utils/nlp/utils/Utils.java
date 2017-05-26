package net.ages.alwb.utils.nlp.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.ages.alwb.utils.nlp.constants.BETA_CODES;
import net.ages.alwb.utils.nlp.models.CharacterInfo;

public class Utils {

	/**
	 * Creates a map of each character in the string.
	 * You can use the GreekCharacter to get
	 * its Unicode block and numeric value.
	 * @param s
	 * @return
	 */
	public static List<CharacterInfo> getCharacterInfo(String s) {
		List<CharacterInfo> result = new ArrayList<CharacterInfo>();
		for (char c : s.toCharArray()) {
			result.add(new CharacterInfo(c));
		}
		return result;
	}

	
	/**
	 * Gives the string index for points at which the two strings differ.
	 * The comparison is based on the Unicode numeric value for each char
	 * 
	 * This method calls its overloaded version that takes List<CharacterInfo> as the parameters.
	 * 
	 * @param s1
	 * @param s2
	 * @return
	 */
	public static List<Integer> getDiff(String s1, String s2) {
		List<Integer> result = new ArrayList<Integer>();
		if (BETA_CODES.toBetaCode(s1).equals(BETA_CODES.toBetaCode(s2))) {
			List<CharacterInfo> list1 = getCharacterInfo(s1);
			List<CharacterInfo> list2 = getCharacterInfo(s2);
			result = getDiff(list1, list2);
		}
		return result;
	}

	/**
	 * Gives the string index for points at which the two character lists differ.
	 * The comparison is based on the Unicode numeric value for each char
	 * @param s1
	 * @param s2
	 * @return
	 */
	public static List<Integer> getDiff(List<CharacterInfo> list1, List<CharacterInfo> list2) {
		List<Integer> result = new ArrayList<Integer>();
		if (list1.size() == list2.size()) {
			int j = list1.size();
			for (int i=0; i < j; i++) {
				if (list1.get(i).value() != list2.get(i).value()) {
					result.add(new Integer(i));
				}
			}
		}
		return result;
	}
	
}
