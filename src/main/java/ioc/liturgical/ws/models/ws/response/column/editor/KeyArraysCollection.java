package ioc.liturgical.ws.models.ws.response.column.editor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.google.gson.annotations.Expose;

import net.ages.alwb.utils.core.datastores.json.models.AbstractModel;

/**
 * Holds the arrays for templates / topics and library keys
 * It is best to create this using the KeyArraysCollectionBuilder.
 * 
 * When sent to the client, it will be Json.  The lists become arrays.
 * 
 * Example:
 * 
 * {
  "about": {
    "template": "bk_memorial",
    "dateGenerated": "2017-06-23T11:38:01.401",
    "logname": "",
    "templateKeyCount": 9,
    "libraryKeyCount": 7,
    "redundantKeyCount": 2,
    "compression": "22%"
  },
  "templateKeys": [
    {
      "_id": "T001",
      "key": "eu.memorial__euMEM.title",
      "libKeysIndex": 1 // points to libraryKeys[1]
    },
    {
      "_id": "T002",
      "key": "eu.funeral__euFUN.Key0600.title",
      "libKeysIndex": 0
    },
    {
      "_id": "T003",
      "key": "misc__Mode5",
      "libKeysIndex": 3
    },
    {
      "_id": "T004",
      "key": "ho.s03__hoMA.FuneralEvlogVerse.text",
      "libKeysIndex": 2
    },
    {
      "_id": "T005",
      "key": "ho.s03__hoMA.FuneralEvlogVerse.text",
      "libKeysIndex": 2
    },
    {
      "_id": "T006",
      "key": "prayers__Allilouia3Doxa.text",
      "libKeysIndex": 4
    },
    {
      "_id": "T007",
      "key": "rubrical__Thrice",
      "libKeysIndex": 6
    },
    {
      "_id": "T008",
      "key": "prayers__DoxaPatri.text",
      "libKeysIndex": 5
    },
    {
      "_id": "T009",
      "key": "rubrical__Thrice",
      "libKeysIndex": 6
    }
  ],
  "libraryKeys": [
    {
      "_id": "eu.funeral__euFUN.Key0600.title",
      "ids": [1] // points to templateKeys[1]
    },
    {
      "_id": "eu.memorial__euMEM.title",
      "ids": [0] // points to templateKeys[0]
    },
    {
      "_id": "ho.s03__hoMA.FuneralEvlogVerse.text",
      "ids": [3,4] // points to templateKeys[3] and  templateKeys[4]
    },
    {
      "_id": "misc__Mode5",
      "ids": [2]
    },
    {
      "_id": "prayers__Allilouia3Doxa.text",
      "ids": [5]
    },
    {
      "_id": "prayers__DoxaPatri.text",
      "ids": [7]
    },
    {
      "_id": "rubrical__Thrice",
      "ids": [6,8]
    }
  ],
  "topics": [
    "eu.memorial",
    "eu.funeral",
    "misc",
    "ho.s03",
    "prayers",
    "rubrical"
  ]
}

 * @see KeyArraysCollectionBuilder
 * @author mac002
 *
 */
public class KeyArraysCollection extends AbstractModel {
	@Expose About about = null;
	/**
	 * templateKeys holds the list of keys used by a template or a topic.
	 * There can be redundant keys in the templateList
	 */
	@Expose List<TemplateKeyValue> templateKeys = new ArrayList<TemplateKeyValue>();
	/**
	 * libraryKeys holds the unique set of keys used by the template.  That is,
	 * each key occurs only one time.
	 */
	@Expose List<LibraryKeyValue> libraryKeys = new ArrayList<LibraryKeyValue>();
	@Expose List<String> topics = new ArrayList<String>();
	private Map<String, Integer> indexMap = new TreeMap<String,Integer>();
	
	public KeyArraysCollection() {
		super();
	}
	public KeyArraysCollection(boolean printPretty) {
		super();
		super.setPrettyPrint(printPretty);
	}
	public About getAbout() {
		return about;
	}
	public void setAbout(About about) {
		this.about = about;
	}
	public List<TemplateKeyValue> getTemplateKeys() {
		return templateKeys;
	}
	public void setTemplateKeys(List<TemplateKeyValue> templateKeys) {
		this.templateKeys = templateKeys;
	}
	public List<LibraryKeyValue> getLibraryKeys() {
		return libraryKeys;
	}
	public void setLibraryKeys(List<LibraryKeyValue> libraryKeys) {
		this.libraryKeys = libraryKeys;
	}
	public List<String> getTopics() {
		return topics;
	}
	public void setTopics(List<String> topics) {
		this.topics = topics;
	}
	
	public void addTemplateKeyValue(TemplateKeyValue value) {
		templateKeys.add(value);
	}
	
	public void addLibraryKeyValue(LibraryKeyValue value) {
		libraryKeys.add(value);
	}
	
}
