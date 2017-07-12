package net.ages.alwb.utils.core.id.managers;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;

import ioc.liturgical.ws.constants.Constants;
import net.ages.alwb.utils.core.error.handling.ErrorUtils;
import net.ages.alwb.utils.core.file.AlwbFileUtils;
import net.ages.alwb.utils.core.misc.AlwbGeneralUtils;

/**
 * Manages multipart IDs, where parts are delimited by the pipe character |
 * There can be any number of parts.
 * For convenience, there are constructors for
 * 4 parts
 * 3 parts
 * 2 parts
 * 
 * There is no need for a single part id.
 * You probably don't need more than four parts to a key.
 * 
 * @author mac002
 *
 */
public class IdManager {
	private static final Logger logger = LoggerFactory.getLogger(IdManager.class);

	private List<String> idParts = new ArrayList<String>();
	private List<String> libraryParts = new ArrayList<String>();
	private List<String> topicParts = new ArrayList<String>();
	private List<String> keyParts = new ArrayList<String>();
	private String libraryLanguage = null;
	private String libraryCountry = null;
	private String libraryRealm = null;
	private boolean libraryIsDomain = false;
	private String delimiter = Constants.ID_DELIMITER;
	private String splitter = Constants.ID_SPLITTER;
	private int topicIndex = 1;
	private int keyIndex = 2;
	
	/**
	 * This constructor expects the id to have parts delimited by Constants.ID_DELIMTER
	 * and it should be a simple ID.  That is, there should not be other IDs embedded
	 * into the ID.  If there are, use the constructor that allows you to indicate
	 * where the topic and key parts start.
	 * @param id
	 */
	public IdManager(String id) {
		try {
			String[] parts = id.split(splitter);
			for (String part : parts) {
					idParts.add(part);
			}
			if (parts.length == 3) {
				this.libraryParts.add(parts[0]);
				this.topicParts.add(parts[1]);
				this.keyParts.add(parts[2]);
				this.libraryIsDomain = true;
				this.setDomainParts();
			}
		} catch (Exception e) {
			ErrorUtils.report(logger, e);
		}
	}

	/**
	 * In the case of a library, topic, or key that embeds another delimited string (e.g. another ID)
	 * this provides a means to indicate where each of the three parts starts.
	 * 
	 * If you use this constructor, then you can subsequently call
	 * getLibrary(), getTopic(), or getKey()
	 * 
	 * @param id
	 * @param topicIndex
	 * @param keyIndex
	 */
	public IdManager(String id, int topicIndex, int keyIndex) {
		this.topicIndex = topicIndex;
		this.keyIndex = keyIndex;
		try {
			String[] parts = id.split(splitter);
			for (String part : parts) {
					idParts.add(part);
			}
			for (int i = 0; i < topicIndex; i++) {
				this.libraryParts.add(idParts.get(i));
			}
			for (int i = topicIndex; i < keyIndex; i++) {
				this.topicParts.add(idParts.get(i));
			}
			for (int i = keyIndex; i < idParts.size(); i++) {
				this.keyParts.add(idParts.get(i));
			}
		} catch (Exception e) {
			ErrorUtils.report(logger, e);
		}
	}

	/**
	 * Constructs an ID from the parts of a String array
	 * @param parts
	 */
	public IdManager(String [] parts) {
		try {
				for (String part : parts) {
					idParts.add(part);
				}
		} catch (Exception e) {
			ErrorUtils.report(logger, e);
		}		
	}
	
	/**
	 * Construct an ID with four parts
	 * @param part1
	 * @param part2
	 * @param part3
	 * @param part4
	 */
	public IdManager(
			  String part1
			, String part2
			, String part3
			, String part4
						) {
		idParts.add(part1);
		idParts.add(part2);
		idParts.add(part3);
		idParts.add(part4);
	}

