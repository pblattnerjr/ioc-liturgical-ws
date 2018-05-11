package net.ages.alwb.utils.transformers.adapters;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.ocmc.ioc.liturgical.utils.ErrorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import ioc.liturgical.ws.constants.Constants;
import ioc.liturgical.ws.managers.databases.external.neo4j.ExternalDbManager;
import net.ages.alwb.utils.transformers.adapters.models.AgesIndexTableData;
import net.ages.alwb.utils.transformers.adapters.models.AgesIndexTableRowData;

/**
 * Reads the index of available sacraments and services from
 * the AGES Initiatives website, and converts it to json
 * data that can be rendered by a React BootstrapTable
 * @author mac002
 *
 */
public class AgesWebsiteIndexToReactTableData {
	private static final Logger logger = LoggerFactory.getLogger(AgesWebsiteIndexToReactTableData.class);
	
	private String baseUrl = "http://www.agesinitiatives.com/dcs/public/dcs/";
	private String booksPath = "h/b/";
	private String servicesIndex = baseUrl + "servicesindex.html";
	private String booksIndex = baseUrl + "booksindex.html";
	private String jsonservicesIndex = baseUrl + "servicesindex.json";
	private String olwBaseUrl = "http://www.agesinitiatives.com/dcs/books/dcs/";
	private String olwBooksUrl = olwBaseUrl + "booksindex.html";
	private String agesOcmcBaseUrl = "http://www.agesinitiatives.com/dcs/ocmc/dcs/";
	private String agesOcmcIndex = "customindex.html";
	private String readingsIndex = agesOcmcBaseUrl + agesOcmcIndex;
	private String theophanyUrl = Constants.LIML_URL + Constants.LIML_STATIC + "theophany.html";
	private String basilUrl = Constants.LIML_URL + Constants.LIML_STATIC + "bk.liturgy.basil.html";
	private List<AgesIndexTableRowData> additionalAgesBookRows = new ArrayList<AgesIndexTableRowData>();
	private boolean printPretty = false;

	public AgesWebsiteIndexToReactTableData() {
		this.addAdditionalUrls();
	}
	public AgesWebsiteIndexToReactTableData(boolean printPretty) {
		this.printPretty = printPretty;
		this.addAdditionalUrls();
	}
	
	private void addAdditionalUrls() {
		String base = this.baseUrl + this.booksPath;
		AgesIndexTableRowData row = new AgesIndexTableRowData(printPretty);
		row.setDayOfWeek("any");
		row.setType("Service of Preparation for Holy Communion (Ἀκολουθία τῆς Θείας Μεταλήψεως)");
		row.setDate("any");
		row.setUrl(base + "ho/s21/gr-en/index.html");		
		this.additionalAgesBookRows.add(row);
		row = new AgesIndexTableRowData(printPretty);
		row.setDayOfWeek("any");
		row.setType("Service of Small Paraklesis (Ἀκολουθία τοῦ Μικροῦ Παρακλητικοῦ Κανόνος)");
		row.setDate("any");
		row.setUrl(base + "ho/s23/gr-en/index.html");		
		this.additionalAgesBookRows.add(row);
		row = new AgesIndexTableRowData(printPretty);
		row.setDayOfWeek("any");
		row.setType("Service of Great Paraklesis (Ἀκολουθία τοῦ Μεγάλου Παρακλητικοῦ Κανόνος)");
		row.setDate("any");
		row.setUrl(base + "ho/s24/gr-en/index.html");		
		this.additionalAgesBookRows.add(row);
	}
	
