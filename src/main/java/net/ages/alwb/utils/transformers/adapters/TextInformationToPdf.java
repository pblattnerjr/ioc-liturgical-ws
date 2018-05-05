package net.ages.alwb.utils.transformers.adapters;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.ocmc.ioc.liturgical.schemas.constants.nlp.DEPENDENCY_LABELS;
import org.ocmc.ioc.liturgical.schemas.constants.nlp.GRAMMAR_ABBREVIATIONS;
import org.ocmc.ioc.liturgical.schemas.models.db.docs.nlp.TokenAnalysis;
import org.ocmc.ioc.liturgical.schemas.models.db.docs.notes.TextualNote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.ages.alwb.utils.core.id.managers.IdManager;
import net.ages.alwb.utils.oslw.OslwUtils;

/**
 * Liturgical books and services are provided by AGES Initiatives as
 * html and PDF files, and by OSLW as PDF files.
 * 
 * For both AGES and OSLW, template files are created that specify
 * the order of information and its formatting.  The information is 
 * retrieved using the combination of a domain, topic, and key.
 * 
 * A meta template can be created from the AGES HTML files, or from the OSLO tex files.
 *  
 * Such a meta template is used by this class to create a PDF file.
 *  
 * This class reads a json string encoding the meta Template for a liturgical book or service,
 * generates OSLO files from the metadata,
 * calls Xelatex to generate a PDF file
 * and returns the path to the file.
 * 
 * The generated PDF can have one, two, or three columns.
 * If one column, there is only a left library to be used.
 * If two columns, there is a left and right library to be used.
 * If three columns, there is a left, center, and right library to be used.
 * There are also 'fallback' libraries that can be specified.  That is,
 * if a specified library does not contain the required topic/key, then
 * the fallback library will be searched.
 * 
 * If the language = English, if the fallback is not found, it will default to AGES_ENGLISH,
 * i.e. en_us_dedes.
 * 
 * Otherwise, if the fallback is not found, it will use gr_gr_cog.
 * 
 */

public class TextInformationToPdf {
	private static final Logger logger = LoggerFactory.getLogger(TextInformationToPdf.class);
	private 	String basePath = "";
	private 	String pathToPdf = "";
	private String textId = "";
	private JsonObject jsonObject = null;
	private StringBuffer texFileSb = new StringBuffer(); // latex tex file content
	private Gson gson = new Gson();
	private Map<String,List<TokenAnalysis>> map = new TreeMap<String,List<TokenAnalysis>>();
	private JsonArray nodes = null;
	private JsonArray tokens = null;
	private Map<String,String> abbr = new TreeMap<String,String>();
	private Map<String,String> usedAbbreviations = new TreeMap<String,String>();
	private Map<String,List<TextualNote>> textNoteMapByType = new TreeMap<String,List<TextualNote>>();
	private Map<String,List<TextualNote>> textNoteMapByScope = new TreeMap<String,List<TextualNote>>();
	
	public TextInformationToPdf (
			JsonObject jsonObject
			, String textId
			)   throws JsonParseException {
		this.jsonObject = jsonObject;
		this.textId = textId;
		this.process();
	}
	
	public void process() {
		IdManager idManager = new IdManager(this.textId);
		this.texFileSb.append("\\documentclass[extrafontsizes,12pt]{memoir}\n");
		this.texFileSb.append("\\usepackage{multicol}%\n");
		this.texFileSb.append("\\usepackage[hyphenate]{system/ocmc-liturgical-text}%\n");
		this.texFileSb.append("\\usepackage{system/ocmc-grammar}%\n");
		this.texFileSb.append("\\input{system/control} %\n");
		this.texFileSb.append("\\usepackage[defaultlines=4,all]{nowidow}%\n");
		
		this.texFileSb.append("\\begin{document}%\n");
		this.texFileSb.append("\\mainmatter%\n");
//		this.texFileSb.append("\\linenumbers");

		this.loadAbbreviations();
		this.loadGrammar();
		
		// add the values for the titles
		String title = this.tokens.get(0).getAsString() + " " + this.tokens.get(1).getAsString();
		String subtitle = idManager.getTopic() + " " + idManager.getKey();
		this.texFileSb.append(OslwUtils.getOslwTitleResources(
				idManager.getLibrary()
				, title
				, title
				, title
				, ""
				)
		);

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
		this.texFileSb.append(this.getInterlinearAsLatex());
		this.texFileSb.append(this.getDependencyDiagramAsLatex());
		this.texFileSb.append(this.getAbbreviationsAsLatex());
		
		// close out the generation
		this.texFileSb.append(
				"\n\n\\tiny\\textit{Generated ");
		this.texFileSb.append(ZonedDateTime.now(ZoneOffset.UTC));
		this.texFileSb.append(" (Universal Time)  using liturgical software from the Orthodox Christian Mission Center (OCMC), St. Augustine, FL, USA.}%\n");
		this.texFileSb.append("\\vfill%\n");
		this.texFileSb.append("\\pagebreak%\n");
		this.texFileSb.append("\\end{document}%\n");
	}
	
	/**
	 * TODO:
	 * 1. Iterate through all text notes, find occurrences of \cite.
	 * 2. If the cite key is not in the referenceMap, add it along with the reference data.
	 * 3. Get the reference data from the database??  
	 * 4. Add the references as an inline file.
	 * 5. If after the iteration the referenceMap is empty, we will want to suppress 
	 * printing the bibliography.
	 * @return
	 */
	private String getBibresource() {
		StringBuffer sb = new StringBuffer();
		return sb.toString();
	}
	
