package ioc.liturgical.ws.models.db;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.Gson;

import ioc.liturgical.ws.models.db.docs.ontology.Animal;
import ioc.liturgical.ws.models.db.forms.AnimalCreateForm;

public class AbstractModelCastingTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}
	
	@Test
	   public void testSerializeDeserialize() {
		Gson gson = new Gson();
		String name = "Dove";
		String comments = "This is peaceful bird!";
		AnimalCreateForm a = new AnimalCreateForm(name);
		a.setDescription(comments);
		String json = a.toJsonString();
		Animal dove = gson.fromJson(json, Animal.class);
		assertTrue(dove.getDescription().equals(comments));
		assertTrue(dove.getName().equals(name));
	    }
	
}
