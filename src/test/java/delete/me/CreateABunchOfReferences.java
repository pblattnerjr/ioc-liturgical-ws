package delete.me;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.Gson;

import ioc.liturgical.test.framework.TestReferences;
import ioc.liturgical.test.framework.TestUsers;
import ioc.liturgical.ws.managers.databases.external.neo4j.ExternalDbManager;
import ioc.liturgical.ws.managers.databases.internal.InternalDbManager;
import ioc.liturgical.ws.models.RequestStatus;
import ioc.liturgical.ws.models.ResultJsonObjectArray;
import ioc.liturgical.ws.models.db.docs.Reference;
import ioc.liturgical.ws.models.db.forms.ReferenceCreateForm;
import net.ages.alwb.utils.core.id.managers.IdManager;

public class CreateABunchOfReferences {

	private static InternalDbManager internalManager;
	private static ExternalDbManager externalManager;

	private static TestReferences testReferences;
	private static String pwd = "";
	private static Gson gson = new Gson();
	
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
	   public void testCreateRefs() {
		
		// create
		for (int i=1; i < 5; i++) {
		   ReferenceCreateForm form = testReferences.getCreateForm(i);
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
		}
	}
}
