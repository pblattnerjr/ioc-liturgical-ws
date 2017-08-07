package ioc.liturgical.ws.managers.auth;

public class UserStatus {
	boolean knownUser = false;
	boolean authenticated = false;
	boolean authorized = false;
	boolean sessionExpired = false;
	boolean mustResetPassword = false;
	
	public boolean isAuthenticated() {
		return this.authenticated;
	}
	public void setAuthenticated(boolean authenticated) {
		this.authenticated = authenticated;
	}
	public boolean isAuthorized() {
		return this.authorized;
	}
	public void setAuthorized(boolean authorized) {
		this.authorized = authorized;
	}
	public boolean isSessionExpired() {
		return this.sessionExpired;
	}
	public void setSessionExpired(boolean sessionExpired) {
		this.sessionExpired = sessionExpired;
	}
	public boolean isKnownUser() {
		return knownUser;
	}
	public void setKnownUser(boolean knownUser) {
		this.knownUser = knownUser;
	}
	public boolean isMustResetPassword() {
		return mustResetPassword;
	}
	public void setMustResetPassword(boolean mustResetPassword) {
		this.mustResetPassword = mustResetPassword;
	}
}
