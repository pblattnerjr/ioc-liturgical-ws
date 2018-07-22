package net.ages.alwb.tasks;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ioc.liturgical.ws.managers.databases.external.neo4j.ExternalDbManager;

import org.ocmc.ioc.liturgical.schemas.constants.DATA_SOURCES;
import org.ocmc.ioc.liturgical.schemas.constants.nlp.UD_DEP_REL_LABEL;
import org.ocmc.ioc.liturgical.schemas.models.db.docs.nlp.UDtbSentence;
import org.ocmc.ioc.liturgical.schemas.models.db.docs.nlp.UDtbWord;
import org.ocmc.ioc.liturgical.schemas.models.labels.UiLabel;
import org.ocmc.ioc.liturgical.schemas.models.ws.response.ResultJsonObjectArray;
import org.ocmc.ioc.liturgical.utils.ErrorUtils;
import org.ocmc.ioc.liturgical.utils.FileUtils;
import net.ages.alwb.utils.nlp.utils.PerseusPostagMapper;
import net.ages.alwb.utils.nlp.utils.UDfeatsMapper;

/**
 * Runs a task (separate thread) to read the Perseus Treebank data
 * from Github and write it to the database
 * .
 * @author mac002
 *
 */
public class UdCreateTagsTask implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(UdCreateTagsTask.class);

	private ExternalDbManager manager = null;
	private String requestor = "";
	boolean deleteFirst = true;
	long breakAt = -1;
	Map<String,JsonArray> tags = null;
	String topic = "";
	String elLibrary = "el_sys_ilr";
	String enLibrary = "en_sys_ilr";
	
	public UdCreateTagsTask (
			ExternalDbManager manager
			, String requestor
			, Map<String,JsonArray> tags
			, boolean deleteFirst
			, long numberOfSentences
			) {
		this.manager = manager;
		this.requestor = requestor;
		this.tags = tags;
		this.deleteFirst = deleteFirst;
		this.breakAt = numberOfSentences;
	}

	@Override
	public void run() {
		for (Entry<String,JsonArray>  entry : this.tags.entrySet()) {
			String topic = entry.getKey();
			for (JsonElement e : entry.getValue()) {
				JsonObject o = e.getAsJsonObject();
				String key = o.get("key").getAsString();
				String value = o.get("value").getAsString();
				UiLabel label = new UiLabel(this.enLibrary, topic, key, value);
				System.out.println(label.id + " = " + label.value);
				this.manager.addLTKDbObject(this.requestor, label.toJsonString());
				label = new UiLabel(this.elLibrary, topic, key, value);
				System.out.println(label.id + " = " + label.value);
				this.manager.addLTKDbObject(this.requestor, label.toJsonString());
			}
		}
	}
}