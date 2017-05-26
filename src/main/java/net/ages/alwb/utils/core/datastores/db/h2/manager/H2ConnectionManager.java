package net.ages.alwb.utils.core.datastores.db.h2.manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.h2.tools.DeleteDbFiles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import ioc.liturgical.ws.constants.Constants;
import net.ages.alwb.utils.core.datastores.db.h2.queries.Query;
import net.ages.alwb.utils.core.error.handling.ErrorUtils;
import net.ages.alwb.utils.core.file.AlwbFileUtils;


/**
 * Manages H2 database connect. 
 * Uses parameterized Prepared Statements.
 * @author mac002
 *
 */
public class H2ConnectionManager {
	private static final Logger logger = LoggerFactory
			.getLogger(H2ConnectionManager.class);

	private Connection connection = null;

    private String DB_DRIVER = "org.h2.Driver";
    private String DB_NAME = Constants.DB_NAME;
    private String DB_PROTOCOL = "jdbc:h2:./";
    private String DB_CONNECTION = DB_PROTOCOL + DB_NAME;
    private String DB_USER = "system";
    private String DB_PASSWORD = "";
    
    private PreparedStatement createPreparedStatement = null;
    private PreparedStatement deletePreparedStatement = null;
    private PreparedStatement insertPreparedStatement = null;
    private PreparedStatement selectPreparedStatement = null;
    private PreparedStatement truncatePreparedStatement = null;
    private PreparedStatement updatePreparedStatement = null;

    private Query currentQuery = null;
    
    /**
     * 
     * @param deleteFirst - first delete the entire database, then create again
     */
    public H2ConnectionManager(
    		String dbName
    		, boolean deleteFirst
    		) {
    	try {
    		setDbName(dbName);
    		if (deleteFirst) {
    			deleteDatabase();
    		}
        	connection = getDBConnection();
            connection.setAutoCommit(false);
    	} catch (Exception e) {
    		ErrorUtils.report(logger, e);
    	}
    }
    
    public void setQuery(Query query) {
    	this.currentQuery = query;
    }
    
    private void setDbName(String name) {
		DB_NAME = name;
		DB_CONNECTION = DB_PROTOCOL + DB_NAME;
    }

    /**
     * 
     * @param deleteFirst - first delete the entire database, then create again
     */
    public H2ConnectionManager(boolean deleteFirst) {
    	try {
    		if (deleteFirst) {
    			deleteDatabase();
    		}
        	connection = getDBConnection();
            connection.setAutoCommit(false);
    	} catch (Exception e) {
    		ErrorUtils.report(logger, e);
    	}
    }
    
    public String getDbName() {
    	return DB_NAME;
    }
    
    public void createTable() throws SQLException {
    	createTable(currentQuery.getCreateQuery());
    }
    
    public void createTable(String statement) throws SQLException {
		createPreparedStatement = connection.prepareStatement(statement);
	    createPreparedStatement.executeUpdate();
        createPreparedStatement.close();
    }
    
    public List<String> getTableNames() {
    	List<String> result = new ArrayList<String>();
    	try {
        	String [] types = {"TABLE"};
        	ResultSet rs = connection.getMetaData().getTables(null, null, "%", types);
        	while (rs.next()) {
        	  result.add(rs.getString("TABLE_NAME"));
        	}        
        } catch (Exception e) {
        	ErrorUtils.report(logger, e);
        }
    	return result;
    }
    
    public boolean existsTable(String table) {
    	List<String> tables = getTableNames();
    	return tables.contains(table);
    }

    /**
     * Deletes a single JsonObject
     * @param doc - to be deleted
     * @throws SQLException 
     */
    public void delete(JsonObject doc) throws SQLException {
    	delete(this.currentQuery.getDeleteQueryWhereEqual(), doc);
    }
    
    /**
     * Deletes a single JsonObject
     * @param statement - SQL for prepare statement
     * @param doc - to be deleted
     * @throws SQLException 
     */
    public void delete(String statement, JsonObject doc) throws SQLException {
			deletePreparedStatement = connection.prepareStatement(statement);
			deletePreparedStatement.setString(1, doc.get("_id").getAsString());
			deletePreparedStatement.executeUpdate();
			deletePreparedStatement.close();
		    commit();
    }
   
