package net.ages.alwb.utils.core.datastores.db.h2.examples;

import java.util.ArrayList;
import java.util.List;

import org.ocmc.ioc.liturgical.schemas.models.db.internal.TKVJsonObject;
import org.ocmc.ioc.liturgical.schemas.models.db.internal.TKVString;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import net.ages.alwb.utils.core.datastores.db.factory.DbConnectionFactory;
import net.ages.alwb.utils.core.datastores.db.h2.manager.H2ConnectionManager;
import net.ages.alwb.utils.core.datastores.db.h2.queries.QueryForJson;


// H2 Database Example

public class ExampleJsonDbWithJsonValue {

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

        	/**
        	 * First we create the value, with extends AbstractModel
        	 */
        	
        	ExampleUser user = new ExampleUser();
        	user.setFirstname("Sam");
        	user.setSurname("McBride");
        	
        	/**
        	 * Next, create an instance of TKVJsonObject
        	 */
        	TKVJsonObject theObject = new TKVJsonObject(
        			"_users" 			// topic
        			, "smcbride"	// key
        			, user.toJsonObject()
        			);
        	
        	manager.insert(theObject.toJsonObject());

        	user.setFirstname("Jim");
        	user.setSurname("Smith");
        	
        	theObject = new TKVJsonObject(
        			"_users" 			// topic
        			, "jsmith"	// key
        			, user.toJsonObject()
        			);
        	
        	manager.insert(theObject.toJsonObject());

        	theObject = new TKVJsonObject(
        			"_users" 			// topic
        			, "jsmyth"	// key
        			, user.toJsonObject()
        			);

        	manager.insert(theObject.toJsonObject());

        	theObject = new TKVJsonObject(
        			"_users" 			// topic
        			, "hsmith"	// key
        			, user.toJsonObject()
        			);

        	manager.insert(theObject.toJsonObject());

        	
        	System.out.println("Getting all docs in the table...");
        	// version without a WHERE clause
        	List<JsonObject> jsonList = manager.queryForJson();  

        	for (JsonObject json : jsonList) {
        		System.out.println(json.toString());
        	}

        	System.out.println("Getting only the doc whose id = _users|smcbride...");
        	jsonList = manager.queryForJsonWhereEqual("_users|smcbride");
 
        	// should only get one back
        	for (JsonObject json : jsonList) {
        		System.out.println(json.toString());
        	}

        	// now reconstitute it!
        	JsonObject jsonObject = jsonList.get(0);
        	theObject = new TKVJsonObject(jsonList.get(0));
        	ExampleUser reborn = (ExampleUser) theObject.getInstance(
        			ExampleUser.class);
        	System.out.println("The user is reborn! Name: " + reborn.firstname + " " + reborn.surname);
        	
        	System.out.println("Getting all docs whose id starts with _users|...");
        	jsonList = manager.queryForJsonWhereLike("_users");

        	// should only get one back
        	for (JsonObject json : jsonList) {
        		System.out.println(json.toString());
        	}

        	System.out.println("RegEx - Getting all docs whose id contains smy...");
        	jsonList = manager.queryForJsonWhereIdRegEx(".*smy");

        	for (JsonObject json : jsonList) {
        		System.out.println(json.toString());
        	}

        	System.out.println("RegEx - Getting all docs whose value contains Sam...");
        	jsonList = manager.queryForJsonWhereValueRegEx(".*Sam");

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

