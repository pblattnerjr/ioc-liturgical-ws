package net.ages.alwb.utils.transformers.adapters;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;

import ioc.liturgical.ws.constants.Constants;
import ioc.liturgical.ws.managers.databases.external.neo4j.ExternalDbManager;

import org.ocmc.ioc.liturgical.schemas.iso.lang.LocaleDate;
import org.ocmc.ioc.liturgical.schemas.models.ws.response.ResultJsonObjectArray;
import org.ocmc.ioc.liturgical.utils.ErrorUtils;
import net.ages.alwb.utils.core.id.managers.IdManager;
import net.ages.alwb.utils.core.misc.AlwbUrl;
import net.ages.alwb.utils.transformers.adapters.models.LDOM;
import net.ages.alwb.utils.transformers.adapters.models.LDOM_Element;


/**
 * Reads an AGES html page for a service or sacrament, and
 * prepares it for using up to three libraries (left, center, right)
 * for display back to a user.
 * 
 * @author mac002
 *
 */
public class AgesHtmlToLDOM {
	private static final Logger logger = LoggerFactory.getLogger(AgesHtmlToLDOM.class);
	private boolean printPretty = false;
	private String url = "";
	private String leftLibrary = "";
	private String centerLibrary = "";
	private String rightLibrary = "";
	private String leftFallback = "";
	private String centerFallback = "";
	private String rightFallback = "";
	private String languageCodes = "";
	private boolean isDailyReading = false;
	private String readingYear = "";
	private String readingMonth = "";
	private Map<String,String> greekValues = new TreeMap<String,String>();
	private Map<String,String> englishValues = new TreeMap<String,String>();
	private ExternalDbManager dbManager = null;
	
	public AgesHtmlToLDOM(
			String url
			, String leftLibrary
			, String centerLibrary
			, String rightLibrary
			, String leftFallback
			, String centerFallback
			, String rightFallback
			, ExternalDbManager dbManager
			) {
		this.url = url;
		this.leftLibrary = leftLibrary;
		this.centerLibrary = centerLibrary;
		this.rightLibrary = rightLibrary;
		this.leftFallback = leftFallback;
		this.centerFallback = centerFallback;
		this.rightFallback = rightFallback;
		this.setLanguageCodes();
		this.dbManager = dbManager;
	}
	public AgesHtmlToLDOM(
			String url
			, String leftLibrary
			, String centerLibrary
			, String rightLibrary
			, String leftFallback
			, String centerFallback
			, String rightFallback
			, boolean printPretty
			, ExternalDbManager dbManager
			) {
		this.url = url;
		this.leftLibrary = leftLibrary;
		this.centerLibrary = centerLibrary;
		this.rightLibrary = rightLibrary;
		this.leftFallback = leftFallback;
		this.centerFallback = centerFallback;
		this.rightFallback = rightFallback;
		this.printPretty = printPretty;
		this.setLanguageCodes();
		this.dbManager = dbManager;
	}

	private void setLanguageCodes() {
		StringBuffer codes = new StringBuffer();
		IdManager idManager = new IdManager();
		idManager.setLibrary(this.leftLibrary);
		codes.append(idManager.getLibraryLanguage());
		if (this.centerLibrary != null && this.centerLibrary.length() > 0) {
			codes.append("-");
			idManager.setLibrary(this.centerLibrary);
			codes.append(idManager.getLibraryLanguage());
		}
		if (this.rightLibrary != null && this.rightLibrary.length() > 0) {
			idManager.setLibrary(this.rightLibrary);
			codes.append("-");
			codes.append(idManager.getLibraryLanguage());
		}
		this.languageCodes = codes.toString();
	}
	/**
	 * 
	 * Walks up the hierarchy of the parents of element e
	 * until it finds the parent that is the td tag (the table cell).
	 * Returns the class for that cell.
	 * @param e
	 * @return
	 */
	private String getClassOfTd(Element e) {
		String result = "";
		for (Element p : e.parents()) {
			if (p.tagName().equals("td")) {
				result = p.attr("class");
			}
		}
		return result;
	}
	
