package net.ages.alwb.utils.core.datastores.db.factory;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.List;

import org.ocmc.ioc.liturgical.schemas.models.db.internal.TK;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import net.ages.alwb.utils.core.datastores.db.h2.manager.H2ConnectionManager;
import net.ages.alwb.utils.core.datastores.db.h2.queries.QueryForJson;
import net.ages.alwb.utils.core.datastores.json.manager.JsonStoreManager;

public class DbConnectionFactory {
	/**
	 * 
	 * @param name - The name to give the database file.
	 * @param tableName - The name of the table to use
	 * @param deleteOldDb - If the DB exists, delete the old one?
	 * @param deleteOldTableRows - If the table exists, delete the old rows?
	 * @return handle to the manager
	 * @throws SQLException 
	 */
	public static H2ConnectionManager getH2Manager(
			String name
			, String tableName
			, boolean deleteOldDb
			, boolean deleteOldTableRows
			) throws SQLException {
    	H2ConnectionManager manager = 
    			new H2ConnectionManager(name, deleteOldDb);
    	manager.setQuery(new QueryForJson(tableName));
    	
    	// create the table.  Will only happen if doesn't already exist.
    	manager.createTable();
    	// in case it did exist, for this example, we will get rid of the rows.
    	if (!deleteOldDb) {
    		if (deleteOldTableRows) {
            	manager.truncateTable();
    		}
    	}

    	return manager;
	};
	
	/**
	 * Get a JsonManager backed by a persistent datastore
	 * @param name of the datastore.  May include a relative or full path.
	 * @param type e.g. Json File, database
	 * @param instance an instance of the class to be used as the class type
	 * @param deleteOldDataStore Delete the old datastore and create a new one?
	 * @param deleteOldEntries Keep the old datastore, but delete the data?
	 * @return
	 * @throws SQLException 
	 */
	public static JsonStoreManager<? extends TK> getPersistedJsonManager(
			String name
			, JsonStoreManager.STORE_TYPE storetype
			, TK instance
			, boolean deleteOldDataStore
			, boolean deleteOldEntries
			) throws SQLException {
		JsonStoreManager<? extends TK> result = new JsonStoreManager<TK>(instance);
		result.setDataStoreName(name);
		result.setStoreType(storetype, deleteOldDataStore, deleteOldEntries);
		return result;
	}
}
