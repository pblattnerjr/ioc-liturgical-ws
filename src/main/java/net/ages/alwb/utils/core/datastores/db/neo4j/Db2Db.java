package net.ages.alwb.utils.core.datastores.db.neo4j;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.lang3.StringEscapeUtils;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.StatementResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import ioc.liturgical.ws.constants.HTTP_RESPONSE_CODES;
import ioc.liturgical.ws.managers.databases.external.neo4j.utils.Neo4jConnectionManager;
import ioc.liturgical.ws.managers.exceptions.DbException;
import ioc.liturgical.ws.models.RequestStatus;
import ioc.liturgical.ws.models.ResultJsonObjectArray;
import net.ages.alwb.utils.core.datastores.db.neo4j.models.NodePairParameters;
import net.ages.alwb.utils.core.datastores.db.neo4j.models.NodePairs;
import net.ages.alwb.utils.core.datastores.db.neo4j.models.NodePairsList;
import net.ages.alwb.utils.core.datastores.json.models.ModelHelpers;
import net.ages.alwb.utils.core.error.handling.ErrorUtils;
import net.ages.alwb.utils.core.misc.ListMap;
import ioc.liturgical.ws.models.db.docs.ontology.Animal;;

public class Db2Db {
	private static final Logger logger = LoggerFactory.getLogger(Db2Db.class);
	private Neo4jConnectionManager fromDb;
	private Neo4jConnectionManager toDb;
	private String boltFrom = "";
	private String boltTo = "";
	private URLCodec urlCodec = new URLCodec();
	
	/**
	 * 
	 * @param boltUrlFrom must include the port number
	 * @param usernameFrom
	 * @param passwordFrom
	 * @param boltUrlTo must include the port number and be different than boltUrlFrom's port
	 * @param usernameTo
	 * @param passwordTo
	 */
	public Db2Db(
					  String boltUrlFrom
					  , String usernameFrom
					  , String passwordFrom
					  , String boltUrlTo
					  , String usernameTo
					  , String passwordTo
			) {

		this.boltFrom = boltUrlFrom;
		this.boltTo = boltUrlTo;
		
		  fromDb = new Neo4jConnectionManager(
				  boltUrlFrom
				  , usernameFrom
				  , passwordFrom
				  , true
				  );
		 toDb = new Neo4jConnectionManager(
				  boltUrlTo
				  , usernameTo
				  , passwordTo
				  , false
				  );
	}
	
	public static String toBoltLocalUrl(String port) {
		return "127.0.0.1" + ":" + port;
	}
	
