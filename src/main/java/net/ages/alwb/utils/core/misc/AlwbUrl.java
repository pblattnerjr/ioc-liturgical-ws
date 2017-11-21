package net.ages.alwb.utils.core.misc;

public class AlwbUrl {
	private String urlString = "";
	private String type = "";
	private String year = "";
	private String month = "";
	private String day = "";
	private String name = "";
	private int startIndex = 0;

	public AlwbUrl(String url) {
		this.urlString = url;
		String [] parts = url.split("/");
		for (int i = parts.length-1; i > -1; i--) {
			if (parts[i].equals("h")) {
				startIndex = i+1;
				break;
			}
		}
		type = parts[startIndex];
		if (type.equals("s")) {
			year = parts[startIndex+1];
			month = parts[startIndex+2];
			day = parts[startIndex+3];
			name = parts[startIndex+4];
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
	
}
