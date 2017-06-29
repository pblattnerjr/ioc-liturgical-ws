package ioc.liturgical.ws.models.ws.response.column.editor;

import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.Test;

public class KeyArraysCollectionBuilderTest {

	@Test
	public void test() {
		KeyArraysCollectionBuilder b = new KeyArraysCollectionBuilder(
				"gr_gr_cog"
				, "bk_memorial"
				, true
				);
		KeyArraysCollection collection = new KeyArraysCollection();
		try {
			b.addTemplateKey("eu.memorial~euMEM.title", "","");
			b.addTemplateKey("eu.funeral~euFUN.Key0600.title", "","");
			b.addTemplateKey("misc~Mode5", "","");
			b.addTemplateKey("ho.s03~hoMA.FuneralEvlog1.text", "","");
			b.addTemplateKey("ho.s03~hoMA.FuneralEvlogVerse.text","","");
			b.addTemplateKey("ho.s03~hoMA.FuneralEvlog1.text", "","");
			b.addTemplateKey("ho.s03~hoMA.FuneralEvlogVerse.text", "","");
			b.addTemplateKey("ho.s03~hoMA.FuneralEvlog5.text", "","");
			b.addTemplateKey("ho.s03~hoMA.FuneralEvlogVerse.text", "","");
			b.addTemplateKey("ho.s03~hoMA.FuneralEvlog4.text", "","");
			b.addTemplateKey("ho.s03~hoMA.FuneralEvlogVerse.text", "","");
			b.addTemplateKey("ho.s03~hoMA.FuneralEvlog6.text", "","");
			b.addTemplateKey("prayers~DoxaPatri.text", "","");
			b.addTemplateKey("ho.s03~hoMA.FuneralEvlog7.text", "","");
			b.addTemplateKey("prayers~KaiNynKaiAei.text", "","");
			b.addTemplateKey("ho.s03~hoMA.FuneralEvlog8.text", "","");
			b.addTemplateKey("prayers~Allilouia3Doxa.text", "","");
			b.addTemplateKey("rubrical~Thrice", "","");
			b.addTemplateKey("titles~Kontakion", "","");
			b.addTemplateKey("misc~Mode8", "","");
			b.addTemplateKey("tr.d014~trMA.Kontakion.text", "","");
			b.addTemplateKey("misc~Mode4", "","");
			b.addTemplateKey("oc.m4.d7~ocVE.Aposticha4.text", "","");
			b.addTemplateKey("oc.m4.d7~ocMA.Lauds4.text", "","");
			b.addTemplateKey("prayers~DoxaPatri.text", "","");
			b.addTemplateKey("eu.funeral~euFUN.Key0103.text", "","");
			b.addTemplateKey("prayers~KaiNynKaiAei.text", "","");
			b.addTemplateKey("oc.m4.d5~ocMA.AposTheotokion.text", "","");
			b.addTemplateKey("misc~Mode3", "","");
			b.addTemplateKey("prayers~res16", "","");
			b.addTemplateKey("rubrical","Thrice", "","");
			collection = b.getCollection();
		} catch (MissingSeparatorException e) {
			System.out.println(e.getMessage());
		}
		System.out.println(collection.toJsonString());
		assertTrue(collection.getAbout().getTemplateKeyCount() == 31);
	}

}
