package net.ages.alwb.utils.transformers.adapters;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import net.ages.alwb.utils.core.id.managers.IdManager;
import net.ages.alwb.utils.transformers.adapters.models.LDOM;

public class MetaTemplateToInterlinear {
	private static final Logger logger = LoggerFactory.getLogger(MetaTemplateToInterlinear.class);
	private 	LDOM template  = null;
	Map<String,Map<String,String>> map = new TreeMap<String,Map<String,String>>();
	StringBuffer tex = new StringBuffer();
	
	public MetaTemplateToInterlinear (
			String metaTemplateJsonString
			)   throws JsonParseException {
		Gson gson = new Gson();
		template = gson.fromJson(
				metaTemplateJsonString
				, LDOM.class
		);
		this.loadMap();
		this.generateTex();
	}
	
	/**
	 * Create a map from the MetaTemplate.
	 * The keys will be topic~key, and the values will be
	 * maps.  
	 * 
	 * Each value that is a map has the full ID as its key,
	 * and the text value as its value.
	 */
	private void loadMap() {
		for (Entry<String, String> entry : template.getValues().entrySet()) {
			IdManager m = new IdManager(entry.getKey());
			Map<String,String> values = new TreeMap<String,String>();
			if (this.map.containsKey(m.getTopicKey())) {
				values = this.map.get(m.getTopicKey());
			}
			values.put(entry.getKey(), entry.getValue());
			this.map.put(m.getTopicKey(), values);
			System.out.println(map.size());
		}
	}
	
	/**
	 * Sets the tex StringBuffer to the lines needed by Xelatex for Expex
	 * interlinear text.
	 */
	private void generateTex() {
		for (String topicKey : template.getTopicKeys()) {
			Map<String,String> value = map.get(topicKey);
			String id = "";
			String s = "";
			List<String> translations = new ArrayList<String>();
			for (Entry<String,String> valueEntry : value.entrySet()) {
				if (valueEntry.getKey().startsWith("gr_gr_cog")) {
					id = valueEntry.getKey();
					s = valueEntry.getValue();
				} else {
					String transId = valueEntry.getKey();
					String transValue = valueEntry.getValue();
					if (transId.length() > 0 && transValue.length() > 0) {
						IdManager transIdManager = new IdManager(transId);
						translations.add(transValue + " (" + transIdManager.getOslwLibrary() + ")");
					}
				}
			}
			if (id.length() > 0 && s.length() > 0) {
				TextToLatexExpexInterlinear t = new TextToLatexExpexInterlinear(
						id
						, s
						, translations
						, false
						, false
						);
				tex.append(t.convert(false,false,false, false));
			} else {
				System.out.print("");
			}
		}
//		for (Entry<String,Map<String,String>> entry : map.entrySet()) {
//			String id = "";
//			String s = "";
//			List<String> translations = new ArrayList<String>();
//			for (Entry<String,String> valueEntry : entry.getValue().entrySet()) {
//				if (valueEntry.getKey().startsWith("gr_gr_cog")) {
//					id = valueEntry.getKey();
//					s = valueEntry.getValue();
//				} else {
//					String transId = valueEntry.getKey();
//					String transValue = valueEntry.getValue();
//					if (transId.length() > 0 && transValue.length() > 0) {
//						IdManager transIdManager = new IdManager(transId);
//						translations.add(transValue + " (" + transIdManager.getLibrary() + ")");
//					}
//				}
//			}
//			if (id.length() > 0 && s.length() > 0) {
//				TextToLatexExpexInterlinear t = new TextToLatexExpexInterlinear(
//						id
//						, s
//						, translations
//						, false
//						, false
//						);
//				tex.append(t.convert(false,false,false, false));
//			} else {
//				System.out.print("");
//			}
//		}
	}

	public Map<String, Map<String, String>> getMap() {
		return this.map;
	}

	public void setMap(Map<String, Map<String, String>> map) {
		this.map = map;
	}

	public StringBuffer getTex() {
		return tex;
	}

	public void setTex(StringBuffer tex) {
		this.tex = tex;
	}

}
