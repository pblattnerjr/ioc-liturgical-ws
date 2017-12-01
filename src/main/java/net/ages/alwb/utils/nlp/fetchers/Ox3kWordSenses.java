package net.ages.alwb.utils.nlp.fetchers;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.ArrayList;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.ocmc.ioc.liturgical.utils.FileUtils;
import net.ages.alwb.utils.nlp.models.WordSenseGev;

/**
 * Provides a means to retrieve senses of words
 *  
 *
 * @author mac002
 *
 */
public class Ox3kWordSenses {

	private static String baseUrl = "http://www.oxfordlearnersdictionaries.com/definition/english/";
	private Ox3kUtils.DOC_SOURCE listDocsSource = Ox3kUtils.DOC_SOURCE.NET;
	private Ox3kUtils.DOC_SOURCE entryDocsSource = Ox3kUtils.DOC_SOURCE.NET;
	private String path = "";
	private String source = "OALD";
	private Map<String,Integer> contextMap = new TreeMap<String,Integer>();
	private boolean prettyprint = false;

	/**
	 * Instantiating will invoke the process.
	 */
	public Ox3kWordSenses(
			) {
	}

	public Ox3kWordSenses(
			Ox3kUtils.DOC_SOURCE listSource
			, Ox3kUtils.DOC_SOURCE entrySource
			, String path
			, boolean prettyprint
			) {
		this.listDocsSource = listSource;
		this.entryDocsSource = entrySource;
		this.path = path;
		this.prettyprint = prettyprint;
	}
	
	private List<Document> getDocs(String baseHref) {
		List<Document> result = new ArrayList<Document>();
		Document doc = Ox3kUtils.getDoc(baseHref);
		if (doc != null) {
			result.add(doc);
			System.out.println(baseHref);
			if (this.entryDocsSource == Ox3kUtils.DOC_SOURCE.NET_THEN_SAVE) {
				FileUtils.writeFile(Ox3kUtils.urlToHtmlFilePath(path, baseHref + Ox3kUtils.entriesDir), doc.html());
			}
		}
		List<String> hrefs = this.getLemmaHrefs(doc.getElementsByClass("arl1"));
		if (hrefs != null) {
			for (String href : hrefs) {
				doc = Ox3kUtils.getDoc(href);
				if (doc != null) {
					result.add(doc);
					System.out.println(href);
					if (this.entryDocsSource == Ox3kUtils.DOC_SOURCE.NET_THEN_SAVE) {
						FileUtils.writeFile(Ox3kUtils.urlToHtmlFilePath(this.path+Ox3kUtils.entriesDir, href), doc.html());
					}
				}
			}
		}
		return result;
	}
	
