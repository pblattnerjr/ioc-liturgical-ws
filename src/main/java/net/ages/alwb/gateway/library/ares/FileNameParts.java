package net.ages.alwb.gateway.library.ares;

public class FileNameParts {
	private String topic;
	private String domain;
	
	public FileNameParts(String topic, String domain) {
		this.topic = topic;
		this.domain = domain;
	}
	
	public String getTopic() {
		return topic;
	}

	public String getDomain() {
		return domain;
	}

}
