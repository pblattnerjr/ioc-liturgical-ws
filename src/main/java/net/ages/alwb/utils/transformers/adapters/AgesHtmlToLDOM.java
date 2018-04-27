package net.ages.alwb.utils.transformers.adapters;

import java.io.File;
import java.time.Duration;
import java.time.Instant;
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

import com.google.gson.JsonObject;

import ioc.liturgical.ws.constants.Constants;
import ioc.liturgical.ws.managers.databases.external.neo4j.ExternalDbManager;

import org.ocmc.ioc.liturgical.schemas.models.ws.response.ResultJsonObjectArray;
import org.ocmc.ioc.liturgical.utils.ErrorUtils;
import org.ocmc.ioc.liturgical.utils.Tuple;

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
	private List<Tuple> libraries = new ArrayList<Tuple>();
	private List<String> topicKeys = new ArrayList<String>();
	private String languageCodes = "";
	private boolean isDailyReading = false;
	private String readingYear = "";
	private String readingMonth = "";
	private Map<String,String> greekValues = new TreeMap<String,String>();
	private Map<String,String> englishValues = new TreeMap<String,String>();
	private Map<String,String> comboValues = new TreeMap<String,String>();
	
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
		this.setTuples();
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
		this.setTuples();
	}

	private void setTuples() {
		this.libraries.add(new Tuple(this.leftLibrary, this.leftFallback));
		if (this.centerLibrary != null && this.centerLibrary.length() > 0) {
			this.libraries.add(new Tuple(this.centerLibrary, this.centerFallback));
		}
		if (this.rightLibrary != null && this.rightLibrary.length() > 0) {
			this.libraries.add(new Tuple(this.rightLibrary, this.rightFallback));
		}

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
//		Instant start = Instant.now();
        for (Element valueSpan : versionDesignations) {
        	String id = valueSpan.select("span.key").attr("data-key");
        	String text = valueSpan.text();
    		valueSpan.addClass("kvp versiondesignation");
        	valueSpan.attr(
        				"data-key"
        				, id
        	); 
	        valueSpan.attr(
	    				"data-original"
	    				, id
	    	); 
    		valueSpan.children().remove();
    		valueSpan.text(text);
        }
//		System.out.println(Duration.between(start, Instant.now()) + " normalizeDesignations");

	}

	private void loadOriginalValues(
			Elements valueSpans
			) {
//		Instant start = Instant.now();
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
//		System.out.println(Duration.between(start, Instant.now()) + " loadOriginalValues");
	}
	
	private void loadOriginalValues2(
			Elements valueSpans
			, LDOM ldom
			) {
		try {

        	IdManager idManager = null;
        	String domain = "gr_gr_cog";
        	String domainAges = "gr_gr_ages";
	        for (Element valueSpan : valueSpans) {
	        	String dataKey = "";
	        	if (valueSpan.hasClass("key")) {
		        	dataKey = valueSpan.select("span.key").attr("data-key");
	        	} else {
		        	dataKey = valueSpan.attr("data-key");
	        	}
	        	idManager = this.dataKeyToIdManager(dataKey);
	        	domain = idManager.getLibrary();
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
    			if (idManager.getKey() != null && idManager.getKey().length() > 0) {
    				if (idManager.getKey().endsWith(".commemoration.text")) {
    					valueSpan.parent().removeClass("reading");
    					valueSpan.parent().addClass("designation");
    				}
    			}
	        	if (idManager.getLibrary().startsWith("gr")) {
	        		domainAges = "gr_gr_ages";
	        	} else {
	        		domainAges = "en_us_ages";
	        	}	
	        	idManager.setLibrary(domainAges);

	        	boolean addValue = false;
	        	if (value.length() == 0) {
	        		if (ldom.values.containsKey(idManager.getId())) {
	        			// ignore it
	        		} else {
	        			addValue = true;
	        		}
	        	} else {
	        		addValue = true;
	        	}
	        	if (addValue) {
        			ldom.addValue(idManager.getId(), value, false);
	    			if (idManager.getTopic().equals("calendar")) {
						ldom.addValue(idManager.getId() + ".doc", value, false);
						ldom.addValue(idManager.getId() + ".toc", value, false);
						ldom.addValue(idManager.getId() + ".header", value, false);
	    			}
	        	}
	        	// Set the span's data-keys to the normalized format for an ID
	        	if (valueSpan.hasClass("key")) {
		        	valueSpan.select("span.key").attr("data-key", idManager.getId());
	        	} else {
		        	valueSpan.attr("data-key", idManager.getId());
	        	}
	        	idManager.setLibrary(domain); // this is the original
	        	if (valueSpan.hasClass("key")) {
		        	valueSpan.select("span.key").attr("data-original", idManager.getId());
	        	} else {
		        	valueSpan.attr("data-original", idManager.getId());
	        	}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	private void equalizeTopicKeys() {
//		Instant start = Instant.now();
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
//		System.out.println(Duration.between(start, Instant.now()) + " equalizeTopicKeys");

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
//		Instant start = Instant.now();
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
//		System.out.println(Duration.between(start, Instant.now()) + " getValues");
		return result;
	}

	public LDOM getValues2(Elements valueSpans, LDOM ldom) throws Exception {
		// now add the fallbacks
		try {
	        for (Element valueSpan : valueSpans) {
	        	String tdClass = this.getClassOfTd(valueSpan);
	        	String dataKey = valueSpan.attr("data-original");
	        	IdManager idManager = new IdManager(dataKey);
	        	String originalDomain = idManager.getLibrary();
	        	dataKey = valueSpan.attr("data-key");
	        	idManager = new IdManager(dataKey);
	        	String domain = idManager.getLibrary();
	        	String fallbackDomain = "";
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
	        			&& fallbackDomain.length() > 0
	        			) {
		        	idManager.setLibrary(domain);
		        	String value = "";

		        	if (! idManager.getKey().equals("version.designation")) {
		        		if (idManager.getTopic().equals("calendar")) {
			        		value = this.getValue(idManager.getId());
			        		if (value == null || value.length() == 0) {
			        			// try the fallback library using the in-memory values
			        			idManager.setLibrary(fallbackDomain);
				        		value = ldom.values.get(idManager.getId());
			        		}
		        		} else {
			        		value = ldom.values.get(idManager.getId());
			        		if (value == null || value.length() == 0) {
			        			// do a database lookup
				        		value = this.getValue(idManager.getId());
				        		if (value == null || value.length() == 0) {
				        			// try the fallback library using the in-memory values
				        			idManager.setLibrary(fallbackDomain);
					        		value = ldom.values.get(idManager.getId());
				        		}
			        		}
		        		}
		        	}
		        	if (value == null || value.startsWith("[saint") || value.startsWith("[para") || value.startsWith("~")) {
		        		value = "";
		        	}
		        		
		        	String topicKey = idManager.getTopicKey();
		        	idManager.setLibrary(domain);
		        	ldom.addDomain(domain);
		        	ldom.addTopicKey(topicKey);
		        	ldom.addValue(idManager.getId(), value, false);
		        	if (idManager.getTopic().equals("calendar")) {
		        		IdManager temp = new IdManager(idManager.getLibrary(), idManager.getTopic(), idManager.getKey() + ".doc");
			        	ldom.addValue(temp.getId(), value, false);
		        		temp = new IdManager(idManager.getLibrary(), idManager.getTopic(), idManager.getKey() + ".toc");
			        	ldom.addValue(temp.getId(), value, false);
		        		temp = new IdManager(idManager.getLibrary(), idManager.getTopic(), idManager.getKey() + ".header");
			        	ldom.addValue(temp.getId(), value, false);
		        	}
	        		valueSpan.attr(
	        				"data-key"
	        				, idManager.getId()
	        		); 
	        		idManager.setLibrary(originalDomain);
	        		valueSpan.attr(
	        				"data-original"
	        				, idManager.getId()
	        		); 
	        		if (value.length() > 0) {
	        			valueSpan.text("text");
	        		}
	        	}
	        }
		} catch (Exception e) {
			throw e;
		}
		return ldom;
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
		try {
			for (Element child : children) {
				try {
					LDOM_Element eChild = new LDOM_Element(true);
					String key = "";
					eChild.setTag(child.tagName());
					if (child.hasAttr("class")) {
						eChild.setClassName(child.attr("class"));
					}
					if (child.hasAttr("data-key")) {
						if (child.parent().hasAttr("class")) {
							eChild.setParentClassName(child.parent().attr("class"));
						}
			        	String dataKey = child.attr("data-key");
			        	IdManager idManager = new IdManager(dataKey);
						eChild.setDataKey(idManager.getId());
						eChild.setTopicKey(idManager.getTopicKey());
			        	if (key.equals("version.designation")) {
			        		child.addClass("versiondesignation");
			        		eChild.setClassName(child.className());
			        	}
					}
					if (child.hasAttr("data-original")) {
			        	String dataOriginal = child.attr("data-original");
			        	IdManager idManager = new IdManager(dataOriginal);
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
	
	private void setLectionaryDates(Elements boldredSpans) {
		// See if this is a lectionary.  If so, we need to handle the day titles.
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
	}
	
	private void loadComboValues(Elements valueSpans) {
    	IdManager idManagerOriginal = null;
    	IdManager idManagerNormalized = null;
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
			/**
			 * Convert the IDs to our format and set both the original
			 * and the normalized in the span.
			 */
			idManagerOriginal = new IdManager(domain, topic, key);

			if (domain.startsWith("gr")) {
	        	idManagerNormalized = new IdManager("gr_gr_ages", topic, key);
        	} else {
	        	idManagerNormalized = new IdManager("en_us_ages", topic, key);
        	}
        	if (valueSpan.hasClass("key")) {
	        	valueSpan.select("span.key").attr("data-key", idManagerNormalized.getId());
	        	valueSpan.select("span.key").attr("data-original", idManagerOriginal.getId());
        	} else {
	        	valueSpan.attr("data-key", idManagerNormalized.getId());
	        	valueSpan.attr("data-original", idManagerOriginal.getId());
        	}
			
        	/**
        	 * Populate the values map with the values we can obtain from the html content.
        	 */
        	if (value.length() == 0) {
        		if (this.comboValues.containsKey(idManagerNormalized.getId())) {
        			// ignore it
        		} else {
	        		this.comboValues.put(idManagerNormalized.getId(), value);
        		}
        	} else {
        		this.comboValues.put(idManagerNormalized.getId(), value);
        	}
        	/**
        	 * Add the topic-key to the topicKeys map
        	 */
			if (topic != null && topic.length() > 0 && key != null && key.length() > 0) {
				String topicKey = new StringBuffer().append("~").append(topic).append("~").append(key).toString();
				if (! this.topicKeys.contains(topicKey)) {
					this.topicKeys.add(topicKey);
				}
			}
		}
        
        /**
         * Use the topicKeys maps to get values for the libraries the user requested.
         * This step sometimes results in calls to the database.  So, you need to 
         * improve the time to process, this might be a good place to start with.
         * Database calls are the things that cause the most delays in processing.
         */
        for (String topicKey : this.topicKeys) {
        	for (Tuple tuple : this.libraries) {
        		String primaryId = tuple.getLeft() + topicKey;
        		String fallbackId = tuple.getRight() + topicKey;
				boolean entryContainsCalendar = topicKey.contains("~calendar~");
				String value = this.comboValues.get(primaryId);

        		if (this.comboValues.containsKey(primaryId) && ! entryContainsCalendar) {
        			
        			// ignore
        		} else {
        			if (this.comboValues.containsKey(fallbackId)) {
        				if (entryContainsCalendar) {
              				value = this.getValue(primaryId);
              				if (value == null || value.length() == 0) {
              					value = this.getValue(fallbackId);
              				}
        				} else {
            				value = this.comboValues.get(fallbackId);
        				}
        			} else {
          				value = this.getValue(primaryId);
          				if (value == null || value.length() == 0) {
          					value = this.getValue(fallbackId);
          				}
        			}
        			if (value == null) {
        				value = "";
        			}
    				this.comboValues.put(primaryId, value);
    				if (entryContainsCalendar) {
						// the .doc, .toc, and .header are used in OSLW (Latex) for the PDFs.
    					this.comboValues.put(primaryId + ".doc", value);
    					this.comboValues.put(primaryId + ".toc", value);
    					this.comboValues.put(primaryId + ".header", value);
    				}
        		}
        	}
        }
	}
	
	/**
	 * The processing steps are:
	 * 
	 * 1. Load the content from the selected web page,
	 * 2. Do some preliminary setup.
	 * 3. Load all the key-value pairs to a map.
	 *          The values will be obtained either from the html
	 *           or via a database lookup.
	 *           This is the most time consuming part of the process.
	 * 4. Remove empty rows.
	 *      We want to determine whether any rows are empty of text
	 *      and need to be deleted.
	 * 5. Build a hierarchical metadata representation
	 * 
	 * @return an LDOM that contains all information needed to generate an HTML page or PDF doc.
	 * @throws Exception
	 */
	public LDOM toLDOM2 () throws Exception {
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
			/**
			 * Major Step: Load the content of the web page 
			 */
			
			if (url.contains("resources/theophany")) {
				File in = new File(url);
				doc = Jsoup.parse(in, "UTF-8", "http://example.com/");
			} else {
				c = Jsoup.connect(url);
				doc = c.timeout(60*1000).maxBodySize(0).get();
			}
			AlwbUrl urlUtils = new AlwbUrl(url);

			/**
			 * Major Step:  Do some preliminary setup.
			 */
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
					
			// remove rows that contain a media-group
			doc.select("tr:has(div.media-group)").remove();
			doc.select("tr:has(div.media-group-empty)").remove();
			content = doc.select("div.content").first();
			Elements boldredSpans = content.select("span.boldred");
			this.setLectionaryDates(boldredSpans);
			Elements versionDesignations = content.select("span.versiondesignation");
			this.normalizeDesignations(versionDesignations);
			
			/**
			 * Major Step:  Load original key-value pairs from html content 
			 */
			Instant start = Instant.now();
			this.loadOriginalValues2(content.select("span.kvp, span.key"), result);
			System.out.println(Duration.between(start, Instant.now()) + " loadOriginalValues2");

			/**
			 * Major Step:  Add or remove columns as needed
			 */
			if (this.centerLibrary.length() > 0) { // add the center column and get the keys again so they have the new column
				this.cloneRightColumn(content);
			}
			if (this.rightLibrary == null || this.rightLibrary.length() == 0) {
				content.select("td.rightCell").remove();
			}
			/**
			 * Major Step:  Load missing key-value pairs from database
			 */
			start = Instant.now();
			this.getValues2(content.select("span.kvp, span.key"), result);
			System.out.println(Duration.between(start, Instant.now()) + " getValues2");

			/**
			 * Step 4. Remove empty rows
			 */
			
			/**
			 * Step 5. Build the hierarchical metadata representation of the content.
			 */
			
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
			start = Instant.now();
			eContent.setChildren(this.getChildren(content.children(), 0));
			System.out.println(Duration.between(start, Instant.now()) + " eContent setChildren");

			result.setTopElement(eContent);
			
		} catch (Exception e) {
			throw(e);
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
//			Instant start = Instant.now();
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
//			System.out.println(Duration.between(start, Instant.now()) + " toLdom boldred spans");
			Elements versionDesignations = content.select("span.versiondesignation");
			this.normalizeDesignations(versionDesignations);
			Elements keys = content.select("span.kvp, span.key");
			this.loadOriginalValues(keys); // load the Greek and English values
			this.equalizeTopicKeys(); // sometimes the Greek or English has extra topic-keys.  Make sure they both have the same ones.
			
			if (this.centerLibrary.length() > 0) { // add the center column and get the keys again so they have the new column
				this.cloneRightColumn(content);
				keys = content.select("span.kvp, span,key");
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
	//		start = Instant.now();
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
	//		System.out.println(Duration.between(start, Instant.now()) + " convert leftCell to cellOneOfX");

	//		start = Instant.now();
			content.select("span.kvp").forEach(e -> {
				e.addClass("readonly");
				if (e.hasClass("versiondesignation")) {
					Element sibling = e.firstElementSibling();
					if (! sibling.hasText()) {
						e.addClass("deleteThis");
					}
				}
//				this.setSynaxarionValue(e);
				this.setValue(e);
			}
			);
//			System.out.println(Duration.between(start, Instant.now()) + " set span.kvp to readonly and mark empty designation for delete");

//			start = Instant.now();
			content.select("p.chapverse").forEach(e -> {
				Element tr = e.parent().parent();
				Element followingTr = tr.nextElementSibling();
				if (followingTr.text().trim().length() == 0) {
					tr.addClass("deleteThis");
				}
			});
//			System.out.println(Duration.between(start, Instant.now()) + " forEach p.chapverse");

//			start = Instant.now();
			content.select("p.break").forEach(e -> {
				Element tr = e.parent().parent();
				tr.addClass("deleteThis");
			});
//			System.out.println(Duration.between(start, Instant.now()) + " forEach p.break");

//			start = Instant.now();
			content.select("tr").forEach(e -> {
				if (e.text().trim().length() == 0) {
					e.addClass("deleteThis");
				}
			});
//			System.out.println(Duration.between(start, Instant.now()) + " mark empty tr for delete");

//			start = Instant.now();
			content.select("tr.deleteThis").remove();
//			System.out.println(Duration.between(start, Instant.now()) + " select remove tr.deleteThis");

//			start = Instant.now();
			content.select("span.deleteThis").remove();
//			System.out.println(Duration.between(start, Instant.now()) + " select remove span.deleteThis");


			LDOM_Element eContent = new LDOM_Element(printPretty);
			eContent.setTag(content.tagName());
			eContent.setClassName(content.attr("class"));
			if (content.parent().hasAttr("class")) {
				eContent.setParentClassName(content.parent().attr("class"));
			}
//			start = Instant.now();
			eContent.setChildren(this.getChildren(content.children(), 0));
//			System.out.println(Duration.between(start, Instant.now()) + " eContent setChildren");

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

//    	if (e.parent().hasClass("LectionaryDate")) {
//			value = e.text();
//    	} else {
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
//    	}
		e.text(value);
	}
	
	private void setSynaxarionValue(Element e) {
    	String value = e.text();
    	if (e.parent().hasClass("LectionaryDate")) {
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
    		e.text(value);
    	}
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
	private void cloneRightColumn(Element content) {
//		Instant start = Instant.now();
		for (Element cell : content.select("td.rightCell")) {
			Element clone = cell.clone();
			clone.attr("class","centerCell");
			cell.before(clone);
		}
//		System.out.println(Duration.between(start, Instant.now()) + " cloneRightColumn");
	}
}
