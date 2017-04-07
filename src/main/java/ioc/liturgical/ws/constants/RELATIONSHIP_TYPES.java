package ioc.liturgical.ws.constants;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.google.gson.JsonObject;

/**
 * Types of Relationships in the Neo4j Database
 * @author mac002
 *
 */
public enum RELATIONSHIP_TYPES {
	REFERS_TO_ANIMAL(
			"REFERS_TO_ANIMAL"
			, "Doc makes reference to an animal."
		)
	, REFERS_TO_BEING(
			"REFERS_TO_BEING"
			, "Doc makes reference to a being."
			)
	, REFERS_TO_BIBLICAL_TEXT(
			"REFERS_TO_BIBLICAL_TEXT"
			, "Doc makes reference to Biblical text."
			)
	, REFERS_TO_CONCEPT(
			"REFERS_TO_CONCEPT"
			, "Doc makes reference to an abstract concept."
			)
	, REFERS_TO_EVENT(
			"REFERS_TO_EVENT"
			, "Doc makes reference to an event."
			)
	, REFERS_TO_GROUP(
			"REFERS_TO_GROUP"
			, "Doc makes reference to a group of people."
			)
	, REFERS_TO_OBJECT(
			"REFERS_TO_OBJECT"
			, "Doc makes reference to an object."
			)
	, REFERS_TO_HUMAN(
			"REFERS_TO_HUMAN"
			, "Doc makes reference to a human being."
			)
	, REFERS_TO_PLACE(
			"REFERS_TO_PLACE"
			, "Doc makes reference to a place."
			)
	, REFERS_TO_PLANT(
			"REFERS_TO_PLANT"
			, "Doc makes reference to a plant."
			)
	, REFERS_TO_ROLE(
			"REFERS_TO_ROLE"
			, "Doc makes reference to a role."
	 )
	;
	public String typename;
	public String description;
	
	private RELATIONSHIP_TYPES(
			String typename
			, String description
			) {
		this.typename = typename;
		this.description = description;
	}

}
