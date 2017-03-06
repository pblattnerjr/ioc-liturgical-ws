package ioc.liturgical.ws.constants;

/**
 * Enum for system libraries
 *
 * Note that some things need their own library|topic|key, e.g. Users|Contact|{username}.
 * In such cases, they have their own library in SYSTEM_LIBS.
 * 
 * Other things don't fit the library|topic|key paradigm on their own.  
 * They just need topic|key. 
 * In such cases, they use the MISC library.
 * 
 * @author mac002
 *
 */
public enum SYSTEM_LIBS {
	MISC("misc", "Library for miscellaneous topics and docs.")
	, ADMINS("admins", "Library used to record users allowed to administer a given library.")
	, AUTHORS("authors", "Library used to record users allowed to author docs in a given library")
	, READERS("readers", "Library used to record users allowed to read docs in a given library")
	, USERS("users", "Library for topics and docs pertaining to the users of the web service")
	;

	public String libname = "";
	public String description = "";
	
	private SYSTEM_LIBS(
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
	public static SYSTEM_LIBS enumForName(String name) {
		for (SYSTEM_LIBS l : SYSTEM_LIBS.values()) {
			if (l.libname.equals(name)) {
				return l;
			}
		}
		return null;
	}
	
	public static boolean isSystemLib(String name) {
		return (enumForName(name) != null);
	}
	
	public String toId(String key) {
		if (key == null || key.length() == 0) {
			return this.libname;
		} else {
			return this.libname + Constants.ID_DELIMITER + key;
		}
	}
}
