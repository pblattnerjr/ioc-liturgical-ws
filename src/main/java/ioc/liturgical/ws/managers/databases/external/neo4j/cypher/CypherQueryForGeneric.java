package ioc.liturgical.ws.managers.databases.external.neo4j.cypher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.ocmc.ioc.liturgical.utils.ErrorUtils;

/**
 *  Provides a means to build a query for searching notes.
 * There are three types of query builders:
 * docs - a query that only matches a node
 * notes - a query that matches two nodes and the relationship between them
 * links - a query that matches three nodes and relationships between them.  
 *             A link is a node that holds information about the relationship between
 *             two other nodes.  A link uses the REFERS_TO relationship type.

 * @author mac002
 *
 */

public class CypherQueryForGeneric {
	private static final Logger logger = LoggerFactory.getLogger(CypherQueryForGeneric.class);

	private String MATCH = "";
	private String TYPE = "";
	private String EXCLUDE_TYPE = "";
	private String NOTE_TYPE = "";
	private String LIBRARY = "";
	private String LABEL = "";
	private String WHERE = "";
	private String CONTAINS = "";
	private String EQUALS = "";
	private String STARTS_WITH = "";
	private String ENDS_WITH = "";
	private String MATCHES_PATTERN = "";
	private String TAGS = "";
	private String TAG_OPERATOR = "";
	private String RETURN = "";
	private String ORDER_BY = "";
	private String REQUESTOR = "";
	private boolean addWherePublic = false;
	
	public CypherQueryForGeneric(
			String MATCH
			, String TYPE
			, String EXCLUDE_TYPE
			, String NOTE_TYPE
			, String LIBRARY
			, String LABEL
			, String WHERE
			, String CONTAINS
			, String EQUALS
			, String STARTS_WITH
			, String ENDS_WITH
			, String MATCHES_PATTERN
			, String TAGS
			, String TAG_OPERATOR
			, String RETURN
			, String ORDER_BY
			, String REQUESTOR
			, boolean addWherePublic
			) {
		this.MATCH = MATCH; // empty string--just used to make builder look like cypher.
		this.TYPE = TYPE;
		this.EXCLUDE_TYPE = EXCLUDE_TYPE;
		this.NOTE_TYPE = NOTE_TYPE;
		this.LIBRARY = LIBRARY;
		this.LABEL = LABEL;
		this.WHERE = WHERE;
		this.CONTAINS = CONTAINS;
		this.EQUALS = EQUALS;
		this.STARTS_WITH = STARTS_WITH;
		this.ENDS_WITH = ENDS_WITH;
		this.MATCHES_PATTERN = MATCHES_PATTERN;
		this.RETURN = RETURN;
		this.ORDER_BY = ORDER_BY;
		this.REQUESTOR = REQUESTOR;
		this.TAG_OPERATOR = TAG_OPERATOR;
		this.TAGS = TAGS;
		this.addWherePublic = addWherePublic;
	};
	

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("MATCH (n:");
		sb.append(LABEL);
		sb.append(") ");

		StringBuffer whereClause = new StringBuffer();
		if (STARTS_WITH.length() > 0) {
			whereClause.append("WHERE n." + WHERE + " STARTS WITH '" + STARTS_WITH + "' ");
		} else if (EQUALS.length() > 0 ) {
			whereClause.append("WHERE n." + WHERE + " = '" + EQUALS + "' ");
		} else 	if (ENDS_WITH.length() > 0) {
			whereClause.append("WHERE n." + WHERE + " ENDS WITH '" + ENDS_WITH + "' ");
		} else if (CONTAINS.length() > 0) {
			whereClause.append("WHERE n." + WHERE + " CONTAINS '" + CONTAINS + "' ");
		} else if (MATCHES_PATTERN.length() > 0) {
			whereClause.append("WHERE n." + WHERE + " =~ '" + MATCHES_PATTERN + "' ");
		} 
		if (LIBRARY.length() > 0) {
			if (whereClause.length() > 0) {
				whereClause.append(" AND ");
			} else {
				whereClause.append(" WHERE ");
			}
			whereClause.append(LIBRARY);
		}
		if (TAGS.length() > 0) {
			if (whereClause.length() > 0) {
				whereClause.append(" AND ");
			} else {
				whereClause.append(" WHERE ");
			}
			whereClause.append(tagMatcher("n.tags", TAGS, TAG_OPERATOR));
		}
		
		if (addWherePublic) {
			if (whereClause.length() > 0) {
				whereClause.append(" AND ");
			} else {
				whereClause.append(" WHERE");
			}
			if (this.REQUESTOR.length() > 0 && (! this.REQUESTOR.startsWith("*"))) {
				whereClause.append(" (n.visibility = 'PUBLIC' or n.createdBy = '");
				whereClause.append(this.REQUESTOR);
				whereClause.append("' or n.assignedTo = '");
				whereClause.append(this.REQUESTOR);
				whereClause.append("') ");
			} else {
				whereClause.append(" n.visibility = 'PUBLIC' ");
			}
		}

