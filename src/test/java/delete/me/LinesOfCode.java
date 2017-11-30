package delete.me;

import org.ocmc.ioc.liturgical.utils.FileUtils;

/**
 * Get the total count of files, lines of code, and function points for this project
 * @author mac002
 *
 */
public class LinesOfCode {

	public static void main(String[] args) {
		System.out.println(FileUtils.fileStatsDirectory("/Users/mac002/Git/ocmc-translation-projects/ioc-liturgical-ws/src/main/java", "java"));
	}

}
