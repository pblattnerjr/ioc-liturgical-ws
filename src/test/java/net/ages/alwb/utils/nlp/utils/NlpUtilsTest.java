package net.ages.alwb.utils.nlp.utils;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Test;

import org.ocmc.ioc.liturgical.utils.FileUtils;
import net.ages.alwb.utils.nlp.models.lemmatizer.LemmaLookup;

public class NlpUtilsTest {

	@Test
	public void testLemmatizer() {
		
		String path = "/Users/mac002/Git/ocmc-translation-projects/ioc-liturgical-ws/src/main/resources/";
		String d = path + "models/en-lemmatizer.dict";
		File f = new File(d);
		LemmaLookup looky = new LemmaLookup();
		for (String line : FileUtils.linesFromFile(f)) {
			String[] parts = line.split("\\t");
			looky.addLemma(parts[0], parts[1], parts[2]);
		}

		FileUtils.writeFile(path + "json/en_lemmaLookup.json", looky.toJsonString());
		String text = "The Lord, who is powerful in wars, uncovered the bottom of the deep sea, and led His people across its dry land, but there He covered their enemies with its waters. For He has gained honour for Himself!";
		try {
			for (String token : NlpUtils.getTokens(
 	 	  			text
 	 				, true // convertToLowerCase
 	 				, true //  ignorePunctuation
 	 				, false //  ignoreLatin
 	 				, false //  ignoreNumbers
 	 				, false //  removeDiacritics
 	 	    	)) {
			System.out.println(token + ": " + NlpUtils.getLemma(token));
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertTrue(text.length() > 0);
	}

}
