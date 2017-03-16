package ioc.liturgical.ws.models.db.docs;

import com.google.gson.annotations.Expose;

import ioc.liturgical.ws.annotations.UiWidget;
import ioc.liturgical.ws.constants.Constants;
import ioc.liturgical.ws.constants.DB_TOPICS;
import ioc.liturgical.ws.models.db.forms.ReferenceCreateForm;
import net.ages.alwb.utils.core.datastores.json.models.AbstractModel;

import com.github.reinert.jjschema.Attributes;

/**
 * This class provides a POJO for use in web forms to create or update a domain
 * @author mac002
 *
 */
@Attributes(title = "Reference", description = "A reference is a doc that records information about a reference made in a text to something else.  For example, a liturgical text might be a hymn that refers to a person, place, or event, e.g. in the Bible.")
public class Reference extends AbstractModel {
	
	@Expose String Id = "";

	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(required = false, description = "Notes on the Text")
	@Expose String text = "";

	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(required = false, description = "Vocabulary (Lexical Items)")
	@Expose String voc = "";

	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(required = false, description = "Literary Devices")
	@Expose String dev = "";

	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(required = false, description = "Literary Genre")
	@Expose String gen = "";

	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(required = false, description = "History and Geography")
	@Expose String hge = "";

	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(required = false, description = "Ancient Texts")
	@Expose String anc = "";

	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(required = false, description = "Ancient Cultures")
	@Expose String cul = "";

	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(required = false, description = "Biblical Intertextuality")
	@Expose String bib = "";

	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(required = false, description = "Comparison of Versions / Synoptic Reading")
	@Expose String syn = "";

	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(required = false, description = "Peritestamental Literature")
	@Expose String ptes = "";

	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(required = false, description = "Jewish Tradition")
	@Expose String jew = "";

	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(required = false, description = "Christian Tradition")
	@Expose String chr = "";

	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(required = false, description = "Liturgy")
	@Expose String lit = "";

	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(required = false, description = "Theology")
	@Expose String theo = "";

	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(required = false, description = "Islam")
	@Expose String isl = "";

	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(required = false, description = "Literature")
	@Expose String litt = "";

	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(required = false, description = "Visual Arts")
	@Expose String vis = "";

	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(required = false, description = "Music")
	@Expose String mus = "";

	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(required = false, description = "Theater, Dance, and Film")
	@Expose String tdf = "";


	@UiWidget(Constants.UI_WIDGET_RADIO)
	@Attributes(required = true, description = "Is this domain active?")
	@Expose boolean active = true;

	@Attributes(readonly=true, description="The user ID of the person who created it.")
	@Expose String createdBy = "";

	@Attributes(readonly=true, description="The date/time when it was created.")
	@Expose String createdWhen = "";
	
	@Attributes(readonly=true, description="The user ID of the person who last modified it.")
	@Expose String modifiedBy = "";

	@Attributes(readonly=true, description="The date/time when it was last modified.")
	@Expose String modifiedWhen = "";
	

	public Reference() {
		super();
		this.serialVersionUID = 1.1;
	}

	public Reference(ReferenceCreateForm form) {
		super();
		this.serialVersionUID = 1.1;
		this.setId(form.getId());
		this.setAnc(form.getAnc());
		this.setBib(form.getBib());
		this.setChr(form.getChr());
		this.setCul(form.getCul());
		this.setDev(form.getDev());
		this.setGen(form.getGen());
		this.setHge(form.getHge());
		this.setIsl(form.getIsl());
		this.setJew(form.getJew());
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


	public boolean isActive() {
		return active;
	}


	public void setActive(boolean active) {
		this.active = active;
	}


	public String getCreatedBy() {
		return createdBy;
	}


	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}


	public String getCreatedWhen() {
		return createdWhen;
	}


	public void setCreatedWhen(String createdWhen) {
		this.createdWhen = createdWhen;
	}


	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getModifiedWhen() {
		return modifiedWhen;
	}

	public void setModifiedWhen(String modifiedWhen) {
		this.modifiedWhen = modifiedWhen;
	}


	public String getId() {
		return Id;
	}


	public void setId(String id) {
		Id = id;
	}

}
