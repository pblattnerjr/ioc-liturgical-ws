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
import ioc.liturgical.ws.models.db.forms.ReferenceCreateForm;
import net.ages.alwb.utils.core.id.managers.IdManager;

public class ExternalDbManagerTest {

	private static InternalDbManager internalManager;
	private static ExternalDbManager externalManager;

	private static TestReferences testReferences;
	private static String pwd = "";
	
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
		   ReferenceCreateForm form = testReferences.getCreateForm(0);
			IdManager fromIdManager = new IdManager(form.getIdReferredByText());
			IdManager toIdManager = new IdManager(form.getIdReferredToText());
		   RequestStatus status = externalManager.addReference(
	    			TestUsers.WS_ADMIN.id
	    			, fromIdManager.get(0)
	    			, fromIdManager.get(1)
	    			, fromIdManager.get(2)
	    			, toIdManager.get(0)
	    			, toIdManager.get(1)
	    			, toIdManager.get(2)
	    			, form.toJsonString()
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
	    	externalManager.updateReference(
	    			TestUsers.WS_ADMIN.id
	    			, ref.getId()
	    			, ref.toJsonString()
	    			);
	    	ref = externalManager.getReference(ref.getId());
	    	assertTrue(ref.getBib().equals(update));
	    	
	    	// delete
	    	status = externalManager.deleteForId(ref.getId());
	    	assertTrue(status.getCode() == 200);
	    }
}
