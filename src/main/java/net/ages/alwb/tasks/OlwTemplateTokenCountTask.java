package net.ages.alwb.tasks;

import java.text.NumberFormat;
import java.util.Map;
import java.util.TreeMap;

import org.ocmc.ioc.liturgical.schemas.models.LDOM.AgesIndexTableData;
import org.ocmc.ioc.liturgical.schemas.models.LDOM.AgesIndexTableRowData;
import org.ocmc.ioc.liturgical.schemas.models.LDOM.LDOM;
import org.ocmc.ioc.liturgical.schemas.models.ws.response.ResultJsonObjectArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ioc.liturgical.ws.managers.databases.external.neo4j.ExternalDbManager;
import net.ages.alwb.utils.nlp.parsers.TextParser;
import net.ages.alwb.utils.transformers.adapters.AgesHtmlToLDOM;
import net.ages.alwb.utils.transformers.adapters.AgesWebsiteIndexToReactTableData;

/**
 * Runs a task (separate thread) to count the number of tokens in a specified
 * liturgical book
 * .
 * @author mac002
 *
 */
public class OlwTemplateTokenCountTask implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(OlwTemplateTokenCountTask.class);

	private ExternalDbManager manager = null;
	private String requestor = "";
	private String out = "";
	private Map<String,Long> tokenMap = new TreeMap<String,Long>();
	private StringBuffer sb = new StringBuffer();

	
	public OlwTemplateTokenCountTask (
			ExternalDbManager manager
			, String requestor
			, String out
			) {
		this.manager = manager;
		this.requestor = requestor;
		this.out = out;
		
	}
	
	private void tokenizeLdom(LDOM template, AgesIndexTableRowData row) {
		Map<String,Long> map = new TreeMap<String,Long>();
		long sentenceCount = 0;
		long tokenCount = 0;
		long textCount = 0;
		long noEnglishCount = 0;
		long textTokenCount = 0;
		int didCount = 1;
		int tkCount = template.getTopicKeys().size();
		for (String topicKey : template.getTopicKeys()) {
			System.out.println(didCount + ":" + tkCount);
			didCount++;
			String value = template.getValues().get("gr_gr_cog~" + topicKey);
//			noEnglishCount = noEnglishCount + this.missingEnglish(topicKey);
			if (topicKey.endsWith(".text")) {
				textCount++;
			}
			if (value != null && value.length() > 0) {
				TextParser parser = new TextParser(value);
				for (String token : parser.getTokens()) {
					token = token.toLowerCase();
					if (token.equals(";") 
							|| token.equals(".")
							|| token.equals("!")
							){
						sentenceCount++;
					}
					long count = 1;
					if (map.containsKey(token)) {
						count = map.get(token);
						count++;
					}
					map.put(token, count);
					tokenCount++;
					if (topicKey.endsWith(".text")) {
						textTokenCount++;
					}
					// update total token map
					count = 1;
					if (tokenMap.containsKey(token)) {
						count = tokenMap.get(token);
						count++;
					}
					tokenMap.put(token, count);
					tokenCount++;
				}
			}
		}
		row.setKeyCount(template.getIds().size());
		row.setSentenceCount(sentenceCount);
		row.setTokenCount(tokenCount);
		row.setTextCount(textCount);
		row.setTextTokenCount(textTokenCount);
		row.setNoEnglishCount(noEnglishCount);
		row.setUniqueTokenCount(map.keySet().size());
	}
	
	private long missingEnglish(String topicKey) {
		long result = 1;
		String query = "match (n:Liturgical) where n.id starts with 'en' and n.id ends with '" 
				+ topicKey 
				+ "' and size(n.value) > 0 return n.id, n.value";
		ResultJsonObjectArray queryResult = this.manager.getForQuery(query, false, false);
		if (queryResult.valueCount > 0) {
			result = 0;
		}
		return result;
	}
	
	private String f(long l) {
		return NumberFormat.getIntegerInstance().format(l);
	}

	@Override
	public void run() {
		AgesWebsiteIndexToReactTableData index = new AgesWebsiteIndexToReactTableData();
		try {
			AgesIndexTableData data = index.toReactTableDataOctoechos();
			long keyCount = 0;
			long sentenceCount = 0;
			long tokenCount = 0;
			long textCount = 0;
			long textTokenCount = 0;
			
			String lastMode = "";
			for (AgesIndexTableRowData row : data.getTableData()) {
				String dow = row.getDayOfWeek();
				// add mode subtotal counters and emit them at end of each mode
				
				if (! row.getDayOfWeek().contains("Orth")) {
					String mode = row.getType().substring(10, 16);
					String day = row.getType().substring(20, row.getType().length());
					if (! mode.equals(lastMode)) {
						if (lastMode.length() > 0) {
							sb.append("\n\\end{tabular}\n\\end{center}");
						}
						sb.append("\n\\begin{center}");
						sb.append(mode);
						sb.append("\n\\begin{tabular}{ c | c | c | c | c | c | c}");
						sb.append("\nDay & Keys & Sentences & Tokens (all) & Hymns & Tokens (Hymns) & No Eng\\\\ [0.5ex]"
								);
						lastMode = mode;
					}
					AgesHtmlToLDOM ages = new AgesHtmlToLDOM(
							row.getUrl()
							, "gr_gr_cog"
							, "en_us_dedes"
							, ""
							, "gr_gr_ages"
							, "en_us_ages"
							, ""
							, false
							, this.manager
							);
					LDOM template = ages.toLDOM();
					this.tokenizeLdom(template, row);
					sb.append("\n"
							+ day 
							+ " & " 
									+ this.f(row.getKeyCount()) 
							+	" & "
							+ this.f(row.getSentenceCount())
							+	" & "
							+ this.f(row.getTokenCount())
							+	" & "
							+ this.f(row.getTextCount())
							+	" & "
							+ this.f(row.getTextTokenCount())
							+	" & "
							+ this.f(row.getNoEnglishCount())
							+ "\\\\ [0.5ex]"
					);
					keyCount = keyCount + row.getKeyCount();
					sentenceCount = sentenceCount + row.getSentenceCount();
					tokenCount = tokenCount + row.getTokenCount();
					textCount = textCount + row.getTextCount();
					textTokenCount = textTokenCount + row.getTextTokenCount();
				}
			}
			sb.append("\n\\end{tabular}\n\\end{center}");
			sb.append("\nTotals"
					+ "\nk=" 
					+ this.f(keyCount) 
			+	"|s="
			+ this.f(sentenceCount)
			+	"|t="
			+ this.f(tokenCount)
			+	"|text="
			+ this.f(textCount)
			+	"|tt="
			+ this.f(textTokenCount)
			+	"|ut="
			+ this.f(this.tokenMap.size())
			);
			System.out.println(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}