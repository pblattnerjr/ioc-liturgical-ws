package delete.me;

import java.util.ArrayList;
import java.util.List;

public class PericopeData {
	Integer size = 0;
	List<ReadingData> readingParts = new ArrayList<ReadingData>();
	String pericopeId = "";
	String topicKey = "";
	String Book = "";
	String Citation = "";
	String bk1 = "";
	String c1 = "";
	String vf1 = "";
	String vt1 = "";
	String bk2 = "";
	String c2 = "";
	String vf2 = "";
	String vt2 = "";
	String bk3 = "";
	String c3 = "";
	String vf3 = "";
	String vt3 = "";
	String bk4 = "";
	String c4 = "";
	String vf4 = "";
	String vt4 = "";
	String bk5 = "";
	String c5 = "";
	String vf5 = "";
	String vt5 = "";
	String bk6 = "";
	String c6 = "";
	String vf6 = "";
	String vt6 = "";
	
	public PericopeData(String csv) {
		this.process(csv);
	}
	
	private void process(String csv) {
		String [] parts = csv.split(",");
		StringBuffer sb = new StringBuffer();
		this.size = parts.length;
		this.topicKey = parts[0];
		this.Book = parts[1];
		this.Citation = parts[2];
		for (int i = 3; i < parts.length; i = i  + 4 ) {
			ReadingData d = new ReadingData(parts[i], parts[i+1], parts[i+2], parts[i+3]);
			this.readingParts.add(d);
			if (sb.length() > 0) {
				sb.append("|");
			}
			sb.append(d.book);
			sb.append("~");
			sb.append(d.chapter);
			sb.append(":");
			sb.append(d.verseFrom);
			sb.append("-");
			sb.append(d.verseTo);
		}
		this.pericopeId = sb.toString();
	}
	public String getBook() {
		return Book;
	}
	public void setBook(String book) {
		Book = book;
	}
	public String getCitation() {
		return Citation;
	}
	public void setCitation(String citation) {
		Citation = citation;
	}
	public String getBk1() {
		return bk1;
	}
	public void setBk1(String bk1) {
		this.bk1 = bk1;
	}
	public String getC1() {
		return c1;
	}
	public void setC1(String c1) {
		this.c1 = c1;
	}
	public String getVf1() {
		return vf1;
	}
	public void setVf1(String vf1) {
		this.vf1 = vf1;
	}
	public String getVt1() {
		return vt1;
	}
	public void setVt1(String vt1) {
		this.vt1 = vt1;
	}
	public String getBk2() {
		return bk2;
	}
	public void setBk2(String bk2) {
		this.bk2 = bk2;
	}
	public String getC2() {
		return c2;
	}
	public void setC2(String c2) {
		this.c2 = c2;
	}
	public String getVf2() {
		return vf2;
	}
	public void setVf2(String vf2) {
		this.vf2 = vf2;
	}
	public String getVt2() {
		return vt2;
	}
	public void setVt2(String vt2) {
		this.vt2 = vt2;
	}
	public String getBk3() {
		return bk3;
	}
	public void setBk3(String bk3) {
		this.bk3 = bk3;
	}
	public String getC3() {
		return c3;
	}
	public void setC3(String c3) {
		this.c3 = c3;
	}
	public String getVf3() {
		return vf3;
	}
	public void setVf3(String vf3) {
		this.vf3 = vf3;
	}
	public String getVt3() {
		return vt3;
	}
	public void setVt3(String vt3) {
		this.vt3 = vt3;
	}
	public String getBk4() {
		return bk4;
	}
	public void setBk4(String bk4) {
		this.bk4 = bk4;
	}
	public String getC4() {
		return c4;
	}
	public void setC4(String c4) {
		this.c4 = c4;
	}
	public String getVf4() {
		return vf4;
	}
	public void setVf4(String vf4) {
		this.vf4 = vf4;
	}
	public String getVt4() {
		return vt4;
	}
	public void setVt4(String vt4) {
		this.vt4 = vt4;
	}
	public String getBk5() {
		return bk5;
	}
	public void setBk5(String bk5) {
		this.bk5 = bk5;
	}
	public String getC5() {
		return c5;
	}
	public void setC5(String c5) {
		this.c5 = c5;
	}
	public String getVf5() {
		return vf5;
	}
	public void setVf5(String vf5) {
		this.vf5 = vf5;
	}
	public String getVt5() {
		return vt5;
	}
	public void setVt5(String vt5) {
		this.vt5 = vt5;
	}
	public String getBk6() {
		return bk6;
	}
	public void setBk6(String bk6) {
		this.bk6 = bk6;
	}
	public String getC6() {
		return c6;
	}
	public void setC6(String c6) {
		this.c6 = c6;
	}
	public String getVf6() {
		return vf6;
	}
	public void setVf6(String vf6) {
		this.vf6 = vf6;
	}
	public String getVt6() {
		return vt6;
	}
	public void setVt6(String vt6) {
		this.vt6 = vt6;
	}
	public String getTopicKey() {
		return topicKey;
	}
	public void setTopicKey(String topicKey) {
		this.topicKey = topicKey;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public List<ReadingData> getReadingParts() {
		return readingParts;
	}

	public void setReadingParts(List<ReadingData> readingParts) {
		this.readingParts = readingParts;
	}

	public String getPericopeId() {
		return pericopeId;
	}

	public void setPericopeId(String pericopeId) {
		this.pericopeId = pericopeId;
	}

	public void setSize(Integer size) {
		this.size = size;
	}
}
