package ioc.liturgical.ws.managers.databases.external.neo4j;

public class CypherQueryBuilder {
	private String MATCH = "";
	private String LABEL = "";
	private String WHERE = "";
	private String STARTS_WITH = "";
	private String ENDS_WITH = "";
	private String MATCHES_PATTERN = "";
	private String CONTAINS = "";
	private String RETURN = "";
	private String ORDER_BY = "";

	public CypherQueryBuilder(){};
	
	public CypherQueryBuilder MATCH() {
		return this;
	}
	
    public CypherQueryBuilder LABEL(String LABEL) {
    	if (LABEL.startsWith("*") || LABEL.toLowerCase().startsWith("all") || LABEL.length() == 0) {
    		// ignore
    	} else {
    		String[] parts = LABEL.split("\\.");
    		for (String part : parts) {
            	if (this.LABEL.length() > 0) {
                    this.LABEL = this.LABEL + ":" + part;
            	} else {
                    this.LABEL = ":" + part;
            	}
    		}
    	}
        return this;
    }
    
    public CypherQueryBuilder WHERE(String WHERE) {
        this.WHERE = WHERE;
        return this;
    }

    public CypherQueryBuilder CONTAINS(String CONTAINS) {
        this.CONTAINS = CONTAINS;
        return this;
    }

    public CypherQueryBuilder STARTS_WITH(String STARTS_WITH) {
        this.STARTS_WITH = STARTS_WITH;
        return this;
    }

    public CypherQueryBuilder ENDS_WITH(String ENDS_WITH) {
        this.ENDS_WITH = ENDS_WITH;
        return this;
    }

    public CypherQueryBuilder MATCHES_PATTERN(String MATCHES_PATTERN) {
        this.MATCHES_PATTERN = MATCHES_PATTERN;
        return this;
    }


    public CypherQueryBuilder RETURN(String RETURN) {
        this.RETURN = RETURN;
        return this;
    }
    
    public CypherQueryBuilder ORDER_BY(String ORDER_BY) {
        this.ORDER_BY = ORDER_BY;
        return this;
    }
    
    public CypherQuery build() {
        return new CypherQuery(MATCH, LABEL, WHERE, CONTAINS, STARTS_WITH, ENDS_WITH, MATCHES_PATTERN, RETURN, ORDER_BY);
    }
}