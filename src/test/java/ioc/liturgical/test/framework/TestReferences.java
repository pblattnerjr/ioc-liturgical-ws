package ioc.liturgical.test.framework;

import java.util.ArrayList;
import java.util.List;

import ioc.liturgical.ws.models.db.forms.LinkRefersToBiblicalTextCreateForm;
import ioc.liturgical.ws.models.ws.db.Domain;

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
	
	List<LinkRefersToBiblicalTextCreateForm> referenceCreateForms = new ArrayList<LinkRefersToBiblicalTextCreateForm>();

	public TestReferences() {
		initializeLiturgicalIds();
		initializeBiblicalIds();
		initializeReferences();
	}
	
	private void initializeBiblicalIds() {
		for (int i=0; i < instanceSize + 1; i++) {
			String id = 
					baseBibDomain + "~" 
						    + baseBibTopic + "~" 
							+ baseBibKeyPart1 
							+ (i+1) 
							;
			biblicalIds.add(id);
		}
	}
	
	private void initializeLiturgicalIds() {
		for (int i=0; i < instanceSize + 1; i++) {
			String id = 
							baseLitDomain + "~" 
						    + baseLitTopic + "~" 
							+ baseLitKeyPart1 
							+ (i+1) 
							+ baseLitKeyPart2
			;
			liturgicalIds.add(id);
		}
	}

	private void initializeReferences() {
    	LinkRefersToBiblicalTextCreateForm refCf = null;
		for (int i=0; i < instanceSize+1 ; i++) {
	    	refCf = new LinkRefersToBiblicalTextCreateForm(
	    			"en_us_pentiuc"
	    			, liturgicalIds.get(i)
	    			, biblicalIds.get(i)
	    			);
	    	refCf.setReferredByPhrase("the referred by phrase");
	    	refCf.setReferredToPhrase("the referred to phrase");
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
	    	refCf.addLabel("DELETE");
	    	refCf.addLabel("a");
	    	refCf.addLabel("b");
	    	refCf.addLabel("c");
	    	refCf.addLabel("d");
	    	refCf.setTdf("tdf");
	    	refCf.setText("text");
	    	refCf.setTheo("theo");
	    	refCf.setVis("vis");
	    	refCf.setVoc("voc");
	    	referenceCreateForms.add(refCf);
		}
	}
	
	public LinkRefersToBiblicalTextCreateForm getCreateForm(int i) {
		return referenceCreateForms.get(i);
	}
	
}
