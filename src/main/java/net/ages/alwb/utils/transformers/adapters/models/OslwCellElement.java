package net.ages.alwb.utils.transformers.adapters.models;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

import ioc.liturgical.ws.constants.Constants;
import net.ages.alwb.utils.core.datastores.json.models.AbstractModel;
import net.ages.alwb.utils.core.id.managers.IdManager;
import switches.AgesHtmlToOslwSwitch;

public class OslwCellElement  extends AbstractModel {
	@Expose public String cellClassName = "";
	@Expose public List<String> commands = new ArrayList<String>();
	@Expose public StringBuffer keys = new StringBuffer();
	@Expose public StringBuffer rids = new StringBuffer();
	@Expose public String versionDesignationId = "";
	@Expose public List<KvpCellElement> kvpCellElements = new ArrayList<KvpCellElement>();
	
	public OslwCellElement() {
		super();
		super.setPrettyPrint(true);
	}
	public void addCommand(String c) {
		commands.add(c);
	}
	
	public void addKey(String k) {
		IdManager id = new IdManager(k);
		if (id.isLibraryDomain()) {
			this.keys.append(id.getOslwTopicKey());
		} else {
			this.keys.append(k);
		}
		if (! k.contains("{version.designation}")) {
			if (this.rids.length() > 0) {
				this.rids.append(" ");
			}
			this.rids.append("\\itRid" + k);
		}
	}
	
	public void addKvpCellElement (KvpCellElement e) {
		this.kvpCellElements.add(e);
	}
	public void appendAll(OslwCellElement a) {
		for (String command : a.getCommands()) {
			this.addCommand(command);
		}
		for (KvpCellElement e : a.getKvpCellElements()) {
			this.addKvpCellElement(e);;
		}
		this.addKey(a.keys.toString());
		if (this.versionDesignationId.length() == 0 && a.getVersionDesignationId().length() > 0) {
			this.setVersionDesignationId(a.getVersionDesignationId());
		}
	}
	
	public String toString() {
		StringBuffer result = new StringBuffer();
		if (this.commands.size () > 0) {
			result.append(this.commandsToString());
			result.append("~");
		}
		result.append(this.keys.toString());
		return result.toString();
	}
	
	public String toOslwRedirectResources(
			int redirectCounter
			, int maxIds
			, String library
			) {
		if (this.keys.toString().startsWith("{dismissals}{dis00}")) {
			System.out.print("");
		}
		StringBuffer result = new StringBuffer();
		
		int j = this.kvpCellElements.size(); 
		if (j > 4) {
			j = 3;
		}
		for (int i=0; i < j; i++) {
			IdManager idManager = new IdManager(
					library 
					+ Constants.ID_DELIMITER 
					+ "redirect" 
					+ Constants.ID_DELIMITER 
					+ "r" 
					+ redirectCounter
					+ i
			);
			IdManager idFromIds = new IdManager(this.kvpCellElements.get(i).getDataKey());
			if (idManager.getLibrary().equals(idFromIds.getLibrary())) {
				result.append(
						idFromIds.getOslwRedirectRid(
								idManager.getTopic()
								, idManager.getKey()
								)
						);
			} else {
				result.append(
						idManager.getOslwResourceForValue(idFromIds.getOslwSid())
				);
			}
		}
		int k = this.kvpCellElements.size(); 
		if (k > 4) {
			IdManager idManager = new IdManager(
					library 
					+ Constants.ID_DELIMITER 
					+ "redirect" 
					+ Constants.ID_DELIMITER 
					+ "r" 
					+ redirectCounter
					+ j
			);
			StringBuffer rids = new StringBuffer();
			for (int i=j; i < k; i++) {
				IdManager ridId = new IdManager(this.kvpCellElements.get(i).dataKey);
				String parentClass = this.kvpCellElements.get(i).parentClassName;
				if (rids.length() > 0) {
					rids.append(" ");
				}
				String format= "";
				switch (parentClass) {
				case ("italics"): {
					format = "\\ltTextBlackItalics";
					break;
				}
				case ("boldred"): {
					format = "\\ltTextRedBold";
					break;
				}
				case ("red"): {
					format = "\\ltTextRed";
					break;
				}
				default: {
					break;
				}
				}
				if (format.length() > 0) {
					rids.append(format);
				}
				rids.append(ridId.getOslwRid());
				if (format.length() > 0) {
					rids.append(" \\ltTextBlack");
				}
			}
			result.append(idManager.getOslwResourceForValue(rids.toString()));
		} else {
			for (int i=j; i < maxIds; i++) {
				IdManager idManager = new IdManager(
						library 
						+ Constants.ID_DELIMITER 
						+ "redirect" 
						+ Constants.ID_DELIMITER 
						+ "r" 
						+ redirectCounter
						+ i
				);
				result.append(idManager.getOslwResourceForValue(""));
			}
		}
		return result.toString();
	}
	
	public String commandsToString() {
		StringBuffer result = new StringBuffer();
		for (String command: commands) {
			result.append(command);
		}
		return result.toString();
	}


	public String toOslw() {
		StringBuffer result = new StringBuffer();
		if (this.commands.size () > 0 && this.keys.length() > 0) {
			String oslw = AgesHtmlToOslwSwitch.getOslw(
					this.commandsToString()
			);
			result.append(oslw + this.keys.toString() + "\n");
		} else {
			// do nothing
		}
		return result.toString();
	}

	public String toOslwRedirect(
			int redirectCounter
			, int idCount
			, boolean hasDesignation
			) {
		StringBuffer result = new StringBuffer();
		int counter = idCount;
		if (counter > 4) {
			counter = 4;
		}
		if (this.commands.size() > 0 && keys.length() > 0) {
			String oslw = AgesHtmlToOslwSwitch.getOslw(
			this.commandsToString()
			+ (hasDesignation ? "-designation" : ""));
			result.append(oslw);
			for (int i = 0; i < counter ; i++) {
				result.append("{redirect}{r" + redirectCounter + i + "}");
			}
		} else {
			// do nothing
		}
		result.append("\n");
		return result.toString();
	}

	public List<String> getCommands() {
		return commands;
	}

	public void setCommands(List<String> commands) {
		this.commands = commands;
	}

	public StringBuffer getKeys() {
		return keys;
	}

	public void setKeys(StringBuffer keys) {
		this.keys = keys;
	}


	public String getCellClassName() {
		return cellClassName;
	}

	public void setCellClassName(String cellClassName) {
		this.cellClassName = cellClassName;
	}

	public StringBuffer getRids() {
		return rids;
	}
	public void setRids(StringBuffer rids) {
		this.rids = rids;
	}
	public String getVersionDesignationId() {
		return versionDesignationId;
	}
	public void setVersionDesignationId(String versionDesignationId) {
		this.versionDesignationId = versionDesignationId;
	}
	public List<KvpCellElement> getKvpCellElements() {
		return kvpCellElements;
	}
	public void setKvpCellElements(List<KvpCellElement> kvpCellElements) {
		this.kvpCellElements = kvpCellElements;
	}

}
