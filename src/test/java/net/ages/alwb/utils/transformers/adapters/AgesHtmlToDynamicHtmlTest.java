package net.ages.alwb.utils.transformers.adapters;

import static org.junit.Assert.*;

import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;

import net.ages.alwb.utils.transformers.adapters.models.LDOM;

public class AgesHtmlToDynamicHtmlTest {

	@Test
	public void test() {
		AgesHtmlToLDOM ages = new AgesHtmlToLDOM(
				"http://www.agesinitiatives.com/dcs/public/dcs/h/b/greatwaterblessing/gr-en/index.html"
				, "swa_ke_aok"
				, "en_us_ages" // en_us_ages
				, "kik_ke_aok" // kik_ke_aok
				, "gr_gr_ages"
				, "en_us_ages"
				, "en_us_ages"
				, true // print pretty
				);
		try {
			LDOM template = ages.toLDOM();
		System.out.println(template.getTopElement().toJsonString());
			Map<String,String> values = template.getValues();
			for ( Entry<String,String> entry: values.entrySet()) {
				if (entry.getKey().startsWith("en_us_ages~ps~psa118.v12.text")) {
					System.out.println(entry.getKey() + " = " + entry.getValue());
				}
			}
			assertTrue(template != null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
