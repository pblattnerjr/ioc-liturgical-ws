package net.ages.alwb.utils.transformers.adapters;

import static org.junit.Assert.*;

import org.junit.Test;

import net.ages.alwb.utils.transformers.adapters.models.AgesIndexTableData;

public class AgesIndexHtmlToReactTableDataTest {

	@Test
	public void test() {
		AgesIndexHtmlToReactTableData ages = new AgesIndexHtmlToReactTableData(true);
		try {
			AgesIndexTableData data = ages.toReactTableData();
			System.out.println(data.toJsonString());
			assertTrue(data != null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
