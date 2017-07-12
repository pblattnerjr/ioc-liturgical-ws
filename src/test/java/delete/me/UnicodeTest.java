package delete.me;

import java.text.Normalizer;

import ioc.liturgical.ws.models.db.docs.ontology.TextLiturgical;
import net.ages.alwb.utils.core.misc.AlwbGeneralUtils;

public class UnicodeTest {
	
	private static String get(String label, String it, int i) {
		return label
				+ "\t" 
				+ it 
				+ " = " 
				+ it.charAt(i) 
				+ " (" 
				+ it.codePointAt(i) 
				+ ")\t " 
				+ it.charAt(i+1) 
				+ " (" 
				+ it.codePointAt(i+1) 
				+ ") "
				;
	}

	public static void main(String[] args) {
		String o = "οἴκου";
		String o2 = "οικου";
		int i = 1;
		String nfc = Normalizer.normalize(o, Normalizer.Form.NFC);
        String nfd = Normalizer.normalize(o, Normalizer.Form.NFD);
        String nfkc = Normalizer.normalize(o, Normalizer.Form.NFKC);
        String nfkd = Normalizer.normalize(o, Normalizer.Form.NFKD);
		System.out.println(get("o",o,i));
		System.out.println(get("o2",o2,i));
		System.out.println(get("nfc",nfc,i));
		System.out.println(get("nfd",nfd,i));
		System.out.println(get("nfkc",nfkc,i));
		System.out.println(get("nfkd",nfkd,i));
		System.out.println(Normalizer.isNormalized(o, Normalizer.Form.NFC));
		System.out.println(Normalizer.isNormalized(nfc, Normalizer.Form.NFC));
		
		String spanish = "Diácono";
		System.out.println(AlwbGeneralUtils.normalize(spanish));
}

}
