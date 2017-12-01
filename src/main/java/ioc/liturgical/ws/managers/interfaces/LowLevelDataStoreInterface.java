package ioc.liturgical.ws.managers.interfaces;

import java.util.List;

import org.ocmc.ioc.liturgical.schemas.models.db.internal.LTKVJsonObject;
import org.ocmc.ioc.liturgical.schemas.models.ws.response.RequestStatus;

import com.google.gson.JsonObject;

import ioc.liturgical.ws.managers.exceptions.DbException;

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
