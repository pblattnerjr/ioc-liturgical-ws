package ioc.liturgical.ws.managers;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import org.ocmc.ioc.liturgical.schemas.constants.VERBS;
import ioc.liturgical.ws.managers.auth.UserStatus;
import ioc.liturgical.ws.managers.databases.internal.InternalDbManager;

public class InternalDbManagerRealDdReadOnlyTest {
	private static InternalDbManager internalManager;
	private static String uid = "";
	private static String pwd = "";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		uid = System.getenv("uid");
		pwd = System.getenv("pwd");
		
		internalManager = new InternalDbManager(
				"ioc-liturgical-db"
				, "json"
				, false // delete old database
				, false // truncate tables (if you don't delete the old db)
				, false // create test users
				, uid // the username of the wsadmin
				, pwd
				);
	}


	@Test
	public void testUserStats() {
    	UserStatus status = internalManager.getUserStatus(
    			uid
    			, pwd
    			, VERBS.GET.keyname
    			, "gr_gr_cog"
    			);
    	assertTrue(status.isAuthenticated());
	}

}
