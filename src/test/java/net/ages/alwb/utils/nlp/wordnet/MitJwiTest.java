package net.ages.alwb.utils.nlp.wordnet;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.gson.JsonObject;

import net.ages.alwb.utils.nlp.wordnet.models.Synset;

public class MitJwiTest {

	@Test
	public void test() {
		MitJwi wn = new MitJwi(true);
		System.out.println(wn.toJsonString("bed"));
		assertTrue(1 == 1);
	}

}
