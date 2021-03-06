package ioc.liturgical.ws.managers.databases.external.neo4j.cypher;

import java.util.ArrayList;
import java.util.List;

/**
 *  Provides a means to build a query for searching docs.
 * There are three types of query builders:
 * docs - a query that only matches a node
 * notes - a query that matches two nodes and the relationship between them
 * links - a query that matches three nodes and relationships between them.  
 *             A link is a node that holds information about the relationship between
 *             two other nodes.  A link uses the REFERS_TO relationship type.

 * @author mac002
 *
 */
public class CypherQueryBuilderForDocs {
	private String MATCH = "";
	private String LABEL = "";
	private String EXCLUDE_LABEL = "";
	private String WHERE = "";
	private String EQUALS = "";
	private String STARTS_WITH = "";
	private String ENDS_WITH = "";
	private String GREATER_THAN = "";
	private String GREATER_THAN_OR_EQUAL = "";
	private String LESS_THAN = "";
	private String LESS_THAN_OR_EQUAL = "";
	private String MATCHES_PATTERN = "";
	private String CONTAINS = "";
	private String TAGS = "";
	private String TAG_OPERATOR = "or";
	private String LIBRARY = "";
	private String TOPIC = "";
	private String RETURN = "";
	private String ORDER_BY = "";
	private String REQUESTOR = "";
	private String REQUESTOR_DOMAINS = "";
    private boolean prefixProperties = true;
    private boolean addWherePublic = false;
    
	public CypherQueryBuilderForDocs(){};
	
	public CypherQueryBuilderForDocs(
			boolean prefixProperties
			, boolean addWherePublic
			) { 
		this.prefixProperties = prefixProperties;
		this.addWherePublic = addWherePublic;
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

    public CypherQueryBuilderForDocs EXCLUDE_LABEL(String LABEL) {
        this.EXCLUDE_LABEL = LABEL;
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

    public CypherQueryBuilderForDocs LESS_THAN(String LESS_THAN) {
        this.LESS_THAN = LESS_THAN;
        return this;
    }

    public CypherQueryBuilderForDocs LESS_THAN_OR_EQUAL(
    		String LESS_THAN_OR_EQUAL
    		) {
        this.LESS_THAN_OR_EQUAL = LESS_THAN_OR_EQUAL;
        return this;
    }

    public CypherQueryBuilderForDocs GREATER_THAN(String GREATER_THAN) {
        this.GREATER_THAN = GREATER_THAN;
        return this;
    }

    public CypherQueryBuilderForDocs GREATER_THAN_OR_EQUAL(
    		String GREATER_THAN_OR_EQUAL
    		) {
        this.GREATER_THAN_OR_EQUAL = GREATER_THAN_OR_EQUAL;
        return this;
    }

    public CypherQueryBuilderForDocs MATCHES_PATTERN(String MATCHES_PATTERN) {
        this.MATCHES_PATTERN = MATCHES_PATTERN;
        return this;
    }


    public CypherQueryBuilderForDocs TAGS(String TAGS) {
    	if (TAGS.startsWith("*") || TAGS.toLowerCase().startsWith("all") || TAGS.length() == 0) {
    		// ignore
    	} else {
    		this.TAGS = TAGS;
    	}
        return this;
    }

    public CypherQueryBuilderForDocs TAG_OPERATOR(String operator) {
    	this.TAG_OPERATOR = operator;
        return this;
    }

    public CypherQueryBuilderForDocs LIBRARY(String LIBRARY) {
    	if (LIBRARY.startsWith("*")) {
            this.LIBRARY = "";
    	} else {
            this.LIBRARY = LIBRARY;
    	}
        return this;
    }

    public CypherQueryBuilderForDocs TOPIC(String TOPIC) {
    	if (TOPIC.startsWith("*")) {
            this.TOPIC = "";
    	} else {
            this.TOPIC = TOPIC;
    	}
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
    
    public CypherQueryBuilderForDocs REQUESTOR(String REQUESTOR) {
        this.REQUESTOR = REQUESTOR;
        return this;
    }

    public CypherQueryBuilderForDocs REQUESTOR_DOMAINS(String DOMAINS) {
        this.REQUESTOR_DOMAINS = DOMAINS;
        return this;
    }

    public CypherQueryForDocs build() {
        return new CypherQueryForDocs(
        		MATCH
        		, LABEL
        		, EXCLUDE_LABEL
        		, WHERE
        		, CONTAINS
        		, EQUALS
        		, GREATER_THAN
        		, GREATER_THAN_OR_EQUAL
        		, LESS_THAN
        		, LESS_THAN_OR_EQUAL
        		, STARTS_WITH
        		, ENDS_WITH
        		, MATCHES_PATTERN
        		, TAGS
        		, TAG_OPERATOR
        		, LIBRARY
        		, TOPIC
        		, RETURN
        		, ORDER_BY
        		, REQUESTOR
        		, REQUESTOR_DOMAINS
        		, prefixProperties
        		, addWherePublic
        		);
    }
}