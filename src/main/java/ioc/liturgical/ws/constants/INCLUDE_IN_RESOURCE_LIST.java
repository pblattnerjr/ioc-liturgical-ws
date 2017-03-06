package ioc.liturgical.ws.constants;

public enum INCLUDE_IN_RESOURCE_LIST {
	YES(true)
	, NO(false)
	;
	public boolean value;
	
	private INCLUDE_IN_RESOURCE_LIST(boolean value) {
		this.value = value;
	}
	
}
