package net.ages.alwb.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import ioc.liturgical.ws.constants.HTTP_RESPONSE_CODES;
import ioc.liturgical.ws.constants.db.external.SCHEMA_CLASSES;
import ioc.liturgical.ws.managers.databases.external.neo4j.utils.Neo4jConnectionManager;
import ioc.liturgical.ws.managers.synch.SynchManager;
import ioc.liturgical.ws.models.RequestStatus;
import ioc.liturgical.ws.models.ResultJsonObjectArray;
import ioc.liturgical.ws.models.db.stats.SynchLog;
import ioc.liturgical.ws.models.db.supers.LTKDb;
import ioc.liturgical.ws.models.db.synch.Transaction;
import net.ages.alwb.utils.core.error.handling.ErrorUtils;

/**
 * Runs a task (separate thread) to push database transaction to the synch server
 * .
 * @author mac002
 *
 */
public class SynchPullTask implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(SynchPullTask.class);
	private static final Gson gson = new GsonBuilder().disableHtmlEscaping().create();
	private static final JsonParser parser = new JsonParser();

	Neo4jConnectionManager dbManager = null;
	SynchManager synchManager = null;
	boolean printpretty = false;
	
	public SynchPullTask (
			Neo4jConnectionManager dbManager
			, SynchManager synchManager
			) {
		this.dbManager = dbManager;
		this.synchManager = synchManager;
	}
	
	public SynchPullTask (
			Neo4jConnectionManager dbManager
			, SynchManager synchManager
			, boolean printpretty
			) {
		this.dbManager = dbManager;
		this.synchManager = synchManager;
		this.printpretty = printpretty;
	}
	
	@Override
	public void run() {
		try {
			if (synchManager.synchConnectionOK()) {
				SynchLog log = dbManager.getSynchLog();
				ResultJsonObjectArray transactions  = synchManager.getTransactionsSince("wsadmin",log.getLastUsedSynchTimestamp());
				if (transactions.valueCount > 0) {
					logger.info("Got " + transactions.valueCount + " transactions from synch server");
					for (JsonObject o : transactions.values) {
						try {
							Transaction trans = gson.fromJson(o, Transaction.class);
							LTKDb ltkDb = gson.fromJson(trans.getJson(), LTKDb.class);
							LTKDb record = 
										gson.fromJson(
												trans.getJson()
												, SCHEMA_CLASSES
													.classForSchemaName(
															ltkDb.get_valueSchemaId())
													.ltkDb.getClass()
									);
							RequestStatus status = dbManager.processTransaction(trans);
							if (status.getCode() == HTTP_RESPONSE_CODES.OK.code) {
								logger.info("Ran transaction " + trans.getId() + " against local database.");
								log.setLastUsedSynchTimestamp(trans.getKey());
								log.recordSynchTime();
								dbManager.recordSynch(log);
							} else {
								logger.error("Could not push " + trans.getId() + " to Synch Server");
							}
						} catch (Exception e) {
							ErrorUtils.report(logger, e);
						}
					}
				}
			} else {
				logger.info("Synch Manager not available...");
			}
		} catch (Exception e) {
			ErrorUtils.report(logger, e);
		}
	}
	
}
