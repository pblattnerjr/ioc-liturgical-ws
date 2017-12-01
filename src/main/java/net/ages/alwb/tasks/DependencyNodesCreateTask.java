package net.ages.alwb.tasks;
import java.util.List;

import org.ocmc.ioc.liturgical.schemas.constants.HTTP_RESPONSE_CODES;
import org.ocmc.ioc.liturgical.schemas.models.db.docs.nlp.TokenAnalysis;
import org.ocmc.ioc.liturgical.schemas.models.ws.response.RequestStatus;
import org.ocmc.ioc.liturgical.utils.ErrorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ioc.liturgical.ws.managers.databases.external.neo4j.ExternalDbManager;

/**
 * Runs a task (separate thread) to generate save newly created
 * .
 * @author mac002
 *
 */
public class DependencyNodesCreateTask implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(DependencyNodesCreateTask.class);

	ExternalDbManager manager = null;
	String requestor = "";
	List<TokenAnalysis> nodes = null;
	
	public DependencyNodesCreateTask (
			ExternalDbManager manager
			, String requestor
			, List<TokenAnalysis> nodes
			) {
		this.manager = manager;
		this.requestor = requestor;
		this.nodes = nodes;
	}
	
	@Override
	public void run() {
		synchronized(this) {
			for (TokenAnalysis node : nodes) {
				try {
					RequestStatus status = manager.addLTKDbObject(requestor, node.toJsonString());
					if (status.getCode() != HTTP_RESPONSE_CODES.CREATED.code) {
						ErrorUtils.reportAnyErrors("Could not create node information for " + node.id);
					}
				} catch (Exception e) {
					ErrorUtils.report(logger, e);
				}
			}
		}
	}
	
}
