package net.ages.alwb.utils.nlp.utils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class RunToDumpUnicodeBlocks {

	public static void main(String[] args) {
	    Set<Character> greekChars = findCharactersInUnicodeBlock(Character.UnicodeBlock.GREEK);
	    Set<Character> greekExtendedChars = findCharactersInUnicodeBlock(Character.UnicodeBlock.GREEK_EXTENDED);
//	    print("Greek", greekChars);
	    printAsSet("Greek", greekChars);
//	    print("Extended Greek", greekExtendedChars);
	}

	private static void printAsSet(String name, Set<Character> characters) {
		Map<Character, String> map = new TreeMap<Character, String>();
		System.out.println("private static Map<String,String> map = new TreeMap<String,String>();\n");
		System.out.println("static {");
		for (Character c : characters) {
			char theChar = c.charValue();
			map.put(c, "");
			if (Character.isLowerCase(theChar) && Character.isLetter(theChar)) {
				String line = "\tmap.put(\"" + c + "\",\"\");";
				System.out.println(line);
			}
		}
		System.out.println("}");
	}

	private static void print(String name, Set<Character> characters) {
		System.out.println(name);
		for (Character c : characters) {
			char theChar = c.charValue();
			if (Character.isLowerCase(theChar) && Character.isLetter(theChar)) {
				System.out.println("\t" + theChar + ": " + (int) theChar);
			}
		}
	}
	private static Set<Character> findCharactersInUnicodeBlock(final Character.UnicodeBlock block) {
	    final Set<Character> chars = new HashSet<Character>();
	    for (int codePoint = Character.MIN_CODE_POINT; codePoint <= Character.MAX_CODE_POINT; codePoint++) {
	        if (block == Character.UnicodeBlock.of(codePoint)) {
	            chars.add((char) codePoint);
	        }
	    }
	    return chars;
	}
}
