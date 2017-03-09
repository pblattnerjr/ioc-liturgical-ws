package ioc.liturgical.ws.managers.databases.external.neo4j.utils;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import ioc.liturgical.ws.constants.DOMAINS_BIBLICAL;
import ioc.liturgical.ws.constants.DOMAINS_LITURGICAL;
import ioc.liturgical.ws.constants.DOMAIN_QUERIES;
import ioc.liturgical.ws.managers.databases.external.neo4j.ExternalDbManager;

public class DomainsService {
	String splitter = "split(n.id,'~')[0]";
	String liturgicalDomains = "match (n:Liturgical) return distinct split(n.id,'~')[0]";
	String biblicalDomains = "match (n:Biblical) return distinct split(n.id,'~')[0]";
	String textDomains = "match (n:Text) return distinct split(n.id,'~')[0]";

	DropdownArray getBiblicalDomains() {
		return getDomainsFor(DOMAIN_QUERIES.BIBLICAL);
	}
	
	DropdownArray getLiturgicalDomains() {
		return getDomainsFor(DOMAIN_QUERIES.LITURGICAL);
	}
	
	String getLabelFor(DOMAIN_QUERIES subject, String key) {
		String label = key;
		switch (subject) {
		case BIBLICAL:
			label = DOMAINS_BIBLICAL.getLabel(key);
			break;
		case LITURGICAL:
			label = DOMAINS_LITURGICAL.getLabel(key);
			break;
		case TEXT:
			break;
		default:
			break;
		}
		return label;
	}
	
	DropdownArray getDomainsFor(DOMAIN_QUERIES subject) {
		List<DropdownItem> theDomains = new ArrayList<DropdownItem>();
		theDomains.add(new DropdownItem("All","*"));
		JsonObject json = ExternalDbManager.queryToDb.getForQuery(subject.query);
		JsonArray values = json.get("values").getAsJsonArray();
		for (int i=0; i < values.size(); i++) {
			JsonElement e = values.get(i);
			String domain = e.getAsJsonObject().get(splitter).getAsString().trim();
			String label = getLabelFor(subject, domain);
			theDomains.add(new DropdownItem(label,domain));
		}
		DropdownArray array = new DropdownArray("domains");
		array.setItems(theDomains);
		return array;
	}
	public static void main(String[] args) {
		DomainsService db = new DomainsService();
		System.out.println(db.getBiblicalDomains());
		System.out.println(db.getLiturgicalDomains());
	}

}
