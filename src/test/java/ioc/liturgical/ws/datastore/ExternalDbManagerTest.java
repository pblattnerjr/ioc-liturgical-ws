package ioc.liturgical.ws.datastore;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import ioc.liturgical.test.framework.LinkRefersToBiblicalTextTextFactory;
import ioc.liturgical.test.framework.TestUsers;
import ioc.liturgical.ws.constants.HTTP_RESPONSE_CODES;
import ioc.liturgical.ws.constants.ONTOLOGY_TOPICS;
import ioc.liturgical.ws.constants.RELATIONSHIP_TYPES;
import ioc.liturgical.ws.managers.databases.external.neo4j.ExternalDbManager;
import ioc.liturgical.ws.managers.databases.internal.InternalDbManager;
import ioc.liturgical.ws.models.RequestStatus;
import ioc.liturgical.ws.models.ResultJsonObjectArray;
import ioc.liturgical.ws.models.db.docs.Animal;
import ioc.liturgical.ws.models.db.docs.Being;
import ioc.liturgical.ws.models.db.docs.Concept;
import ioc.liturgical.ws.models.db.docs.Event;
import ioc.liturgical.ws.models.db.forms.AnimalCreateForm;
import ioc.liturgical.ws.models.db.forms.BeingCreateForm;
import ioc.liturgical.ws.models.db.forms.ConceptCreateForm;
import ioc.liturgical.ws.models.db.forms.EventCreateForm;
import ioc.liturgical.ws.models.db.forms.LinkRefersToBiblicalTextCreateForm;
import ioc.liturgical.ws.models.db.links.LinkRefersToBiblicalText;
import net.ages.alwb.utils.core.id.managers.IdManager;

public class ExternalDbManagerTest {

	private static InternalDbManager internalManager;
	private static ExternalDbManager externalManager;

	private static LinkRefersToBiblicalTextTextFactory testReferences;
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
		
