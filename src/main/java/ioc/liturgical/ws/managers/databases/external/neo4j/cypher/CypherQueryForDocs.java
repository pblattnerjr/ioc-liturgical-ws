package ioc.liturgical.ws.managers.databases.external.neo4j.cypher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ages.alwb.utils.core.error.handling.ErrorUtils;

public class CypherQueryForDocs {
	private static final Logger logger = LoggerFactory.getLogger(CypherQueryForDocs.class);
	private String MATCH = "";
	private String LABEL = "";
	private String EXCLUDE_LABEL = "";
	private String WHERE = "";
	private String CONTAINS = "";
	private String EQUALS = "";
	private String GREATER_THAN = "";
	private String GREATER_THAN_OR_EQUAL = "";
	private String LESS_THAN = "";
	private String LESS_THAN_OR_EQUAL = "";
	private String STARTS_WITH = "";
	private String ENDS_WITH = "";
	private String MATCHES_PATTERN = "";
	private String TAGS = "";
	private String TAG_OPERATOR = "";
	private String TOPIC = "";
	private String RETURN = "";
	private String ORDER_BY = "";
	private boolean prefixProperties = true;
	
	public CypherQueryForDocs(
			String MATCH
			, String LABEL
			, String EXCLUDE_LABEL
			, String WHERE
			, String CONTAINS
			, String EQUALS
			, String GREATER_THAN
			, String GREATER_THAN_OR_EQUAL
			, String LESS_THAN
			, String LESS_THAN_OR_EQUAL
			, String STARTS_WITH
			, String ENDS_WITH
			, String MATCHES_PATTERN
			, String TAGS
			, String TAG_OPERATOR
			, String TOPIC
			, String RETURN
			, String ORDER_BY
			, boolean prefixProperties
			) {
		this.MATCH = MATCH; // empty string--just used to make builder look like cypher.
		this.LABEL = LABEL;
		this.EXCLUDE_LABEL = EXCLUDE_LABEL;
		this.WHERE = WHERE;
		this.CONTAINS = CONTAINS;
		this.EQUALS = EQUALS;
		this.GREATER_THAN = GREATER_THAN;
		this.GREATER_THAN_OR_EQUAL = GREATER_THAN_OR_EQUAL;
		this.LESS_THAN = LESS_THAN;
		this.LESS_THAN_OR_EQUAL = LESS_THAN_OR_EQUAL;
		this.STARTS_WITH = STARTS_WITH;
		this.ENDS_WITH = ENDS_WITH;
		this.MATCHES_PATTERN = MATCHES_PATTERN;
		this.RETURN = RETURN;
		this.ORDER_BY = ORDER_BY;
		this.prefixProperties = prefixProperties;
		this.TAG_OPERATOR = TAG_OPERATOR;
		this.TAGS = TAGS;
		this.TOPIC = TOPIC;
	};
	

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("MATCH (doc");
		if (LABEL.length() >0) {
			if (! LABEL.startsWith(":")) {
				sb.append(":");
			}
			sb.append(LABEL);
		}
		sb.append(") ");

		StringBuffer whereClause = new StringBuffer();

		if (STARTS_WITH.length() > 0) {
			whereClause.append("WHERE doc." + WHERE + " STARTS WITH '" + STARTS_WITH + "' ");
		} else if (EQUALS.length() > 0 ) {
			whereClause.append("WHERE doc." + WHERE + " = '" + EQUALS + "' ");
		} else 	if (ENDS_WITH.length() > 0) {
			whereClause.append("WHERE doc." + WHERE + " ENDS WITH '" + ENDS_WITH + "' ");
		} else if (CONTAINS.length() > 0) {
			whereClause.append("WHERE doc." + WHERE + " CONTAINS '" + CONTAINS + "' ");
		} else if (MATCHES_PATTERN.length() > 0) {
			whereClause.append("WHERE doc." + WHERE + " =~ '" + MATCHES_PATTERN + "' ");
		} else if (GREATER_THAN.length() > 0 ) {
			whereClause.append("WHERE doc." + WHERE + " > '" + GREATER_THAN + "' ");
		} else if (GREATER_THAN_OR_EQUAL.length() > 0 ) {
			whereClause.append("WHERE doc." + WHERE + " >= '" + GREATER_THAN_OR_EQUAL + "' ");
		} 

