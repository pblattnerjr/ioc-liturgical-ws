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
import ioc.liturgical.ws.constants.ADMIN_ENDPOINTS;
import ioc.liturgical.ws.constants.Constants;
import ioc.liturgical.ws.constants.HTTP_RESPONSE_CODES;
import ioc.liturgical.ws.constants.NEW_FORM_CLASSES;
import ioc.liturgical.ws.models.ws.db.UserContact;
import ioc.liturgical.ws.models.ws.forms.UserCreateForm;
import ioc.liturgical.ws.models.ws.forms.UserUpdateForm;

public class TestPathAdminDbUsers {
	
	/**
	 * TODO: test missing required field
	 */
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ServiceProvider.main(
				new String[] {
						TestUsers.WS_ADMIN.password
						, TestConstants.DISABLE_EXTERNAL_DB
						});
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}
	
	@Test 
	public void testCreateUser() {
		UserCreateForm user = new UserCreateForm();
		user.setUsername("mmouse");
		user.setFirstname("Mickey");
		user.setLastname("Mouse");
		user.setEmail("mmouse@disneyland.com");
		user.setEmailReenter(user.getEmail()); // just to make sure they match for this test
		user.setPassword("Ithinkthat23!itMustBeStrong!");
		user.setPasswordReenter(user.getPassword()); // again, for this test, make sure they match
	    io.restassured.RestAssured
    	.given()
		.baseUri(TestConstants.BASE_URL)
	    .body(user.toJsonString())
		.auth(). preemptive().basic(TestUsers.WS_ADMIN.id, TestUsers.WS_ADMIN.password)
	       .accept(ContentType.JSON)
	       .expect().statusCode(HTTP_RESPONSE_CODES.CREATED.code)
    	.when().post(NEW_FORM_CLASSES.NEW_USER.toPostPath());
	}
	
	@Test 
	public void testCreateUserContact() {
		UserCreateForm user = new UserCreateForm();
		user.setUsername("jjones");
		user.setFirstname("Jimmy");
		user.setLastname("Jones");
		user.setEmail("jjones@happyland.com");
		user.setEmailReenter(user.getEmail());
		user.setPassword("A1!wedkdkdkdkdkdjadsfkj$");
		user.setPasswordReenter(user.getPassword());
	    io.restassured.RestAssured
    	.given()
		.baseUri(TestConstants.BASE_URL)
	    .body(user.toJsonString())
		.auth(). preemptive().basic(
				TestUsers.WS_ADMIN.id
				, TestUsers.WS_ADMIN.password)
	       .accept(ContentType.JSON)
	       .expect().statusCode(HTTP_RESPONSE_CODES.CREATED.code)
    	.when().post(
    			NEW_FORM_CLASSES.NEW_USER.toPostPath() 
    	);
	}
	
	@Test 
	public void testUpdateUserContact() {
		UserContact user = new UserContact();
		String username = "jjones";
		user.setFirstname("James");
		user.setLastname("Jones");
		user.setEmail("jjones@happyland.com");
	    io.restassured.RestAssured
    	.given()
		.baseUri(TestConstants.BASE_URL)
	    .body(user.toJsonString())
		.auth(). preemptive().basic(TestUsers.WS_ADMIN.id, TestUsers.WS_ADMIN.password)
	       .accept(ContentType.JSON)
	       .expect().statusCode(HTTP_RESPONSE_CODES.OK.code)
    	.when().put(
    			ADMIN_ENDPOINTS.USERS_CONTACT.toLibraryPath()
    			+ "/"
    			+ username
    			);
	}

	@Test 
	public void testUpdateUserContactMalformedEmail() {
		UserContact user = new UserContact();
		String username = "jjones";
		user.setFirstname("James");
		user.setLastname("Jones");
		user.setEmail("happyland.com");
	    io.restassured.RestAssured
    	.given()
		.baseUri(TestConstants.BASE_URL)
	    .body(user.toJsonString())
		.auth(). preemptive().basic(TestUsers.WS_ADMIN.id, TestUsers.WS_ADMIN.password)
	       .accept(ContentType.JSON)
	       .expect().statusCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code)
	    	.when().put(
	    			ADMIN_ENDPOINTS.USERS_CONTACT.toLibraryPath()
	    			+ "/"
	    			+ username
	    			);
	}

	@Test 
	public void testCreateUserMissingRequiredField() {
		UserCreateForm user = new UserCreateForm();
		user.setFirstname("Abby");
		user.setLastname("Monk");
		user.setUsername("amonk");
		user.setEmail("amonk@quietland.com");
		user.setEmailReenter("amonk@quietland.com");
		user.setPassword("I19amaPassword!");
		user.setPasswordReenter("I19amaPassword!");
		JsonObject obj = user.toJsonObject();
		obj.remove("firstname");
	    io.restassured.RestAssured
    	.given()
		.baseUri(TestConstants.BASE_URL)
	    .body(obj.toString())
		.auth(). preemptive().basic(TestUsers.WS_ADMIN.id, TestUsers.WS_ADMIN.password)
	       .accept(ContentType.JSON)
	       .expect().statusCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code)
    	.when().post(NEW_FORM_CLASSES.NEW_USER.toPostPath() );
	}

	@Test 
	public void testCreateUserPasswordsDoNotMatch() {
		UserCreateForm user = new UserCreateForm();
		user.setFirstname("Jimmy");
		user.setLastname("Jones");
		user.setUsername("jjones");
		user.setEmail("jjones@happyland.com");
		user.setEmailReenter("jjones@disney.com");
		user.setPassword("I19amaPassword!");
		user.setPasswordReenter("I19amaPassword!");
	    io.restassured.RestAssured
    	.given()
		.baseUri(TestConstants.BASE_URL)
	    .body(user.toJsonString())
		.auth(). preemptive().basic(TestUsers.WS_ADMIN.id, TestUsers.WS_ADMIN.password)
	       .accept(ContentType.JSON)
	       .expect().statusCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code)
    	.when().post(NEW_FORM_CLASSES.NEW_USER.toPostPath() );
	}

	@Test 
	public void testCreateUserPasswordNotStrong() {
		UserCreateForm user = new UserCreateForm();
		user.setFirstname("Jimmy");
		user.setLastname("Jones");
		user.setUsername("jjones");
		user.setEmail("jjones@happyland.com");
		user.setEmailReenter("jjones@happyland.com");
		user.setPassword("abcd");
		user.setPasswordReenter(user.getPassword());
	    io.restassured.RestAssured
    	.given()
		.baseUri(TestConstants.BASE_URL)
	    .body(user.toJsonString())
		.auth(). preemptive().basic(
				TestUsers.WS_ADMIN.id
				, TestUsers.WS_ADMIN.password)
	       .accept(ContentType.JSON)
	       .expect().statusCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code)
    	.when().post(NEW_FORM_CLASSES.NEW_USER.toPostPath());
	}

	@Test
	public void testCreateUserEmailsDoNotMatch() {
		UserCreateForm user = new UserCreateForm();
		user.setFirstname("Jimmy");
		user.setLastname("Jones");
		user.setUsername("jjones");
		user.setEmail("jjones@happlyland.com");
		user.setEmailReenter("jjones@happyland.com");
		user.setPassword("I19amaPassword!");
		user.setPasswordReenter("I19amaPassword!");
	    io.restassured.RestAssured
    	.given()
		.baseUri(TestConstants.BASE_URL)
	    .body(user.toJsonString())
		.auth(). preemptive().basic(TestUsers.WS_ADMIN.id, TestUsers.WS_ADMIN.password)
	       .accept(ContentType.JSON)
	       .expect().statusCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code)
