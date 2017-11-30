package net.ages.alwb.utils.core.file;



import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.json.XML;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ioc.liturgical.ws.app.ServiceProvider;
import net.ages.alwb.utils.core.error.handling.ErrorUtils;

public class AlwbFileUtils {
	private static final Logger logger = LoggerFactory
			.getLogger(AlwbFileUtils.class);
	
	public static String fileStatsDirectory(String dir, String extension) {
		StringBuffer result = new StringBuffer();
		long fileCount = 0;
		long lineCount = 0;
		for (File f : AlwbFileUtils.getFilesInDirectory(dir, extension)) {
			fileCount = fileCount + 1;
			lineCount = lineCount + AlwbFileUtils.linesFromFile(f).size();
		}
		result.append("Files: ");
		result.append(fileCount);
		result.append("\n");
		result.append("Lines: ");
		result.append(String.format("%,d", lineCount));
		result.append("\n");
		if (extension.equals("java")) {
			result.append("Function points: ");
			result.append(String.format("%,d", lineCount * 53));
			result.append("\n");
		} else if (extension.equals("js")) {
			result.append("Function points: ");
			result.append(String.format("%,d", lineCount * 53)); // same as java, actually
			result.append("\n");
		}
		return result.toString();
	}

	/**
	 * Read all the lines in the given file and return as a list
	 * @param f - the file to read
	 * @return the lines as a List
	 */
	public static List<String> linesFromFile(File f) {
		Path path = f.toPath();
		List<String> list = null;
		try {
			list = Files.readAllLines(path, Charset.forName("UTF-8"));
		} catch (IOException e) {
			list = null;
		}
		return list;
	}

	public static String fileAsString(File f) {
		StringBuffer result = new StringBuffer();
		List<String> list  = linesFromFile(f);
		for (String line : list) {
			result.append(line);
		}
		return result.toString();
	}
	public static List<String> linesFromFile(InputStream is) {
		List<String> list = new ArrayList<String>();
		try {
			String line;
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			while  ((line = br.readLine()) != null) {
				list.add(line);
			}
		} catch (Exception e) {
			ErrorUtils.report(logger, e);
			list = null;
		}
		return list;
	}
	
	/**
	 * Finds all index.html files that are for services
	 * @param directory
	 * @param extension
	 * @return list<File> of index.html files
	 */
	public static List<File> getServicesHtmlFilesInDirectory(String directory, final String extension) {
		List<File> list = getFilesInDirectory(directory, extension);
		List<File> files = new ArrayList<File>();
		Iterator<File> it = list.iterator();
		String path = "";
		File f;
		while(it.hasNext()) {
			f = it.next();
			path = FilenameUtils.separatorsToUnix((String) f.getPath());
			if (path.contains("/h/s") && (path.endsWith("index.html"))) {
				files.add(f);
			}
		}
		return files;
	}
	
	
	public static Elements getPdfHrefs(File file) {
		Elements result = null;
		try {
			org.jsoup.nodes.Document doc = Jsoup.parse(file, "UTF-8", "http://example.com/");
			result = doc.select("a[href$=pdf]");
		} catch (IOException e) {
			e.printStackTrace();
			result = null;
		}
//		hrefsToString(result); // use for debugging
		return result;
	}

	/**
	 * Attempts to get the Href to use for the combined PDFs for this service.
	 * @param file
	 * @return the Href if found, else null
	 */
	public static String getMergedPdfHrefFromHtmlFile(File file) {
		String result = null;
		try {
			org.jsoup.nodes.Document doc = Jsoup.parse(file, "UTF-8", "http://example.com/");
			result = doc.select("title").attr("data-combo-pdf-href");
		} catch (IOException e) {
			e.printStackTrace();
			result = null;
		}
		return result;
	}

	/**
	 * Normalizes a HREF so it points to the local media Eclipse project
	 * @param hrefBaseUrl - part of URL to strip from href.  Needs to end with forward slash
	 * @param localMediaPath - path to prefix to the stripped href.  Needs to end with forward slash
	 * @param href - href to normalize
	 * @return normalized href
	 */
	public static String normalizeMediaPath(String hrefBaseUrl, String localMediaPath, String href) {
		String result = "";
		try {
			result = localMediaPath + href.split(hrefBaseUrl)[1];
		} catch (Exception e) {
//			e.printStackTrace();
			result = localMediaPath + href;
		}
		return result;
	}
	
