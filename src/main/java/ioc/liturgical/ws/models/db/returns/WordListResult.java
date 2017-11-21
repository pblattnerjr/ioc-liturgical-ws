package ioc.liturgical.ws.models.db.returns;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import ioc.liturgical.ws.models.db.docs.nlp.ConcordanceLine;
import ioc.liturgical.ws.models.db.docs.nlp.WordInflected;

public class WordListResult {
	
	private Map<String,WordInflected> resultWordList = new TreeMap<String,WordInflected>();
	private Map<String,List<ConcordanceLine>> resultConcordance = new TreeMap<String,List<ConcordanceLine>>();

	public Map<String, WordInflected> getResultWordList() {
		return resultWordList;
	}

	public void addWord(String key, WordInflected value) {
		resultWordList.put(key, value);
	}
	
	public WordInflected getWord(String key) {
		return resultWordList.get(key);
	}
	
	public boolean containsWord(String key) {
		return resultWordList.containsKey(key);
	}
	
	public void incrementFrequency(String key) {
		WordInflected word = resultWordList.get(key);
		word.setFrequency(word.getFrequency() + 1);
		resultWordList.put(key, word);
	}
	
	public void setResultWordList(Map<String, WordInflected> resultWordList) {
		this.resultWordList = resultWordList;
	}
	
	public Map<String, List<ConcordanceLine>> getResultConcordance() {
		return resultConcordance;
	}
	
	public void setResultConcordance(Map<String, List<ConcordanceLine>> resultConcordance) {
		this.resultConcordance = resultConcordance;
	}
	
	public boolean existsConcordanceLine(String key) {
		return resultConcordance.containsKey(key);
	}
	
	/**
	 * Add a concordance line for the specified key
	 * @param key
	 * @param value
	 */
	public void addConcordanceLine(String key, ConcordanceLine value) {
		List<ConcordanceLine> list = null;
		if (resultConcordance.containsKey(key)) {
			list = resultConcordance.get(key);
			list.add(value);
		} else {
			list = new ArrayList<ConcordanceLine>();
			list.add(value);
		}
		resultConcordance.put(key, list);
	}
	
}
