package net.ages.alwb.utils.core.misc;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map.Entry;

import org.junit.Test;

public class GenericMapOfListsTest {

	@Test
	public void test() {
		ListMap<String> mLists = new ListMap<String>();
		mLists.put("a", "apple");
		mLists.put("a", "apple");
		mLists.put("a", "avocado");
		mLists.put("b", "banana");
		mLists.put("c", "carrot");
		mLists.put("c", "cabbage");
		mLists.put("c", "carrot");
		
		for (Entry<String,List<String>> entry : mLists.getMap().entrySet()) {
			System.out.println(entry.getKey());
			for (String s : entry.getValue()) {
				System.out.println("\t" + s);
			}
		}
		fail("Not yet implemented");
	}

}
