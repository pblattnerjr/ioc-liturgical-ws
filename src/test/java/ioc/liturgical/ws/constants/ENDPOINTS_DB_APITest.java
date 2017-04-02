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
}
