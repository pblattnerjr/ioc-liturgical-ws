package net.ages.alwb.utils.transformers.adapters.models;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.google.gson.annotations.Expose;

import org.ocmc.ioc.liturgical.schemas.models.supers.AbstractModel;

/**
 * Takes a template and compiles it's tree by:
 * - applying Liturgical Day Properties if it is a dated service
 * - applying conditions if it is a dated service
 * - recursively inserting all requested templates and sections
 * - resolving all key redirectors (values that are another ID rather than a text)
 * 
 *  A complied template is like an LDOM, but lacks the text values.
 *  
 * @author mac002
 *
 */
public class AbstractLDOM extends AbstractModel {
	@Expose public String source = "";
	@Expose public String keyLibrary = "";
	@Expose public String serviceDate = "";
	@Expose public String pdfId = ""; 
	@Expose public String pdfFilename = ""; 
	@Expose public List<String> topicKeys = new ArrayList<String>();
	@Expose public LDOM_Element topElement = new LDOM_Element(true);
	
	public AbstractLDOM(String source) {
		super();
		this.source = source;
	}

	public AbstractLDOM(String source, boolean printPretty) {
		super();
		super.setPrettyPrint(printPretty);
		this.source = source;
	}


	public List<String> getTopicKeys() {
		return topicKeys;
	}

	public void setTopicKeys(List<String> topicKeys) {
		this.topicKeys = topicKeys;
	}

	public void addTopicKey(String topicKey) {
		if (! topicKeys.contains(topicKey)) {
			topicKeys.add(topicKey);
		}
	}
	
	public LDOM_Element getTopElement() {
		return topElement;
	}

	public void setTopElement(LDOM_Element topElement) {
		this.topElement = topElement;
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

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getKeyLibrary() {
		return keyLibrary;
	}

	public void setKeyLibrary(String keyLibrary) {
		this.keyLibrary = keyLibrary;
	}

	public String getServiceDate() {
		return serviceDate;
	}

	public void setServiceDate(String serviceDate) {
		this.serviceDate = serviceDate;
	}

	public void setPdfFilename(String pdfFilename) {
		this.pdfFilename = pdfFilename;
	}

}
