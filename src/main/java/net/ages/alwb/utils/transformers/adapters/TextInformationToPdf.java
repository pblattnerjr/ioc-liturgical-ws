package net.ages.alwb.utils.transformers.adapters;

import java.net.URL;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.ocmc.ioc.liturgical.schemas.constants.NOTE_TYPES;
import org.ocmc.ioc.liturgical.schemas.constants.SCHEMA_CLASSES;
import org.ocmc.ioc.liturgical.schemas.constants.nlp.DEPENDENCY_LABELS;
import org.ocmc.ioc.liturgical.schemas.constants.nlp.GRAMMAR_ABBREVIATIONS;
import org.ocmc.ioc.liturgical.schemas.models.db.docs.nlp.TokenAnalysis;
import org.ocmc.ioc.liturgical.schemas.models.db.docs.notes.TextualNote;
import org.ocmc.ioc.liturgical.schemas.models.db.docs.notes.UserNote;
import org.ocmc.ioc.liturgical.schemas.models.supers.BibliographyEntry;
import org.ocmc.ioc.liturgical.schemas.models.ws.response.ResultJsonObjectArray;
import org.ocmc.ioc.liturgical.utils.ErrorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import ioc.liturgical.ws.managers.databases.external.neo4j.ExternalDbManager;
import net.ages.alwb.utils.core.generics.MultiMapWithList;
import net.ages.alwb.utils.core.id.managers.IdManager;
import net.ages.alwb.utils.oslw.OslwUtils;

/**
 * This class creates a .tex file and a .bib file (if there are citations) to be
 * written to disk and then processed by Xelatex.  Since there could be
 * a bibliography, it is necessary to run xelatex, then biber, then xelatex again.
 * These are all combined in the script makepdf, which is placed in the data
 * directory where the .tex and .bib files are written to. 
 */

public class TextInformationToPdf {
	private static final Logger logger = LoggerFactory.getLogger(TextInformationToPdf.class);
	private 	String basePath = "";
	private 	String pathToPdf = "";
	private String textId = "";
	private JsonObject jsonObject = null;
	private StringBuffer texFileSb = new StringBuffer(); // latex tex file content
	private StringBuffer bibtexFileSb = new StringBuffer(); // latex tex file content
	private Gson gson = new Gson();
	private Map<String,List<TokenAnalysis>> map = new TreeMap<String,List<TokenAnalysis>>();
	private JsonArray nodes = null;
	private JsonArray tokens = null;
	private Map<String, Map<NOTE_TYPES, List<TextualNote>>> bigMap = new TreeMap<String,Map<NOTE_TYPES,List<TextualNote>>>();
	private Map<String,String> abbr = new TreeMap<String,String>();
	private Map<String,JsonObject> abbrJsonStrings = new TreeMap<String,JsonObject>();
	private Map<String,JsonObject> biblioJsonStrings = new TreeMap<String,JsonObject>();
	private Map<String,String> usedAbbreviations = new TreeMap<String,String>();
	private List<TextualNote> notesList = new ArrayList<TextualNote>();
	private List<TextualNote> summaryList = new ArrayList<TextualNote>();
	private List<TextualNote> adviceList = new ArrayList<TextualNote>();
	private List<UserNote> userList = new ArrayList<UserNote>();
	private Map<String,String> domainMap = null;
	private ExternalDbManager dbManager = null;
	private String fileId = "";
	private boolean hasBibliography = false;
	private boolean includeAdviceNotes = false;
	private boolean includeGrammar = false;
	private boolean includePersonalNotes = false;
	private boolean combineNotes = false;
	
	public TextInformationToPdf (
			JsonObject jsonObject
			, String textId
			, Map<String,String> domainMap
			, ExternalDbManager dbManager
			, String fileId
			, boolean includePersonalNotes
			, boolean includeAdviceNotes
			, boolean includeGrammar
			, boolean combineNotes
			)   throws JsonParseException {
		this.jsonObject = jsonObject;
		this.textId = textId;
		this.domainMap =  domainMap;
		this.dbManager = dbManager;
		this.fileId = fileId;
		this.includeAdviceNotes = includeAdviceNotes;
		this.includePersonalNotes = includePersonalNotes;
		this.includeGrammar = includeGrammar;
		this.combineNotes = combineNotes;
		this.process();
	}
	