	public String getInterlinearAsLatex() {
		StringBuffer sb = new StringBuffer();
		sb.append("\\vfill\n\\newpage\n");
		sb.append("\\section{Interlinear Text}\n");
		sb.append("This section provides information about the grammar of words (that is, the morphology). The Greek words appear in the same order as they do in the source text.\n\n");
		sb.append(this.nodesToInterlinear());
		return sb.toString();
	}

	public String getDependencyDiagramAsLatex() {
		StringBuffer sb = new StringBuffer();
		sb.append("\\section{Dependency Diagram}\n");
		sb.append("This section uses a dependency diagram to show the syntactic structure of the text.  \\textit{Syntax} means \\textit{the way words are put together to create phrases and clauses and sentences}.  This diagram shows the structure using dependencies instead of constituency.  It is based on dependency grammar. The order of each Greek word in the diagram is based on the word it depends on. It appears indented and after the word it depends on. The first word to appear in the diagram is the root of the structure.\n");
		sb.append("\\newline");
		sb.append(this.processNode(null, new StringBuffer()));
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
		sb.append("\\section{Summary}\n");
		sb.append("\\section{Discussion}\n");
//		this.groupNotes();
	//	sb.append(this.processNotesByType());
		return sb.toString();
	}
	
	private void groupNotes() {
		for (JsonElement e : this.jsonObject.get("textNotes").getAsJsonArray()) {
			TextualNote note = this.gson.fromJson(e, TextualNote.class);
			List<TextualNote> list = null;
			String key = note.getNoteType().name();
			if (this.textNoteMapByType.containsKey(key)) {
				list = this.textNoteMapByType.get(key);
			} else {
				list = new ArrayList<TextualNote>();
			}
			list.add(note);
			this.textNoteMapByType.put(key, list);
			list = null;
			key = note.getLiturgicalScope().trim();
			if (this.textNoteMapByScope.containsKey(key)) {
				list = this.textNoteMapByScope.get(key);
			} else {
				list = new ArrayList<TextualNote>();
			}
			list.add(note);
			this.textNoteMapByScope.put(key, list);
		}
	}
	
	private String processNotesByType() {
		StringBuffer sb = new StringBuffer();
		for (Entry<String, List<TextualNote>> entry : this.textNoteMapByType.entrySet()) {
			String type = entry.getKey();
			List<TextualNote> list = entry.getValue();
			switch (type) {
			case ("ADVICE_FOR_TRANSLATORS") : {
				break;
			}
			case ("ADVICE_FOR_TRANSLATION_CHECKERS") : {
				break;
			}
			case ("CULTURE") : {
				break;
			}
			case ("GENERAL") : {
				break;
			}
			case ("GEOGRAPHY") : {
				break;
			}
			case ("GRAMMAR") : {
				break;
			}
			case ("LEMMA") : {
				break;
			}
			case ("HISTORY") : {
				break;
			}
			case ("LITURGICAL_USAGE") : {
				break;
			}
			case ("MEANING") : {
				for (TextualNote note : list) {
					sb.append("\\noteLexical{");
					sb.append(note.liturgicalScope);
					sb.append(note.liturgicalLemma);
					sb.append("} ");
					if (note.noteTitle.length() > 0) {
						sb.append(note.noteTitle);
						sb.append(" ");
					}
					sb.append("\\noteMeaning{");
					sb.append(note.value);
					sb.append("}\n");
				}
				break;
			}
			case ("REF_TO_ANIMAL") : {
				break;
			}
			case ("REF_TO_BEING") : {
				break;
			}
			case ("REF_TO_BIBLE") : {
				break;
			}
			case ("REF_TO_CONCEPT") : {
				break;
			}
			case ("REF_TO_EVENT") : {
				break;
			}
			case ("REF_TO_GOD") : {
				break;
			}
			case ("REF_TO_GROUP") : {
				break;
			}
			case ("REF_TO_HUMAN") : {
				break;
			}
			case ("REF_TO_MYSTERY") : {
				break;
			}
			case ("REF_TO_OBJECT") : {
				break;
			}
			case ("REF_TO_PLACE") : {
				break;
			}
			case ("REF_TO_PLANT") : {
				break;
			}
			case ("REF_TO_ROLE") : {
				break;
			}
			case ("TRANSLATORS_NOTE") : {
				break;
			}
			case ("UNIT") : {
				break;
			}
			case ("VOCABULARY") : {
				break;
			}
			}
		}
		return sb.toString();
	}

	public String getVersionsAsLatex() {
		StringBuffer transSb = new StringBuffer();
		StringBuffer result = new StringBuffer();
		
		String greekLibrary = "gr_gr_cog";
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
			String value = o.get("value").getAsString();
			if (value != null && value.trim().length() > 0) {
				if (library.equals(greekLibrary)) {
					greekValue = value;
				} else {
					transSb.append(idManager.getLibraryLanguage());
					transSb.append("\\textunderscore ");
					transSb.append(idManager.getLibraryCountry());
					transSb.append("\\textunderscore ");
					transSb.append(idManager.getLibraryRealm());
					transSb.append(" & ");
					transSb.append(value);
					transSb.append(" \\\\ ");
					transSb.append("\n\\hline\n");
				}
			}
		}
		transSb.append("\n\\hline\n\\end{tabular}\n\n\n");

		if (greekValue.length() > 0) {
			result.append("\\section{Source Text}\n");
			result.append("\\setlength{\\arrayrulewidth}{1mm}");
			result.append("\\setlength{\\tabcolsep}{18pt}");
			result.append("\\renewcommand{\\arraystretch}{1.5}");
			result.append("\\begin{tabular}{ |p{3cm}|p{12cm}| }\n\\hline\n");
			result.append("gr\\textunderscore gr\\textunderscore cog");
			result.append(" & ");
			result.append(greekValue);
			result.append(" \\\\ ");
			result.append("\n");
			result.append("\n\\hline\n\\end{tabular}\n\n\n");
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


}
