package ioc.liturgical.ws.constants;

import static org.junit.Assert.*;

import org.junit.Test;
import org.ocmc.ioc.liturgical.schemas.constants.TOPICS;

public class TOPICSTest {

	@Test
	public void testGetSubRoot() {
		TOPICS plant = TOPICS.PLANT;
		TOPICS root = TOPICS.getSubRoot(plant);
		assertTrue(root == TOPICS.ONTOLOGY_ROOT);
	}

}
