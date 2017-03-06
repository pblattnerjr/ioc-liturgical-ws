package ioc.liturgical.ws.models.forms;

import com.google.gson.JsonObject;

import ioc.liturgical.ws.models.ws.forms.UserPasswordForm;

import org.junit.Test;

import junit.framework.TestCase;

public class UserPasswordFormTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	@Test
	public void testUserSchemaFromBadJson() {
		UserPasswordForm userAccess = new UserPasswordForm();
		userAccess = (UserPasswordForm) userAccess.fromJsonString("bad");
		assertNull(userAccess);
	}

	@Test
	public void testUserSchemaValidationNoErrors() {
		UserPasswordForm userAccess = new UserPasswordForm();
		userAccess.password = "I!2abedei2e2";
		userAccess.passwordReenter = "I!2abedei2e2";
		userAccess.username = "abc";
		assertTrue(userAccess.valid(userAccess.toJsonString()));
	}

	@Test
	public void testSchemaValidationBadUsername() {
		UserPasswordForm userAccess = new UserPasswordForm();
		userAccess.password = "I!2abedei2e2";
		userAccess.passwordReenter = "I!2abedei2e2";
		userAccess.username = "";
		assertFalse(userAccess.valid(userAccess.toJsonString()));
	}

	@Test
	public void testSchemaValidationMissingUsername() {
		UserPasswordForm userAccess = new UserPasswordForm();
		userAccess.password = "I!2abedei2e2";
		userAccess.passwordReenter = "I!2abedei2e2";
		JsonObject obj = userAccess.toJsonObject();
		obj.remove("username");
		assertFalse(userAccess.valid(obj.toString()));
	}

	@Test
	public void testSchemaValidationPasswordsDoNotMatch() {
		UserPasswordForm userAccess = new UserPasswordForm();
		userAccess.password = "I!2abedei2e2";
		userAccess.passwordReenter = "I!2abedei2e3";
		userAccess.username = "abc";
		assertFalse(userAccess.valid(userAccess.toJsonString()));
	}

	
	@Test
	public void testSchemaValidationBadPasswordLength() {
		UserPasswordForm userAccess = new UserPasswordForm();
		userAccess.password = "I!2abed";
		userAccess.passwordReenter = "I!2abed";
		userAccess.username = "abc";
		assertFalse(userAccess.valid(userAccess.toJsonString()));
	}

	@Test
	public void testSchemaValidationBadPasswordNoCapitalLetter() {
		UserPasswordForm userAccess = new UserPasswordForm();
		userAccess.password = "i!2abedei2e2";
		userAccess.passwordReenter = "i!2abedei2e2";
		userAccess.username = "abc";
		assertFalse(userAccess.valid(userAccess.toJsonString()));
	}
	@Test
	public void testSchemaValidationBadPasswordNoSpecialCharacter() {
		UserPasswordForm userAccess = new UserPasswordForm();
		userAccess.password = "Ii2abedei2e2";
		userAccess.passwordReenter = "Ii2abedei2e2";
		userAccess.username = "abc";
		assertFalse(userAccess.valid(userAccess.toJsonString()));
	}
}