	public List<TextualNote> sortNotes(List<TextualNote> list) {
		List<TextualNote> result = new ArrayList<TextualNote>();
		Map<String, List<String>> noteFollowers = new TreeMap<String,List<String>>();
		Map<String, TextualNote> noteMap = new TreeMap<String,TextualNote>();
		
		// create the maps
		for (TextualNote note : list) {
			noteMap.put(note.id, note);
			String follows = "Root";
			if (note.followsNoteId.length() > 0) {
				follows = note.followsNoteId;
			}
			List<String> followers = new ArrayList<String>();
			if (noteFollowers.containsKey(follows)) {
				followers = noteFollowers.get(follows);
			}
			followers.add(note.getId());
			noteFollowers.put(follows, followers);
		}
		// create the sorted result
		List<String> followers = this.getFollowers("Root", noteFollowers);
		int counter = 1;
		for (String id : followers) {
			TextualNote note = noteMap.get(id);
			note.adhocSortKey = String.format("%03d", counter);
			result.add(note);
			counter++;
		}
		return result;
	}
	
	// makes a recursive call
	public List<String> getFollowers(String forId, Map<String,List<String>> map) {
		List<String> result = new ArrayList<String>();
		if (map.containsKey(forId)) {
			List<String> followers = map.get(forId);
			if (followers != null) {
				for (String follower : followers) {
					if (! result.contains(follower)) { // just in case this is cyclical instead of a linked list
						result.add(follower);
						List<String> moreFollowers = this.getFollowers(follower, map);
						for (String id : moreFollowers) {
							result.add(id);
						}
					}
				}
			}
		}
		return result;
	}

	
	private String createBibFileContent() {
		StringBuffer sb = new StringBuffer();
		for (JsonObject json: this.biblioJsonStrings.values()) {
			try {
				BibliographyEntry record = 
						 (BibliographyEntry) gson.fromJson(
						json.toString()
						, SCHEMA_CLASSES
							.classForSchemaName(
									json.get("_valueSchemaId").getAsString())
									.ltkDb.getClass()
				);
				String bibTex = record.toBibtex();
				sb.append(bibTex);
				sb.append("\n");
			} catch (Exception e) {
				ErrorUtils.report(logger, e);
			}
		}
		return sb.toString();
	}

	public void process() {
		IdManager idManager = new IdManager(this.textId);

		this.texFileSb.append("\\documentclass[extrafontsizes,12pt]{memoir}\n");
		this.texFileSb.append("\\usepackage{multicol}%\n");
		this.texFileSb.append("\\usepackage{system/ocmc-grammar}%\n");
		this.texFileSb.append("\\usepackage[hyphenate]{system/ocmc-liturgical-text}%\n");
		this.texFileSb.append("\\input{system/control} %\n");
		this.texFileSb.append("\\usepackage[defaultlines=4,all]{nowidow}%\n");
		
		this.loadAbbreviations();
		if (this.includeGrammar) {
			this.loadGrammar();
			// add the values for the titles
			String title = this.tokens.get(0).getAsString() + " " + this.tokens.get(1).getAsString();
			this.texFileSb.append(OslwUtils.getOslwTitleResources(
					idManager.getLibrary()
					, title
					, title
					, title
					, ""
					)
			);
		}
		this.loadNotes();
		
		this.hasBibliography = this.biblioJsonStrings.size() > 0;
		if (this.hasBibliography) {
			this.hasBibliography = true;
			this.texFileSb.append("\\addbibresource{");
			this.texFileSb.append(this.fileId);
			this.texFileSb.append(".bib}%\n\n");
			this.bibtexFileSb.append(this.createBibFileContent());
		}
		
		this.texFileSb.append("\\begin{document}%\n");
		this.texFileSb.append("\\mainmatter%\n");
		this.texFileSb.append("\\selectlanguage{english}\n");


		// set the name of the template
		this.texFileSb.append("\\ltChapter{pdf}{title}\n");

		// provide the topic and key we are using
		this.texFileSb.append("\\begin{center}\n");
		this.texFileSb.append("AGES Topic\\textasciitilde Key: ");
		this.texFileSb.append(idManager.getTopic());
		this.texFileSb.append("\\textasciitilde ");
		this.texFileSb.append(idManager.getKey());
		this.texFileSb.append("\n");
		this.texFileSb.append("\\end{center}\n");
		
		// process the content
		this.texFileSb.append(this.getVersionsAsLatex());
		this.texFileSb.append(this.getNotesAsLatex());
		if (this.includeGrammar) {
			this.texFileSb.append(this.getInterlinearAsLatex());
			this.texFileSb.append(this.getDependencyDiagramAsLatex());
		}
		this.texFileSb.append(this.getAbbreviationsAsLatex());
		
		if (this.hasBibliography) {
			this.texFileSb.append("\\section*{Bibliography}\n\n");
			this.texFileSb.append("\\selectlanguage{english}\n");
			this.texFileSb.append("\\printbibliography[heading=bibempty]\n");
		}
		if (this.includePersonalNotes) {
			this.texFileSb.append(this.getUserNotesAsLatex());
		}
		// close out the generation
		this.texFileSb.append(
				"\n\n\\tiny\\textit{Generated ");
		this.texFileSb.append(ZonedDateTime.now(ZoneOffset.UTC));
		this.texFileSb.append(" (Universal Time)  using liturgical software from the Orthodox Christian Mission Center (OCMC), St. Augustine, FL, USA. Glory to God! Δόξα σοι, ὁ Θεὸς ἡμῶν· δόξα σοι! }%\n");
		this.texFileSb.append("\\end{document}%\n");
	}
	
