package net.ages.alwb.utils.transformers.adapters;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ioc.liturgical.ws.constants.Constants;
import net.ages.alwb.utils.core.error.handling.ErrorUtils;
import net.ages.alwb.utils.core.id.managers.IdManager;
import net.ages.alwb.utils.transformers.adapters.models.AgesReactTemplate;
import net.ages.alwb.utils.transformers.adapters.models.HtmlElement;

import org.jsoup.nodes.Element;

/**
 * Reads an AGES html page for a service or sacrament, and
 * prepares it for using up to three libraries (left, center, right)
 * for display back to a user.
 * 
 * @author mac002
 *
 */
public class AgesHtmlToDynamicHtml {
	private static final Logger logger = LoggerFactory.getLogger(AgesHtmlToDynamicHtml.class);
	private boolean printPretty = false;
	private String url = "";
	private String leftLibrary = "";
	private String centerLibrary = "";
	private String rightLibrary = "";
	private String leftFallback = "";
	private String centerFallback = "";
	private String rightFallback = "";
	private Map<String,String> greekValues = new TreeMap<String,String>();
	private Map<String,String> englishValues = new TreeMap<String,String>();
	
	public AgesHtmlToDynamicHtml(
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
	}
	public AgesHtmlToDynamicHtml(
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
	
	private void loadOriginalValues(Elements valueSpans) {
		try {
        	IdManager idManager = null;
	        for (Element valueSpan : valueSpans) {
	        	String dataKey = valueSpan.attr("data-key");
	        	if (dataKey.endsWith("euLI.Key0200.text")) {
	        		System.out.print("");
	        	}
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
		} catch (Exception e) {
			throw e;
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
	public AgesReactTemplate getValues(Elements valueSpans) throws Exception {
		AgesReactTemplate result = new AgesReactTemplate(url, printPretty);
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
			        	if (dataKey.endsWith("euLI.Key0200.text")) {
//			        		System.out.println(idManager.getId());
//			        		System.out.println(greekValues.get(idManager.getId()));
			        	}
		        		value = greekValues.get(idManager.getId());
		        	} else {
			        	if (dataKey.endsWith("euLI.Key0200.text")) {
//			        		System.out.println(idManager.getId());
//			        		System.out.println(greekValues.get(idManager.getId()));
			        	}
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
			if (rightKeys.size() > 0) { // handles the case where we only have one column
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
					if (rightKeys.size() < index) {
						result = rightKeys.get(index).attr("data-key"); 
					} else {
						System.out.print("");
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
					if (leftKeys.size() < index) {
						result = leftKeys.get(index).attr("data-key"); 
					} else {
						System.out.print("");
					}
				}
			}
		} catch (Exception e) {
			ErrorUtils.report(logger, e);
		}
		return result;
	}
	private List<HtmlElement> getChildren(Elements children, int seq) throws Exception {
		List<HtmlElement> result = new ArrayList<HtmlElement>();
		try {
			for (Element child : children) {
				try {
					HtmlElement eChild = new HtmlElement(true);
					eChild.setTag(child.tagName());
					if (child.hasAttr("class")) {
						eChild.setClassName(child.attr("class"));
					}
					if (child.hasAttr("data-key")) {
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
						eChild.setDataKey(idManager.getId());
						eChild.setTopicKey(topicKey);
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
	public AgesReactTemplate toReactTemplateMetaData() throws Exception {
		AgesReactTemplate result = new AgesReactTemplate(url, printPretty);
		Document doc = null;
		Element content = null;
		try {
			Connection c = Jsoup.connect(url);
			doc = c.timeout(60*1000).get();
			content = doc.select("div.content").first();
			
			content.select("div.media-group").remove();
			content.select("div.media-group-empty").remove();
			
			Elements keys = content.select("span.kvp");
			if (keys.size() == 0) {
				keys = content.select("span.key");
			}
			this.loadOriginalValues(keys); // save off the Greek and English values
			
			if (this.centerLibrary.length() > 0) { // add the center column and get the keys again so they have the new column
				this.cloneGreek(content);
				keys = content.select("span.kvp");
				if (keys.size() == 0) {
					keys = content.select("span.key");
				}
			}
			AgesReactTemplate values = this.getValues(keys);
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
			HtmlElement eContent = new HtmlElement(printPretty);
			eContent.setTag(content.tagName());
			eContent.setClassName(content.attr("class"));
			eContent.setChildren(this.getChildren(content.children(), 0));
			result.setTopElement(eContent);
		} catch (Exception e) {
			throw e;
		}
		return result;
	}
	/**
	 * Because the content parameter is actually a pointer,
	 * this is manipulating it by cloning the rightCell (the Greek)
	 * and inserting a center cell.
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
