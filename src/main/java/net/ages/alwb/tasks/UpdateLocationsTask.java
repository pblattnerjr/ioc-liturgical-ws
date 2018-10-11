package net.ages.alwb.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.restassured.path.json.JsonPath;
import ioc.liturgical.ws.constants.Constants;
import ioc.liturgical.ws.managers.databases.external.neo4j.ExternalDbManager;

import java.net.URL;

import org.ocmc.ioc.liturgical.schemas.models.db.stats.LocationLog;
import org.ocmc.ioc.liturgical.schemas.models.ws.response.ResultJsonObjectArray;
import org.ocmc.ioc.liturgical.utils.ErrorUtils;

/**
 * Runs a task (separate thread) to record the location of a request to search the database
 * 
 * @author mac002
 *
 */
public class UpdateLocationsTask implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(UpdateLocationsTask.class);

	ExternalDbManager manager = null;
	String address = "";
	
	public UpdateLocationsTask (
			ExternalDbManager manager
			, String address
			) {
		this.manager = manager;
		this.address = address;
		if (this.address == null) {
			this.address = "countryCode|country|region|regionName|city";
		}
	}
	
	@Override
	public void run() {
		synchronized(this) {
			try {
				//countryCode|country|region|regionName|city
				   String countryCode = "unknown";
				   String country = "unknown";
				   String region =  "unknown";
				   String regionName = "unknown";
				   String city = "unknown";
				   LocationLog log = new LocationLog(countryCode, region, city);
				   String [] parts = this.address.split(Constants.PIPE_SPLITTER);
				   if (parts.length == 5) {
					   countryCode = parts[0];
					   country = parts[1];
					   region = parts[2];
					   regionName = parts[3];
					   city = parts[4];
					   log = new LocationLog(countryCode, region, city);
				   }
				   String query = "match (n:LocationLog) where n.id = '" + log.getId() + "' return properties(n)";
				   ResultJsonObjectArray result = this.manager.getForQuery(query, false, false);
				   try {
						log = this.manager.gson.fromJson(result.getFirstObject().get("properties(n)").getAsJsonObject(), LocationLog.class);
				   } catch (Exception e) {
				   }
				   if (country.equals("country")) {
					   country = "unknown";
				   }
				   log.setCountry(country);
				   log.setRegionName(regionName);
				   log.increment();
				   ExternalDbManager.neo4jManager.mergeWhereEqual("LocationLog", log);
			} catch (Exception e) {
				ErrorUtils.report(logger, e);
			}
		}
	}
	
}
