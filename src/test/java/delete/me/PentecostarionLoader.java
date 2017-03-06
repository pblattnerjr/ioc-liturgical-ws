package delete.me;

import java.io.File;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import net.ages.alwb.utils.core.file.AlwbFileUtils;

public class PentecostarionLoader {

	public static void main(String[] args) {
		Map<String, String> mapGreek = new TreeMap<String, String>();
		Map<String, String> mapEnglish = new TreeMap<String, String>();

		File f = new File("/Users/mac002/Git/mcolburn/ioc-liturgical-ws/src/test/resources/pentecostarionTitles.txt");
		for (String line : AlwbFileUtils.linesFromFile(f)) {
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
		for (int i = 71; i < 128; i++) {
			String day = "d";
			if (i < 10) {
				day = day + "00";
			} else if (i < 100) {
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
