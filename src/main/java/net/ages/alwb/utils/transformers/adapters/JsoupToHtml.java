package net.ages.alwb.utils.transformers.adapters;

import java.util.ArrayList;
import java.util.List;

import org.ocmc.ioc.liturgical.utils.FileUtils;
import org.eclipse.jgit.util.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;

/**
 * Methods to create an HTML file.
 * You can then use pandoc to convert the HTML to other formats:
 * pandoc -s test.html -o test.rtf
 * Note that pandoc loses formatting.  It will preserve tables though.
 * So the best option for people is to save the html, open in Word,
 * then save it as either rtf or docx.

 * @author mac002
 *
 */
public class JsoupToHtml {
	private Element root = Jsoup.parse("<html/>");
	private Element head = null;
	private Element body = null;
	private Element content = null;
	private List<Element> spans = new ArrayList<Element>();
	private List<Element> cells = new ArrayList<Element>();
	private List<Element> rows = new ArrayList<Element>();
	private Element table = null;
	
	private String sLeft = "text-align:left;";
	private String sCenter = "text-align:center;";
	private String sRight = "text-align:right;";
	private String sBorder = "border: 1px solid black; padding: 10px;";

	private String orthoCross = "&#9768;";
	private String balloonAsterisk = "&#10020;";
	private String rightArrow = "&#10143;";
	private String bullet = "&#8226;";

	private String cBody = "olwBody";
	private String sBody = "border: 1px solid red;";

	private String cContent = "olwContent";
	private String sContent = "margin: 5px; background-color: #FBF0D9; padding: 10px;";

	private String cTable = "olwTable";
	private String sTable = "margin-top: 15px; margin-left: auto; margin-right: auto; border: 1px solid black;";
	
	private String cTh = "olwTh";
	
	private String cTr = "olwTr";
	
	private String cTd = "olwTd";
	
	private String cH1 = "olwH1";
	private String sH1 = "text-align:center;";
	
	private String cH2 = "olwH2";
	private String sH2 = "text-align:center;";
	
	private String cH3 = "olwH3";
	private String sH3 = "text-align:center;";
	
	private String cSpan = "olwSpan";
	
	
	public JsoupToHtml(String title) {
		this.head = this.root.getElementsByTag("head").first();
		this.head.appendChild(this.e("title", title));
		this.head.appendChild(this.getStyleCss());
		this.body = this.root.getElementsByTag("body").first();
		this.body.addClass("olwBody");
		this.content = this.e("div");
		this.content.addClass(this.cContent);
		this.body.appendChild(content);
		
	}
	
	private String getCss(String className, String style) {
		StringBuffer sb = new StringBuffer();
		sb.append(".");
		sb.append(className);
		sb.append(" {");
		sb.append(style);
		sb.append("} ");
		return sb.toString();
	}

	private Element getStyleCss() {
		Element css = new Element(Tag.valueOf("style"), "");
		StringBuffer sb = new StringBuffer();
		sb.append(this.getCss(this.cBody, this.sBody));
		sb.append(this.getCss(this.cContent, this.sContent));
		sb.append(this.getCss(this.cH1, this.sH1));
		sb.append(this.getCss(this.cH2, this.sH2));
		sb.append(this.getCss(this.cH3, this.sH3));
		sb.append(this.getCss(this.cTable, this.sTable));
		css.text(sb.toString());
		return css;
	}

	public Element e(String tag, String value) {
		Element e = new Element(Tag.valueOf(tag), "");
		e.text(value);
		e.addClass("olw" + StringUtils.capitalize(tag));
		return e;
	}
	
	public Element e(String tag) {
		Element e = new Element(Tag.valueOf(tag), "");
		e.addClass("olw" + StringUtils.capitalize(tag));
		return e;
	}
	
	public void addSpans() {
		this.addDiv(this.spans);
		spans = new ArrayList<Element>();
	}
	
	public void addSpan(Element span) {
		this.spans.add(span);
	}
	
	public void addRows() {
		Element table = this.getTable();
		table.addClass(this.cTable);
		for (Element e : this.rows) {
			table.appendChild(e);
		}
		this.content.appendChild(table);
		this.rows = new ArrayList<Element>();
	}
	 
	public void addRow() {
		this.addRow(this.sCenter);
	}
	
	public void addRow(String align) {
		Element row = this.getTr();
		for (Element e : this.cells) {
			Element td = this.getTd();
			td.attr("style", align + this.sBorder);
			td.appendChild(e);
			row.appendChild(td);
		}
		this.rows.add(row);
		this.cells = new ArrayList<Element>();
	}

	public void addHeadingRow(String align) {
		Element row = this.getTr();
		for (Element e : this.cells) {
			Element th = this.getTh();
			th.attr("align", align + this.sBorder);
			th.appendChild(e);
			row.appendChild(th);
		}
		this.rows.add(row);
		this.cells = new ArrayList<Element>();
	}

