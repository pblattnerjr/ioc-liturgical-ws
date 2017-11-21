package ioc.liturgical.ws.managers.databases.external.neo4j.utils;

public class ReturnProperty {
	private String handle = "";
	private String propName = "";
	private String asName = "";
	public enum TYPES {NA, PROPERTIES, TYPE};
	private TYPES type = TYPES.NA;
	
	public ReturnProperty(
			String handle
			, String propName
			, String asName
			) {
		this.handle = handle;
		this.propName = propName;
		this.asName = asName;
	}

	public ReturnProperty(
			String handle
			, TYPES type
			) {
		this.handle = handle;
		this.type = type;
	}
	public String getHandle() {
		return handle;
	}

	public void setHandle(String handle) {
		this.handle = handle;
	}

	public String getPropName() {
		return propName;
	}

	public void setPropName(String propName) {
		this.propName = propName;
	}

	public String getAsName() {
		return asName;
	}

	public void setAsName(String asName) {
		this.asName = asName;
	}
	
	public String toString() {
		switch (this.type) {
		case PROPERTIES:
			return "properties(" + this.handle + ")";
		case TYPE:
			return "type(" + this.handle + ") as type";
		default:
			return this.handle + "." + this.propName + " as " + this.asName;
		}
	}
}
