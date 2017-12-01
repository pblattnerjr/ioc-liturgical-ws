package ioc.liturgical.ws.constants;

import org.junit.Test;
import org.ocmc.ioc.liturgical.schemas.constants.ENDPOINTS_DB_API;

public class ENDPOINTS_DB_APITest {

	@Test
	public void testDocsEndpoint() {
		String path = ENDPOINTS_DB_API.DOCS.pathname;
		assert(path.equals("/db/api/v1/docs"));
	}


	@Test
	public void testRelsEndpoint() {
		String path = ENDPOINTS_DB_API.LINKS.pathname;
		assert(path.equals("/db/api/v1/links"));
	}

	@Test
	public void testRelsEndpointLibraryWildcard() {
		String path = ENDPOINTS_DB_API.LINKS.toLibraryPath();
		assert(path.equals("/db/api/v1/links/*"));
	}

	@Test
	public void testRelsEndpointDropdownsText() {
		String path = ENDPOINTS_DB_API.DROPDOWNS_TEXTS.pathname;
		assert(path.equals("/db/api/v1/dropdowns/texts"));
	}

	@Test
	public void testConceptsEndpoint() {
		String path = ENDPOINTS_DB_API.CONCEPTS.pathname;
		assert(path.equals("/db/api/v1/ontology/concepts"));
	}

	@Test
	public void testGroupsEndpoint() {
		String path = ENDPOINTS_DB_API.GROUPS.pathname;
		assert(path.equals("/db/api/v1/ontology/groups"));
	}
	@Test
	public void testObjectsEndpoint() {
		String path = ENDPOINTS_DB_API.OBJECTS.pathname;
		assert(path.equals("/db/api/v1/ontology/objects"));
	}
	@Test
	public void testPersonsEndpoint() {
		String path = ENDPOINTS_DB_API.HUMANS.pathname;
		assert(path.equals("/db/api/v1/ontology/humans"));
	}
	@Test
	public void testPlacesEndpoint() {
		String path = ENDPOINTS_DB_API.PLACES.pathname;
		assert(path.equals("/db/api/v1/ontology/places"));
	}
	@Test
	public void testGrLibTopicsEndpoint() {
		String path = ENDPOINTS_DB_API.DROPDOWNS_GR_LIB_TOPICS.pathname;
		assert(path.equals("/db/api/v1/dropdowns/grlibtopics"));
	}
	@Test
	public void testViewTemplateEndpoint() {
		String path = ENDPOINTS_DB_API.VIEW_TEMPLATE.pathname;
		assert(path.equals("/db/api/v1/docs/viewtemplate"));
	}
	@Test
	public void testViewTopicEndpoint() {
		String path = ENDPOINTS_DB_API.VIEW_TOPIC.pathname;
		assert(path.equals("/db/api/v1/docs/viewtopic"));
	}
	@Test
	public void testViewTopicEndpointWithParms() {
		String path = ENDPOINTS_DB_API.VIEW_TOPIC.toLibraryPath();
		assert(path.equals("/db/api/v1/docs/viewtopic/*"));
	}
	@Test
	public void testCreateUpdateValue() {
		String path = ENDPOINTS_DB_API.VALUE.toLibraryPath();
		assert(path.equals("/db/api/v1/docs/value/*"));
	}
	@Test
	public void testAgesIndexTableData() {
		String path = ENDPOINTS_DB_API.AGES_INDEX.pathname;
		assert(path.equals("/db/api/v1/docs/agesindex"));
	}
	@Test
	public void testAgesReadOnlyTemplate() {
		String path = ENDPOINTS_DB_API.AGES_READ_ONLY_TEMPLATE.toLibraryPath();
		assert(path.equals("/db/api/v1/docs/agesreadonlytemplate/*"));
	}
	@Test
	public void testAgesReactTemplate() {
		String path = ENDPOINTS_DB_API.AGES_REACT_TEMPLATE.toLibraryPath();
		assert(path.equals("/db/api/v1/docs/agesreacttemplate/*"));
	}
	@Test
	public void testCloneLibrary() {
		String path = ENDPOINTS_DB_API.CLONE.toLibraryPath();
		assert(path.equals("/db/api/v1/docs/clone/*"));
	}
}
