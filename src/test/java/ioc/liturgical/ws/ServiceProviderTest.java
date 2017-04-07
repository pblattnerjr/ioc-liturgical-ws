package ioc.liturgical.ws;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.http.ContentType;
import ioc.liturgical.test.framework.TestConstants;
import ioc.liturgical.test.framework.LinkRefersToBiblicalTextTextFactory;
import ioc.liturgical.test.framework.TestUsers;
import ioc.liturgical.ws.app.ServiceProvider;
import ioc.liturgical.ws.constants.Constants;
import ioc.liturgical.ws.constants.ENDPOINTS_DB_API;
import ioc.liturgical.ws.constants.HTTP_RESPONSE_CODES;
import ioc.liturgical.ws.constants.NEW_FORM_CLASSES_ADMIN_API;
import ioc.liturgical.ws.constants.NEW_FORM_CLASSES_DB_API;
import ioc.liturgical.ws.constants.RELATIONSHIP_TYPES;
import ioc.liturgical.ws.models.db.forms.LinkRefersToBiblicalTextCreateForm;
import ioc.liturgical.ws.models.ws.forms.DomainCreateForm;

public class ServiceProviderTest {
	
	public static String pwd = "";
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		pwd = System.getenv("pwd");
		ServiceProvider.main(new String[] {
				pwd
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
				, pwd)
	       .accept(ContentType.JSON)
	       .expect().statusCode(HTTP_RESPONSE_CODES.CREATED.code)
    	.when().post(NEW_FORM_CLASSES_ADMIN_API.NEW_DOMAIN.toPostPath());
	}

	// Reference Controller Tests

	@Test 
	public void testCreateAndDeleteReference() {
		LinkRefersToBiblicalTextTextFactory testReferences = new LinkRefersToBiblicalTextTextFactory();
		LinkRefersToBiblicalTextCreateForm obj = testReferences.getCreateForm(0);
	    io.restassured.RestAssured
    	.given()
		.baseUri(TestConstants.BASE_URL)
	    .body(obj.toJsonString())
		.auth(). preemptive().basic(
				TestUsers.WS_ADMIN.id
				, pwd)
	       .accept(ContentType.JSON)
	       .expect().statusCode(HTTP_RESPONSE_CODES.CREATED.code)
    	.when().post(ENDPOINTS_DB_API.LINKS.pathname);

	    io.restassured.RestAssured
    	.given()
		.baseUri(TestConstants.BASE_URL)
		.auth(). preemptive().basic(
				TestUsers.WS_ADMIN.id
				, pwd)
		.param("id", obj.getId())
	       .accept(ContentType.JSON)
	       .expect().statusCode(HTTP_RESPONSE_CODES.OK.code)
    	.when().delete(ENDPOINTS_DB_API.LINKS.pathname + "/" + obj.getIdAsPath());
	}
	
	@Test 
	public void testGetLinksRefersToBiblicalText() {
	    io.restassured.RestAssured
    	.given()
		.baseUri(TestConstants.BASE_URL)
		.auth(). preemptive().basic(
				TestUsers.WS_ADMIN.id
				, pwd)
	       .accept(ContentType.JSON)
	       .param("t", RELATIONSHIP_TYPES.REFERS_TO_BIBLICAL_TEXT.name())
	       .param("d", "en_us_pentiuc")
	       .param("l", "a, b")
	       .expect().statusCode(HTTP_RESPONSE_CODES.OK.code)
    	.when().get(ENDPOINTS_DB_API.LINKS.pathname);
	}

	@Test 
	public void testGetDropdownsSearchRelationships() {
	    io.restassured.RestAssured
    	.given()
		.baseUri(TestConstants.BASE_URL)
		.auth(). preemptive().basic(
				TestUsers.WS_ADMIN.id
				, pwd)
	       .accept(ContentType.JSON)
	       .expect().statusCode(HTTP_RESPONSE_CODES.OK.code)
    	.when().get(ENDPOINTS_DB_API.DROPDOWNS_RELATIONSHIPS.pathname);
	}

}
