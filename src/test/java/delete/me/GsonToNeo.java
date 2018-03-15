package delete.me;

import org.ocmc.ioc.liturgical.schemas.models.db.docs.ontology.TextBiblical;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class GsonToNeo {
	
	public static void main (String [] args) {
		TextBiblical t = new TextBiblical("gr_gr_ntpt", "MAT", "C002:003");
		JsonObject o = t.toJsonObject();
		o.remove("property");
		System.out.println(o.toString());
	}

}
