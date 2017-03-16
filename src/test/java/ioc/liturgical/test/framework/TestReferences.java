package ioc.liturgical.test.framework;

import java.util.ArrayList;
import java.util.List;

import ioc.liturgical.ws.models.db.forms.ReferenceCreateForm;

/**
 * Provides instances of Reference and ReferenceCreateForm
 * @author mac002
 *
 */
public class TestReferences {
	
	int instanceSize = 5;
	
	String baseBibDomain = "gr_gr_ntpt";
	String baseBibTopic = "LUK";
	String baseBibKeyPart1 = "C01:0";
	
	String baseLitDomain = "gr_gr_cog";
	String baseLitTopic = "me.m01.d02";
	String baseLitKeyPart1 = "meVE.Stichera0";
	String baseLitKeyPart2 = ".text";

	List<String> liturgicalIds = new ArrayList<String>();
	List<String> biblicalIds = new ArrayList<String>();
	
	List<ReferenceCreateForm> referenceCreateForms = new ArrayList<ReferenceCreateForm>();

	public TestReferences() {
		initializeLiturgicalIds();
		initializeBiblicalIds();
		initializeReferences();
	}
	
	private void initializeBiblicalIds() {
		for (int i=0; i < instanceSize + 1; i++) {
			biblicalIds.add(
				baseBibDomain + "~" 
			    + baseBibTopic + "~" 
				+ baseBibKeyPart1 
				+ i 
				);
		}
	}
	private void initializeLiturgicalIds() {
		for (int i=0; i < instanceSize + 1; i++) {
			liturgicalIds.add(
				baseLitDomain + "~" 
			    + baseLitTopic + "~" 
				+ baseLitKeyPart1 
				+ i 
				+ baseLitKeyPart2
				);
		}
	}

	private void initializeReferences() {
    	ReferenceCreateForm refCf = null;
		for (int i=0; i < instanceSize+1 ; i++) {
	    	refCf = new ReferenceCreateForm();
	    	refCf.setDomain("en_us_pentiuc");
	    	refCf.setIdReferredByText(liturgicalIds.get(i));
	    	refCf.setIdReferredToText(biblicalIds.get(i));
	    	refCf.setAnc("anc");
	    	refCf.setBib("bib");
	    	refCf.setChr("chr");
	    	refCf.setCul("cul");
	    	refCf.setDev("dev");
	    	refCf.setGen("gen");
	    	refCf.setHge("hge");
	    	refCf.setIsl("isl");
	    	refCf.setJew("jew");
	    	refCf.setLit("lit");
	    	refCf.setLitt("litt");
	    	refCf.setMus("mus");
	    	refCf.setPtes("ptes");
	    	refCf.setSyn("syn");
	    	refCf.setLabels("DELETE:a:b:c:d");
	    	refCf.setTdf("tdf");
	    	refCf.setText("text");
	    	refCf.setTheo("theo");
	    	refCf.setVis("vis");
	    	refCf.setVoc("voc");
	    	referenceCreateForms.add(refCf);
		}
	}
	
	public ReferenceCreateForm getCreateForm(int i) {
		return referenceCreateForms.get(i);
	}
}
