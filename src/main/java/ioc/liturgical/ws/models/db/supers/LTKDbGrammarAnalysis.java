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
@Attributes(title = "LTKDb Grammar Analysis", description = "Abstract Grammar Analysis")
public class LTKDbGrammarAnalysis extends LTKDb {
	
	private static ONTOLOGY_TOPICS topic = ONTOLOGY_TOPICS.GRAMMAR;

	@Attributes(required = true, description = "Form (the form of the word or token as it appears in the text")
	@Expose public String form = "";

	@Attributes(required = true, description = "Lemma (the form of the word as it appears in a lexicon (i.e. dictionary)")
	@Expose public String lemma = "";
	
	@Attributes(required = true, description = "Expanded Form")
	@Expose public String expandedForm = "";

	@Attributes(required = true, description = "Dialect in which this form and analysis occurs")
	@Expose public String dialect = "";

	@Attributes(required = true, description = "Part of Speech for this token")
	@Expose public PARTS_OF_SPEECH pos = PARTS_OF_SPEECH.UNKNOWN;

	@Attributes(required = true, description = "Dependency label for this token")
	@Expose public DEPENDENCY_LABELS label = DEPENDENCY_LABELS.TBD;

	public LTKDbGrammarAnalysis(
			String form
			, String seq
			, String schema
			, double serialVersion
			) {
		super(
				EXTERNAL_DB_LIBS.LINGUISTICS.toSystemDomain()
				, form
				, seq
				, schema
				,  serialVersion
				, topic
				);
		this.form = form;
	}

	public String getForm() {
		return form;
	}

	public void setForm(String form) {
		this.form = form;
	}

	public String getLemma() {
		return lemma;
	}

	public void setLemma(String lemma) {
		this.lemma = lemma;
	}

	public String getExpandedForm() {
		return expandedForm;
	}

	public void setExpandedForm(String expandedForm) {
		this.expandedForm = expandedForm;
	}

	public String getDialect() {
		return dialect;
	}

	public void setDialect(String dialect) {
		this.dialect = dialect;
	}

	public PARTS_OF_SPEECH getPos() {
		return pos;
	}

	public void setPos(PARTS_OF_SPEECH pos) {
		this.pos = pos;
	}

	public DEPENDENCY_LABELS getLabel() {
		return label;
	}

	public void setLabel(DEPENDENCY_LABELS label) {
		this.label = label;
	}
	
	/**
	 * Return this token (surface form) formatted as a gloss line
	 * for Latex ExPex
	 * @return
	 */
	public String toExPexGloss(String grammar) {
		StringBuffer result = new StringBuffer();
		result.append("[");
		result.append(this.form);
		result.append("//");
		result.append(grammar);
		result.append("/");
		result.append(this.lemma);
		return result.toString();
	}


}