	public String getUserNotesAsLatex() {
		StringBuffer sb = new StringBuffer();
		sb.append("\n\\vfill%\n");
		sb.append("\\pagebreak%\n");
		this.texFileSb.append("\\section{Your Personal Notes}\n\n");
		if (this.userList.size() > 0) {
			for (UserNote note : this.userList) {
				if (note.getValue() != null) {
					sb.append("\n");
					sb.append(note.getValue());
					if (note.getTags().size() > 0) {
						sb.append(" Tags: ");
						sb.append(note.getTags().toString());
					}
					sb.append("\n\n");
				}
			}
		} else {
			sb.append("No personal notes found for this text.");
		}
		return sb.toString();
	}
	
	public String getInterlinearAsLatex() {
		StringBuffer sb = new StringBuffer();
		sb.append("\n\\vfill\n\\newpage\n");
		sb.append("\\section{Interlinear Text}\n");
		sb.append("This section provides information about the grammar of words (that is, the morphology). The Greek words appear in the same order as they do in the source text.\n\n");
		sb.append(this.nodesToInterlinear());
		sb.append("\n\\sectionline\n");
		return sb.toString();
	}

	public String getDependencyDiagramAsLatex() {
		StringBuffer sb = new StringBuffer();
		sb.append("\n\\vfill\n\\newpage\n");
		sb.append("\\section{Dependency Diagram}\n");
		sb.append("This section uses a dependency diagram to show the syntactic structure of the text.  \\textit{Syntax} means \\textit{the grammatical relationship between words}, that is, \\textit{the way words are put together to create phrases and clauses and sentences}.  This diagram shows the structure based on a type of grammar theory called dependency grammar. The order of each Greek word in the diagram is based on the word it depends on. It appears indented and after the word it depends on. The first word to appear in the diagram is the root of the structure.\n");
		sb.append("\\newline");
		sb.append(this.processNode(null, new StringBuffer()));
		sb.append("\n\\sectionline\n");
		return sb.toString();
	}
	
	private void loadGrammar() {
		JsonArray array = this.jsonObject.get("grammar").getAsJsonArray();
		this.tokens = array.get(1).getAsJsonObject().get("tokens").getAsJsonArray();
		this.nodes = array.get(3).getAsJsonObject().get("nodes").getAsJsonArray();

		for (JsonElement e : this.nodes) {
			TokenAnalysis node = this.gson.fromJson(e, TokenAnalysis.class);
			String dependsOn = node.getDependsOn();
			List<TokenAnalysis> children = null;
			if (this.map.containsKey(dependsOn)) {
				children = this.map.get(dependsOn);
			} else {
				children = new ArrayList<TokenAnalysis>();
			}
			children.add(node);
			this.map.put(dependsOn, children);
		}
	}
		
	private String nodesToInterlinear() {
		StringBuffer sb = new StringBuffer();
		int j = this.nodes.size();
		sb.append("\n\\begingl[glstyle=nlevel]\n");
		for (int i=0; i < j; i++) {
			TokenAnalysis node = this.gson.fromJson(this.nodes.get(i), TokenAnalysis.class);
			String gloss = node.getGloss();
			gloss = gloss.replaceAll("\\[", "{[}");
			gloss = gloss.replaceAll("\\]", "{]}");
			sb.append(i+1);
			sb.append("[");
			sb.append(node.getToken());
			sb.append("/");
			sb.append(gloss);
			sb.append("/");
			sb.append(node.getGrammar());
			sb.append("/");
			sb.append(node.getLemma());
			sb.append("]\n");
			this.addGrammarAbbreviations(node.getGrammar());
		}
		sb.append("\\endgl\n\\vfill\n");
		return sb.toString();
	}
	
