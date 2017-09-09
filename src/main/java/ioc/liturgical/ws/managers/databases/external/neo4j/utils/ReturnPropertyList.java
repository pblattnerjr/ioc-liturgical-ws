package ioc.liturgical.ws.managers.databases.external.neo4j.utils;

import java.util.ArrayList;
import java.util.List;

public class ReturnPropertyList {
	private List<ReturnProperty> properties = new ArrayList<ReturnProperty>();

	public void addType(String handle) {
		properties.add(new ReturnProperty(handle, ReturnProperty.TYPES.TYPE));
	}
	public void addProperties(String handle) {
		properties.add(new ReturnProperty(handle, ReturnProperty.TYPES.PROPERTIES));
	}
	public void add(String handle, String propName) {
		properties.add(new ReturnProperty(handle, propName, propName));
	}
	public void add(String handle, String propName, String asName) {
		properties.add(new ReturnProperty(handle, propName, asName));
	}
	
	/**
	 * Returns a comma delimited properties list for use in a neo4j return clause
	 **/
	public String toString() {
		StringBuffer result = new StringBuffer();
		for (ReturnProperty prop : properties) {
			if (result.length() > 0) {
				result.append(", ");
			}
			result.append(prop.toString());
		}
		return result.toString();
	}
}
