package ioc.liturgical.ws.models.db.returns;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TreeMap;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;

import ioc.liturgical.ws.managers.databases.external.neo4j.ExternalDbManager;
import ioc.liturgical.ws.models.RequestStatus;
import net.ages.alwb.utils.core.datastores.json.models.AbstractModel;
import net.ages.alwb.utils.core.datastores.json.models.DropdownItem;
import net.ages.alwb.utils.core.error.handling.ErrorUtils;

/**
 * Holds the results for a request for the forms to create new instances of things.
 * @author mac002
 *
 */
public class ResultNewForms extends AbstractModel {
	private static final Logger logger = LoggerFactory.getLogger(ResultNewForms.class);
	@Expose public String query;
	@Expose public RequestStatus status;
	@Expose public Long valueCount = Long.parseLong("0");
	@Expose public Map<String,JsonObject> valueSchemas = new TreeMap<String,JsonObject>();
	@Expose public Map<String,JsonObject> values = new TreeMap<String,JsonObject>();
	@Expose public JsonObject domains;
	@Expose public List<DropdownItem> ontologyTypesDropdown = new ArrayList<DropdownItem>();
	@Expose public Map<String,JsonArray> ontologyDropdowns = new TreeMap<String,JsonArray>();
	@Expose public List<JsonObject> formsDropdown = new ArrayList<JsonObject>();
	@Expose public List<DropdownItem> biblicalBooksDropdown = new ArrayList<DropdownItem>();
	@Expose public List<DropdownItem> biblicalChaptersDropdown = new ArrayList<DropdownItem>();
	@Expose public List<DropdownItem> biblicalVersesDropdown = new ArrayList<DropdownItem>();
	@Expose public List<DropdownItem> biblicalSubversesDropdown = new ArrayList<DropdownItem>();

	public ResultNewForms(boolean prettyPrint) {
		super();
		super.setPrettyPrint(prettyPrint);
		status = new RequestStatus();
	}
	
	public void addSchema(String id, JsonObject schema) {
		if (! valueSchemas.containsKey(id)) {
			valueSchemas.put(id, schema);
		}
	}
	
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}

	public void setResult(Map<String,JsonObject> result) {
		this.values = result;
		if (result != null) {
			this.valueCount = (long) result.size();
		}
	}

	public Long getCount() {
		return valueCount;
	}

	public void setCount(Long count) {
		this.valueCount = count;
	}

	public Long getResultCount() {
		return valueCount;
	}

	public void setResultCount(Long resultCount) {
		this.valueCount = resultCount;
	}

	public RequestStatus getStatus() {
		return status;
	}

	public void setStatus(RequestStatus status) {
		this.status = status;
	}

	public Long getValueCount() {
		return valueCount;
	}

	public void setValueCount(Long valueCount) {
		this.valueCount = valueCount;
	}

	public void setStatusCode(int code) {
		this.status.setCode(code);
	}

	/**
	 * Sets status developer message and user message to the same message
	 * @param message
	 */
	public void setStatusMessage(String message) {
		this.status.developerMessage = message;
		this.status.userMessage = message;
	}

	public void setStatusDeveloperMessage(String message) {
		this.status.developerMessage = message;
	}

	public void setStatusUserMessage(String message) {
		this.status.userMessage = message;
	}

	public Map<String, JsonObject> getValueSchemas() {
		return valueSchemas;
	}

	public void setValueSchemas(Map<String, JsonObject> valueSchemas) {
		this.valueSchemas = valueSchemas;
		Map<String,DropdownItem> dropdownMap = new TreeMap<String,DropdownItem>();
		try {
			for (Entry<String, JsonObject> entry  : valueSchemas.entrySet()) {
				try {
					String value = entry.getKey();
					String label = entry.getValue().get("schema").getAsJsonObject().get("title").getAsString();
					DropdownItem d = new DropdownItem(label,value);
					dropdownMap.put(label, d);
				} catch (Exception entryException) {
					ErrorUtils.report(logger, entryException, entry.getKey() + " bad schema");
				}
			}
			for (DropdownItem d : dropdownMap.values()) {
				formsDropdown.add(d.toJsonObject());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public JsonObject getDomains() {
		return domains;
	}

	public void setDomains(JsonObject domains) {
		this.domains = domains;
	}

	public Map<String, JsonObject> getValues() {
		return values;
	}

	public void setValues(Map<String, JsonObject> values) {
		this.values = values;
	}

	public List<JsonObject> getFormsDropdown() {
		return formsDropdown;
	}

	public void setFormsDropdown(List<JsonObject> formsDropdown) {
		this.formsDropdown = formsDropdown;
	}

	public Map<String, JsonArray> getOntologyDropdowns() {
		return ontologyDropdowns;
	}

	public void setOntologyDropdowns(Map<String, JsonArray> ontologyDropdowns) {
		this.ontologyDropdowns = ontologyDropdowns;
	}

	public List<DropdownItem> getOntologyTypesDropdown() {
		return ontologyTypesDropdown;
	}

	public void setOntologyTypesDropdown(List<DropdownItem> ontologyTypesDropdown) {
		this.ontologyTypesDropdown = ontologyTypesDropdown;
	}

	public List<DropdownItem> getBiblicalBooksDropdown() {
		return biblicalBooksDropdown;
	}

	public void setBiblicalBooksDropdown(List<DropdownItem> biblicalBooksDropdown) {
		this.biblicalBooksDropdown = biblicalBooksDropdown;
	}

	public List<DropdownItem> getBiblicalChaptersDropdown() {
		return biblicalChaptersDropdown;
	}

	public void setBiblicalChaptersDropdown(List<DropdownItem> biblicalChaptersDropdown) {
		this.biblicalChaptersDropdown = biblicalChaptersDropdown;
	}

	public List<DropdownItem> getBiblicalVersesDropdown() {
		return biblicalVersesDropdown;
	}

	public void setBiblicalVersesDropdown(List<DropdownItem> biblicalVersesDropdown) {
		this.biblicalVersesDropdown = biblicalVersesDropdown;
	}

	public List<DropdownItem> getBiblicalSubversesDropdown() {
		return biblicalSubversesDropdown;
	}

	public void setBiblicalSubversesDropdown(List<DropdownItem> biblicalSubversesDropdown) {
		this.biblicalSubversesDropdown = biblicalSubversesDropdown;
	}
}
