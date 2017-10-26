package delete.me;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FileUrlTest {
	

	public static void main(String[] args) {
		String macPath = "/a/b/c/h/d.txt";
		File f = new File(macPath);
		Path path = Paths.get(f.toURI());
		Iterator<Path> it = path.iterator();
		List<String> dirs = new ArrayList<String>();
		boolean found = false;
		while (it.hasNext() ) {
			Path dir = it.next();
			if(dir.toString().equals("h")) {
				found = true;
			}
			if (found) {
				dirs.add(dir.toString());
			}
		}
		StringBuffer sb = new StringBuffer();
		for (String dir : dirs) {
			if (sb.length() > 0) {
				sb.append(File.separator);
			}
			sb.append(dir);
		}
		System.out.println(sb.toString());
	}

}
