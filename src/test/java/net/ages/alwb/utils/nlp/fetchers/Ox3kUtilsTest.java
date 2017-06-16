package net.ages.alwb.utils.nlp.fetchers;

import static org.junit.Assert.*;

import java.util.List;

import org.h2.util.StringUtils;
import org.junit.Test;

public class Ox3kUtilsTest {

	@Test
	public void testHrefRetrieval() {
		List<Href> hrefs = Ox3kUtils.getListHrefs(
				Ox3kUtils.DOC_SOURCE.DISK
				, "/volumes/ssd2/canBeRemoved/wordlist"
				);
		for (Href href : hrefs) {
			System.out.println(href.lemma);
		}
		assertTrue(hrefs.size() > 0);
	}

}