	private void addGrammarAbbreviations(String grammar) {
		try {
			String [] keys = grammar.split("\\.");
			for (String key : keys) {
				if (! this.usedAbbreviations.containsKey(key)) {
					String value = "";
					if (this.abbr.containsKey(key)) {
						value = this.abbr.get(key);
					}
					this.usedAbbreviations.put(key, value);
				}
			}
		} catch (Exception e) {
			// ignore
		}
	}

	private void addSyntacticLabel(String key) {
		try {
				if (! this.usedAbbreviations.containsKey(key)) {
					String value = "";
					if (this.abbr.containsKey(key)) {
						value = this.abbr.get(key);
					}
					this.usedAbbreviations.put(key, value);
				}
		} catch (Exception e) {
			// ignore
		}
	}
	private StringBuffer processNode(
			TokenAnalysis node
			, StringBuffer sb
			) {
		String key = "";
	    if (node == null) {
	        key = "Root";
	     } else {
	 	    sb.append("\\begin{mdframed}");
		    sb.append("[everyline=true,style=dependency]");
	         key  = node.getKey();
	         int intKey = Integer.parseInt(key);
	         intKey++;
	         String label = node.getLabel();
	          this.addSyntacticLabel(label);
	          if (label.endsWith("_CO")) {
	            label = label.replace("_", "\\textunderscore ");
	          }
	          String gloss = node.getGloss();
	          gloss = gloss.replaceAll("\\[", "{[}");
	          gloss = gloss.replaceAll("\\]", "{]}");
	          sb.append("\\grNode");
	          sb.append("{");
			  sb.append(intKey);
			  sb.append("}");
				sb.append("{");
				sb.append(label);
				sb.append( "}");
				sb.append("{");
				sb.append(node.getToken());
				sb.append("}");
				sb.append("{");
				sb.append(gloss);
				sb.append("}");
				sb.append("{");
				sb.append(node.getGrammar());
				sb.append("}");
				sb.append("{");
				sb.append(node.getLemma());
				sb.append("}\n");
	      }
		if (this.map.containsKey(key)) {
			List<TokenAnalysis> children = this.map.get(key);
			for (TokenAnalysis child : children) {
				this.processNode(child, sb);
			}
		}
	    if (node != null) {
		    sb.append("\\end{mdframed}\n");
	    }
		return sb;
	}
	
	public void loadAbbreviations() {
		for (DEPENDENCY_LABELS v : DEPENDENCY_LABELS.values()) {
			abbr.put(v.keyname, v.description);
		}
		for (GRAMMAR_ABBREVIATIONS v : GRAMMAR_ABBREVIATIONS.values()) {
			abbr.put(v.keyname, v.description);
		}
	}
	public String getAbbreviationsAsLatex() {
		StringBuffer sb = new StringBuffer();
		if (this.usedAbbreviations.size() > 0) {
			sb.append("\\vfill\\newpage\n");
			sb.append("\\section{List of Abbreviations and Acronymns}\n");
			sb.append("\\begin{tabular}{ r | l }\n");
			int i = 0;
			for (Entry<String,String> entry : this.usedAbbreviations.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				if (! value.startsWith("empty")) {
			        if (key.endsWith("_CO")) {
			            key = key.replace("_", "\\textunderscore ");
			    }
				sb.append(key);
				sb.append(" & ");
				sb.append(entry.getValue());
				sb.append("\\\\");
				sb.append("\n");
				i++;
				if (i == 25) {
					sb.append("\\end{tabular}\n");
					sb.append("\\vfill\\newpage\n");
					sb.append("\\begin{tabular}{ r | l }\n");
					i = 0;
				}
				}
			}
			sb.append("\\end{tabular}\n");
		}
		return sb.toString();
	}
	
	/**
	 * This is what you need to do:
	 * 1. See if you can create a map of notes, where the key = the scope
	 * 2. If that is successful, then output the notes grouped by the scope
	 *     but, you will need to determine the correct order in which they should
	 *     appear based on the GEV-SOT.
	 *  3. If the scope is not using GEV-SOT, then just group them by the note type.
	 * @return
	 */
	public String getNotesAsLatex() {
		StringBuffer sb = new StringBuffer();
		sb.append("\\vfill%\n");
		sb.append("\\clearpage%\n");
		sb.append("\\section{Summary}\n");
		for (TextualNote note : this.summaryList) {
			sb.append(note.getValue());
			sb.append("\n");
		}
		sb.append("\\section{Discussion}\n");
		if (this.combineNotes) {
			sb.append(this.combineNotes());
		} else {
			sb.append(this.processNotesByType());
			if (this.includeAdviceNotes) {
				sb.append("\\section{Advice for Translators and Translation Checkers}\n");
				sb.append(this.processAdviceNotes());
			}
		}
		sb.append("\n\\sectionline\n");
		return sb.toString();
	}
	
