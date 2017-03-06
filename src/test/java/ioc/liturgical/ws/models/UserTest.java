package ioc.liturgical.ws.models;

import ioc.liturgical.ws.models.ws.db.User;
import junit.framework.TestCase;

public class UserTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}
	
	public void testUserSchema() {
		User userAccess = new User();
		userAccess.setUsername("testuser");
		userAccess.setFirstname("Sally");
		userAccess.setSurname("Jones");
		String schema = userAccess.toJsonString();
		assertTrue(userAccess.toJsonSchema() != null);
	}
	

}