	/**
	 * Construct an ID with 3 parts
	 * @param part1
	 * @param part2
	 * @param part3
	 */
	public IdManager(
			String part1
			, String part2
			, String part3
			) {
		idParts.add(part1);
		idParts.add(part2);
		idParts.add(part3);
		this.libraryParts.add(part1);
		this.setDomainParts();
	}

	/**
	 * Construct an ID with 2 parts
	 * @param part1
	 * @param part2
	 */
	public IdManager(
			String part1
			, String part2
			) {
		idParts.add(part1);
		idParts.add(part2);
	}
	
	private void setDomainParts() {
		if (this.libraryParts.size() == 1) {
			try {
				String [] parts = this.libraryParts.get(0).split(Constants.DOMAIN_DELIMITER);
				if (parts.length == 3) {
					this.libraryLanguage = parts[0];
					this.libraryCountry = parts[1];
					this.libraryRealm = parts[2];
					this.setLibraryIsDomain(true);
				}
			} catch (Exception e) {
				
			}
		}
	}
	
	/**
	 * Returns the number of parts for this key
	 * @return
	 */
	public int partCount() {
		int i = idParts.size();
		return idParts.size();
	}
	
	/**
	 * Return the ID with all its parts as a delimited string
	 * @return
	 */
	public String getId() {
		if (idParts.size() > 1) {
			return Joiner.on(Constants.ID_DELIMITER).join(idParts);
		} else {
			return idParts.get(0);
		}
	} 
	
	public String getLibrary() {
		if (this.libraryParts.size() > 1) {
			return Joiner.on(Constants.ID_DELIMITER).join(this.libraryParts);
		} else {
			return this.libraryParts.get(0);
		}
	}
	
	public void setLibrary(String library) {
		this.libraryParts.clear();
		this.idParts.set(0, library);
		this.libraryParts.add(library);
		this.setDomainParts();
	}

	public String getTopic() {
		if (this.topicParts.size() > 1) {
			return Joiner.on(Constants.ID_DELIMITER).join(this.topicParts);
		} else {
			return this.topicParts.get(0);
		}
	}
	
	public String getTopicKey() {
		return getTopic() + Constants.ID_DELIMITER + getKey();
	}

	public String getKey() {
		if (this.keyParts.size() > 1) {
			return Joiner.on(Constants.ID_DELIMITER).join(this.keyParts);
		} else {
			return this.keyParts.get(0);
		}
	}

	/**
	 * Get the delimiter character
	 * @return
	 */
	public String getDelimiter() {
		return delimiter;
	}
	
	/**
	 * Return the key part at the index value
	 * @param index
	 * @return the value corresponding to the index
	 */
	public String get(int index) {
		try {
			return idParts.get(index);
		} catch (Exception e) {
			ErrorUtils.report(logger, e);
		}
		return null;
	}

	public static void main (String[] args) {
		IdManager myManager = new IdManager("gr_GR_cog", "actors", "priest");
		System.out.println(myManager.getId());
		myManager = new IdManager("en_US_dedes~actors~deacon");
		System.out.println("domain: " + myManager.get(0));
		System.out.println("topic: " + myManager.get(1));
		System.out.println("key: " + myManager.get(2));
	}

	public String getLibraryLanguage() {
		return libraryLanguage;
	}

	public void setLibraryLanguage(String libraryLanguage) {
		this.libraryLanguage = libraryLanguage;
	}

	public String getLibraryCountry() {
		return libraryCountry;
	}

	public void setLibraryCountry(String libraryCountry) {
		this.libraryCountry = libraryCountry;
	}

	public String getLibraryRealm() {
		return libraryRealm;
	}

	public void setLibraryRealm(String libraryRealm) {
		this.libraryRealm = libraryRealm;
	}

	public boolean isLibraryDomain() {
		return libraryIsDomain;
	}

	public boolean isLibraryDomain(String domain) {
		String[] parts = domain.split(Constants.ID_SPLITTER);
		if (parts.length == 1) {
			parts = domain.split(Constants.DOMAIN_SPLITTER);
			return parts.length == 3;
		} else {
			return false;
		}
	}
	public void setLibraryIsDomain(boolean libraryIsDomain) {
		this.libraryIsDomain = libraryIsDomain;
	}
	
