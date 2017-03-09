package ioc.liturgical.ws.managers.databases.external.neo4j.utils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

import org.apache.commons.lang3.math.NumberUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import ioc.liturgical.ws.constants.BIBLICAL_BOOKS;
import ioc.liturgical.ws.constants.DROPDOWN_VALUES;
import ioc.liturgical.ws.constants.HTTP_RESPONSE_CODES;
import ioc.liturgical.ws.constants.LITURGICAL_BOOKS;
import ioc.liturgical.ws.models.ResultJsonObjectArray;

public class DomainTopicMapBuilder {

    private Node root = new Node();
    
	private static final Pattern PATH_SEPARATOR = Pattern.compile("\\.|~|:|,");
	
	/**
	 * This is where the magic happens to create the correct sub-dropdown menus.
	 * 
	 * @param path 
	 */
    public void addPath(String path) {
        String[] names = PATH_SEPARATOR.split(path);
        Node node = root;
        for (String name : names) {
        	if (names[1].equals("hi") && names[2].matches(name)) {
        		node = node.getChild("hi"+name);
        	} else {
              node = node.getChild(name);
        	}
        }
    }
    
    public JsonObject toJsonObject() {
    	return root.toJsonObject();
    }
    

    
    private void getJsonObject(String name, Node node, JsonObject json) {
        Map<String, Node> children = node.getChildren();
        if (children.isEmpty())
            return;
        for (Map.Entry<String, Node> child : children.entrySet()) {
        	JsonArray array = getTheJsonArray(child.getValue());
        	if (array != null) {
        		if (name.length() > 0) {
            		json.add(name+"."+child.getKey(), array);
        		} else {
            		json.add(child.getKey(), array);
        		}
        	}
            getJsonObject(child.getKey(), child.getValue(), json);
        }
    }

    private JsonArray getTheJsonArray(Node node) {
    	JsonArray array = new JsonArray();
    	array.add(new DropdownItem("Any", "*").toJsonObject());
        Map<String, Node> children = node.getChildren();
        if (children.isEmpty())
            return null;
        for (Map.Entry<String, Node> child : children.entrySet()) {
        	String key = child.getKey().trim();
        	String label = DROPDOWN_VALUES.getLabel(key);
            array.add(new DropdownItem(label, key).toJsonObject());
        }
        return array;
    }

    public JsonObject getArray() {
    	JsonObject result = new JsonObject();
    	getJsonObject("", this.root, result);
    	return result;
    }


