package ioc.liturgical.ws.models.ws.response.column.editor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.ages.alwb.utils.core.misc.AlwbGeneralUtils;

/**
 * Used to build a KeyArraysCollection
 * @author mac002
 *
 */
public class KeyArraysCollectionBuilder {
	private boolean printPretty = false;
	private String title = "";
	public KeyArraysCollection collection = new KeyArraysCollection();
	private List<String> topics = new ArrayList<String>();
	private List<String> templateKeys = new ArrayList<String>();
	private Map<String, LibraryKeyValue> libraryKeyMap = new TreeMap<String,LibraryKeyValue>();

	/**
	 * 
	 * @param title title of the template or topic
	 */
	public KeyArraysCollectionBuilder(String title) {
		this.title = title;
	}
	
	/**
	 * 
	 * @param title title of the template or topic
	 * @param printPretty print the json string in a user friendly way
	 */
	public KeyArraysCollectionBuilder(String title, boolean printPretty) {
		this.title = title;
		this.printPretty = printPretty;
	}

	/**
	 * Convenience method for when we have the topic and key 
	 * as separate values. 
	 * @param topic
	 * @param key
	 */
	public void addTemplateKey(String topic, String key)  throws MissingSeparatorException {
		this.addTemplateKey(topic + "__" + key);
	}
	
	/**
	 * Add the template key and update the
	 * library keys as well
	 * @param key is actually topic + __ + key
	 */
	public void addTemplateKey(String key) throws MissingSeparatorException {
		// update the topic list
		if (key.contains("__")) {
			String[] parts = key.split("__");
			if (topics.contains(parts[0])) {
				// ignore
			} else {
				topics.add(parts[0]);
			}
			// update the map of library keys
			int templateKeyIndex = templateKeys.size();
			LibraryKeyValue lkv = new LibraryKeyValue(printPretty);
			if (libraryKeyMap.containsKey(key)) {
				lkv = libraryKeyMap.get(key);
			} else {
				lkv.set_id(key);
			}
			lkv.addIdIndex(templateKeyIndex);
			libraryKeyMap.put(key, lkv);
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
			collection.addLibraryKeyValue(libraryKeyMap.get(key));
			libraryKeyIndex.put(key, i);
			i++;
		}
		int s = templateKeys.size();
		for (i = 0; i < s; i++) {
			TemplateKeyValue tkv = new TemplateKeyValue(printPretty);
			tkv.set_id("T" + String.format("%03d", i+1));
			tkv.setKey(templateKeys.get(i));
			tkv.setLibKeysIndex(libraryKeyIndex.get(tkv.getKey()));
			collection.addTemplateKeyValue(tkv);
		}
		About about = new About(printPretty);
		about.setTemplate(title);
		about.setTemplateKeyCount(collection.getTemplateKeys().size());
		about.setLibraryKeyCount(collection.getLibraryKeys().size());
		about.setRedundantKeyCount(about.getTemplateKeyCount() - about.getLibraryKeyCount());
		about.setCompression(AlwbGeneralUtils.compression(about.getLibraryKeyCount(), about.getTemplateKeyCount()));
		collection.setAbout(about);
		collection.setTopics(topics);
		return collection;
	}

	
}
