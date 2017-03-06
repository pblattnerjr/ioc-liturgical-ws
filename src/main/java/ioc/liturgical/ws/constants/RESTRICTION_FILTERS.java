package ioc.liturgical.ws.constants;

public enum RESTRICTION_FILTERS {
	WS_ADMIN("web service administrator")
	, ALL_DOMAINS_ADMIN("all-domains administrator")
	, DOMAIN_ADMIN("domain administrator")
	, NONE("unrestricted")
	;

	public String keyname = "";
	
	private RESTRICTION_FILTERS(
			String keyname
			) {
		this.keyname = keyname;
	}
		
}
