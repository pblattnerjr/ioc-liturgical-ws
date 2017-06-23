package ioc.liturgical.ws.constants;

import org.junit.Test;

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
}
