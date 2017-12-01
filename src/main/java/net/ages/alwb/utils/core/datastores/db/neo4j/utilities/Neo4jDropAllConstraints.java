package net.ages.alwb.utils.core.datastores.db.neo4j.utilities;

import ioc.liturgical.ws.managers.databases.external.neo4j.utils.Neo4jConnectionManager;
import org.ocmc.ioc.liturgical.schemas.models.ws.response.RequestStatus;

public class Neo4jDropAllConstraints {
	public static void main(String[] args) {
		Neo4jConnectionManager neo4jManager = null;
		String uid = System.getenv("uid");
		String pwd = System.getenv("pwd");
		  neo4jManager = new Neo4jConnectionManager(
				  "localhost"
				  , uid
				  , pwd
				  , false
				  );
		  RequestStatus s = neo4jManager.dropAllConstraints();
		  System.out.println(s.getCode() + " " + s.getDeveloperMessage());
		  s = neo4jManager.removeLabel("OntoRoot");
		  System.out.println(s.getCode() + " " + s.getDeveloperMessage());
		  s = neo4jManager.addRootLabel();
		  System.out.println(s.getCode() + " " + s.getDeveloperMessage());
	}

}
