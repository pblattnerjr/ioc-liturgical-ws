package net.ages.alwb.utils.transformers.adapters;

import java.net.URL;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.joda.time.LocalDate;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.ocmc.ioc.liturgical.schemas.constants.BIBLIOGRAPHY_CITATION_OVERRIDE;
import org.ocmc.ioc.liturgical.schemas.constants.NOTE_TYPES;
import org.ocmc.ioc.liturgical.schemas.constants.SCHEMA_CLASSES;
import org.ocmc.ioc.liturgical.schemas.constants.nlp.DEPENDENCY_LABELS_PERSEUS;
import org.ocmc.ioc.liturgical.schemas.constants.nlp.GRAMMAR_ABBREVIATIONS;
import org.ocmc.ioc.liturgical.schemas.constants.nlp.UD_DEPENDENCY_LABELS;
import org.ocmc.ioc.liturgical.schemas.models.db.docs.nlp.TokenAnalysis;
import org.ocmc.ioc.liturgical.schemas.models.db.docs.notes.TextualNote;
import org.ocmc.ioc.liturgical.schemas.models.db.docs.notes.UserNote;
import org.ocmc.ioc.liturgical.schemas.models.db.docs.ontology.TextLiturgical;
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
    private String biblioLibraryTopic = "";
	private List<TextualNote> notesList = new ArrayList<TextualNote>();
	private List<TextualNote> checkBibleList = new ArrayList<TextualNote>();
	private List<TextualNote> adviceList = new ArrayList<TextualNote>();
	private List<TextualNote> summaryList = new ArrayList<TextualNote>();
	private List<TextualNote> supplementalList = new ArrayList<TextualNote>();
	private List<UserNote> userList = new ArrayList<UserNote>();
	private List<String> noteIds = new ArrayList<String>();
	private Map<String,String> domainMap = null;
	private ExternalDbManager dbManager = null;
	private String fileId = "";
	private boolean hasBibliography = false;
	private boolean hasBibliographyList = false;
	private boolean hasBibliographyPrimary = false;
	private boolean hasBibliographySecondary = false;
	private boolean hasBibliographyTertiary = false;
	private boolean includeAdviceNotes = false;
	private boolean includeGrammar = false;
	private boolean includePersonalNotes = false;
	private boolean combineNotes = false;
	private boolean createToc = false;
	private String pdfTitle = "";
	private String pdfSubTitle = "";
	private String alignmentLibrary = "";
	private String alignmentLibraryLatex = "";
	private String alignmentText = "";
	private String author = "";
	private String authorTitle = "";
	private String authorAffiliation = "";
	private String citestyle = "authoryear";
	private StringBuffer textFile = new StringBuffer();
	private String NETS = "Quotations marked NETS are taken from A New English Translation of the Septuagint, ©2007 by the International Organization for Septuagint and Cognate Studies, Inc. Used by permission of Oxford University Press. All rights reserved.";
	
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
			, boolean createToc
			, String alignmentLibrary
			, String pdfTitle
			, String pdfSubTitle
			, String author
			, String authorTitle
			, String authorAffiliation
			, String citestyle
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
		this.createToc = createToc;
		this.alignmentLibrary = alignmentLibrary;
		this.pdfTitle = pdfTitle.trim();
		this.pdfSubTitle = pdfSubTitle.trim();
		if (pdfTitle.equals("undefined")) {
			this.pdfTitle = "";
		}
		if (pdfSubTitle.equals("undefined")) {
			this.pdfSubTitle = "";
		}
		this.author = author.trim();
		if (author.equals("undefined")) {
			this.author = "";
		}
		this.authorTitle = authorTitle.trim();
		if (authorTitle.equals("undefined")) {
			this.authorTitle = "";
		}
		this.authorAffiliation = authorAffiliation.trim();
		if (authorAffiliation.equals("undefined")) {
			this.authorAffiliation = "";
		}
		this.citestyle = citestyle;
		if (citestyle.equals("undefined")) {
			this.citestyle = "authoryear";
		}
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
			if (note.followsNoteId.length() > 0 && this.noteIds.contains(note.followsNoteId)) {
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
				// we use hasBibliographyList later to determine whether to print an abbreviation based bibliography
				if (this.hasBibliographyList) {
					// ignore
				} else {
					if (record.citeoverride != BIBLIOGRAPHY_CITATION_OVERRIDE.NONE) {
						this.hasBibliographyList = true;
					}
				}
				switch (record.getSourceType()) {
				case PRIMARY:
					this.hasBibliographyPrimary = true;
					break;
				case SECONDARY:
					this.hasBibliographySecondary = true;
					break;
				case TERTIARY:
					this.hasBibliographyTertiary = true;
					break;
				}
				String bibTex = record.toBibtex();
				sb.append(bibTex);
				sb.append("\n");
			} catch (Exception e) {
				ErrorUtils.report(logger, e);
			}
		}
		return sb.toString();
	}

	private void appendText(String text) {
		this.textFile.append(text);
		this.textFile.append("\n");
	}
	
	private String getTitle() {
		StringBuffer sb = new StringBuffer();
		sb.append("\\Large ");
		sb.append("\\begin{center}\n\\textbf{");
		sb.append(this.pdfTitle);
		this.appendText(this.pdfTitle);
		sb.append("}\n\\end{center}");
		if (this.pdfSubTitle.length() > 0) {
			sb.append("{\\scshape\\large ");
			sb.append("\\begin{center}");
			sb.append(this.pdfSubTitle);
			this.appendText(this.pdfSubTitle);
			sb.append("\\end{center}");
			sb.append("}%\n");
		}
		sb.append("{\\scshape\\small\n");
		sb.append("\\begin{center}\n\\textit{");
		if (this.author.length() > 0) {
			sb.append(this.author);
			this.appendText(this.author);
			sb.append("\\\\");
		}
		if (this.authorTitle.length() > 0) {
			sb.append(this.authorTitle);
			this.appendText(this.authorTitle);
			sb.append("\\\\");
		}
		if (this.authorAffiliation.length() > 0) {
			sb.append(this.authorAffiliation);
			this.appendText(this.authorAffiliation);
			sb.append("\\\\");
		}
		sb.append(LocalDate.now().toString());
		sb.append("}\n\\end{center}");
		sb.append("}%\n");
		if (this.includeAdviceNotes && this.author.contains("Colburn")) {
			sb.append("\\includegraphics[width=.5\\textwidth]{system/images/nicodemos.jpg}");
			sb.append("{\\scshape\\footnotesize\n");
			sb.append("\\begin{center}\n");
			sb.append("O Lord, through the prayers of our Holy Fathers Kosmos the Poet, John of Damascus, and Nikodemos of the Holy Mountain, give us understanding and grace to create translations that are pleasing to You!");
			sb.append("\n\\end{center}");
			sb.append("}%\n");
		}
		sb.append("\\vfill%\n");
		return sb.toString();
	}
	
	private String getTableOfContents() {
		StringBuffer sb = new StringBuffer();
		sb.append("\\tableofcontents*\n");
		sb.append("\\vfill\n");
		return sb.toString();
	}
	
	private String getPreface() {
		StringBuffer sb = new StringBuffer();
		sb.append("\\pagebreak\\n\\renewcommand*{\\pagenumbering}[1]{}\\n\\section{Purpose}\n");
		sb.append("This handbook is for people who speak English as a foreign language and are translating the liturgical texts into a non-Indoeuropean language. It answers the following questions: What does the Greek text say? What does it mean?  What problems might I have translating it and how might I solve those problems?  It answers these questions by providing the Greek text, example English translations, notes about each Greek word or phrase, and a detailed grammatical analysis\\footnote{Comments, corrections, and suggestions for improvement should be sent to Michael Colburn at m.colburn@ocmc.org.}.");
		sb.append("\n\\section{Types of Translations}\n");
		sb.append("The type of translation a person creates depends on what the priority is. If the priority is to preserve the order of the Greek words and phrases and to use words that give the literal meaning of the Greek, I call this a \\textit{Structure-Oriented Translation}.  I created the GE-SOT as an example. If the priority is to convey the meaning of the Greek text, I call this a \\textit{Meaning-Oriented Translation}. I created the GE-MOT as an example. Some translations balance these two priorities, for example, the translation in \\textit{The Festal Menaion} by His Eminence Kallistos Ware and Mother Mary (TFM). I created the GEV as another example. Whether or not a translation is structure-oriented, or meaning-oriented, or falls in between, when you translate a hymn, it is important to create a translation that can be sung based on a melody.  So, the translation sometimes has to be adjusted by using different words or a different word order. Fr. Seraphim Dedes' translation is an example.");
		sb.append("\n\\section{About the Global English Translations}\n");
		sb.append("");
		sb.append("\nThe GEV, GE-SOT and GE-MOT use the Oxford 3000\\footnote{\\url{https://www.oxfordlearnersdictionaries.com/us/about/oxford3000}.} list of the most important English words. People who speak English as a second language should know these words. By trying to use only these 3000 words, the Global English translations have to adjust the translation in ways similar to what happens when people translate the Greek into non-Indoeuropean languages. These translations demonstrate three types of translation.  The GE-SOT is the closest to the Greek word order and the literal meaning.  It is used for the word by word discussion of the meaning and for the interlinear grammar and dependency diagram.  The GE-MOT will help you understand the meaning.  It does this by adding information that is implied by the Greek text.  If you must translate from English, ask your Bishop which translation He prefers. If He is not available to ask, then use the GEV.\n");
		sb.append("\n\\section{How to Use this Handbook}\n");
		sb.append("If you can, first read the Greek text. Then study the  translations. Note what is similar and different. Make a first draft translation from the Greek or the GEV. Write down questions you have. Look for answers in the notes and grammar sections. Make changes to your translation based on what you read. Let other people read your translation.  Ask questions to learn how they understand it.  Make changes as needed. Adjust the translation to fit the melody it will be sung to.");
		return sb.toString();
	}
	
	private String getTitlePage() {
		StringBuffer sb = new StringBuffer();
		sb.append("\\begin{titlingpage}%\n");
		sb.append("\\openup .5em%\n");
		sb.append("\\begin{center}%\n");
		sb.append("\\begin{minipage}{25em}%\n");
		sb.append("\\begin{center}%\n");
		sb.append("\\linespread{2}%\n");
		sb.append(this.getTitle());
		sb.append("\\end{center}%\n");
		sb.append("\\end{minipage}%\n");
		sb.append("\\end{center}%\n");
		sb.append("\\end{titlingpage}%\n");
		return sb.toString();
	}
	
	public void process() {
		IdManager idManager = new IdManager(this.textId);

		this.texFileSb.append("\\documentclass[extrafontsizes,openany,12pt]{memoir}\n");
		this.texFileSb.append("\\usepackage{multicol}%\n");
		this.texFileSb.append("\\usepackage{system/ocmc-grammar}%\n");
		this.texFileSb.append("\\usepackage[hyphenate]{system/ocmc-liturgical-text}%\n");
		this.texFileSb.append("\\input{system/control} %\n");
		this.texFileSb.append("\\usepackage[defaultlines=4,all]{nowidow}%\n");
		boolean sbl = false; // note working yet
		if (sbl) {
			this.texFileSb.append(this.getBiblatexSblPackage());
		} else {
			this.texFileSb.append(this.getBiblatexPackage(
					this.citestyle // bibstyle
					, this.citestyle // citestyle
					, "nyt" // sorting
					, "true" // sortcites
					, "true" // autopunct
					, "hyphen" // babel
					, "true" // hyperref
					, "false" // abbreviate
					));
		}
		this.loadAbbreviations();
		if (this.includeGrammar) {
			this.loadGrammar();
			// add the values for the titles
			if (this.pdfTitle.length() == 0) {
				this.pdfTitle = this.tokens.get(0).getAsString() + " " + this.tokens.get(1).getAsString();
			}
		}
		try {
			String firstChar = this.pdfTitle.substring(0, 1).toLowerCase();
			if (firstChar.matches("[α-ω]")) {
				TextLiturgical dummy = new TextLiturgical("en_us_system", "a", "b");
				dummy.setValue(this.pdfTitle);
				this.pdfTitle = dummy.getNnp().toUpperCase();
			}
		} catch (Exception e) {
			ErrorUtils.report(logger, e, this.pdfTitle);
		}
		String title = this.pdfTitle.replaceAll("\\\\", " ");
		this.texFileSb.append(OslwUtils.getOslwTitleResources(
				idManager.getLibrary()
				, this.pdfTitle
				, title
				, title
				, ""
				)
		);

		this.loadNotes();
		
		this.hasBibliography = this.biblioJsonStrings.size() > 0;
		if (this.hasBibliography) {
			this.hasBibliography = true;
			this.texFileSb.append("\\addbibresource{");
			this.texFileSb.append(this.fileId);
			this.texFileSb.append(".bib}%\n\n");
			this.bibtexFileSb.append(this.createBibFileContent());
			this.texFileSb.append("\n\\DeclareFieldFormat*{citetitle}{\\textnormal{\\textit{#1}}}\n");
			this.texFileSb.append("\\newcommand{\\ltCitePrimary}[1]{\n");
			this.texFileSb.append("\\citeauthor*{#1}. \\citetitle*{#1}.%\n");
			this.texFileSb.append("}\n");
		}
//		this.texFileSb.append("\\OnehalfSpacing");
		this.texFileSb.append("\\linespread{1.0}%\n");
		this.texFileSb.append("\\setaftersecskip{0}");
		this.texFileSb.append("\\begin{document}%\n");
		this.texFileSb.append("\\parskip=6pt");
		this.texFileSb.append(this.getTitlePage());
		if (this.createToc) {
			this.texFileSb.append(this.getTableOfContents());
		}
		this.texFileSb.append("\n");
		if (this.includeAdviceNotes && this.author.contains("Colburn")) {
			this.texFileSb.append(this.getPreface());
		}
		this.texFileSb.append("\n");
		this.texFileSb.append("\\mainmatter%\n");
		this.texFileSb.append("\\nopartblankpage");
		this.texFileSb.append("\\selectlanguage{english}\n");

		// set the title for the pdf 
	    this.texFileSb.append("\\ltChapter{pdf}{title}\n"); 
	    
	    // provide the topic and key we are using
		StringBuffer ages = new StringBuffer();
		ages.append("\\begin{center}\n");
		ages.append("\\tiny{");
		ages.append("\nAGES Topic\\textasciitilde Key: ");
		ages.append(idManager.getTopic());
		ages.append("\\textasciitilde ");
		ages.append(idManager.getKey());
		ages.append("}\n");
		String description = idManager.getTopicDescription();
		if (description.length() > 0) {
			ages.append("\n\n\\color{red}");
			ages.append(description);
			ages.append("\\color{black}\n");
		}
		ages.append("\\end{center}\n");
		
		// process the content
		this.texFileSb.append(this.getVersionsAsLatex(ages.toString()));
		this.texFileSb.append("\\vfill");
		this.texFileSb.append(this.getNotesAsLatex());
		this.texFileSb.append("\\vfill");
		if (this.includeGrammar) {
			this.texFileSb.append("\n\\section{Grammar}");
			this.texFileSb.append(this.getInterlinearAsLatex());
			this.texFileSb.append("\\vfill");
			this.texFileSb.append(this.getDependencyDiagramAsLatex());
			this.texFileSb.append("\\vfill");
		}
		this.texFileSb.append(this.getAbbreviationsAsLatex());
		
		if (this.hasBibliography) {
			this.texFileSb.append("\n\\section{Bibliography}\n\n");
			this.texFileSb.append("\\selectlanguage{english}\n");
			if (this.hasBibliographyList) {
				this.texFileSb.append("\\small{\\textbf{By Abbreviation}}\n\n");				
				this.texFileSb.append("\\printbiblist[heading=bibempty]{shorttitle}\n\n");
			}
			if (this.hasBibliographyPrimary || this.hasBibliographySecondary || this.hasBibliographyTertiary) {
				if (this.hasBibliographyPrimary) {
					this.texFileSb.append("\\small{\\textbf{By Author (Primary Sources)}}\n\n");
					this.texFileSb.append("\\printbibliography[heading=bibempty,keyword=primary]\n");
				}
				if (this.hasBibliographySecondary) {
					this.texFileSb.append("\\small{\\textbf{By Author (Secondary Sources)}}\n\n");
					this.texFileSb.append("\\printbibliography[heading=bibempty,keyword=secondary]\n");
				}
				if (this.hasBibliographyTertiary) {
					this.texFileSb.append("\\small{\\textbf{By Author (Tertiary Sources)}}\n\n");
					this.texFileSb.append("\\printbibliography[heading=bibempty,keyword=tertiary]\n");
				}
			} else {
				this.texFileSb.append("\\small{\\textbf{By Author}}\n\n");
				this.texFileSb.append("\\printbibliography[heading=bibempty]\n");
			}
		}
		if (this.includePersonalNotes) {
			this.texFileSb.append(this.getUserNotesAsLatex());
		}
		// close out the generation
		this.texFileSb.append(
				"\n\n\\tiny\\textit{Generated ");
		this.texFileSb.append(ZonedDateTime.now(ZoneOffset.UTC));
		this.texFileSb.append(" (Universal Time)  using the OCMC Online Liturgical Workstation at ");
		this.texFileSb.append("\\url{https://olw.ocmc.org}");
		this.texFileSb.append(". Glory to God! Δόξα σοι, ὁ Θεὸς ἡμῶν· δόξα σοι! }%\n");
//		this.texFileSb.append(" (Universal Time)  using liturgical software from the Orthodox Christian Mission Center (OCMC), St. Augustine, FL, USA. Glory to God! Δόξα σοι, ὁ Θεὸς ἡμῶν· δόξα σοι! }%\n");
		this.texFileSb.append("\\vfill");
		this.texFileSb.append("\\end{document}%\n");
	}
	
	public String getUserNotesAsLatex() {
		StringBuffer sb = new StringBuffer();
//		sb.append("\n\\vfill%\n");
//		sb.append("\\pagebreak%\n");
		this.texFileSb.append("\n\\section{Your Personal Notes}\n\n");
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
		sb.append("\n\\subsection{Interlinear Text}\n");
		sb.append("This section provides information about the grammar of words (that is, the morphology) \\footnote{You can read about Greek grammar at \\url{https://ancientgreek.pressbooks.com}.}. The Greek words appear in the same order as they do in the source text.\n");
		sb.append(this.nodesToInterlinear());
		sb.append("\n\\sectionline\n");
		return sb.toString();
	}

	public String getDependencyDiagramAsLatex() {
		StringBuffer sb = new StringBuffer();
		sb.append("\n\\subsection{Dependency Diagram}\n");
		sb.append("A dependency diagram shows the syntax of the text. \\textit{Syntax} means \\textit{the grammatical relationship between words}.  It shows the relationships based on dependency grammar\\footnote{See \\url{http://universaldependencies.org}}. The order of each Greek word in the diagram is based on the word it depends on\\footnote{\\textit{depends on} means \\textit{is governed by or subordinate to}.}. It appears indented and after the word it depends on. The first word in the diagram is the root, that is, the starting point of the dependencies.");
		sb.append("\\newline");
		sb.append("{\\footnotesize	");
		sb.append(this.processNode(null, new StringBuffer(),0));
		sb.append("}\n\\sectionline\n");
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
			sb.append("\\color{burgundy}");
			sb.append(i+1);
			sb.append("[\\color{blue}\\textbf{");
			sb.append(node.getToken());
			sb.append("}/\\color{burgundy}");
			sb.append(gloss);
			sb.append("/");
			sb.append(node.getGrammar());
			sb.append("/\\color{blue}");
			sb.append(node.getLemma());
			sb.append("\\color{black}]\n");
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

	private String getBiblatexPackage(
			String bibstyle
			, String citestyle
			, String sorting
			, String sortcites
			, String autopunct
			, String babel
			, String hyperref
			, String abbreviate
			
			) {
		StringBuffer sb = new StringBuffer();
		sb.append("\\usepackage[bibstyle=");
		sb.append(bibstyle);
		sb.append(",citestyle=");
		sb.append(citestyle);
		sb.append(",sorting=");
		sb.append(sorting);
		sb.append(",sortcites=");
		sb.append(sortcites);
		sb.append(",autopunct=");
		sb.append(autopunct);
		sb.append(",babel=");
		sb.append(babel);
		sb.append(",hyperref=");
		sb.append(hyperref);
		sb.append(",abbreviate=");
		sb.append(abbreviate);
		sb.append(",backref=true,bibencoding=utf8, backend=biber]{biblatex}%\n");
		sb.append("\\defbibheading{bibempty}{}%\n");
		return sb.toString();
	}
	
	private String getBiblatexSblPackage(
			) {
		StringBuffer sb = new StringBuffer();
		sb.append("\\usepackage{biblatex-sbl}%\n");
		sb.append("\\usepackage[style=sbl]{biblatex}%\n");
		return sb.toString();
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
			, int spaces
			) {
		String key = "";
	    if (node == null) {
	        key = "Root";
	     } else {
	    	 sb.append("\\newline");
	    	 sb.append("\\color{white}");
	    	 sb.append("$\\vert$");
	    	 for (int i=0; i < spaces; i++) {
	    		 if (i == spaces - 2) {
		    		 sb.append("\\color{black}$\\vert$");
	    		 } else {
		    		 sb.append("--");
	    		 }
	    	 }
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
				this.processNode(child, sb, spaces+2);
			}
		}
	    if (node != null) {
//		    sb.append("\\end{minipage}\n\n");
//		    sb.append("\\prevdepth\n\n");
//		    sb.append("\\end{mdframed}\n");
	    }
		return sb;
	}
	
	public void loadAbbreviations() {
		for (DEPENDENCY_LABELS_PERSEUS v : DEPENDENCY_LABELS_PERSEUS.values()) {
			abbr.put(v.keyname, v.description);
		}
		for (UD_DEPENDENCY_LABELS v : UD_DEPENDENCY_LABELS.values()) {
			abbr.put(v.keyname, v.fullname);
		}
		for (GRAMMAR_ABBREVIATIONS v : GRAMMAR_ABBREVIATIONS.values()) {
			abbr.put(v.keyname, v.description);
		}
	}
	public String getAbbreviationsAsLatex() {
		StringBuffer sb = new StringBuffer();
		if (this.usedAbbreviations.size() > 0) {
			sb.append("\\section{Abbreviations}\n");
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
		sb.append("\n\\sectionline\n");
		return sb.toString();
	}
	
	public String getNotesAsLatex() {
		StringBuffer sb = new StringBuffer();
		sb.append("\\section{Notes About the Text}");
		sb.append("\\subsection{Summary}\n");
		for (TextualNote note : this.summaryList) {
			sb.append(note.getValue());
			sb.append("\n");
		}
		if (! this.checkBibleList.isEmpty()) {
//			sb.append("\\vfill%\n");
			sb.append("\n\\subsection{References to the Bible}\n\n");
			sb.append("This liturgical text makes the following references to the Bible:\n");
			sb.append(this.processCheckBibleNotes());
		}
		sb.append("\n\n\\subsection{Discussion}\n");
		if (this.combineNotes) {
			sb.append("The notes are sorted based the order of words in the ");
			sb.append(this.alignmentLibraryLatex);
			sb.append(" version of the text. English quotations from the Bible are from the World English Bible British Edition (WEBBE) unless otherwise stated.\n");
//			sb.append("\\vfill%\n");
			sb.append(this.combineNotes());
		} else {
			sb.append(this.processNotesByType());
			if (this.includeAdviceNotes) {
				sb.append("\\subsection{Advice for Translators and Translation Checkers}\n");
				sb.append(this.processAdviceNotes());
			}
		}
		if (this.supplementalList.size() > 0) {
			sb.append("\n\\subsection{Supplemental Notes}\n");
			for (TextualNote note : this.supplementalList) {
				sb.append(
						this.getNoteAsLatexForNonRef(
							note
							, true
						)
				);
//				sb.append(note.getValue());
				sb.append("\n");
			}
		}
		sb.append("\n\\sectionline\n");
		sb.append("\\vfill%\n");

		return sb.toString();
	}
	
	private void addListToBigMap(List<TextualNote> listToAdd) {
		for (TextualNote note : listToAdd) {
			String scope = note.getLiturgicalScope().trim();
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
	
	private Map<Integer, String> getAlignmentList() {
		Map<String, List<String>> indexMap = new TreeMap<String, List<String>>();
		TextLiturgical dummy = new TextLiturgical("en_us_system", "a", "b");
		dummy.setValue(this.alignmentText);
		String nnp = ' ' + dummy.getNnp() + ' ';
		List<String> notFound = new ArrayList<String>();
		for (String scope : this.bigMap.keySet()) {
			scope = scope.trim();
			dummy.setValue(scope);
			String scopeNnp = ' ' + dummy.getNnp() + ' ';
			int i = 0;
			if (nnp.contains(scopeNnp)) {
				while (i > -1) {
					i = nnp.indexOf(scopeNnp, i);
					if (i > -1) {
						List<String> list = new ArrayList<String>();
						if (indexMap.containsKey(scope)) {
							list = indexMap.get(scope);
						}
						if (! list.contains(i)) {
							list.add(Integer.toString(i));
						}
						indexMap.put(scope, list);
						i++;
					}
				}
			} else {
				notFound.add(scope);
			}
		}
		Map<Integer, String> sortedScopes = new TreeMap<Integer,String>();
		Integer index = 0;
		for (Entry<String,List<String>> entry : indexMap.entrySet()) {
			List<String> list = entry.getValue();
			for (String s : list) {
				index = Integer.parseInt(s);
				if (sortedScopes.containsKey(index)) {
					index++;
				}
				sortedScopes.put(index, entry.getKey());
			}
		}
		// add any that we could not align
		Collections.sort(notFound);
		for (String scope : notFound) {
			index++;
			sortedScopes.put(index, scope);
		}
		return sortedScopes;
	}

	private String combineNotes() {
		StringBuffer sb = new StringBuffer();
		// load the big Map
		this.addListToBigMap(this.notesList);
		if (this.includeAdviceNotes) {
			this.addListToBigMap(this.adviceList);
		}
		
		// create the alignment index
		Collection<String> scopeCollection = null;
		if (this.alignmentText.length() > 0) {
			scopeCollection = this.getAlignmentList().values();
		}
		if (scopeCollection == null || scopeCollection.size() == 0) {
			scopeCollection = this.bigMap.keySet();
		}
		
		// process the notes grouped by scope
		for (String bigMapKey : scopeCollection) {
			StringBuffer combo = new StringBuffer();
			Map<NOTE_TYPES, List<TextualNote>> scopeMap = this.bigMap.get(bigMapKey);
			NOTE_TYPES sampleKey = null;
			for (NOTE_TYPES t : scopeMap.keySet()) {
				sampleKey = t;
				break;
			}
			TextualNote sample = scopeMap.get(sampleKey).get(0);
			String scope = sample.getLiturgicalScope().trim();
			String lemma = sample.getLiturgicalLemma();
			combo.append("\n\\noteLexical{");
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
			sb.append("\n");
			sb.append(combo.toString());
		}
		sb.append("\n\\vfill%");
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
			this.noteIds.add(note.getId());
			note.setValueFormatted(this.htmlToLatex(note.getValueFormatted()));
			if (note.getNoteType() == NOTE_TYPES.UNIT) {
				this.summaryList.add(note);
			} else if (note.getNoteType() == NOTE_TYPES.REF_TO_BIBLE) {
				this.checkBibleList.add(note);
			} else 	if (note.getNoteType() == NOTE_TYPES.ADVICE_FOR_TRANSLATION_CHECKERS
					|| note.getNoteType() == NOTE_TYPES.ADVICE_FOR_TRANSLATORS
					) {
					tempAdviceList.add(note);
			} else if (note.getNoteType() == NOTE_TYPES.SUPPLEMENTAL_NOTE) {
				this.supplementalList.add(note);
			} else {
				tempTopicsList.add(note);
			}
		}
		if (this.includeAdviceNotes) {
			this.adviceList = this.sortNotes(tempAdviceList); // sorts using the followsNoteId property
		}
		Collections.sort(tempTopicsList);
		this.notesList = tempTopicsList;
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
				, TextualNote.noteTypeLiturgicalScopeComparator);
		for (TextualNote note : this.notesList) {
			NOTE_TYPES type = note.getNoteType();
			if (currentType != type) {
				sb.append("\n\\subsubsection{");
				sb.append(type.fullname);
				sb.append("}\n");
				currentType = type;
			}
			if (type == NOTE_TYPES.REF_TO_BIBLE) {
//				sb.append(this.getNoteAsLatexForBibleRef(note));
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

	private String processCheckBibleNotes() {
		StringBuffer sb = new StringBuffer();
		Collections.sort(
				this.checkBibleList
				, TextualNote.noteTypeLiturgicalScopeComparator);
		for (TextualNote note : this.checkBibleList) {
				sb.append(this.getNoteAsLatexForBibleRef(note));
		}
		return sb.toString();
	}

	private String processAdviceNotes() {
		StringBuffer sb = new StringBuffer();
		Collections.sort(
				this.adviceList
				, TextualNote.noteLiturgicalScopeComparator);
		// the noteTypeAdHocComparator is used if we use the dependency sort
//		Collections.sort(
//				this.adviceList
//				, TextualNote.noteTypeAdHocComparator);
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
		if (note.value.trim().length() > 0) {
			sb.append("\n\\noteLexicalRefToBibleTitle{");
		} else {
			sb.append("\n\\noteLexicalRefToBible{");
	    }
		sb.append(note.liturgicalScope.trim());
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
		sb.append(note.liturgicalScope.trim());
		sb.append("}{");
		sb.append(note.liturgicalLemma);
		sb.append("}{");
		sb.append(note.noteTitle);
		if (note.value.trim().length() > 0) {
			sb.append("}{");
			sb.append(note.value);
		}
		sb.append("}\n");
		return sb.toString();
	}
	
	private String htmlToLatex(String html) {
		try {
			// process the anchors, which represent an abbreviation or a citation
			// e.g. @
			Document doc = Jsoup.parse(html);
			Elements anchors = doc.select("a");
			for (Element anchor : anchors) {
				String id = anchor.attr("href");
				if (id.startsWith("http")) {
					URL url = new URL(id);
					id = url.getPath().substring(1);
				}
				if (id.contains("~")) {
					String dataValue = anchor.attr("data-value");
					if (dataValue == null || dataValue.length() == 0) {
						try {
							dataValue = anchor.attr("href").split("~")[2];
						} catch (Exception e) {
							// ignore
						}
					}
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
								if (this.biblioLibraryTopic.length() < 1) {
									this.biblioLibraryTopic = idManager.getLibrary() + "~" + idManager.getTopic();
								}
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
					}
					}
					// convert to latex citation
					anchor.tagName("span");
					// TODO: language selection should be automatic in Babel but not working.
					// it is printing και for and, as in Louw και Nida.  So, for now, force it to be English
						try {
							JsonObject o = this.biblioJsonStrings.get(this.biblioLibraryTopic + "~" + dataValue);
							String shortTitle = "";
							try {
								shortTitle = o.get("shorttitle").getAsString();
							} catch (Exception e) {
								ErrorUtils.report(logger, e);
							}
							String citeOverride = o.get("citeoverride").getAsString();
							if (shortTitle.trim().length() > 0) {
								switch (citeOverride) {
								case ("NONE"): {
									anchor.text("\\selectlanguage{english}\\cite{" + dataValue + "}");
									break;
								}
								case ("AUTHOR_TITLE"): {
									anchor.text("\\selectlanguage{english}\\ltCitePrimary{" + dataValue + "}");
									break;
								}
								case ("SHORT_TITLE"): {
									anchor.text("\\selectlanguage{english}\\citetitle{" + dataValue + "}");
									break;
								}
								}
							} else {
								anchor.text("\\selectlanguage{english}\\cite{" + dataValue + "}");
							}
						} catch (Exception e) {
							ErrorUtils.report(logger, e);
							anchor.text("\\selectlanguage{english}\\cite{" + dataValue + "}");
						}
				} else {
					// this is a real macoy anchor and href
					anchor.tagName("span");
					String href = anchor.attr("href");
					String innerText = anchor.text();
					anchor.text(innerText + "\\footnote{\\url{" + href + "}})");
				}
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
		sb.append(note.liturgicalScope.trim());
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
	public String getVersionsAsLatex(
			String ages
			) {
		StringBuffer transSb = new StringBuffer();
		StringBuffer result = new StringBuffer();
		boolean hasScansion = false;
		
		String greekLibrary = "gr_gr_cog";
		String greekLatex = "";
		String greekValue = "";
		String gevLibrary = "en_uk_gev";
		String gevLatex = "";
		String gevValue = "";
		String gevMotLibrary = "en_uk_gemot";
		String gevMotLatex = "";
		String gevMotValue = "";
		String gevSotLibrary = "en_uk_gesot";
		String gevSotLatex = "";
		String gevSotValue = "";
		String libraryLatex = "";
		String tfmLibrary = "en_uk_tfm";
		String dedesLibrary = "en_us_dedes";
		StringBuffer lf  = new StringBuffer();
		greekLatex = lf.toString();

		transSb.append("\\setlength{\\arrayrulewidth}{0.4pt}");
		transSb.append("\\setlength{\\tabcolsep}{18pt}");
		transSb.append("\\renewcommand{\\arraystretch}{1.5}");
		transSb.append("\\begin{tabular}{ |p{3cm}|p{10cm}| }\n\\hline\n");
		for (JsonElement e : this.jsonObject.get("versions").getAsJsonArray()) {
			JsonObject o = e.getAsJsonObject();
			String library = o.get("library").getAsString();
			IdManager idManager = new IdManager(o.get("id").getAsString());
			lf  = new StringBuffer();
			lf.append(idManager.getLibraryLanguage());
			lf.append("\\textunderscore ");
			lf.append(idManager.getLibraryCountry());
			lf.append("\\textunderscore ");
			lf.append(idManager.getLibraryRealm());
			libraryLatex = lf.toString();
			
			String value = o.get("value").getAsString();
			if (value != null && value.trim().length() > 0) {
				if (idManager.getLibrary().equals(this.alignmentLibrary)) {
					this.alignmentText = value; // we will use this later if we are aligning notes
					this.alignmentLibraryLatex = lf.toString();
				}
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
					greekLatex = lf.toString();
				} else if (library.equals(gevLibrary)) {
					gevValue = value;
					gevLatex = lf.toString();
				} else if (library.equals(gevMotLibrary)) {
					gevMotValue = value;
					gevMotLatex = lf.toString();
				} else if (library.equals(gevSotLibrary)) {
					gevSotValue = value;
					gevSotLatex = lf.toString();
				} else if (library.equals(tfmLibrary) || library.equals(dedesLibrary)){
					transSb.append(libraryLatex);
					transSb.append(" & ");
					transSb.append(value);
					transSb.append(" \\\\ ");
					transSb.append("\n\\hline\n");
				} else if (! this.author.contains("Colburn")) {
					transSb.append(libraryLatex);
					transSb.append(" & ");
					transSb.append(value);
					transSb.append(" \\\\ ");
					transSb.append("\n\\hline\n");
				}
				// add the version (library) to the list of those used
				if (library.equals(greekLibrary) || library.equals(gevSotLibrary) || library.equals(gevMotLibrary) || library.equals(gevLibrary) || library.equals(tfmLibrary) || library.equals(dedesLibrary)) {
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
		}
		transSb.append("\n\\hline\n\\end{tabular}\n\n\n");
		if (hasScansion) {
			transSb.append("Note: some liturgical hymns originally used punctuation marks to indicate the boundary of metric feet. They do not have a grammatical role.  They are called \\textit{scansion} symbols. In the modern version of source text or translations you might see asterisks (*) or forward slashes (/) used as scansion symbols.\n");
		}
		if (this.includeAdviceNotes && this.author.contains("Colburn")) {
			transSb.append("\n\\begin{center}\\n\\includegraphics[width=1.0\\textwidth]{system/images/translationContinuumWhite.jpg}\\n\\end{center}\\n");
		}
		result.append("\\section{The Text and Translations}");
		this.appendText("The Text and Translations");
		result.append(ages);
		if (greekValue.length() > 0) {
			result.append("\\subsection{Source Text}\n");
			result.append("If you know Greek, use this as your source text:\n\n");
			this.appendText("Source Text");
			result.append("\\setlength{\\arrayrulewidth}{0.4pt}");
			result.append("\\setlength{\\tabcolsep}{18pt}");
			result.append("\\renewcommand{\\arraystretch}{1.5}");
			result.append("\\begin{tabular}{ |m{3cm}|p{10cm}| }\n\\hline\n");
			result.append(greekLatex);
			result.append(" & ");
			result.append(greekValue);
			this.appendText(greekValue);
			result.append(" \\\\ ");
			result.append("\n");
			result.append("\n\\hline\n\\end{tabular}\n");
			result.append("\\vfill\n");
		}
		if (this.includeAdviceNotes && (gevValue.length() > 0 || gevSotValue.length() > 0 || gevMotValue.length() > 0) ) {
			result.append("\\subsection{Global English Translations}\n");
			result.append("If you do not know Greek, you can use the en\\textunderscore uk\\textunderscore gev as your source text (model):\n");
			result.append("\\setlength{\\arrayrulewidth}{0.4pt}");
			result.append("\\setlength{\\tabcolsep}{18pt}");
			result.append("\\renewcommand{\\arraystretch}{1.5}");
			result.append("\\begin{tabular}{ |p{3cm}|p{10cm}| }\n\\hline\n");
			if (gevSotValue.length() > 0) {
				result.append("Structure Oriented (");
				result.append(gevSotLatex);
				result.append(")");
				result.append(" & ");
				result.append(gevSotValue);
				result.append(" \\\\ ");
				result.append("\n\\hline\n");
			}
			if (gevValue.length() > 0) {
				result.append("Model (");
				result.append(gevLatex);
				result.append(")");
				result.append(" & ");
				result.append(gevValue);
				result.append(" \\\\ ");
				result.append("\n\\hline\n");
			}
			if (gevMotValue.length() > 0) {
				result.append("Meaning Oriented (");
				result.append(gevMotLatex);
				result.append(")");
				result.append(" & ");
				result.append(gevMotValue);
				result.append(" \\\\ ");
				result.append("\n\\hline\n");
			}
			result.append("\n\\hline\n\\end{tabular}\n\n");
			result.append("\\vfill\n");
			result.append("\\subsection{Other Translations}\n");
		} else {
			result.append("\\subsection{Translations}\n");
		}
		result.append(transSb.toString());
		result.append("\\vfill");
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

	public StringBuffer getTextFile() {
		return textFile;
	}

	public void setTextFile(StringBuffer textFile) {
		this.textFile = textFile;
	}


}
