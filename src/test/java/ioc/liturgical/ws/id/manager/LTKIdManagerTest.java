package ioc.liturgical.ws.id.manager;

import org.junit.Test;

import net.ages.alwb.utils.core.id.managers.LTKIdManager;

public class LTKIdManagerTest {

	public static String simpleLibrary = "gr_gr_cog";
	public static String simpleTopic = "actors";
	public static String simpleKey = "Priest";
	public static LTKIdManager simpleManager = new LTKIdManager(simpleLibrary, simpleTopic, simpleKey);
	
	public static String complexLibrary = "gr_gr_cog~me.m01.d01~doxastakon.text";
	public static LTKIdManager complexLibraryManager = new LTKIdManager(complexLibrary, simpleTopic, simpleKey);

	public static String complexTopic = "gr_gr_cog~me.m01.d01~doxastakon.text";
	public static LTKIdManager complexTopicManager = new LTKIdManager(simpleLibrary, complexTopic, simpleKey);
	
	public static String complexKey = "gr_gr_cog~me.m01.d01~doxastakon.text";
	public static LTKIdManager complexKeyManager = new LTKIdManager(simpleLibrary, simpleTopic, complexKey);
	
	public static String superComplexKey = complexKey + "~" + complexKey;
	public static LTKIdManager superComplexKeyManager = new LTKIdManager(simpleLibrary, simpleTopic, superComplexKey);

	@Test
	public void simpleIdIsSimple() {
		assert(simpleManager.isComplexId() == false);
	}

	@Test
	public void simpleIdLibraryMatches() {
		assert(simpleManager.getLibrary().getId().equals(simpleLibrary));
	}
	
	@Test
	public void simpleIdTopicMatches() {
		assert(simpleManager.getTopic().getId().equals(simpleTopic));
	}
	@Test
	public void simpleIdKeyMatches() {
		assert(simpleManager.getKey().getId().equals(simpleKey));
	}

	@Test
	public void complexLibraryIdIsComplex() {
		assert(complexLibraryManager.isComplexId() == true);
	}

	@Test
	public void complexLibraryIdLibraryMatches() {
		assert(complexLibraryManager.getLibrary().getId().equals(complexLibrary));
	}

	@Test
	public void complexIdIsComplex() {
		assert(complexTopicManager.isComplexId() == true);
	}

	@Test
	public void complexTopixIdTopicMatches() {
		assert(complexTopicManager.getTopic().getId().equals(complexTopic));
	}

	@Test
	public void complexkeyIdIsComplex() {
		assert(complexKeyManager.isComplexId() == true);
	}

	@Test
	public void complexKeyIdTopicMatches() {
		assert(complexKeyManager.getKey().getId().equals(complexKey));
	}

	@Test
	public void superComplexkeyIdIsComplex() {
		assert(superComplexKeyManager.isComplexId() == true);
	}

	@Test
	public void superComplexKeyIdTopicMatches() {
		assert(superComplexKeyManager.getKey().getId().equals(superComplexKey));
	}

}
