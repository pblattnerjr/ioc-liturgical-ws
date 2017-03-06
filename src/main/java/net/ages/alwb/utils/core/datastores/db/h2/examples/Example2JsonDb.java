package net.ages.alwb.utils.core.datastores.db.h2.examples;

import java.util.List;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import net.ages.alwb.utils.core.datastores.db.h2.manager.H2ConnectionManager;
import net.ages.alwb.utils.core.datastores.db.h2.queries.QueryForJson;


// H2 Database Example

public class Example2JsonDb {

    public static void main(String[] args) throws Exception {
    	insertWithPreparedStatement();
    }

    // H2 SQL Prepared Statement Example
    private static void insertWithPreparedStatement() {

    	boolean delete = false;
    	String tbName = "JSON_gr_gr_cog";                                                                                                                                                                                                                                    
    	
    	H2ConnectionManager manager = new H2ConnectionManager("example", delete);
    
    	QueryForJson query = new QueryForJson(tbName);
        
        try {
        	
        	System.out.println("Getting all docs whose id starts with actors|...");
        	List<JsonObject> jsonList = manager.queryForJson(
        			query.getSelectQueryWhereLike() // version with a WHERE like clause
        			, "actors|%"); // the id value.  Note the %.  Means, match up to this point.

        	// should only get one back
        	for (JsonObject json : jsonList) {
        		System.out.println(json.toString());
        	}

        	System.out.println("Getting all docs whose id contains Priest...");
        	jsonList = manager.queryForJson(
        			query.getSelectQueryWhereLike() // version with a WHERE like clause
        			, "%Priest%"); // the id value.  Note the %.  

        	for (JsonObject json : jsonList) {
        		System.out.println(json.toString());
        	}
        
        } catch (Exception e) {
        	e.printStackTrace();
        }
   }
    
    private static JsonObject getJson(String id, String value) {
    	JsonObject json = new JsonObject();
        json.add("_id", new JsonPrimitive(id));
        json.add("value", new JsonPrimitive(value));
    	return json;
    }
    
}

