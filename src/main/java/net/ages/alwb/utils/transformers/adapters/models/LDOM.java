package net.ages.alwb.utils.transformers.adapters.models;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.google.gson.annotations.Expose;

import ioc.liturgical.ws.constants.Constants;

import org.ocmc.ioc.liturgical.schemas.models.supers.AbstractModel;

/**
 * Both AGES Liturgical Workbench (ALWB) and the OCMC ShareLatex Workbench (OSLW)
 * make use of templates to define the order and formatting of data for services and books.
 * 
 * This class is used to hold the meta data of a template from either source.
 * When returned from a REST call, it is populated with values read by
 * binding user specified libraries to the topic-keys in the template.
 * 
 * LDOM = Liturgical Document Object Model
 * 
 * @author mac002
 *
 */
public class LDOM extends AbstractLDOM {
	@Expose public String url = "";
	@Expose public String html = "";
	@Expose public String leftLibrary = "";
	@Expose public String leftLibraryFallback = "";
	@Expose public String centerLibrary = "";
	@Expose public String centerLibraryFallback = "";
	@Expose public String rightLibrary = "";
	@Expose public String rightLibraryFallback = "";
	@Expose public String leftTitle = "";
	@Expose public String centerTitle = "";
	@Expose public String rightTitle = "";
	@Expose public String leftHeaderTitle = "";
	@Expose public String centerHeaderTitle = "";
	@Expose public String rightHeaderTitle = "";
	@Expose public String leftTitleDate = "";
	@Expose public String centerTitleDate = "";
	@Expose public String rightTitleDate = "";
	@Expose public List<String> domains = new ArrayList<String>();
	@Expose public Map<String,String> values = Collections.synchronizedMap(new TreeMap<String,String>());
	
	public LDOM(String source) {
		super(source);
		this.url = source;
	}

	public LDOM(String source, boolean printPretty) {
		super(source);
		super.setPrettyPrint(printPretty);
		this.url = source;
	}

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public List<String> getTopicKeys() {
		return topicKeys;
	}

