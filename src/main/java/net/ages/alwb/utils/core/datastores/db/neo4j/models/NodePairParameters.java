package net.ages.alwb.utils.core.datastores.db.neo4j.models;

import com.google.gson.annotations.Expose;

import net.ages.alwb.utils.core.datastores.json.models.AbstractModel;

public class NodePairParameters extends AbstractModel {
	@Expose NodePairs parameters = new NodePairs();
	
	public NodePairParameters (NodePairs pairs) {
		super();
		this.parameters = pairs;
	}

	public NodePairs getParameters() {
		return parameters;
	}

	public void setParameters(NodePairs parameters) {
		this.parameters = parameters;
	}
}
