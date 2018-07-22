package delete.me;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.ocmc.ioc.liturgical.utils.FileUtils;

/**
 * If the AresToNeo4j loader reports "Redirect key does not exist" or "Did not create",
 * copy the console output and run this to see if the not exist count = the not create count.
 * They seem to be the same, indicating that the problem starts with the redirect.
 * @author mac002
 *
 */
public class CountMissing {

	public static void main(String[] args) {
		String in = "/Users/mac002/Git/ocmc-translation-projects/ioc-liturgical-ws/src/test/java/delete/me/missing.txt";
		int r = 0;
		int d = 0;
		for (String line : FileUtils.linesFromFile(new File(in))) {
			if (line.startsWith("R")) {
				r++;
			} else if (line.startsWith("D")) {
				d++;
			}
		}
		System.out.println(r);
		System.out.println(d);
	}

}
