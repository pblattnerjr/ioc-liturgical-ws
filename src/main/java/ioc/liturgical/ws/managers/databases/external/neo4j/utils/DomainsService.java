package ioc.liturgical.ws.managers.databases.external.neo4j.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.ocmc.ioc.liturgical.schemas.models.DropdownArray;
import org.ocmc.ioc.liturgical.schemas.models.DropdownItem;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.ocmc.ioc.liturgical.schemas.constants.DOMAINS_BIBLICAL;
import org.ocmc.ioc.liturgical.schemas.constants.DOMAINS_LITURGICAL;
import org.ocmc.ioc.liturgical.schemas.constants.DOMAIN_QUERIES;
import ioc.liturgical.ws.managers.databases.external.neo4j.ExternalDbManager;

public class DomainsService {
	String splitter = "split(n.id,'~')[0]";
	String liturgicalDomains = "match (n:Liturgical) return distinct split(n.id,'~')[0]";
	String biblicalDomains = "match (n:Biblical) return distinct split(n.id,'~')[0]";
	String textDomains = "match (n:Text) return distinct split(n.id,'~')[0]";

	DropdownArray getBiblicalDomains() {
		return getDomainsDropdownArrayFor(DOMAIN_QUERIES.BIBLICAL, null);
	}
	
	DropdownArray getLiturgicalDomains(Map<String, DropdownItem> filter) {
		return getDomainsDropdownArrayFor(DOMAIN_QUERIES.LITURGICAL, filter);
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
	
	JsonArray getDomainsAsJsonArrayFor(DOMAIN_QUERIES subject) {
		JsonArray result = new JsonArray();
		JsonObject json = ExternalDbManager
				.neo4jManager
				.getForQuery(subject.query)
				.toJsonObject();
		result = json.get("values").getAsJsonArray();
		return result;
	}

	DropdownArray getDomainsDropdownArrayFor(
			DOMAIN_QUERIES subject
			, Map<String, DropdownItem> filter // can be null
			) {
		List<DropdownItem> theDomains = new ArrayList<DropdownItem>();
		theDomains.add(new DropdownItem("Any","*"));
		JsonArray values = this.getDomainsAsJsonArrayFor(subject);
		for (int i=0; i < values.size(); i++) {
			JsonElement e = values.get(i);
			String domain = e.getAsJsonObject().get(splitter).getAsString().trim();
			if (filter == null) {
				theDomains.add(new DropdownItem(this.getLabelFor(subject, domain), domain));
			} else if (filter.containsKey(domain)) {
				theDomains.add(filter.get(domain));
			}
		}
		Collections.sort(theDomains);
		DropdownArray array = new DropdownArray("domains");
		array.setItems(theDomains);
		return array;
	}


}