	public RequestStatus test() {
		Animal puppy = new Animal("puppy");
		try {
			return toDb.insert(puppy);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * This transfer is for nodes and relationships without schemas.
	 * 
	 * TODO for WordNet
	 * 1. Create (n:lemon#LexicalForm) and put the lemma in it (without the pos) and link with LexicalEntry
	 * 			if it is LexicalEntry or LexicalSense, grab the forms.
	 *         Have two maps, one for entries, one for synsets
	 *         If the form has an entry, grab the list and add the id for entry, and the same for the 
	 *         store the id of the LexicalEntry or the LexicalSense
	 *         Then process the maps to create links from n:lemon#LexicalForm to the LexicalEntry and to the LexicalSense
	 * 2. CREATE INDEX ON :`lemon#LexicalEntry`(id)
	 *     CREATE INDEX ON :`lemon#LexicalSense`(id)
	 *     CREATE INDEX ON :`wdo#Synset`(id)
	 * @return
	 */
    public RequestStatus transferWordNet(
        	boolean copyFromDbNodes
        	, boolean copyFromDbLinks
        	, boolean createLexicalForms
        	, boolean createLinksFromLexicalFormToLexicalEntry
        	, boolean createLinksFromLexicalFormToSynset
    		)  {
    	RequestStatus result = new RequestStatus();
    	StringBuffer message = new StringBuffer();
    	/**
    	 * booleans for restarting the process
    	 */

    	try {
        	logger.info("Transferring WordNet data from " + this.boltFrom + " to " + this.boltTo);
			String query = "CREATE constraint on (p:`lemon#LexicalForm`) ASSERT p.id IS UNIQUE";
        	toDb.processConstraintQuery(query);
			query = "CREATE constraint on (p:`lemon#LexicalEntry`) ASSERT p.id IS UNIQUE";
        	toDb.processConstraintQuery(query);
			query = "CREATE constraint on (p:`lemon#LexicalSense`) ASSERT p.id IS UNIQUE";
        	toDb.processConstraintQuery(query);
			query = "CREATE constraint on (p:`wdo#Synset`) ASSERT p.id IS UNIQUE";
        	toDb.processConstraintQuery(query);

        	query = "match (doc) return doc";
        	logger.info("fromDB getting nodes");
        	ResultJsonObjectArray fromNodes = this.fromDb.getForQuery(query);
        	message.append(fromNodes.status.getUserMessage() + " from Node count  = " + fromNodes.getResultCount() +"\n");
 
        	logger.info("toDb creating nodes");
        	ListMap<String> entryMap = new ListMap<String>();
        	Map<String, List<String>> synsetMap = new TreeMap<String, List<String>>();

        	for (JsonObject node : fromNodes.values) {
        		JsonObject doc = node.get("doc").getAsJsonObject();
        		JsonObject props = doc.get("properties").getAsJsonObject();
        		String id = "";
        		try {
            		id = props.get("id").getAsJsonObject().get("val").getAsString();
        		} catch (Exception e) {
        			throw e;
        		}
        		if (copyFromDbNodes) {
            		toDb.insert(node);
        		}
        		JsonArray labels = doc.get("labels").getAsJsonArray();
        		String label = labels.get(0).getAsString();
        		if (label.equals("lemon#LexicalEntry")) {
        			String form = id.substring(3,id.length()-2); // 'wn/Pinctada+margaritifera-n' = Pinctada+margaritifera
        			entryMap.put(form, id);
        		}
        	}
        	logger.info("toDb creating " + entryMap.itemsCount() + " LexicalForm(s)");
			
        	long valueCount = entryMap.itemsCount();
        	long cnt = 0;
			long interval = 10000;
			long intervalCnt = 0;
			
        	for (String form : entryMap.keySet()) {
        		for (String id : entryMap.get(form)) {
        			query = "create (n:`lemon#LexicalForm` {id:'wn/form/" + urlCodec.encode(form) + "'}) return n.id";
        			try {
        				if (createLexicalForms) {
                			ResultJsonObjectArray queryResult = toDb.getForQuery(query);
                			cnt = cnt + queryResult.valueCount;
                			intervalCnt = intervalCnt + 1;
                			if (intervalCnt == interval) {
                				logger.info(done(cnt, valueCount));
                				intervalCnt = 0;
                			}
            			}
        			} catch (Exception e) {
        				logger.error(e.getMessage() + " " + id);
        			}
        		}
        	}
        	logger.info("toDb creating " + entryMap.itemsCount() + " links from LexicalForm to LexicalEntry");
        	cnt = 0;
        	for (String form : entryMap.keySet()) {
        		for (String id : entryMap.get(form)) {
        			query  = "match (f:`lemon#LexicalForm`) where f.id ends with '/form/" 
							+ urlCodec.encode(form) 
					+ "' match (s:`lemon#LexicalEntry`) where s.id =  '" + id + "' "
					+ "merge (f)-[r:`wn/form/lemma`]->(s) return count(r)"
					;
        			if (createLinksFromLexicalFormToLexicalEntry) {
            			ResultJsonObjectArray queryResult = toDb.getForQuery(query);
            			cnt = cnt + queryResult.valueCount;
            			intervalCnt = intervalCnt + 1;
            			if (intervalCnt == interval) {
            				logger.info(done(cnt, valueCount));
            				intervalCnt = 0;
            			}
        			}
        		}
        	}
        	valueCount = getCount(synsetMap);
        	logger.info("toDb creating " + valueCount + " links from LexicalForm to Synset");
        	cnt = 0;
        	for (String form : synsetMap.keySet()) {
        		for (String id : synsetMap.get(form)) {
        			query  = "match (f:`lemon#LexicalForm`) where f.id ends with '/form/" 
        								+ urlCodec.encode(form) 
        						+ "' match (s:`wdo#Synset`) where s.id =  '" + id + "' "
        						+ "merge (f)-[r:`wn/form/lemma`]->(s) return count(r)"
        			;
        			if (createLinksFromLexicalFormToSynset) {
            			ResultJsonObjectArray queryResult = toDb.getForQuery(query);
            			cnt = cnt + queryResult.valueCount;
            			intervalCnt = intervalCnt + 1;
            			if (intervalCnt == interval) {
            				logger.info(done(cnt, valueCount));
            				intervalCnt = 0;
            			}
        			}
        		}
        	}
        	logger.info("fromDb getting relationships and toDb creating relationships");
        	NodePairs pairs = null;
        	query = "match ()-[r]->() return distinct type(r)";
            ResultJsonObjectArray fromRelationshipTypes = this.fromDb.getForQuery(query);
        	for (JsonObject linkType : fromRelationshipTypes.values) {
            	cnt = 0;
        		String type = linkType.get("type(r)").getAsString();
                query = "match (a)-[r:`" + type +"`]->(b) return a.id, labels(a), type(r), b.id, labels(b)";
                ResultJsonObjectArray fromRelationships = this.fromDb.getForQuery(query);
                valueCount = fromRelationships.valueCount;
            	logger.info("toDb creating " + valueCount + " relationships of type " + type) ;
            	for (JsonObject link : fromRelationships.values) {
            		if (copyFromDbLinks) {
                		toDb.createRelationship(
                				link.get("a.id").getAsString()
                				, toDb.createLabels(link.get("labels(a)").getAsJsonArray())
                				, link.get("b.id").getAsString()
                				, toDb.createLabels(link.get("labels(b)").getAsJsonArray())
                				, type
                				);
            			cnt = cnt + 1;
            			intervalCnt = intervalCnt + 1;
            			if (intervalCnt == interval) {
            				logger.info(done(cnt, valueCount));
            				intervalCnt = 0;
            			}
            		}
            	}
        	}
		} catch (Exception e) {
			ErrorUtils.report(logger, e);
			result.setCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
			result.setMessage(e.getMessage());
			message.append(e.getMessage());
		}
       	logger.info(message.toString());
    	return result;
    }

    private static String done(long x, long y) {
 	   int percentage = (int)(x * 100.0 / y + 0.5);
     	return String.format("%,d%% done.", percentage);
     }

    
    private long getCount(Map<String,List<String>> map) {
    	long result = 0;
    	for (String a : map.keySet()) {
    		result = result + map.get(a).size();
    	}
    	return result;
    }
	public Neo4jConnectionManager getFromDb() {
		return fromDb;
	}

	public void setFromDb(Neo4jConnectionManager fromDb) {
		this.fromDb = fromDb;
	}

	public Neo4jConnectionManager getToDb() {
		return toDb;
	}

	public void setToDb(Neo4jConnectionManager toDb) {
		this.toDb = toDb;
	}

	public String getBoltFrom() {
		return boltFrom;
	}

	public void setBoltFrom(String boltFrom) {
		this.boltFrom = boltFrom;
	}

	public String getBoltTo() {
		return boltTo;
	}

	public void setBoltTo(String boltTo) {
		this.boltTo = boltTo;
	}
}
