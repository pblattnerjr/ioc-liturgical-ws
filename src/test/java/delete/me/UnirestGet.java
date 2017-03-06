package delete.me;

import java.io.InputStream;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class UnirestGet {

	public static void main(String[] args) {
		String url = "http://localhost:7474/browser/scripts/9b91d422.components.js";
		try {
			HttpResponse<InputStream> responseGet = Unirest.get(url)
			        .header("content-type", "*/*")
			        .asBinary(); 		
			} catch (UnirestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
}
