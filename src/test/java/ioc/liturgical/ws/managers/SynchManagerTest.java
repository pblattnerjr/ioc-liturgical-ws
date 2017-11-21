package ioc.liturgical.ws.managers;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import ioc.liturgical.test.framework.LinkRefersToBiblicalTextTextFactory;
import ioc.liturgical.test.framework.TestUsers;
import ioc.liturgical.ws.constants.db.external.TOPICS;
import ioc.liturgical.ws.managers.databases.external.neo4j.ExternalDbManager;
import ioc.liturgical.ws.managers.databases.internal.InternalDbManager;
import ioc.liturgical.ws.managers.synch.SynchManager;
import ioc.liturgical.ws.models.RequestStatus;
import ioc.liturgical.ws.models.ResultJsonObjectArray;
import ioc.liturgical.ws.models.db.docs.ontology.Being;

public class SynchManagerTest {

	private static InternalDbManager internalManager;
	private static ExternalDbManager externalManager;
	private static SynchManager synchManager;

	private static LinkRefersToBiblicalTextTextFactory testReferences;
	private static String pwd = "";
	private static Gson gson = new Gson();
	private static Being form = null;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		pwd = System.getenv("pwd");
		
		internalManager = new InternalDbManager(
				"test-db" // store name
				, "json" // tablename
				, true // delete old database
				, true // truncate tables (if you don't delete the old db)
				, true // create test users
				, TestUsers.WS_ADMIN.id // the username of the wsadmin
				, pwd
				);

		externalManager = new ExternalDbManager(
				"localhost"
				, "bolt://127.0.0.1:7687"
				, true
				, true
				, TestUsers.WS_ADMIN.id
				, pwd
				, false // do not build domainMap
				, false // not read only
				, internalManager
				);
		
		// do a clean up in case we aborted during a previous run without deleting Test entries
		// 1. Get a list of ids for ontology instances we created
		ResultJsonObjectArray resultArray = externalManager.getForQuery("match (n:" + TOPICS.ONTOLOGY_ROOT.label + ") where n.name starts with \"Test\" return n.id", false, false);
		// 2. Delete them
		externalManager.getForQuery("match (n:" + TOPICS.ONTOLOGY_ROOT.label + ") where n.name starts with \"Test\" delete(n)", false, false);
		// 3. Delete their transactions
		for (JsonObject o : resultArray.values) {
			externalManager.getForQuery("match (n:Transaction) where n.topic = " + o.get("id") + " delete(n)", false, false);
		}
		
		/**
		 * TODO:
		 * 1. in Neo4jConnectionManager, write transactions for all types of create, update, delete
		 *     including nodes and relationships.
		 *  2. Write test cases here to verify transactions written for create update delete of 
		 *       TestAngel
		 */
		// create the test data
		String name = "TestAngel";
		String description = "A divine messenger.";
		
		// create
		form = new Being(name);
		form.setDescription(description);
		RequestStatus result = 
				externalManager.addLTKDbObject(
						"wsadmin"
						, form.toJsonString()
						);
		
		// read
		ResultJsonObjectArray json = externalManager.getForId(form.getId());
		Being entry = gson.fromJson(
				json.getFirstObject()
				, Being.class
				);

		// update
		description = "this is an update to the comments";
		entry.setDescription(description);
		result = externalManager.updateLTKDbObject(
				"wsadmin"
				, entry.toJsonString()
				);
		
		// delete
		result = externalManager.deleteForId(form.getId());
		
		synchManager = new SynchManager(
				"localhost"
				, "8687"
				, "wsadmin"
				, pwd
				);
		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}
	
	@Test
	   public void testGetTransactionsById() { 
			ResultJsonObjectArray result = 
					synchManager.getTransactionsForId(
							"wsadmin"
							, form.getId()
							);
			assertTrue(result.valueCount > 0);
	    }
	
}
