package net.ages.alwb.utils.core.id.managers;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;

import ioc.liturgical.ws.constants.Constants;
import net.ages.alwb.utils.core.error.handling.ErrorUtils;

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
	private List<String> libaryParts = new ArrayList<String>();
	private List<String> topicParts = new ArrayList<String>();
	private List<String> keyParts = new ArrayList<String>();
	private String delimiter = Constants.ID_DELIMITER;
	private String splitter = Constants.ID_SPLITTER;
	private int topicIndex = 1;
	private int keyIndex = 2;
	
	/**
	 * This constructor expects the id to have parts delimited by Constants.ID_DELIMTER
	 * @param id
	 */
	public IdManager(String id) {
		try {
			String[] parts = id.split(splitter);
			for (String part : parts) {
					idParts.add(part);
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
				this.libaryParts.add(idParts.get(i));
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
		if (this.libaryParts.size() > 1) {
			return Joiner.on(Constants.ID_DELIMITER).join(this.libaryParts);
		} else {
			return this.libaryParts.get(0);
		}
	}

	public String getTopic() {
		if (this.topicParts.size() > 1) {
			return Joiner.on(Constants.ID_DELIMITER).join(this.topicParts);
		} else {
			return this.topicParts.get(0);
		}
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
		myManager = new IdManager("en_US_dedes|actors|deacon");
		System.out.println("domain: " + myManager.get(0));
		System.out.println("topic: " + myManager.get(1));
		System.out.println("key: " + myManager.get(2));
	}
}
