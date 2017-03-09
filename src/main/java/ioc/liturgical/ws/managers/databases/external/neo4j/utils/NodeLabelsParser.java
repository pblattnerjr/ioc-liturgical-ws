package ioc.liturgical.ws.managers.databases.external.neo4j.utils;

import com.google.gson.annotations.Expose;

import ioc.liturgical.ws.constants.BIBLICAL_BOOKS;
import ioc.liturgical.ws.constants.DOMAINS_BIBLICAL;
import ioc.liturgical.ws.constants.DOMAINS_LITURGICAL;
import ioc.liturgical.ws.constants.LITURGICAL_BOOKS;
import net.ages.alwb.utils.core.datastores.json.models.AbstractModel;

/**
 * TODO: needs more work or to be deleted.
 * I am trying to build the dropdown based on node labels, because it is quicker.
 * But it is difficult to identify label parts.  
 * @author mac002
 *
 */
public class NodeLabelsParser extends AbstractModel {
	@Expose DropdownItem domain = null;
	@Expose DropdownItem book = null;
	@Expose DropdownItem chapter = null;
	String type = "";
	String [] labels;
	int size = 0;

	
	public NodeLabelsParser(String type, String labels) {
		super();
		this.type = type;
		this.labels = labels.split(",");
		this.size = this.labels.length;
		parse();
	}
	
	private void parse() {
		this.domain = get("domain");
		this.book = get("book");
		this.chapter = get("chapter");
	}
	
	private DropdownItem get(String property) {
		for (int i=0; i < size; i++) {
			String label = this.labels[i].trim();
			switch (property) {
			case "domain": {
				if (label.contains("_")) {
					if (this.type.matches("Biblical")) {
						return new DropdownItem(label, DOMAINS_BIBLICAL.get(label));
					} else if (this.type.matches("Liturgical")) {
						return new DropdownItem(label, DOMAINS_LITURGICAL.get(label));
					} else {
						return new DropdownItem(label, label);
					}
				}
				break;
			}
			case "book": {
				switch (this.type) {
				case "Biblical": {
					if (BIBLICAL_BOOKS.containsKey(label)) {
						return new DropdownItem(label, BIBLICAL_BOOKS.get(label));
					}
					break;
				}
				case "Liturgical": {
					if (LITURGICAL_BOOKS.containsKey(label)) {
						return new DropdownItem(label, LITURGICAL_BOOKS.get(label));
					}
					break;
				}
				}
				break;
			}
			case "chapter": {
				switch (this.type) {
				case "Biblical": {
					if (label.toLowerCase().startsWith("c") && Character.isDigit(label.charAt(label.length()-1))) {
						return new DropdownItem(label,label);
					}
					break;
				}
				case "Liturgical": {
					if (this.book != null) {
						if (label.matches("me")) {
							return new DropdownItem(label, this.labels[i+1]);
						}
					}
					break;
				}
				}
				break;
			}
			}
		}
		return null;
	}

	public DropdownItem getDomain() {
		return domain;
	}

	public void setDomain(DropdownItem domain) {
		this.domain = domain;
	}

	public DropdownItem getBook() {
		return book;
	}

	public void setBook(DropdownItem book) {
		this.book = book;
	}

	public DropdownItem getChapter() {
		return chapter;
	}

	public void setChapter(DropdownItem chapter) {
		this.chapter = chapter;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String[] getLabels() {
		return labels;
	}

	public void setLabels(String[] labels) {
		this.labels = labels;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public static void main(String[] args) {
		NodeLabelsParser p = new NodeLabelsParser("Biblical", "Text, gr_gr_ntpt, MAT, Biblical, NT, Gospel, C04");
		System.out.println(p.getDomain().toJsonString());
		System.out.println(p.getBook().toJsonString());
		System.out.println(p.getChapter().toJsonString());
		
		p = new NodeLabelsParser("Liturgical", "en_us_dedes, actors, Text, Liturgical");
		System.out.println(p.getDomain().toJsonString());
		System.out.println(p.getBook().toJsonString());
		System.out.println(p.getChapter().toJsonString());
	}

}