	public AgesIndexTableData  toReactTableDataFromOlwBooksHtml() throws Exception {
		AgesIndexTableData result = new AgesIndexTableData(printPretty);
		Document readingsIndexDoc = null;
		Connection readingsIndexConnection = null;
		try {
			readingsIndexConnection = Jsoup.connect(olwBooksUrl);
			readingsIndexDoc = readingsIndexConnection.timeout(60*1000).maxBodySize(0).get();
			Elements books = readingsIndexDoc.select("a.index-books-file-link");
			for (Element bookAnchor : books) {
				String href = bookAnchor.attr("href");
				// 0  1  2    3       4
				// h/b/me/m01/d01/gr-en/index.html = menaion
				// h/b/pe/d00p/gr-en/index.html = Pentecostarion
				// h/b/sy/m01/d03_olw/gr-en/index.html = Synaxarion
				// h/b/tr/d046/gr-en/index.html = Triodion
				String[] hrefParts = href.split("/");
				if (href.startsWith("h/b/")) {
					String bookType = hrefParts[2];
					AgesIndexTableRowData row = new AgesIndexTableRowData(printPretty);
					row.setDayOfWeek("any");
					row.setUrl(olwBaseUrl + href);
					switch (bookType) {
					// TODO
					case ("ho"): {
						String [] dateParts = hrefParts[4].split("_");
						String date = hrefParts[3] + "/" + dateParts[0];
						row.setDate(date);
						row.setType("Horologion (τό ῾Ωρλόγιον)");
						result.addRow(row);
						break;
					}
					case ("me"): {
						String [] dateParts = hrefParts[4].split("_");
						String date = hrefParts[3] + "/" + dateParts[0];
						row.setDate(date);
						row.setType("Menaion (τά Μηναῖα)");
						result.addRow(row);
						break;
					}
					// TODO
					case ("oc"): {
						String [] dateParts = hrefParts[4].split("_");
						String date = hrefParts[3] + "/" + dateParts[0];
						row.setDate(date);
						row.setType("Octoechos (ἡ Παρακλητική)");
						result.addRow(row);
						break;
					}
					case ("pe"): {
						String [] dateParts = hrefParts[3].split("_");
						String date = dateParts[0];
						if (date.equals("d00p")) {
							date = "d000 Pascha (Το Πάσχα)";
						}
						row.setDate(date);
						row.setType("Pentecostarion (τό Πεντηκοστάριον)");
						result.addRow(row);
						break;
					}
					// TODO
					case ("psalter"): {
						String [] dateParts = hrefParts[4].split("_");
						String date = hrefParts[3] + "/" + dateParts[0];
						row.setDate(date);
						row.setType("Psalterion (τό Ψαλτήριον)");
						result.addRow(row);
						break;
					}
					case ("sy"): {
						String [] dateParts = hrefParts[4].split("_");
						String date = hrefParts[3] + "/" + dateParts[0];
						row.setDate(date);
						row.setType("Synaxarion (τό Συναξάριον)");
						result.addRow(row);
						break;
					}
					case ("tr"): {
						String [] dateParts = hrefParts[3].split("_");
						String date = dateParts[0];
						row.setDate(date);
						row.setType("Triodion (τό Τριῴδιον)");
						result.addRow(row);
						break;
					}
					default: {
						logger.info("Missing switch for OLW Daily Readings book " + bookType);
					}
					}
				}
			}
			AgesIndexTableRowData theophany = new AgesIndexTableRowData(printPretty);
			theophany.setDayOfWeek("any");
			theophany.setType("Theophany");
			theophany.setDate("m01/d06");
			theophany.setUrl(theophanyUrl);
			result.addRow(theophany);
			AgesIndexTableRowData basil = new AgesIndexTableRowData(printPretty);
			basil.setDayOfWeek("any");
			basil.setType("Divine Liturgy of St. Basil");
			basil.setDate("any");
			basil.setUrl(basilUrl);
			result.addRow(basil);
			for (AgesIndexTableRowData row : this.additionalAgesBookRows) {
				result.addRow(row);
			}
		} catch (Exception e) {
			ErrorUtils.report(logger, e);
		}
		return result;
	}	
	