		if (LESS_THAN.length() > 0 ) {
			if (whereClause.length() > 0) {
				whereClause.append(" AND ");
			} 
			whereClause.append("WHERE doc." + WHERE + " < '" + LESS_THAN + "' ");
		} else if (LESS_THAN_OR_EQUAL.length() > 0 ) {
			if (whereClause.length() > 0) {
				whereClause.append(" AND ");
			} else {
				whereClause.append(" WHERE ");
			}
			whereClause.append("doc." + WHERE + " <= '" + LESS_THAN_OR_EQUAL + "' ");
		}

		if (TOPIC.length() > 0) {
			if (whereClause.length() > 0) {
				whereClause.append(" AND ");
			} else {
				whereClause.append(" WHERE ");
			}
			whereClause.append("doc.topic = \"" + TOPIC + "\"");
		}

		if (whereClause.length() > 0) {
			sb.append(whereClause);
		}

		if (TAGS.length() > 0) {
			if (whereClause.length() > 0) {
				sb.append(" AND ");
			} else {
				sb.append(" WHERE ");
			}
			sb.append(tagMatcher("doc.tags", TAGS, TAG_OPERATOR));
		}

		if (EXCLUDE_LABEL.length() > 0) {
			if (whereClause.length() > 0) {
				sb.append(" AND NOT ");
			} else {
				sb.append(" WHERE NOT ");
			}
			sb.append("(doc:" + EXCLUDE_LABEL + ") ");
		}

		if (RETURN.contains("split")) {
			sb.append(" RETURN " + RETURN);
		} else if (RETURN.equals("*") || RETURN.length() == 0){
			sb.append(" RETURN properties(doc)");
		} else {
			if (! RETURN.contains("_valueSchemaId")) {
				RETURN = RETURN + ",_valueSchemaId";
			}
			if (prefixProperties) {
				sb.append(" RETURN doc." + RETURN.replaceAll(" ", "").replaceAll(",", ", doc."));
			} else {
				RETURN = RETURN.replaceAll(" ", "");
				String [] props = RETURN.split(",");
				sb.append(" RETURN doc." + props[0] + " as " + props[0]);
				for (int i=1; i < props.length; i++) {
					sb.append(", doc." + props[i] + " as " + props[i]);
				}
			}
		}
		if (ORDER_BY.length() == 0) {
			sb.append(" ORDER BY doc.id");
		} else {
			sb.append(" ORDER BY " + ORDER_BY);
		}
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

	public void setMATCH(String mATCH) {
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

	public String getLABEL() {
		return LABEL;
	}

	public void setLABEL(String lABEL) {
		LABEL = lABEL;
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


	public String getTAGS() {
		return TAGS;
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
		if (operator.trim().equals("or")) {
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


	public void setTAGS(String tAGS) {
		TAGS = tAGS;
	}


	public String getTAG_OPERATOR() {
		return TAG_OPERATOR;
	}


	public void setTAG_OPERATOR(String tAG_OPERATOR) {
		TAG_OPERATOR = tAG_OPERATOR;
	}


	public String getTOPIC() {
		return TOPIC;
	}


	public void setTOPIC(String TOPIC) {
		if (TOPIC.startsWith("*")) {
			this.TOPIC = "";
		} else {
			this.TOPIC = TOPIC;
		}
	}


	public String getGREATER_THAN() {
		return GREATER_THAN;
	}


	public void setGREATER_THAN(String gREATER_THAN) {
		GREATER_THAN = gREATER_THAN;
	}


	public String getGREATER_THAN_OR_EQUAL() {
		return GREATER_THAN_OR_EQUAL;
	}


	public void setGREATER_THAN_OR_EQUAL(String gREATER_THAN_OR_EQUAL) {
		GREATER_THAN_OR_EQUAL = gREATER_THAN_OR_EQUAL;
	}


	public String getLESS_THAN() {
		return LESS_THAN;
	}


	public void setLESS_THAN(String lESS_THAN) {
		LESS_THAN = lESS_THAN;
	}


	public String getLESS_THAN_OR_EQUAL() {
		return LESS_THAN_OR_EQUAL;
	}


	public void setLESS_THAN_OR_EQUAL(String lESS_THAN_OR_EQUAL) {
		LESS_THAN_OR_EQUAL = lESS_THAN_OR_EQUAL;
	}


	public boolean isPrefixProperties() {
		return prefixProperties;
	}


	public void setPrefixProperties(boolean prefixProperties) {
		this.prefixProperties = prefixProperties;
	}


}