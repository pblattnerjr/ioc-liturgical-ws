package ioc.liturgical.ws.models.db.links;

import com.google.gson.annotations.Expose;

import ioc.liturgical.ws.annotations.UiWidget;
import ioc.liturgical.ws.constants.Constants;
import ioc.liturgical.ws.constants.ONTOLOGY_TOPICS;
import ioc.liturgical.ws.constants.RELATIONSHIP_TYPES;
import ioc.liturgical.ws.models.db.forms.LinkRefersToBiblicalTextCreateForm;
import ioc.liturgical.ws.models.db.supers.LTKLink;

import com.github.reinert.jjschema.Attributes;

/**
 * @author mac002
 *
 */
@Attributes(title = "Reference to Biblical Text", description = "A reference is a doc that records information about a reference made in a text to a Biblical text.  For example, a liturgical text might be a hymn that refers to a person, place, or event in the Bible.")
public class LinkRefersToBiblicalText extends LTKLink {

	private static double serialVersion = 1.1;
	private static String schema = LinkRefersToBiblicalText.class.getSimpleName();
	private static RELATIONSHIP_TYPES type = RELATIONSHIP_TYPES.REFERS_TO_BIBLICAL_TEXT;
	private static ONTOLOGY_TOPICS ontoTopic = ONTOLOGY_TOPICS.TEXT_BIBLICAL;

	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(required = false, description = "Word or phrase that makes the reference")
	@Expose public String referredByPhrase = "";

	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(required = false, description = "Word or phrase that is referred to")
	@Expose public String referredToPhrase = "";

	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(required = false, description = "Notes on the Text")
	@Expose public String text = "";

	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(required = false, description = "Vocabulary (Lexical Items)")
	@Expose public String voc = "";

	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(required = false, description = "Literary Devices")
	@Expose public String dev = "";

	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(required = false, description = "Literary Genre")
	@Expose public String gen = "";

	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(required = false, description = "History and Geography")
	@Expose public String hge = "";

	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(required = false, description = "Ancient Texts")
	@Expose public String anc = "";

	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(required = false, description = "Ancient Cultures")
	@Expose public String cul = "";

	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(required = false, description = "Biblical Intertextuality")
	@Expose public String bib = "";

	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(required = false, description = "Comparison of Versions / Synoptic Reading")
	@Expose public String syn = "";

	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(required = false, description = "Peritestamental Literature")
	@Expose public String ptes = "";

	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(required = false, description = "Jewish Tradition")
	@Expose public String jew = "";

	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(required = false, description = "Christian Tradition")
	@Expose public String chr = "";

	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(required = false, description = "Liturgy")
	@Expose public String lit = "";

	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(required = false, description = "Theology")
	@Expose public String theo = "";

	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(required = false, description = "Islam")
	@Expose public String isl = "";

	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(required = false, description = "Literature")
	@Expose public String litt = "";

	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(required = false, description = "Visual Arts")
	@Expose public String vis = "";

	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(required = false, description = "Music")
	@Expose public String mus = "";

	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(required = false, description = "Theater, Dance, and Film")
	@Expose public String tdf = "";
	
	public LinkRefersToBiblicalText(
			String library
			, String topic
			, String key
			) {
		super(
				library
				, topic
				, key
				, LinkRefersToBiblicalText.schema
				, LinkRefersToBiblicalText.serialVersion
				, LinkRefersToBiblicalText.type
				, LinkRefersToBiblicalText.ontoTopic
				);
	}

	public LinkRefersToBiblicalText(LinkRefersToBiblicalTextCreateForm form) {
		super(
				form.getLibrary()
				, form.getTopic()
				, form.getKey()
				, LinkRefersToBiblicalText.schema
				, LinkRefersToBiblicalText.serialVersion
				, LinkRefersToBiblicalText.type
				, LinkRefersToBiblicalText.ontoTopic
				);
		this.setReferredByPhrase(form.getReferredByPhrase());
		this.setReferredToPhrase(form.getReferredToPhrase());
		this.setAnc(form.getAnc());
		this.setBib(form.getBib());
		this.setChr(form.getChr());
		this.setCul(form.getCul());
		this.setDev(form.getDev());
		this.setGen(form.getGen());
		this.setHge(form.getHge());
		this.setIsl(form.getIsl());
		this.setJew(form.getJew());
		this.setTags(form.getTags());
		this.setLit(form.getLit());
		this.setLitt(form.getLitt());
		this.setMus(form.getMus());
		this.setPtes(form.getPtes());
		this.setSyn(form.getSyn());
		this.setTdf(form.getTdf());
		this.setText(form.getText());
		this.setTheo(form.getTheo());
		this.setVis(form.getVis());
		this.setVoc(form.getVoc());
	}
    
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getVoc() {
		return voc;
	}


	public void setVoc(String voc) {
		this.voc = voc;
	}


	public String getDev() {
		return dev;
	}


	public void setDev(String dev) {
		this.dev = dev;
	}


	public String getGen() {
		return gen;
	}


	public void setGen(String gen) {
		this.gen = gen;
	}


	public String getHge() {
		return hge;
	}


	public void setHge(String hge) {
		this.hge = hge;
	}


	public String getAnc() {
		return anc;
	}


	public void setAnc(String anc) {
		this.anc = anc;
	}


	public String getCul() {
		return cul;
	}


	public void setCul(String cul) {
		this.cul = cul;
	}


	public String getBib() {
		return bib;
	}


	public void setBib(String bib) {
		this.bib = bib;
	}


	public String getSyn() {
		return syn;
	}


	public void setSyn(String syn) {
		this.syn = syn;
	}


	public String getPtes() {
		return ptes;
	}


	public void setPtes(String ptes) {
		this.ptes = ptes;
	}


	public String getJew() {
		return jew;
	}


	public void setJew(String jew) {
		this.jew = jew;
	}


	public String getChr() {
		return chr;
	}


	public void setChr(String chr) {
		this.chr = chr;
	}


	public String getLit() {
		return lit;
	}


	public void setLit(String lit) {
		this.lit = lit;
	}


	public String getTheo() {
		return theo;
	}


	public void setTheo(String theo) {
		this.theo = theo;
	}


	public String getIsl() {
		return isl;
	}


	public void setIsl(String isl) {
		this.isl = isl;
	}


	public String getLitt() {
		return litt;
	}


	public void setLitt(String litt) {
		this.litt = litt;
	}


	public String getVis() {
		return vis;
	}


	public void setVis(String vis) {
		this.vis = vis;
	}


	public String getMus() {
		return mus;
	}


	public void setMus(String mus) {
		this.mus = mus;
	}


	public String getTdf() {
		return tdf;
	}


	public void setTdf(String tdf) {
		this.tdf = tdf;
	}

	public String getReferredByPhrase() {
		return referredByPhrase;
	}

	public void setReferredByPhrase(String referredByPhrase) {
		this.referredByPhrase = referredByPhrase;
	}

	public String getReferredToPhrase() {
		return referredToPhrase;
	}

	public void setReferredToPhrase(String referredToPhrase) {
		this.referredToPhrase = referredToPhrase;
	}

}
