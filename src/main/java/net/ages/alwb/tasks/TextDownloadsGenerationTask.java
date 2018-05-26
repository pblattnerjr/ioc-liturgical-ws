package net.ages.alwb.tasks;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import ioc.liturgical.ws.constants.Constants;
import ioc.liturgical.ws.managers.databases.external.neo4j.ExternalDbManager;

import org.ocmc.ioc.liturgical.utils.ErrorUtils;
import org.ocmc.ioc.liturgical.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;

import net.ages.alwb.utils.transformers.adapters.TextInformationToPdf;

/**
 * Runs a task (separate thread) to generate the Xelatex content for
 * a tex file.  The tex file can then be used to create a PDF.
 * @author mac002
 *
 */
public class TextDownloadsGenerationTask implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(TextDownloadsGenerationTask.class);
	JsonObject jsonObject = null;
	String pdfId = "";
	String textId = "";
	String dockerPath = "/usr/local/bin/";
	Map<String,String> domainMap = null;
	ExternalDbManager dbManager = null;
	boolean includeAdviceNotes = false;
	boolean includePersonalNotes = false;
	boolean includeGrammar  = false;
	boolean combineNotes = false;
	String alignmentLibrary = "";
	String pdfTitle = "";
	String pdfSubTitle = "";
	
	/**
	 * 
	 * @param template
	 * @param pathOut - path and filename to write the tex file
	 */
	public TextDownloadsGenerationTask (
			JsonObject jsonObject
			, String pdfId
			, String textId
			, Map<String,String> domainMap
			, ExternalDbManager dbManager
			, boolean includePersonalNotes
			, boolean includeAdviceNotes
			, boolean includeGrammar
			, boolean combineNotes
			, String alignmentLibrary
			, String pdfTitle
			, String pdfSubTitle
			) {
		this.jsonObject = jsonObject;
		this.pdfId = pdfId;
		this.textId = textId;
		this.domainMap = domainMap;
		this.dbManager = dbManager;
		this.includeAdviceNotes = includeAdviceNotes;
		this.includePersonalNotes = includePersonalNotes;
		this.includeGrammar = includeGrammar;
		this.combineNotes = combineNotes;
		this.alignmentLibrary = alignmentLibrary;
		this.pdfTitle = pdfTitle;
		this.pdfSubTitle = pdfSubTitle;
	}
	
	@Override
	public void run() {
//		String command = this.dockerPath + "docker run --rm -v " + Constants.PDF_FOLDER + ":/data macolburn/xelatex:1.0.0 make";
		TextInformationToPdf textInfoToPdf = new TextInformationToPdf(
				this.jsonObject
				, this.textId
				, this.domainMap
				, this.dbManager
				, this.pdfId
				, this.includePersonalNotes
				, this.includeAdviceNotes
				, this.includeGrammar
				, this.combineNotes
				, this.alignmentLibrary
				, this.pdfTitle
				, this.pdfSubTitle
				);
		if (textInfoToPdf.hasBibliography()) {
			FileUtils.writeFile(Constants.PDF_FOLDER + "/" + this.pdfId + ".bib", textInfoToPdf.getBibtexFileSb().toString());
		}
		FileUtils.writeFile(Constants.PDF_FOLDER + "/" + this.pdfId + ".tex", textInfoToPdf.getTexFileContent().toString());
		String command = Constants.PDF_FOLDER;
		if (textInfoToPdf.hasBibliography()) {
			command = command + "/makepdfwithbib";
		} else {
			command = command + "/makepdf";
		}
		 String result = this.executeCommandProcessor(command, this.pdfId, Constants.PDF_FOLDER); 
		if (result != null && result.length() > 0) {
			System.out.println(result);
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
