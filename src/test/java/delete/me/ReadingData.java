package delete.me;

public class ReadingData {
	String book = "";
	String chapter = "";
	String verseFrom = "";
	String verseTo = "";
	
	public ReadingData(String book, String chapter, String verseFrom, String verseTo) {
		this.book = book;
		this.chapter = chapter;
		this.verseFrom = verseFrom;
		this.verseTo = verseTo;
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
