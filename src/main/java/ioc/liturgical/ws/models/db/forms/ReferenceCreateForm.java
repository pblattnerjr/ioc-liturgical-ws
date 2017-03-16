package ioc.liturgical.ws.models.db.forms;

import com.google.gson.annotations.Expose;

import ioc.liturgical.ws.annotations.UiWidget;
import ioc.liturgical.ws.constants.Constants;
import ioc.liturgical.ws.constants.DB_TOPICS;
import net.ages.alwb.utils.core.datastores.json.models.AbstractModel;

import com.github.reinert.jjschema.Attributes;

/**
 * This class provides a POJO for use in web forms to create or update a domain
 * @author mac002
 *
 */
@Attributes(title = "Reference", description = "A reference is a doc that records information about a reference made in a text to something else.  For example, a liturgical text might be a hymn that refers to a person, place, or event, e.g. in the Bible.")
public class ReferenceCreateForm extends AbstractModel {
	
	@Attributes(required = true, description = "The Domain for this Reference.")
	@Expose String domain = "";

	@Attributes(required = true, description = "The database ID of the doc containing the text that makes a reference to another text.")
	@Expose String idReferredByText = "";

	@Attributes(required = true, description = "The database ID of the doc containing the text that is referred to by another text.")
	@Expose String idReferredToText = "";

	@Attributes(required = true, readonly=true, description = "Search Labels")
	@Expose String labels = "";
	
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

	public ReferenceCreateForm() {
		super();
		this.serialVersionUID = 1.1;
	}
	
	public static void main(String[] args) {
		ReferenceCreateForm form = new ReferenceCreateForm();
		form.setPrettyPrint(true);
		System.out.println(form.toJsonUiSchemaObject().toString());
		System.out.println(form.toJsonSchemaObject().toString());
		System.out.println(form.toJsonObject().toString());
	}

	public String getIdReferredByText() {
		return idReferredByText;
	}

	public void setIdReferredByText(String idReferredByText) {
		this.idReferredByText = idReferredByText;
	}

	public String getIdReferredToText() {
		return idReferredToText;
	}

	public void setIdReferredToText(String idReferredToText) {
		this.idReferredToText = idReferredToText;
	}

	public String getLabels() {
		return labels;
	}

	public void setLabels(String labels) {
		this.labels = labels;
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

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}
	
	public String getId() {
		return this.domain + "|" + DB_TOPICS.REFERENCES.topic + "|" +  this.getIdReferredByText() + "~" + this.getIdReferredToText();
	}

	/**
	 * Returns the parts of the ID delimited by a forward slash.
	 * The path returned will also start with a forward slash.
	 * @return
	 */
	public String getIdAsPath() {
		return "/" + this.domain + "/" + DB_TOPICS.REFERENCES.topic + "/" +  this.getIdReferredByText() + "~" + this.getIdReferredToText();
	}

	public String getKey() {
		return this.getIdReferredByText() + "~" + this.getIdReferredToText();
	}

}
