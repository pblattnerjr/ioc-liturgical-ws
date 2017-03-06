package net.ages.alwb.utils.core.datastores.db.h2.queries;

/**
 * Extends Query for json table where
 * each row is a key-value pair, where the
 * value is a json string, and the key is the id
 * from the json string.
 * @author mac002
 *
 */
public class QueryForJson extends Query {

	private String tbName = "FORMAT_JSON";
	
    public QueryForJson(String tableName) {
    	if (tableName != null) {
    		tbName = tableName;
    	}
    	super.setCreateQuery(tbName, "_id varchar(255) primary key, value varchar");
       	super.setDeleteQueryWhereEqual(tbName, "_id = ?");
    	super.setInsertQuery(tbName, "(_id, value) values" + "(?,?)");
    	// although the statement is written generically, we will retrieve all columns.
    	super.setSelectQuery(tbName, "*");
    	super.setSelectCountQuery(tbName);
    	// although the statement is written generically, we will only retrieve by id.
    	// Note: if retrieving for a specific ID, it is faster to use the equal rather than the like
    	super.setSelectQueryWhereEqual(tbName, "*", "_id = ?");
    	// although the statement is written generically, we will only retrieve by id.
    	// Use this when you want to search based on patterns applied to the id,
    	// e.g. the topic part of the id, e.g. actors|
    	super.setSelectQueryWhereLike(tbName, "*", "_id LIKE ?");
    	super.setSelectQueryWhereIdRegEx(tbName, "*", "_id REGEXP ?");
    	super.setSelectQueryWhereValueRegEx(tbName, "*", "value REGEXP ?");
    	super.setTruncateQuery(tbName);
       	super.setUpdateQueryWhereEqual(tbName, "value = ?", "_id = ?");
    }

}
