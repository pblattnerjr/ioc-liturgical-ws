package net.ages.alwb.tasks;
import ioc.liturgical.ws.managers.databases.external.neo4j.ExternalDbManager;

/**
 * Runs a task (separate thread) to generate the Xelatex content for
 * a tex file.  The tex file can then be used to create a PDF.
 * @author mac002
 *
 */
public class DomainDropdownsUpdateTask implements Runnable {
	ExternalDbManager manager = null;
	public DomainDropdownsUpdateTask (
			ExternalDbManager manager
			) {
		this.manager = manager;
	}
	
	@Override
	public void run() {
		synchronized(this) {
			this.manager.buildDomainTopicMap();
		}
	}
	
}
