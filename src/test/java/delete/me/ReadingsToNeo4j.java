package delete.me;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReadingsToNeo4j {

	public static void main(String[] args) {
		List<String> list = new ArrayList<String>();
		List<PericopeData> rawPericopes = new ArrayList<PericopeData>();
		List<String> pericopeIds = new ArrayList<String>();
		String in = "/Users/mac002/Downloads/agesLectionary/Sheet 1-1-AGES Lectionary.csv";
		for (String line : org.ocmc.ioc.liturgical.utils.FileUtils.linesFromFile(new File(in))) {
			if (line.startsWith("topic")) {
				continue;
			}
			PericopeData rd = new PericopeData(line);
			if (! rd.topicKey.startsWith("topic")) {
				rawPericopes.add(rd);
				if (! pericopeIds.contains(rd.pericopeId)) {
					pericopeIds.add(rd.pericopeId);
				}
			}
			String size = String.format("%02d", rd.size);
			if (! list.contains(size) && ! size.equals("27")) {
				list.add(size);
			}
		}
		Collections.sort(pericopeIds);
		for (String i : pericopeIds) {
			System.out.println(i);
		}
		Collections.sort(list);
		for (String i : list) {
			System.out.println(i);
		}
	}

}
