package net.ages.alwb.utils.core.id.managers;

import static org.junit.Assert.*;

import org.junit.Test;

public class IdManagerTest {

	public static String simpleLibrary = "gr_gr_cog";
	public static String simpleEnglishLibrary = "en_us_dedes";
	public static String simpleTopic = "actors";
	public static String simpleKey = "Priest";
	public static IdManager joiner = new IdManager(simpleLibrary, simpleTopic, simpleKey);
	public static IdManager simpleIdManager = new IdManager(joiner.getId(), 1, 2);
	public static IdManager simpleEnglishIdManager = null;

	public static String complexLibrary = "gr_gr_cog~me.m01.d01~doxastakon.text";
	public static IdManager libraryJoiner = new IdManager(complexLibrary, simpleTopic, simpleKey);
	public static IdManager complexLibraryManager = new IdManager(libraryJoiner.getId(), 3, 4);
	
	public static String complexTopic = "gr_gr_cog~me.m01.d01~doxastakon.text";
	public static IdManager topicJoiner = new IdManager(simpleLibrary, complexTopic, simpleKey);
	public static IdManager complexTopicManager = new IdManager(topicJoiner.getId(), 2, 5);
	
	public static String complexKey = "gr_gr_cog~me.m01.d01~doxastakon.text";
	public static IdManager keyJoiner = new IdManager(simpleLibrary, simpleTopic, complexKey);
	public static IdManager complexKeyManager = new IdManager(keyJoiner.getId(), 1, 2);
	
	public static String superComplexKey = complexKey + "~" + complexKey;
	public static IdManager superComplexJoiner = new IdManager(complexLibrary, complexTopic, complexKey);
	public static IdManager superComplexManager = new IdManager(superComplexJoiner.getId(), 3, 6);

	@Test
	public void simpleTest() {
		assert simpleIdManager.getId().equals(joiner.getId());
	}

	public void simpleTestLibrary() {
		assert simpleIdManager.getLibrary().equals(joiner.getLibrary());
	}
	public void simpleTestTopic() {
		assert simpleIdManager.getTopic().equals(joiner.getTopic());
	}
	@Test
	public void complexLibraryIdTest() {
		assert complexLibraryManager.getId().equals(libraryJoiner.getId());
	}

	public void complexLibraryLibraryTest() {
		assert complexLibraryManager.getLibrary().equals(libraryJoiner.getLibrary());
	}
	public void complexLibraryTopicTest() {
		assert complexLibraryManager.getTopic().equals(libraryJoiner.getTopic());
	}
	public void complexLibraryKeyTest() {
		assert complexLibraryManager.getKey().equals(libraryJoiner.getKey());
	}
	public void simpleEnglishLibraryKeyTest() {
		simpleEnglishIdManager = new IdManager(
				simpleIdManager.getLibrary()
				, simpleIdManager.getTopic()
				, simpleIdManager.getKey()
				);
		simpleEnglishIdManager.setLibrary(simpleEnglishLibrary);
		assert simpleEnglishIdManager.getId().equals("en_us_dedes~actors~Priest");
	}
}
