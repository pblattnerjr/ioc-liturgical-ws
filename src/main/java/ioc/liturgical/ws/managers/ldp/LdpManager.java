package ioc.liturgical.ws.managers.ldp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;

import ioc.liturgical.ws.managers.databases.internal.InternalDbManager;

import org.ocmc.ioc.liturgical.schemas.constants.HTTP_RESPONSE_CODES;
import org.ocmc.ioc.liturgical.schemas.models.db.internal.LTKVJsonObject;
import org.ocmc.ioc.liturgical.schemas.models.ws.response.LiturgicalDayPropertiesForm;
import org.ocmc.ioc.liturgical.schemas.models.ws.response.ResultJsonObjectArray;
import org.ocmc.ioc.liturgical.utils.ErrorUtils;
import org.ocmc.ioc.liturgical.utils.LITURGICAL_CALENDAR_TYPE;
import org.ocmc.ioc.liturgical.utils.LiturgicalDayProperties;

public class LdpManager {
	private static final Logger logger = LoggerFactory.getLogger(LdpManager.class);
	private LiturgicalDayProperties theDay;
	private InternalDbManager dbManager = null;
	/**
	 * Constructor
	 */
	public LdpManager(InternalDbManager dbManager) {
		this.dbManager = dbManager;
		theDay = new LiturgicalDayProperties();
	}
	
	/**
	 * Returns the liturgical day properties for the current date and Gregorian Calendar
	 * @return
	 */
	public ResultJsonObjectArray getLdpForToday() {
		return getLdpForToday(LITURGICAL_CALENDAR_TYPE.GREGORIAN.code);
	}

	/**
	 * Returns the liturgical day properties for the current date
	 * @param calendarType, a code for Julian or Gregorian
	 * @return
	 */
	public ResultJsonObjectArray getLdpForToday(String calendarType) {
		ResultJsonObjectArray result = new ResultJsonObjectArray(true); // true means PrettyPrint the json
		LiturgicalDayPropertiesForm form = new LiturgicalDayPropertiesForm();
		String date = ""; // set the date later, using the LiturgicalDayProperties instance you create.
		try {
			theDay = new LiturgicalDayProperties();
			date = theDay.getFormattedDate();
			LITURGICAL_CALENDAR_TYPE type = LITURGICAL_CALENDAR_TYPE.forWsname(calendarType);
			theDay = new LiturgicalDayProperties(date, type);
			if (theDay.getIsoDateValid()) {
				form = new LiturgicalDayPropertiesForm(theDay);
				LTKVJsonObject record = 
						new LTKVJsonObject(
							"ldp" // normally this and the next two parms are used for database writes
							, "ldp" // but these won't be used, so we will fake it
							, "ldp"
							, form.schemaIdAsString()
							, form.toJsonObject()
							);
				List<JsonObject> dbResults = new ArrayList<JsonObject>();
				dbResults.add(record.toJsonObject());
				result.setValueSchemas(dbManager.getSchemas(dbResults, null));
				result.setResult(dbResults);
			} else {
				result.setStatusCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
				result.setStatusMessage(HTTP_RESPONSE_CODES.BAD_REQUEST.message);
			}
			result.setQuery("Liturgical Properties for " + date + " using the " + type.description + " calendar ");
		} catch (Exception e) {
			result.setStatusCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
			result.setStatusMessage(HTTP_RESPONSE_CODES.BAD_REQUEST.message);
			ErrorUtils.report(logger, e);
		}
		return result;
	}

	/**
	 * Returns the liturgical day properties for the specified date
	 * @param date ISO String, ex: "2016-11-19T12:00:00.000Z"
	 * @param calendarType, a code for Gregorian or Julian
	 * @return
	 */
	public ResultJsonObjectArray getLdpForDate(String date, String calendarType) {
		ResultJsonObjectArray result = new ResultJsonObjectArray(true); // true means PrettyPrint the json
		try {
			LiturgicalDayPropertiesForm form = new LiturgicalDayPropertiesForm();
			LITURGICAL_CALENDAR_TYPE type = LITURGICAL_CALENDAR_TYPE.forWsname(calendarType);
			theDay = new LiturgicalDayProperties(date, type);
			if (theDay.getIsoDateValid()) {
				form = new LiturgicalDayPropertiesForm(theDay);
				LTKVJsonObject record = 
						new LTKVJsonObject(
							"ldp" // normally this and the next two parms are used for database writes
							, "ldp" // but these won't be used, so we will fake it
							, "ldp"
							, form.schemaIdAsString()
							, form.toJsonObject()
							);
				List<JsonObject> dbResults = new ArrayList<JsonObject>();
				dbResults.add(record.toJsonObject());
				result.setValueSchemas(dbManager.getSchemas(dbResults, null));
				result.setResult(dbResults);
			} else {
				result.setStatusCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
				result.setStatusMessage(HTTP_RESPONSE_CODES.BAD_REQUEST.message);
			}
			result.setQuery("Liturgical Properties for " + date + " using the " + type.description + " calendar ");
		} catch (Exception e) {
			result.setStatusCode(HTTP_RESPONSE_CODES.BAD_REQUEST.code);
			result.setStatusMessage(HTTP_RESPONSE_CODES.BAD_REQUEST.message);
		}
		return result;
	}
	
}