	/**
	 * Create an OSLW entry for this ID and the supplied value
	 * e.g.
	 * 		meMA.Ode6C225 = "Δήμοις τε φαίνει, προσδραμεῖν τῷ Δεσπότῃ."
	 * 
	 * Note: the value will be empty unless:
	 * 1. The size of the library, topic, and key == 1 in all three cases
	 * 2. The library value can be split usng Constants.DOMAIN_DELIMITER and the resulting size is exactly three
	 * @param value
	 * @return
	 */
	public String getAlwbResourceForValue(String value) {
		StringBuffer sb = new StringBuffer();
		if (libraryIsDomain 
				&& this.libraryParts.size() == 1 
				&& this.topicParts.size() == 1 
				&& this.keyParts.size() == 1
				) {
			sb.append(this.keyParts.get(0));
			sb.append(" = ");
			sb.append(AlwbGeneralUtils.wrapQuotes(value.trim()));
			sb.append("\n");
		}
		return sb.toString();
	}

public String getOslwResourceForValue(String value) {
	StringBuffer sb = new StringBuffer();
	if (libraryIsDomain 
			&& this.libraryParts.size() == 1 
			&& this.topicParts.size() == 1 
			&& this.keyParts.size() == 1
			) {
		sb.append("\\itId{");
		sb.append(this.libraryLanguage);
		sb.append("}{");
		sb.append(this.libraryCountry);
		sb.append("}{");
		sb.append(this.libraryRealm);
		sb.append("}{");
		sb.append(this.topicParts.get(0));
		sb.append("}{");
		sb.append(this.keyParts.get(0));
		sb.append("}{\n");
		sb.append(value.trim());
		sb.append("\n}%\n");
	}
	return sb.toString();
}

	/**
	 * If the ID is actually a sequence number, e.g.
	 * 
	 * gr_gr_cog~me.m01.d06~L0031
	 * 
	 * this method will return the int number,
	 * 
	 * i.e. 31
	 * 
	 * If unsuccessful, returns -1
	 * @param id
	 * @return
	 */
	public static int getSeqNbr(String id) {
		int result = -1;
		IdManager idManager = new IdManager(id);
		try {
			if (idManager.getKey().startsWith("L")) {
			result = Integer.parseInt(idManager.getKey().substring(1));
			}
		} catch (Exception e) {
			result = -1;
		}
		return result;
	}
	
	/**
	 * Gets the index to use as the starting sequence number for
	 * a window of values before the specified ID.
	 * 
	 * If the ID occurs at a position < size, the index will be adjusted
	 * accordingly.
	 * 
	 * @param id
	 * @param size
	 * @return
	 */
	public static int getWindowPrefixIndex(String id, int size) {
		int result = -1;
		int seq = getSeqNbr(id);
		if (seq > -1) {
			result = seq - size;
			if (result < 1) {
				result = 1;
			}
		}
		return result;
	}
	
	/**
	 * Gets the index to use as the starting sequence number for
	 * a window of values before the specified ID.
	 * 
	 * If the ID occurs at a position < size, the index will be adjusted
	 * accordingly.
	 * 
	 * @param id
	 * @param size
	 * @return
	 */
	public static int getWindowSuffixIndex(String id, int size) {
		int result = -1;
		int seq = getSeqNbr(id);
		if (seq > -1) {
			result = seq + size;
		}
		return result;
	}

	/**
	 * Pad the value as a valid sequence number as used in the database
	 * L + 5 digits, left padded with zeros
	 * 
	 * and return library~topic~line number
	 * 
	 * @param value
	 * @return
	 */
	public static String createSeqNbr(String library, String topic, int value) {
		return library + "~" + topic + "~" + AlwbGeneralUtils.padNumber("L", 5, value);
	}
	
}
