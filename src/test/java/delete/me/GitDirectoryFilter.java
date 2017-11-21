package delete.me;

import java.io.File;
import java.io.FilenameFilter;

public class GitDirectoryFilter implements FilenameFilter {

	@Override
	public boolean accept(File dir, String name) {
		if (dir.isDirectory() && name.equals(".git")) {
			return true;
		} else {
			return false;
		}
	}

}
