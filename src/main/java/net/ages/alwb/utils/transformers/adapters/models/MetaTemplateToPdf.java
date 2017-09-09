package net.ages.alwb.utils.transformers.adapters.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import ioc.liturgical.ws.constants.Constants;
import net.ages.alwb.utils.core.id.managers.IdManager;

/**
 * Liturgical books and services are provided by AGES Initiatives as
 * html and PDF files, and by OSLW as PDF files.
 * 
 * For both AGES and OSLW, template files are created that specify
 * the order of information and its formatting.  The information is 
 * retrieved using the combination of a domain, topic, and key.
 * 
 * A meta template can be created from the AGES HTML files, or from the OSLO tex files.
 *  
 * Such a meta template is used by this class to create a PDF file.
 *  
 * This class reads a json string encoding the meta Template for a liturgical book or service,
 * generates OSLO files from the metadata,
 * calls Xelatex to generate a PDF file
 * and returns the path to the file.
 * 
 * The generated PDF can have one, two, or three columns.
 * If one column, there is only a left library to be used.
 * If two columns, there is a left and right library to be used.
 * If three columns, there is a left, center, and right library to be used.
 * There are also 'fallback' libraries that can be specified.  That is,
 * if a specified library does not contain the required topic/key, then
 * the fallback library will be searched.
 * 
 * If the language = English, if the fallback is not found, it will default to AGES_ENGLISH,
 * i.e. en_us_dedes.
 * 
 * Otherwise, if the fallback is not found, it will use gr_gr_cog.
 * 
 */

public class MetaTemplateToPdf {
	private static final Logger logger = LoggerFactory.getLogger(MetaTemplateToPdf.class);
	private 	MetaTemplate template  = null;
	private 	String metaTemplateJsonString = "";
	private 	String basePath = "";
	private 	String pathToPdf = "";
	private List<OslwElement> list = new ArrayList<OslwElement>();
	private StringBuffer resFileSb = new StringBuffer();
	public Map<String,String> x = new TreeMap<String,String>();
	
	/**
	 * @param metaTemplateJsonString - a json string containing the metadata of the service
	 * @throws JsonParseException if the metaService is not valid json
	 */
	public MetaTemplateToPdf (
			String metaTemplateJsonString
			)   throws JsonParseException {
		this.metaTemplateJsonString = metaTemplateJsonString;
		Gson gson = new Gson();
		template = gson.fromJson(
				metaTemplateJsonString
				, MetaTemplate.class
				);
		if (template.getLeftLibrary() != null && template.getLeftLibrary().length() > 0) {
			IdManager m = new IdManager();
			m.setDomain(template.getLeftLibrary());
			this.resFileSb.append(m.getOslwSetDomain(IdManager.COLUMNS.Primary));
		}
		if (template.getCenterLibrary() != null && template.getCenterLibrary().length() > 0) {
			IdManager m = new IdManager();
			m.setDomain(template.getCenterLibrary());
			this.resFileSb.append(m.getOslwSetDomain(IdManager.COLUMNS.Center));
		}
		if (template.getRightLibrary() != null && template.getRightLibrary().length() > 0) {
			IdManager m = new IdManager();
			m.setDomain(template.getRightLibrary());
			this.resFileSb.append(m.getOslwSetDomain(IdManager.COLUMNS.Primary));
		}
		System.out.print(this.resFileSb.toString());
		this.valuesToOslw();
		System.out.print(this.resFileSb.toString());
		this.process(template.getTopElement());
		for (OslwElement o : list) {
			if (o.command.length() > 0) {
				if (! x.containsKey(o.command.toString())) {
					x.put(o.command.toString(), o.keys.toString() + " (" + template.url + ")");
				}
			}
			String oslw = o.toString();
			int l = oslw.length();
			if (l > 1 ) {
//				System.out.println(o.toString());
			}
		}
	}
	
	/**
	 * Process the json element to extract the information
	 * and create the latex template file data
	 */
	private void process(TemplateElement e) {
		if (e.getTag().equals("tr")) {
			this.list.add(this.processRow(e.getChildren().get(0)));
		} else {
			for (TemplateElement child : e.children) {
				this.process(child);
			}
		}
	}
	
	private OslwElement processRow(TemplateElement row) {
		OslwElement result  = new OslwElement();
		for (TemplateElement s : row.getChildren()) {
			if (s.getClassName().startsWith("kvp")) {
				result.addCommand(s.getTag());
				result.addKey(dump(s));
			} else {
				result.addCommand(dump(s));
			}
			for (TemplateElement t : s.getChildren()) {
				result.appendAll(processRowElement(t));
			}
		}
		return result;
	}

	private OslwElement processRowElement(TemplateElement e) {
		OslwElement result  = new OslwElement();
		String theDump = "";
		theDump = dump(e);
		if (theDump.startsWith("{")) {
			result.addKey(theDump);
		} else {
			result.addCommand(theDump);
		}
		for (TemplateElement c : e.getChildren()) {
			theDump = dump(c);
			if (theDump.startsWith("{")) {
				result.addKey(theDump);
			} else {
				result.addCommand(theDump + "~");
			}
		}
		return result;
	}

	private String dump(TemplateElement e) {
		StringBuffer sb = new StringBuffer();
		if (e.getClassName().startsWith("kvp")) {
			String [] parts = e.getTopicKey().split("~");
			sb.append("{");
			sb.append(parts[0]);
			sb.append("}{");
			sb.append(parts[1]);
			sb.append("}");
		} else {
			if (! e.getTag().startsWith("span")) {
				sb.append(e.getTag()); 
			}
			if (e.getClassName().length() > 0) {
				sb.append("-"); 
				sb.append(e.getClassName()); 
			}
		}
		return sb.toString();
	}

	public void valuesToOslw() {
		for (Entry<String,String> entry :this.template.getValues().entrySet()) {
			IdManager m = new IdManager(entry.getKey());
			String oslw = m.getOslwResourceForValue(entry.getValue());
			this.resFileSb.append(oslw);
		}
		
	}
	public String getMetaTemplateJsonString() {
		return metaTemplateJsonString;
	}

	public void setMetaTemplateJsonString(String metaTemplateJsonString) {
		this.metaTemplateJsonString = metaTemplateJsonString;
	}


	public String getBasePath() {
		return basePath;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	public String getPathToPdf() {
		return pathToPdf;
	}

	public void setPathToPdf(String pathToPdf) {
		this.pathToPdf = pathToPdf;
	}

	public MetaTemplate getTemplate() {
		return template;
	}

	public void setTemplate(MetaTemplate template) {
		this.template = template;
	}


}
