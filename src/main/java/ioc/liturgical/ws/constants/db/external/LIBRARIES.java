package ioc.liturgical.ws.constants.db.external;

import ioc.liturgical.ws.constants.Constants;

/**
 * Enum for external database libraries
 * 
 * Each entry must have a corresponding EXTERNAL_DB_X_TOPICS enum, e.g. 
 * ONTOLOGY has EXTERNAL_DB_ONTOLOGY_TOPICS
 * 
 * Note that most docs stored in the external database use a domain name
 * as the library, so they do not show up here.
 * 
 * The ones here are not specific to a domain.
 * 
 * @author mac002
 *
 */
public enum LIBRARIES {
	LINGUISTICS("linguistics", "Library for linguistic information about the texts in the database.")
	, ONTOLOGY("ontology", "Library for entries in the database ontology.")
	, TABLES("tables", "Json data stored in a format that can be used by a React js table component")
	, WORDNET("wordnet", "A lexical database developed by Princeton University.")
	;

	public String libname = "";
	public String description = "";
	
	private LIBRARIES(
			String keyname
			, String description
			) {
		this.libname = keyname;
		this.description = description;
	}
		
	/**
	 * Find the Role for this string
	 * @param pathname
	 * @return Libs
	 */
	public static LIBRARIES enumForName(String name) {
		for (LIBRARIES l : LIBRARIES.values()) {
			if (l.libname.equals(name)) {
				return l;
			}
		}
		return null;
	}
	
	public static boolean isSystemLib(String name) {
		return (enumForName(name) != null);
	}

	/**
	 * Returns a domain based on the enum value
	 * @return e.g. en_sys_ontology
	 */
	public String toSystemDomain() {
		return "en" + Constants.DOMAIN_DELIMITER + "sys" + Constants.DOMAIN_DELIMITER + this.libname;
	}

}
