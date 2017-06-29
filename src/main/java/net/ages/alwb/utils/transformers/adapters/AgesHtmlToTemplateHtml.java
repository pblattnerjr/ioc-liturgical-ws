package net.ages.alwb.utils.transformers.adapters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ioc.liturgical.ws.constants.Constants;
import ioc.liturgical.ws.controllers.db.neo4j.Neo4jController;
import net.ages.alwb.utils.core.error.handling.ErrorUtils;
import net.ages.alwb.utils.core.id.managers.IdManager;
import net.ages.alwb.utils.transformers.adapters.models.AgesReactTemplate;
import net.ages.alwb.utils.transformers.adapters.models.HtmlElement;

import org.jsoup.nodes.Element;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;
public class AgesHtmlToTemplateHtml {
	private static final Logger logger = LoggerFactory.getLogger(AgesHtmlToTemplateHtml.class);
	private boolean printPretty = false;
	private String url = "";
	
	public AgesHtmlToTemplateHtml(String url) {
		this.url = url;
	}
	public AgesHtmlToTemplateHtml(String url, boolean printPretty) {
		this.url = url;
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
	 * Replaces the text values with expressions that are evaluated when the new content is loaded.
	 * Adds an onClick property to each value.
	 */
	public AgesReactTemplate getValues(Elements valueSpans) throws Exception {
		AgesReactTemplate result = new AgesReactTemplate(url, printPretty);
		try {
	        for (Element valueSpan : valueSpans) {
	        	String dataKey = valueSpan.attr("data-key");
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
	        	String value = valueSpan.text();
	        	String topicKey = topic + Constants.ID_DELIMITER + key;
	        	IdManager idManager = new IdManager(domain, topic, key);
	        	result.addTopicKey(topicKey);
	        	result.addValue(idManager.getId(), value);
	        }
		} catch (Exception e) {
			throw e;
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
		Parser parser = Parser.htmlParser();
		parser.settings(new ParseSettings(true, true));
		Document doc = null;
		Element content = null;
		try {
			Connection c = Jsoup.connect(url);
			c.parser(parser);
			doc = c.timeout(60*1000).get();
			content = doc.select("div.content").first();
			content.select("div.media-group").remove();
			AgesReactTemplate values = this.getValues(content.select("span.kvp"));
			result.setTopicKeys(values.getTopicKeys());
			result.setValues(values.getValues());
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
}
