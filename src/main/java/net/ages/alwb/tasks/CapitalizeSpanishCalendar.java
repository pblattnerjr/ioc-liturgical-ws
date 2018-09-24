package net.ages.alwb.tasks;
import org.eclipse.jgit.util.StringUtils;
import org.ocmc.ioc.liturgical.schemas.models.db.docs.ontology.TextLiturgical;
import org.ocmc.ioc.liturgical.schemas.models.ws.response.ResultJsonObjectArray;

import com.google.gson.JsonObject;

import ioc.liturgical.ws.managers.databases.external.neo4j.ExternalDbManager;
import ioc.liturgical.ws.managers.exceptions.DbException;

/**
 * Runs a task (separate thread) to set fix the day number for Guatemala calendar
 * @author mac002
 *
 */
public class CapitalizeSpanishCalendar implements Runnable {
	ExternalDbManager manager = null;
	boolean updateMonthDay = false;
	boolean updateYearMonthDay = false;
	
	public CapitalizeSpanishCalendar (
			ExternalDbManager manager
			, boolean updateMonthDay
			, boolean updateYearMonthDay
			) {
		this.manager = manager;
		this.updateMonthDay = updateMonthDay;
		this.updateYearMonthDay = updateYearMonthDay;
	}
	
	private void updateMonthDay() {
		String query = "match (n:Root:Liturgical) where n.library = 'spa_gt_odg' and n.topic = 'calendar' and n.key starts with 'y20' and n.key ends with '.md' return properties(n);";
		ResultJsonObjectArray docs = this.manager.getForQuery(query, false, false);
		for (JsonObject doc : docs.getValues()) {
			String json = doc.get("properties(n)").getAsJsonObject().toString();
			TextLiturgical text = this.manager.gson.fromJson(json, TextLiturgical.class);
			try {
				String value = text.getValue(); // 7 abril (domingo)
				String [] valueParts = value.split(" ");
				String day = valueParts[2].trim();
				day = day.substring(1, day.length()-2);
				String newValue = valueParts[0] + " de " + StringUtils.capitalize(valueParts[1]) + " (" + StringUtils.capitalize(day) + ")";
				System.out.println(newValue);
				text.setValue(newValue);
			ExternalDbManager.neo4jManager.updateWhereEqual(text, false);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	private void updateYearMonthDay() {
		String query = "match (n:Root:Liturgical) where n.library = 'spa_gt_odg' and n.topic = 'calendar' and n.key starts with 'y20' and n.key ends with '.ymd' return properties(n);";
		ResultJsonObjectArray docs = this.manager.getForQuery(query, false, false);
		for (JsonObject doc : docs.getValues()) {
			String json = doc.get("properties(n)").getAsJsonObject().toString();
			TextLiturgical text = this.manager.gson.fromJson(json, TextLiturgical.class);
			try {
				String value = text.getValue(); // lunes 30 de diciembre de 2019
				String [] valueParts = value.split(" ");
				String newValue = 
						StringUtils.capitalize(valueParts[0]) 
								+ " " + valueParts[1] 
								+ " " + valueParts[2] 
								+ " " + StringUtils.capitalize(valueParts[3]) 
								+ " " + valueParts[4] 
								+ " " + valueParts[5] 
								;
				System.out.println(newValue);
				text.setValue(newValue);
			ExternalDbManager.neo4jManager.updateWhereEqual(text, false);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void run() {
		synchronized(this) {
			if (this.updateMonthDay) {
				this.updateMonthDay();
			}
			if (this.updateYearMonthDay) {
				this.updateYearMonthDay();
			}
		}
	}
	
}
