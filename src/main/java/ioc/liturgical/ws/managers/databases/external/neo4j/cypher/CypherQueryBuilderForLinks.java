package ioc.liturgical.ws.managers.databases.external.neo4j.cypher;

public class CypherQueryBuilderForLinks {
	private String MATCH = "";
	private String TYPE = "";
	private String LIBRARY = "";
	private String WHERE = "";
	private String EQUALS = "";
	private String STARTS_WITH = "";
	private String ENDS_WITH = "";
	private String MATCHES_PATTERN = "";
	private String CONTAINS = "";
	private String TAGS = "";
	private String TAG_OPERATOR = "or";
	private String RETURN = "";
	private String ORDER_BY = "";
    private boolean prefixProperties = true;
    
	public CypherQueryBuilderForLinks(){};
	
	public CypherQueryBuilderForLinks(boolean prefixProperties) { 
		this.prefixProperties = prefixProperties;
	}

	public CypherQueryBuilderForLinks MATCH() {
		return this;
	}
	
    public CypherQueryBuilderForLinks TYPE(String TYPE) {
    	if (TYPE.startsWith("*") || TYPE.toLowerCase().startsWith("all") || TYPE.length() == 0) {
    		// ignore
    	} else {
          this.TYPE = ":" + TYPE;
    	}
        return this;
    }
    
    public CypherQueryBuilderForLinks LIBRARY(String LIBRARY) {
    	if (LIBRARY.startsWith("*") || LIBRARY.toLowerCase().startsWith("all") || LIBRARY.length() == 0) {
    		// ignore
    	} else {
          this.LIBRARY = " link.library = \'" + LIBRARY + "\'";
    	}
        return this;
    }
    
    public CypherQueryBuilderForLinks TAGS(String TAGS) {
    	if (TAGS.startsWith("*") || TAGS.toLowerCase().startsWith("all") || TAGS.length() == 0) {
    		// ignore
    	} else {
    		this.TAGS = TAGS;
    	}
        return this;
    }

    public CypherQueryBuilderForLinks TAG_OPERATOR(String operator) {
    	this.TAG_OPERATOR = operator;
        return this;
    }

    public CypherQueryBuilderForLinks WHERE(String WHERE) {
    	if (WHERE.startsWith("*") || WHERE.toLowerCase().startsWith("all") || WHERE.length() == 0) {
    		// ignore
    	} else {
            this.WHERE = WHERE;
    	}
        return this;
    }

    public CypherQueryBuilderForLinks CONTAINS(String CONTAINS) {
    	if (CONTAINS.startsWith("*") || CONTAINS.toLowerCase().startsWith("all") || CONTAINS.length() == 0) {
    		// ignore
    	} else {
            this.CONTAINS = CONTAINS;
    	}
        return this;
    }

    public CypherQueryBuilderForLinks STARTS_WITH(String STARTS_WITH) {
        this.STARTS_WITH = STARTS_WITH;
        return this;
    }

    public CypherQueryBuilderForLinks EQUALS(String EQUALS) {
    	if (EQUALS.startsWith("*") || EQUALS.toLowerCase().startsWith("all") || EQUALS.length() == 0) {
    		// ignore
    	} else {
            this.EQUALS = EQUALS;
    	}
        return this;
    }

    public CypherQueryBuilderForLinks ENDS_WITH(String ENDS_WITH) {
    	if (ENDS_WITH.startsWith("*") || ENDS_WITH.toLowerCase().startsWith("all") || ENDS_WITH.length() == 0) {
    		// ignore
    	} else {
            this.ENDS_WITH = ENDS_WITH;
    	}
        return this;
    }

    public CypherQueryBuilderForLinks MATCHES_PATTERN(String MATCHES_PATTERN) {
        this.MATCHES_PATTERN = MATCHES_PATTERN;
        return this;
    }

    public CypherQueryBuilderForLinks RETURN(String RETURN) {
        this.RETURN = RETURN;
        return this;
    }
    
    public CypherQueryBuilderForLinks ORDER_BY(String ORDER_BY) {
        this.ORDER_BY = ORDER_BY;
        return this;
    }
    
    public CypherQueryForLinks build() {
        return new CypherQueryForLinks(
        		MATCH
        		, TYPE
        		, LIBRARY
        		, WHERE
        		, CONTAINS
        		, EQUALS
        		, STARTS_WITH
        		, ENDS_WITH
        		, MATCHES_PATTERN
        		, TAGS
        		, TAG_OPERATOR
        		, RETURN
        		, ORDER_BY
        		);
    }
}