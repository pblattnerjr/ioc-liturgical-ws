package net.ages.alwb.utils.transformers.adapters;

import static org.junit.Assert.*;

import org.junit.Test;

import net.ages.alwb.utils.transformers.adapters.models.LDOM;

public class AgesHtmlToTemplateHtmlTest {
	
	@Test
	public void testMetaData() {
		String url = "http://www.agesinitiatives.com/dcs/public/dcs/h/b/baptism/gr-en/index.html";
		AgesHtmlToEditableLDOM ages = new AgesHtmlToEditableLDOM(url, true);
		try {
			LDOM result = ages.toLDOM();
			assertTrue(result.getTopElement().getChildren().size() > 0);
		} catch (Exception e) {
			assertTrue(e.getMessage().length() == 0);
		}
	}

}
