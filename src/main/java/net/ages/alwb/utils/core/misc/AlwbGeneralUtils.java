package net.ages.alwb.utils.core.misc;

import java.awt.Component;
import java.awt.Point;
import java.io.File;
import java.io.InputStream;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.apache.commons.collections.ListUtils;
import org.apache.commons.collections.list.SetUniqueList;
import org.apache.tools.ant.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.ages.alwb.utils.core.error.handling.ErrorUtils;

public class AlwbGeneralUtils {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(AlwbGeneralUtils.class);

	public final static String QUOTE = "\"";
	
	/**
	 * If the supplied domain is truly a domain, e.g. gr_gr_cog,
	 * ensures that all parts are lowercase except the middle
	 * which must be upper.
	 * @param domain e.g. gr_gr_cog
	 * @return e.g. gr_GR_cog
	 */
	public static String toAlwbFormat(String domain) {
		String result = domain;
		try {
			String [] parts = domain.split("_");
			if (parts.length == 3) {
				result = 
						parts[0].toLowerCase() 
						+ "_"
						+ parts[1].toUpperCase() 
						+ "_"
						+ parts[2].toLowerCase()
						;
			}
		} catch (Exception e) {
			ErrorUtils.report(LOGGER, e);
		}
		return result;
	}
	/**
	 * For a JTextField, sets the value, validates, and repaints
	 * @param f - the field to update
	 * @param value
	 */
	public static void setValue (JTextField f, String value) {
		f.setText(value);
		f.validate();
		f.repaint();
	}

	/**
	 * Attempts to convert the string to a JsonObject
	 * by parsing the string as json.
	 * @param json
	 * @return JsonObject made from the string.  Null if parse fails.
	 */
	public static JsonObject getJsonObject(String json) {
		JsonObject result = null;
		try {
			result = new JsonParser().parse(json).getAsJsonObject();
		} catch (Exception e) {
			ErrorUtils.report(LOGGER, e);
			result = null;
		}
		return result;
	}
	
	public static String fillString(String value, int size) {
		StringBuffer sb = new StringBuffer();
		for (int i=0; i < size; i++) {
			sb.append(value);
		}
		return sb.toString();
	}
	
	public static String padNumber(String firstChar, int width, int value) {
		return firstChar + String.format("%0" + width + "d", value);
	}
	
