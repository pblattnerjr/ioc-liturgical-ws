package ioc.liturgical.ws.constants.db.external;

import static org.junit.Assert.*;

import org.junit.Test;

public class TOPICSTest {

	@Test
	public void test() {
		String labels = TOPICS.ANIMAL.toDelimitedLabels();
		assertTrue(labels.equals("Root:OntologyRoot:Being:Animal"));
	}

	@Test
	public void testRoot() {
		for (TOPICS topic : TOPICS.values()) {
			String labels = topic.toDelimitedLabels();
			assertTrue(labels.startsWith("Root"));
		}
	}
}
