package ioc.liturgical.ws.forms.manager;

public class FormRegExConstants {
	
	public static final String NOSPACES = "^\\S*$";
	public static final String NOTEMPTY = "^.*(?=.{1,})";
	public static final String USERNAME = "^.*(?=.{3,})";
	public static final String USERNAME_DESCRIPTION = "username must be at least three characters.";
	public static final String EMAIL = "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$";
	public static final String PASSWORD_PATTERN = "^.*(?=.{10,})(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[\\W]).*$";
	public static final String PASSWORD_DESCRIPTION = "password must be at least 10 characters and contain at least one lowercase letter, one uppercase letter, one number, and one special character";
}
