package net.ages.alwb.utils.transformers.adapters;

import static org.junit.Assert.*;

import org.junit.Test;

import net.ages.alwb.utils.transformers.adapters.models.PopulatedObjectModel;

public class AgesHtmlToTemplateHtmlTest {
	
	@Test
	public void testMetaData() {
		String url = "http://www.agesinitiatives.com/dcs/public/dcs/h/b/baptism/gr-en/index.html";
		AgesHtmlToEditablePOM ages = new AgesHtmlToEditablePOM(url, true);
		try {
			PopulatedObjectModel result = ages.toPOM();
			assertTrue(result.getTopElement().getChildren().size() > 0);
		} catch (Exception e) {
			assertTrue(e.getMessage().length() == 0);
		}
	}

}