	private List<WordSenseGev> processEntriesFromDirectory() {
		List<WordSenseGev> lemmaSenses = new ArrayList<WordSenseGev>();
		try {
			List<Document> docs = FileUtils.getJsoupDocsFromDirectory(this.path + Ox3kUtils.entriesDir);
			for (Document doc : docs) {
				Element webtop = doc.getElementsByClass("webtop-g").first();
				String lemma = "";
				String pos = "";
				try {
					lemma = webtop.getElementsByTag("h2").first().text();
					pos = webtop.getElementsByClass("pos").first().text();		
				} catch (Exception inner) {
					// ignore.  If not here, it might be with the sense.  In some cases, there is no POS at all, e.g. cannot
				}
				doc.getElementsByClass("idm-gs").remove();
				Elements elements = doc.getElementsByClass("sn-gs");
				for (Element e : elements) {
					Elements entries = e.getElementsByClass("sn-g");
					String context = "";
					try {
						context = e.getElementsByClass("shcut").first().text();
						recordContext(context);
					} catch (Exception i) {
						// ignore
					}
					for (Element entry : entries) {
						boolean isOxford3000 = false;
						try {
							Element ox3000 = entry.getElementsByClass("oxford3000").first(); 
							isOxford3000 = ox3000 != null;
						} catch (Exception eOx) {
							isOxford3000 = false;
						}
						if (pos.length() < 1) { // the head word didn't specify the POS, so see if it is in the sense
							try {
								pos = e.getElementsByClass("pos").first().text();
							} catch (Exception i) {
								// ignore
							}
						}
						String senseNbr = "1";
						try {
							senseNbr = entry.getElementsByClass("num").first().text();
						} catch (Exception i) {
							senseNbr = "1";
						}
						WordSenseGev sense = new WordSenseGev(
								lemma
								, pos
								, senseNbr
								, this.prettyprint
								);
						sense.setSource(this.source);
						sense.setContext(context);
						try {
							sense.setDef(entry.getElementsByClass("def").first().text());
						} catch (Exception i) {
							// ignore
						}
						if (isOxford3000 && sense.getDef().length() > 0) {
							if (sense.get_id().contains("TBD")) {
								System.out.println(sense.toJsonString());
							}
							lemmaSenses.add(sense);
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return lemmaSenses;

	}
	
	private List<WordSenseGev> fetch() {
		List<WordSenseGev> lemmaSenses = new ArrayList<WordSenseGev>();
		try {
			if (this.entryDocsSource == Ox3kUtils.DOC_SOURCE.DISK) {
				lemmaSenses = this.processEntriesFromDirectory(); // this has code that duplicates the code below
			} else {
				// TODO: the code below duplicates code in processEntriesFromDirectory and needs to be refactored
				List<Href> listHrefs = Ox3kUtils.getListHrefs(
						this.listDocsSource
						, this.path
						);
				for (Href listHref : listHrefs) {
					String lemma = listHref.lemma;
					List<Document> docs = getDocs(listHref.url);
					for (Document doc : docs) {
						Element webtop = doc.getElementsByClass("webtop-g").first();
						String pos = "";
						try {
							pos = webtop.getElementsByClass("pos").first().text();		
						} catch (Exception inner) {
							// ignore
						}
						doc.getElementsByClass("idm-gs").remove();
						Elements elements = doc.getElementsByClass("sn-gs");
						for (Element e : elements) {
							Elements entries = e.getElementsByClass("sn-g");
							String context = "";
							try {
								context = e.getElementsByClass("shcut").first().text();
								recordContext(context);
							} catch (Exception i) {
								// ignore
							}
							int displayInterval = 100;
							int displayed = 0;
							for (Element entry : entries) {
								boolean isOxford3000 = false;
								try {
									Element ox3000 = entry.getElementsByClass("oxford3000").first(); 
									isOxford3000 = ox3000 != null;
								} catch (Exception eOx) {
									isOxford3000 = false;
								}
								String senseNbr = "1";
								try {
									senseNbr = entry.getElementsByClass("num").first().text();
								} catch (Exception i) {
									senseNbr = "1";
								}
								WordSenseGev sense = new WordSenseGev(
										lemma
										, pos
										, senseNbr
										, this.prettyprint
										);
								sense.setContext(context);
								try {
									sense.setDef(entry.getElementsByClass("def").first().text());
								} catch (Exception i) {
									// ignore
								}
								if (isOxford3000 && sense.getDef().length() > 0) {
									System.out.println(sense.toJsonString());
									displayed ++;
									if (displayed >= displayInterval) {
										displayed = 0;
									}
									lemmaSenses.add(sense);
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lemmaSenses;
	}

	private void recordContext(String context) {
		if (this.contextMap.containsKey(context)) {
			Integer i = contextMap.get(context);
			i++;
			contextMap.put(context, i);
		} else {
			contextMap.put(context, new Integer(1));
		}
	}

	private List<String> getLemmaHrefs(Elements elements) {
		List<String> hrefs = new ArrayList<String>();
		for (Element e: elements) {
			String pos = "";
			try {
				// exclude if doesn't have a part of speech
				pos = e.getElementsByClass("pos").first().text();
			} catch (Exception inner) {
				try {
					pos = e.getElementsByTag("pos").first().text();
				} catch (Exception inner2) {
					// ignore
				}
			}
			if (pos.length() > 0) {
				hrefs.add(e.parent().attr("href"));
			}
		}
		return hrefs;
	}
	public static String getUrl() {
		return baseUrl;
	}

	public static void setUrl(String url) {
		Ox3kWordSenses.baseUrl = url;
	}

	public List<WordSenseGev> getLemmaSenses() {
		return this.fetch();
	}

	public Map<String, Integer> getContextMap() {
		return contextMap;
	}

	public void setContextMap(Map<String, Integer> contextMap) {
		this.contextMap = contextMap;
	}

}
