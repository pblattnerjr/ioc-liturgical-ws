package net.ages.alwb.utils.core.misc;

import java.io.File;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.tokenize.Tokenizer;

/**
 * HTML constants and helper methods to create HTML blocks
 * @author mac002
 *
 */
public class HtmlUtils {
	public static final String HTMLopen = "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>\n<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\"\n \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">\n<html xmlns=\"http://www.w3.org/1999/xhtml\">";
	public static final String HTMLclose = "\n</html>";
	public static final String HEADopen = "\n<head>";
	public static final String HEADclose = "\n</head>";
	public static final String BODYopen = "\n<body>";
	public static final String BODYclose = " </body>";
	public static final String TABLEopen = "\n<table>";
	public static final String TABLEopenParm = "<table ";
	public static final String TABLEclose = " </table>";
	public static final String Q = "\"";
	public static final String ROWopenParm = "\n<tr ";
	public static final String ROWclose = "\n</tr>";
	public static final String COLopenParm = "\n<td ";
	public static final String COLclose = "</td>";
	public static final String PopenParm = "\n<p ";
	public static final String Pclose = "</p>";
	public static final String CLASS = " class=";
	
	public static String para(String theClass, String theValue) {
		return PopenParm + CLASS + q(theClass) + ">"+ theValue + Pclose;
	}
	
	/**
	 * 
	 * @param title
	 * @param css
	 * @return
	 */
	public static String getHead(String title, String css) {
		String link = "";
		if (css.length() > 0) {
			link = "<link href=\""
					+ css
					+ "\" rel=\"stylesheet\" type=\"text/css\" />";
		}
			
		return HEADopen
				+ "<title>"
				+ title
				+ "</title>"
				+ link
				+ "<meta charset=\"utf-8\" />"
				+ HEADclose;
	}
	
