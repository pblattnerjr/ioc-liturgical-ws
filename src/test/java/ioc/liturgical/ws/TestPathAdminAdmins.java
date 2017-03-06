package ioc.liturgical.ws;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.http.ContentType;
import ioc.liturgical.test.framework.TestConstants;
import ioc.liturgical.test.framework.TestUsers;
import ioc.liturgical.ws.app.ServiceProvider;
import ioc.liturgical.ws.constants.Constants;

public class TestPathAdminAdmins {
	
	String testPath = "/admins/";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ServiceProvider.main(new String [] {
				TestUsers.WS_ADMIN.password
				, TestConstants.DISABLE_EXTERNAL_DB // so test runs faster
				});
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	   public void testAdminDbUsersGetAdminsForAllLibraries() {
		    io.restassured.RestAssured
		    	.given()
				.baseUri(TestConstants.BASE_URL)
				.basePath(Constants.INTERNAL_DATASTORE_API_PATH)
				.auth(). preemptive().basic(
						TestUsers.WS_ADMIN.id
						, TestUsers.WS_ADMIN.password
						)
			    .expect().statusCode(200)
		    	.when().get(testPath);
	    }

	@Test
	   public void testAdminDbUsersGetAdminsForDedesLibrary() {
		    io.restassured.RestAssured
		    	.given()
				.baseUri(TestConstants.BASE_URL)
				.basePath(Constants.INTERNAL_DATASTORE_API_PATH)
				.auth(). preemptive().basic(
						TestUsers.WS_ADMIN.id
						, TestUsers.WS_ADMIN.password)
			       .accept(ContentType.JSON)
			       .expect().statusCode(200)
		    	.when().get(testPath + "en_us_dedes");
	    }

	@Test
	   public void testAdminDbUsersGetSpecificAdminForDedesLibrary() {
		    io.restassured.RestAssured
		    	.given()
				.baseUri(TestConstants.BASE_URL)
				.basePath(Constants.INTERNAL_DATASTORE_API_PATH)
				.auth(). preemptive().basic(TestUsers.WS_ADMIN.id, TestUsers.WS_ADMIN.password)
			       .accept(ContentType.JSON)
			       .expect().statusCode(200)
		    	.when().get(testPath+"en_us_dedes/adminForEnUsDedes");
	    }

	@Test
	    public void testAdminDbUsersGet401() {
	    	io.restassured.RestAssured
	    		.given()
	    		.baseUri(TestConstants.BASE_URL)
	    		.basePath(Constants.INTERNAL_DATASTORE_API_PATH)
	    		.when()
	    		.get(testPath)
	    		.then().statusCode(401);
	    }	

	@Test
	    public void testAdminDbUsersGet404() {
		    io.restassured.RestAssured
		    	.given()
				.baseUri(TestConstants.BASE_URL)
				.basePath(Constants.INTERNAL_DATASTORE_API_PATH)
				.auth(). preemptive().basic(TestUsers.WS_ADMIN.id, TestUsers.WS_ADMIN.password)
			       .accept(ContentType.JSON)
			       .expect().statusCode(404)
		    	.when().get(testPath+"junk");
	    }

}
