package ioc.liturgical.ws.constants;

import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

/**
 * Adapted from mkyong dot com
 * @author mac002
 *
 */
public class IsoCountries {
	private Map<String, String> languagesMap = new TreeMap<String, String>();
	
	public IsoCountries() {
		this.initLanguageMap();
	}
	
	public void getListOfCountries() {

		String[] countries = Locale.getISOCountries();

		int supportedLocale = 0, nonSupportedLocale = 0;

		for (String countryCode : countries) {
		  Locale obj = null;
		  if (languagesMap.get(countryCode) == null) {
			obj = new Locale("", countryCode);
			nonSupportedLocale++;
		  } else {
			//create a Locale with own country's languages
			obj = new Locale(languagesMap.get(countryCode), countryCode);
			supportedLocale++;

		  }

		  System.out.println("Country Code = " + obj.getCountry() 
			+ ", Country Name = " + obj.getDisplayCountry(obj)
			+ ", Languages = " + obj.getDisplayLanguage());

		  }

		  System.out.println("nonSupportedLocale : " + nonSupportedLocale);
		  System.out.println("supportedLocale : " + supportedLocale);

	    }
	public void initLanguageMap() {
		Locale[] locales = Locale.getAvailableLocales();
		for (Locale obj : locales) {
			  if ((obj.getDisplayCountry() != null) && (!"".equals(obj.getDisplayCountry()))) {
				languagesMap.put(obj.getCountry(), obj.getLanguage());
			  }
		}
	 }
	
	public Locale getLocale(String countryCode) {
		Locale obj = null;
		if (languagesMap.get(countryCode) == null) {
			obj = new Locale("", countryCode);
		  } else {
			obj = new Locale(languagesMap.get(countryCode), countryCode);
		  }
		return obj;
	}

	public String getLocaleName(String countryCode) {
		String name = "";
		try {
			Locale locale = this.getLocale(countryCode);
			name = locale.getDisplayCountry(locale);
		} catch (Exception e) {
			// ignore
		}
		return name;
	}
	
	public static void main(String[] args) {

		IsoCountries obj = new IsoCountries();
		obj.getListOfCountries();
		System.out.println(obj.getLocaleName("AE"));

	 }
}