	public static String filenameFromHref(String href) {
		String result = "";
		try {
			String [] parts = href.split("m/s")[1].split("/");
			result = parts[parts.length-1];
		} catch (Exception e) {
			e.printStackTrace();
			result = href;
		}
		return result;
	}

	public static String pathFromHref(String href) {
		String result = "";
		try {
			String [] parts = href.split("m/s")[1].split("/");
			result = StringUtils.join(parts,"/",0,parts.length-1);
		} catch (Exception e) {
			e.printStackTrace();
			result = href;
		}
		return result;
	}	
	public static String getFileContents(File f) {
		StringBuilder result = new StringBuilder();
		List<String> list = linesFromFile(f);
		try {
			for (String s : list) {
				result.append(s + " ");
			}
		} catch (Exception e) {
			// ignore
		}
		return result.toString();
	}
	
	/**
	 * Converts xml (i.e. html) into a Json Object
	 * @param f the xml (i.e. html)
	 * @return
	 */
	public static JSONObject xmlToJson(File f) {
		try {
			return XML.toJSONObject(getFileContents(f));
		} catch (Exception e) {
			ErrorUtils.report(logger, e);
			return null;
		}
	}
	
	public static String[] getPathsToFilesInDirectory(String directory, String extension, String excludeSubPath) {
		List<File> list = getFilesInDirectory(directory, extension);
		List<String> paths = new ArrayList<String>();
		Iterator<File> it = list.iterator();
		while(it.hasNext()) {
			File f = it.next();
			String path = (String)f.getPath();
			if (! path.contains(excludeSubPath)) {
				paths.add(path);
			}
		}
		return paths.toArray(new String[paths.size()]);	
	}	
	
	public static String getPathToFile(String name) {
		File f = new File(name);
		if (f.exists()) {
			return f.getAbsolutePath();
		} else {
			return "does not exist";
		}
	}
	
	/**
	 * Recursively read contents of directory and return all files found
	 * @param directory
	 * @param fileRegularExpression - a regex to apply to the filename
	 * @param extension period + file extension, e.g. .html
	 * @return List containing all files found
	 */
	public static List<File> getMatchingFilesInDirectory(String directory, String fileRegularExpression, String extension) {
		List<String> list = new ArrayList<String>();
		list.add(fileRegularExpression);
		return getMatchingFilesInDirectory(directory,list,extension);
	}
	
	
	/**
	 * Recursively read contents of directory and return all files found
	 * @param directory
	 * @param fileRegularExpressions - a string array of regular expressions to apply to the filename
	 * @param extension period + file extension, e.g. .html
	 * @return List containing all files found
	 */
	public static List<File> getMatchingFilesInDirectory(String directory, List<String> fileRegularExpressions, String extension) {
		List<File> list = getFilesInDirectory(directory, extension);
		List<File> files = new ArrayList<File>();
		Iterator<File> it = list.iterator();
		Iterator<String> regExIt;
		File f;
		
		while(it.hasNext()) {
			f = it.next();
			regExIt = fileRegularExpressions.iterator();
			while (regExIt.hasNext()) {
				if (f.getName().matches(regExIt.next())) {
					files.add(f);
				}
			}
		}
		return files;
	}

	/**
	 * Recursively read contents of directory and return all files found
	 * @param directory
	 * @param extension period + file extension, e.g. .html
	 * @return A list for all files found
	 */
	public static List<File> getFilesInDirectory(String directory, final String extension) {
		File dir = new File(directory);
		String [] extensions = {extension};
		List<File> files = null;
		try {
			files = (List<File>) FileUtils.listFiles(dir, extensions, true);
		} catch (Exception e) {
			files = null;
		}
		return files;
	}
	
