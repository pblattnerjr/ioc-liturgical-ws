package net.ages.alwb.utils.core.datastores.db.h2.examples;

import java.util.ArrayList;
import java.util.List;

import org.ocmc.ioc.liturgical.schemas.models.db.internal.TKVString;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import net.ages.alwb.utils.core.datastores.db.factory.DbConnectionFactory;
import net.ages.alwb.utils.core.datastores.db.h2.manager.H2ConnectionManager;
import net.ages.alwb.utils.core.datastores.db.h2.queries.QueryForJson;


// H2 Database Example

public class ExampleJsonDb3WriteABunch {

    public static void main(String[] args) throws Exception {
    	insertWithPreparedStatement();
    }

    // H2 SQL Prepared Statement Example
    private static void insertWithPreparedStatement() {

    	try {
        	
        	H2ConnectionManager manager = 
        			DbConnectionFactory.getH2Manager(
        					"example"
        					, "JSON_gr_gr_cog"
        					, false
        					, false
        					);

        	// create the table.  Will only happen if doesn't already exist.
        	manager.createTable();
        	// in case it did exist, for this example, we will get rid of the rows.
        	manager.truncateTable();
        	
        	List<JsonObject> list = new ArrayList<JsonObject>();

        	String key = "data|key";
        	
        	for (int i=0; i < 1000000; i++) {
        		manager.insert(
            			getJson(
            					key + i
            					, "Τῆς Παναγίας ἀχράντου, ὑπερευλογημένης, ἐνδόξου Δεσποίνης ἡμῶν Θεοτόκου καὶ ἀειπαρθένου Μαρίας μετὰ πάντων τῶν Ἁγίων μνημονεύσαντες, ἑαυτοὺς καὶ ἀλλήλους καὶ πᾶσαν τὴν ζωὴν ἡμῶν Χριστῷ τῷ Θεῷ παραθώμεθα."
            			)
            	);
        	}

        	System.out.println("Getting all docs in the table...");
        	// version without a WHERE clause
        	List<JsonObject> jsonList = manager.queryForJson();  
        	System.out.println("Count of retrieved docs is: " + jsonList.size());
        	
        } catch (Exception e) {
        	e.printStackTrace();
        }
   }
    
    private static JsonObject getJson(String id, String value) {
    	TKVString json = new TKVString();
    	json.set_id(id);
    	json.setValue(value);
    	return json.toJsonObject();
    }
    
}

