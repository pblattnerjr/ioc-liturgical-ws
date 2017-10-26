package net.ages.alwb.tasks;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import ioc.liturgical.ws.constants.Constants;
import ioc.liturgical.ws.managers.databases.external.neo4j.ExternalDbManager;
import net.ages.alwb.utils.core.file.AlwbFileUtils;
import net.ages.alwb.utils.transformers.adapters.MetaTemplateToPdf;
import net.ages.alwb.utils.transformers.adapters.models.MetaTemplate;

/**
 * Runs a task (separate thread) to generate the Xelatex content for
 * a tex file.  The tex file can then be used to create a PDF.
 * @author mac002
 *
 */
public class OntologyTagsUpdateTask implements Runnable {
	ExternalDbManager manager = null;
	public OntologyTagsUpdateTask (
			ExternalDbManager manager
			) {
		this.manager = manager;
	}
	
	@Override
	public void run() {
		synchronized(this) {
			this.manager.updateOntologyTags();
		}
	}
	
}
