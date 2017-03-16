package ioc.liturgical.ws;


import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.JsonObject;

import io.restassured.http.ContentType;

import org.hamcrest.Matchers;

import ioc.liturgical.test.framework.TestConstants;
import ioc.liturgical.test.framework.TestUsers;
import ioc.liturgical.ws.app.ServiceProvider;
import ioc.liturgical.ws.constants.HTTP_RESPONSE_CODES;
import ioc.liturgical.ws.constants.NEW_FORM_CLASSES_ADMIN_API;
import ioc.liturgical.ws.models.ws.forms.DomainCreateForm;

public class TestPathDomains {
	
	/**
	 * TODO: test missing required field
	 */
	
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

}