	private void addListToBigMap(List<TextualNote> listToAdd) {
		for (TextualNote note : listToAdd) {
			String scope = note.getLiturgicalScope() + note.getLiturgicalLemma();
			Map<NOTE_TYPES, List<TextualNote>> multi = new TreeMap<NOTE_TYPES,List<TextualNote>>();
			if (this.bigMap.containsKey(scope)) {
				multi = this.bigMap.get(scope);
			}
			List<TextualNote> list = new ArrayList<TextualNote>();
			if (multi.containsKey(note.getNoteType())) {
				list = multi.get(note.getNoteType());
			}
			list.add(note);
			multi.put(note.getNoteType(), list);
			this.bigMap.put(scope, multi);
		}
	}

	private String getNoteAsGenericLatex(TextualNote note) {
		StringBuffer sb = new StringBuffer();
		sb.append("\\noteGeneric{");
		sb.append(note.getNoteType().notename);
		sb.append("}{");
		if (note.getNoteTitle().trim().length() > 0) {
			sb.append(note.getNoteTitle());
			sb.append("}{");
		}
		sb.append(note.getValue());
		sb.append("}");
		return sb.toString();
	}

	private String getNotesAsGenericLatex(List<TextualNote> list) {
		StringBuffer sb = new StringBuffer();
		for (TextualNote note : list) {
			sb.append(this.getNoteAsGenericLatex(note));
		}
		return sb.toString();
	}
	
	private String getNoteAsReferenceLatex(TextualNote note) {
		StringBuffer sb = new StringBuffer();
		sb.append("\\noteRefersTo{");
		sb.append(note.getNoteType().notename);
		sb.append("}{");
		if (note.getNoteTitle().trim().length() > 0) {
			sb.append(note.getNoteTitle());
			sb.append("}{");
		}
		sb.append(note.getValue());
		sb.append("}");
		return sb.toString();
	}

	private String getNotesAsReferencesLatex(List<TextualNote> list) {
		StringBuffer sb = new StringBuffer();
		for (TextualNote note : list) {
			sb.append(this.getNoteAsReferenceLatex(note));
		}
		return sb.toString();
	}
	private String combineNotes() {
		StringBuffer sb = new StringBuffer();
		// load the big Map
		this.addListToBigMap(this.notesList);
		if (this.includeAdviceNotes) {
			this.addListToBigMap(this.adviceList);
		}
		// process the notes grouped by scope
		for (String scopeAndLemma : this.bigMap.keySet()) {
			StringBuffer combo = new StringBuffer();
			Map<NOTE_TYPES, List<TextualNote>> scopeMap = this.bigMap.get(scopeAndLemma);
			NOTE_TYPES sampleKey = null;
			for (NOTE_TYPES t : scopeMap.keySet()) {
				sampleKey = t;
				break;
			}
			TextualNote sample = scopeMap.get(sampleKey).get(0);
			String scope = sample.getLiturgicalScope();
			String lemma = sample.getLiturgicalLemma();
			combo.append("\n\n\\noteLexical{");
			combo.append(scope);
			combo.append("}{");
			combo.append(lemma);
			combo.append("} ");
			// we need to strictly control the order for some types of notes
			if (scopeMap.containsKey(NOTE_TYPES.MEANING)) {
				combo.append(this.getNotesAsGenericLatex(scopeMap.get(NOTE_TYPES.MEANING)));
			}
			if (scopeMap.containsKey(NOTE_TYPES.GRAMMAR)) {
				combo.append(this.getNotesAsGenericLatex(scopeMap.get(NOTE_TYPES.GRAMMAR)));
			}
			
			for (Entry<NOTE_TYPES, List<TextualNote>> entry : scopeMap.entrySet()) {
				NOTE_TYPES key = entry.getKey();
				if (key == NOTE_TYPES.REF_TO_BIBLE) {
					for (TextualNote note : scopeMap.get(key)) {
						combo.append("\\noteRefToBible{");
						combo.append(note.biblicalScope);
						combo.append("}{");
						combo.append(note.biblicalLemma);
						if (note.noteTitle.trim().length() > 0) {
							combo.append("}{");
							combo.append(note.noteTitle);
						}
						if (note.getValue().trim().length() > 0) {
							combo.append("}{");
							combo.append(note.getValue());
						} else {
							combo.append(".");
						}
						combo.append("}");
					}
				} else if (key.name().startsWith("REF_TO")) {
					combo.append(this.getNotesAsReferencesLatex(scopeMap.get(key)));
				} else {
					if (key == NOTE_TYPES.MEANING
							|| key == NOTE_TYPES.GRAMMAR
							|| key == NOTE_TYPES.ADVICE_FOR_TRANSLATION_CHECKERS
							|| key == NOTE_TYPES.ADVICE_FOR_TRANSLATORS
							|| key == NOTE_TYPES.CHECK_YOUR_BIBLE
							) {
						// ignore
					} else {
						combo.append(this.getNotesAsGenericLatex(scopeMap.get(key)));
					}
				}
			}
			if (this.includeAdviceNotes) {
				if (scopeMap.containsKey(NOTE_TYPES.ADVICE_FOR_TRANSLATORS)) {
					combo.append(this.getNotesAsGenericLatex(scopeMap.get(NOTE_TYPES.ADVICE_FOR_TRANSLATORS)));
				}
				if (scopeMap.containsKey(NOTE_TYPES.ADVICE_FOR_TRANSLATION_CHECKERS)) {
					combo.append(this.getNotesAsGenericLatex(scopeMap.get(NOTE_TYPES.ADVICE_FOR_TRANSLATION_CHECKERS)));
				}
				if (scopeMap.containsKey(NOTE_TYPES.CHECK_YOUR_BIBLE)) {
					for (TextualNote note : scopeMap.get(NOTE_TYPES.CHECK_YOUR_BIBLE)) {
						combo.append("\\noteCheckYourBible{");
						combo.append(note.biblicalScope);
						combo.append("}{");
						combo.append(note.biblicalLemma);
						if (note.noteTitle.trim().length() > 0) {
							combo.append("}{");
							combo.append(note.noteTitle);
						}
						if (note.getValue().trim().length() > 0) {
							combo.append("}{");
							combo.append(note.getValue());
						} else {
							combo.append(".");
						}
						combo.append("}");
					}
				}
			}
			sb.append(combo.toString());
		}
		return sb.toString();
	}
	
