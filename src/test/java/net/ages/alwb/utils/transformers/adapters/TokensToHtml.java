package net.ages.alwb.utils.transformers.adapters;

import org.junit.Test;

import net.ages.alwb.utils.core.file.AlwbFileUtils;
import net.ages.alwb.utils.core.misc.HtmlUtils;

public class TokensToHtml {

	@Test
	public void test() {
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
		String s = "Βυθοῦ ἀνεκάλυψε πυθμένα, καὶ διὰ ξηρᾶς οἰκείους ἕλκει, ἐν αὐτῷ κατακαλύψας ἀντιπάλους,  ὁ κραταιός, ἐν πολέμοις Κύριος˙ ὅτι δεδόξασται.";
		String innerPanel = HtmlUtils.getPanel("gr_gr_cog~me.m01.d06~meMA.Ode1C1H.text", s, false);
		sb.append(innerPanel);
		sb.append(HtmlUtils.getPanelAsIframe("Perseus", false));
		sb.append(getPanel("Lexigram"));
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
		AlwbFileUtils.writeFile("/Users/mac002/temp/greek.html", sb.toString());
	}
	
	private String getPanel(String id) {
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
		sb.append("\" class=\"panel-collapse collapse\">");
		sb.append("      <div class=\"panel-body\"></div>");
		sb.append("          <iframe width=\"100%\" height=\"100%\" id=\"");
		sb.append(id);
		sb.append("\" name=\"");
		sb.append(id);
		sb.append("\"></iframe>");
		sb.append("      </div>");
		sb.append("    </div>");
		sb.append("  </div>");
		sb.append("</div>");
		return sb.toString();
	}

}