    public void truncateTable() throws SQLException {
    	truncateTable(currentQuery.getTruncateQuery());
    }
    
    /**
     * Removes all rows from a table.
     * @see http://www.h2database.com/html/grammar.html#truncate_table
     * @param statement
     * @throws SQLException 
     */
    public void truncateTable(String statement) throws SQLException {
			truncatePreparedStatement = connection.prepareStatement(statement);
			truncatePreparedStatement.executeUpdate();
			truncatePreparedStatement.close();
    }

    public void insert(List<JsonObject> docs) throws SQLException {
    	insert(currentQuery.getInsertQuery(), docs);
    }
    
    /**
     * Insert each of the Json docs.
     * @param statement - SQL for prepare statement
     * @param docs - json to be inserted
     * @throws SQLException 
     */
    public void insert(String statement, List<JsonObject> docs) throws SQLException {
		insertPreparedStatement = connection.prepareStatement(statement);
        for (JsonObject json : docs) {
            insertPreparedStatement.setString(1, json.get("_id").getAsString());
            insertPreparedStatement.setString(2, json.toString());
            insertPreparedStatement.executeUpdate();
        }
		insertPreparedStatement.close();
		commit();
    }
    
    /**
     * Inserts a single JsonObject
     * @param doc - to be inserted
     */
    public void insert(JsonObject doc) throws SQLException {
    	insert(currentQuery.getInsertQuery(), doc);
    }
    
    	/**
     * Inserts a single JsonObject
     * @param statement - SQL for prepare statement
     * @param doc - to be inserted
     */
    public void insert(String statement, JsonObject doc) throws SQLException {
		insertPreparedStatement = connection.prepareStatement(statement);
        insertPreparedStatement.setString(1, doc.get("_id").getAsString());
        insertPreparedStatement.setString(2, doc.toString());
        insertPreparedStatement.executeUpdate();
		insertPreparedStatement.close();
	    commit();
    }

    /**
     * Inserts a single JsonObject
     * @param key - table id.  Normally same as in doc, but in case you need to make it different, use this.
     * @param statement - SQL for prepare statement
     * @param doc - to be inserted
     * @throws SQLException 
     */
    public void insert(String statement, String key, JsonObject doc) throws SQLException {
		insertPreparedStatement = connection.prepareStatement(statement);
        insertPreparedStatement.setString(1, key);
        insertPreparedStatement.setString(2, doc.toString());
        insertPreparedStatement.executeUpdate();
		insertPreparedStatement.close();
	    commit();
    }

    /**
     * Updates a single JsonObject
     * The _id for the update is retrieved from the JsonObject.
     * @param doc - to be inserted
     * @throws SQLException 
     */
    public void updateWhereEqual(JsonObject doc) throws SQLException {
    	update(currentQuery.getUpdateQueryWhereEqual(), doc);
    }
    
    /**
     * Updates a single JsonObject
     * The _id for the update is retrieved from the JsonObject.
     * @param statement - SQL for prepare statement
     * @param doc - to be inserted
     * @throws SQLException 
     */
    public void update(String statement, JsonObject doc) throws SQLException {
		updatePreparedStatement = connection.prepareStatement(statement);
        updatePreparedStatement.setString(1, doc.toString());
        updatePreparedStatement.setString(2, doc.get("_id").getAsString());
        updatePreparedStatement.executeUpdate();
		updatePreparedStatement.close();
	    commit();
    }
    
    public boolean contains(String id) throws SQLException {
    	List<JsonObject> result = this.queryForJsonWhereEqual(id);
    	if (result == null) {
    		return false;
    	} else {
    		return result.size() > 0;
    	}
    }
    

    /**
     * Updates a single JsonObject
     * The _id for the update is retrieved from the JsonObject.
     * @param statement - SQL for prepare statement
     * @param doc - to be inserted
     * @throws SQLException 
     */
    public void update(String statement, String id, JsonObject doc) throws SQLException {
			updatePreparedStatement = connection.prepareStatement(statement);
            updatePreparedStatement.setString(1, doc.toString());
            updatePreparedStatement.setString(2, id);
            updatePreparedStatement.executeUpdate();
			updatePreparedStatement.close();
		    commit();
    }

