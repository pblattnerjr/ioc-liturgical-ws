package ioc.liturgical.ws.datastore;

import ioc.liturgical.ws.constants.Constants;
import ioc.liturgical.ws.constants.ROLES;
import ioc.liturgical.ws.constants.VERBS;
import ioc.liturgical.ws.managers.databases.internal.InternalDbManager;
import ioc.liturgical.ws.models.ws.db.UserContact;
import ioc.liturgical.ws.models.ws.db.UserHash;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
/**
 * TODO: add ability to make REST calls so you can assert that you get the expected result back
 * 
 * @author mac002
 *
 */
public class InternalDbManagerTest extends TestCase {
	
	private InternalDbManager accessManager;
	private static final String password = "secret";
	private static final String wsAdmin = "wsadmin";
	
	protected void setUp() throws Exception {
		super.setUp();
		accessManager = new InternalDbManager(
				"test-db" // store name
				, "json" // tablename
				, true // delete old database
				, true // truncate tables (if you don't delete the old db)
				, true // create test users
				, wsAdmin // the username of the wsadmin
				);
		
		for (int i=1; i < 5; i++) {
			UserContact userContact = new UserContact();
			userContact.setFirstname("User 0" + i);
			userContact.setLastname("Test");
			userContact.setEmail("testuser0" + i + "@testland.com");
			accessManager.addUserContact("testUser0"+i, userContact);
			UserHash hash = new UserHash();
			hash.setHashedPassword(accessManager.hashPassword(password));
			accessManager.addUserHash("testUser0"+i, hash);
		}
		accessManager.grantRole(wsAdmin,ROLES.ADMIN, Constants.DOMAINS_LIB,"testUser01");
		accessManager.grantRole(wsAdmin,ROLES.ADMIN, "gr_gr_cog","testUser02");
		accessManager.grantRole(wsAdmin,ROLES.AUTHOR, "gr_gr_cog","testUser03");
		accessManager.grantRole(wsAdmin,ROLES.READER, "gr_gr_cog","testUser04");

	}

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( InternalDbManagerTest.class );
    }

    public void testApp()
    {
    	assertTrue(accessManager.existsLibrary(Constants.SYSTEM_LIB));
    	assertTrue(accessManager.existsLibrary(Constants.DOMAINS_LIB));
    	assertFalse(accessManager.existsLibrary("libraryOfAlexandriaEgypt"));
    	
        assertTrue( accessManager.existsUser("testUser01") );
        assertFalse( accessManager.existsUser("SiobaUra") );
        
        assertAuths(wsAdmin,Constants.SYSTEM_LIB);
        assertAuths(wsAdmin,Constants.DOMAINS_LIB);
        assertAuths(wsAdmin,"gr_gr_cog");
        assertAuths("testUser02","gr_gr_cog");

        assertReadOnlyAuths("testUser04", "gr_gr_cog");
    }
    
    private void assertAuths(String user, String library) {
    	
        assertTrue(accessManager.authorized(user, VERBS.GET, library));
        assertTrue(accessManager.authorized(user, VERBS.PUT, library));
        assertTrue(accessManager.authorized(user, VERBS.POST, library));
        assertTrue(accessManager.authorized(user, VERBS.DELETE, library));
        
        if (accessManager.authorized(user, VERBS.PUT, Constants.SYSTEM_LIB)) {
            if (! library.matches(Constants.SYSTEM_LIB) && ! library.matches(Constants.DOMAINS_LIB)) {
                assertTrue(accessManager.authorized(user, VERBS.GET, Constants.DOMAINS_LIB));
                assertTrue(accessManager.authorized(user, VERBS.PUT, Constants.DOMAINS_LIB));
                assertTrue(accessManager.authorized(user, VERBS.POST, Constants.DOMAINS_LIB));
                assertTrue(accessManager.authorized(user, VERBS.DELETE, Constants.DOMAINS_LIB));
            }
        } else  {
            if (! library.matches(Constants.SYSTEM_LIB)) {
                assertFalse(accessManager.authorized(user, VERBS.GET, Constants.SYSTEM_LIB));
                assertFalse(accessManager.authorized(user, VERBS.PUT, Constants.SYSTEM_LIB));
                assertFalse(accessManager.authorized(user, VERBS.POST, Constants.SYSTEM_LIB));
                assertFalse(accessManager.authorized(user, VERBS.DELETE, Constants.SYSTEM_LIB));
            }
            if (! library.matches(Constants.SYSTEM_LIB) && ! library.matches(Constants.DOMAINS_LIB)) {
                assertFalse(accessManager.authorized(user, VERBS.GET, Constants.DOMAINS_LIB));
                assertFalse(accessManager.authorized(user, VERBS.PUT, Constants.DOMAINS_LIB));
                assertFalse(accessManager.authorized(user, VERBS.POST, Constants.DOMAINS_LIB));
                assertFalse(accessManager.authorized(user, VERBS.DELETE, Constants.DOMAINS_LIB));
            }
        }
    }
    private void assertReadOnlyAuths(String user, String library) {
    	
        assertTrue(accessManager.authorized(user, VERBS.GET, library));
        assertFalse(accessManager.authorized(user, VERBS.PUT, library));
        assertFalse(accessManager.authorized(user, VERBS.POST, library));
        assertFalse(accessManager.authorized(user, VERBS.DELETE, library));
        
            if (! library.matches(Constants.SYSTEM_LIB)) {
                assertFalse(accessManager.authorized(user, VERBS.GET, Constants.SYSTEM_LIB));
                assertFalse(accessManager.authorized(user, VERBS.PUT, Constants.SYSTEM_LIB));
                assertFalse(accessManager.authorized(user, VERBS.POST, Constants.SYSTEM_LIB));
                assertFalse(accessManager.authorized(user, VERBS.DELETE, Constants.SYSTEM_LIB));
            }
            if (! library.matches(Constants.SYSTEM_LIB) && ! library.matches(Constants.DOMAINS_LIB)) {
                assertFalse(accessManager.authorized(user, VERBS.GET, Constants.DOMAINS_LIB));
                assertFalse(accessManager.authorized(user, VERBS.PUT, Constants.DOMAINS_LIB));
                assertFalse(accessManager.authorized(user, VERBS.POST, Constants.DOMAINS_LIB));
                assertFalse(accessManager.authorized(user, VERBS.DELETE, Constants.DOMAINS_LIB));
            }
    }
}
