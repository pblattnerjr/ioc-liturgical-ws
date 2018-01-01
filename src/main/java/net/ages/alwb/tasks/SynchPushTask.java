package net.ages.alwb.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import ioc.liturgical.ws.managers.databases.external.neo4j.utils.Neo4jConnectionManager;
import ioc.liturgical.ws.managers.synch.SynchManager;

import org.ocmc.ioc.liturgical.schemas.constants.HTTP_RESPONSE_CODES;
import org.ocmc.ioc.liturgical.schemas.models.synch.Transaction;
import org.ocmc.ioc.liturgical.schemas.models.ws.response.RequestStatus;
import org.ocmc.ioc.liturgical.schemas.models.ws.response.ResultJsonObjectArray;
import org.ocmc.ioc.liturgical.utils.ErrorUtils;

/**
 * Runs a task (separate thread) to push database transaction to the synch server
 * .
 * @author mac002
 *
 */
public class SynchPushTask implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(SynchPushTask.class);
	private static final Gson gson = new GsonBuilder().disableHtmlEscaping().create();
	private static final JsonParser parser = new JsonParser();

	Neo4jConnectionManager dbManager = null;
	SynchManager synchManager = null;
	boolean printpretty = false;
	
	public SynchPushTask (
			Neo4jConnectionManager dbManager
			, SynchManager synchManager
			) {
		this.dbManager = dbManager;
		this.synchManager = synchManager;
	}
	
	public SynchPushTask (
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
				ResultJsonObjectArray transactions  = dbManager.getTransactions(printpretty);
				for (JsonObject o : transactions.values) {
					Transaction trans = gson.fromJson(o, Transaction.class);
					RequestStatus requestStatus = synchManager.recordTransaction(trans);
					if (requestStatus.getCode() == HTTP_RESPONSE_CODES.CREATED.code) {
						logger.info("Pushed " + trans.getId() + " to Synch Server and deleted from local database");
						RequestStatus deleteStatus = dbManager.deleteTransaction(trans.getId());
						if (deleteStatus.getCode() != HTTP_RESPONSE_CODES.OK.code) {
							logger.error("Could not delete " + trans.getId() + ": " + deleteStatus.getDeveloperMessage());
						}
					} else {
						logger.error("Could not push " + trans.getId() + " to Synch Server");
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
