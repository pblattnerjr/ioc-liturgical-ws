package ioc.liturgical.ws.managers.interfaces;

import com.google.gson.JsonObject;

import ioc.liturgical.ws.managers.exceptions.DbException;
import net.ages.alwb.utils.core.datastores.json.exceptions.MissingSchemaIdException;

import org.ocmc.ioc.liturgical.schemas.exceptions.BadIdException;
import org.ocmc.ioc.liturgical.schemas.models.ws.response.RequestStatus;
import org.ocmc.ioc.liturgical.schemas.models.ws.response.ResultJsonObjectArray;

public interface HighLevelDataStoreInterface {

	/**
	 * Add a doc whose value is a JsonObject and whose _id is a library|topic|key
	 * @param library
	 * @param topic
	 * @param key
	 * @param json
	 * @param MissingSchemaIdException
	 * @throws DbException 
	 * @throws BadIdException 
	 */
	public RequestStatus addLTKVJsonObject(
			String library
			, String topic
			, String key
			, String schemaId
			, JsonObject json
			) throws DbException, MissingSchemaIdException, BadIdException; 

	
	/**
	 * Is there a single doc that matches this id?
	 * @param _id
	 * @return true if there is only one doc that matches
	 */
	public boolean existsUnique(String _id);
	
	
	/**
	 * Get doc whose id matches
	 * @param pattern, e.g. _users/{id}
	 * @return matching doc
	 */
	public ResultJsonObjectArray getForId(String id);

	/**
	 * Get all docs whose id starts with specified pattern
	 * @param pattern, e.g. _users
	 * @return all matching docs
	 */
	public ResultJsonObjectArray getForIdStartsWith(String id);
	

	/**
	 * Update a database doc whose value is a JsonObject and whose _id is library|topic|key
	 * @param library
	 * @param topic
	 * @param key
	 * @param schemaId
	 * @param json
	 * @return RequestStatus
	 * @throws BadIdException
	 * @throws MissingSchemaIdException
	 * @throws DbException
	 */
	public RequestStatus updateLTKVJsonObject(
			String library
			, String topic
			, String key
			, String schemaId
			, JsonObject json
			) throws BadIdException, MissingSchemaIdException, DbException;
	
	public RequestStatus deleteForId(String id);
}
