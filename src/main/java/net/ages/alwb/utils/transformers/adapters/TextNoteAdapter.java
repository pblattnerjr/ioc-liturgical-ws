package net.ages.alwb.utils.transformers.adapters;

import org.ocmc.ioc.liturgical.schemas.models.db.docs.notes.TextualNote;
import org.jsoup.nodes.Element;

public class TextNoteAdapter {
	private TextualNote note = null;
	private String balloonAsterisk = "&#10020;";
	private String rightArrow = "&#10143;";
	private String bullet = "&#8226;";
	private JsoupToHtml j2h = new JsoupToHtml("Temp");
	private Element noteDiv = null;
	private boolean combineOnHead = true;
	
	public TextNoteAdapter(
			TextualNote note
			, boolean combineOnHead
			) {
		this.note = note;
		this.combineOnHead = combineOnHead;
		noteDiv = j2h.getDiv();
	}
	
	private String summaryNoteToHtml() {
		StringBuffer sb = new StringBuffer();
		return sb.toString();
	}

	private String genericNoteToHtml() {
		StringBuffer sb = new StringBuffer();
		return sb.toString();
	}

	private String ontologyNoteToHtml() {
		StringBuffer sb = new StringBuffer();
		return sb.toString();
	}
	
	private String bibleRefNoteToHtml() {
		StringBuffer sb = new StringBuffer();
		return sb.toString();
	}

	public String toHtml() {
		StringBuffer sb = new StringBuffer();
		switch (note.noteType) {
		case ADVICE_FOR_TRANSLATION_CHECKERS:
			sb.append(this.genericNoteToHtml());
			break;
		case ADVICE_FOR_TRANSLATORS:
			sb.append(this.genericNoteToHtml());
			break;
		case CHECK_YOUR_BIBLE:
			break;
		case CULTURE:
			sb.append(this.genericNoteToHtml());
			break;
		case GENERAL:
			sb.append(this.genericNoteToHtml());
			break;
		case GEOGRAPHY:
			sb.append(this.genericNoteToHtml());
			break;
		case GRAMMAR:
			sb.append(this.genericNoteToHtml());
			break;
		case HISTORY:
			sb.append(this.genericNoteToHtml());
			break;
		case LEMMA:
			sb.append(this.genericNoteToHtml());
			break;
		case LITURGICAL_USAGE:
			sb.append(this.genericNoteToHtml());
			break;
		case MEANING:
			sb.append(this.genericNoteToHtml());
			break;
		case REF_TO_ANIMAL:
			sb.append(this.ontologyNoteToHtml());
			break;
		case REF_TO_BEING:
			sb.append(this.ontologyNoteToHtml());
			break;
		case REF_TO_BIBLE:
			sb.append(this.bibleRefNoteToHtml());
			break;
		case REF_TO_CONCEPT:
			sb.append(this.ontologyNoteToHtml());
			break;
		case REF_TO_EVENT:
			sb.append(this.ontologyNoteToHtml());
			break;
		case REF_TO_GOD:
			sb.append(this.ontologyNoteToHtml());
			break;
		case REF_TO_GROUP:
			sb.append(this.ontologyNoteToHtml());
			break;
		case REF_TO_HUMAN:
			sb.append(this.ontologyNoteToHtml());
			break;
		case REF_TO_MYSTERY:
			sb.append(this.ontologyNoteToHtml());
			break;
		case REF_TO_OBJECT:
			sb.append(this.ontologyNoteToHtml());
			break;
		case REF_TO_PLACE:
			sb.append(this.ontologyNoteToHtml());
			break;
		case REF_TO_PLANT:
			sb.append(this.ontologyNoteToHtml());
			break;
		case REF_TO_ROLE:
			sb.append(this.ontologyNoteToHtml());
			break;
		case TRANSLATORS_NOTE:
			sb.append(this.genericNoteToHtml());
			break;
		case UNIT:
			sb.append(this.summaryNoteToHtml());
			break;
		case VOCABULARY:
			sb.append(this.genericNoteToHtml());
			break;
		default:
			sb.append(this.genericNoteToHtml());
			break;
		}
		return sb.toString();
	}
	
}
