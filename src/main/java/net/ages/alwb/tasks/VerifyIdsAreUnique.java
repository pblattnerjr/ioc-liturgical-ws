package net.ages.alwb.tasks;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.util.StringUtils;
import org.ocmc.ioc.liturgical.schemas.models.db.docs.ontology.TextLiturgical;
import org.ocmc.ioc.liturgical.schemas.models.ws.response.ResultJsonObjectArray;

import com.google.gson.JsonObject;

import ioc.liturgical.ws.managers.databases.external.neo4j.ExternalDbManager;
import ioc.liturgical.ws.managers.exceptions.DbException;

/**
 * Runs a task (separate thread) to set fix the day number for Guatemala calendar
 * @author mac002
 *
 */
public class VerifyIdsAreUnique implements Runnable {
	ExternalDbManager manager = null;
	
	public VerifyIdsAreUnique (
			ExternalDbManager manager
			) {
		this.manager = manager;
	}
	
	@Override
	public void run() {
		synchronized(this) {
			List<String> theIds = new ArrayList<String>();
		}
	}
	
}
