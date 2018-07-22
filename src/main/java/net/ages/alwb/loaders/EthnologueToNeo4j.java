package net.ages.alwb.loaders;


import ioc.liturgical.ws.constants.IsoCountries;
import ioc.liturgical.ws.managers.databases.external.neo4j.utils.Neo4jConnectionManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import org.ocmc.ioc.liturgical.schemas.iso.lang.EthnologueCountry;
import org.ocmc.ioc.liturgical.schemas.iso.lang.EthnologueLanguage;
import org.ocmc.ioc.liturgical.schemas.iso.lang.IsoLangThreeToTwo;
import org.ocmc.ioc.liturgical.utils.FileUtils;
	
	import com.google.gson.Gson;
	import com.google.gson.GsonBuilder;
	import com.google.gson.JsonElement;
	import com.google.gson.JsonObject;
	import com.google.gson.JsonParser;

	/**
	 * A one-off utility for loading Ethnologue tab delimited files to Neo4j
	 * @author mac002
	 *
	 */
	public class EthnologueToNeo4j {
		private Neo4jConnectionManager dbManager = null;
		private Map<String,EthnologueCountry> countries = new TreeMap<String,EthnologueCountry>();
		private Map<String,EthnologueLanguage> languages = new TreeMap<String,EthnologueLanguage>();
		private Map<String,EthnologueLanguage> index = new TreeMap<String,EthnologueLanguage>();
		private IsoCountries isoCountries = new IsoCountries();
		
		public EthnologueToNeo4j(
				String server
				, String uid
				, String pwd
				, String pathToCountries
				, String pathToLangs
				, String pathToIndex
				) {
			this.process(server, uid, pwd, pathToCountries, pathToLangs, pathToIndex);
		}
		
		
		private void process(
				String url
				, String uid
				, String pwd
				, String pathToCountries
				, String pathToLangs
				, String pathToIndex
				) {
			  this.dbManager = new Neo4jConnectionManager(
					  url
					  , uid
					  , pwd
					  , false
					  );
			  this.loadCountries(pathToCountries);
			  this.loadLanguages(pathToLangs);
			  this.loadIndex(pathToIndex);
//			this.updateDatabaseWithCountries();
//			this.updateDatabaseWithLanguages();
		}
		
		private void loadCountries(String path) {
			for (String line : FileUtils.linesFromFile(new File(path))) {
				String [] parts = line.split("\\t");
				EthnologueCountry country = null;
				if (parts.length == 3) {
					country = new EthnologueCountry(parts[0]);
					country.setCountryName(parts[1]);
					country.setCountryNameLocal(isoCountries.getLocaleName(parts[0]));
					country.setArea(parts[2]);
				}
				this.countries.put(country.getCountryCode(), country);
			}
		}
		
		private void loadLanguages(String path) {
			for (String line : FileUtils.linesFromFile(new File(path))) {
				Locale locale = null;
				String [] parts = line.split("\\t");
				if (! parts[0].startsWith("LangID")) {
					EthnologueLanguage lang = null;
					if (parts.length == 4) {
						if (parts[2].equals("L")) {
							lang = new EthnologueLanguage(parts[1], parts[0]);
							EthnologueCountry country = this.countries.get(lang.getCountryCode());
							lang.setCountryName(country.getCountryName());
							lang.setCountryNameLocal(country.getCountryNameLocal());
							lang.setArea(country.getArea());
							lang.setLanguageCodeTwo(IsoLangThreeToTwo.threeToTwo(lang.getLanguageCode()));
							lang.setLanguageName(parts[3]);
							lang.setLanguageNameLocal(lang.getLanguageName());
							this.languages.put(lang.getLanguageCode(), lang);
						}
					}
				}
			}
		}
		private void loadIndex(String path) {
			for (String line : FileUtils.linesFromFile(new File(path))) {
				Locale locale = null;
				String [] parts = line.split("\\t");
				if (! parts[0].startsWith("LangID")) {
					EthnologueLanguage lang = null;
					if (parts.length == 4) {
						if (parts[2].equals("DP") || parts[2].equals("LP")) {
							// ignore.  These are pejorative names for the language
						} else {
							lang = new EthnologueLanguage(parts[1], parts[0]);
							EthnologueCountry country = this.countries.get(lang.getCountryCode());
							EthnologueLanguage eLang = this.languages.get(lang.getLanguageCode());
							if (eLang != null) {
								lang.setCountryName(country.getCountryName());
								lang.setCountryNameLocal(country.getCountryNameLocal());
								lang.setArea(country.getArea());
								lang.setLanguageCodeTwo(IsoLangThreeToTwo.threeToTwo(lang.getLanguageCode()));
								try {
									lang.setLanguageName(eLang.getLanguageName());
								} catch (Exception e) {
									e.printStackTrace();
								}
								lang.setLanguageNameLocal(parts[3]);
								this.index.put(lang.getId(), lang);
							}
						}
					}
				}
			}
		}
		
		private void updateDatabaseWithCountries() {
			System.out.println("Loading database for " + this.countries.size() + " countries");
			int count = 1;
			int all =this.countries.size();
			for (EthnologueCountry country :this.countries.values()) {
				try {
					System.out.println(count + " of " + all + " " + country.getId());
					count++;
					this.dbManager.mergeWhereEqual("Root:IsoCountry", country);
					country.setPrettyPrint(true);
					System.out.println(country.toJsonString());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		private void updateDatabaseWithLanguages() {
			System.out.println("Loading database for " + this.index.size() + " languages");
			int count = 1;
			int all = this.index.size();
			for (EthnologueLanguage lang : this.index.values()) {
				try {
					System.out.println(count + " of " + all + " " + lang.getId());
					count++;
					this.dbManager.mergeWhereEqual("Root:Ethnologue", lang);
					lang.setPrettyPrint(true);
					System.out.println(lang.toJsonString());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		public static void main(String[] args) {
			String pathToLangs = "/Users/mac002/Git/ocmc-translation-projects/ethnologue/LanguageCodes.tab.txt";
			String pathToCountries = "/Users/mac002/Git/ocmc-translation-projects/ethnologue/CountryCodes.tab.txt";
			String pathToIndex = "/Users/mac002/Git/ocmc-translation-projects/ethnologue/LanguageIndex.tab.txt";
			String uid = System.getenv("uid");
			String pwd = System.getenv("pwd");
			String url= "localhost"; // "159.203.89.233";
			EthnologueToNeo4j labels2Neo = new EthnologueToNeo4j(
					url
					, uid
					, pwd
					, pathToCountries
					, pathToLangs
					, pathToIndex
					);
			Locale locale = new Locale("ar","SA");
			System.out.println(locale.getDisplayCountry(locale) + ": " + locale.getDisplayLanguage(locale));
		}
	}
