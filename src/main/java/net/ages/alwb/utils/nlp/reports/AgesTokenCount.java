package net.ages.alwb.utils.nlp.reports;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import net.ages.alwb.utils.nlp.fetchers.Ox3kUtils;
import net.ages.alwb.utils.nlp.parsers.TextParser;

/**
 * For specified urls, lists the tokens for the Greek text 
 * with frequency counts and total.
 * @author mac002
 *
 */
public class AgesTokenCount {
	double minutesPerToken = 1.5;
	Map<String, String> urls = new TreeMap<String,String>();
	Map<String, Map<String, Long>> tokens = new TreeMap<String, Map<String, Long>>();
	Map<String, Long> summary  = new TreeMap<String,Long>();
	public AgesTokenCount(
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
			Document doc = Ox3kUtils.getDoc(entry.getValue());
			Elements cells = doc.select("td.leftCell");
			Map<String,Long> map = new TreeMap<String,Long>();
			if (this.tokens.containsKey(entry.getKey())) {
				map = this.tokens.get(entry.getKey());
			} 
			for (Element cell : cells) {
				TextParser parser = new TextParser(cell.text());
				for (String token : parser.getTokens()) {
					token = token.toLowerCase();
					long count = 1;
					if (map.containsKey(token)) {
						count = map.get(token);
						count++;
					}
					map.put(token, count);
				}
			}
			this.tokens.put(entry.getKey(), map);
		}
	}
	public static void main(String[] args) {
		Map<String, String> urls = new TreeMap<String,String>();
		urls.put("Baptism", "http://www.agesinitiatives.com/dcs/public/dcs/h/b/baptism/gr-en/index.html");
		urls.put("Funeral", "http://www.agesinitiatives.com/dcs/public/dcs/h/b/funeral/gr-en/index.html");
		urls.put("Memorial", "http://www.agesinitiatives.com/dcs/public/dcs/h/b/memorial/gr-en/index.html");
		urls.put("Unction", "http://www.agesinitiatives.com/dcs/public/dcs/h/b/unction/gr-en/index.html");
		urls.put("Great Blessing of Waters", "http://www.agesinitiatives.com/dcs/public/dcs/h/b/greatwaterblessing/gr-en/index.html");
		urls.put("Small Blessing of Waters", "http://www.agesinitiatives.com/dcs/public/dcs/h/b/smallwaterblessing/gr-en/index.html");
		urls.put("Wedding", "http://www.agesinitiatives.com/dcs/public/dcs/h/b/wedding/gr-en/index.html");
		urls.put("Matins - Ordinary", "http://www.agesinitiatives.com/dcs/public/dcs/h/c/matinsordinary/gr-en/index.html");
		urls.put("Matins - Ordinary - Paschal", "http://www.agesinitiatives.com/dcs/public/dcs/h/c/matinsordinary_paschal/gr-en/index.html");
		urls.put("Matins - Ordinary - Ascension", "http://www.agesinitiatives.com/dcs/public/dcs/h/c/matinsordinary_ascension/gr-en/index.html");
		urls.put("Service of Preparation for Holy Communion", "http://www.agesinitiatives.com/dcs/public/dcs/h/b/ho/s21/gr-en/index.html");
		urls.put("Paraklesis - Theotokos - Small", "http://www.agesinitiatives.com/dcs/public/dcs/h/b/ho/s23/gr-en/index.html");
		urls.put("Paraklesis - Theotokos - Great", "http://www.agesinitiatives.com/dcs/public/dcs/h/b/ho/s24/gr-en/index.html");
		urls.put("Divine Liturgy - St. John C - 2018/7/1", "http://www.agesinitiatives.com/dcs/public/dcs/h/s/2018/07/01/li8/gr-en/index.html");
		urls.put("Divine Liturgy - St. Basil", "https://liml.org/static/bk.liturgy.basil.html");
		urls.put("Vespers - 2018/7/1", "http://www.agesinitiatives.com/dcs/public/dcs/h/s/2018/07/01/ve/gr-en/index.html");
		AgesTokenCount it = new AgesTokenCount(urls);
		System.out.println(it.toString());
	}

}
