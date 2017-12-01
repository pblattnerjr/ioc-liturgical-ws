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

public class ExampleJsonDb {

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
        	
        	list.add(
        			getJson(
        					"actors|Deacon"
        					, "καλημέρα!"
        			)
        	);
        	list.add(
        			getJson(
        					"actors|Priest"
        					, "Δόχα τω Θεό!"
        			)
        	);
        	list.add(
        			getJson(
        					"titles|Liturgy"
        					, "The Divine Liturgy"
        			)
        	);
        	
        	manager.insert(list);

        	System.out.println("Getting all docs in the table...");
        	// version without a WHERE clause
        	List<JsonObject> jsonList = manager.queryForJson();  

        	for (JsonObject json : jsonList) {
        		System.out.println(json.toString());
        	}

        	System.out.println("Getting only the doc whose id = actors|Deacon...");
        	jsonList = manager.queryForJsonWhereEqual("actors|Deacon");
 
        	// should only get one back
        	for (JsonObject json : jsonList) {
        		System.out.println(json.toString());
        	}

        	System.out.println("Getting all docs whose id starts with actors|...");
        	jsonList = manager.queryForJsonWhereLike("actors");

        	// should only get one back
        	for (JsonObject json : jsonList) {
        		System.out.println(json.toString());
        	}

        	System.out.println("Getting all docs whose id contains Priest...");
        	jsonList = manager.queryForJsonWhereLike("Priest");

        	for (JsonObject json : jsonList) {
        		System.out.println(json.toString());
        	}
        	
        	
        	System.out.println("Before Update: Getting all docs whose id starts with titles|...");
        	jsonList = manager.queryForJsonWhereStartsWith("titles");

        	for (JsonObject json : jsonList) {
        		System.out.println(json.toString());
        	}
        	JsonObject newJson =         			
        			getJson(
					"titles|Liturgy"
					, "The Divine Liturgy is my favorite service!"
			);

        	manager.updateWhereEqual(newJson);

        	System.out.println("After Update: Getting all docs whose id starts with titles|...");
        	jsonList = manager.queryForJsonWhereStartsWith("titles");
 
        	for (JsonObject json : jsonList) {
        		System.out.println(json.toString());
        	}
        
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

