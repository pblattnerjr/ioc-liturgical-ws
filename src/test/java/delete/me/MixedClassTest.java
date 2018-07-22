package delete.me;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import switches.AgesHtmlToOslwSwitch;

public class MixedClassTest {
	
	
	private List<String> getUrls(String base) {
		List<String> list = new ArrayList<String>();
		Document doc = null;
		Connection c = null;
		try {
			c = Jsoup.connect(base + "servicesindex.html");
			doc = c.timeout(60*1000).maxBodySize(0).get();
			for (Element tr : doc.select("a.index-day-link")) {
				String href = tr.attr("href");
				for (String fileUrl : this.getFileUrls(base + "/" + href)) {
					list.add(base + fileUrl);
				}
			}
			c = Jsoup.connect(base + "booksindex.html");
			doc = c.timeout(60*1000).maxBodySize(0).get();
			for (Element li : doc.select("li")) {
				if (li.hasAttr("dcslink")) {
					String href = li.attr("dcslink");
					if (href.contains("dcs/h")) {
						String fileUrl = base + href.substring(17);
						list.add(fileUrl);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	private List<String> getFileUrls(String base) {
		List<String> list = new ArrayList<String>();
		Document doc = null;
		Connection c = null;
		try {
			c = Jsoup.connect(base);
			doc = c.timeout(60*1000).maxBodySize(0).get();
			for (Element tr : doc.select("a.index-file-link")) {
				String href = tr.attr("href");
				if (href.startsWith("h")) {
					list.add(href);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	private String getCase(String value) {
		StringBuffer sb = new StringBuffer();
		sb.append("\tcase \"");
		sb.append(value);
		sb.append("\": {\n");
		sb.append("\t\tresult = \"Undefined\";\n");
		sb.append("\t\tbreak;\n\t}\n");
		return sb.toString();
	}
	private void doIt(String base) {
		Document doc = null;
		Connection c = null;
		Map<String,String> map = new TreeMap<String,String>();
		StringBuffer titles = new StringBuffer();
		for (String url : this.getUrls(base)) {
			System.out.println(url);
			try {
				c = Jsoup.connect(url);
				doc = c.timeout(60*1000).maxBodySize(0).get();
				System.out.println(doc.select("title").text() + " - " + url);
				titles.append(doc.select("title").text() + " - " + url + "\n");
				for (Element e : doc.select("td >  p")) {
					StringBuffer names = new StringBuffer();
					names.append("p-");
					names.append(e.className());
					names.append(this.getChildClassName(e));
					String classNames = names.toString().trim();
					classNames = classNames.replaceAll("\\-\\-", "-");
					if (classNames.startsWith("p--")) {
						classNames = "p-" + classNames.substring(3);
					} 
					if (! map.containsKey(classNames)) {
						if (classNames.equals("p-") || classNames.contains("dummy")) {
						} else {
							map.put(classNames, classNames);
						}
					}
				}
			} catch (Exception d) {
				d.printStackTrace();
			}
		}
		List<String> unknown = new ArrayList<String>();
		
		for (String name : map.keySet()) {
			String oslw = AgesHtmlToOslwSwitch.getOslw(name);
			if (oslw.endsWith("Unknown")) {
				unknown.add(name);
			} else {
				System.out.println(name + oslw);
			}
		}
		for (String u : unknown) {
			System.out.println(this.getCase(u));
		}
		org.ocmc.ioc.liturgical.utils.FileUtils.writeFile("titlesAndUrls.txt", titles.toString());
	}
	
	private String getChildClassName(
			Element ele
			) {
		StringBuffer sb = new StringBuffer();
		for (Element e : ele.children()) {
			if (e.className().equals("kvp") || e.className().equals("key")) {
				if (e.parent().className().equals("mixed")) {
					sb.append("-text");
				}
			} else {
				sb.append("-" + e.className());
			}
			sb.append(this.getChildClassName(e));
		}
		return sb.toString();
	}
	public static void main(String[] args) {
		String url = "http://www.agesinitiatives.com/dcs/public/dcs/";
		MixedClassTest t = new MixedClassTest();
		t.doIt(url);
	}

}