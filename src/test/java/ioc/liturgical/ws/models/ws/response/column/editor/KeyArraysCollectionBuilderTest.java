package ioc.liturgical.ws.models.ws.response.column.editor;

import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.Test;

public class KeyArraysCollectionBuilderTest {

	@Test
	public void test() {
		KeyArraysCollectionBuilder b = new KeyArraysCollectionBuilder("bk_memorial", true);
		KeyArraysCollection collection = new KeyArraysCollection();
		try {
			b.addTemplateKey("eu.memorial__euMEM.title");
			b.addTemplateKey("eu.funeral__euFUN.Key0600.title");
			b.addTemplateKey("misc__Mode5");
			b.addTemplateKey("ho.s03__hoMA.FuneralEvlog1.text");
			b.addTemplateKey("ho.s03__hoMA.FuneralEvlogVerse.text");
			b.addTemplateKey("ho.s03__hoMA.FuneralEvlog1.text");
			b.addTemplateKey("ho.s03__hoMA.FuneralEvlogVerse.text");
			b.addTemplateKey("ho.s03__hoMA.FuneralEvlog5.text");
			b.addTemplateKey("ho.s03__hoMA.FuneralEvlogVerse.text");
			b.addTemplateKey("ho.s03__hoMA.FuneralEvlog4.text");
			b.addTemplateKey("ho.s03__hoMA.FuneralEvlogVerse.text");
			b.addTemplateKey("ho.s03__hoMA.FuneralEvlog6.text");
			b.addTemplateKey("prayers__DoxaPatri.text");
			b.addTemplateKey("ho.s03__hoMA.FuneralEvlog7.text");
			b.addTemplateKey("prayers__KaiNynKaiAei.text");
			b.addTemplateKey("ho.s03__hoMA.FuneralEvlog8.text");
			b.addTemplateKey("prayers__Allilouia3Doxa.text");
			b.addTemplateKey("rubrical__Thrice");
			b.addTemplateKey("titles__Kontakion");
			b.addTemplateKey("misc__Mode8");
			b.addTemplateKey("tr.d014__trMA.Kontakion.text");
			b.addTemplateKey("misc__Mode4");
			b.addTemplateKey("oc.m4.d7__ocVE.Aposticha4.text");
			b.addTemplateKey("oc.m4.d7__ocMA.Lauds4.text");
			b.addTemplateKey("prayers__DoxaPatri.text");
			b.addTemplateKey("eu.funeral__euFUN.Key0103.text");
			b.addTemplateKey("prayers__KaiNynKaiAei.text");
			b.addTemplateKey("oc.m4.d5__ocMA.AposTheotokion.text");
			b.addTemplateKey("misc__Mode3");
			b.addTemplateKey("prayers__res16");
			b.addTemplateKey("rubrical","Thrice");
			collection = b.getCollection();
		} catch (MissingSeparatorException e) {
			System.out.println(e.getMessage());
		}
		System.out.println(collection.toJsonString());
		assertTrue(collection.getAbout().getTemplateKeyCount() == 31);
	}

}
