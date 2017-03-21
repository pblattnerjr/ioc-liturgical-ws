package net.ages.alwb.utils.core.id.managers;


import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import ioc.liturgical.ws.constants.Constants;

/**
 * Manages Library-Topic-Key IDs, where parts are delimited by Constants.ID_DELIMITER.
 * 
 * @author mac002
 *
 */
public class LTKIdManager {
	private static final Logger logger = LoggerFactory.getLogger(LTKIdManager.class);

	private String delimiter =  Constants.ID_DELIMITER;
	private String splitter = Constants.ID_SPLITTER;
	private IdManager library;
	private IdManager topic;
	private IdManager key;
	
	public LTKIdManager(String library, String topic, String key) {
		this.library = new IdManager(library);
		this.topic = new IdManager(topic);
		this.key = new IdManager(key);
	}
	
	public String getDelimiter() {
		return delimiter;
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	public String getSplitter() {
		return splitter;
	}

	public void setSplitter(String splitter) {
		this.splitter = splitter;
	}

	public IdManager getLibrary() {
		return library;
	}

	public void setLibrary(IdManager library) {
		this.library = library;
	}
	
	public void setLibraryAsId(String library, String topic, String key) {
		this.library = new IdManager(library, topic, key);
	}

	public IdManager getTopic() {
		return topic;
	}

	public void setTopic(IdManager topic) {
		this.topic = topic;
	}

	public void setTopicAsId(String library, String topic, String key) {
		this.topic = new IdManager(library, topic, key);
	}

	public IdManager getKey() {
		return key;
	}

	public void setKey(IdManager key) {
		this.key = key;
	}

	public void setKeyAsId(String library, String topic, String key) {
		this.key = new IdManager(library, topic, key);
	}

	public boolean isComplexLibrary() {
		return library.partCount() > 1;
	}
	
	public boolean isComplexTopic() {
		return topic.partCount() > 1;
	}
	
	public boolean isComplexKey() {
		return key.partCount() > 1;
	}
	
	public boolean isComplexId() {
		return isComplexLibrary() || isComplexTopic() || isComplexKey();
	}
	
	public String get(int index) {
		String result = "";
		return result;
	}
}
