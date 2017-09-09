package net.ages.alwb.utils.transformers.adapters.models;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.junit.Test;

import net.ages.alwb.utils.core.file.AlwbFileUtils;

public class MetaTemplateToPdfTest {

	@Test
	public void test() {
		String path = "/Volumes/ssd2/template.json";
		MetaTemplateToPdf xform = null;
		Map<String,String> x = new TreeMap<String,String>();
		
		try {
			for (File f : AlwbFileUtils.getFilesInDirectory("/Volumes/ssd2/templates", "json")) {
				xform = new MetaTemplateToPdf (
						AlwbFileUtils.fileAsString(f)
						);
				for (Entry<String,String> entry : xform.x.entrySet()) {
					if (! x.containsKey(entry.getKey())) {
						x.put(entry.getKey(), entry.getValue());
					}
				}
			}
			StringBuffer sb = new StringBuffer();
			sb.append("switch (command) {\n");
			for (Entry<String,String> entry : x.entrySet()) {
						sb.append("\tcase \"");
						sb.append(entry.getKey());
						sb.append("\":\n");
						sb.append("\t// " + entry.getValue() + "\n");
						sb.append("\t\tbreak;\n");
						sb.append("\t}\n");
			}
			sb.append("\tdefault:\n");
			sb.append("\t}\n");
			sb.append("}\n");
			System.out.println(sb.toString());			
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		assertTrue(xform.getBasePath().length() == 0);
	}

}
