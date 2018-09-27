package net.ages.alwb.tasks;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.TimeUnit;

import ioc.liturgical.ws.constants.Constants;

import org.ocmc.ioc.liturgical.schemas.models.LDOM.LDOM;
import org.ocmc.ioc.liturgical.schemas.models.generation.GenerationStatus;
import org.ocmc.ioc.liturgical.utils.ErrorUtils;
import org.ocmc.ioc.liturgical.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ages.alwb.utils.transformers.adapters.MetaTemplateToPdf;

/**
 * Runs a task (separate thread) to generate the Xelatex content for
 * a tex file.  The tex file can then be used to create a PDF.
 * @author mac002
 *
 */
public class PdfGenerationTask implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(PdfGenerationTask.class);
	LDOM template = null;
	String pdfId = "";
	String dockerPath = "/usr/local/bin/";
	
	/**
	 * 
	 * @param template
	 * @param pathOut - path and filename to write the tex file
	 */
	public PdfGenerationTask (
			LDOM template
			, String pdfId
			) {
		this.template = template;
		this.pdfId = pdfId;
	}
	
	@Override
	public void run() {
		try {
			GenerationStatus genStatus = new GenerationStatus(this.pdfId, GenerationStatus.TYPE.PDF);
			genStatus.setStart(Instant.now().toString());
			MetaTemplateToPdf metaTemplateToPdf = new MetaTemplateToPdf(this.template);
			FileUtils.writeFile(Constants.PDF_FOLDER + "/" + this.pdfId + ".tex", metaTemplateToPdf.getTexFileContent().toString());
			String result = this.executeCommandProcessor(Constants.PDF_FOLDER + "/makepdf", this.pdfId, Constants.PDF_FOLDER);
			if (result != null && result.length() > 0) {
				System.out.println(result);
				genStatus.setFinish(Instant.now().toString());
			}
		} catch (Exception e) {
			ErrorUtils.report(logger, e);
		}
	}
	
	private synchronized String executeCommandProcessor(String command, String file, String dir) {
		StringBuffer result = new StringBuffer();
		try {
				ProcessBuilder  ps = new ProcessBuilder(command, file);
				ps.directory(new File(dir));
				ps.redirectErrorStream(true);

				Process pr = ps.start();  

				BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
				String line;
				while ((line = in.readLine()) != null) {
					result.append(line);
				}
				pr.waitFor();
				
				in.close();
				result.append("OK");
		} catch (Exception e) {
			ErrorUtils.report(logger, e);
		}
		return result.toString();
	}
	
	private synchronized String executeCommand(List<String> commands) {

		try {
			synchronized(this) {
				for (String command : commands) {
					Process p;
					p = Runtime.getRuntime().exec(command);
					BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
					String line;
					while ((line = in.readLine()) != null) {
					    System.out.println(line);
					}
					p.waitFor(1, TimeUnit.MINUTES);
					in.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return "1";

	}

}
