package ioc.liturgical.ws.calendar;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import org.joda.time.LocalDate;
import org.ocmc.ioc.liturgical.schemas.id.managers.IdManager;
import org.ocmc.ioc.liturgical.schemas.iso.lang.LocaleDate;

import ioc.liturgical.ws.constants.Constants;

/**
 * 28 days Feb, but 29 in leap year
 * 30 days Sept, April, June, Nov
 * 31 days Jan, Mar, May, July, Aug, Oct, Dec
 * 
 * @author mac002
 *
 */
public class DateGenerator {
	private Locale locale = null;
	private int year = 0;
	private String library = "";
	
	public DateGenerator(
			String library
			, int year
			) {
		this.library = library;
		this.year = year;
		IdManager idManager = new IdManager(
				library 
				+ Constants.ID_DELIMITER
				+ "topic"
				+ Constants.ID_DELIMITER
				+ "key"
				);
		this.locale = idManager.getLocale();
	}
	
	public Map<String,String> getDays() {
		Map<String,String> days = new TreeMap<String,String>();
		for (int month = 1; month < 13; month++) {
			for (LocaleDate date : this.getDaysList(month)) {
				IdManager idManager = new IdManager(
						library
						+ date.getTopicKey()
						);
				days.put(idManager.getId() + ".ymd", date.getDateFull());
				days.put(idManager.getId() + ".md", date.getDayOfWeekAlt());
			}
		}
		return days;
	}
	
	private List<LocaleDate> getDaysList(
			int month
			) {
		List<LocaleDate> days = new ArrayList<LocaleDate>();
		String strYear = Integer.toString(this.year);
		String strMonth = Integer.toString(month);
		int daysInMonth = this.daysInMonth(month) + 1;
		for (int day = 1; day < daysInMonth; day++) {
			String strDay = Integer.toString(day);
			LocaleDate date = new LocaleDate(
					this.locale
					, strYear
					, strMonth
					, strDay
			);
			days.add(date);
		}
		return days;
	}
	
	private int daysInMonth(int month) {
		/**
		 * 28 days Feb, but 29 in leap year
		 * 30 days Sept, April, June, Nov
		 * 31 days Jan, Mar, May, July, Aug, Oct, Dec
		 */
		int days = 31;
		switch (month) {
		case 2: { // Feb
			if (DateGenerator.isLeapYear(this.year)) {
				days = 29;
			} else {
				days = 28;
			}
			break;
		}
		case 4: { // April
			days = 30;
			break;
		}
		case 6: { // June
			days = 30;
			break;
		}
		case 9: { // Sept
			days = 30;
			break;
		}
		case 11: { // Nov
			days = 30;
			break;
		}
		}
		return days;
	}
	public static boolean isLeapYear(int year) {
		if ((year % 400 == 0) || ((year % 4 == 0) && (year % 100 != 0))) {
			return true;
		} else {
			return false;
		}
	}
}
