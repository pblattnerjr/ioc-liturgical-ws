package net.ages.alwb.utils.transformers.adapters.models;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.junit.Test;

import org.ocmc.ioc.liturgical.utils.FileUtils;
import net.ages.alwb.utils.transformers.adapters.MetaTemplateToPdf;
import net.ages.alwb.utils.transformers.adapters.TextToLatexExpexInterlinear;
import switches.AgesHtmlToOslwSwitch;

public class MetaTemplateToPdfTest {

	@Test
	public void test() {
		boolean createExpex = false;
		String path = "/Volumes/ssd2/templates";
		String out = "/Volumes/ssd2/templatesOut/servicedata.tex";
		MetaTemplateToPdf xform = null;
		Map<String, String> x = new TreeMap<String, String>();

		try {
			for (File f : FileUtils.getFilesInDirectory(path, "json")) {
				xform = new MetaTemplateToPdf(FileUtils.fileAsString(f));
				StringBuffer sb = new StringBuffer();
				sb.append(xform.getTexFileContent().toString());
				for (Entry<String, String> entry : x.entrySet()) {
					if (createExpex) {
						String s = "Βυθοῦ ἀνεκάλυψε πυθμένα, καὶ διὰ ξηρᾶς οἰκείους ἕλκει, ἐν αὐτῷ κατακαλύψας ἀντιπάλους,  ὁ κραταιός, ἐν πολέμοις Κύριος˙ ὅτι δεδόξασται.";
						List<String> translations = new ArrayList<String>();
						translations.add("T 1");
						TextToLatexExpexInterlinear t = new TextToLatexExpexInterlinear(entry.getKey(),
								entry.getValue(), translations, false, true);
						sb.append(t.convert());
					} else {
						if (!entry.getKey().contains("media")) {
							System.out.println(AgesHtmlToOslwSwitch.getOslw(entry.getKey()) + entry.getValue() + "\n");
							sb.append(AgesHtmlToOslwSwitch.getOslw(entry.getKey()) + entry.getValue() + "\n");
						}
					}
				}
			}
			FileUtils.writeFile(out, xform.getTexFileContent().toString());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		assertTrue(xform.getBasePath().length() == 0);
	}

}
