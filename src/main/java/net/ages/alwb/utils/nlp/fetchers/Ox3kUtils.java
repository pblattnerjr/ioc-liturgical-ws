package net.ages.alwb.utils.nlp.fetchers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.ocmc.ioc.liturgical.utils.FileUtils;

public class Ox3kUtils {
	private static final Logger logger = LoggerFactory.getLogger(Ox3kUtils.class);

	public static enum DOC_SOURCE {
	    DISK // get docs from disk
	    , NET // get docs from network, but don't save them
	    , NET_THEN_SAVE, // get docs from network, then write docs to disk
	    ; 
	}
	
	public static String listUrl = "http://www.oxfordlearnersdictionaries.com/wordlist/english/oxford3000/";
	public static String baseUrl = "http://www.oxfordlearnersdictionaries.com/definition/english/";
	public static String listDir = "/list";
	public static String entriesDir = "/entries";
	
	public static Document getDoc(String href) {
		Document doc = null;
		try {
			doc = Jsoup.connect(href)
					.timeout(6000)
					.get();
		} catch (HttpStatusException h ) {
				try {
					doc = Jsoup.connect(href + "_1")
							.timeout(6000)
							.get();
				} catch (IOException e) {
					e.printStackTrace();
					doc = null;
				}
		} catch (Exception e) {
			e.printStackTrace();
			doc = null;
		}
		return doc;
	}

	/**
	 * Gets the URLs for the Oxford 300 wordlist entries
	 * using the online list
	 * @param save
	 * @param path
	 * @return
	 */
	public static List<Href> getListHrefs(DOC_SOURCE source, String path) {
		
		// load up a list of docs for each web page to visit
		Document startingDoc = Ox3kUtils.getDoc(listUrl);
		List<Document> docList = null;
		
			docList = Ox3kUtils.getPageDocs(
					listUrl+"Oxford3000_A-B/"
					, source
					, path
					);
			Elements selectorHrefs = startingDoc.getElementById("entries-selector").getElementsByTag("a");
			for (Element selectorHref : selectorHrefs) {
				for (Document doc : getPageDocs(
						selectorHref.attr("href")
						, source
						, path
						)
						) {
					docList.add(doc);
				}
			}
		
		List<Href> hrefs = new ArrayList<Href>();
		
		for (Document doc : docList) {
			for (Href href : getPageHrefs(doc)) {
				hrefs.add(href);
			}
		}
		return hrefs;
	}

	public static List<Href> getPageHrefs(Document doc) {
		List<Href> hrefs = new ArrayList<Href>();
		Elements elements = doc.getElementsByClass("side-selector__right").first().getElementsByTag("a");
		for (Element e : elements) {
			if (e.hasText() && e.attr("href").contains("/definition/")) {
				hrefs.add(new Href(e.attr("href"), e.text()));
			}
		}
		return hrefs;
	}

	public static List<Document> getPageDocs(
			String href
			, DOC_SOURCE source
			, String path
			) {
		List<Document> result = new ArrayList<Document>();
		if (source == DOC_SOURCE.DISK) {
			result = FileUtils.getJsoupDocsFromDirectory(path + listDir);
		} else {
			String[] parts = href.split("/");
			String segment = parts[parts.length-1].split("_")[1];
			String filename = path + listDir + "/" + segment;
			Document doc = null;
			for (int i=1; i < 100; i++) {
				String pageHref = href + "?page=" + i;
				doc = Ox3kUtils.getDoc(pageHref);
				if (doc.getElementById("entrylist1").getElementsByClass("result-list1").first().childNodeSize() > 0) {
					result.add(doc);
					if (source == DOC_SOURCE.NET_THEN_SAVE) {
						FileUtils.writeFile(filename + i + ".html", doc.html());
					}
				}
				Elements pagingLinks = doc.getElementsByClass("paging_links").first().getElementsByTag("a");
				if (! pagingLinks.last().text().equals(">")) {
					break;
				}
			}
		}
		return result;
	}
	
	/**
	 * Creates a filepath using the last segment of an href
	 * @param path
	 * @param url -- a url.  It can have the protocol at the beginning, but will be ignored
	 * @return
	 */
	public static String urlToHtmlFilePath(String path, String url) {
		String[] parts = url.split("/");
		return (path.endsWith("/") ? path : path + "/") + parts[parts.length-1] + ".html";
	}


	

}
