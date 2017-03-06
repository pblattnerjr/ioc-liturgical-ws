package ioc.liturgical.ws;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runners.Suite;

import io.restassured.http.ContentType;
import ioc.liturgical.test.framework.TestConstants;
import ioc.liturgical.test.framework.TestUsers;
import ioc.liturgical.ws.app.ServiceProvider;
import ioc.liturgical.ws.constants.Constants;
import ioc.liturgical.ws.constants.HTTP_RESPONSE_CODES;

public class ServiceProviderTest {
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ServiceProvider.main(new String[] {
				TestUsers.WS_ADMIN.password
				, TestConstants.DISABLE_EXTERNAL_DB
				});
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}
	
	@Test
	   public void testNoAuthenticationCredentials() {
		    io.restassured.RestAssured
		    	.given()
				.baseUri(TestConstants.BASE_URL)
				.basePath(Constants.INTERNAL_DATASTORE_API_PATH)
			       .accept(ContentType.JSON)
			       .expect().statusCode(HTTP_RESPONSE_CODES.UNAUTHORIZED.code)
		    	.when().get("/");
	    }
	
}
