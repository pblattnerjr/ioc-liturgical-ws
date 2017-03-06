package ioc.liturgical.test.framework;

public enum TestUsers {
		WS_ADMIN("wsadmin")
	, TEST_USER_AUTH_01("testUser01")
	, TEST_USER_AUTH_02("testUser02")
	, TEST_USER_AUTH_03("testUser03")
	, TEST_USER_AUTH_04("testUser04")
	, TEST_USER_AUTH_05("testUser05")
	;
	
	public String id = "";
	public String password = System.getenv("pwd");
	
	private TestUsers(
			String id
			) {
		this.id = id;
	}
		
}
