package net.ages.alwb.utils.oslw;

import ioc.liturgical.ws.constants.Constants;
import net.ages.alwb.utils.core.id.managers.IdManager;

public class OslwUtils {
	
	/**
	 * OSLW \ltChapter, \ltSection, etc., expects to find
	 * three resources for each title:
	 * 1. The .doc version - appears in the text body
	 * 2. The .toc version - appears in the table of contents
	 * 3. The .header version - appears in the header
	 * 
	 * This method returns title resources for the specified domain and
	 * the values for doc, toc, and header.
	 * @param domain
	 * @param docValue
	 * @param tocValue
	 * @param headerValue
	 * @return
	 */
	public static String getOslwTitleResources(
			String domain
			, String docValue
			, String tocValue
			, String headerValue
			, String date
			) {
		StringBuffer result = new StringBuffer();
		String paddedDate = "";
		if (date != null && date.length() > 0) {
			paddedDate = " - " + date;
		}
		if (domain != null && domain.length() > 0) {
			result.append(getOslwTitleResource(domain, "doc", docValue));
			result.append(getOslwTitleResource(domain, "toc", tocValue + paddedDate));
			result.append(getOslwTitleResource(domain, "header", headerValue + paddedDate));
			result.append(getOslwTitleResource(domain, "date", date));
		}
		return result.toString();
	}
	
	/**
	 * Returns a title resource for the specified domain, key, and value.
	 * @param domain
	 * @param key should be one of doc, toc, or header
	 * @param value
	 * @return
	 */
	public static String getOslwTitleResource(
			String domain
			, String key
			, String value
			) {
		StringBuffer result = new StringBuffer();
		IdManager idManager = new IdManager(
				domain 
				+ Constants.ID_DELIMITER
				+ "pdf"
				+ Constants.ID_DELIMITER
				+ "title." + key
				);
		result.append(idManager.getOslwResourceForValue(value));
		return result.toString();
	}
}
