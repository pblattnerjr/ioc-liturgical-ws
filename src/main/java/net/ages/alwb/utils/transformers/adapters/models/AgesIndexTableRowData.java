package net.ages.alwb.utils.transformers.adapters.models;

import com.google.gson.annotations.Expose;

import org.ocmc.ioc.liturgical.schemas.models.supers.AbstractModel;

public class AgesIndexTableRowData extends AbstractModel {
	@Expose String url = ""; // url to the file
	@Expose String type = ""; // Sacrament name, matins, vespers, liturgy, etc.
	@Expose String date = "";
	@Expose String dayOfWeek = "";
	
	public AgesIndexTableRowData() {
		super();
	}
	
	public AgesIndexTableRowData(boolean printPretty) {
		super();
		super.setPrettyPrint(printPretty);
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public String getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
}
