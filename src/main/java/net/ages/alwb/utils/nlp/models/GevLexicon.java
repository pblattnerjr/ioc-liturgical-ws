package net.ages.alwb.utils.nlp.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.google.gson.annotations.Expose;

import  net.ages.alwb.utils.nlp.models.WordSenseGev;
import org.ocmc.ioc.liturgical.schemas.models.supers.AbstractModel;
import org.ocmc.ioc.liturgical.utils.FileUtils;
import net.ages.alwb.utils.nlp.fetchers.Ox3kUtils;
import net.ages.alwb.utils.nlp.fetchers.Ox3kWordSenses;

public class GevLexicon extends AbstractModel {
	
	private Ox3kUtils.DOC_SOURCE listSource = Ox3kUtils.DOC_SOURCE.NET;
	private Ox3kUtils.DOC_SOURCE entrySource = Ox3kUtils.DOC_SOURCE.NET;
	private boolean saveJsonToFile;
	private String path = "'";
	private  boolean prettyprint = false;
	
	@Expose Map<String,List<WordSenseGev>> lexicon = new TreeMap<String,List<WordSenseGev>>();

	/**
	 * Parameterless constructor will only read from the Internet (not disk) and not write a json file
	 */
	public GevLexicon() {
		super();
		this.setPrettyPrint(false);
	}

	/**
	 * 
	 * @param prettyprint true if want json displayed with parts on separate lines.  If false, reduces the size by 1.6 mb
	 */
	public GevLexicon(
			Ox3kUtils.DOC_SOURCE listSource
			, Ox3kUtils.DOC_SOURCE entrySource
			, boolean saveJsonToFile
			, String path
			, boolean prettyprint
			) {
		super();
		
		this.setPrettyPrint(prettyprint);
		this.listSource = listSource;
		this.entrySource = entrySource;
		this.path = path;
		this.saveJsonToFile = saveJsonToFile;
	}

	public void load() {
		Ox3kWordSenses e = null;
		e = new Ox3kWordSenses(
				Ox3kUtils.DOC_SOURCE.DISK
				, Ox3kUtils.DOC_SOURCE.DISK
				, path
				, prettyprint
				);
		List<WordSenseGev> lemmaSenses = e.getLemmaSenses();
		for (WordSenseGev sense : lemmaSenses) {
			if (lexicon.containsKey(sense.getLemma())) {
				List<WordSenseGev> list = lexicon.get(sense.getLemma());
				list.add(sense);
				lexicon.put(sense.getLemma(), list);
			} else {
				List<WordSenseGev> list = new ArrayList<WordSenseGev>();
				list.add(sense);
				lexicon.put(sense.getLemma(), list);
			}
		}
		if (saveJsonToFile) {
			FileUtils.writeFile(
					path + "/en_senses.json"
					, this.toJsonString()
			);
		}
	}

	public Map<String, List<WordSenseGev>> getLexicon() {
		return lexicon;
	}

	public void setLexicon(Map<String, List<WordSenseGev>> lexicon) {
		this.lexicon = lexicon;
	}
}
