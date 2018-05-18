package net.ages.alwb.utils.core.generics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * When you need to keep 
 * a map of objects
 * and
 * a map of Lists
 * and
 * the key for each is the same,
 * you can use this generic class.
 * @author mac002
 *
 * @param <T1> the type of the objects in the simple map
 * @param <T2> the type of the objects in the lists in the map with Lists.
 */
public class MultiMapWithList <T1, T2>{
	private Map<String,T1> mapSimple = new TreeMap<String,T1>();
	private Map<String,List<T2>> mapWithLists = new TreeMap<String,List<T2>>();
	private int maxListSize = 0;

	/**
	 * The parameterless constructor will contain a mapWithLists such that the lists can contain an infinite number of objects
	 */
	public MultiMapWithList () {
		this.maxListSize = 0;
	}

	/**
	 * Restricts the number of objects in each list in the mapWithLists to maxListSize
	 * 
	 * @param maxListSize
	 */
	public MultiMapWithList (int maxListSize) {
		this.maxListSize = maxListSize;
	}
	
	public int size() {
		return mapSimple.size();
	}
	public Map<String, T1> getMapSimple() {
		return mapSimple;
	}
	
	public Collection<T1> getMapSimpleValues() {
		return mapSimple.values();
	}

	public void addValueToMapSimple(String key, T1 value) {
		mapSimple.put(key, value);
	}
	
	public T1 getValueFromMapSimple(String key) {
		return mapSimple.get(key);
	}
	
	public boolean mapSimpleContainsValue(String key) {
		return mapSimple.containsKey(key);
	}
	
	public void setMapSimple(Map<String, T1> resultWordList) {
		this.mapSimple = resultWordList;
	}
	
	public List<T2> getListForKey(String key) {
		return mapWithLists.get(key);
	}
	
	public Map<String, List<T2>> getMapWithLists() {
		return mapWithLists;
	}
	
	public void setMapWithLists(Map<String, List<T2>> mapWithList) {
		this.mapWithLists = mapWithList;
	}
	
	public boolean existsKeyInMapWithLists(String key) {
		return mapWithLists.containsKey(key);
	}
	
	/**
	 * Add a concordance line for the specified key
	 * @param key
	 * @param value
	 */
	public void addValueToMapWithLists(String key, T2 value) {
		List<T2> list = null;
		if (maxListSize > 0) {
			if (this.mapWithLists.containsKey(key)) {
				list = this.mapWithLists.get(key);
				if (list.size() <= maxListSize) {
					list.add(value);
				}
			} else {
				list = new ArrayList<T2>();
				list.add(value);
			}
			this.mapWithLists.put(key, list);
		}
	}
	
	public T2 getFirstValueFromMapList() {
		if (mapWithLists.size() > 0) {
			String key = "";
			for (String k : mapWithLists.keySet()) {
				key = k;
				break;
			}
			return this.getFirstValueFromMapList(key);
		} else {
			return null;
		}
	}
	
	public T2 getFirstValueFromMapList(String key) {
		if (mapWithLists.size() > 0) {
			if (mapWithLists.containsKey(key)) {
				return mapWithLists.get(key).get(0);
			} 
		}
		return null;
	}
	
	/**
	 * Get the size of the list for the specified key
	 * @param key
	 * @return
	 */
	public int listSize(String key) {
		if (mapWithLists.containsKey(key)) {
			return mapWithLists.get(key).size();
		} else {
			return 0;
		}
	}

	public int getMaxListSize() {
		return maxListSize;
	}
	
	/**
	 * Set the maximum number of objects each list can contain in the mapWithLists.
	 * @param maxListSize - zero means infinite.  > zero is exact number of maximum objects
	 */
	public void setMaxListSize(int maxListSize) {
		this.maxListSize = maxListSize;
	}

}
