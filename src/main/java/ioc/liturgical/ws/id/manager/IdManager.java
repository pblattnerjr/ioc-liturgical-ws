package ioc.liturgical.ws.id.manager;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;

import ioc.liturgical.ws.constants.Constants;
import ioc.liturgical.ws.constants.DB_TOPICS;
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

	private List<String> keyparts = new ArrayList<String>();
	private String delimiter = "|";
	private String splitter = "\\|";
	
	/**
	 * This constructor expects the id to have parts delimited by the pipe |
	 * @param id
	 */
	public IdManager(String id) {
		try {
			String[] parts = id.split(splitter);
			if (parts.length > 1) {
				for (String part : parts) {
					keyparts.add(part);
				}
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
			if (parts.length > 1) {
				for (String part : parts) {
					keyparts.add(part);
				}
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
		keyparts.add(part1);
		keyparts.add(part2);
		keyparts.add(part3);
		keyparts.add(part4);
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
		keyparts.add(part1);
		keyparts.add(part2);
		keyparts.add(part3);
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
		keyparts.add(part1);
		keyparts.add(part2);
	}
	
	/**
	 * Returns the number of parts for this key
	 * @return
	 */
	public int partCount() {
		return keyparts.size();
	}
	
	/**
	 * Return the ID with all its parts as a delimited string
	 * @return
	 */
	public String getId() {
		return Joiner.on(Constants.ID_DELIMITER).join(keyparts);
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
			return keyparts.get(index);
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