	private void normalizeDesignations(Elements versionDesignations) {
        for (Element valueSpan : versionDesignations) {
        	String id = valueSpan.select("span.key").attr("data-key");
        	String text = valueSpan.text();
    		valueSpan.addClass("kvp versiondesignation");
        		valueSpan.attr(
        				"data-key"
        				, id
        		); 
        		valueSpan.children().remove();
        		valueSpan.text(text);
        }
	}

	private void loadOriginalValues(
			Elements valueSpans
			) {
		try {
        	IdManager idManager = null;
	        for (Element valueSpan : valueSpans) {
	        	String dataKey = "";
	        	if (valueSpan.hasClass("key")) {
		        	dataKey = valueSpan.select("span.key").attr("data-key");
	        	} else {
		        	dataKey = valueSpan.attr("data-key");
	        	}
	        	String [] parts = dataKey.split("\\|");
	        	String key = parts[1];
	        	parts = parts[0].split("_");
	        	String domain = "gr_GR_cog";
	        	if (parts.length == 4) {
		        	domain = parts[1] 
							+ Constants.DOMAIN_DELIMITER 
							+ parts[2].toLowerCase() 
							+ Constants.DOMAIN_DELIMITER 
		        			+ parts[3]
		        	;
	        	}
	        	String topic = parts[0];
	        	String value = "";
	        	if (valueSpan.hasClass("key")) {
	        		value = valueSpan.parent().text().trim();
	        	} else {
		        	value = valueSpan.text().trim();
	        	}   
    			if (value.startsWith("[saint")
    					|| value.startsWith("[paragraph")
    					|| value.contains("~")
    					) {
    				value = "";
    				valueSpan.text("");
    			}
    			if (key != null && key.length() > 0) {
    				if (key.endsWith(".commemoration.text")) {
    					valueSpan.parent().removeClass("reading");
    					valueSpan.parent().addClass("designation");
    				}
    			}
	        	if (domain.startsWith("gr")) {
		        	idManager = new IdManager("gr_gr_ages", topic, key);
		        	if (value.length() == 0) {
		        		if (this.greekValues.containsKey(idManager.getId())) {
		        			// ignore it
		        		} else {
			        		this.greekValues.put(idManager.getId(), value);
		        		}
		        	} else {
		        		this.greekValues.put(idManager.getId(), value);
		        	}
	        	} else {
		        	idManager = new IdManager("en_us_ages", topic, key);
		        	if (value.length() == 0) {
		        		if (this.englishValues.containsKey(idManager.getId())) {
		        			// ignore it
		        		} else {
			        		this.englishValues.put(idManager.getId(), value);
		        		}
		        	} else {
		        		this.englishValues.put(idManager.getId(), value);
		        	}
	        	}
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	private void equalizeTopicKeys() {
		// make sure that the englishValues contains the same topic-keys as the greekValues
		for (String key : greekValues.keySet()) {
			IdManager idManager = new IdManager(key);
			idManager.setLibrary("en_us_ages");
			if (! englishValues.containsKey(idManager.getId())) {
				englishValues.put(idManager.getId(), "");
			}
		}
		// make sure that the greekValues contains the same topic-keys as the englishValues
		for (String key : englishValues.keySet()) {
			IdManager idManager = new IdManager(key);
			idManager.setLibrary("gr_gr_ages");
			if (! greekValues.containsKey(idManager.getId())) {
				greekValues.put(idManager.getId(), "");
			}
		}
	}
	
	private IdManager dataKeyToIdManager(String dataKey) {
		IdManager result = null;
		try {
	    	String [] parts = dataKey.split("\\|");
	    	String key = parts[1];
	    	parts = parts[0].split("_");
	    	String topic = parts[0];
	    	String domain = "gr_GR_cog";
	    	if (parts.length == 4) {
		    	domain = parts[1] 
						+ Constants.DOMAIN_DELIMITER 
						+ parts[2].toLowerCase() 
						+ Constants.DOMAIN_DELIMITER 
		    			+ parts[3]
		    	;
	    	}
	    	domain = domain.toLowerCase();
	    	result = new IdManager(domain + Constants.ID_DELIMITER + topic + Constants.ID_DELIMITER + key);
		} catch (Exception e) {
			ErrorUtils.report(logger, e);
		}
		return result;
	}
	
	/**
	 * Gets the content for the specified URL
	 * Builds an array of the ids used in the content.  They are a set (no duplicates).
	 */
	public LDOM getValues(Elements valueSpans) throws Exception {
		LDOM result = new LDOM(url, printPretty);
		// first add all the Greek and English values just in case
		for (Entry<String,String> entry : this.greekValues.entrySet()) {
			String value = entry.getValue();
			String key = entry.getKey();
			if (key.contains("~calendar~")) {
				result.addValue(key + ".doc", value, false);
				result.addValue(key + ".toc", value, false);
				result.addValue(key + ".header", value, false);
			}
			result.addValue(key, value, false);
		}
		for (Entry<String,String> entry : this.englishValues.entrySet()) {
			String value = entry.getValue();
			String key = entry.getKey();
			if (key.contains("~calendar~")) {
				result.addValue(key + ".doc", value, false);
				result.addValue(key + ".toc", value, false);
				result.addValue(key + ".header", value, false);
			}
			result.addValue(key, value, false);
		}
		// now add the fallbacks
		try {
	        for (Element valueSpan : valueSpans) {
	        	String tdClass = this.getClassOfTd(valueSpan);
	        	String dataKey = valueSpan.attr("data-key");
	        	IdManager idManager = this.dataKeyToIdManager(dataKey);
	        	String domain = idManager.getLibrary();
	        	String fallbackDomain = "";
	        	String originalDomain = domain;
	        	if (tdClass.equals("leftCell")) {
	        		domain = this.leftLibrary;
	        		fallbackDomain = this.leftFallback;
	        	} else if (tdClass.equals("centerCell")) {
	        		domain = this.centerLibrary;
	        		fallbackDomain = this.centerFallback;
	        	} else if (tdClass.equals("rightCell")) {
	        		domain = this.rightLibrary;
	        		fallbackDomain = this.rightFallback;
	        	}
	        	if (domain != null 
	        			&& domain.length() > 0 
	        			&& fallbackDomain != null 
	        			&& fallbackDomain.length()> 0
	        			) {
		        	idManager.setLibrary(fallbackDomain);
		        	String value = "";
		        	if (fallbackDomain.startsWith("gr")) {
		        		value = greekValues.get(idManager.getId());
		        	} else {
			        	if (idManager.getKey().equals("version.designation")) {
			        		// ignore
			        	} else {
			        		value = englishValues.get(idManager.getId());
			        	}
		        	}
		        	String topicKey = idManager.getTopicKey();
		        	idManager.setLibrary(domain);
		        	result.addDomain(domain);
		        	result.addTopicKey(topicKey);
		        	result.addValue(idManager.getId(), value, false);
	        		valueSpan.attr(
	        				"data-key"
	        				, idManager.getTopic()
	        					+ "_" 
	        						+ domain 
	        						+ "|" 
	        						+ idManager.getKey()
	        		); // e.g., titles_en_US_dedes|OnSunday
	        		valueSpan.attr(
	        				"data-original"
	        				, idManager.getTopic()
	        					+ "_" 
	        						+ originalDomain 
	        						+ "|" 
	        						+ idManager.getKey()
	        		); // e.g., titles_en_US_dedes|OnSunday
	        	}
	        }
		} catch (Exception e) {
			throw e;
		}
		return result;
	}

	/**
	 * Gets the children (recursively) for the elements in the parameter named 'children'
	 * @param children an array of Elements that are the children of the root element
	 * @param seq the sequence number
	 * @return a list of LDOM element
	 * @throws Exception if an error occurs
	 */
	private List<LDOM_Element> getChildren(Elements children, int seq) throws Exception {
		List<LDOM_Element> result = new ArrayList<LDOM_Element>();
		int count = -1;
		try {
			for (Element child : children) {
				count++;
				try {
					LDOM_Element eChild = new LDOM_Element(true);
					String key = "";
					eChild.setTag(child.tagName());
					if (child.hasAttr("class")) {
						eChild.setClassName(child.attr("class"));
					}
					if (child.hasAttr("data-key")) {
						if (child.parent().hasAttr("class")) {
							Element parent = child.parent();
							eChild.setParentClassName(child.parent().attr("class"));
						}
			        	String dataKey = child.attr("data-key");
			        	String [] parts = dataKey.split("\\|");
			        	key = parts[1];
			        	parts = parts[0].split("_");
			        	String domain = 
			        			parts[1] 
								+ Constants.DOMAIN_DELIMITER 
								+ parts[2].toLowerCase() 
								+ Constants.DOMAIN_DELIMITER 
			        			+ parts[3]
			        	;
			        	String topic = parts[0];
			        	String topicKey = topic + Constants.ID_DELIMITER + key;
			        	IdManager idManager = new IdManager(domain, topic, key);
						eChild.setDataKey(idManager.getId());
						eChild.setTopicKey(topicKey);
			        	if (key.equals("version.designation")) {
			        		Element childParent = child.parent();
			        		Element sibling = child.firstElementSibling();
			        		child.addClass("versiondesignation");
			        		eChild.setClassName(child.className());
			        	}
					}
					if (child.hasAttr("data-original")) {
			        	String dataOriginal = child.attr("data-original");
			        	String [] parts = dataOriginal.split("\\|");
			        	key = parts[1];
			        	parts = parts[0].split("_");
			        	String domain = 
			        			parts[1] 
								+ Constants.DOMAIN_DELIMITER 
								+ parts[2].toLowerCase() 
								+ Constants.DOMAIN_DELIMITER 
			        			+ parts[3]
			        	;
			        	String topic = parts[0];
			        	IdManager idManager = new IdManager(domain, topic, key);
						eChild.setDataOriginal(idManager.getId());
					}
					seq = seq + 1;
					eChild.setKey("V" + seq);
					if (child.children().size() > 0) {
						eChild.setChildren(getChildren(child.children(), seq));
					} 
					if (child.text().contains("RSV")) {
						String stop = "halt";
					}
					result.add(eChild);
				} catch (Exception inner) {
					throw inner;
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return result;
	}
	
	
	/**
	 * This version removes the media-group
	 * @return
	 * @throws Exception
	 */
	public LDOM toLDOM() throws Exception {
		LDOM result = new LDOM(url, printPretty);
		result.setLibraries(
				leftLibrary
				, centerLibrary
				, rightLibrary
				, leftFallback
				, centerFallback
				, rightFallback
				);
		Document doc = null;
		Element content = null;
		Connection c = null;
		try {
			if (url.contains("resources/theophany")) {
				File in = new File(url);
				doc = Jsoup.parse(in, "UTF-8", "http://example.com/");
			} else {
				c = Jsoup.connect(url);
				doc = c.timeout(60*1000).maxBodySize(0).get();
			}
			AlwbUrl urlUtils = new AlwbUrl(url);

			// see if this webpage is for daily readings for OCMC
			String titleText = doc.select("title").text();
			if (titleText.startsWith("cu.ocmc_guatemala")) {
				String [] titleParts = titleText.split("_");
				if (titleParts.length == 4) {
					this.isDailyReading = true;
					this.readingYear = titleParts[2];
					this.readingMonth = titleParts[3];
				}
			}
			
			result.setPdfFilename(urlUtils.getFileName(), this.languageCodes);
			content = doc.select("div.content").first();
					
			// remove rows that contain a media-group
			content.select("tr:has(div.media-group)").remove();
			content.select("tr:has(div.media-group-empty)").remove();

			// See if this is a lectionary.  If so, we need to handle the day titles.
			Elements boldredSpans = content.select("span.boldred");
			for (Element boldred : boldredSpans) {
				if (boldred.select("span.kvp").isEmpty()) {
					String library =  "gr_gr_cog";
					String day = "";
					if (boldred.parent().parent().hasClass("rightCell")) {
						library = "en_us_dedes";
					}
					String text = boldred.text();
					String [] parts = text.split(",");
					if (parts.length == 3) {
						parts = parts[1].trim().split(" ");
						if (parts.length == 2) {
							int intDay = Integer.parseInt(parts[1].trim());
							day = String.format("%02d", intDay);
						} else {
							logger.info("Unexpected text format.  Not a date? " + text);
						}
					} else {
						logger.info("Unexpected text format.  Not a date? " + text);
					}
					String newKey = "calendar_" 
							+ library 
							+ "|y" 
							+ this.readingYear 
							+ ".m" 
							+ this.readingMonth
							+ ".d" 
							+ day
							+ ".md";
					boldred.attr("class", "kvp");
					boldred.attr("data-key", newKey);
					boldred.parent().attr("class","LectionaryDate");
				}
			}
			Elements versionDesignations = content.select("span.versiondesignation");
			this.normalizeDesignations(versionDesignations);
			Elements keys = content.select("span.kvp, span.key");
			this.loadOriginalValues(keys); // load the Greek and English values
			this.equalizeTopicKeys(); // sometimes the Greek or English has extra topic-keys.  Make sure they both have the same ones.
			
			if (this.centerLibrary.length() > 0) { // add the center column and get the keys again so they have the new column
				this.cloneGreek(content);
				keys = content.select("span.kvp");
				if (keys.size() == 0) {
					keys = content.select("span.key");
				}
			}

			LDOM values = this.getValues(keys);
			result.setDomains(values.getDomains());
			result.setTopicKeys(values.getTopicKeys());
			result.setValues(values.getValues());

			if (this.centerLibrary == null || this.centerLibrary.length() == 0) {
				content.select("td.centerCell").remove();
			}
			if (this.rightLibrary == null || this.rightLibrary.length() == 0) {
				content.select("td.rightCell").remove();
			}
			if (this.centerLibrary.length() > 0 && this.rightLibrary.length() > 0) {
				content.select("td.leftCell").forEach(e -> e.attr("class", "cellOneOfThree"));
				content.select("td.centerCell").forEach(e -> e.attr("class", "cellTwoOfThree"));
				content.select("td.rightCell").forEach(e -> e.attr("class", "cellThreeOfThree"));
			} else if (this.centerLibrary.length() > 0) {
				content.select("td.leftCell").forEach(e -> e.attr("class", "cellOneOfTwo"));
				content.select("td.centerCell").forEach(e -> e.attr("class", "cellTwoOfTwo"));
			} else if (this.rightLibrary.length() > 0) {
				content.select("td.leftCell").forEach(e -> e.attr("class", "cellOneOfTwo"));
				content.select("td.rightCell").forEach(e -> e.attr("class", "cellTwoOfTwo"));
			} else {
				content.select("td.leftCell").forEach(e -> e.attr("class", "cellOneOfOne"));
			}
			content.select("span.kvp").forEach(e -> {
				e.addClass("readonly");
				if (e.hasClass("versiondesignation")) {
					Element sibling = e.firstElementSibling();
					if (! sibling.hasText()) {
						e.addClass("deleteThis");
					}
				}
				this.setValue(e);
			}
			);
			
			content.select("p.chapverse").forEach(e -> {
				Element tr = e.parent().parent();
				Element followingTr = tr.nextElementSibling();
				if (followingTr.text().trim().length() == 0) {
					tr.addClass("deleteThis");
				}
			});
			content.select("p.break").forEach(e -> {
				Element tr = e.parent().parent();
				tr.addClass("deleteThis");
			});
			content.select("tr").forEach(e -> {
				if (e.text().trim().length() == 0) {
					e.addClass("deleteThis");
				}
			});
			
			content.select("tr.deleteThis").remove();
			content.select("span.deleteThis").remove();

			LDOM_Element eContent = new LDOM_Element(printPretty);
			eContent.setTag(content.tagName());
			eContent.setClassName(content.attr("class"));
			if (content.parent().hasAttr("class")) {
				eContent.setParentClassName(content.parent().attr("class"));
			}
			eContent.setChildren(this.getChildren(content.children(), 0));
			result.setTopElement(eContent);
		} catch (Exception e) {
			throw e;
		}
		return result;
	}
		
	private void setValue(Element e) {
    	String value = "";
    	String dataKey = e.attr("data-key");
    	String [] parts = dataKey.split("\\|");
    	String key = parts[1];
    	parts = parts[0].split("_");
    	String domain = 
    			parts[1] 
				+ Constants.DOMAIN_DELIMITER 
				+ parts[2].toLowerCase() 
				+ Constants.DOMAIN_DELIMITER 
    			+ parts[3]
    	;
    	if (parts[1].equals("gr")) {
    		domain = "gr_gr_ages";
    	} else if (parts[1].equals("en")) {
    		domain = "en_us_ages";
    	}
    	String topic = parts[0];
    	IdManager idManager = new IdManager(domain, topic, key);

    	if (e.parent().hasClass("LectionaryDate")) {
			value = e.text();
    	} else {
        	// first see if we already have the value
        	if (domain.startsWith("gr")) {
        		if (this.greekValues.containsKey(idManager.getId())) {
        			value = this.greekValues.get(idManager.getId());
        		}
        	} else if (domain.startsWith("en")) {
        		if (this.englishValues.containsKey(idManager.getId())) {
        			value = this.englishValues.get(idManager.getId());
        		}
        	}
        	if (value.length() == 0) {
        		// we did not find the value in memory, so do a database call
            	value = this.getValue(idManager.getId());
            	if (value.length() == 0) {
        			String fallbackLibrary = "";
        			if (leftLibrary != null && domain.equals(leftLibrary)) {
        				fallbackLibrary = leftFallback;
        			} else if (centerLibrary != null && domain.equals(centerLibrary)) {
        				fallbackLibrary = centerFallback;
        			} else if (rightLibrary != null && domain.equals(rightLibrary)) {
        				fallbackLibrary = rightFallback;
        			}
        			idManager.setLibrary(fallbackLibrary);
        	    	value = this.getValue(idManager.getId());
            	}
        	}
        	if (value.startsWith("[saint") || value.startsWith("[paragraph")) {
        		value = "";
        	}
    	}
		e.text(value);
	}
	
	private String getValue(String id) {
		String result = "";
		ResultJsonObjectArray dbValue = dbManager.getForId(id, "Root");
		if (dbValue.valueCount == 1) {
			JsonObject o = dbValue.getFirstObject();
			if (o.get("value").getAsString().trim().length() > 0) {
				result = dbValue.getFirstObjectValueAsString();
			}
		}
		return result;
	}
	/**
	 * Because Java uses pass by reference for parameters,
	 * the content parameter is actually a pointer.
	 * This method uses the pointer to clone the rightCell (the Greek)
	 * and insert a center cell.
	 * @param content
	 */
	private void cloneGreek(Element content) {
		for (Element cell : content.select("td.rightCell")) {
			Element clone = cell.clone();
			clone.attr("class","centerCell");
			cell.before(clone);
		}
	}
}