	/**
	 * Create a map of files in the directory, where the 
	 * key is the name of the file and the value is the File.
	 * @param directory - where to start the loading
	 * @param extension - what file extension to look for
	 * @param dropExtension - true if you want the key to not use the file extension
	 * @return the map
	 */
	public static TreeMap<String,File> getMapOfFilesInDirectory(String directory, final String extension, boolean dropExtension) {
		TreeMap<String,File> result = new TreeMap<String,File>();
		List<File> files = getFilesInDirectory(directory,extension);
		Iterator<File> it = files.iterator();
		while (it.hasNext()) {
			File f = it.next();
			String name = f.getName();
			if (name.startsWith("._")) { // to handle a bug in Apache FileUtils that only occurs in Windows 8
				name = name.substring(2);
			}
			if (dropExtension) {
				name = name.replace("." + extension, "");
			}
			result.put(name, f);
		}
		return result;
	}
	
	/**
	 * Takes a map as input and returns a new map that only contains
	 * keys that had a substring that matches the target.
	 * @param map - the map to search
	 * @param target - the substring to apply against strings
	 * @return a map only containing entries whose key contains the target.
	 */
	public static TreeMap<String,File> filter(TreeMap<String,File> map, String target) {
		TreeMap<String,File> result = new TreeMap<String,File>();
		Iterator<String> it = map.keySet().iterator();
		String key;
		try {
			while (it.hasNext()) {
				key = (String) it.next();
				if (key.contains(target)) {
					result.put(key, map.get(key));
				}
			}
		} catch (Exception e) {
			result = null;
			ErrorUtils.report(logger, e);
		}
		return result;
	}
	
	/**
	 * Takes a map as input and returns a new map that replaces the existing domain with a new one.
	 * @param map - the map to search
	 * @param target - the substring to apply against strings
	 * @return a map only containing entries whose key contains the target.
	 */
	public static TreeMap<String,File> map(TreeMap<String,File> map, String currentDomain, String newDomain) {
		TreeMap<String,File> result = new TreeMap<String,File>();
		Iterator<String> it = map.keySet().iterator();
		String key;
		String path;
		try {
			while (it.hasNext()) {
				key = (String) it.next();
				if (key.contains(currentDomain)) {
					path = map.get(key).getPath().replace(currentDomain, newDomain);
					result.put(key.replace(currentDomain, newDomain), new File(path));
				}
			}
		} catch (Exception e) {
			result = null;
			ErrorUtils.report(logger, e);
		}
		return result;
	}

	/**
	 * Returns a file iterator for each file found in the directory, whose extension matches the call parameter
	 * @param directory - path to the root directory to start searching from.  Includes subdirectories.
	 * @param extension - the file extension to look for
	 * @return - a file iterator for matching files
	 */
	public static Iterator<File> getIteratorForFilesInDirectory(String directory, final String extension) {
		return getFilesInDirectory(directory,extension).iterator();
	}
	
	/**
	 * RestManager the part of the path that starts after the delimiter
	 * @param delimiter - string at end of which to split
	 * @param path - the path to be split
	 * @return the subpath
	 */
	public static String getSubPath(String delimiter, String path) {
		String result = "";
		try {
			result = path.split(delimiter)[1];
		} catch (Exception e) {
			ErrorUtils.report(logger, e);
		}
		return result;
	}
	
	public static String getResourceAsString(Object o, String resource) {
		String result = null;
		try {
			result = StringUtils.join(getResourceLines(o, resource), " ");
		} catch (Exception e) {
			ErrorUtils.report(logger, e);
		}
		return result;
	}
	
	public static File getResourceAsFile(Object o, String resource) {
		File result = getResourceUrlWithClassLoader(o, resource);
		if (result == null) {
			result = getResourceUrl(o,resource);
			if (result == null) {
				result = getResourceInJar(o,resource);
			}
		}
		return result;
	}
	
	/**
	 * Get the contents of the specified resource 
	 * @param o
	 * @param resource
	 * @return
	 */
	public static String getResourceFileContent(Object o, String resource) {
		try {
			return getFileContents(getResourceAsFile(o, resource));
		} catch (Exception e) {
			ErrorUtils.report(logger, e);
		}
		return null;
	}
	
