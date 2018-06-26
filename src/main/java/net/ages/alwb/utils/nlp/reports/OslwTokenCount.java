package net.ages.alwb.utils.nlp.reports;

import java.io.File;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.ocmc.ioc.liturgical.utils.FileUtils;


/**
 * For specified urls, lists the tokens for the Greek text 
 * with frequency counts and total.
 * @author mac002
 *
 */
public class OslwTokenCount {
	double minutesPerToken = 1.5;
	Map<String, String> urls = new TreeMap<String,String>();
	Map<String, Map<String, Long>> tokens = new TreeMap<String, Map<String, Long>>();
	Map<String, Long> summary  = new TreeMap<String,Long>();
	public OslwTokenCount(
			Map<String, String> urls
			) {
		this.urls = urls;
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
		return sb.toString();
	}
	
	public void load() {
		for (Entry<String,String> entry : this.urls.entrySet()) {
			long count = 0;
			for (String line : FileUtils.linesFromFile(new File(entry.getValue()))) {
				if (line.matches("^[0-9]\\[.*")) {
//					System.out.println(line);
					count++;
				}
			}
			System.out.println(entry.getKey() + ": " + count);
		}
	}
	public static void main(String[] args) {
		Map<String, String> urls = new TreeMap<String,String>();
		urls.put("Divine Liturgy St. John Chrysostom", "/Users/mac002/Git/oslw-repositories/oslw.2016.004.liturgy.interlinear/liturgystjohn/liturgystjohn.interlinear.tex");
		urls.put("Orthos", "/Users/mac002/Git/oslw-repositories/oslw.2016.004.liturgy.interlinear/liturgystjohn/matinsordinary.interlinear.tex");
		OslwTokenCount it = new OslwTokenCount(urls);
		System.out.println(it.toString());
	}

}