		testReferences = new LinkRefersToBiblicalTextTextFactory();
		
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
		// if this fails, make sure you have run the CreateABunchOfReferences unit test first
	    	ResultJsonObjectArray result = 
	    			externalManager.getRelationshipForType(
	    					RELATIONSHIP_TYPES.REFERS_TO_BIBLICAL_TEXT.typename
	    					);
			assertTrue(result.getResultCount() > 0 && result.getStatus().getCode() == 200);
	    }

	@Test
	   public void testReferenceSearch() {
		// if this fails, make sure you have run the CreateABunchOfReferences unit test first
	    	JsonObject result = 
	    			externalManager.searchRelationships(
	    					RELATIONSHIP_TYPES.REFERS_TO_BIBLICAL_TEXT.typename
	    					, "en_us_pentiuc"
	    					, "syn"  // query
	    					, "syn" // property
	    					, "c" // matcher
	    					, "a,b" // tags
	    					, "or" // operator
	    					);
			assertTrue(result.get("status").getAsJsonObject().get("code").getAsInt() == 200);
	    }

	@Test
	   public void testRelationshipPropertiesMap() {
	    	ResultJsonObjectArray result = 
	    			externalManager.getRelationshipTypePropertyMaps();
			assertTrue(result.getStatus().getCode() == 200 && result.getResultCount() > 0);
	    }
	
	
	@Test
	   public void testRelationshipLabelsList() {
		// if this fails, make sure you have run the CreateABunchOfReferences unit test first
	    	JsonArray result = 
	    			externalManager.getRelationshipTags(RELATIONSHIP_TYPES.REFERS_TO_BIBLICAL_TEXT.typename);
			assertTrue(result.size() > 0);
	    }

	@Test
	   public void testRelationshipDomainsList() {
	    	JsonObject result = 
	    			externalManager.getRelationshipLibrarysForAllTypes() ;
			assertNotNull(result);
	    }

	@Test
	   public void testRelationshipDropdown() {
	    	ResultJsonObjectArray result = 
	    			externalManager.getRelationshipSearchDropdown();
			assertTrue(result.getStatus().getCode() == 200 && result.getResultCount() > 0);
	    }

	@Test
	   public void testGetUsers() {
			JsonObject result = externalManager.callDbmsSecurityListUsers();
			assertNotNull(result);
	    }
	

	@Test
	   public void testGetNewForms() {
			JsonObject result = externalManager.getNewDocForms(TestUsers.WS_ADMIN.id , "get new forms");
			assertNotNull(result);
	    }

	@Test
	   public void testGetConstraintsObject() {
			JsonObject result = externalManager.callDbConstraints();
			assertNotNull(result);
	    }
	
	@Test
	   public void testDbHasConstraint() {
			assertTrue(externalManager.dbHasConstraint("OntoRoot"));
	    }

	@Test
	   public void testGetConstraintsList() {
			List<String> result = externalManager.getDbConstraints();
			assertTrue(result.contains("Text"));
	    }

	@Test
	   public void testDbIsReadOnly() {
			assertFalse(externalManager.dbIsReadOnly());
	    }
	
	@Test
	   public void testOntologyCheck() {
			assertTrue(externalManager.dbHasOntologyEntries());
	    }
	
	@Test
	   public void testConstraintOnNodeCreateAndDrop() {
		String label = "banana";
		// create
			RequestStatus status = externalManager.createConstraintUniqueNodeId(label);
			assertTrue(status.getCode() == HTTP_RESPONSE_CODES.OK.code);
		// create again
			status = externalManager.createConstraintUniqueNodeId(label);
			assertTrue(status.getCode() == HTTP_RESPONSE_CODES.CONFLICT.code);
		// drop
			status = externalManager.dropConstraintUniqueNodeId(label);
			assertTrue(status.getCode() == HTTP_RESPONSE_CODES.OK.code);
		// drop again
			status = externalManager.dropConstraintUniqueNodeId(label);
			assertTrue(status.getCode() == HTTP_RESPONSE_CODES.BAD_REQUEST.code);
	}
	
	@Test
	   public void testCreateOntologyEntryCrudAnimal() {
			String name = "TestDove";
			String description = "A symbol of peace.";

			// create
			AnimalCreateForm form = new AnimalCreateForm(name);
			form.setDescription(description);
			RequestStatus result = 
					externalManager.addLTKDbObject(
							"wsadmin"
							, ONTOLOGY_TOPICS.ANIMAL
							, form.toJsonString()
							);
			assertTrue(result.getCode() == HTTP_RESPONSE_CODES.CREATED.code);
			
			// read
			JsonObject json = externalManager.getForId(form.getId());
			Animal entry = gson.fromJson(
					json.get("values")
					.getAsJsonArray()
					.get(0)
					.getAsJsonObject()
					, Animal.class
					);
			assertNotNull(entry.getId());
			assertNotNull(entry.getLibrary());
			assertNotNull(entry.getTopic());
			assertNotNull(entry.getKey());
			
			assertTrue(entry.getId().equals(form.getId()));
			assertTrue(entry.getLibrary().equals(form.getLibrary()));
			assertTrue(entry.getTopic().equals(form.getTopic()));
			assertTrue(entry.getKey().equals(form.getKey()));

			// update
			description = "this is an update to the comments";
			entry.setDescription(description);
			result = externalManager.updateLTKDbObject(
					"wsadmin"
					, entry.toJsonString()
					);
			
			assertTrue(result.getCode() == HTTP_RESPONSE_CODES.OK.code);
			
			// read again
			json = externalManager.getForId(form.getId());
			Animal revisedEntry = gson.fromJson(
					json.get("values")
					.getAsJsonArray()
					.get(0)
					.getAsJsonObject()
					, Animal.class
					);
			assertTrue(entry.getDescription().equals(revisedEntry.getDescription()));

			// delete
			result = externalManager.deleteForId(form.getId());
			assertTrue(result.getCode() == HTTP_RESPONSE_CODES.OK.code);
	}
	@Test
	   public void testCreateOntologyEntryCrudBeing() {
			String name = "TestAngel";
			String description = "A divine messenger.";

			// create
			Being form = new Being(name);
			form.setDescription(description);
			RequestStatus result = 
					externalManager.addLTKDbObject(
							"wsadmin"
							, ONTOLOGY_TOPICS.BEING
							, form.toJsonString()
							);
			assertTrue(result.getCode() == HTTP_RESPONSE_CODES.CREATED.code);
			
			// read
			JsonObject json = externalManager.getForId(form.getId());
			Being entry = gson.fromJson(
					json.get("values")
					.getAsJsonArray()
					.get(0)
					.getAsJsonObject()
					, Being.class
					);
			assertNotNull(entry.getId());
			assertNotNull(entry.getLibrary());
			assertNotNull(entry.getTopic());
			assertNotNull(entry.getKey());
			
			assertTrue(entry.getId().equals(form.getId()));
			assertTrue(entry.getLibrary().equals(form.getLibrary()));
			assertTrue(entry.getTopic().equals(form.getTopic()));
			assertTrue(entry.getKey().equals(form.getKey()));

			// update
			description = "this is an update to the comments";
			entry.setDescription(description);
			result = externalManager.updateLTKDbObject(
					"wsadmin"
					, entry.toJsonString()
					);
			
			assertTrue(result.getCode() == HTTP_RESPONSE_CODES.OK.code);
			
			// read again
			json = externalManager.getForId(form.getId());
			Being revisedEntry = gson.fromJson(
					json.get("values")
					.getAsJsonArray()
					.get(0)
					.getAsJsonObject()
					, Being.class
					);
			assertTrue(entry.getDescription().equals(revisedEntry.getDescription()));

			// delete
			result = externalManager.deleteForId(form.getId());
			assertTrue(result.getCode() == HTTP_RESPONSE_CODES.OK.code);
	}
	@Test
	   public void testCreateOntologyEntryCrudConcept() {
			String name = "TestTrinity";
			String description = "Theological assertion that the Godhead is three persons.";

			// create
			ConceptCreateForm form = new ConceptCreateForm(name);
			form.setDescription(description);
			RequestStatus result = 
					externalManager.addLTKDbObject(
							"wsadmin"
							, ONTOLOGY_TOPICS.CONCEPT
							, form.toJsonString()
							);
			assertTrue(result.getCode() == HTTP_RESPONSE_CODES.CREATED.code);
			
			// read
			JsonObject json = externalManager.getForId(form.getId());
			Concept entry = gson.fromJson(
					json.get("values")
					.getAsJsonArray()
					.get(0)
					.getAsJsonObject()
					, Concept.class
					);
			assertNotNull(entry.getId());
			assertNotNull(entry.getLibrary());
			assertNotNull(entry.getTopic());
			assertNotNull(entry.getKey());
			
			assertTrue(entry.getId().equals(form.getId()));
			assertTrue(entry.getLibrary().equals(form.getLibrary()));
			assertTrue(entry.getTopic().equals(form.getTopic()));
			assertTrue(entry.getKey().equals(form.getKey()));

			// update
			description = "this is an update to the comments";
			entry.setDescription(description);
			result = externalManager.updateLTKDbObject(
					"wsadmin"
					, entry.toJsonString()
					);
			
			assertTrue(result.getCode() == HTTP_RESPONSE_CODES.OK.code);
			
			// read again
			json = externalManager.getForId(form.getId());
			Concept revisedEntry = gson.fromJson(
					json.get("values")
					.getAsJsonArray()
					.get(0)
					.getAsJsonObject()
					, Concept.class
					);
			assertTrue(entry.getDescription().equals(revisedEntry.getDescription()));

			// delete
			result = externalManager.deleteForId(form.getId());
			assertTrue(result.getCode() == HTTP_RESPONSE_CODES.OK.code);
	}
	@Test
	   public void testCreateOntologyEntryCrudEvent() {
			String name = "TestBirthOfChrist";
			String description = "Commemorated Dec 25.";

			// create
			EventCreateForm form = new EventCreateForm(name);
			form.setDescription(description);
			RequestStatus result = 
					externalManager.addLTKDbObject(
							"wsadmin"
							, ONTOLOGY_TOPICS.EVENT
							, form.toJsonString()
							);
			assertTrue(result.getCode() == HTTP_RESPONSE_CODES.CREATED.code);
			
			// read
			JsonObject json = externalManager.getForId(form.getId());
			Event entry = gson.fromJson(
					json.get("values")
					.getAsJsonArray()
					.get(0)
					.getAsJsonObject()
					, Event.class
					);
			assertNotNull(entry.getId());
			assertNotNull(entry.getLibrary());
			assertNotNull(entry.getTopic());
			assertNotNull(entry.getKey());
			
			assertTrue(entry.getId().equals(form.getId()));
			assertTrue(entry.getLibrary().equals(form.getLibrary()));
			assertTrue(entry.getTopic().equals(form.getTopic()));
			assertTrue(entry.getKey().equals(form.getKey()));

			// update
			description = "this is an update to the comments";
			entry.setDescription(description);
			result = externalManager.updateLTKDbObject(
					"wsadmin"
					, entry.toJsonString()
					);
			
			assertTrue(result.getCode() == HTTP_RESPONSE_CODES.OK.code);
			
			// read again
			json = externalManager.getForId(form.getId());
			Event revisedEntry = gson.fromJson(
					json.get("values")
					.getAsJsonArray()
					.get(0)
					.getAsJsonObject()
					, Event.class
					);
			assertTrue(entry.getDescription().equals(revisedEntry.getDescription()));

			// delete
			result = externalManager.deleteForId(form.getId());
			assertTrue(result.getCode() == HTTP_RESPONSE_CODES.OK.code);
	}
}