	public static InputStream getResourceAsStream(Object o, String resource) {
		String newPath = "";
		ClassLoader cl = o.getClass().getClassLoader();
		if (resource.startsWith("/")) {
			newPath = "/liturgical-ws/resources" + resource;
		} else {
			newPath = "/liturgical-ws/resources/" + resource;
		}
		try {
			return cl.getResourceAsStream(newPath);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static void unzip(Object o, String resource, String targetDir) {
		  final int BUFFER = 2048;
		      try {
		    	  File target = new File(targetDir);
		    	  target.mkdirs();
		    	  InputStream is = getResourceAsStream(o,resource);
		         BufferedOutputStream dest = null;
		         ZipInputStream zis = new 
			   ZipInputStream(new BufferedInputStream(is));
		         ZipEntry entry;
		         while((entry = zis.getNextEntry()) != null) {
		            System.out.println("Extracting: " +entry);
		            int count;
		            if (entry.isDirectory()) {
		            	File dir = new File(targetDir + entry.getName());
		            	dir.mkdirs();
		            } else {
			            byte data[] = new byte[BUFFER];
			            // write the files to the disk
			            FileOutputStream fos = new 
				      FileOutputStream(targetDir + entry.getName());
			            dest = new 
			              BufferedOutputStream(fos, BUFFER);
			            while ((count = zis.read(data, 0, BUFFER)) 
			              != -1) {
			               dest.write(data, 0, count);
			            }
			            dest.flush();
			            dest.close();
		            }
		         }
		         zis.close();
		      } catch(Exception e) {
		    	  ErrorUtils.report(logger, e);
		      }
	}
	
	/**
	 * Assumptions: o is the main app class in a runnable jar and 
	 * there is a resources directory in the same folder as that class
	 * @param o
	 * @param resource
	 * @return
	 */
	public static File getResourceInJar(Object o, String resource) {
		String newPath = "";
		if (resource.startsWith("/")) {
			newPath = "/net/ages/alwb/utils/app/resources" + resource;
		} else {
			newPath = "/net/ages/alwb/utils/app/resources/" + resource;
		}
		logger.info("Dude, getting " + newPath);
		File result = getResourceUrlWithClassLoader(o, newPath);
		if (result == null) {
			result = getResourceUrl(o,newPath);
		}
		return result;
	}

	private static String switchPath(String path) {
		String newPath = path;
		if (path.startsWith("/")) {
			newPath = path.substring(1);
		} else {
			newPath = "/" + path;
		}
		return newPath;
	}
	
	private static File getResourceUrlWithClassLoader(Object o, String resource) {
		logger.info("getResourceUrilWithClassLoader: " + resource);
		File result = null;
		try {
			URL url = o.getClass().getClassLoader().getResource(resource);
			Path path = Paths.get(url.toURI());
			result = path.toFile();
		} catch (Exception e) {
			try {
				URL url = o.getClass().getClassLoader().getResource(switchPath(resource));
				Path path = Paths.get(url.toURI());
				result = path.toFile();
			} catch (Exception f) {
				result = null;
			}
		}
		return result;
	}

	private static File getResourceUrl(Object o, String resource) {
		logger.info("getResourceUrl: " + resource);
		File result = null;
		try {
			URL url = o.getClass().getResource(resource);
			Path path = Paths.get(url.toURI());
			result = path.toFile();
		} catch (Exception e) {
			try {
				URL url = o.getClass().getResource(switchPath(resource));
				Path path = Paths.get(url.toURI());
				result = path.toFile();
			} catch (Exception f) {
				result = null;
			}
		}
		return result;
	}

	public static List<String> getResourceLines(Object o, String resource) {
		List<String> result = null;
		try {
			URL url = o.getClass().getClassLoader().getResource(resource);
			Path path = Paths.get(url.toURI());
			result = Files.readAllLines(path, StandardCharsets.UTF_8);
		} catch (Exception e) {
			ErrorUtils.report(logger, e);
			result = null;
		}
		return result;
	}
	
	public static String getResourcesParentPath() {
		String result = null;
		try {
			// create a dummy test file
			File file = new File("resources/test.txt");
			String path = file.getAbsolutePath();
			result = new File(path).getParentFile().getParent();
		} catch (Exception e) {
			ErrorUtils.report(logger, e);
			result = null;
		}
		return result;
	}
	/**
	 * RestManager the part of the path that starts before the delimiter
	 * @param delimiter - string at end of which to split
	 * @param path - the path to be split
	 * @return the parent path with the delimiter added back
	 */
	public static String getParentPath(String delimiter, String path) {
		String result = "";
		try {
			String [] parts = path.split(delimiter);
			result = parts[0];
			if (parts.length > 1) {
				result = result + delimiter;
			}
		} catch (Exception e) {
			ErrorUtils.report(logger, e);
		}
		return result;
	}

	
	/**
	 * Replaces forward slashes in path with periods and returns a file 
	 * name based on the path information
	 * @param path
	 * @param fileName
	 * @return
	 */
	public static String pathToName(String path, String fileName) {
		return path.split("m/s/")[1].replace("/", ".") + fileName;
	}
	
	
	/**
	 * For a given ares filename, return the topic and domain parts
	 * @param file - ares filename
	 * @return array with topic in [0] and domain in [1]
	 */
	public static String[] getAresFileParts(String file) {
		String [] theParts;
		String [] result;
		try {
			theParts = file.split("_");
			result = new String[2];
			if (theParts.length ==4) {
				result[0] = theParts[0];
				result[1] = (theParts[1] + "." + theParts[2] + "." + theParts[3].replace(".tsf", "")).toLowerCase();
			} else {
				result = null;
			}
		} catch (Exception e) {
			result = null;
			ErrorUtils.report(logger, e);
		}
		return result;
	}	
	
	/**
	 * Create a new file and write the contents to it
	 * @param filename the name of the file, including the path
	 * @param content the contents to write
	 */
			
	public static void writeFile(String filename, String content) {
		File file;
		BufferedWriter bw = null;
		 
		try {
			file = createFile(filename);
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			bw = new BufferedWriter(fw);
			bw.write(content);
		} catch (IOException e) {
			ErrorUtils.report(logger, e);
		} finally {
			try {
				bw.close();
			} catch (IOException e) {
				ErrorUtils.report(logger, e);
			}
		}

	}
	
	
	/**
	 * Creates a new file in a safe manner
	 * @param path
	 * @return File
	 */
	public static File createFile(String path) {
		File file = null;
		try {
			file = new File(path);
			if (!file.exists()) {
				try {
					file.getParentFile().mkdirs();
				} catch (Exception ie) {}
				file.createNewFile();
			}
		} catch (Exception oe) {
			ErrorUtils.report(logger, oe, path);
		}
		return file;
	}

	/**
	 * Checks to see if the file or directory specified
	 * in the path actually exists
	 * @param path
	 * @return true if it does
	 */
	public static boolean fileExists(String path) {
		if (path.length() > 0) {
			if ((new File(path)).exists()) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * Convert from a filename to a file URL.
	 * @author John Holder
	 * @param filename
	 * @return file URL
	 */
	public static String convertToFileURL ( String filename )
	{
	    // On JDK 1.2 and later, simplify this to:
	    // "path = file.toURL().toString()".
	    String path = new File ( filename ).getAbsolutePath ();
	    if ( File.separatorChar != '/' )
	    {
	        path = path.replace ( File.separatorChar, '/' );
	    }
	    if ( !path.startsWith ( "/" ) )
	    {
	        path = "/" + path;
	    }
	    String retVal =  "file://" + path;

	    return retVal;
	}

	/**
	 * RestManager the canonical path to the tms, appending the supplied
	 * folder.
	 * @author John Holder
	 * @param folder - subfolder within the tms directory
	 * @return canonical path.  You will need to add File.separator if you append further directories.
	 */
	public static String getTmsSubFolderCanonicalPath(String subfolder) {
		String result = null;
    		try {
    			result = new File(System.getProperty("user.dir")).getCanonicalPath() + File.separator + subfolder;
		} catch (IOException e) {
			ErrorUtils.report(logger, e);
		}
    		return result;
	}

	/**
	 * Combines calls to getTmsSubFolderCanonicalPath and convertToFileURL into single method
	 * @author John Holder
	 * @param subfolder - subpath within the TMS folder
	 * @return the path formatted as a File URL, prefixed with the canonical path to the TMS folder
	 */
	public static String getTmsSubFolderFileUrlEncoded(String subpath) {
		String result = null;
		try {
			result = convertToFileURL(getTmsSubFolderCanonicalPath(subpath));
		} catch (Exception e) {
			ErrorUtils.report(logger, e);
			result = null;
		}
		return result;
	}
	
	/**
	 * Return the supplied path as a subpath within the TMS folder
	 * @author John Holder
	 * @param subfolder within the TMS folder
	 * @return the URL of the canonical path to the subfolder, prefixed with the path to the TMS folder.
	 */
	public static URL getTmsSubFolderFileURL(String subpath) {
		URL result = null;
		try {
			result = new URL (getTmsSubFolderFileUrlEncoded(subpath));
		} catch (Exception e) {
			ErrorUtils.report(logger, e);
			result = null;
		}
		return result;
	}
	/**
	 * Convenience method to get the Path to the folder containing
	 * the file or directory in the supplied path.
	 * @param path
	 * @return path to parent
	 */
	public static String getParentPath(String path) {
		try {
			return new File(path).getParent();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * For the supplied path to a directory
	 * @param path
	 * @return the immediate child directories
	 */
	public static File[] getDirectChildDirectories(String path) {
		File root = new File(path);
		return root.listFiles(directoryFilter);
	}
	
	public static File[] getDirectChildYearDirectories(String path) {
		File root = new File(path);
		return root.listFiles(directoryYearFilter);
	}

	public static File[] getDirectChildMonthDirectories(String path) {
		File root = new File(path);
		return root.listFiles(directoryMonthFilter);
	}

	public static File[] getDirectChildDayDirectories(String path) {
		File root = new File(path);
		return root.listFiles(directoryDayFilter);
	}

	public static FileFilter directoryFilter = new FileFilter() {
		public boolean accept(File file) {
			return file.isDirectory();
		}
	};
	
	public static FileFilter directoryYearFilter = new FileFilter() {
		public boolean accept(File file) {
			if (file.isDirectory()) {
				try {
					int year = Integer.parseInt(file.getName());
					return (year > 1000 && year < 3000);
				} catch (Exception e) {
					return false;
				}
			} else {
				return false;
			}
		}
	};
	
	public static FileFilter directoryMonthFilter = new FileFilter() {
		public boolean accept(File file) {
			if (file.isDirectory()) {
				try {
					int month = Integer.parseInt(file.getName());
					return (month > 0 && month < 13);
				} catch (Exception e) {
					return false;
				}
			} else {
				return false;
			}
		}
	};

	public static FileFilter directoryDayFilter = new FileFilter() {
		public boolean accept(File file) {
			if (file.isDirectory()) {
				try {
					int day = Integer.parseInt(file.getName());
					return (day > 0 && day < 32);
				} catch (Exception e) {
					return false;
				}
			} else {
				return false;
			}
		}
	};
	public static void createDir(String path) {
		try {
			File f = new File(path);
			f.mkdirs();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static List<File> getFilesFromSubdirectories(String directory, String extension) {
		List<File> list = getFilesInDirectory(directory, extension);
		String rootPath = new File(directory).getPath();
		List<File> files = new ArrayList<File>();
		Iterator<File> it = list.iterator();
		File f;
		while(it.hasNext()) {
			f = it.next();
			if (f.getParent().length() > rootPath.length()) {
				files.add(f);
			}
		}
		return files;
	}

	/**
	 * Reads HTML files in the specified directory and
	 * returns them as Jsoup documents
	 * @param dirPath
	 * @return
	 */
	public static List<Document> getJsoupDocsFromDirectory(String dirPath) {
		List<Document> result = new ArrayList<Document>();
		List<File> files = AlwbFileUtils.getFilesInDirectory(dirPath, "html");
		for (File f : files) {
			try {
				result.add(Jsoup.parse(f, "UTF-8"));
			} catch (IOException e) {
				ErrorUtils.report(logger, e);
			}
		}
		return result;
	}


}
