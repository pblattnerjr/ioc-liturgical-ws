package net.ages.alwb.utils.transformers.adapters;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ioc.liturgical.ws.constants.Constants;
import net.ages.alwb.utils.core.id.managers.IdManager;
import net.ages.alwb.utils.transformers.adapters.models.PopulatedObjectModel;
import net.ages.alwb.utils.transformers.adapters.models.TemplateElement;

import org.jsoup.nodes.Element;

public class AgesHtmlToEditablePOM {
	private static final Logger logger = LoggerFactory.getLogger(AgesHtmlToEditablePOM.class);
	private boolean printPretty = false;
	private String url = "";
	private String centerLibrary = "";
	
	public AgesHtmlToEditablePOM(String url) {
		this.url = url;
	}
	public AgesHtmlToEditablePOM(String url, boolean printPretty) {
		this.url = url;
		this.printPretty = printPretty;
	}

	public AgesHtmlToEditablePOM(String url, String centerLibrary) {
		this.url = url;
		this.centerLibrary = centerLibrary;
	}
	public AgesHtmlToEditablePOM(String url, String centerLibrary, boolean printPretty) {
		this.url = url;
		this.centerLibrary = centerLibrary;
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
	/**
	 * Gets the content for the specified URL
	 * Builds an array of the ids used in the content.  They are a set (no duplicates).
	 */
	public PopulatedObjectModel getValues(
			Elements valueSpans
			, Elements versionDesignations
			) throws Exception {
		PopulatedObjectModel result = new PopulatedObjectModel(url, printPretty);
		try {
	        for (Element valueSpan : valueSpans) {
	        	String tdClass = this.getClassOfTd(valueSpan);
	        	String dataKey = valueSpan.attr("data-key");
	        	String [] parts = dataKey.split("\\|");
	        	String key = parts[1];
	        	parts = parts[0].split("_");
	        	String domain = "";
	        	if (tdClass.equals("centerCell")) {
	        		domain = this.centerLibrary;
	        	} else {
	        		domain =
		        			parts[1] 
		    						+ Constants.DOMAIN_DELIMITER 
		    						+ parts[2].toLowerCase() 
		    						+ Constants.DOMAIN_DELIMITER 
		    	        			+ parts[3]
		    	        	;
	        	}
	        	String topic = parts[0];
	        	String value = "";
	        	if (valueSpan.hasClass("key")) {
	        		value = valueSpan.parent().text();
	        	} else {
		        	value = valueSpan.text();
	        	}
	        	String topicKey = topic + Constants.ID_DELIMITER + key;
	        	IdManager idManager = new IdManager(domain, topic, key);
	        	result.addDomain(domain);
	        	result.addTopicKey(topicKey);
	        	result.addValue(idManager.getId(), value);
	        	if (tdClass.equals("centerCell")) {
	        		valueSpan.attr(
	        				"data-key"
	        				, topic
	        					+ "_" 
	        						+ domain 
	        						+ "|" 
	        						+ key
	        		); // e.g., titles_en_US_dedes|OnSunday
	        	}
	        }
	        for (Element valueSpan : versionDesignations) {
	        	String tdClass = this.getClassOfTd(valueSpan);
	        	String dataKey = valueSpan.select("span.key").attr("data-key");
	        	String [] parts = dataKey.split("\\|");
	        	String key = parts[1];
	        	parts = parts[0].split("_");
	        	String domain = "";
	        	if (tdClass.equals("centerCell")) {
	        		domain = this.centerLibrary;
	        	} else {
	        		domain =
		        			parts[1] 
		    						+ Constants.DOMAIN_DELIMITER 
		    						+ parts[2].toLowerCase() 
		    						+ Constants.DOMAIN_DELIMITER 
		    	        			+ parts[3]
		    	        	;
	        	}
	        	String topic = parts[0];
	        	String value = "";
	        	if (valueSpan.hasClass("key")) {
	        		value = valueSpan.parent().text();
	        	} else {
		        	value = valueSpan.text();
	        	}
	        	String topicKey = topic + Constants.ID_DELIMITER + key;
	        	IdManager idManager = new IdManager(domain, topic, key);
	        	result.addDomain(domain);
	        	result.addTopicKey(topicKey);
	        	result.addValue(idManager.getId(), value);
	        	if (tdClass.equals("centerCell")) {
	        		valueSpan.attr(
	        				"data-key"
	        				, topic
	        					+ "_" 
	        						+ domain 
	        						+ "|" 
	        						+ key
	        		); // e.g., titles_en_US_dedes|OnSunday
	        	}
	        }
		} catch (Exception e) {
			throw e;
		}
		return result;
	}

	private List<TemplateElement> getChildren(Elements children, int seq) throws Exception {
		List<TemplateElement> result = new ArrayList<TemplateElement>();
		try {
			for (Element child : children) {
				try {
					TemplateElement eChild = new TemplateElement(true);
					eChild.setTag(child.tagName());
					if (child.hasAttr("class")) {
						eChild.setClassName(child.attr("class"));
					}
					if (child.parent().hasAttr("class")) {
						eChild.setParentClassName(child.parent().attr("class"));
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
			        	eChild.setDataDomain(domain);
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
	public PopulatedObjectModel toPOM() throws Exception {
		PopulatedObjectModel result = new PopulatedObjectModel(url, printPretty);
		Document doc = null;
		Element content = null;
		try {
			Connection c = Jsoup.connect(url);
			doc = c.timeout(60*1000).get();
			content = doc.select("div.content").first();
			// remove rows that contain a media-group
			content.select("tr:has(div.media-group)").remove();
			content.select("tr:has(div.media-group-empty)").remove();

			if (this.centerLibrary.length() > 0) {
				this.cloneGreek(content);
			}
			Elements keys = content.select("span.kvp");
			if (keys.size() == 0) {
				keys = content.select("span.key");
			}
			Elements versionDesignations = content.select("span.versiondesignation");
			PopulatedObjectModel values = this.getValues(keys, versionDesignations);
			result.setDomains(values.getDomains());
			result.setTopicKeys(values.getTopicKeys());
			result.setValues(values.getValues());
			TemplateElement eContent = new TemplateElement(printPretty);
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
	
	private void cloneGreek(Element content) {
		for (Element cell : content.select("td.rightCell")) {
			Element clone = cell.clone();
			clone.attr("class","centerCell");
			cell.before(clone);
		}
	}
}
