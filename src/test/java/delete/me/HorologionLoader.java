package delete.me;

import java.io.File;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import net.ages.alwb.utils.core.file.AlwbFileUtils;

public class HorologionLoader {

	public static void main(String[] args) {
		Map<String, String> mapGreek = new TreeMap<String, String>();
		Map<String, String> mapEnglish = new TreeMap<String, String>();

		File f = new File("/Users/mac002/Git/mcolburn/ioc-liturgical-ws/src/test/resources/horologion.txt");
		for (String line : AlwbFileUtils.linesFromFile(f)) {
			String[] parts = line.split(" =");
			String id = parts[0];
			String value = parts[1].trim().replace("\"", "");
			String[] idParts = id.split("\\.");
			String day = "s" + idParts[0].substring(2);
			System.out.println("map.put(\"" + day + "\", \"" + day + " - " + value + "\");");
		}

	}

}
