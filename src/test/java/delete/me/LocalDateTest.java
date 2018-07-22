package delete.me;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class LocalDateTest {
	public static void main(String [] args) {
		String s1 = "Sunday, June 10, 2018";
        Date date = new Date();
 
  
 
        // Get a France locale using a Locale constant.
        Locale localeFR = Locale.FRANCE;
        // Get a Guatemala locale using a Locale constant.
        Locale localeGT = new Locale("es", "ES");
        // Create an English/US locale using the constructor.
        Locale localeEN = new Locale("en", "US" );
 
        // Get a date time formatter for display in France.
        DateFormat fullDateFormatFR =
            DateFormat.getDateInstance(
            		DateFormat.FULL,
            		localeFR
            );
   
 
        // Get a date time formatter for display in Guatemala.
        DateFormat fullDateFormatGT =
            DateFormat.getDateInstance(
            		DateFormat.FULL,
            		localeGT
            );
   
 
        // Get a date time formatter for display in the U.S.
 
        DateFormat fullDateFormatEN =
 
            DateFormat.getDateInstance(
            		DateFormat.FULL,
            		localeEN
            );
 
        System.out.println("Locale: " + localeFR.getDisplayName());
        System.out.println(fullDateFormatFR.format(date));
 
        System.out.println("Locale: " + localeGT.getDisplayName());
       System.out.println(fullDateFormatGT.format(date));

        System.out.println("Locale: " + localeEN.getDisplayName());
        System.out.println(fullDateFormatEN.format(date));	}
}
