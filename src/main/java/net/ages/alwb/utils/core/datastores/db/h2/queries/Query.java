package net.ages.alwb.utils.core.datastores.db.h2.queries;

/**
 * Superclass for all queries.
 * @author mac002
 *
 */
public class Query {
    private String createQuery;
    private String deleteQueryWhereEqual;
    private String insertQuery;
    private String selectQuery;
    private String selectCountQuery;
    private String selectQueryWhereEqual;
    private String selectQueryWhereLike;
    private String selectQueryWhereIdRegEx;
    private String selectQueryWhereValueRegEx;
    private String truncateQuery;
    private String updateQueryWhereEqual;
    private String selectQueryTableNames = "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES";

    public void setCreateQuery(String table, String value) {
    	createQuery = "CREATE TABLE IF NOT EXISTS " + table + "(" + value + ")";
    }
    public String getCreateQuery() {
		return createQuery;
	}
    public void setTruncateQuery(String table) {
    	truncateQuery = "TRUNCATE TABLE " + table;
    }
    public String getTruncateQuery() {
		return truncateQuery;
	}
    public void setInsertQuery(String table, String value) {
    	insertQuery = "INSERT INTO " + table + value;
    }
	public String getInsertQuery() {
		return insertQuery;
	}
	public void setSelectQuery(String table, String what) {
		selectQuery = "SELECT " + what + " FROM " + table;
	}
	public String getSelectQuery() {
		return selectQuery;
	}
	public void setSelectCountQuery(String table) {
		selectCountQuery = "SELECT COUNT(*)  FROM " + table;
	}
	public String getSelectCountQuery() {
		return selectCountQuery;
	}
	public void setSelectQueryWhereEqual(String table, String what, String where) {
		selectQueryWhereEqual = "SELECT " + what + " FROM " + table + " WHERE " + where;
	}
	public String getSelectQueryWhereEqual() {
		return selectQueryWhereEqual;
	}
	public void setSelectQueryWhereLike(String table, String what, String where) {
		selectQueryWhereLike = "SELECT " + what + " FROM " + table + " WHERE " + where;
	}
	public String getSelectQueryWhereLike() {
		return selectQueryWhereLike;
	}
	
	public void setSelectQueryWhereIdRegEx(String table, String what, String where) {
		selectQueryWhereIdRegEx = "SELECT " + what + " FROM " + table + " WHERE " + where;
	}
	public String getSelectQueryWhereIdRegEx() {
		return selectQueryWhereIdRegEx;
	}
	public void setSelectQueryWhereValueRegEx(String table, String what, String where) {
		selectQueryWhereValueRegEx = "SELECT " + what + " FROM " + table + " WHERE " + where;
	}
	public String getSelectQueryWhereValueRegEx() {
		return selectQueryWhereValueRegEx;
	}

	public void setUpdateQueryWhereEqual(String table, String value, String where) {
    	updateQueryWhereEqual = "UPDATE " + table + " SET " + value  + " WHERE " + where;
    }
	public String getUpdateQueryWhereEqual() {
		return updateQueryWhereEqual;
	}
	public String getDeleteQueryWhereEqual() {
		return deleteQueryWhereEqual;
	}
	public void setDeleteQueryWhereEqual(String table, String where) {
    	deleteQueryWhereEqual = "DELETE FROM " + table  + " WHERE " + where;
	}
	
	public String getSelectTableNamesQuery() {
		return selectQueryTableNames;
	}
	
}
