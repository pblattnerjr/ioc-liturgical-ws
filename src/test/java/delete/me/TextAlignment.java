package delete.me;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.ocmc.ioc.liturgical.schemas.models.db.docs.ontology.TextLiturgical;

public class TextAlignment {

	public static void main(String[] args) {
		TextLiturgical text = new TextLiturgical("en_us_gesots", "x", "y");
		text.setValue("[He] uncovered [the] bottom of [the] deep, and leads [his] own through dry land, [he who] covered in it [those] opposing, the Lord, powerful in wars: for he has gained honour for himself.");
		String nnp = " " + text.getNnp() + " ";
		List<String> scopes = new ArrayList<String>();
		scopes.add("[his] own");
		scopes.add("for He has gained honour for Himself");
		scopes.add("[those] opposing");
		scopes.add("the deep ");
		scopes.add("Lord");
		scopes.add("and");
		scopes.add("through dry land");
		scopes.add("leads");
		scopes.add("[He] uncovered");
		scopes.add("of [the] deep");
		scopes.add("[he who] covered");
		Map<String, List<String>> indexMap = new TreeMap<String, List<String>>();
		TextLiturgical dummy = new TextLiturgical("en_us_system","a","b");
		
		for (String scope : scopes) {
			dummy.setValue(scope.trim());
			String scopeNnp = " " + dummy.getNnp() + " ";
			int i = 0;
			while (i > -1) {
				i = nnp.indexOf(scopeNnp, i);
				if (i > -1) {
					List<String> list = new ArrayList<String>();
					if (indexMap.containsKey(scopeNnp)) {
						list = indexMap.get(scopeNnp);
					}
					list.add(Integer.toString(i));
					indexMap.put(scopeNnp, list);
					i++;
				}
			}
		}
		Map<Integer, String> sortedScopes = new TreeMap<Integer,String>();
		StringBuffer sb = new StringBuffer();
		for (Entry<String,List<String>> entry : indexMap.entrySet()) {
			List<String> list = entry.getValue();
			for (String s : list) {
				sortedScopes.put(Integer.parseInt(s), entry.getKey());
			}
		}
		for (Entry<Integer,String> entry :sortedScopes.entrySet()) {
			sb.append("\n");
			sb.append(entry.getKey());
			sb.append("\t");
			sb.append(entry.getValue());
			sb.append(" ");
		}
		System.out.println(sb.toString());
	}

}
