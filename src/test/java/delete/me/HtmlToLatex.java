package delete.me;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

public class HtmlToLatex {

	private static String processV1(Document doc) {
		StringBuffer sb = new StringBuffer();

		// We will search nodes in a breadth-first way
		Queue<Node> nodes = new ArrayDeque<>();

		nodes.addAll(doc.childNodes());
		

		while (!nodes.isEmpty()) {
		    Node n = nodes.remove();

		    if (n instanceof TextNode && ((TextNode) n).text().trim().length() > 0) {
		    	String tag = n.parent().nodeName();
		    	String text = ((TextNode) n).text().trim();
		    	switch (tag) {
		    	case ("p") : {
		    		sb.append(text);
		    		break;
		    	}
		    	case ("strong"): {
		    		sb.append("\\textbf{");
		    		sb.append(text);
		    		sb.append("}");
		    		break;
		    	}
		    	case ("em"): {
		    		sb.append("\\textit{");
		    		sb.append(text);
		    		sb.append("}");
		    		break;
		    	}
		    	case ("ins"): {
		    		sb.append("\\underline{");
		    		sb.append(text);
		    		sb.append("}");
		    		break;
		    	}
		    	default: {
		    		sb.append(text);
		    	}
		    	}
		    } else {
		        nodes.addAll(n.childNodes());
		    }
		}
		System.out.println(sb.toString());
		return sb.toString();
	}
	
	
	
	public static void main(String[] args) {
		try {
			StringBuffer sb = new StringBuffer();
			String html = "<p>This is <strong>bold</strong>.  This is <em>italics</em>.  This is <ins>underline</ins>.&nbsp;</p>";
//			String html = "<h1>Heading 1</h1><h2>Heading 2</h2><blockquote>Blockquote</blockquote><pre>Code</pre><p>Normal</p><p>This is <strong>bold</strong>.  This is <em>italics</em>.  This is <ins>underline</ins>.&nbsp;</p><p>This is <span style=\"font-size: 30px;\">30 point</span><span style=\"font-size: 8px;\"> and this is 8 pt.</span></p><p></p><p><span style=\"font-size: 14px;\">This is an unordered list:</span></p><ul><li><span style=\"font-size: 14px;\">apple</span></li><li><span style=\"font-size: 14px;\">orange</span></li></ul><ol><li><span style=\"font-size: 14px;\">Item one</span></li><li><span style=\"font-size: 14px;\">Item two</span></li></ol><p><span style=\"font-size: 14px;\">This is a url </span><a href=\"https://olw.ocmc.org\" target=\"_self\"><span style=\"font-size: 14px;\">OLW OCMC</span></a><span style=\"font-size: 14px;\"> </span></p><p></p><p></p>";
//			String html = "<h1>Heading 1</h1><h2>Heading 2</h2>";
			Document doc = Jsoup.parse(html);
			html = html.replaceAll("<p>", "");
			html = html.replaceAll("</p>", "\n");
			html = html.replaceAll("<em>", "\\\\textit{");
			html = html.replaceAll("</em>", "}");
			html = html.replaceAll("<strong>", "\\\\textbf{");
			html = html.replaceAll("</strong>", "}");
			html = html.replaceAll("<ins>", "\\\\underline{");
			html = html.replaceAll("</ins>", "}");
			html = html.replaceAll("&nbsp;", " ");
			System.out.println(html);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