	public JsonObject getDropdownItems() {
		ResultJsonObjectArray result = new ResultJsonObjectArray(true);
		try  {
			DomainsService domainsService = new DomainsService();
			List<String> types = new ArrayList<String>();
			types.add("Biblical");
			types.add("Liturgical");

			DropdownArray biblicalDomains = domainsService.getBiblicalDomains();
			DropdownArray liturgicalDomains = domainsService.getLiturgicalDomains();

			List<JsonObject> list = new ArrayList<JsonObject>();
			
			for (String type: types) {
		    	this.root = new Node();
				TopicsService topicsInDb = new TopicsService();
				List<String> topics = null;
				switch (type) {
				case "Biblical": {
			    	topics = topicsInDb.getBiblicalTopicsFor("", "");
			    	JsonObject biblicalJson = new JsonObject();
			    	biblicalJson.add("all", this.getGenericBiblicalBooksDropdownItems().get("values"));
			    	JsonObject biblicalDomainsObject = biblicalDomains.toJsonObject();
			    	JsonArray biblicalTopicsArray = biblicalDomainsObject.get("items").getAsJsonArray();
			    	biblicalJson.add("domains", biblicalTopicsArray);
			    	for (String topic : topics) {
			            addPath(topic);
			        }
			    	biblicalJson.add("topics", getArray());
			    	list.add(biblicalJson);
					break;
				}
				case "Liturgical": {
			    	topics = topicsInDb.getLiturgicalTopicsFor("", "");
			    	JsonObject liturgicalJson = new JsonObject();
			    	liturgicalJson.add("all", this.getGenericLiturgicalBooksDropdownItems().get("values"));
			    	JsonObject liturgicalDomainsObject = liturgicalDomains.toJsonObject();
			    	JsonArray liturgicalTopicsArray = liturgicalDomainsObject.get("items").getAsJsonArray();
			    	liturgicalJson.add("domains", liturgicalTopicsArray);
			    	for (String topic : topics) {
			            addPath(topic);
			        }
			    	liturgicalJson.add("topics", getArray());
			    	list.add(liturgicalJson);
					break;
				}
				}
			}
	    	result.setResult(list);
		} catch (Exception e) {
			result.setStatusCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
			result.setStatusMessage(e.getMessage());
		}
		return result.toJsonObject();
	}

	
	public JsonObject getGenericBiblicalBooksDropdownItems() {
		ResultJsonObjectArray result = new ResultJsonObjectArray(true);
		try  {
			LabelsService labelsService = new LabelsService();
			List<JsonObject> list = new ArrayList<JsonObject>();
			List<String> topics = labelsService.getLabelsFor("Biblical");
			Map<String, DropdownItem> bookMap = new TreeMap<String,DropdownItem>();
			Map<String, DropdownItem> chapterMap = new TreeMap<String,DropdownItem>();
	    	JsonObject json = new JsonObject();
	    	for (String topic : topics) {
	    		String[] parts = topic.split(",");
	    		for (int i=0; i < parts.length; i++) {
	    			String key = parts[i].trim();
	    			if (BIBLICAL_BOOKS.containsKey(key)) {
	    				if (! bookMap.containsKey(key)) {
	    					bookMap.put(key, new DropdownItem(BIBLICAL_BOOKS.getLabel(key), key));
	    				}
	    			} else if (key.toLowerCase().startsWith("c") && NumberUtils.isNumber(key.substring(key.length()-1))) {
	    				if (! chapterMap.containsKey(key)) {
	    					chapterMap.put(key, new DropdownItem(key));
	    				} 
	    			}
	    		}
	        }
	    	DropdownArray booksArray = new DropdownArray();
	    	booksArray.add(new DropdownItem("Any", "*"));
	    	booksArray.setItems(bookMap);
	    	json.add("books", booksArray.toJsonObject().get("items").getAsJsonArray());
	    	DropdownArray chaptersArray = new DropdownArray();
	    	chaptersArray.add(new DropdownItem("Any", "*"));
	    	chaptersArray.setItems(chapterMap);
	    	json.add("chapters", chaptersArray.toJsonObject().get("items").getAsJsonArray());
	    	list.add(json);
	    	result.setResult(list);
		} catch (Exception e) {
			result.setStatusCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
			result.setStatusMessage(e.getMessage());
		}
		return result.toJsonObject();
	}

	public JsonObject getGenericLiturgicalBooksDropdownItems() {
		ResultJsonObjectArray result = new ResultJsonObjectArray(true);
		try  {
			LabelsService labelsService = new LabelsService();
			List<JsonObject> list = new ArrayList<JsonObject>();
			List<String> topics = labelsService.getLabelsFor("Liturgical");
			Map<String, DropdownItem> bookMap = new TreeMap<String,DropdownItem>();
	    	JsonObject json = new JsonObject();
	    	for (String topic : topics) {
	    		String[] parts = topic.split(",");
	    		for (int i=0; i < parts.length; i++) {
	    			String key = parts[i].trim();
	    			if (key.equals("le")) {
	    				String leKey = "le.ep";
    					bookMap.put(leKey, new DropdownItem(LITURGICAL_BOOKS.getLabel(leKey), leKey));
    					leKey = "le.go";
    					bookMap.put(leKey, new DropdownItem(LITURGICAL_BOOKS.getLabel(leKey), leKey));
    					leKey = "le.pr";
    					bookMap.put(leKey, new DropdownItem(LITURGICAL_BOOKS.getLabel(leKey), leKey));
	    			} else {
		    			if (LITURGICAL_BOOKS.containsKey(key)) {
		    				if (! bookMap.containsKey(key)) {
		    					bookMap.put(key, new DropdownItem(LITURGICAL_BOOKS.getLabel(key), key));
		    				}
		    			}
	    			}
	    		}
	        }
	    	DropdownArray booksArray = new DropdownArray();
	    	booksArray.add(new DropdownItem("Any", "*"));
	    	booksArray.setItems(bookMap);
	    	json.add("books", booksArray.toJsonObject().get("items").getAsJsonArray());
	    	list.add(json);
	    	result.setResult(list);
		} catch (Exception e) {
			result.setStatusCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
			result.setStatusMessage(e.getMessage());
		}
		return result.toJsonObject();
	}

	public static void main(String[] args) {
    	DomainTopicMapBuilder builder = new DomainTopicMapBuilder();
    	System.out.println(builder.getGenericBiblicalBooksDropdownItems().toString());
    	System.out.println(builder.getGenericLiturgicalBooksDropdownItems().toString());
	}
}