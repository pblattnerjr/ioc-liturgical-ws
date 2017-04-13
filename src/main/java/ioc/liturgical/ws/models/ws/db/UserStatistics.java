package ioc.liturgical.ws.models.ws.db;

import com.github.reinert.jjschema.Attributes;
import com.google.gson.annotations.Expose;
import net.ages.alwb.utils.core.datastores.json.models.AbstractModel;

@Attributes(readonly=true)
public class UserStatistics extends AbstractModel {
	@Attributes(description = "The number of times someone attempted to login with this username.")
	@Expose public int failedLoginCount = 0;
	
	@Attributes(description="The last date and time when someone failed to login with this username.  The time is recorded in Zulu (Greenwhich mean time).")
	@Expose public String lastFailedAccessDateTime = "";
	
	@Attributes(description="The nano time when the system was last accessed using this username.")
	@Expose public long lastAccessNanos = 0;

	@Attributes(description="The last date and time when the system was last successfully accessed using this username. The time is recorded in Zulu (Greenwhich mean time).")
	@Expose public String lastSuccessfulAccessDateTime = "";
	
	@Attributes(description="The number of times the system has been successfully accessed using this username.  Each web service call counts as an access.")
	@Expose public int accessCount = 0;
	
	public UserStatistics() {
		super();
		this.serialVersionUID = 1.1;
	}

	public int getFailedLoginCount() {
		return failedLoginCount;
	}

	public void setFailedLoginCount(int failedLoginCount) {
		this.failedLoginCount = failedLoginCount;
	}

	public long getLastAccessNanos() {
		return lastAccessNanos;
	}

	public void setLastAccessNanos(long lastAccess) {
		this.lastAccessNanos = lastAccess;
	}

	public int getAccessCount() {
		return accessCount;
	}

	public void setAccessCount(int accessCount) {
		this.accessCount = accessCount;
	}

	public String getLastFailedAccessDateTime() {
		return lastFailedAccessDateTime;
	}

	public void setLastFailedAccessDateTime(String lastFailedAccessDateTime) {
		this.lastFailedAccessDateTime = lastFailedAccessDateTime;
	}

	public String getLastSuccessfulAccessDateTime() {
		return lastSuccessfulAccessDateTime;
	}

	public void setLastSuccessfulAccessDateTime(String lastSuccessfulAccessDateTime) {
		this.lastSuccessfulAccessDateTime = lastSuccessfulAccessDateTime;
	}
	
		
}
