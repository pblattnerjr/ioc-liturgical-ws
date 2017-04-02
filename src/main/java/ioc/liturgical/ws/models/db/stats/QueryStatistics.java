package ioc.liturgical.ws.models.db.stats;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import com.google.gson.annotations.Expose;

import net.ages.alwb.utils.core.datastores.json.exceptions.BadIdException;
import net.ages.alwb.utils.core.datastores.json.models.LTK;

/**
 * Records a query and the result count.
 * If you add a new property, add it to the getAsPropertiesMap as well.
 * @author mac002
 *
 */
public class QueryStatistics extends LTK {

	@Expose String query = "";
	@Expose long resultCount = 0;
	@Expose int statusCode = 200;
	@Expose int year;
	@Expose int month;
	@Expose int dayOfMonth;
	@Expose int dayOfYear;
	Instant now = Instant.now();
	
	public QueryStatistics(
			String library
			, String topic
			, String query
			, int statusCode
			, long resultCount
			) throws BadIdException {
		super(library, topic, "");
		Instant now = Instant.now();
		this.setKey(now.toString());
		this.joinPartsIntoId();
		LocalDateTime time = LocalDateTime.ofInstant(now,ZoneId.of("UTC"));
		this.year = time.getYear();
		this.month = time.getMonthValue();
		this.dayOfMonth = time.getDayOfMonth();
		this.dayOfYear = time.getDayOfYear();
 		this.query = query;
 		this.statusCode = statusCode;
		this.resultCount = resultCount;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public long getResultCount() {
		return resultCount;
	}

	public void setResultCount(long resultCount) {
		this.resultCount = resultCount;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getDayOfMonth() {
		return dayOfMonth;
	}

	public void setDayOfMonth(int dayOfMonth) {
		this.dayOfMonth = dayOfMonth;
	}

	public int getDayOfYear() {
		return dayOfYear;
	}

	public void setDayOfYear(int dayOfYear) {
		this.dayOfYear = dayOfYear;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

}