    public void closeSelect() throws SQLException {
        selectPreparedStatement.close();
    }

    /**
     * Query the database and get the results.
     * Important!!! Be sure to call the closeSelect() method
     * after you have processed the ResultSet!
     * 
     * @param statement
     * @return the results of running the query
     * @throws SQLException 
     */
    public ResultSet query(String statement, String where) throws SQLException {
    	ResultSet rs = null;
		selectPreparedStatement = connection.prepareStatement(statement);
		if (where != null) {
			selectPreparedStatement.setString(1, where);
		}
        rs = selectPreparedStatement.executeQuery();
        return rs;
    }
   
    public List<JsonObject> queryForJson() throws SQLException {
    	return queryForJson(currentQuery.getSelectQuery(), null);
    }
    
    /**
     * Assuming that all rows retrieved will have a value column and that the
     * value is actually a json string, will return JsonObjects for all rows
     * in the table.
     * @param statement
     * @return
     * @throws SQLException 
     */
    public List<JsonObject> queryForJson(String statement) throws SQLException {
    	return queryForJson(statement,null);
    }
    
    public void closeConnection() throws SQLException {
		connection.close();
    }
    

    public int getRowCount(String statement) {
    	int result = 0;
    	ResultSet rs = null;
        try {
			selectPreparedStatement = connection.prepareStatement(statement);
	        rs = selectPreparedStatement.executeQuery();
	        while (rs.next()) {
            	rs.getString("value");
	        	result = result + 1;
	        }
	        selectPreparedStatement.close();
		} catch (SQLException e) {
			result = 0;
		} catch (Exception e) {
			result = 0;
		}
        return result;
    	
    }

    public List<JsonObject> queryForJsonWhereEqual(String where) throws SQLException {
    	return queryForJson(currentQuery.getSelectQueryWhereEqual(), where);
    }

    public List<JsonObject> queryForJsonWhereStartsWith(String with) throws SQLException {
    	return queryForJson(currentQuery.getSelectQueryWhereLike(), with + Constants.ID_DELIMITER +"%");
    }

    public List<JsonObject> queryForJsonWhereLike(String like) throws SQLException {
    	return queryForJson(currentQuery.getSelectQueryWhereLike(), "%"+like+"%");
    }
    
    public List<JsonObject> queryForJsonWhereIdRegEx(String regex) throws SQLException {
    	return queryForJson(currentQuery.getSelectQueryWhereIdRegEx(), regex);
    }

    public List<JsonObject> queryForJsonWhereValueRegEx(String regex) throws SQLException {
    	return queryForJson(currentQuery.getSelectQueryWhereValueRegEx(), regex);
    }
    /**
     * Assuming that all rows retrieved will have a value column and that the
     * value is actually a json string, will return JsonObjects for all retrieved
     * rows that match the WHERE statement.
     * @param statement
     * @param where
     * @return
     * @throws SQLException 
     */
    public List<JsonObject> queryForJson(String statement, String where) throws SQLException {
    	List<JsonObject> result = new ArrayList<JsonObject>();
    	JsonParser parser = new JsonParser();
    	ResultSet rs = null;
		selectPreparedStatement = connection.prepareStatement(statement);
		if (where != null) {
			selectPreparedStatement.setString(1, where);
		}
        rs = selectPreparedStatement.executeQuery();
        while (rs.next()) {
            result.add(parser.parse(rs.getString("value")).getAsJsonObject());
        }
        selectPreparedStatement.close();
        return result;
    }


    public void commit() throws SQLException {
			connection.commit();
    }
    public void deleteDatabase() {
    	String path = AlwbFileUtils.getPathToFile(DB_NAME);
        DeleteDbFiles.execute("./" + path, DB_NAME, true);
    }

    private Connection getDBConnection() throws SQLException {
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
        	ErrorUtils.report(logger, e);
        }
        return DriverManager.getConnection(
        		DB_CONNECTION
        		, DB_USER
        		, DB_PASSWORD);
    }

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}
}
