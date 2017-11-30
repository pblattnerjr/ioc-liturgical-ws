package net.ages.alwb.tasks;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import ioc.liturgical.ws.constants.Constants;
import org.ocmc.ioc.liturgical.utils.FileUtils;
import net.ages.alwb.utils.transformers.adapters.MetaTemplateToPdf;
import net.ages.alwb.utils.transformers.adapters.models.MetaTemplate;

/**
 * Runs a task (separate thread) to generate the Xelatex content for
 * a tex file.  The tex file can then be used to create a PDF.
 * @author mac002
 *
 */
public class PdfGenerationTask implements Runnable {
	MetaTemplate template = null;
	String pdfId = "";
	String dockerPath = "/usr/local/bin/";
	
	/**
	 * 
	 * @param template
	 * @param pathOut - path and filename to write the tex file
	 */
	public PdfGenerationTask (
			MetaTemplate template
			, String pdfId
			) {
		this.template = template;
		this.pdfId = pdfId;
	}
	
	@Override
	public void run() {
		String command = this.dockerPath + "docker run --rm -v " + Constants.PDF_FOLDER + ":/data macolburn/xelatex:1.0.0 make";
		MetaTemplateToPdf metaTemplateToPdf = new MetaTemplateToPdf(this.template);
		FileUtils.writeFile(Constants.PDF_FOLDER + "/" + this.pdfId + ".tex", metaTemplateToPdf.getTexFileContent().toString());
		this.createMakeFile(pdfId, Constants.PDF_FOLDER);
		List<String> commands = new ArrayList<String>();
		commands.add(command);
		this.executeCommand(commands);
	}
	
	private synchronized String executeCommand(List<String> commands) {

		try {
			synchronized(this) {
				for (String command : commands) {
					Process p;
					p = Runtime.getRuntime().exec(command);
					p.waitFor(1, TimeUnit.MINUTES);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return "1";

	}
	
	private void createMakeFile(
			String identifier
			, String path
			) {
		StringBuffer result = new StringBuffer();
		result.append("filename=" + identifier + "\n\n");
		result.append("pdf:\n");
		result.append("\txelatex ${filename}\n");
		result.append("\txelatex ${filename}\n");
		FileUtils.writeFile(path + "/make", result.toString());
		File make = new File(path + "/make");
		make.setExecutable(true, true);
		FileUtils.writeFile(path + "/makefile", result.toString());
	}

}
