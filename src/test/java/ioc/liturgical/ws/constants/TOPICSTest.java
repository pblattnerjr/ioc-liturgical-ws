package ioc.liturgical.ws.constants;

import static org.junit.Assert.*;

import org.junit.Test;

import ioc.liturgical.ws.constants.db.external.TOPICS;

public class TOPICSTest {

	@Test
	public void testGetSubRoot() {
		TOPICS plant = TOPICS.PLANT;
		TOPICS root = TOPICS.getSubRoot(plant);
		assertTrue(root == TOPICS.ONTOLOGY_ROOT);
	}

}
