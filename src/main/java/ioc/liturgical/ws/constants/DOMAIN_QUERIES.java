package ioc.liturgical.ws.constants;

/**
 * Enum for querying a Neo4j database for nodes that have domains
 *
 * @author mac002
 *
 */
public enum DOMAIN_QUERIES {
	BIBLICAL("Biblical", "match (n:Biblical) return distinct split(n.id,'~')[0]")
	, LITURGICAL("Liturgical", "match (n:Liturgical) return distinct split(n.id,'~')[0]")
	, TEXT("Text", "match (n:Text) return distinct split(n.id,'~')[0]")
	;

	public String domainType = "";
	public String query = "";
	
	private DOMAIN_QUERIES(
			String domainType
			, String query
			) {
		this.domainType = domainType;
		this.query = query;
	}
		
}
