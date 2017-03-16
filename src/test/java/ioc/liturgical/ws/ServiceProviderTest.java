package ioc.liturgical.ws;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.http.ContentType;
import ioc.liturgical.test.framework.TestConstants;
import ioc.liturgical.test.framework.TestReferences;
import ioc.liturgical.test.framework.TestUsers;
import ioc.liturgical.ws.app.ServiceProvider;
import ioc.liturgical.ws.constants.Constants;
import ioc.liturgical.ws.constants.ENDPOINTS_DB_API;
import ioc.liturgical.ws.constants.HTTP_RESPONSE_CODES;
import ioc.liturgical.ws.constants.NEW_FORM_CLASSES_ADMIN_API;
import ioc.liturgical.ws.constants.NEW_FORM_CLASSES_DB_API;
import ioc.liturgical.ws.models.db.forms.ReferenceCreateForm;
import ioc.liturgical.ws.models.ws.forms.DomainCreateForm;

public class ServiceProviderTest {
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ServiceProvider.main(new String[] {
				TestUsers.WS_ADMIN.password
//				, TestConstants.DISABLE_EXTERNAL_DB
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

	// Domain Controller Tests
	@Test 
	public void testCreateDomain() {
		DomainCreateForm obj = new DomainCreateForm();
		obj.setLanguageCode("sw");
		obj.setCountryCode("ke");
		obj.setRealm("bogus");
		obj.setDescription("translations by Somebody");
	    io.restassured.RestAssured
    	.given()
		.baseUri(TestConstants.BASE_URL)
	    .body(obj.toJsonString())
		.auth(). preemptive().basic(
				TestUsers.WS_ADMIN.id
				, TestUsers.WS_ADMIN.password)
	       .accept(ContentType.JSON)
	       .expect().statusCode(HTTP_RESPONSE_CODES.CREATED.code)
    	.when().post(NEW_FORM_CLASSES_ADMIN_API.NEW_DOMAIN.toPostPath());
	}

	// Reference Controller Tests

	@Test 
	public void testCreateReference() {
		TestReferences testReferences = new TestReferences();
		ReferenceCreateForm obj = testReferences.getCreateForm(0);
	    io.restassured.RestAssured
    	.given()
		.baseUri(TestConstants.BASE_URL)
	    .body(obj.toJsonString())
		.auth(). preemptive().basic(
				TestUsers.WS_ADMIN.id
				, TestUsers.WS_ADMIN.password)
	       .accept(ContentType.JSON)
	       .expect().statusCode(HTTP_RESPONSE_CODES.CREATED.code)
    	.when().post(NEW_FORM_CLASSES_DB_API.NEW_REFERENCE.toPostPath());

	    io.restassured.RestAssured
    	.given()
		.baseUri(TestConstants.BASE_URL)
		.auth(). preemptive().basic(
				TestUsers.WS_ADMIN.id
				, TestUsers.WS_ADMIN.password)
		.param("id", obj.getId())
	       .accept(ContentType.JSON)
	       .expect().statusCode(HTTP_RESPONSE_CODES.OK.code)
    	.when().delete(Constants.EXTERNAL_DATASTORE_API_PATH + obj.getIdAsPath());
	}
}
