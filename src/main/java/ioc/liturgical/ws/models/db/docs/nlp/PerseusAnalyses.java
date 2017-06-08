package ioc.liturgical.ws.models.db.docs.nlp;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

import net.ages.alwb.utils.core.datastores.json.models.AbstractModel;

public class PerseusAnalyses extends AbstractModel {

	@Expose public String token = "";
	@Expose public List<PerseusAnalysis> analyses = new ArrayList<PerseusAnalysis>();
	
	public PerseusAnalyses(
			String token
			) {
		super();
		this.token = token.trim();
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	public void addAnalysis(PerseusAnalysis analysis) {
		this.analyses.add(analysis);
	}

	public List<PerseusAnalysis> getAnalyses() {
		return analyses;
	}

	public void setAnalyses(List<PerseusAnalysis> analyses) {
		this.analyses = analyses;
	}

}