	/**
	 * Extracts the value from a key-value pair from
	 * an ares file
	 * @param line
	 * @return the value
	 */
	public static String valueFromPair(String line) {
		String result = null;
		try {
			if (! line.trim().startsWith("/")) {
				String [] parts = line.split("=");
				if (parts.length == 2) {
					result = parts[1].trim();
					parts = result.split("//");
					if (parts.length == 2) {
						result = parts[0];
					}
 				}
			}
		} catch (Exception e) {
			result = null;
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * Checks an ares key-value line to see if the value is a key (i.e. a pointer)
	 * If so, it will return the value as key. 
	 * @param line
	 * @param domain
	 * @return value as key if found, otherwise returns null
	 * Example:  priest = actors_gr_GR_cog.priest, will return actors_gr_GR_cog
	 */
	public static String valueAsKeyFromPair(String line, String domain) {
		String result = valueFromPair(line);
		if (result != null) {
			result = result.trim();
			if (result.startsWith("\"")) {
				result = null;
			} else {
				if (result.length() > 3) {
					String [] parts = result.split("_"+domain); // throws domain away.  We are left with left key and right key
					if (parts.length > 1) {
						result = parts[0] + "_" + domain;
					} else {
						result = null;
					}
				} else {
					result = null;
				}
			}
		}
		return result;
	}
	
	/**
	 * For the given resource name (without a .ares extension),
	 * extract the domain portion of the resource.
	 * In other words, strip off the topic part.
	 * @param resource
	 * @return the domain portion of the resource name
	 */
	public static String domainFromResource(String resource) {
		String result = "";
		try {
			String[] parts = resource.split("_");
			if (parts.length > 3) {
				result = parts[1] + "_" 
						+ parts[2] + "_"
						+ parts[3];
			} else if (parts.length == 3) {
				result = resource;
			} else {
				result = null;
			}
		} catch (Exception e) {}
		return result;
	}
	
	/**
	 * For the given resource, replace the domain portion with a new one
	 * @param resource - the resource name to be converted
	 * @param newDomain - the new domain to use
	 * @return - the converted resource
	 */
	public static String changeResourceDomain(String resource, String newDomain) {
		String result = "";
		try {
			String[] parts = resource.split("_");
			if (parts.length > 3) {
				result = parts[0] + "_" + newDomain;
			}
		} catch (Exception e) {}
		return result;
	}

	/**
	 * Convert a Set of Strings to an Array of Strings
	 * @param set
	 * @return the string array
	 */
	public static String[] setToStringArray(Set<String> set) {
		return set.toArray(new String[set.size()]);
	}
	/**
	 * Convert a List of Strings to an Array of Strings
	 * @param list
	 * @return the string array
	 */
	public static String[] listToStringArray(List<String> list) {
		return list.toArray(new String[list.size()]);
	}
	
	/**
	 * Convert a list of lists to a two dimensional string array
	 * @param multiList - the list of lists where the 2nd dimension lists are the same size
	 * @return
	 */
	public static String[][] multiListToMultiArray(List<List<String>> multiList) {
		String[][] array = new String[multiList.size()][];
		String[] blankArray = new String[0];
		for(int i=0; i < multiList.size(); i++) {
		    array[i] = multiList.get(i).toArray(blankArray);
		}
		return array;
	}
	
	/**
	 * Gets the width of the user's screen and multiplies it by the multipler.
	 * For example, to get a value that is 80% the width of the window,
	 * pass in .8.
	 * @param multiplier
	 * @return
	 */
	public static int getScreenWidth(double multiplier) {
		return (int) (java.awt.Toolkit.getDefaultToolkit().getScreenSize().width*multiplier);
	}

	/**
	 * Gets the width of the user's screen and multiplies it by the multipler.
	 * For example, to get a value that is 80% the width of the window,
	 * pass in .8.
	 * @param multiplier
	 * @return
	 */
	public static int getScreenHeight(double multiplier) {
		return (int) (java.awt.Toolkit.getDefaultToolkit().getScreenSize().height*multiplier);
	}

	/**
	 * Display an alert
	 * @param component
	 * @param alert
	 */
	public static void showAlert(String alert) {
		JOptionPane.showMessageDialog(null, alert);
	}
	
	/**
	 * Combine two lists, with no duplicates
	 * @param list1
	 * @param list2
	 * @return
	 */
	public static List<String> setUnique(List<String> list1, List<String> list2) {
		return SetUniqueList.decorate(ListUtils.union(list1, list2));
	}
	/**
	 * Converts a comma delimited String into a List<String>
	 * @param delimitedString
	 * @return the List. 
	 */
	public static List<String> stringToList(String delimitedString) {
		List<String> result = new ArrayList<String>();
		try {
			for (String s : Arrays.asList(delimitedString.split(","))) {
				result.add(s.trim());
			}
		} catch (Exception e) {
			result = new ArrayList<String>();
		}
		return result;
	}
	public static InputStream getResource(Class c, String path) {
		return c.getResourceAsStream(path);
	}
	/**
	 * Convenience method to get the Path to the folder containing
	 * the file or directory in the supplied path.
	 * @param path
	 * @return path to parent
	 */
	public static String getParentPath(String path) {
		try {
			return new File(path).getParent();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * Removes diacritics from UTF-8
	 * @param text
	 * @return lower case version of text with all diacritic marks removed
	 */
	public static String normalize(String text) {
		String result = text;
		try {
			while(result.length() > 1 && ! Character.isAlphabetic(result.charAt(0))) {
				if (result.length() > 1) {
					result = result.substring(1,result.length());
				}
			}
			result = Normalizer.normalize(result.toLowerCase(), Normalizer.Form.NFD);
			result = result.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	public static String firstNWords(String s, int nbrWords) {
		String result = "";
		StringBuffer sb = new StringBuffer();
		try {
			String [] parts = s.split(" ");
			int cnt;
			if (parts.length > nbrWords) {
				cnt = nbrWords;
				for (int i = 0; i < cnt; i++) {
					String word = parts[i];
					sb.append(word + " ");
				}
				result = sb.toString().trim();
				char last = result.charAt(result.length()-1);
				if (! Character.isLetter(last)) {
					if (last != ')') {
						result = result.substring(0, result.length()-1);
					}
				}
				result = result.replaceAll(" * ", " ");
				result = result + "...";
			} else {
				result = s;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * Create a service date by using the directory information in the path
	 * @param path - must contain at end /s/{year}/{month}{service}/{lang}/filename.html
	 * @return year/month/day
	 */
	public static Date serviceDateFromPath(String path) {
		Date result = null;
		try {
			String [] theDirs = FileUtils.getPathStack(path);
			int l = theDirs.length;
			if (theDirs.length > 7) {
				String s = theDirs[l-7];
				if (s.startsWith("s")) {
					String day = theDirs[l-4];
					String month = theDirs[l-5];
					String year = theDirs[l-6];
					SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
					result = sdf.parse(
							day 
							+ "-"
							+ month
							+ "-"
							+ year
							+ "-"
							);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	public static Properties getProperties(Class c, String path) {
		Properties prop = new Properties();
		try {
			InputStream input = null;
				input = getResource(c,path);
				prop.load(input);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return prop;
	}
	
	/**
	 * Converts a List<JsonObject> to a JsonArray containing the JsonObject occurrences.
	 * @param list
	 * @return
	 */
	public static JsonArray listToJsonArray(List<JsonObject> list) {
		JsonArray result = new JsonArray();
		for (JsonObject json : list) {
			result.add(json);
		}
		return result;
	}
	
	/**
	 * Remove leading and trailing quote from a quoted string
	 * @param s
	 * @return
	 */
	public static String trimQuotes(String s) {
		String result = s;
		if (s.length() > 2) {
			if (s.startsWith(QUOTE) || s.startsWith("“")) {
				result = s.substring(1, s.length());
			}
			if (s.endsWith(QUOTE) || s.endsWith("”")) {
				result = result.substring(0, s.length()-2);
			}
		} else if (s.compareTo(QUOTE+QUOTE) == 0) {
			result = "";
		}
		return result;
	}
	
	/**
	 * Wrap the string in quotes
	 * @param s
	 * @return
	 */
	public static String wrapQuotes(String s) {
		if (s.length() > 0) {
			return QUOTE + escapeQuotes(s) + QUOTE;
		} else {
			return QUOTE+QUOTE;
		}
	}
	
	
	public static String escapeQuotes(String text) {
		return text.replaceAll(QUOTE, "\\\\"+QUOTE);
	}
	
	public static String unescapeQuotes(String text) {
			return text.replaceAll("\\\\"+QUOTE,QUOTE);
	}

}
