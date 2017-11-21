package net.ages.alwb.utils.core.misc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

/**
 * A generic class that supports a map
 * that uses a String as the key, 
 * and for each key there is a list of unique values of type T
 * @author mac002
 *
 * @param <String>
 * @param <T>
// */
public class ListMap<T> {
	
	private Map<String,List<T>> map = new TreeMap<String,List<T>>();
	
	public List<T> getList(String key) {
		return map.get(key);
	}
	
	public int size() {
		return map.size();
	}
	
	public Set<String> keySet() {
		return map.keySet();
	}
	
	public List<T> get(String key) {
		return map.get(key);
	}
	
	/**
	 * Adds up the count of items for each list for each key
	 * @return
	 */
    public int itemsCount() {
    	int result = 0;
    	for (String a : map.keySet()) {
    		result = result + map.get(a).size();
    	}
    	return result;
    }

	public Set<Entry<String, List<T>>> getEntrySet() {
		return map.entrySet();
	}
	
	/**
	 * Ensures that key is unique, and that 
	 * key has a list of unique values for t
	 * @param key
	 * @param t
	 */
	public void put(String key, T t) {
			if (map.containsKey(key)) {
				List<T> list = map.get(key);
				if (! list.contains(t)) {
					list.add(t);
					map.put(key, list);
				}
			} else {
				List<T> ids = new ArrayList<T>();
				ids.add(t);
				map.put(key, ids);
			}

	}

	public Map<String, List<T>> getMap() {
		return map;
	}

	public void setMap(Map<String, List<T>> map) {
		this.map = map;
	}
}
