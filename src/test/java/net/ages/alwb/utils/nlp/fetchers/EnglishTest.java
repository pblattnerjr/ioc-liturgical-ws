package net.ages.alwb.utils.nlp.fetchers;

import static org.junit.Assert.*;

import java.util.List;
import org.junit.Test;

import net.ages.alwb.utils.nlp.models.WordSenseGev;

public class EnglishTest {

	@Test
	public void test() {
		Ox3kWordSenses e = null;
		e = new Ox3kWordSenses(
				Ox3kUtils.DOC_SOURCE.DISK
				, Ox3kUtils.DOC_SOURCE.DISK
				, "/volumes/ssd2/canBeRemoved/wordlist"
				, true
				);
		List<WordSenseGev> lemmaSenses = e.getLemmaSenses();
		int dumpStart = 100;
		int dumpEnd = 110;
		for (int i = dumpStart; i < dumpEnd; i++) {
			System.out.println(lemmaSenses.get(i).toJsonString());
		}
		System.out.println(lemmaSenses.size());
		assertTrue(lemmaSenses.size() > 0);
	}

}
