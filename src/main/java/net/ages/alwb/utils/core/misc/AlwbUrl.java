package net.ages.alwb.utils.core.misc;

import ioc.liturgical.ws.app.ServiceProvider;
import ioc.liturgical.ws.constants.Constants;

public class AlwbUrl {
	private String urlString = "";
	private String type = "";
	private String year = "";
	private String month = "";
	private String day = "";
	private String name = "";
	private int startIndex = 0;
	private String mode = "";

	public AlwbUrl(String url) {
		this.urlString = url;
		String [] parts = url.split("/");
		for (int i = parts.length-1; i > -1; i--) {
			if (parts[i].equals("h")) {
				startIndex = i+1;
				break;
			}
		}
		if (parts[2].equals("liml.org")) {
			type  = "b"; // book
		} else {
			type = parts[startIndex];
		}
		if (type.equals("s")) {
			year = parts[startIndex+1];
			month = parts[startIndex+2];
			day = parts[startIndex+3];
			name = parts[startIndex+4];
		} else if (type.equals("c")) {
			try {
				parts = url.split("\\/");
				parts = parts[8].split(Constants.DOMAIN_SPLITTER);
				month = parts[3];
				year = parts[2];
				name  = "dr"; // daily readings
			} catch (Exception e) {
				ServiceProvider.sendMessage("ExternalDbManager.getAgesService can't parse date from url " + url);
			}
		} else if (type.equals("b")) {
			if (parts[4].contains("basil")) {
				if (parts[4].contains("vesperal")) {
					name = "vl";
				} else {
					name = "li2";
				}
			} else if (parts[4].contains("mexico")) {
				if (parts[5].contains("basil")) {
					name = "li";
				}
			} else {
				if (parts[7].equals("oc")) {
					name = parts[7];
					mode = parts[8];
					day = parts[9];
				}
			}
		} else {
			name = parts[startIndex+1];
		}
	}
	
	public String getFileName() {
		StringBuffer result = new StringBuffer();
		if (this.isService()) {
			result.append("se");
			result.append(".");
			result.append(this.getYear());
			result.append(".");
			result.append(this.getMonth());
			result.append(".");
			result.append(this.getDay());
			result.append(".");
		} else {
			result.append("bk");
			result.append(".");
		}
		result.append(this.getName());
		return result.toString();
	}

	public boolean isService() {
		return this.type.equals("s");
	}
	
	public String getUrlString() {
		return urlString;
	}

	public void setUrlString(String urlString) {
		this.urlString = urlString;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getMonth() {
		return month;
	}

	public String getJavaMonth() {
		
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}
	
}
