package ioc.liturgical.ws.models.ws.response.column.editor;

import java.util.ArrayList;
import java.util.Collection;
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
      "key": "eu.memorial~euMEM.title",
      "libKeysIndex": 1 // points to libraryKeys[1]
    },
    {
      "_id": "T002",
      "key": "eu.funeral~euFUN.Key0600.title",
      "libKeysIndex": 0
    },
    {
      "_id": "T003",
      "key": "misc~Mode5",
      "libKeysIndex": 3
    },
    {
      "_id": "T004",
      "key": "ho.s03~hoMA.FuneralEvlogVerse.text",
      "libKeysIndex": 2
    },
    {
      "_id": "T005",
      "key": "ho.s03~hoMA.FuneralEvlogVerse.text",
      "libKeysIndex": 2
    },
    {
      "_id": "T006",
      "key": "prayers~Allilouia3Doxa.text",
      "libKeysIndex": 4
    },
    {
      "_id": "T007",
      "key": "rubrical~Thrice",
      "libKeysIndex": 6
    },
    {
      "_id": "T008",
      "key": "prayers~DoxaPatri.text",
      "libKeysIndex": 5
    },
    {
      "_id": "T009",
      "key": "rubrical~Thrice",
      "libKeysIndex": 6
    }
  ],
  "libraryKeys": [
    {
      "_id": "eu.funeral~euFUN.Key0600.title",
      "ids": [1] // points to templateKeys[1]
    },
    {
      "_id": "eu.memorial~euMEM.title",
      "ids": [0] // points to templateKeys[0]
    },
    {
      "_id": "ho.s03~hoMA.FuneralEvlogVerse.text",
      "ids": [3,4] // points to templateKeys[3] and  templateKeys[4]
    },
    {
      "_id": "misc~Mode5",
      "ids": [2]
    },
    {
      "_id": "prayers~Allilouia3Doxa.text",
      "ids": [5]
    },
    {
      "_id": "prayers~DoxaPatri.text",
      "ids": [7]
    },
    {
      "_id": "rubrical~Thrice",
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
	@Expose List<TemplateTopicKey> templateKeys = new ArrayList<TemplateTopicKey>();
	/**
	 * libraryKeys holds the unique set of keys used by the template.  That is,
	 * each key occurs only one time.
	 */
	@Expose List<LibraryTopicKey> libraryKeys = new ArrayList<LibraryTopicKey>();
	@Expose Map<String,List<LibraryTopicKeyValue>> libraryKeyValues = new TreeMap<String,List<LibraryTopicKeyValue>>();
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
	public List<TemplateTopicKey> getTemplateKeys() {
		return templateKeys;
	}
	public void setTemplateKeys(List<TemplateTopicKey> templateKeys) {
		this.templateKeys = templateKeys;
	}
	public List<LibraryTopicKey> getLibraryKeys() {
		return libraryKeys;
	}
	public Map<String,LibraryTopicKeyValue> getEmptyLtkvMap() {
		Map<String,LibraryTopicKeyValue> result = new TreeMap<String,LibraryTopicKeyValue>();
		for (LibraryTopicKeyValue ltkv : this.libraryKeyValues.get(about.library)) {
			LibraryTopicKeyValue newLtkv =  new LibraryTopicKeyValue();
			newLtkv.set_id(ltkv.get_id());
			newLtkv.setIds(ltkv.getIds());
			newLtkv.setValue("");
			result.put(newLtkv._id, newLtkv);
		}
		return result;
	}
	public void setLibraryKeys(List<LibraryTopicKey> libraryKeys) {
		this.libraryKeys = libraryKeys;
	}
	public List<String> getTopics() {
		return topics;
	}
	public void setTopics(List<String> topics) {
		this.topics = topics;
	}
	
	public void addTemplateKeyValue(TemplateTopicKey value) {
		templateKeys.add(value);
	}
	
	public void addLibraryTopicKey(LibraryTopicKey value) {
		libraryKeys.add(value);
	}

	public void addLibraryTopicKeyValue(String library, LibraryTopicKeyValue value) {
		List<LibraryTopicKeyValue> list = new ArrayList<LibraryTopicKeyValue>();
		if (libraryKeyValues.containsKey(library)) {
			list = libraryKeyValues.get(library);
		}
		list.add(value);
		libraryKeyValues.put(library, list);
	}

	public Map<String,List<LibraryTopicKeyValue>> getLibraryKeyValues() {
		return libraryKeyValues;
	}
	
	public void addLibraryKeyValues(
			String library
			, Collection<LibraryTopicKeyValue> libraryKeyValues
			) {
		for (LibraryTopicKeyValue value :  libraryKeyValues) {
			this.addLibraryTopicKeyValue(library, value);
		}
	}
	
	public void setLibraryKeyValues(Map<String,List<LibraryTopicKeyValue>> libraryKeyValues) {
		this.libraryKeyValues = libraryKeyValues;
	}
}
