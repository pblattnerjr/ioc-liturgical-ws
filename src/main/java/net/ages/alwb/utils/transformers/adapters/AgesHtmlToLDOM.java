package net.ages.alwb.utils.transformers.adapters;

import java.util.ArrayList;
import java.util.List;
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

import ioc.liturgical.ws.constants.Constants;
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
	private Map<String,String> greekValues = new TreeMap<String,String>();
	private Map<String,String> englishValues = new TreeMap<String,String>();
	
	public AgesHtmlToLDOM(
			String url
			, String leftLibrary
			, String centerLibrary
			, String rightLibrary
			, String leftFallback
			, String centerFallback
			, String rightFallback
			) {
		this.url = url;
		this.leftLibrary = leftLibrary;
		this.centerLibrary = centerLibrary;
		this.rightLibrary = rightLibrary;
		this.leftFallback = leftFallback;
		this.centerFallback = centerFallback;
		this.rightFallback = rightFallback;
		this.setLanguageCodes();
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
	
	private void loadOriginalValues(
			Elements valueSpans
			, Elements versionDesignations
			) {
		try {
        	IdManager idManager = null;
	        for (Element valueSpan : valueSpans) {
	        	String dataKey = valueSpan.attr("data-key");
	        	String [] parts = dataKey.split("\\|");
	        	String key = parts[1];
	        	parts = parts[0].split("_");
	        	String domain = parts[1] 
						+ Constants.DOMAIN_DELIMITER 
						+ parts[2].toLowerCase() 
						+ Constants.DOMAIN_DELIMITER 
	        			+ parts[3]
	        	;
	        	String topic = parts[0];
	        	String value = "";
	        	if (valueSpan.hasClass("key")) {
	        		value = valueSpan.parent().text().trim();
	        	} else {
		        	value = valueSpan.text().trim();
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
	        for (Element valueSpan : versionDesignations) {
	        	String dataKey = valueSpan.select("span.key").attr("data-key");
	        	String [] parts = dataKey.split("\\|");
	        	String key = parts[1];
	        	parts = parts[0].split("_");
	        	String domain = parts[1] 
						+ Constants.DOMAIN_DELIMITER 
						+ parts[2].toLowerCase() 
						+ Constants.DOMAIN_DELIMITER 
	        			+ parts[3]
	        	;
	        	String topic = parts[0];
	        	String value = "";
	        	if (valueSpan.hasClass("key")) {
	        		value = valueSpan.parent().text().trim();
	        	} else {
		        	value = valueSpan.text().trim();
	        	}
	        	idManager = new IdManager(domain, topic, key);
	        	if (domain.startsWith("gr")) {
	        		if (this.greekValues.containsKey(idManager.getId())) {
	        			// ignore it
	        		} else {
		        		this.greekValues.put(idManager.getId(), value);
	        		}
	        	} else {
	        		if (this.englishValues.containsKey(idManager.getId())) {
	        			// ignore it
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
	    	String domain = parts[1] 
					+ Constants.DOMAIN_DELIMITER 
					+ parts[2].toLowerCase() 
					+ Constants.DOMAIN_DELIMITER 
	    			+ parts[3]
	    	;
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
			result.addValue(entry.getKey(), entry.getValue());
		}
		for (Entry<String,String> entry : this.englishValues.entrySet()) {
			result.addValue(entry.getKey(), entry.getValue());
		}
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
	        	if (domain != null && domain.length() > 0 && fallbackDomain != null && fallbackDomain.length()> 0) {
		        	idManager.setLibrary(fallbackDomain);
		        	String value = "";
		        	if (domain.startsWith("gr")) {
		        		value = greekValues.get(idManager.getId());
		        	} else {
		        		value = englishValues.get(idManager.getId());
		        	}
		        	if (value == null || value.length() == 0) {
		        		IdManager temp = this.dataKeyToIdManager(getTopicKeyOfAdjoiningCell(dataKey, valueSpan));
			        	if (fallbackDomain.startsWith("gr")) {
			        		temp.setLibrary("gr_gr_ages");
			        		value = greekValues.get(temp.getId());
			        	} else {
			        		temp.setLibrary("en_us_ages");
			        		value = englishValues.get(temp.getId());
			        	}
		        	}
		        	String topicKey = idManager.getTopicKey();
		        	idManager.setLibrary(domain);
		        	result.addDomain(domain);
		        	result.addTopicKey(topicKey);
		        	result.addValue(idManager.getId(), value);
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

	private String getTopicKeyOfAdjoiningCell(String dataKey, Element span) {
		String result = dataKey;
		String cellClass = "";
		Element parent = null;
		Element test = span;
		int count = -1;
		while (parent == null && count < 10) { // don't want in infinite loop, so control it with the count variable.
			test = test.parent();
			String tag = test.tagName();
			if (tag.equals("td")) {
				cellClass = test.attr("class");
			}
			if (tag.equals("tr")) {
				parent = test;
				break;
			}
			count++;
		}
		Elements leftKeys = parent.child(0).select("span.kvp");
		Elements rightKeys = null;

		if (parent.children().size() == 2) {
			rightKeys = parent.child(1).select("span.kvp");
		} else if (parent.children().size() == 3) {
			rightKeys = parent.child(2).select("span.kvp");
		}
		try {
			String key = "";
			if (rightKeys != null) { // handles the case where we only have one column
				if (cellClass.equals("leftCell") || cellClass.equals("centerCell")) {
					int index = 0;
					for (Element keySpan : leftKeys) {
						key = keySpan.attr("data-key").toLowerCase();
						if (key.equals(dataKey.toLowerCase())) {
							break;
						} else {
							index++;
						}
					}
					if (index < rightKeys.size()) {
						result = rightKeys.get(index).attr("data-key"); 
					}
				} else {
					int index = 0;
					for (Element keySpan : rightKeys) {
						key = keySpan.attr("data-key").toLowerCase();
						if (key.equals(dataKey.toLowerCase())) {
							break;
						} else {
							index++;
						}
					}
					if (index < leftKeys.size()) {
						result = leftKeys.get(index).attr("data-key"); 
					}
				}
			}
		} catch (Exception e) {
			ErrorUtils.report(logger, e);
		}
		return result;
	}
	/**
	 * Gets the children (recursively) for the elements in the parameter named 'children'
	 * @param children
	 * @param seq
	 * @return
	 * @throws Exception
	 */
	private List<LDOM_Element> getChildren(Elements children, int seq) throws Exception {
		List<LDOM_Element> result = new ArrayList<LDOM_Element>();
		try {
			for (Element child : children) {
				try {
					LDOM_Element eChild = new LDOM_Element(true);
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
			        	String key = parts[1];
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
			        	if (key.equals("version.designation")) {
			        		if (child.attr("class").equals("key")) {
								eChild.setDataKey(idManager.getId());
								eChild.setTopicKey(topicKey);
			        		}
			        	} else {
							eChild.setDataKey(idManager.getId());
							eChild.setTopicKey(topicKey);
			        	}
					}
					if (child.hasAttr("data-original")) {
			        	String dataOriginal = child.attr("data-original");
			        	String [] parts = dataOriginal.split("\\|");
			        	String key = parts[1];
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
		result.setLibraries(leftLibrary, centerLibrary, rightLibrary, leftFallback, centerFallback, rightFallback);
		Document doc = null;
		Element content = null;
		try {
			Connection c = Jsoup.connect(url);
			doc = c.timeout(60*1000).get();
			AlwbUrl urlUtils = new AlwbUrl(url);
			result.setPdfFilename(urlUtils.getFileName(), this.languageCodes);
			content = doc.select("div.content").first();

			// remove rows that contain a media-group
			content.select("tr:has(div.media-group)").remove();
			content.select("tr:has(div.media-group-empty)").remove();

			Elements keys = content.select("span.kvp");
			Elements versionDesignations = content.select("span.versiondesignation");
			if (keys.size() == 0) {
				keys = content.select("span.key");
			}
			this.loadOriginalValues(keys, versionDesignations); // save off the Greek and English values
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
			content.select("span.kvp").forEach(e -> e.attr("class", "kvp readonly"));
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
