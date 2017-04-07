package ioc.liturgical.ws.managers.interfaces;

import java.sql.SQLException;
import java.util.List;

import org.neo4j.driver.v1.StatementResult;

import com.google.gson.JsonObject;

import ioc.liturgical.ws.constants.HTTP_RESPONSE_CODES;
import ioc.liturgical.ws.managers.exceptions.DbException;
import ioc.liturgical.ws.models.RequestStatus;
import ioc.liturgical.ws.models.ResultJsonObjectArray;
import net.ages.alwb.utils.core.datastores.json.models.LTKVJsonObject;

public interface LowLevelDataStoreInterface {

	/**
     * Inserts a single JsonObject
     * @param doc - to be inserted
     * @throws DbException
     */
    public void insert(JsonObject doc) throws DbException; 

    /**
     * Inserts all JsonObjects in the list
     * @param docs
     * @throws DbException
     */
    public void insert(List<JsonObject> docs) throws DbException;
    
    /**
     * Updates a single JsonObject (i.e., one that exists).
     * The _id for the update is retrieved from the JsonObject.
     * @param doc - to be inserted
     * @throws DbException 
     */
    public RequestStatus updateWhereEqual(LTKVJsonObject doc) throws DbException;

	public RequestStatus insert(LTKVJsonObject doc) throws DbException;
    
    public RequestStatus deleteNodeWhereEqual(String id) throws DbException;
	
}
