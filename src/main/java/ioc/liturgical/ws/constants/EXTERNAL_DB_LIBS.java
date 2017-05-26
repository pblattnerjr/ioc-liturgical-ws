package ioc.liturgical.ws.constants;

/**
 * Enum for external database libraries
 *
 * Note that most docs stored in the external database use a domain name
 * as the library, so they do not show up here.
 * 
 * The ones here are not specific to a domain.
 * 
 * @author mac002
 *
 */
public enum EXTERNAL_DB_LIBS {
	LINGUISTICS("linguistics", "Library for linguistic information about the texts in the database.")
	, ONTOLOGY("ontology", "Library for entries in the database ontology.")
	;

	public String libname = "";
	public String description = "";
	
	private EXTERNAL_DB_LIBS(
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
	public static EXTERNAL_DB_LIBS enumForName(String name) {
		for (EXTERNAL_DB_LIBS l : EXTERNAL_DB_LIBS.values()) {
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

	public String toId(String key) {
		if (key == null || key.length() == 0) {
			return this.libname;
		} else {
			return this.libname + Constants.ID_DELIMITER + key;
		}
	}
}
