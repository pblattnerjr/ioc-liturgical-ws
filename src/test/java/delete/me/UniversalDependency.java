package delete.me;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.ocmc.ioc.liturgical.utils.FileUtils;
public class UniversalDependency {
	public static void main(String [] args) {
		// http://universaldependencies.org/docs/format.html
		int linecount = 0;
		int tokenCount = 0;
		int sentCount = 0;
		int docCount = 0;
		List<String> depsList = new ArrayList<String>();
		List<String> miscList = new ArrayList<String>();
		
		String path = "/Users/mac002/Git/udep/UD_Ancient_Greek-Perseus/grc_perseus-ud-train.conllu";
		for (String line : FileUtils.linesFromFile(new File(path))) {
			String docId = "";
			String sentId = "";
			String text = "";
			if (line.trim().length() > 0) {
				if (line.startsWith("#")) {
					String [] parts = line.split("= ");
					if (line.startsWith("# newdoc")) {
						docId = parts[1].trim();
						docCount++;
					} else if (line.startsWith("# sent_id")) {
						sentId = parts[1].trim();
						sentCount++;
					} else if (line.startsWith("# text")) {
						text = parts[1].trim();
					} else {
						System.out.println("Can't identify line type");
						System.out.println(line);
						break;
					}
				} else {
					String [] parts = line.split("\t");
					if (parts.length == 10) {
						tokenCount++;
						String id = parts[0];
						String form = parts[1];
						String lemma = parts[2];
						String upostag = parts[3];
						String xpostag = parts[4];
						String feats = parts[5];
						String head = parts[6];
						String deprel = parts[7];
						String deps = parts[8];
						String misc = parts[9];
						if (! deps.equals("_")) {
							if (! depsList.contains(deps)) {
								depsList.add(deps);
							}
						}
						if (! misc.equals("_")) {
							if (! miscList.contains(misc)) {
								miscList.add(misc);
							}
						}
					} else {
						System.out.println("Unexpected tab count");
						System.out.println(line);
						break;
					}
				}
//				System.out.println(line);
				linecount++;
				if (linecount == 500000) {
					break;
				}
			}
		}
		System.out.println("P 9");
		for (String p : depsList) {
			System.out.println(p);
		}
		System.out.println("P 10");
		for (String p : miscList) {
			System.out.println(p);
		}
		System.out.println("Docs = " + docCount);
		System.out.println("Sents = " + sentCount);
		System.out.println("Tokens = " + tokenCount);

	}
}