	public AgesIndexTableData  toReactTableDataFromDailyReadingHtml() throws Exception {
		AgesIndexTableData result = new AgesIndexTableData(printPretty);
		Document readingsIndexDoc = null;
		Connection readingsIndexConnection = null;
		try {
			readingsIndexConnection = Jsoup.connect(readingsIndex);
			readingsIndexDoc = readingsIndexConnection.timeout(60*1000).maxBodySize(0).get();
			Elements months = readingsIndexDoc.select("a.index-custom-file-link");
			for (Element monthAnchor : months) {
				String href = monthAnchor.attr("href");
				String[] hrefParts = href.split("/");
				if (hrefParts.length == 5) {
					if (hrefParts[0].equals("h")) {
						if (hrefParts[3].equals("gr-en") || hrefParts[3].equals("gr-spa")) {
							String [] dateParts = hrefParts[2].split("_");
							// TODO: we need to compute the year or it needs to be included in the html
							String year = dateParts[2];
							String month = dateParts[3];
							String date = year + "/"  + month;
							AgesIndexTableRowData row = new AgesIndexTableRowData(printPretty);
							row.setDate(date);
							row.setDayOfWeek("all");
							row.setType("daily readings");
							row.setUrl(agesOcmcBaseUrl + href);
							result.addRow(row);
						}
					}
				}
			}
		} catch (Exception e) {
			ErrorUtils.report(logger, e);
		}
		return result;
	}	
	public AgesIndexTableData  toReactTableDataFromHtml() throws Exception {
		AgesIndexTableData result = new AgesIndexTableData(printPretty);
		Document booksIndexDoc = null;
		Document servicesIndexDoc = null;
		Document dayIndexDoc = null;
		Document readingsIndexDoc = null;
		Connection datesIndexConnection = null;
		Connection servicesIndexConnection = null;
		Connection booksIndexConnection = null;
		Connection readingsIndexConnection = null;
		try {
			datesIndexConnection = Jsoup.connect(servicesIndex);
			servicesIndexDoc = datesIndexConnection.timeout(60*1000).maxBodySize(0).get();
			Elements days = servicesIndexDoc.select("a.index-day-link");
			for (Element day : days) {
				String href = day.attr("href");
				String year = href.substring(8, 12);
				String month = href.substring(12, 14);
				String monthDay = href.substring(14,16);
				String date = year + "/" + month + "/" + monthDay;
				String dayOfWeekName = day.text().trim().split("-")[1];
				servicesIndexConnection = Jsoup.connect(baseUrl + href);
				dayIndexDoc = servicesIndexConnection.timeout(60*1000).maxBodySize(0).get();
				Elements services = dayIndexDoc.select("a.index-file-link");
				for (Element service : services) {
					String fileHref = service.attr("href");
					String[] hrefParts = fileHref.split("/");
					if (hrefParts.length == 8) {
						if (hrefParts[0].equals("h")) {
							if (hrefParts[6].equals("gr-en")) {
								AgesIndexTableRowData row = new AgesIndexTableRowData(printPretty);
								row.setDate(date);
								row.setDayOfWeek(dayOfWeekName);
								row.setType(hrefParts[5]);
								row.setUrl(baseUrl + fileHref);
								result.addRow(row);
							}
						}
					}
				}
			}
			booksIndexConnection = Jsoup.connect(booksIndex);
			booksIndexDoc = booksIndexConnection.timeout(60*1000).maxBodySize(0).get();
			Element menu = booksIndexDoc.getElementById("dcs_tree");
			Elements anchors = menu.select("li > ul > li > ul > li");
			for (Element anchor : anchors) {
				if (anchor.hasAttr("dcslink")) {
					String fileHref =  anchor.attr("dcslink");
					String[] hrefParts = fileHref.split("/");
					if (hrefParts.length == 5) {
						if (hrefParts[0].equals("h")) {
							if (hrefParts[3].equals("gr-en")) {
								AgesIndexTableRowData row = new AgesIndexTableRowData(printPretty);
								row.setDate("any");
								row.setDayOfWeek("any");
								row.setType(hrefParts[2]);
								row.setUrl(baseUrl + fileHref);
								result.addRow(row);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			ErrorUtils.report(logger, e);
		}
		return result;
	}

	public AgesIndexTableData  toReactTableDataFromJson() throws Exception {
		AgesIndexTableData result = new AgesIndexTableData(printPretty);
		Connection serviceIndexConnection = null;
		Connection booksIndexConnection = null;
		Document booksIndexDoc = null;
		try {
			serviceIndexConnection = Jsoup.connect(this.jsonservicesIndex);
			String s  = serviceIndexConnection
					.timeout(60*1000)
					.maxBodySize(0)
					.ignoreContentType(true)
					.execute()
					.body();
			JsonParser p = new JsonParser();
			JsonObject o = p.parse(s).getAsJsonObject();
			// years
			JsonArray years = o.get("years").getAsJsonArray();
			for (JsonElement year : years) {
				for (Entry<String, JsonElement> yearEntry : year.getAsJsonObject().entrySet()) {
					// months
					JsonArray months = yearEntry.getValue().getAsJsonArray();
					for (JsonElement month : months) {
						for (Entry<String, JsonElement> monthEntry : month.getAsJsonObject().entrySet()) {
							// days
							JsonArray days = monthEntry.getValue().getAsJsonArray();
							for (JsonElement day : days) {
								for (Entry<String, JsonElement> dayEntry : day.getAsJsonObject().entrySet()) {
									// service types
									JsonArray serviceTypes = dayEntry.getValue().getAsJsonArray();
									for (JsonElement serviceType : serviceTypes) {
										for (Entry<String, JsonElement> serviceTypeEntry : serviceType.getAsJsonObject().entrySet()) {
											// service language combinations
											JsonArray serviceLanguages = serviceTypeEntry.getValue().getAsJsonArray();
											for (JsonElement serviceLanguage : serviceLanguages) {
												for (Entry<String, JsonElement> serviceLanguageEntry : serviceLanguage.getAsJsonObject().entrySet()) {
													JsonObject serviceLanguageEntryValue = serviceLanguageEntry.getValue().getAsJsonArray().get(0).getAsJsonObject();
													String fileHref = serviceLanguageEntryValue.get("href").getAsString();
													String type = serviceLanguageEntryValue.get("type").getAsString();
													if (serviceLanguageEntry.getKey().startsWith("GR-EN") && type.startsWith("Text/Music")) {
														String theYear = fileHref.substring(4, 8);
														String theMonth = fileHref.substring(9, 11);
														String monthDay = fileHref.substring(12,14);
														String date = theYear + "/" + theMonth + "/" + monthDay;
														String dayOfWeekName = dayEntry.getKey().trim().split("-")[1];
														String [] hrefParts = fileHref.split("/");
														if (hrefParts.length == 8) {
																	AgesIndexTableRowData row = new AgesIndexTableRowData(printPretty);
																	row.setDate(date);
																	row.setDayOfWeek(dayOfWeekName);
																	row.setType(serviceTypeEntry.getKey());
																	row.setUrl(baseUrl + fileHref);
																	result.addRow(row);
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
			booksIndexConnection = Jsoup.connect(booksIndex);
			booksIndexDoc = booksIndexConnection.timeout(60*1000).maxBodySize(0).get();
			Element menu = booksIndexDoc.getElementById("dcs_tree");
			Elements anchors = menu.select("li > ul > li > ul > li");
			for (Element anchor : anchors) {
				if (anchor.hasAttr("dcslink")) {
					String fileHref =  anchor.attr("dcslink");
					String[] hrefParts = fileHref.split("/");
					if (hrefParts.length == 5) {
						if (hrefParts[0].equals("h")) {
							if (hrefParts[3].equals("gr-en")) {
								AgesIndexTableRowData row = new AgesIndexTableRowData(printPretty);
								row.setDate("any");
								row.setDayOfWeek("any");
								row.setType(hrefParts[2]);
								row.setUrl(baseUrl + fileHref);
								result.addRow(row);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return result;
	}
}