	private void loadNotes() {
		List<TextualNote> tempAdviceList = new ArrayList<TextualNote>();
		List<TextualNote> tempTopicsList = new ArrayList<TextualNote>();
		for (JsonElement e : this.jsonObject.get("textNotes").getAsJsonArray()) {
			TextualNote note = this.gson.fromJson(
					e.getAsJsonObject().get("properties(to)").getAsJsonObject()
					, TextualNote.class
					);
			note.setValueFormatted(this.htmlToLatex(note.getValueFormatted()));
			if (note.getNoteType() == NOTE_TYPES.UNIT) {
				this.summaryList.add(note);
			} else 	if (note.getNoteType() == NOTE_TYPES.ADVICE_FOR_TRANSLATION_CHECKERS
					|| note.getNoteType() == NOTE_TYPES.ADVICE_FOR_TRANSLATORS
					) {
					tempAdviceList.add(note);
			} else {
				tempTopicsList.add(note);
			}
		}
		if (this.includeAdviceNotes) {
			this.adviceList = this.sortNotes(tempAdviceList); // sorts using the followsNoteId property
		}
		this.notesList = this.sortNotes(tempTopicsList); // sorts using the followsNoteId property
		if (this.includePersonalNotes) {
			if (this.jsonObject.has("userNotes")) {
				for (JsonElement e : this.jsonObject.get("userNotes").getAsJsonArray()) {
					UserNote note = this.gson.fromJson(
							e.getAsJsonObject().get("properties(to)").getAsJsonObject()
							, UserNote.class
							);
					this.userList.add(note);
				}
			}
		}
	}

	private String processNotesByType() {
		StringBuffer sb = new StringBuffer();
		NOTE_TYPES currentType = null;
		Collections.sort(
				this.notesList
				, TextualNote.noteTypeAdHocComparator);
		for (TextualNote note : this.notesList) {
			NOTE_TYPES type = note.getNoteType();
			if (currentType != type) {
				sb.append("\n\\subsection{");
				sb.append(type.fullname);
				sb.append("}\n");
				currentType = type;
			}
			if (type == NOTE_TYPES.REF_TO_BIBLE) {
				sb.append(this.getNoteAsLatexForBibleRef(note));
			} else if (type.name().startsWith("REF_TO")){
				sb.append(
						this.getNoteAsLatexRefersTo(
							note
						)
				);
			} else {
				sb.append(
						this.getNoteAsLatexForNonRef(
							note
							, true
						)
				);
			}
		}
		return sb.toString();
	}

