package ioc.liturgical.ws.constants;

import com.google.common.base.Joiner;

public enum ROLES {
	ADMIN(SYSTEM_LIBS.ADMINS.libname,"Administrator - authorized as Author and Reader for this library and can also create, read, update, delete users, and user authorizations.")
	, AUTHOR(SYSTEM_LIBS.AUTHORS.libname, "Author - authorized as a Reader and can create, read, update, and delete docs in this library.")
	, READER(SYSTEM_LIBS.READERS.libname, "Reader - can read docs in this library, but not create, update, or delete them.")
	, REVIEWER(SYSTEM_LIBS.REVIEWERS.libname, "Reviewer - can review docs in this library, but not create, update, or delete them.");
	;

	public String keyname = "";
	public String description = "";
	
	private ROLES(
			String keyname
			, String description
			) {
		this.keyname = keyname;
		this.description = description;
	}
		
	/**
	 * Find the Role for this string
	 * @param rolename
	 * @return Role
	 */
	public static ROLES forWsname(String rolename) {
		for (ROLES r : ROLES.values()) {
			if (r.keyname.equals(rolename)) {
				return r;
			}
		}
		return null;
	}
	
	/**
	 * Creates a delimited string id for this role, library, and user 
	 * @param library
	 * @param user
	 * @return
	 */
	public String toId(String library, String user) {
		return Joiner.on(Constants.ID_DELIMITER).join(this.keyname, library, user);
	}
}
