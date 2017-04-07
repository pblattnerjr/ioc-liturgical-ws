package ioc.liturgical.ws.constants;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class ONTOLOGY_TOPICSTest {

	@Test
	public void testAnimal() {
		String labels = ONTOLOGY_TOPICS.ANIMAL.toDelimitedLabels();
		assertTrue(labels.equals("OntoRoot:Being:Animal"));
	}

	@Test
	public void testBeing() {
		String labels = ONTOLOGY_TOPICS.BEING.toDelimitedLabels();
		assertTrue(labels.equals("OntoRoot:Being"));
	}

	@Test
	public void testConcept() {
		String labels = ONTOLOGY_TOPICS.CONCEPT.toDelimitedLabels();
		assertTrue(labels.equals("OntoRoot:Concept"));
	}

	@Test
	public void testEvent() {
		String labels = ONTOLOGY_TOPICS.EVENT.toDelimitedLabels();
		assertTrue(labels.equals("OntoRoot:Event"));
	}
	
	@Test
	public void testHuman() {
		String labels = ONTOLOGY_TOPICS.HUMAN.toDelimitedLabels();
		assertTrue(labels.equals("OntoRoot:Being:Human"));
	}
	@Test
	public void testHumanList() {
		List<String> labels = ONTOLOGY_TOPICS.HUMAN.toLabelsList();
		assertTrue(labels.get(0).equals("OntoRoot") 
				&& labels.get(1).contains("Being") 
				&& labels.get(2).equals("Human"));
	}

	@Test
	public void testPlace() {
		String labels = ONTOLOGY_TOPICS.PLACE.toDelimitedLabels();
		assertTrue(labels.equals("OntoRoot:Place"));
	}
	@Test
	public void testPlant() {
		String labels = ONTOLOGY_TOPICS.PLANT.toDelimitedLabels();
		assertTrue(labels.equals("OntoRoot:Being:Plant"));
	}
	
}
