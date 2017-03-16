package ioc.liturgical.ws.datastore;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import ioc.liturgical.test.framework.TestReferences;
import ioc.liturgical.test.framework.TestUsers;
import ioc.liturgical.ws.managers.databases.external.neo4j.ExternalDbManager;
import ioc.liturgical.ws.managers.databases.internal.InternalDbManager;
import ioc.liturgical.ws.models.RequestStatus;
import ioc.liturgical.ws.models.db.docs.Reference;

public class ExternalDbManagerTest {

	private static InternalDbManager internalManager;
	private static ExternalDbManager externalManager;

	private static TestReferences testReferences;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		String pwd = System.getenv("pwd");
		
		internalManager = new InternalDbManager(
				"test-db" // store name
				, "json" // tablename
				, true // delete old database
				, true // truncate tables (if you don't delete the old db)
				, true // create test users
				, TestUsers.WS_ADMIN.id // the username of the wsadmin
				);

		externalManager = new ExternalDbManager(
				"localhost"
				, true
				, true
				, TestUsers.WS_ADMIN.id
				, pwd
				, false // do not build domainMap
				, internalManager
				);
		
		testReferences = new TestReferences();
		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}
	
	@Test
	   public void testGetForIdStartswith() {
			assertTrue(externalManager.getForIdStartsWith("gr_gr_cog~actors").get("valueCount").getAsInt() > 0);
	    }
	
	@Test
	   public void testGetForId() {
    		assertTrue(externalManager.getForId("gr_gr_cog~actors~ClergyAndPeople").get("valueCount").getAsInt() == 1);
	    }
	
	@Test
	   public void testReferenceCrud() {
		
		// create
	    	RequestStatus status = externalManager.addReference(
	    			TestUsers.WS_ADMIN.id
	    			, testReferences.getCreateForm(0).toJsonString()
	    			);
	    	assertTrue(status.getCode() == 201); // created
	    	
	    	// read
	    	Reference ref = externalManager.getReference(
	    			testReferences.getCreateForm(0).getId()
	    			);
	    	assertTrue(ref.getId().equals(testReferences.getCreateForm(0).getId()));
	    	
	    	// update
	    	String update = "updated Bib";
	    	ref.setBib(update);
	    	externalManager.updateReference(TestUsers.WS_ADMIN.id, ref.getId(), ref.toJsonString());
	    	ref = externalManager.getReference(ref.getId());
	    	assertTrue(ref.getBib().equals(update));
	    	
	    	// delete
	    	status = externalManager.deleteForId(ref.getId());
	    	assertTrue(status.getCode() == 200);
	    }
}
