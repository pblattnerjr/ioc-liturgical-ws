package ioc.liturgical.ws.datastore;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import ioc.liturgical.test.framework.TestReferences;
import ioc.liturgical.test.framework.TestUsers;
import ioc.liturgical.ws.constants.RELATIONSHIP_TYPES;
import ioc.liturgical.ws.managers.databases.external.neo4j.ExternalDbManager;
import ioc.liturgical.ws.managers.databases.internal.InternalDbManager;
import ioc.liturgical.ws.models.RequestStatus;
import ioc.liturgical.ws.models.ResultJsonObjectArray;
import ioc.liturgical.ws.models.db.forms.LinkRefersToBiblicalTextCreateForm;
import ioc.liturgical.ws.models.db.links.LinkRefersToBiblicalText;
import net.ages.alwb.utils.core.id.managers.IdManager;

public class ExternalDbManagerTest {

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
				, false // not read only
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
		   LinkRefersToBiblicalTextCreateForm form = testReferences.getCreateForm(0);
		   RequestStatus status = externalManager.addReference(
	    			TestUsers.WS_ADMIN.id
	    			, form.toJsonString()
	    			);
	    	assertTrue(status.getCode() == 201); // created
	    	
	    	// read
	    	ResultJsonObjectArray result = externalManager.getReferenceObjectByRefId(
	    			testReferences.getCreateForm(0).getId()
	    			);
			LinkRefersToBiblicalText ref = (LinkRefersToBiblicalText) gson.fromJson(
					result.getValues().get(0)
					, LinkRefersToBiblicalText.class
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
	    	status = externalManager.deleteRelationshipForId(ref.getId());
	    	assertTrue(status.getCode() == 200);
	    }
	
	@Test
	   public void testReferenceGetForType() {
	    	ResultJsonObjectArray result = 
	    			externalManager.getRelationshipForType(
	    					RELATIONSHIP_TYPES.REFERS_TO_BIBLICAL_TEXT.typename
	    					);
			assertTrue(result.getResultCount() > 0 && result.getStatus().getCode() == 200);
	    }

	@Test
	   public void testReferenceSearch() {
	    	ResultJsonObjectArray result = 
	    			externalManager.searchRelationships(
	    					RELATIONSHIP_TYPES.REFERS_TO_BIBLICAL_TEXT.typename
	    					, "en_us_pentiuc"
	    					, "a,b" // labels
	    					, "or" // operator
	    					);
			assertTrue(result.getResultCount() > 0 && result.getStatus().getCode() == 200);
	    }

	@Test
	   public void testRelationshipPropertiesMap() {
	    	ResultJsonObjectArray result = 
	    			externalManager.getRelationshipTypePropertyMaps();
			assertTrue(result.getStatus().getCode() == 200 && result.getResultCount() > 0);
	    }
	
	
	@Test
	   public void testRelationshipLabelsList() {
	    	JsonArray result = 
	    			externalManager.getRelationshipLabels(RELATIONSHIP_TYPES.REFERS_TO_BIBLICAL_TEXT.typename);
			assertTrue(result.size() > 0);
	    }

	@Test
	   public void testRelationshipDropdown() {
	    	ResultJsonObjectArray result = 
	    			externalManager.getRelationshipSearchDropdown();
			assertTrue(result.getStatus().getCode() == 200 && result.getResultCount() > 0);
	    }

}
