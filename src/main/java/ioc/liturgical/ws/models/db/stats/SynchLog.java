package ioc.liturgical.ws.models.db.stats;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import com.google.gson.annotations.Expose;

import ioc.liturgical.ws.constants.Constants;
import ioc.liturgical.ws.models.db.supers.LTKLite;
import ioc.liturgical.ws.models.db.synch.Transaction;
import net.ages.alwb.utils.core.datastores.json.exceptions.BadIdException;

/**
 * Records a query and the result count.
 * If you add a new property, add it to the getAsPropertiesMap as well.
 * @author mac002
 *
 */
public class SynchLog extends LTKLite {
	private static String schema = SynchLog.class.getSimpleName();
	private static double version = 1.1;
	public static final String singletonId = Constants.LIBRARY_SYNCH 
			+ Constants.ID_DELIMITER 
			+ Constants.TOPIC_SYNCH_LOG
			+ Constants.ID_DELIMITER 
			+ Constants.KEY_SYNCH_LOG
			;
	@Expose  public String lastUsedSynchTimestamp = "2000-01-01T12:00:23.764Z";
	@Expose  public String whenSynchLastRan = "";
	
	public SynchLog() {
		super(
				Constants.LIBRARY_SYNCH
				, Constants.TOPIC_SYNCH_LOG
				, Constants.KEY_SYNCH_LOG
				, schema
				, version
				);		
	}

	public String getLastUsedSynchTimestamp() {
		return lastUsedSynchTimestamp;
	}

	public void setLastUsedSynchTimestamp(String lastUsedSynchTimestamp) {
		this.lastUsedSynchTimestamp = lastUsedSynchTimestamp;
	}

	public String getWhenSynchLastRan() {
		return whenSynchLastRan;
	}

	public void setWhenSynchLastRan(String whenSynchLastRan) {
		this.whenSynchLastRan = whenSynchLastRan;
	}
	
	public void recordSynchTime() {
		this.whenSynchLastRan = Instant.now().toString();
	}

}
