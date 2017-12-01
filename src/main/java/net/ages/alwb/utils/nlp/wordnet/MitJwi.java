package net.ages.alwb.utils.nlp.wordnet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.ISenseEntry;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;
import org.ocmc.ioc.liturgical.utils.ErrorUtils;
import net.ages.alwb.utils.nlp.wordnet.models.Synset;
import net.ages.alwb.utils.nlp.wordnet.models.Synsets;

public class MitJwi {
	private static final Logger logger = LoggerFactory.getLogger(MitJwi.class);
	private boolean printPretty = false;
	private IDictionary dict = null;
	
	public MitJwi(boolean printPretty) {
		try {
			this.printPretty = printPretty;
			dict = new Dictionary(MitJwi.class.getResource("/dict"));
			dict.open();
		} catch (IOException e) {
			ErrorUtils.report(logger, e);
		}
	}
	
	public List<IWordID> getIds(String lemma) {
		List<IWordID> result = new ArrayList<IWordID>();
		for (POS pos : POS.values()) {
			IIndexWord idxWord = dict.getIndexWord(lemma, pos);
			if (idxWord != null) {
				for (IWordID id : idxWord.getWordIDs()) {
					result.add(id);
				}
			}
		}
		return result;
	}
	
	public String toJsonString(String lemma) {
		JsonObject result = new JsonObject();
		JsonObject synsets = this.synsets(lemma).toJsonObject();
		result.add(lemma, synsets.get("list").getAsJsonArray());
		return result.toString();
	}
	
	public Synsets synsets(String lemma) {
		Synsets result = new Synsets(lemma);
		for (IWordID id : this.getIds(lemma)) {
			IWord word = dict.getWord(id);
			ISenseEntry e = dict.getSenseEntry(word.getSenseKey());
			Synset s = new Synset(word.getSynset(), printPretty);
			s.setWords(this.getSynonyms(word.getSynset().getWords()));
			result.add(s);
		}
		return result;
	}
	
	private String getSynonyms(List<IWord> words) {
		StringBuffer result = new StringBuffer();
		for (IWord word : words) {
			if (result.length() > 0) {
				result.append(";");
			}
			result.append(word.getLemma() + "." + String.valueOf(word.getPOS().getTag())  + "." + dict.getSenseEntry(word.getSenseKey()).getSenseNumber());
		}
		return result.toString();
	}


}