	public void setTopicKeys(List<String> topicKeys) {
		this.topicKeys = topicKeys;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Map<String, String> getValues() {
		return this.values;
	}

	public void setValues(Map<String, String> values) {
		this.values = values;
	}
	
	/**
	 * Add a value to the value map
	 * @param key
	 * @param value
	 * @param useKeyAsValue if the value is empty, use the key as the value
	 */
	public void addValue(String key, String value, boolean useKeyAsValue) {
		if (! this.values.containsKey(key)) {
			if (useKeyAsValue) {
				if (value.trim().length() == 0) {
					String [] parts = key.split(Constants.ID_SPLITTER);
					if (parts.length == 3) {
						value = parts[1] + Constants.ID_DELIMITER + parts[2];
					} else {
						value = key;
					}
				}
				if (value.contains("~")) {
					value = value.replaceAll("~", " ~ ");
				}
			}
			this.values.put(key, value);
		}
	}
	
	public void addDomain(String domain) {
		if (! domains.contains(domain)) {
			domains.add(domain);
		}
	}

	public void addTopicKey(String topicKey) {
		if (! topicKeys.contains(topicKey)) {
			topicKeys.add(topicKey);
		}
	}
	
	/**
	 * Get the set of IDs used in this template
	 * @return
	 */
	public Set<String> getIds() {
		return this.values.keySet();
	}

	public LDOM_Element getTopElement() {
		return topElement;
	}

	public void setTopElement(LDOM_Element topElement) {
		this.topElement = topElement;
	}

	public String getLeftLibrary() {
		return leftLibrary;
	}

	public void setLeftLibrary(String leftLibrary) {
		this.leftLibrary = leftLibrary;
	}

	public String getLeftLibraryFallback() {
		return leftLibraryFallback;
	}

	public void setLeftLibraryFallback(String leftLibraryFallback) {
		this.leftLibraryFallback = leftLibraryFallback;
	}

	public String getCenterLibrary() {
		return centerLibrary;
	}

	public void setCenterLibrary(String centerLibrary) {
		this.centerLibrary = centerLibrary;
	}

	public String getCenterLibraryFallback() {
		return centerLibraryFallback;
	}

	public void setCenterLibraryFallback(String centerLibraryFallback) {
		this.centerLibraryFallback = centerLibraryFallback;
	}

	public String getRightLibrary() {
		return rightLibrary;
	}

	public void setRightLibrary(String rightLibrary) {
		this.rightLibrary = rightLibrary;
	}

	public String getRightLibraryFallback() {
		return rightLibraryFallback;
	}

	public void setRightLibraryFallback(String rightLibraryFallback) {
		this.rightLibraryFallback = rightLibraryFallback;
	}
	
	public void setLibraries(
			String leftLibrary
			, String centerLibrary
			, String rightLibrary
			, String leftLibraryFallback
			, String centerLibraryFallback
			, String rightLibraryFallback
			) {
		this.leftLibrary = leftLibrary;
		this.leftLibraryFallback = leftLibraryFallback;
		this.centerLibrary = centerLibrary;
		this.centerLibraryFallback = centerLibraryFallback;
		this.rightLibrary = rightLibrary;
		this.rightLibraryFallback = rightLibraryFallback;
	}

	public String getLeftTitle() {
		return leftTitle;
	}

	public void setLeftTitle(String leftTitle) {
		this.leftTitle = leftTitle;
	}

	public String getCenterTitle() {
		return centerTitle;
	}

	public void setCenterTitle(String centerTitle) {
		this.centerTitle = centerTitle;
	}

	public String getRightTitle() {
		return rightTitle;
	}

	public void setRightTitle(String rightTitle) {
		this.rightTitle = rightTitle;
	}

	public String getLeftHeaderTitle() {
		return leftHeaderTitle;
	}

	public void setLeftHeaderTitle(String leftHeaderTitle) {
		this.leftHeaderTitle = leftHeaderTitle;
	}

	public String getCenterHeaderTitle() {
		return centerHeaderTitle;
	}

	public void setCenterHeaderTitle(String centerHeaderTitle) {
		this.centerHeaderTitle = centerHeaderTitle;
	}

	public String getRightHeaderTitle() {
		return rightHeaderTitle;
	}

	public void setRightHeaderTitle(String rightHeaderTitle) {
		this.rightHeaderTitle = rightHeaderTitle;
	}

	public String getServiceDate() {
		return serviceDate;
	}

	public void setServiceDate(String serviceDate) {
		this.serviceDate = serviceDate;
	}

	/**
	 * If this is a service, then this method returns a date
	 * formatted to the locale of the leftLibrary
	 * @return
	 */
	public String getLeftTitleDate() {
		return leftTitleDate;
	}

	public void setLeftTitleDate(String leftTitleDate) {
		this.leftTitleDate = leftTitleDate;
	}

	/**
	 * If this is a service, then this method returns a date
	 * formatted to the locale of the centerLibrary
	 * @return
	 */
	public String getCenterTitleDate() {
		return centerTitleDate;
	}

	public void setCenterTitleDate(String centerTitleDate) {
		this.centerTitleDate = centerTitleDate;
	}

	/**
	 * If this is a service, then this method returns a date
	 * formatted to the locale of the rightLibrary
	 * @return
	 */
	public String getRightTitleDate() {
		return rightTitleDate;
	}

	public void setRightTitleDate(String rightTitleDate) {
		this.rightTitleDate = rightTitleDate;
	}

	public List<String> getDomains() {
		return domains;
	}

	public void setDomains(List<String> domains) {
		this.domains = domains;
	}

	/**
	 * When a MetaTemplate is created, the caller
	 * can initiate the generation of a PDF.
	 * The pdfId holds an identifier
	 * for the PDF tex file.  This is can be set in the
	 * template so that a client can later on ask for
	 * this specific PDF file.
	 * @return
	 */
	public String getPdfId() {
		return pdfId;
	}

	/**
	 * When a MetaTemplate is created, the caller
	 * can initiate the generation of a PDF.
	 * The pdfId holds an identifier
	 * for the PDF tex file.  This is can be set in the
	 * template so that a client can later on ask for
	 * this specific PDF file.
	 * @param pdfId the id of a PDF file
	 */
	public void setPdfId(String pdfId) {
		this.pdfId = pdfId;
	}

	public String getPdfFilename() {
		return pdfFilename;
	}

	public void setPdfFilename(String pdfFilename, String languageCodes) {
		StringBuffer sb = new StringBuffer();
		sb.append(pdfFilename);
		sb.append(".");
		sb.append(languageCodes);
		sb.append(".pdf");
		this.pdfFilename = sb.toString();
	}

}
