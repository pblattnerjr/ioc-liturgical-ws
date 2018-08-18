package delete.me;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReadingsToNeo4j {

	public static void main(String[] args) {
		List<String> list = new ArrayList<String>();
		String in = "/Users/mac002/Downloads/agesLectionary/Sheet 1-1-AGES Lectionary.csv";
		for (String line : org.ocmc.ioc.liturgical.utils.FileUtils.linesFromFile(new File(in))) {
			PericopeData rd = new PericopeData(line);
			String size = String.format("%02d", rd.size);
			if (! list.contains(size) && ! size.equals("27")) {
				list.add(size);
			}
		}
		Collections.sort(list);
		for (String i : list) {
			System.out.println(i);
		}
	}

}
