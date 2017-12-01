package net.ages.alwb.utils.core.datastores.db.neo4j.utilities;

import ioc.liturgical.ws.managers.databases.external.neo4j.utils.Neo4jConnectionManager;
import org.ocmc.ioc.liturgical.schemas.models.ws.response.RequestStatus;

public class Neo4jRenameLabel {
	public static void main(String[] args) {
		String oldLabel = "Critter";
		String newLabel = "Animal";
		Neo4jConnectionManager neo4jManager = null;
		String uid = System.getenv("uid");
		String pwd = System.getenv("pwd");
		  neo4jManager = new Neo4jConnectionManager(
				  "localhost"
				  , uid
				  , pwd
				  , false
				  );
		  RequestStatus s = neo4jManager.renameLabel(oldLabel, newLabel);
		  System.out.println(s.getCode() + " " + s.getDeveloperMessage());
	}

}
