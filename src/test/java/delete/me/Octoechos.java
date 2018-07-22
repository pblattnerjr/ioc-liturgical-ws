package delete.me;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import org.apache.commons.io.FileUtils;
//import org.ocmc.ioc.liturgical.utils.FileUtils;

/**
 * Reads AGES path for octoechos and holy week and renames the files and
 * puts them into a different directory.
 * @author mac002
 *
 */
public class Octoechos {
	String in = "";
	String out = "";
	String e = "/e/b/";
	String h = "/h/b/";
	String p = "/p/b/";
	String oc = "oc/";
	String tr = "tr/";
	
	public Octoechos(String in, String out) {
		this.in = in;
		this.out = out;
		this.doIt();
	}

	private void processFiles(String type, String folder) {
		String inPath = this.in;
		String outPath = this.out;
		String name = "";
		switch (type) {
			case ("epub"): {
				inPath = inPath + this.e;
				outPath = outPath + this.e;
				break;
			}
			case ("html"): {
				inPath = inPath + this.h;
				outPath = outPath + this.h;
				break;
			}
			case ("pdf"): {
				inPath = inPath + this.p;
				outPath = outPath + this.p;
				break;
			}
		}
		String target = "oc";
		if (folder.startsWith("/tr")) {
			target = "tr";
		}
		inPath = inPath + folder;
		for (File srcFile : org.ocmc.ioc.liturgical.utils.FileUtils.getFilesFromSubdirectories(inPath, type)) {
			File destFile = null;
			Path path = srcFile.toPath();
			int count = path.getNameCount();
			int j = count;
			if (srcFile.getName().endsWith("epub") || srcFile.getName().endsWith("pdf")) {
				for (int i = j ; i > 0; i--) {
					Path p = path.subpath(i-2, count-1);
					name = p.toString();
					if (name.startsWith(target)) {
						break;
					}
				}
				name = name.replaceAll("\\/", ".") + "." + type;
				destFile = new File(outPath + name);
			} else {
				name = "index.html";
				String parent = srcFile.getParent();
				String [] inPathParts = parent.split(this.in + "/h/b/");
				destFile = new File(outPath + inPathParts[1] + "/" + name);
			}
			System.out.println(name);
			try {
				FileUtils.copyFile(srcFile, destFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	private void doIt() {
		this.processFiles("epub", this.oc);
		this.processFiles("html", this.oc);
		this.processFiles("pdf", this.oc);
		this.processFiles("epub", this.tr);
		this.processFiles("html", this.tr);
	}

	public static void main(String[] args) {
		String in = "/Users/mac002/Git/alwb-repositories/ages/ages-alwb-templates/net.ages.liturgical.workbench.templates/src-gen/website/test/dcs";
		String out = "/Volumes/ssd2/canBeRemoved/dcs";
		Octoechos o = new Octoechos(in, out);
	}

}