	public void addCell(Element e) {
		this.cells.add(e);
	}

	public void add(String tag, String value) {
		content.appendChild(e(tag,value));
	}
	
	public void add(String tag, String value, String className) {
		Element e = e(tag, value);
		e.addClass(className);
		content.appendChild(e);
	}
	
	public void h1(String value) {
		this.add("h1", value, this.cH1);
	}
	
	public void h2(String value) {
		this.add("h2", value, this.cH2);
	}
	public void h3(String value) {
		this.add("h3", value, this.cH3);
	}

	public void addDiv(List<Element> list) {
		Element div = this.getDiv();
		for (Element e : list) {
			div.appendChild(e);
		}
		this.content.appendChild(div);
	}
	
	public Element getTable() {
		return this.e("table");
	}
	
	public Element getTr() {
		return this.e("tr");
	}
	
	public Element getTh() {
		return this.e("th");
	}

	public Element getTd() {
		return this.e("td");
	}

	public Element getDiv() {
		return this.e("div");
	}
	
	public Element s(String value) {
		return e("span",value);
	}
	
	public Element sr(String value) {
		Element s = s(value);
		return s.attr("style","color: red;");
	}

	public Element sbl(String value) {
		Element s = s(value);
		return s.attr("style","color: blue;");
	}

	public Element sbr(String value) {
		Element s = s(value);
		return s.attr("style","color: #800020;");
	}

	public Element si(String value) {
		Element s = s(value);
		return s.attr("style","font-style: italic; color: black;");
	}
	
	public Element sird(String value) {
		Element s = s(value);
		return s.attr("style","font-style: italic; color: red;");
	}
	
	public Element sibl(String value) {
		Element s = s(value);
		return s.attr("style","font-style: italic; color: blue;");
	}

	public Element sibr(String value) {
		Element s = s(value);
		return s.attr("style","font-style: italic; color: #800020;");
	}

	public Element sb(String value) {
		Element s = s(value);
		return s.attr("style","font-weight: bold;");
	}
	
	public Element sbrd(String value) {
		Element s = s(value);
		return s.attr("style","font-weight: bold; color: red;");
	}

	public Element sbbl(String value) {
		Element s = s(value);
		return s.attr("style","font-weight: bold; color: blue;");
	}

	public Element sbbr(String value) {
		Element s = s(value);
		return s.attr("style","font-weight: bold; color: #800020;");
	}

	public void addGevTable(String sotTxt, String gevTxt, String motTxt) {
		String sot = "Structure Oriented";
		String sotId = "(en_uk_gesot)";
		String gev = "Model";
		String gevId = "(en_uk_gev)";
		String mot = "Meaning Oriented";
		String motId = "(en_uk_gemot)";

		// create table header
		this.addCell(this.sb(sot));
		this.addCell(this.sb(gev));
		this.addCell(this.sb(mot));
		this.addHeadingRow(this.sCenter);

		this.addCell(this.sb(sotId));
		this.addCell(this.sb(gevId));
		this.addCell(this.sb(motId));
		this.addHeadingRow(this.sCenter);

		this.addCell(this.s(sotTxt));
		this.addCell(this.s(gevTxt));
		this.addCell(this.s(motTxt));
		this.addRow(this.sLeft);
		this.addRows();
	}
	public static void main(String[] args) {
		String sotTxt = "[He] uncovered [the] bottom of [the] deep, and pulls [his] own across dry land, [he who] covered in it [those] opposing, the Lord, powerful in wars: for he has gained honour for himself.";
		String gevTxt = "The Lord, who is powerful in wars, uncovered the bottom of the deep sea, and led His people across its dry land, but there He covered their enemies with its waters. For He has gained honour for Himself!";
		String motTxt = "The Lord, who is like a great soldier in a battle, exposed the bottom of the deep sea, and he led his own people across the dry ground at the bottom of the sea; but there He covered their enemies with its water! Sing to the Lord, because He has gained honour for Himself!";
		
		JsoupToHtml jth = new JsoupToHtml("Theophany");
		jth.h1("Theophany, Canon 1, Ode 1, Heirmos");
		jth.h2("Dr. Michael Colburn");
		// create a div with spans
		jth.addSpan(jth.s("This is ordinary. "));
		jth.addSpan(jth.sb("This is bold. "));
		jth.addSpan(jth.si("This is italics. "));
		jth.addSpan(jth.sr("This is red. "));
		jth.addSpan(jth.sbl("This is blue. "));
		jth.addSpan(jth.sbr("This is burgandy. "));
		jth.addSpans();
		

		jth.addGevTable(sotTxt, gevTxt, motTxt);
		
		String path = "/volumes/ssd2/canBeRemoved/test.html";
		FileUtils.writeFile(path, jth.root.html());
		System.out.println(jth.root.html());
	}

}