package ioc.liturgical.ws.constants;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import ioc.liturgical.ws.constants.db.external.TOPICS;

public class ONTOLOGY_TOPICSTest {

	@Test
	public void testAnimal() {
		String labels = TOPICS.ANIMAL.toDelimitedLabels();
		assertTrue(labels.equals("OntoRoot:Being:Animal"));
	}

	@Test
	public void testBeing() {
		String labels = TOPICS.BEING.toDelimitedLabels();
		assertTrue(labels.equals("OntoRoot:Being"));
	}

	@Test
	public void testConcept() {
		String labels = TOPICS.CONCEPT.toDelimitedLabels();
		assertTrue(labels.equals("OntoRoot:Concept"));
	}

	@Test
	public void testEvent() {
		String labels = TOPICS.EVENT.toDelimitedLabels();
		assertTrue(labels.equals("OntoRoot:Event"));
	}
	
	@Test
	public void testHuman() {
		String labels = TOPICS.HUMAN.toDelimitedLabels();
		assertTrue(labels.equals("OntoRoot:Being:Human"));
	}
	@Test
	public void testHumanList() {
		List<String> labels = TOPICS.HUMAN.toLabelsList();
		assertTrue(labels.get(0).equals("OntoRoot") 
				&& labels.get(1).contains("Being") 
				&& labels.get(2).equals("Human"));
	}

	@Test
	public void testPlace() {
		String labels = TOPICS.PLACE.toDelimitedLabels();
		assertTrue(labels.equals("OntoRoot:Place"));
	}
	@Test
	public void testPlant() {
		String labels = TOPICS.PLANT.toDelimitedLabels();
		assertTrue(labels.equals("OntoRoot:Being:Plant"));
	}
	
}