	private String processAdviceNotes() {
		StringBuffer sb = new StringBuffer();
		Collections.sort(
				this.notesList
				, TextualNote.noteTypeAdHocComparator);
		for (TextualNote note : this.adviceList) {
			sb.append(
					this.getNoteAsLatexForNonRef(
						note
						, true
					)
			);
		}
		return sb.toString();
	}

	private String getNoteAsLatexForBibleRef(
			TextualNote note
			) {
		StringBuffer sb = new StringBuffer();
			sb.append("\n\\noteLexicalRefToBibleTitle{");
			sb.append(note.liturgicalScope);
			sb.append("}{");
			sb.append(note.liturgicalLemma);
			sb.append("}{");
			sb.append(note.biblicalScope);
			sb.append("}{");
			sb.append(note.biblicalLemma);
			if (note.noteTitle.trim().length() > 0) {
				sb.append("}{");
				sb.append(note.noteTitle);
			}
			if (note.value.trim().length() > 0) {
				sb.append("}{");
				sb.append(note.value);
			}
		sb.append("}\n");

		return sb.toString();
	}
	
	private String getNoteAsLatexRefersTo(
			TextualNote note
			) {
		StringBuffer sb = new StringBuffer();
		sb.append("\n\\noteRefersTo{");
		sb.append(note.liturgicalScope);
		sb.append("}{");
		sb.append(note.liturgicalLemma);
		sb.append("}{");
		sb.append(note.noteTitle);
		sb.append("}{");
		sb.append(note.value);
		sb.append("}\n");

		return sb.toString();
	}
	
	private String htmlToLatex(String html) {
		try {
			// process the anchors, which represent an abbreviation or a citation
			Document doc = Jsoup.parse(html);
			Elements anchors = doc.select("a");
			for (Element anchor : anchors) {
				String id = anchor.attr("href");
				if (id.startsWith("http")) {
					URL url = new URL(id);
					id = url.getPath().substring(1);
				}
				String dataValue = anchor.attr("data-value");
				IdManager idManager = new IdManager(id);
				switch (idManager.getTopic()) {
				case ("abbreviation"): {
					if (! this.abbrJsonStrings.containsKey(id)) {
						try {
							ResultJsonObjectArray queryResult = this.dbManager.getForId(id);
							if (queryResult.valueCount > 0) {
								this.abbrJsonStrings.put(id, queryResult.getFirstObjectValueAsObject());
							}
						} catch (Exception e) {
							ErrorUtils.report(logger, e, id + " not found");
						}
					}
					break;
				}
				case ("biblioentry"): {
					if (! this.biblioJsonStrings.containsKey(id)) {
						try {
							ResultJsonObjectArray queryResult = this.dbManager.getForId(id);
							if (queryResult.valueCount > 0) {
								this.biblioJsonStrings.put(id, queryResult.getFirstObjectValueAsObject());
							}
						} catch (Exception e) {
							ErrorUtils.report(logger, e, id + " not found");
						}
					}
					break;
				}
				default: {
					// TODO
				}
				}
				// convert to latex citation
				anchor.tagName("span");
				// TODO: language selection should be automatic in Babel but not working.
				// it is printing και for and, as in Louw και Nida.  So, for now, force it to be English
				anchor.text("\\selectlanguage{english}\\cite{" + dataValue + "}");
			}
			html = doc.html();
			html = html.replaceAll("<p>", "");
			html = html.replaceAll("</p>", "\n");
			html = html.replaceAll("<em>", "\\\\textit{");
			html = html.replaceAll("</em>", "}");
			html = html.replaceAll("<strong>", "\\\\textbf{");
			html = html.replaceAll("</strong>", "}");
			html = html.replaceAll("<ins>", "\\\\underline{");
			html = html.replaceAll("</ins>", "}");
			html = html.replaceAll("&nbsp;", " ");
		} catch (Exception e) {
			ErrorUtils.report(logger, e);
		}
		return html;
	}
	
