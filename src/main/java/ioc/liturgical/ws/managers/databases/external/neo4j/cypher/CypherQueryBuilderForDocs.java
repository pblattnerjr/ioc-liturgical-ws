package ioc.liturgical.ws.managers.databases.external.neo4j.cypher;

public class CypherQueryBuilderForDocs {
	private String MATCH = "";
	private String LABEL = "";
	private String WHERE = "";
	private String EQUALS = "";
	private String STARTS_WITH = "";
	private String ENDS_WITH = "";
	private String MATCHES_PATTERN = "";
	private String CONTAINS = "";
	private String RETURN = "";
	private String ORDER_BY = "";
    private boolean prefixProperties = true;
    
	public CypherQueryBuilderForDocs(){};
	
	public CypherQueryBuilderForDocs(boolean prefixProperties) { 
		this.prefixProperties = prefixProperties;
	}

	public CypherQueryBuilderForDocs MATCH() {
		return this;
	}
	
    public CypherQueryBuilderForDocs LABEL(String LABEL) {
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
    
    public CypherQueryBuilderForDocs WHERE(String WHERE) {
        this.WHERE = WHERE;
        return this;
    }

    public CypherQueryBuilderForDocs CONTAINS(String CONTAINS) {
        this.CONTAINS = CONTAINS;
        return this;
    }

    public CypherQueryBuilderForDocs STARTS_WITH(String STARTS_WITH) {
        this.STARTS_WITH = STARTS_WITH;
        return this;
    }

    public CypherQueryBuilderForDocs EQUALS(String EQUALS) {
        this.EQUALS = EQUALS;
        return this;
    }

    public CypherQueryBuilderForDocs ENDS_WITH(String ENDS_WITH) {
        this.ENDS_WITH = ENDS_WITH;
        return this;
    }

    public CypherQueryBuilderForDocs MATCHES_PATTERN(String MATCHES_PATTERN) {
        this.MATCHES_PATTERN = MATCHES_PATTERN;
        return this;
    }


    public CypherQueryBuilderForDocs RETURN(String RETURN) {
        this.RETURN = RETURN;
        return this;
    }
    
    public CypherQueryBuilderForDocs ORDER_BY(String ORDER_BY) {
        this.ORDER_BY = ORDER_BY;
        return this;
    }
    
    public CypherQueryForDocs build() {
        return new CypherQueryForDocs(
        		MATCH
        		, LABEL
        		, WHERE
        		, CONTAINS
        		, EQUALS
        		, STARTS_WITH
        		, ENDS_WITH
        		, MATCHES_PATTERN
        		, RETURN
        		, ORDER_BY
        		, prefixProperties
        		);
    }
}