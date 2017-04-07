package ioc.liturgical.ws.models.db.supers;

import com.google.gson.annotations.Expose;

import ioc.liturgical.ws.constants.Constants;
import ioc.liturgical.ws.constants.ONTOLOGY_TOPICS;
import net.ages.alwb.utils.core.datastores.json.models.AbstractModel;
import net.ages.alwb.utils.core.error.handling.ErrorUtils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.reinert.jjschema.Attributes;

/**
 * Superclass for all docs in the external database.
 * Also used for the CreateForm subclasses.
 * 
 * Note the distinction between labels and tags.
 * Labels become actual Neo4j node labels, whereas
 * tags are stored as an array of strings.  Labels are
 * system created and confined to a limited, predefined set.  
 * Tags are user created and ad hoc.
 * 
 * Labels fall into one of the following categories:
 * 
 * Ontology labels: categorize the doc based on the ontological hierarch.
 * Library label: the library becomes a label.
 * Topic labels: in some cases the entire topic becomes a label, or each part of the topic becomes a separate label.
 * Key labels: in some cases (e.g. Biblical texts), the chapter part of the key becomes a label.
 * 
 * @author mac002
 *
 */
@Attributes(title = "Class Name", description = "A {class name} {description}")
public class LTK extends AbstractModel {
	
	private static final Logger logger = LoggerFactory.getLogger(LTK.class);
	
	@Expose private ONTOLOGY_TOPICS ontologyTopic = ONTOLOGY_TOPICS.ROOT;
	
	@Attributes(required = true, readonly = true, minLength = 1, description = "The unique identifier for the schema.")
	@Expose String _valueSchemaId = "";

	@Attributes(required = true, readonly = true, minLength = 1, description = "The unique identifier, that is, the ID.")
	@Expose String id = "";

	@Attributes(required = true, readonly = false, minLength = 1,  description = "The library part of the ID, that is, typically the domain.")
	@Expose String library = "";

	@Attributes(required = true, readonly = false, minLength = 1, description = "The topic part of the ID.")
	@Expose String topic = "";

	@Attributes(required = true, readonly = false, minLength = 1, description = "The key part of the ID")
	@Expose String key = "";

	@Attributes(readonly=true, description="Tags to use when searching for this Doc.")
	@Expose List<String> tags = new ArrayList<String>();
	
	public LTK(
			String library
			, String topic
			, String key
			, String schema
			, double serialVersion
			, ONTOLOGY_TOPICS ontologyTopic
			) {
		super();
		this.library = library;
		this.topic = topic;
		this.key = key;
		this.joinIdParts();
		this._valueSchemaId = schema + ":" + this.serialVersionUID;
		if (ontologyTopic == null) {
			logger.error("Ontology Topic is null for " + this.id);
		} else {
			this.ontologyTopic = ontologyTopic;
		}
	}

	public String getLibrary() {
		return library;
	}

	public void setLibrary(String library) {
		this.library = library;
		joinIdParts();
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
		joinIdParts();
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
		joinIdParts();
	}

	
	private void joinIdParts() {
		this.id = this.library + Constants.ID_DELIMITER + this.topic + Constants.ID_DELIMITER + this.key;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public String get_valueSchemaId() {
		return _valueSchemaId;
	}

	public void set_valueSchemaId(String _valueSchemaId) {
		this._valueSchemaId = _valueSchemaId;
	}
	
	public void addTag(String tag) {
		this.tags.add(tag);
	}
	
	public boolean hasTag(String tag) {
		return this.tags.contains(tag);
	}
	
	public void removeTag(String tag) {
		try {
			this.tags.remove(tag);
		} catch (Exception e) {
			ErrorUtils.report(logger, e);
		}
	}
	
	public String getIdAsPath() {
		return this.library + "/" + this.topic + "/" + this.key;
	}

	public String getSchemaAsLabel() {
		String result = this._valueSchemaId;
		try {
			String [] parts = this._valueSchemaId.split(":");
			if (parts.length > 1) {
				result = parts[0];
			}
		} catch (Exception e) {
			// ignore
		}
		return result;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ONTOLOGY_TOPICS getOntologyTopic() {
		return ontologyTopic;
	}

	public void setOntologyTopic(ONTOLOGY_TOPICS ontologyTopic) {
		this.ontologyTopic = ontologyTopic;
	}

	public List<String> getOntologyLabels() {
		return ontologyTopic.toLabelsList();
	}
	
	/**
	 * Get the ontology labels.  They will be in the proper order.
	 * However, note that Neo4j does not use this order.
	 * It creates an internal ID for each label and uses the ID order.
	 * It is out of our control how they are stored (I, MAC, think).
	 * @return
	 */
	public String getDelimitedOntologyLabels() {
		return StringUtils.join(ontologyTopic.toLabelsList(),":");
	}

}
