package net.ages.alwb.tasks;
import org.joda.time.Instant;
import org.ocmc.ioc.liturgical.schemas.constants.DATA_SOURCES;
import org.ocmc.ioc.liturgical.schemas.constants.HTTP_RESPONSE_CODES;
import org.ocmc.ioc.liturgical.schemas.constants.STATUS;
import org.ocmc.ioc.liturgical.schemas.constants.VISIBILITY;
import org.ocmc.ioc.liturgical.schemas.id.managers.IdManager;
import org.ocmc.ioc.liturgical.schemas.models.db.docs.ontology.TextLiturgical;
import org.ocmc.ioc.liturgical.schemas.models.ws.response.RequestStatus;
import org.ocmc.ioc.liturgical.schemas.models.ws.response.ResultJsonObjectArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;

import ioc.liturgical.ws.managers.databases.external.neo4j.ExternalDbManager;
import ioc.liturgical.ws.managers.databases.external.neo4j.utils.Neo4jConnectionManager;
import ioc.liturgical.ws.managers.exceptions.DbException;

/**
 * Runs a task (separate thread) to copy liturgical texts from one library to another
 * @author mac002
 *
 */
public class CopyLiturgicalTexts implements Runnable {
	private static final Logger logger = LoggerFactory
			.getLogger(CopyLiturgicalTexts.class);
	ExternalDbManager manager = null;
	String fromLibrary = "";
	String toLibrary = "";
	
	public CopyLiturgicalTexts (
			ExternalDbManager manager
			, String fromLibrary
			, String toLibrary
			) {
		this.manager = manager;
		this.fromLibrary = fromLibrary;
		this.toLibrary = toLibrary;
	}
	
	@Override
	public void run() {
		synchronized(this) {
			String query = "match (n:Root:Liturgical) where n.library = '" 
					+ this.fromLibrary 
					+ "' return properties(n);";
			ResultJsonObjectArray docs = this.manager.getForQuery(query, false, false);
			for (JsonObject doc : docs.getValues()) {
				String json = doc.get("properties(n)").getAsJsonObject().toString();
				TextLiturgical text = this.manager.gson.fromJson(json, TextLiturgical.class);
				try {
					text.setLibrary(this.toLibrary);
					IdManager idManager = new IdManager(text.getLibrary(), text.getTopic(), text.getKey());
					text.setId(idManager.getId());
//					logger.info("Copying from " + this.fromLibrary + " to " + text.getId());
					if (text.adHocLabel == null) {
						text.setAdHocLabel("");
					}
					if (text.adhocSortKey == null) {
						text.adhocSortKey = "";
					}
					if (text.getComment() == null) {
						text.setComment("");
					}
					if (text.dataSource == null) {
						text.setDataSource(DATA_SOURCES.ONLINE);
					}
					text.setStatus(STATUS.RELEASED);
					text.setVisibility(VISIBILITY.PUBLIC);
					text.setModifiedBy("wsadmin");
					text.setModifiedWhen(Instant.now().toString());
				    RequestStatus status = ExternalDbManager.neo4jManager.mergeWhereEqual(text);
				    if (status.code != HTTP_RESPONSE_CODES.OK.code) {
				    	logger.error("Error updating " + text.getId() + " " + status.getCode() + ": " + status.getDeveloperMessage());
				    } else {
				    	logger.info("Copied from " + this.fromLibrary + " to " + text.getId());
				    }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
	}
	
}
