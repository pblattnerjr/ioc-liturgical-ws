package ioc.liturgical.ws.managers.databases.external.neo4j.constants;


/**
 * @author mac002
 *
 */
public enum MATCHERS {
	STARTS_WITH("sw")
	, ENDS_WITH("ew")
	, CONTAINS("c")
	, REG_EX("rx")
	;

	public String label;
	private MATCHERS(
			String label
			) {
		this.label = label;
	}
	
	/**
	 * Find the enum for this string
	 * @param label
	 * 	 * @return Enum
	 */
	public static MATCHERS forLabel(String label) {
		for (MATCHERS r : MATCHERS.values()) {
			if (r.label.equals(label)) {
				return r;
			}
		}
		return null;
	}
}
