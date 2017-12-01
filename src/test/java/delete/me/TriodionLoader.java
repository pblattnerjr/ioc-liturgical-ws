package delete.me;

import java.io.File;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.ocmc.ioc.liturgical.utils.FileUtils;

public class TriodionLoader {

	public static void main(String[] args) {
		Map<String, String> mapGreek = new TreeMap<String, String>();
		Map<String, String> mapEnglish = new TreeMap<String, String>();

		File f = new File("/Users/mac002/Git/mcolburn/ioc-liturgical-ws/src/test/resources/triodionDayTitles.txt");
		for (String line : FileUtils.linesFromFile(f)) {
			String[] parts = line.split(",");
			String id = parts[0];
			String value = parts[1];
			String[] idParts = id.split("~");
			String[] dayParts = idParts[1].split("\\.");
			String day = dayParts[1];

			if (id.startsWith("gr_gr_cog")) {
				mapGreek.put(day, value);
			} else if (id.startsWith("en_us_dedes")) {
				mapEnglish.put(day, value);
			}

		}
		
		// add days for which there is no title
		for (int i = 1; i < 72; i++) {
			String day = "d0";
			if (i < 10) {
				day = day + "0";
			}
			day  = day + i;
			if (mapGreek.containsKey(day)) {
				// ignore
			} else {
				mapGreek.put(day, "");
			}
		}
		// map.put("a", "a - Automela (Αυτόμελα)");
		for (Entry<String, String> entry : mapGreek.entrySet()) {
			String english = "";
			String greek = entry.getValue();
			String value = "";
			if (mapEnglish.containsKey(entry.getKey())) {
				english = mapEnglish.get(entry.getKey());
				value = english + " (" + greek + ")";
			} else {
				value = greek;
			}
			System.out.println("map.put(\"" + entry.getKey() + "\", \"" + entry.getKey() + " - " + value + "\");");
		}

	}

}
