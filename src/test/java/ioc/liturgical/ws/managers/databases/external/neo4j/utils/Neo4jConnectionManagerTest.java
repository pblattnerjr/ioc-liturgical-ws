package ioc.liturgical.ws.managers.databases.external.neo4j.utils;

import static org.junit.Assert.*;
import ioc.liturgical.ws.managers.databases.external.neo4j.utils.Neo4jConnectionManager;
import org.junit.Test;

public class Neo4jConnectionManagerTest {

	@Test
	public void test() {
		Neo4jConnectionManager c = new Neo4jConnectionManager(
				  "ioc-liturgical-db.net:7687"
				  , "wsadmin"
				  , ">ggog?vGxxRra7x8#VEcreov>vE7MX"
				  , false
				);
		assertTrue(c.isConnectionOK());
	}

}