		if (EXCLUDE_TYPE != null && EXCLUDE_TYPE.length() > 0) {
			if (whereClause.length() > 0) {
				whereClause.append(" AND NOT ");
			} else {
				whereClause.append(" WHERE NOT ");
			}
			whereClause.append("type(n) = '" + EXCLUDE_TYPE + "' ");
		}

		if (whereClause.length() > 0) {
			sb.append(whereClause);
		}
		
		sb.append(" RETURN " + RETURN);
		sb.append(" ORDER BY " + ORDER_BY);
		return sb.toString();
	}

	public String getCONTAINS() {
		return CONTAINS;
	}

	public void setCONTAINS(String CONTAINS) {
		this.CONTAINS = CONTAINS;
	}

	public String getMATCH() {
		return MATCH;
	}

	public void setMATCH(String MATCH) {
		this.MATCH = MATCH;
	}

	public String getWHERE() {
		return WHERE;
	}

	public void setWHERE(String WHERE) {
		this.WHERE = WHERE;
	}

	public String getRETURN() {
		return RETURN;
	}

	public void setRETURN(String RETURN) {
		this.RETURN = RETURN;
	}

	public String getTYPE() {
		return TYPE;
	}

	public void setTYPE(String type) {
		TYPE = type;
	}

	public String getSTARTS_WITH() {
		return STARTS_WITH;
	}

	public void setSTARTS_WITH(String sTARTS_WITH) {
		STARTS_WITH = sTARTS_WITH;
	}

	public String getENDS_WITH() {
		return ENDS_WITH;
	}


	public void setENDS_WITH(String eNDS_WITH) {
		ENDS_WITH = eNDS_WITH;
	}


	public String getMATCHES_PATTERN() {
		return MATCHES_PATTERN;
	}

	public void setMATCHES_PATTERN(String mATCHES_PATTERN) {
		MATCHES_PATTERN = mATCHES_PATTERN;
	}


	public String getORDER_BY() {
		return ORDER_BY;
	}


	public void setORDER_BY(String oRDER_BY) {
		ORDER_BY = oRDER_BY;
	}


	public String getEQUALS() {
		return EQUALS;
	}


	public void setEQUALS(String eQUALS) {
		EQUALS = eQUALS;
	}


	public String getLIBRARY() {
		return LIBRARY;
	}


	public void setLIBRARY(String lIBRARY) {
		LIBRARY = lIBRARY;
	}

	public String getREQUESTOR() {
		return REQUESTOR;
	}


	public void setREQUESTOR(String requestor) {
		REQUESTOR = requestor;
	}

	/**
	 * if the operator is an 'or':
	 * 
	 * any (x in ["a", "b"] where x in link.tags)
	 *
	 * if the operator is an 'and':
	 * 
	 * all (x in ["a", "b"] where x in link.tags)
	 * 
	 * @param property
	 * @param tags
	 * @param operator
	 * @return
	 */
	private String tagMatcher(
			String property
			, String tags
			, String operator
			) {
		StringBuffer result = new StringBuffer();
		String theOperator = " all "; // initialize to 'all', but change to 'any' if using 'or'
		if (operator.trim().equals("or") || operator.trim().equals("any")) {
			theOperator = " any ";
		}
		if (tags != null && tags.length() > 0) {
			try {
				String [] parts = tags.split(",");
				if (parts.length > 0) {
					result.append(theOperator);
					result.append(" (x in [");
				    result.append("\"");
				    result.append(parts[0].trim());
				    result.append("\" ");
				}
				if (parts.length > 1) {
					for (int i=1; i < parts.length; i++) {
					    result.append(", \"");
					    result.append(parts[i].trim());
					    result.append("\"");
					}
				}
				result.append("] where x in ");
				result.append(property);
				result.append(") ");
			} catch (Exception e) {
				ErrorUtils.report(logger, e);
			}
		}
		return result.toString();
	}


	public String getTAGS() {
		return TAGS;
	}


	public void setTAGS(String tAGS) {
		TAGS = tAGS;
	}


	public String getTAG_OPERATOR() {
		return TAG_OPERATOR;
	}


	public void setTAG_OPERATOR(String tAG_OPERATOR) {
		TAG_OPERATOR = tAG_OPERATOR;
	}


	public String getEXCLUDE_TYPE() {
		return EXCLUDE_TYPE;
	}


	public void setEXCLUDE_TYPE(String eXCLUDE_TYPE) {
		EXCLUDE_TYPE = eXCLUDE_TYPE;
	}


	public String getLABEL() {
		return LABEL;
	}


	public void setLABEL(String lABEL) {
		LABEL = lABEL;
	}


	public String getNOTE_TYPE() {
		return NOTE_TYPE;
	}


	public void setNOTE_TYPE(String NOTE_TYPE) {
		this.NOTE_TYPE = NOTE_TYPE;
	}

}