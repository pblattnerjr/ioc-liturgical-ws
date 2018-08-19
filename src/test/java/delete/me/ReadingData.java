package delete.me;

public class ReadingData {
	String book = "";
	String chapter = "";
	String verseFrom = "";
	String verseTo = "";
	
	public ReadingData(String book, String chapter, String verseFrom, String verseTo) {
		this.book = book;
		this.chapter = this.pC(chapter);
		this.verseFrom = this.pad(verseFrom);
		this.verseTo = this.pad(verseTo);
	}
	
	private String pC(String s) {
		return "C" + String.format ("%03d", Integer.parseInt(s));
	}
	private String pad(String s) {
		return String.format ("%03d", Integer.parseInt(s));
	}
	public String getBook() {
		return book;
	}
	public void setBook(String book) {
		this.book = book;
	}
	public String getChapter() {
		return chapter;
	}
	public void setChapter(String chapter) {
		this.chapter = chapter;
	}
	public String getVerseFrom() {
		return verseFrom;
	}
	public void setVerseFrom(String verseFrom) {
		this.verseFrom = verseFrom;
	}
	public String getVerseTo() {
		return verseTo;
	}
	public void setVerseTo(String verseTo) {
		this.verseTo = verseTo;
	}
}
