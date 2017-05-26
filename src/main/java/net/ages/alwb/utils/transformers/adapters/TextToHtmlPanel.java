package net.ages.alwb.utils.transformers.adapters;

import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.tokenize.Tokenizer;

public class TextToHtmlPanel {
	private String id = "";
	private String text  = "";
	
	public TextToHtmlPanel() {
		this.id = id;
		this.text = text;
	}
	
	public String getHtmlPanel(
			String id
			, String text
			) {
		StringBuffer sb = new StringBuffer();
		Tokenizer tokenizer = SimpleTokenizer.INSTANCE;
        String [] theTokens = tokenizer.tokenize(this.text);
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
		return sb.toString();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
