package net.ages.alwb.utils.core.datastores.json.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.io.File;
import java.lang.reflect.Type;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import ioc.liturgical.ws.constants.Constants;
import org.ocmc.ioc.liturgical.schemas.models.db.internal.TK;

import net.ages.alwb.utils.core.datastores.db.factory.DbConnectionFactory;
import net.ages.alwb.utils.core.datastores.db.h2.manager.H2ConnectionManager;
import org.ocmc.ioc.liturgical.utils.ErrorUtils;
import org.ocmc.ioc.liturgical.utils.FileUtils;


/**
 * Manager for a Datastore of objects that extend TK.
 * Holds Values in memory, store as Topic + Key.
 * Provides Topic-Key value methods.
 * Serializes to, deserializes from either a json file or database (H2).
 * 
 * Example: Topic: actors Key: Priest Value: ΙΕΡΕΑΣ
 * @author mac002
 *
 * @param <T> the type of the Value.  Must be a subclass of TopicKey (TK)
 */
public class JsonStoreManager <T extends TK>  {
	private static final Logger logger = LoggerFactory
			.getLogger(ErrorUtils.class);

	private Table<String,String,T> table = HashBasedTable.create();
	private T instanceOfT = null;
	
	private Gson gson = null;
	private static final String defaultStore = Constants.APP_DATA_PATH+Constants.DB_NAME;
	
	public enum STORE_TYPE {
		 DB(defaultStore, "")
		, FILE(defaultStore, ".json")
		, MEMORY("","");

		private String storeName;
		private String extension;
		
		private STORE_TYPE(String name, String extension) {
			this.storeName = name;
			this.extension = extension;
		}
		
		public String getDefaultStoreName() {
			return this.storeName;
		}
		public String getExtension() {
			return this.extension;
		}
	}
	
	private H2ConnectionManager dbManager = null;
	private STORE_TYPE storeType = STORE_TYPE.FILE;
	private boolean prettyPrint = false; // store json pretty printed?
	
	private String datastoreName = 
			STORE_TYPE.FILE.getDefaultStoreName() 
			+ STORE_TYPE.FILE.getExtension();
	
	// Constructor
	/**
	 * Give the Manager an instance of T so that it can use
	 * have access to its methods.
	 * @param instance
	 */
	public JsonStoreManager(T instance) {
		this.instanceOfT = instance;
    	gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();        		
	}
	
	public int getSize() {
		return table.size();
	}

	/**
	 * Add the given value
	 * @param value
	 * @throws SQLException 
	 */
	public void add(T value) throws SQLException {
		if (this.storeType == STORE_TYPE.DB) {
			if (dbManager.contains(value.get_id())) {
				dbManager.updateWhereEqual(value.toJsonObject());
			} else {
				dbManager.insert(value.toJsonObject());
			}
		}
		table.put(value.getKey(), value.getTopic(), value);
	}
	
	/**
	 * Add each value in the list
	 * @param list
	 * @throws SQLException 
	 */
	public void add(List<T> list) throws SQLException {
		for (T value : list) {
			add(value);
		}
	}
	
	/**
	 * Get a map of entries for the specified topic.
	 * @param topic
	 * @return
	 */
    public Map<String,T> getTopicMap(String topic) {
    	return table.column(topic);
    }
    
    /**
     * Get a collection of entries for the given topic.
     * @param topic
     * @return
     */
    public Collection<T> getTopicCollection(String topic) {
    	return getTopicMap(topic).values();
    }
    
    /**
     * Get a specific entry
     * @param topic
     * @param key
     * @return
     */
    public T getEntry(String topic, String key) {
    	return table.get(key, topic);
    }

    /**
     * Remove the indicated topic and key combination
     * @param topic
     * @param key
     */
    public void remove (String topic, String key) {
    	try {
    		T entry = getEntry(topic, key);
    		if (this.storeType == STORE_TYPE.DB) {
    			if (dbManager.contains(entry.get_id())) {
    				dbManager.delete(entry.toJsonObject());
    			}
    		}
    		table.remove(key, topic);
    	} catch (Exception e) {
    		ErrorUtils.report(logger, e);
    	}
    }
    /**
     * Returns all entries whose key matches the one passed in.
     * @param key
     * @return Map, where the keys are topics, and T is the entry.
     */
    public Map<String,T> getKeyMap(String key) {
    	return table.row(key);
    }

    /**
     * Return the entire dataset as a json string.
     * Uses whatever value prettyPrint was set to via the prettyPrint(boolean value) method.
     * @return the Json
    public String getAsJson() {
    	return this.getAsJson(prettyPrint);
    }
    
    /**
     * Return the entire dataset as a json string.
     * @param prettyprint If true, has tabs and line feeds inserted to be human readable
     * @return the Json
     */
    public String getAsJson(boolean prettyprint) {
    	String result = "{}";
        try {
        	Gson gson = null;
        	if (prettyprint) {
            	gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
        	} else {
            	gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();        		
        	}
        	Collection<T> entries = table.values();
        	result = gson.toJson(entries);
        } catch (Exception e) {
        	ErrorUtils.report(logger, e);
        	result = "{}";
        }
    	return result;
    }

    /**
     * Load the table from the provided json string
     * @param json
     */
    public void load(String json) {
    	try {
    		List<T> entries = jsonToList(json);
    		if (entries != null) {
    			table.clear();
    	   		add(entries);
    	   	}
    	} catch (Exception e) {
    		ErrorUtils.report(logger, e);
    	}
    }
    
