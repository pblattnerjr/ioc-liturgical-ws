package delete.me;

import java.net.MalformedURLException;
import java.net.URL;

import org.ocmc.ioc.liturgical.schemas.constants.ENDPOINTS_DB_API;
import org.ocmc.ioc.liturgical.schemas.constants.HTTP_RESPONSE_CODES;
import org.ocmc.ioc.liturgical.schemas.models.db.stats.LocationLog;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import ioc.liturgical.test.framework.TestConstants;
import ioc.liturgical.test.framework.TestUsers;

public class RestAssuredAsClient {

	public static void main(String[] args) {
		String address = "196.201.214.47";
		URL url;
		try {
			url = new URL("http://ip-api.com/json/" + address);
		    JsonPath json = io.restassured.RestAssured
		    .when()
		    .get(url)
		    .jsonPath()
		    ;
		   String status = json.get("status");
		   if (status.equals("success")) {
			   String countryCode = json.get("countryCode");
			   String country = json.get("country");
			   String region = json.get("region");
			   String regionName = json.get("regionName");
			   String city = json.get("city");
			   LocationLog log = new LocationLog(countryCode, region, city);
			   log.setCountry(country);
			   log.setRegionName(regionName);
			   log.increment();
			   log.setPrettyPrint(true);
			   System.out.println(log.toJsonString());
		   }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