	public static Document getDoc(File file) {
		Document result = null;
		try {
			result = Jsoup.parse(file, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String getAlwbDescription(File file) {
		Document doc = getDoc(file);
		try {
			return doc.title();
		} catch (Exception e) {
			return file.getPath();
		}
	}
	public static String td(String name, String id, String text) {
		return wrap("td",name,id,text);
	}
	
	public static String tr(String name, String id, String text) {
		return wrap("tr",name,id,text);
	}
	
	public static String wrap(String tag, String name, String id, String text) {
		return
				"\n<" 
				+ tag
				+ " "
				+ "class = \""
				+ name
				+ "\" "
				+ "id = \""
				+ id
				+ "\">"
				+ text
				+ "\n</"
				+ tag
				+ ">";
	}
	public static String wrappedAnchor(String tag, String name, String href, String text) {
		return
				"\n<" 
				+ tag
				+ " "
				+ "class = \""
				+ name
				+ "\">"
				+ "\n<a href=\""
				+ href
				+ "\">"
				+ text
				+"\n</a>"
				+ "\n</"
				+ tag
				+ ">"
				;
		
	}
	public static String getIndexPage(String heading, String message, String css) {
		StringBuffer sb = new StringBuffer();
		sb.append(HTMLopen);
		sb.append(getHead(heading,css));
		sb.append(BODYopen);
		sb.append("<h1>"+heading+"</h1>");
		sb.append("<p>"+message+"</p>");
		sb.append(BODYclose);
		sb.append(HTMLclose);
		return sb.toString();
	}
	
	public static String getTableOpen(
			int border, 
			int cellspacing, 
			int cellpadding, 
			String theClass) {
		return TABLEopenParm 
				+ " border=" + q(border)
				+ " cellspacing=" + q(cellspacing)
				+ " cellpadding=" + q(cellpadding)
				+ " class=" + Q + theClass
				+ TABLEclose
				;
	}
	
	public static String getRowOpen(String theClass) {
		return ROWopenParm + "class=" + q(theClass) + ">";
	}
	
	public static String getColOpen(String theClass) {
		return COLopenParm + " class=" + q(theClass) + ">";
	}
	
	public static String getCol(String theClass, String text) {
		return getColOpen(theClass) + text.trim() + COLclose;
	}
	
	public static String q(int v) {
		return Q + v + Q;
	}
	public static String q(String v) {
		return Q + v + Q;
	}
	
	
	public static String anchor(String pClassName, String href, String text) {
		return
				"<p class=\""
				+ pClassName 
				+ "\">"
				+  "<a class=\"" 
				+ pClassName 
				+ "\" href=\"" 
				+ href 
				+ "\">"
				+ text 
				+ "</a>"
				+ "</p>";
	}

	public static String createHtmlFileContents(
			List<String> lines
			) {
		StringBuffer sb = new StringBuffer();
		sb.append("<html>");
		sb.append("<head>");
		sb.append("<title>Greek Word Study Tool</title>");
		sb.append("<meta charset=\"utf-8\">");
		sb.append("<script src=\"https://code.jquery.com/jquery-3.2.1.min.js\" integrity=\"sha256-hwg4gsxgFZhOsEEamdOYGBf13FyQuiTwlAQgxVSNgt4=\" crossorigin=\"anonymous\"></script>");
		sb.append("<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css\" integrity=\"sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u\" crossorigin=\"anonymous\">");
		sb.append("<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css\" integrity=\"sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp\" crossorigin=\"anonymous\">");
		sb.append("<script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js\" integrity=\"sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa\" crossorigin=\"anonymous\"></script>");
		sb.append("</head>");
		sb.append("<body>");
		for (String line : lines) {
			sb.append(getHtmlPanel("", line, false));
		}
		sb.append(getPanelAsIframe("Perseus", false));
		sb.append(getPanelAsIframe("Lexigram", false));
		sb.append("</body>");
		sb.append("\n<script>");
		sb.append("\n\tvar loadPerseus = (token) => {");
		sb.append("\n\t\tvar frame = document.getElementById(\"Perseus\");");
		sb.append("\n\t\tframe.src = \"http://www.perseus.tufts.edu/hopper/morph?l=\" + token + \"&la=greek\";");
		sb.append("\n}");
		sb.append("\n\tvar loadLexigram = (token) => {");
		sb.append("\n\t\tvar frame = document.getElementById(\"Lexigram\");");
		sb.append("\n\t\tframe.src = \"http://www.lexigram.gr/lex/arch/\" + token;");
		sb.append("\n}");
		sb.append("\n\tvar loadFrames = (token) => {");
		sb.append("\n\t\tloadPerseus(token);");
		sb.append("\n\t\tloadLexigram(token);");
		sb.append("\n}");
		sb.append("\n</script>");
		sb.append("</html>");
		return sb.toString();
	}
	
	public static String getHtmlPanel(
			String id
			, String text
			, boolean startOpen
			) {
		StringBuffer sb = new StringBuffer();
		Tokenizer tokenizer = SimpleTokenizer.INSTANCE;
        String [] theTokens = tokenizer.tokenize(text);
		sb.append("<div id=\"text\" class=\"text\">");
        for (String token : theTokens) {
        	sb.append("<span onClick=\"loadFrames('");
        	sb.append(token.trim());
        	sb.append("');\" >");
        	sb.append(token.trim());
        	sb.append("&nbsp;");
        	sb.append("</span>");
        }
		sb.append("</div>");
		sb.append(getPanel(id, sb.toString(), startOpen));
		return sb.toString();
	}

	public static String getPanelAsIframe(
			String id
			, boolean startOpen
			) {
		return getPanel(id, getIframe(id), startOpen);
	}
	/**
	 * 
	 * @param id - used for the panel name visible to the user as well as for the id and class of the panel
	 * @param content - the contents of the panel.  Should either be text, or valid HTML
	 * @startOpen - if true, the panel will show its contents as soon as it loads.
	 * @return
	 */
	public static String getPanel(
			String id
			, String content
			, boolean startOpen
			) {
		StringBuffer sb = new StringBuffer();
		sb.append("<div class=\"panel-group\">");
		sb.append("  <div class=\"panel panel-default\">");
		sb.append("    <div class=\"panel-heading\">");
		sb.append("      <h4 class=\"panel-title\">");
		sb.append("        <a data-toggle=\"collapse\" href=\"#collapse");
		sb.append(id);
		sb.append("\">");
		sb.append(id);
		sb.append("</a>");
		sb.append("      </h4>");
		sb.append("    </div>");
		sb.append("    <div id=\"collapse");
		sb.append(id);
		sb.append("\" class=\"panel-collapse collapse");
		if (startOpen) {
			sb.append(" in");
		}
		sb.append(")\">");
		sb.append("      <div class=\"panel-body\"></div>");
		sb.append(content);
		sb.append("      </div>");
		sb.append("    </div>");
		sb.append("  </div>");
		sb.append("</div>");
		return sb.toString();
	}
	
	public static String getIframe(String id) {
		StringBuffer sb = new StringBuffer();
		sb.append("<iframe width=\"100%\" height=\"100%\" id=\"");
		sb.append(id);
		sb.append("\" name=\"");
		sb.append(id);
		sb.append("\"></iframe>");
		return sb.toString();		
	}

}