    /**
     * Load the table from the json file on disk.
     * @throws SQLException 
     */
    public void loadFromJsonFile() throws SQLException {
    	setStoreType(STORE_TYPE.FILE, false, false);
    	load();
    }
    
    public void loadFromDatabase() throws SQLException {
    	setStoreType(STORE_TYPE.DB, false, false);
    	load();
    }
    
    /**
     * Convert the provided json string into a list of the instantiated type for this Manager.
     * @param json
     * @return
     */
    public List<T> jsonToList(String json) {
    	List<T> result = new ArrayList<T>();
    	List<JsonObject> jsonList;
    	Type listType = new TypeToken<List<JsonObject>>() {}.getType();
    	try {
        	jsonList = 
        			gson.fromJson(
        					json
        					, listType
        	);
        	for (JsonObject o : jsonList) {
        		result.add((T) instanceOfT.fromJsonString(o.toString()));
        	}
    	} catch (Exception e) {
    		ErrorUtils.report(logger, e);
    		result = null;
    	}
    	return result;
    }
    
    /**
     * Reads the entries from the specified STORE_TYPE.
     * @throws SQLException 
     * 
     */
    public void load() throws SQLException {
    	switch (this.storeType) {
    		case FILE: {
    			loadFromFile();
    			break;
    		}
    		case DB :  {
    			loadFromDb();
    			break;
    		}
    	}
    }
    
    private void loadFromDb() throws SQLException {
    	List<JsonObject> list = dbManager.queryForJson();
    	for (JsonObject json : list) {
    		T entry = (T) instanceOfT.fromJsonString(json.toString());
    		add(entry);
    	}
    }
    
    private void loadFromFile() {
    	try {
			String json = FileUtils.getFileContents(new File(datastoreName));
			load(json);
		} catch (Exception e) {
			ErrorUtils.report(logger, e);
		}
    }
    
    /**
     * Writes the entries to the datastore previously
     * specified via STORE_TYPE.  If not set, 
     * defaults to json file.
     * @throws SQLException 
     */
    public void write() throws SQLException {
    	switch (this.storeType) {
			case FILE: {
				writeToFile();
				break;
			}
			case DB :  {
				writeToDb();
				break;
			}
    	}
    }
    

    /**
     * Normally, it is expected that the user of this class will
     * set the type to DB at the beginning.  In which case, 
     * an add will automatically write to the DB, eliminating
     * the need to call the writeToDb method.
     * @throws SQLException 
     */
    private void writeToDb() throws SQLException {
    	for (T value : table.values()) {
    		if (dbManager.contains(value.get_id())) {
    			dbManager.updateWhereEqual(value.toJsonObject());
    		} else {
    			dbManager.insert(value.toJsonObject());
    		}
    	}
    }
    
    /**
     * Write the entries to a json file on the disk.
     */
    private void writeToFile() {
    	if (this.storeType == STORE_TYPE.FILE) {
	    	try {
	    		FileUtils.writeFile(datastoreName, this.getAsJson(prettyPrint));
			} catch (Exception e) {
				ErrorUtils.report(logger, e);
			}
    	}
    }
    
    /**
     * Set the prettyprint option for saving json.  
     * @param value If true, the json will have tabs and linebreaks inserted so it is more readable for people.  If false, will be a single line.
     */
    public void setPrettyPrint(boolean value) {
    	this.prettyPrint = value;
    }
    
    /**
     * Set the name of the file in which to store the entries.
     * No need to include file extension.  Will be .json if a file.
     * Will be .db if a database.
     * @param name of file.  May include the path.  Otherwise, will save relative to the application.
     */
    public void setDataStoreName(String name) {
    	switch (this.storeType) {
    	case DB: {
    		this.datastoreName = name;
    		break;
    	}
    	case FILE:
        	if (name.endsWith(STORE_TYPE.FILE.extension)) {
            	this.datastoreName = name;
        	} else {
        		this.datastoreName = name + STORE_TYPE.FILE.extension;
        	}
    		break;
    	}
    }
    
    /**
     * If the datastore file exists, provides the full path to its location.
     * @return
     */
    public String getPathToDataStore() {
    	return FileUtils.getPathToFile(this.datastoreName);
    }
    
    public void setStoreType(STORE_TYPE type
    		, boolean deleteOldDataStore
    		, boolean deleteOldEntries) throws SQLException {
    	this.storeType = type;
    	switch (type) {
	    	case DB: {
	    		initializeDbManager(deleteOldDataStore, deleteOldEntries);
	    		break;
	    	}
	    	case FILE: {
	    		initializeFileStore(deleteOldDataStore);
	    		break;
	    	}
    	}
    }
    
    public STORE_TYPE getStoreType() {
    	return this.storeType;
    }
    
    private void initializeFileStore(boolean deleteFirst) {
    	// TODO
    }
    private void initializeDbManager(boolean deleteOldDb, boolean deleteOldRows) throws SQLException {
      	dbManager = 
    			DbConnectionFactory.getH2Manager(
    					this.datastoreName
    					, "JSON_MANAGER"
    					, deleteOldDb
    					, deleteOldRows
    					);
    }
    
    public void clear() {
    	table.clear();
    }
    
}

