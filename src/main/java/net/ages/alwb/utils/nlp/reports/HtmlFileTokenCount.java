package net.ages.alwb.utils.nlp.reports;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.ocmc.ioc.liturgical.schemas.id.managers.IdManager;

import net.ages.alwb.utils.nlp.parsers.TextParser;

/**
 * For specified urls, lists the tokens for the Greek text 
 * with frequency counts and total.
 * @author mac002
 *
 */
public class HtmlFileTokenCount {
	double minutesPerToken = 1.5;
	List<File> fileList = null;
	List<String> keys = new ArrayList<String>();
	Map<String,Map<String,List<String>>> keyMap = new TreeMap<String,Map<String,List<String>>>();
	long sentenceCount = 0;
	String path = "";
	Map<String, Map<String, Long>> tokens = new TreeMap<String, Map<String, Long>>();
	Map<String, Long> summary  = new TreeMap<String,Long>();
	public HtmlFileTokenCount(
			String path
			) {
		this.path = path;
		this.load();
	}
	
	public double toHours(Long count) {
		return (minutesPerToken * count) / 60;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		long total = 0;
		for (Entry<String, Map<String,Long>>  entry: this.tokens.entrySet()) {
			long subTotal = 0;
			sb.append(entry.getKey());
			sb.append(":\n");
			for (Entry<String,Long> tokenEntry : entry.getValue().entrySet()) {
				sb.append("\t");
				sb.append(tokenEntry.getKey());
				sb.append("\t\t");
				sb.append(tokenEntry.getValue());
				sb.append("\n");
				total = total + tokenEntry.getValue();
				subTotal = subTotal + tokenEntry.getValue();
			}
			sb.append("\n\nSubtotal: ");
			sb.append(subTotal);
			this.summary.put(entry.getKey(), subTotal);
		}
		sb.append("\n\nTotal: ");
		sb.append(total);
		sb.append("\n\n");
		sb.append("Service, tokens, hours, weeks20, weeks40\n");
		for (Entry<String,Long> entry : this.summary.entrySet()) {
			sb.append(entry.getKey());
			sb.append(",");
			sb.append(entry.getValue());
			double hours = this.toHours(entry.getValue());
			sb.append("," + ((int) Math.round(hours)));
			sb.append(",");
			sb.append((int) Math.round(hours / 20));
			sb.append(",");
			sb.append((int) Math.round(hours / 40));
			sb.append("\n");
		}
		sb.append("\nID count = " + this.keys.size());
		sb.append("\nSentence count = " + this.sentenceCount);
		long mapKeyCount = 0;
		for (String mode : this.keyMap.keySet()) {
			sb.append("\nMode " + mode);
			Map<String,List<String>> dayMap = this.keyMap.get(mode);
			for (String day : dayMap.keySet()) {
				sb.append("\n\tDay " + day);
				List<String> keys = dayMap.get(day);
				sb.append(" Keys = " + keys.size());
				mapKeyCount = mapKeyCount + keys.size();
			}
		}
		sb.append("\nMap ID count = " + mapKeyCount);
		return sb.toString();
	}
	
	public void load() {
		Map<String,Long> map = new TreeMap<String,Long>();
		IdManager idManager = new IdManager();
		///Users/mac002/canBeRemoved/dcs/h/b/oc/m8/d7/gr-en/index.html
		for (File f : org.ocmc.ioc.liturgical.utils.FileUtils.getFilesFromSubdirectories(path, "html")) {
			String fPath = f.getPath();
			if (f.getPath().endsWith("gr-en/index.html")) {
				Document doc;
				try {
					doc = Jsoup.parse(f, "UTF-8", "http://example.com/");
					// get keys
					Elements kvps = doc.select("span.kvp");
					for (Element kvp : kvps) {
						String value = kvp.attr("data-key");
						value = IdManager.dataKeyToId(value);
						if (value.startsWith("gr_gr_cog") && value.endsWith(".text")) {
							idManager = new IdManager(value);
							String [] topicParts = idManager.getTopic().split("\\.");
							String book = topicParts[0];
							String mode = "";
							String day  = "";
							if (book.equals("oc") && topicParts.length > 2) {
								mode = topicParts[1];
								day = topicParts[2];
							} else {
								mode = "other";
								day = idManager.getTopic();
							}
							Map<String,List<String>> dayKeys = new TreeMap<String,List<String>>();
							if (this.keyMap.containsKey(mode)) {
								dayKeys = this.keyMap.get(mode);
							}
							List<String> keyList = new ArrayList<String>();
							if (dayKeys.containsKey(day)) {
								keyList = dayKeys.get(day);
							}
							keyList.add(value);
							dayKeys.put(day, keyList);
							this.keyMap.put(mode, dayKeys);
							if (! this.keys.contains(value)) {
								this.keys.add(value);
							}
						}
					}
					Collections.sort(keys);
					// get tokens
					Elements cells = doc.select("td.leftCell");
					for (Element cell : cells) {
						TextParser parser = new TextParser(cell.text());
						for (String token : parser.getTokens()) {
							token = token.toLowerCase();
							if (token.equals(";") 
									|| token.equals(".")
									|| token.equals("!")
									){
								this.sentenceCount++;
							}
							long count = 1;
							if (map.containsKey(token)) {
								count = map.get(token);
								count++;
							}
							map.put(token, count);
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		this.tokens.put("oc", map);
	}
	public static void main(String[] args) {
		String path = "/Users/mac002/canBeRemoved/dcs/h";
		HtmlFileTokenCount it = new HtmlFileTokenCount(path);
		System.out.println(it.toString());
	}

}
