package delete.me;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FindGitDirs {

	public static List<String> getGitDirectories(File root) {
		List<String> result = new ArrayList<String>();
		for (String fileName : root.list()) {
			try {
				File file = new File(root.getCanonicalFile() + "/" + fileName);
				if (file.isDirectory()) {
					if (file.getName().equals(".git")) {
						result.add(file.getCanonicalPath());
						return result;
					} else {
						result.addAll(searchSubDirectories(file, result));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public static List<String> searchSubDirectories(File root, List<String> resultList) {
		for (String fileName : root.list()) {
			try {
				File file = new File(root.getCanonicalFile() + "/" + fileName);
				if (file.isDirectory()) {
					if (file.getName().equals(".git")) {
						resultList.add(file.getCanonicalPath());
						return resultList;
					} else {
						resultList.addAll(searchSubDirectories(file, resultList));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return resultList;
	}
	
	public static void main(String[] args) {
		try {
//			File home = new File(FileUtils.getUserDirectory().getCanonicalPath() + "/git");
			File home = new File("/volumes/ssd2/canBeRemoved/gittest");
			for (String gitDir : FindGitDirs.getGitDirectories(home)) {
				System.out.println(gitDir);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
