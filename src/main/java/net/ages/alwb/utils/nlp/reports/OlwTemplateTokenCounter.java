package net.ages.alwb.utils.nlp.reports;

import org.ocmc.ioc.liturgical.schemas.models.LDOM.AgesIndexTableData;
import org.ocmc.ioc.liturgical.schemas.models.LDOM.AgesIndexTableRowData;

import net.ages.alwb.utils.transformers.adapters.AgesWebsiteIndexToReactTableData;

public class OlwTemplateTokenCounter {
	String uid = "";
	String pwd = "";
	String host = "";
	
	public OlwTemplateTokenCounter(
			String uid
			, String pwd
			, String host
			) {
		this.uid = uid;
		this.pwd = pwd;
		this.host = host;
	}
	public void process() {
		AgesWebsiteIndexToReactTableData index = new AgesWebsiteIndexToReactTableData();
		try {
			AgesIndexTableData data = index.toReactTableDataOctoechos();
			for (AgesIndexTableRowData row : data.getTableData()) {
				if (! row.getDayOfWeek().contains("Orthros")) {
					String mode = row.getType().substring(10, 16);
					String day = row.getType().substring(17, row.getType().length());
					System.out.println("Processing Octoechos " + mode + " " + day);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		String uid = System.getenv("uid");
		String pwd = System.getenv("pwd");
		String dbHost = "localhost"; // "159.203.89.233"; 
		OlwTemplateTokenCounter it = new OlwTemplateTokenCounter(
				uid
				, pwd
				, dbHost
				);
		it.process();
	}

}
