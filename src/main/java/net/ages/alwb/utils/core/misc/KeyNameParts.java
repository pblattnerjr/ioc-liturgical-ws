package net.ages.alwb.utils.core.misc;

import org.ocmc.ioc.liturgical.utils.ErrorUtils;
import org.ocmc.ioc.liturgical.schemas.constants.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Example of Ares Key: template.titles_gr_GR_cog.d.onSundayEvening
 * Example of CouchDBKey: template.titles|d.onSundayEvening"
 * @author mac002
 *
 */
public class KeyNameParts {
    private static final Logger logger = LoggerFactory.getLogger(KeyNameParts.class);
	private String domain;
	private String topic;
	private String key;
	private String templateSeparator = "__";
	/**
	 * Javascript does not allow a period in a name unless it is separating
	 * an object and a method.  Because ares names allow periods, we need
	 * an alternative to the period--one that allows us to recover the period when
	 * it is needed.
	 */
	private String altPeriod = "$"; 
	
	public String getCouchDbKey() {
		return topic + Constants.ID_DELIMITER + key;
	}
	
	public String getAresKey() {
		return topic + "_" + domain + "." + key;
	}

	public void parseCouchDbDomainAndKey(String domainAndKey) {
		try {
			String [] parts = domainAndKey.split(Constants.ID_SPLITTER);
			if (parts.length == 2) {
				this.key = parts[1];
				String [] domainParts = parts[0].split("_");
				if (domainParts.length == 4) {
					this.topic = domainParts[0];
					this.domain = domainParts[1] + "_" + domainParts[2] + "_" + domainParts[3];
				}
			}
			parseCouchDbKey(domain, parts[1]);
		} catch (Exception e) {
			ErrorUtils.report(logger, e);
		}
	}
	
	public void parseCouchDbKey(String domain, String key) {
		this.domain = domain;
		try {
			String[] parts = key.split(Constants.ID_SPLITTER);
			if (parts.length == 2) {
				this.topic = parts[0];
				this.key = parts[1];
			}
		} catch (Exception e) {
			logger.error(e.getMessage() + " using " + Constants.ID_DELIMITER + " to split "+ key);
		}
	}
	public  void parseAresKey(String key) {
		try {
			String[] parts = key.split("_");
			if (parts.length == 4) { // all ok
				topic = parts[0].trim();
				domain = parts[1].trim() + "_" + parts[2].trim() +"_";
				String[] subparts = parts[3].trim().split("\\.");
				domain = domain + subparts[0].trim();
				this.key = parts[3].replaceFirst(subparts[0]+"\\.", "").trim();
			}
		} catch (Exception e) {
			ErrorUtils.report(logger, e, " splitting " + key);
		}
	}
	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getAsTemplateKey() {
		return altPeriod(topic) + templateSeparator + altPeriod(key); 
	}
	
	private String altPeriod(String key) {
		return key.replace(".", altPeriod);
	}
	
	public String getAltPeriod() {
		return altPeriod;
	}
	
	public String getTemplateSeparator() {
		return templateSeparator;
	}

}
