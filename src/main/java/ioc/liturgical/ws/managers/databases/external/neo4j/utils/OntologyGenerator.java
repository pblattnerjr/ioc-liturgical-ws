package ioc.liturgical.ws.managers.databases.external.neo4j.utils;

import java.util.ArrayList;
import java.util.List;

import ioc.liturgical.ws.models.db.docs.Animal;
import ioc.liturgical.ws.models.db.docs.Being;
import ioc.liturgical.ws.models.db.docs.Concept;
import ioc.liturgical.ws.models.db.docs.Event;
import ioc.liturgical.ws.models.db.docs.Group;
import ioc.liturgical.ws.models.db.docs.Human;
import ioc.liturgical.ws.models.db.docs.Object;
import ioc.liturgical.ws.models.db.docs.Place;
import ioc.liturgical.ws.models.db.docs.Plant;
import ioc.liturgical.ws.models.db.links.LinkRefersToBeing;
import ioc.liturgical.ws.models.db.links.LinkRefersToBiblicalText;
import ioc.liturgical.ws.models.db.links.LinkRefersToEvent;
import ioc.liturgical.ws.models.db.links.LinkRefersToGroup;
import ioc.liturgical.ws.models.db.links.LinkRefersToHuman;
import ioc.liturgical.ws.models.db.supers.LTK;
import ioc.liturgical.ws.models.db.supers.LTKDbOntologyEntry;
import ioc.liturgical.ws.models.db.supers.LTKLink;

/**
 * When the database does not have any docs of type OntoRoot (ontology entries)
 * use this class to get a set of objects to add to the database.
 * @author mac002
 *
 */
public class OntologyGenerator {
	private List<LTKDbOntologyEntry> entries = new ArrayList<LTKDbOntologyEntry>();
	private List<LTKLink> links = new ArrayList<LTKLink>();
	
	public OntologyGenerator() {
		generateEntries();
		generateRelationships();
	}
	
	/**
	 * The following hymn is used to seed the ontology:
	 * 
	 * Holding in contempt the monument to dread Herod's wickedness, 
	 * come, let us believers go towards Jordan, 
	 * that we may see Christ the Redeemer 
	 * being baptised in the flesh 
	 * by the Forerunner in its streams. 
	 * All creation blesses him as it gives him glory to the ages.
	 * 
	 * The greek (source) text id = "gr_gr_cog~he.h.m6~StilinKakiasAntitheou.text"
	 * 
	 * It makes the following references:
	 * 
	 * Beings:
	 * 		GodTheSon
	 * Biblical Text: Matthew, Mark, Luke, and John, which all refer to the baptism of Christ.
	 * Events:
	 * 		BaptismOfChrist
	 * Groups:
	 * 		TheChurch
	 * Humans:
	 * 		JesusChrist
	 * 		JohnTheForerunner
	 * 
	 * We will create relationships to represent this information.
	 */
	private void generateRelationships() {
		// id = library, topic, key, where...
		//   library = a domain
		String library = "en_us_ocmc"; 
		//   topic = the starting ID (node from which the relationship starts)
		String startId = "gr_gr_cog~he.h.m6~StilinKakiasAntitheou.text"; 
		//   key = string literals below
		
		// Beings
		links.add(new LinkRefersToBeing(library, startId, "ontology~Being~GodTheSon"));
		// Biblical Texts
		links.add(new LinkRefersToBiblicalText(library, startId, "gr_gr_ntpt~MAT~C03:13"));
		links.add(new LinkRefersToBiblicalText(library, startId, "gr_gr_ntpt~LUK~C03:21"));
		links.add(new LinkRefersToBiblicalText(library, startId, "gr_gr_ntpt~MAR~C01:09"));
		links.add(new LinkRefersToBiblicalText(library, startId, "gr_gr_ntpt~JOH~C01:32"));
		// Events
		links.add(new LinkRefersToEvent(library, startId, "ontology~Event~BaptismOfChrist"));
		// Groups
		links.add(new LinkRefersToGroup(library, startId, "ontology~Group~TheChurch"));
		// Humans
		links.add(new LinkRefersToHuman(library, startId, "ontology~Human~JesusChrist"));
		links.add(new LinkRefersToHuman(library, startId, "ontology~Human~JohnTheForerunner"));
	}
	
	private void generateEntries() {
		// Animals
			entries.add(new Animal("Dove"));
			entries.add(new Animal("Serpent"));
		// Beings
			entries.add(new Being("God"));
			entries.add(new Being("GodTheFather"));
			entries.add(new Being("GodTheSon"));
			entries.add(new Being("GodTheHolySpirit"));
			entries.add(new Being("GabrielTheArchangel"));
			entries.add(new Being("MichaelTheArchangel"));
			entries.add(new Being("Satan"));
			entries.add(new Being("Angels"));
			entries.add(new Being("Powers"));
			entries.add(new Being("Cherubim"));
			entries.add(new Being("Seraphim"));
		// Concepts
			entries.add(new Concept("ForgivenessOfSins"));
			entries.add(new Concept("HolyTrinity"));
			entries.add(new Concept("Repentence"));
			entries.add(new Concept("Salvation"));
		// Events
			entries.add(new Event("BaptismOfChrist"));
			entries.add(new Event("BirthOfChrist"));
			entries.add(new Event("BetrayalOfChrist"));
			entries.add(new Event("ThirdCouncilOfTheFathers"));
			entries.add(new Event("FourthCouncilOfTheFathers"));
			entries.add(new Event("CrucifixionOfChrist"));
			entries.add(new Event("ResurrectionOfChrist"));
		// Groups
			entries.add(new Group("CouncilOfTheFathers"));
			entries.add(new Group("Egyptians"));
			entries.add(new Group("Gentiles"));
			entries.add(new Group("Jews"));
			entries.add(new Group("Pharisees"));
			entries.add(new Group("Sadducees"));
			entries.add(new Group("Sanhedrin"));
			entries.add(new Group("Scribes"));
			entries.add(new Group("TheChurch"));
		// Humans
			entries.add(new Human("JesusChrist"));
			entries.add( new Human("MaryMotherOfJesusChrist"));
			entries.add(new Human("JohnTheForerunner"));
			entries.add(new Human("SaintPeterTheApostle"));
			entries.add(new Human("SaintJamesTheApostle"));
			entries.add(new Human("SaintJohnTheApostle"));
			entries.add(new Human("SaintPaulTheApostle"));
			entries.add(new Human("JudasTheBetrayer"));
		// Objects
			entries.add(new Object("TheHolyBread"));
			entries.add(new Object("TheHolyCup"));
			entries.add(new Object("TheHolyCross"));
		// Places
			entries.add(new Place("RiverJordan"));
			entries.add(new Place("Jerusalem"));
			entries.add(new Place("MountOfOlives"));
			entries.add(new Place("Egypt"));
			entries.add(new Place("Bethlehem"));
			entries.add(new Place("Nazereth"));
			entries.add(new Place("Galilee"));
			entries.add(new Place("SeaOfGalilee"));
			entries.add(new Place("Capernaum"));
		// Plants
			entries.add(new Plant("Olive"));
			entries.add(new Plant("Oak"));
	}

	public List<LTKDbOntologyEntry> getEntries() {
		return entries;
	}

	public void setEntries(List<LTKDbOntologyEntry> entries) {
		this.entries = entries;
	}

	public List<LTKLink> getLinks() {
		return links;
	}

	public void setLinks(List<LTKLink> links) {
		this.links = links;
	}

}
