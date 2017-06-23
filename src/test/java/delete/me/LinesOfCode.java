package delete.me;

import net.ages.alwb.utils.core.file.AlwbFileUtils;

/**
 * Get the total count of files, lines of code, and function points for this project
 * @author mac002
 *
 */
public class LinesOfCode {

	public static void main(String[] args) {
		System.out.println(AlwbFileUtils.fileStatsDirectory("/Users/mac002/Git/ocmc-translation-projects/ioc-liturgical-ws/src/main/java", "java"));
	}

}