	private String getNoteAsLatexForNonRef(
			TextualNote note
			, boolean lexical
			) {
		StringBuffer sb = new StringBuffer();
		sb.append("\\noteLexicalTitleText{");
		sb.append(note.liturgicalScope);
		sb.append("}{");
		sb.append(note.liturgicalLemma);
		if (note.getNoteTitle().trim().length() > 0) {
			sb.append("}{");
			sb.append(note.noteTitle);
		}
		if (note.value.trim().length() > 0) {
			sb.append("}{");
			sb.append(note.value);
		}
		sb.append("}\n\n");

		return sb.toString();
	}
	public String getVersionsAsLatex() {
		StringBuffer transSb = new StringBuffer();
		StringBuffer result = new StringBuffer();
		boolean hasScansion = false;
		
		String greekLibrary = "gr_gr_cog";
		String libraryLatex = "";
		String greekValue = "";
		transSb.append("\\section{Translations}\n");
		transSb.append("\\setlength{\\arrayrulewidth}{1mm}");
		transSb.append("\\setlength{\\tabcolsep}{18pt}");
		transSb.append("\\renewcommand{\\arraystretch}{1.5}");
		transSb.append("\\begin{tabular}{ |p{3cm}|p{12cm}| }\n\\hline\n");
		for (JsonElement e : this.jsonObject.get("versions").getAsJsonArray()) {
			JsonObject o = e.getAsJsonObject();
			String library = o.get("library").getAsString();
			IdManager idManager = new IdManager(o.get("id").getAsString());
			StringBuffer lf  = new StringBuffer();
			lf.append(idManager.getLibraryLanguage());
			lf.append("\\textunderscore ");
			lf.append(idManager.getLibraryCountry());
			lf.append("\\textunderscore ");
			lf.append(idManager.getLibraryRealm());
			libraryLatex = lf.toString();
			
			String value = o.get("value").getAsString();
			if (value != null && value.trim().length() > 0) {
				// check for scansion but don't bother if we already found a text with it...
				if (!hasScansion) {
					if (value.contains("*") || value.contains("/")) {
						if (! hasScansion) {
							hasScansion = true;
						}
					}
				}
				if (library.equals(greekLibrary)) {
					greekValue = value;
				} else {
					transSb.append(libraryLatex);
					transSb.append(" & ");
					transSb.append(value);
					transSb.append(" \\\\ ");
					transSb.append("\n\\hline\n");
				}
				// add the version (library) to the list of those used
				if (! this.usedAbbreviations.containsKey(libraryLatex)) {
					String description = "";
					if (this.domainMap.containsKey(idManager.getLibrary())) {
						description = this.domainMap.get(idManager.getLibrary());
						if (idManager.getId().equals(description)) { // this means the description is missing
							description = "";
						}
					} else {
						description = "";
					}
					if (description.contains("_")) {
						description = "(description unavailable)";
					}
					this.usedAbbreviations.put(libraryLatex, description);
				}
			}
		}
		transSb.append("\n\\hline\n\\end{tabular}\n\n\n");
		if (hasScansion) {
			transSb.append("Note: some liturgical hymns originally used punctuation marks to indicate the boundary of metric feet. They do not have a grammatical role.  They are called \\textit{scansion} symbols. In the modern version of source text or translations you might see asterisks (*) or forward slashes (/) used as scansion symbols.\n");
		}

		if (greekValue.length() > 0) {
			result.append("\\section{Source Text}\n");
			result.append("\\setlength{\\arrayrulewidth}{1mm}");
			result.append("\\setlength{\\tabcolsep}{18pt}");
			result.append("\\renewcommand{\\arraystretch}{1.5}");
			result.append("\\begin{tabular}{ |p{3cm}|p{12cm}| }\n\\hline\n");
			result.append(libraryLatex);
			result.append(" & ");
			result.append(greekValue);
			result.append(" \\\\ ");
			result.append("\n");
			result.append("\n\\hline\n\\end{tabular}\n\n");
		}
		result.append(transSb.toString());
		return result.toString();
	}
	public String getBasePath() {
		return basePath;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	public String getPathToPdf() {
		return pathToPdf;
	}

	public void setPathToPdf(String pathToPdf) {
		this.pathToPdf = pathToPdf;
	}

	/**
	 * Get the the Xeatex tex file content that Xelatex can use
	 * to create the PDF.
	 * @return
	 */
	public StringBuffer getTexFileContent() {
		return texFileSb;
	}

	public void setTexFileContent(StringBuffer resFileSb) {
		this.texFileSb = resFileSb;
	}

	public boolean hasBibliography() {
		return hasBibliography;
	}

	public void setHasBibliography(boolean hasBibliography) {
		this.hasBibliography = hasBibliography;
	}

	public StringBuffer getBibtexFileSb() {
		return bibtexFileSb;
	}

	public void setBibtexFileSb(StringBuffer bibtexFileSb) {
		this.bibtexFileSb = bibtexFileSb;
	}


}
