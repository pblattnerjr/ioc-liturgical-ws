package delete.me;

import org.ocmc.ioc.liturgical.schemas.constants.TOPICS;
import org.ocmc.ioc.liturgical.schemas.models.db.docs.ontology.TextLiturgical;
import org.ocmc.ioc.liturgical.schemas.models.supers.LTKDb;

public class CypherTest {

	public static String getMergeQuery(LTKDb doc) {
		String result = null;
		try {
			result = new StringBuffer()
					.append("merge (n:")
					.append(TOPICS.ROOT.label)
					.append(" {id: \"")
					.append(doc.getId())
					.append("\" }) set n = {props}  return count(n);")
					.toString();
		} catch (Exception e) {
			
		}
		return result;
	}
	public static void main(String[] args) {
		TextLiturgical doc = new TextLiturgical("en_us_dedes", "actors", "Priest");
		System.out.println(CypherTest.getMergeQuery(doc));
	}

}
