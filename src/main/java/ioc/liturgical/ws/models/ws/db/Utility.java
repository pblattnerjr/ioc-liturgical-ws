package ioc.liturgical.ws.models.ws.db;

import java.util.ArrayList;
import java.util.List;

import com.github.reinert.jjschema.Attributes;
import com.google.gson.annotations.Expose;

import ioc.liturgical.ws.annotations.UiWidget;
import ioc.liturgical.ws.constants.Constants;
import ioc.liturgical.ws.constants.UTILITIES;
import ioc.liturgical.ws.models.ws.supers.WsDbAbstractModel;

@Attributes(title = "Utility", description = "Information about a Utility that can be run")
public class Utility extends WsDbAbstractModel {
	
	@Attributes(required = true, readonly=true, description = "The name of this utility")
	@Expose public String name = "";

	@UiWidget(Constants.UI_WIDGET_TEXTAREA)
	@Attributes(required = true, readonly=true, description = "Description of what the utility does.")
	@Expose public String description = "";

	@Attributes(required = true, readonly=true, description = "Utilities that will run before this one.")
	@Expose public List<UTILITIES> preprocessors = new ArrayList<UTILITIES>();

	@Attributes(required = true, readonly=true, description = "Utilities that will run after this one.")
	@Expose public List<UTILITIES> postprocessors = new ArrayList<UTILITIES>();

	public Utility() {
		super();
		this.serialVersionUID = 1.1;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	public void putPreProcessor(UTILITIES p) {
		this.preprocessors.add(p);
	}

	public void putPostProcessor(UTILITIES p) {
		this.postprocessors.add(p);
	}

	public List<UTILITIES> getPreprocessors() {
		return preprocessors;
	}

	public void setPreprocessors(List<UTILITIES> preprocessors) {
		this.preprocessors = preprocessors;
	}

	public List<UTILITIES> getPostprocessors() {
		return postprocessors;
	}

	public void setPostprocessors(List<UTILITIES> postprocessors) {
		this.postprocessors = postprocessors;
	}
}
