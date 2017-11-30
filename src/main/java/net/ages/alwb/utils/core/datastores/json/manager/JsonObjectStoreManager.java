package net.ages.alwb.utils.core.datastores.json.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.io.File;
import java.lang.reflect.Type;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import ioc.liturgical.ws.constants.Constants;
import net.ages.alwb.utils.core.datastores.db.factory.DbConnectionFactory;
import net.ages.alwb.utils.core.datastores.db.h2.manager.H2ConnectionManager;
import net.ages.alwb.utils.core.datastores.json.models.TKVJsonObject;
import net.ages.alwb.utils.core.error.handling.ErrorUtils;
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
 */
public class JsonObjectStoreManager  {
	private static final Logger logger = LoggerFactory
			.getLogger(ErrorUtils.class);

	/*
	 *  com.google.common.collect.Table<R,C,V>
	 *  R = row
	 *  C = Column
	 *  V = Value
	 */
	private Table<String,String,TKVJsonObject> table = HashBasedTable.create();
	
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
	public JsonObjectStoreManager() {
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
	public void add(TKVJsonObject value) throws SQLException {
		if (this.storeType == STORE_TYPE.DB) {
			if (dbManager.contains(value.get_id())) {
				dbManager.updateWhereEqual(value.toJsonObject());
			} else {
				dbManager.insert(value.toJsonObject());
			}
		}
		table.put(
				value.getKey() 	// row key
				, value.getTopic() // column key
				, value 
				);
	}

	/**
	 * Wrapper to put item in the table.
	 * @param value
	 */
	private void put(TKVJsonObject value) {
		table.put(value.getKey(), value.getTopic(), value);
	}

	/**
	 * Add each value in the list
	 * @param list
	 * @throws SQLException 
	 */
	public void add(List<TKVJsonObject> list) throws SQLException {
		for (TKVJsonObject value : list) {
			add(value);
		}
	}
	
	/**
	 * Get a map of entries for the specified topic.
	 * @param topic
	 * @return
	 */
    public Map<String,TKVJsonObject> getTopicMap(String topic) {
    	return table.column(topic);
    }
    
    /**
     * Get a collection of entries for the given topic.
     * @param topic
     * @return
     */
    public Collection<TKVJsonObject> getTopicCollection(String topic) {
    	return getTopicMap(topic).values();
    }
    
    public JsonArray getTopicsAsJsonObjectFiltered(
    		String startsWith // only return topics that start with this string
    		) {
    	JsonArray result = new JsonArray();
    	Set<String> topicKeys = table.columnKeySet();
    	for (String topicKey : topicKeys) {
    		if (topicKey.startsWith(startsWith)) {
    	    	Collection<TKVJsonObject> topics = getTopicCollection(topicKey);
    	    	for (TKVJsonObject json : topics) {
    	    		if (json.getTopic().startsWith(startsWith)) {
    	        		result.add(json.toJsonObject());
    	    		}
    	    	}
    		}
    	}
    	return result;
    }

    public JsonArray getTopicsAsJsonObject(String topic) {
    	JsonArray result = new JsonArray();
    	Collection<TKVJsonObject> topics = getTopicCollection(topic);
    	for (TKVJsonObject json : topics) {
    		result.add(json.toJsonObject());
    	}
    	return result;
    }
    
    /**
     * Get a specific entry
     * @param topic
     * @param key
     * @return
     */
    public TKVJsonObject getEntry(String topic, String key) {
    	return table.get(key, topic);
    }
    
    /**
     * If supplied a class that has @Expose annotations for gson, and 
     * the saved value is json that maps to that class, returns an instance
     * after having done a look up by topic and key.
     * @param c the class for which to get an instance from the json value
     * @param topic of the entry to find
     * @param key of the entry to find
     * @return the value of the entry as an instance of the requested class.
     */
	@SuppressWarnings("unchecked")
	public Object getInstance(@SuppressWarnings("rawtypes") Class c, String topic, String key) {
		Object result = null;
		try {
			TKVJsonObject entry = getEntry(topic, key);
			String json = null;
			if (entry.getValue().has("value")) {
				json = entry.getValue().get("value").toString();
			} else {
				json = entry.getValue().toString();
			}
			result = gson.fromJson(json, c);
		} catch (Exception e) {
			result = null;
		}
		return result;
	}

    public Boolean isTrue(String topic, String key) {
        return getEntry(topic,key).getValue().getAsBoolean();
    }

    /**
     * Remove the indicated topic and key combination
     * @param topic
     * @param key
     */
    public void remove (String topic, String key) {
    	try {
    		TKVJsonObject entry = getEntry(topic, key);
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
    public Map<String,TKVJsonObject> getKeyMap(String key) {
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
        	Collection<TKVJsonObject> entries = table.values();
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
    		List<TKVJsonObject> entries = jsonToList(json);
    		if (entries != null && entries.size() > 0) {
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
    public List<TKVJsonObject> jsonToList(String json) {
    	List<TKVJsonObject> result = new ArrayList<TKVJsonObject>();
    	List<JsonObject> jsonList;
    	Type listType = new TypeToken<List<JsonObject>>() {}.getType();
    	try {
        	jsonList = 
        			gson.fromJson(
        					json
        					, listType
        	);
        	if (jsonList != null) {
            	for (JsonObject o : jsonList) {
            		result.add(new TKVJsonObject(o));
            	}
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
    		case MEMORY: {break;}
    	}
    }
    
    private void loadFromDb() throws SQLException {
    	List<JsonObject> list = dbManager.queryForJson();
    	for (JsonObject json : list) {
    		put(new TKVJsonObject(json));
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
			case MEMORY: {break;}
    	}
    }
    
    public boolean hasValues() {
    	return table.size() > 0;
    }

    /**
     * Normally, it is expected that the user of this class will
     * set the type to DB at the beginning.  In which case, 
     * an add will automatically write to the DB, eliminating
     * the need to call the writeToDb method.
     * @throws SQLException 
     */
    private void writeToDb() throws SQLException {
    	for (TKVJsonObject value : table.values()) {
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
		case MEMORY:
			break;
		default:
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
		case MEMORY:
			break;
		default:
			break;
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