//	       .expect().body("userMessage", org.hamcrest.Matchers.containsString("Expected email to match emailReenter"))
    	.when().post(NEW_FORM_CLASSES.NEW_USER.toPostPath() );
	}


	@Test
	public void testCreateExistingUser() {
		UserCreateForm user = new UserCreateForm();

		user.setFirstname("Wimmy");
		user.setLastname("Jones");
		user.setUsername("wjones");
		user.setEmail("wjones@happyland.com");
		user.setEmailReenter("wjones@happyland.com");
		user.setPassword("I19amaPassword!");
		user.setPasswordReenter("I19amaPassword!");

		io.restassured.RestAssured
    	.given()
		.baseUri(TestConstants.BASE_URL)
	    .body(user.toJsonString())
		.auth(). preemptive().basic(TestUsers.WS_ADMIN.id, TestUsers.WS_ADMIN.password)
	       .accept(ContentType.JSON)
	       .expect().statusCode(HTTP_RESPONSE_CODES.CREATED.code)
    	.when().post(NEW_FORM_CLASSES.NEW_USER.toPostPath() );

		io.restassured.RestAssured
    	.given()
		.baseUri(TestConstants.BASE_URL)
	    .body(user.toJsonString())
		.auth(). preemptive().basic(TestUsers.WS_ADMIN.id, TestUsers.WS_ADMIN.password)
	       .accept(ContentType.JSON)
	       .expect().statusCode(HTTP_RESPONSE_CODES.CONFLICT.code)
    	.when().post(NEW_FORM_CLASSES.NEW_USER.toPostPath() );

	}

	@Test
	public void testUpdateNonExistentUser() {
		UserContact user = new UserContact();
		String username = "IdoNotExist";
		user.setFirstname("Jimmy");
		user.setLastname("Jones");
		user.setEmail("jjones@happyland.com");
	    io.restassured.RestAssured
    	.given()
		.baseUri(TestConstants.BASE_URL)
	    .body(user.toJsonString())
		.auth(). preemptive().basic(TestUsers.WS_ADMIN.id, TestUsers.WS_ADMIN.password)
	       .accept(ContentType.JSON)
	       .expect().statusCode(HTTP_RESPONSE_CODES.NOT_FOUND.code)
	      	.when().put(
	    			ADMIN_ENDPOINTS.USERS_CONTACT.toLibraryPath()
	    			+ "/"
	    			+ username
	    			);
	}

	@Test
	public void testUpdateUserNoJsonBody() {
		String username = "jjones";
	    io.restassured.RestAssured
    	.given()
		.baseUri(TestConstants.BASE_URL)
		.auth(). preemptive().basic(TestUsers.WS_ADMIN.id, TestUsers.WS_ADMIN.password)
	       .accept(ContentType.JSON)
	       .expect().statusCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code)
	      	.when().put(
	    			ADMIN_ENDPOINTS.USERS_CONTACT.toLibraryPath()
	    			+ "/"
	    			+ username
	    			);
	}

}
