package ioc.liturgical.ws.managers.databases.external.neo4j.cypher;

/**
 *  Provides a means to build a query for searching notes.
 * There are four types of query builders:
 * docs - a query that only matches a node
 * notes - a query that matches two nodes and the relationship between them
 * links - a query that matches three nodes and relationships between them.  
 *             A link is a node that holds information about the relationship between
 *             two other nodes.  A link uses the REFERS_TO relationship type.
 * templates - a query that matches a node that is a Template or Section

 * @author mac002
 *
 */

public class CypherQueryBuilderForTemplates {
	private String MATCH = "";
	private String TYPE = "";
	private String EXCLUDE_TYPE;
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
	private String LABEL = "";
	private String REQUESTOR = "";
    private boolean prefixProperties = true;
    private boolean addWherePublic = true;
    
	public CypherQueryBuilderForTemplates(){};
	
	public CypherQueryBuilderForTemplates(
			boolean prefixProperties
			, boolean addWherePublic
			) { 
		this.prefixProperties = prefixProperties;
		this.addWherePublic = addWherePublic;
	}

	public CypherQueryBuilderForTemplates MATCH() {
		return this;
	}
	
    public CypherQueryBuilderForTemplates TYPE(String TYPE) {
    	if (TYPE.startsWith("*") || TYPE.toLowerCase().startsWith("all") || TYPE.length() == 0) {
    		// ignore
    	} else {
          this.TYPE = ":" + TYPE;
    	}
        return this;
    }
    
    public CypherQueryBuilderForTemplates EXCLUDE_TYPE(String TYPE) {
    	if (TYPE.length() > 0) {
          this.EXCLUDE_TYPE = TYPE;
    	}
        return this;
    }


    public CypherQueryBuilderForTemplates LABEL(String LABEL) {
    	if (LABEL.startsWith("*") || LABEL.toLowerCase().startsWith("all") || LABEL.length() == 0) {
    		// ignore
    	} else {
    		String[] parts = LABEL.split("\\.");
    		for (String part : parts) {
            	if (this.LABEL.length() > 0) {
                    this.LABEL = this.LABEL + ":" + part;
            	} else {
                    this.LABEL = part;
            	}
    		}
    	}
        return this;
    }

    public CypherQueryBuilderForTemplates LIBRARY(String LIBRARY) {
    	if (LIBRARY.startsWith("*") || LIBRARY.toLowerCase().startsWith("all") || LIBRARY.length() == 0) {
    		// ignore
    	} else {
          this.LIBRARY = " to.library = \'" + LIBRARY + "\'";
    	}
        return this;
    }
    
    public CypherQueryBuilderForTemplates TAGS(String TAGS) {
    	if (TAGS.startsWith("*") || TAGS.toLowerCase().startsWith("all") || TAGS.length() == 0) {
    		// ignore
    	} else {
    		this.TAGS = TAGS;
    	}
        return this;
    }

    public CypherQueryBuilderForTemplates TAG_OPERATOR(String operator) {
    	this.TAG_OPERATOR = operator;
        return this;
    }

    public CypherQueryBuilderForTemplates WHERE(String WHERE) {
    	if (WHERE.startsWith("*") || WHERE.toLowerCase().startsWith("all") || WHERE.length() == 0) {
    		// ignore
    	} else {
            this.WHERE = WHERE;
    	}
        return this;
    }

    public CypherQueryBuilderForTemplates CONTAINS(String CONTAINS) {
    	if (CONTAINS.startsWith("*") || CONTAINS.toLowerCase().startsWith("all") || CONTAINS.length() == 0) {
    		// ignore
    	} else {
            this.CONTAINS = CONTAINS;
    	}
        return this;
    }

    public CypherQueryBuilderForTemplates STARTS_WITH(String STARTS_WITH) {
        this.STARTS_WITH = STARTS_WITH;
        return this;
    }

    public CypherQueryBuilderForTemplates EQUALS(String EQUALS) {
    	if (EQUALS.startsWith("*") || EQUALS.toLowerCase().startsWith("all") || EQUALS.length() == 0) {
    		// ignore
    	} else {
            this.EQUALS = EQUALS;
    	}
        return this;
    }

    public CypherQueryBuilderForTemplates ENDS_WITH(String ENDS_WITH) {
    	if (ENDS_WITH.startsWith("*") || ENDS_WITH.toLowerCase().startsWith("all") || ENDS_WITH.length() == 0) {
    		// ignore
    	} else {
            this.ENDS_WITH = ENDS_WITH;
    	}
        return this;
    }

    public CypherQueryBuilderForTemplates MATCHES_PATTERN(String MATCHES_PATTERN) {
        this.MATCHES_PATTERN = MATCHES_PATTERN;
        return this;
    }

    public CypherQueryBuilderForTemplates RETURN(String RETURN) {
        this.RETURN = RETURN;
        return this;
    }
    
    public CypherQueryBuilderForTemplates REQUESTOR(String REQUESTOR) {
        this.REQUESTOR = REQUESTOR;
        return this;
    }
    
    public CypherQueryBuilderForTemplates ORDER_BY(String ORDER_BY) {
        this.ORDER_BY = ORDER_BY;
        return this;
    }
    
    public CypherQueryForTemplates build() {
        return new CypherQueryForTemplates(
        		MATCH
        		, TYPE
        		, EXCLUDE_TYPE
        		, LIBRARY
        		, LABEL
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
        		, REQUESTOR
        		, this.addWherePublic
        		);
    }
}