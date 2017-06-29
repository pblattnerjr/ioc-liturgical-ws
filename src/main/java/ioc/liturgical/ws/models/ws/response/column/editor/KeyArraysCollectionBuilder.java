package ioc.liturgical.ws.models.ws.response.column.editor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import ioc.liturgical.ws.constants.Constants;
import net.ages.alwb.utils.core.misc.AlwbGeneralUtils;

/**
 * Used to build a KeyArraysCollection
 * @author mac002
 *
 */
public class KeyArraysCollectionBuilder {
	private boolean printPretty = false;
	private String topic = "";
	private String library = "";
	public KeyArraysCollection collection = new KeyArraysCollection();
	private List<String> topics = new ArrayList<String>();
	private List<String> templateKeys = new ArrayList<String>();
	private Map<String, LibraryTopicKey> libraryKeyMap = new TreeMap<String,LibraryTopicKey>();
	private Map<String, LibraryTopicKeyValue> libraryKeyValueMap = new TreeMap<String,LibraryTopicKeyValue>();

	/**
	 * 
	 * @param library library from which the keys come
	 * @param topic topic or template name
	 */
	public KeyArraysCollectionBuilder(
			String library
			, String topic
			) {
		this.library = library;
		this.topic = topic;
	}
	
	/**
	 * 
	 * @param library library from which the keys come
	 * @param topic topic or template name
	 * @param printPretty print the json string in a user friendly way
	 */
	public KeyArraysCollectionBuilder(
			String library
			, String topic
			, boolean printPretty
			) {
		this.library = library;
		this.topic = topic;
		this.printPretty = printPretty;
	}

	/**
	 * Convenience method for when we have the topic and key 
	 * as separate values. 
	 * @param topic
	 * @param key
	 * @param value
	 * @param seq
	 */
	public void addTemplateKey(
			String topic
			, String key
			, String value
			, String seq
			)  throws MissingSeparatorException {
		this.addTemplateKey(
				topic + Constants.ID_DELIMITER + key
				, value
				, seq
		);
	}
	
	/**
	 * Add the template key and update the
	 * library keys as well
	 * @param key is actually topic + ~ + key
	 */
	public void addTemplateKey(
			String key
			, String value
			, String seq
			) throws MissingSeparatorException {
		// update the topic list
		if (key.contains(Constants.ID_DELIMITER)) {
			String[] parts = key.split(Constants.ID_DELIMITER);
			if (topics.contains(parts[0])) {
				// ignore
			} else {
				topics.add(parts[0]);
			}
			// update the map of library keys
			int templateKeyIndex = templateKeys.size();
			LibraryTopicKey ltk = new LibraryTopicKey(printPretty);
			LibraryTopicKeyValue ltkv = new LibraryTopicKeyValue(printPretty);
			if (libraryKeyMap.containsKey(key)) {
				ltk = libraryKeyMap.get(key);
			} else {
				ltk.set_id(key);
			}
			if (libraryKeyValueMap.containsKey(key)) {
				ltkv = libraryKeyValueMap.get(key);
			} else {
				ltkv.set_id(key);
				ltkv.setValue(value);
				ltkv.setSeq(seq);
			}
			ltk.addIdIndex(templateKeyIndex);
			ltkv.addIdIndex(templateKeyIndex);
			libraryKeyMap.put(key, ltk);
			libraryKeyValueMap.put(key, ltkv);
			// now add the template key
			templateKeys.add(key);
		} else {
			throw new MissingSeparatorException(key);
		}
	}

	public KeyArraysCollection getCollection() {
		collection = new KeyArraysCollection(printPretty);
		Map<String,Integer> libraryKeyIndex = new TreeMap<String,Integer>();
		int i = 0;
		for (String key : libraryKeyMap.keySet()) {
			collection.addLibraryTopicKey(libraryKeyMap.get(key));
			collection.addLibraryTopicKeyValue(library, libraryKeyValueMap.get(key));
			libraryKeyIndex.put(key, i);
			i++;
		}
		int s = templateKeys.size();
		for (i = 0; i < s; i++) {
			TemplateTopicKey tkv = new TemplateTopicKey(printPretty);
			tkv.set_id("T" + String.format("%03d", i+1));
			tkv.setKey(templateKeys.get(i));
			tkv.setLibKeysIndex(libraryKeyIndex.get(tkv.getKey()));
			collection.addTemplateKeyValue(tkv);
		}
		About about = new About(printPretty);
		about.setTemplate(topic);
		about.setLibrary(library);
		about.setTemplateKeyCount(collection.getTemplateKeys().size());
		about.setLibraryKeyCount(collection.getLibraryKeys().size());
		about.setRedundantKeyCount(about.getTemplateKeyCount() - about.getLibraryKeyCount());
		about.setCompression(AlwbGeneralUtils.compression(about.getLibraryKeyCount(), about.getTemplateKeyCount()));
		collection.setAbout(about);
		collection.setTopics(topics);
		return collection;
	}

	
}
