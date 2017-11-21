package net.ages.alwb.utils.nlp.models;

import static org.junit.Assert.*;

import org.junit.Test;

import net.ages.alwb.utils.nlp.fetchers.Ox3kUtils;

public class GevLexiconTest {

	@Test
	public void test() {
		GevLexicon lexicon = new GevLexicon(
				Ox3kUtils.DOC_SOURCE.DISK
				, Ox3kUtils.DOC_SOURCE.DISK
				, true
				, "/Volumes/ssd2/canBeRemoved/wordlist"
				, false
		);
		lexicon.load();
		System.out.println("done");
		assertTrue("dude".length() > 0);
	}

}
