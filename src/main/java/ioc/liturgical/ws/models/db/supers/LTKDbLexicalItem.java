package ioc.liturgical.ws.models.db.supers;

import com.google.gson.annotations.Expose;

import ioc.liturgical.ws.constants.EXTERNAL_DB_LIBS;
import ioc.liturgical.ws.constants.ONTOLOGY_TOPICS;
import ioc.liturgical.ws.constants.PARTS_OF_SPEECH;
import net.ages.alwb.utils.nlp.constants.DEPENDENCY_LABELS;

import com.github.reinert.jjschema.Attributes;

/**
 * @author mac002
 *
 */
@Attributes(title = "LTKDb Grammar Lexicon", description = "Abstract Lexicon")
public class LTKDbLexicalItem extends LTKDb {
	
	private static ONTOLOGY_TOPICS topic = ONTOLOGY_TOPICS.LEXICAL_ITEM;

	@Attributes(id="bottom", readonly=true, description="The number of times it occurs in the database")
	@Expose public int frequency = 0;

	/**
	 * 
	 * @param key the lemma or one of its forms
	 * @param frequency the number of times it occurs in the database
	 * @param schema the schema for this record type
	 * @param serialVersion the version number for this record type
	 */
	public LTKDbLexicalItem(
			String key
			, int frequency
			, String schema
			, double serialVersion
			) {
		super (
				EXTERNAL_DB_LIBS.LINGUISTICS.toSystemDomain()
				, topic.keyname
				, key
				, schema
				, serialVersion
				, topic
				);
		this.frequency = frequency;
	}
	
	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

}
