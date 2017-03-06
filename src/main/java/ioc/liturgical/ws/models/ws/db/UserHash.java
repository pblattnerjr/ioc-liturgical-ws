package ioc.liturgical.ws.models.ws.db;


import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.annotations.Expose;

import net.ages.alwb.utils.core.auth.PasswordHasher;
import net.ages.alwb.utils.core.datastores.json.models.AbstractModel;
import net.ages.alwb.utils.core.error.handling.ErrorUtils;

public class UserHash extends AbstractModel {
	private static final Logger logger = LoggerFactory.getLogger(UserHash.class);
	
	@Expose String hashedPassword = "";
	@Expose boolean resetPwdNextLogin = true;
	
	public UserHash() {
		super();
		this.serialVersionUID = 1.1;
	}
		
	public String getHashedPassword() {
		return hashedPassword;
	}
	
	/**
	 * Use this method if you are starting from a hashed password.
	 * This is not the typical case.  Normally you will be starting with
	 * a plain text password.  if so, call setPassword instead.
	 * @param hashedPassword
	 */
	public void setHashedPassword(String hashedPassword) {
		this.hashedPassword = hashedPassword;
	}
	/**
	 * Use this method when the user provides a plain text password.
	 * It will be hashed and stored as a hash.  Subsequent
	 * authentication will use the hashed value.
	 * @param password
	 */
	public void setPassword(String password) {
		try {
			setHashedPassword(PasswordHasher.createHash(password));
		} catch (NoSuchAlgorithmException e) {
			ErrorUtils.report(logger, e);
		} catch (InvalidKeySpecException e) {
			ErrorUtils.report(logger, e);
		}
	}

	public boolean isResetPwdNextLogin() {
		return resetPwdNextLogin;
	}

	public void setResetPwdNextLogin(boolean resetPwdNextLogin) {
		this.resetPwdNextLogin = resetPwdNextLogin;
	}

}
