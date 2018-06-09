package delete.me;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;

public class JsoupToHtml {
	private Element root = Jsoup.parse("<html/>");
	private Element head = null;
	private Element body = null;
	private List<Element> spans = new ArrayList<Element>();
	
	public JsoupToHtml(String title) {
		this.head = this.root.getElementsByTag("head").first();
		this.head.text(title);
		this.body = this.root.getElementsByTag("body").first();
	}
	private Element e(String tag, String value) {
		Element e = new Element(Tag.valueOf(tag), "");
		e.text(value);
		return e;
	}
	
	private void addSpans() {
		this.addDiv(this.spans);
		spans = new ArrayList<Element>();
	}
	
	private void addSpan(Element span) {
		this.spans.add(span);
	}
	
	private void add(String tag, String value) {
		body.appendChild(e(tag,value));
	}
	
	private void h1(String value) {
		this.add("h1", value);
	}
	
	private void h2(String value) {
		this.add("h2", value);
	}
	private void h3(String value) {
		this.add("h3", value);
	}

	private void addDiv(List<Element> list) {
		Element div = this.getDiv();
		for (Element e : list) {
			div.appendChild(e);
		}
		this.body.appendChild(div);
	}
	private Element getDiv() {
		return new Element(Tag.valueOf("div"), "");
	}
	
	private Element span(String value) {
		return e("span",value);
	}
	
	private Element spanRed(String value) {
		Element s = span(value);
		return s.attr("style","color: red;");
	}

	private Element spanBlue(String value) {
		Element s = span(value);
		return s.attr("style","color: blue;");
	}

	private Element spanBurgandy(String value) {
		Element s = span(value);
		return s.attr("style","color: #800020;");
	}

	private Element spanItalics(String value) {
		Element s = span(value);
		return s.attr("style","font-style: italic;");
	}
	
	private Element spanItalicsRed(String value) {
		Element s = span(value);
		return s.attr("style","font-style: italic; color: red;");
	}
	
	private Element spanItalicsBlue(String value) {
		Element s = span(value);
		return s.attr("style","font-style: italic; color: blue;");
	}

	private Element spanItalicsBurgandy(String value) {
		Element s = span(value);
		return s.attr("style","font-style: italic; color: #800020;");
	}

	private Element spanBold(String value) {
		Element s = span(value);
		return s.attr("style","font-weight: bold;");
	}
	
	private Element spanBoldRed(String value) {
		Element s = span(value);
		return s.attr("style","font-weight: bold; color: red;");
	}

	private Element spanBoldBlue(String value) {
		Element s = span(value);
		return s.attr("style","font-weight: bold; color: blue;");
	}

	private Element spanBoldBurgandy(String value) {
		Element s = span(value);
		return s.attr("style","font-weight: bold; color: #800020;");
	}

	public static void main(String[] args) {
		JsoupToHtml jth = new JsoupToHtml("Theophany");
		jth.h1("Theophany, Canon 1, Ode 1, Heirmos");
		jth.h2("Dr. Michael Colburn");
		jth.addSpan(jth.span("This is a div. "));
		jth.addSpan(jth.spanBold("This is bold. "));
		jth.addSpan(jth.spanItalics("This is italics. "));
		jth.addSpan(jth.spanRed("This is red. "));
		jth.addSpan(jth.spanBlue("This is blue. "));
		jth.addSpan(jth.spanBurgandy("This is burgandy. "));
		jth.addSpans();
		System.out.println(jth.root.html());
	}

}
