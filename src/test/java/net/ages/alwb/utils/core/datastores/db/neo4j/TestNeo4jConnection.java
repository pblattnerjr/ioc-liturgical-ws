package net.ages.alwb.utils.core.datastores.db.neo4j;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import ioc.liturgical.ws.app.ServiceProvider;
import org.ocmc.ioc.liturgical.schemas.models.ws.response.RequestStatus;

public class TestNeo4jConnection {
	
	public static String uid = "";
	public static String pwd = "";
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		uid = System.getenv("uid");
		pwd = System.getenv("pwd");
	}

	@Test
	public void test() {
		Db2Db db2db = new Db2Db(
				Db2Db.toBoltLocalUrl("7688")
				, uid
				, pwd
				, Db2Db.toBoltLocalUrl("7687")
				, uid
				, pwd
				);
		assertTrue(db2db.getFromDb().isConnectionOK());
		assertTrue(db2db.getToDb().isConnectionOK());
//		RequestStatus result = db2db.test();
		RequestStatus result = db2db.transferWordNet(
	        	false // copyFromDbNodes
	        	, true // copyFromDbLinks
	        	, false // createLexicalForms
	        	, false // createLinksFromLexicalFormToLexicalEntry
	        	, false // createLinksFromLexicalFormToSynset
				);
		assertTrue(result.code == 200);
	}

}
