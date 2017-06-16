package net.ages.alwb.utils.nlp.models.lemmatizer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.annotations.Expose;

import net.ages.alwb.utils.core.datastores.json.models.AbstractModel;
import net.ages.alwb.utils.core.error.handling.ErrorUtils;
import net.ages.alwb.utils.core.file.AlwbFileUtils;
import net.ages.alwb.utils.nlp.constants.GRAMMAR_ABBREVIATIONS;
import net.ages.alwb.utils.nlp.models.GevLexicon;

/**
 * Original version of the forms file from: https://github.com/richardwilly98/elasticsearch-opennlp-auto-tagging
 * @author mac002
 *
 */
public class LemmaLookup extends AbstractModel {
	private static final Logger logger = LoggerFactory.getLogger(LemmaLookup.class);
	private GevLexicon lexicon = null;
	@Expose Map<String,List<LemmaPos>> forms = new TreeMap<String,List<LemmaPos>>();

	public LemmaLookup() {
		super();
		super.setPrettyPrint(false);
		try {
			String json = 	AlwbFileUtils.getResourceFileContent(
					this
					, "json/en_senses.json"
					);
			lexicon = gson.fromJson(
				   json
					, GevLexicon.class
					);
		} catch (Exception e) {
			ErrorUtils.report(logger, e);
		}
	}

	public Map<String, List<LemmaPos>> getForms() {
		return forms;
	}

	public void setForms(Map<String, List<LemmaPos>> forms) {
		this.forms = forms;
	}
	
	public void addLemma(String form, String partOfSpeech, String lemma) {
		GRAMMAR_ABBREVIATIONS enumPos = GRAMMAR_ABBREVIATIONS.forNamePenn(partOfSpeech);
		String pos = enumPos.keyname;
		if (pos.equals(partOfSpeech)) {
			System.out.println(partOfSpeech + " not mapped");
		}
		if (forms.containsKey(form)) {
			List<LemmaPos> value = forms.get(form);
			LemmaPos lp = new LemmaPos(lemma, pos);
			lp.setPosPenn(partOfSpeech);
			lp.setPosWn(enumPos.wordnetAbr);
			lp.setPosWnNbr(enumPos.wordnetNbr);
			lp.setOxford3k(lexicon.getLexicon().containsKey(lemma));
			if (! value.contains(lp)) {
				value.add(lp);
			}
			forms.put(form, value);
		} else {
			LemmaPos lp = new LemmaPos(lemma, pos);
			lp.setPosPenn(partOfSpeech);
			lp.setPosWn(enumPos.wordnetAbr);
			lp.setPosWnNbr(enumPos.wordnetNbr);
			List<LemmaPos> value = new ArrayList<LemmaPos>();
			value.add(lp);
			forms.put(form, value);
		}
	}
	
	public boolean containsForm(String form) {
		return this.forms.containsKey(form);
	}
	
	public List<LemmaPos> getLemmas(String form) {
		if (containsForm(form)) {
			return forms.get(form);
		} else {
			return null;
		}
	}
}
