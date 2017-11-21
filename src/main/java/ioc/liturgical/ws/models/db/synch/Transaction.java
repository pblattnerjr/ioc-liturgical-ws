package ioc.liturgical.ws.models.db.synch;

import java.time.Instant;

import com.google.gson.annotations.Expose;

import net.ages.alwb.utils.core.datastores.json.exceptions.BadIdException;
import ioc.liturgical.ws.constants.Constants;
import ioc.liturgical.ws.models.db.supers.LTKDb;
import ioc.liturgical.ws.models.db.supers.LTKLite;

/**
 * Provides a model for transactions stored in a synch database.
 * 
 * The database should have a constraint on n:Transaction property timestamp.
 * 
 * @author mac002
 *
 */
public class Transaction extends LTKLite {
	private static String schema = Transaction.class.getSimpleName();
	private static double version = 1.1;

	@Expose public String json = null;
	@Expose public String whenTransactionRecordedInThisDatabase = "";
	@Expose public String requestingServer = "";
	@Expose public String requestingMac = "";
	@Expose public String cypher = "";
	
	/**
	 * Constructor for a Cypher transaction that creates or updates a doc
	 * 
	 * The library will be set to "en_sys_synch"
	 * The topic will be set to transaction.getId()
	 * The key will be set to transaction.getModifiedWhen()
	 * 
	 * @param doc - the Json Doc for the transaction
	 * @param cypher - the Neo4j Cypher statement(s) used for the transaction
	 * @param requestingServer - the address of the server that made the request to save this transaction
	 * @throws BadIdException
	 */
	public Transaction(
			String cypher
			, LTKDb doc
			, String requestingServer
			) throws BadIdException {
		super(
				Constants.LIBRARY_SYNCH
				, doc.getId()
				, doc.getModifiedWhen()
				, schema
				, version
				);
		doc.setPrettyPrint(false);
		this.json = doc.toJsonString();
		this.cypher = cypher;
		this.whenTransactionRecordedInThisDatabase = Instant.now().toString();
		this.requestingServer = requestingServer;
		this.status = doc.getStatus();
	}

	/**
	 * Constructor for a Cypher transaction that creates or updates a doc
	 * 
	 * The library will be set to "en_sys_synch"
	 * The topic will be set to "cypher"
	 * The key will be set to Instant.now().toString(), i.e. it is a timestamp
	 * 
	 * @param cypher - the Neo4j Cypher statement(s) used for the transaction
	 * @param requestingServer - the address of the server that made the request to save this transaction
	 * @throws BadIdException
	 */
	public Transaction(
			String cypher
			, String requestingServer
			) throws BadIdException {
		super(
				Constants.LIBRARY_SYNCH
				, Constants.TOPIC_SYNCH
				, Instant.now().toString()
				, schema
				, version
				);
		this.cypher = cypher;
		this.whenTransactionRecordedInThisDatabase = this.getKey();
		this.requestingServer = requestingServer;
	}

	public String getRequestingServer() {
		return requestingServer;
	}

	public void setRequestingServer(String requestingServer) {
		this.requestingServer = requestingServer;
	}

	public String getCypher() {
		return cypher;
	}

	public void setCypher(String cypher) {
		this.cypher = cypher;
	}

	/**
	 * The most important timestamp is the one obtained by Transaction.getKey().
	 * It is the timestamp for when the original record was saved to the requesting local database.
	 * This method gets the timestamp for when the transaction was recorded by the SynchManager web service.
	 * The whenTransactionRecordedInThisDatabase timestamp will likely be more recent than Transaction.getKey().
	 * The usefulness of whenTransactionRecordedInThisDatabase is to determine if there was a significant
	 * time lag between the local database being updated and the synch database getting the transaction.
	 * @return
	 */
	public String getWhenTransactionRecordedInThisDatabase() {
		return whenTransactionRecordedInThisDatabase;
	}

	public void setWhenTransactionRecordedInThisDatabase(String whenTransactionRecordedInThisDatabase) {
		this.whenTransactionRecordedInThisDatabase = whenTransactionRecordedInThisDatabase;
	}

	public String getRequestingMac() {
		return requestingMac;
	}

	public void setRequestingMac(String requestingMac) {
		this.requestingMac = requestingMac;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

}
