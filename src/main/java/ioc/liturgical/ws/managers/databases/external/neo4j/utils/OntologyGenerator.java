package ioc.liturgical.ws.managers.databases.external.neo4j.utils;

import java.util.ArrayList;
import java.util.List;

import org.ocmc.ioc.liturgical.schemas.constants.CENTURIES;
import org.ocmc.ioc.liturgical.schemas.constants.GENDERS;
import org.ocmc.ioc.liturgical.schemas.models.db.docs.ontology.Animal;
import org.ocmc.ioc.liturgical.schemas.models.db.docs.ontology.Being;
import org.ocmc.ioc.liturgical.schemas.models.db.docs.ontology.Concept;
import org.ocmc.ioc.liturgical.schemas.models.db.docs.ontology.Event;
import org.ocmc.ioc.liturgical.schemas.models.db.docs.ontology.God;
import org.ocmc.ioc.liturgical.schemas.models.db.docs.ontology.Group;
import org.ocmc.ioc.liturgical.schemas.models.db.docs.ontology.Human;
import org.ocmc.ioc.liturgical.schemas.models.db.docs.ontology.Mystery;
import org.ocmc.ioc.liturgical.schemas.models.db.docs.ontology.Object;
import org.ocmc.ioc.liturgical.schemas.models.db.docs.ontology.Place;
import org.ocmc.ioc.liturgical.schemas.models.db.docs.ontology.Plant;
import org.ocmc.ioc.liturgical.schemas.models.db.docs.ontology.Role;
import org.ocmc.ioc.liturgical.schemas.models.db.links.LinkRefersToAnimal;
import org.ocmc.ioc.liturgical.schemas.models.db.links.LinkRefersToBeing;
import org.ocmc.ioc.liturgical.schemas.models.db.links.LinkRefersToBiblicalText;
import org.ocmc.ioc.liturgical.schemas.models.db.links.LinkRefersToEvent;
import org.ocmc.ioc.liturgical.schemas.models.db.links.LinkRefersToGod;
import org.ocmc.ioc.liturgical.schemas.models.db.links.LinkRefersToHuman;
import org.ocmc.ioc.liturgical.schemas.models.db.links.LinkRefersToMystery;
import org.ocmc.ioc.liturgical.schemas.models.db.links.LinkRefersToPlace;
import org.ocmc.ioc.liturgical.schemas.models.supers.LTKDbOntologyEntry;
import org.ocmc.ioc.liturgical.schemas.models.supers.LTKLink;

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
	 * Today the Master stands in the Jordan. * 
	 * He is baptized in the waters by the holy Forerunner. 
	 * The Father testifies from on high: 
	 * This is my Son, my Beloved! 
	 * The Spirit is revealed in a strange visit, 
	 * descending as a dove upon Him.
	 * 
	 * The greek (source) text id = "gr_gr_cog~me.m01.d10~meMA.Kathisma11.text"
	 * 
	 * It makes the following references:
	 * 
	 * God:
	 * 		GodTheFather, GodTheSon, and GodTheHolySpirit
	 * Animals:
	 *     Dove
	 * Beings:
	 * 		ArchangelGabriel, ArchangelMichael
	 * Biblical Text: Matthew, Mark, Luke, and John, which all refer to the baptism of Christ.
	 * Events:
	 * 		BaptismOfChrist
	 * Humans:
	 * 		JesusChrist
	 * 		JohnTheForerunner
	 * Location:
	 *    RiverJordan
	 * 
	 * We will create relationships to represent this information.
	 */
	private void generateRelationships() {
		// id = library, topic, key, where...
		//   library = a domain
		String library = "en_sys_ontology"; 
		//   topic = the starting ID (node from which the relationship starts)
		String startId = "gr_gr_cog~me.m01.d10~meMA.Kathisma11.text"; 
		//   key = string literals below
		
		// God
		links.add(new LinkRefersToGod(library, startId, "en_sys_ontology~God~GodTheFather"));
		links.add(new LinkRefersToGod(library, startId, "en_sys_ontology~God~GodTheSon"));
		links.add(new LinkRefersToGod(library, startId, "en_sys_ontology~God~GodTheHolySpirit"));

		// Animals
		links.add(new LinkRefersToAnimal(library, startId, "en_sys_ontology~Animal~Dove"));
		// Biblical Texts
		links.add(new LinkRefersToBiblicalText(library, startId, "gr_gr_ntpt~MAT~C03:13"));
		links.add(new LinkRefersToBiblicalText(library, startId, "gr_gr_ntpt~LUK~C03:21"));
		links.add(new LinkRefersToBiblicalText(library, startId, "gr_gr_ntpt~MAR~C01:09"));
		links.add(new LinkRefersToBiblicalText(library, startId, "gr_gr_ntpt~JOH~C01:32"));
		// Events
		links.add(new LinkRefersToEvent(library, startId, "en_sys_ontology~Event~BaptismOfChrist"));
		links.add(new LinkRefersToEvent(library, startId, "en_sys_ontology~Event~TheophanyAtBaptismOfChrist"));
		// Humans
		links.add(new LinkRefersToHuman(library, startId, "en_sys_ontology~Human~JesusChrist"));
		links.add(new LinkRefersToHuman(library, startId, "en_sys_ontology~Human~JohnTheForerunner"));
		// Locations
		links.add(new LinkRefersToPlace(library, startId, "en_sys_ontology~Place~RiverJordan"));
		// Mysteries
		links.add(new LinkRefersToMystery(library, startId, "en_sys_ontology~Mystery~Baptism"));
	}
	
	private void generateEntries() {
		// God
		entries.add(new God("God"));
		entries.add(new God("GodTheFather", "God the Father"));
		entries.add(new God("GodTheSon", "God the Son"));
		entries.add(new God("GodTheHolySpirit", "God the Holy Spirit"));
		// Animals
			entries.add(new Animal("Dove"));
			entries.add(new Animal("Serpent"));
		// Beings
			entries.add(new Being("GabrielTheArchangel", "Gabriel the Archangel"));
			entries.add(new Being("MichaelTheArchangel", "Michael the Archangel"));
			entries.add(new Being("Satan"));
			entries.add(new Being("Angels"));
			entries.add(new Being("Powers"));
			entries.add(new Being("Cherubim"));
			entries.add(new Being("Seraphim"));
		// Concepts
			entries.add(new Concept("Fasting"));
			entries.add(new Concept("ForgivenessOfSins", "Forgiveness of Sins"));
			entries.add(new Concept("HolyTrinity", "Holy Trinity"));
			entries.add(new Concept("Law"));
			entries.add(new Concept("Prayer"));
			entries.add(new Concept("Repentence"));
			entries.add(new Concept("Salvation"));
		// Events
			Event event = new Event("BaptismOfChrist", "Baptism of Christ");
			event.setStartCentury(CENTURIES.AD01);
			entries.add(event);
			event = new Event("BirthOfChrist", "Birth of Christ");
			event.setStartCentury(CENTURIES.AD01);
			event.setEndCentury(CENTURIES.AD01);
			entries.add(event);
			event = new Event("TheophanyAtBaptismOfChrist", "Theophany at the baptism of Christ");
			event.setStartCentury(CENTURIES.AD01);
			event.setEndCentury(CENTURIES.AD01);
			entries.add(event);
			event = new Event("BetrayalOfChrist", "Betrayal of Christ");
			event.setStartCentury(CENTURIES.AD01);
			event.setEndCentury(CENTURIES.AD01);
			entries.add(event);
			event = new Event("FirstCouncilOfNicea", "First Council of Nicea");
			event.setStartCentury(CENTURIES.AD04);
			event.setEndCentury(CENTURIES.AD04);
			event.addTag("ecumenical");
			event.addTag("council");
			event.setDescription("Repudiated Arianism, adopted the Nicene Creed");
			entries.add(event);
			event = new Event("FirstCouncilOfConstantinople", "First Council of Constantinople");
			event.setStartCentury(CENTURIES.AD04);
			event.setEndCentury(CENTURIES.AD04);
			event.addTag("ecumenical");
			event.addTag("council");
			event.setDescription("Revised the Nicene Creed into the present form used in the Eastern and Oriental Orthodox churches");
			entries.add(event);
			event = new Event("CouncilOfEphesus", "Council of Ephesus");
			event.setStartCentury(CENTURIES.AD05);
			event.setEndCentury(CENTURIES.AD05);
			event.addTag("ecumenical");
			event.addTag("council");
			event.setDescription("Repudiated Nestorianism, proclaimed the Virgin Mary as the Mother of God (Greek, Θεοτόκος)");
			entries.add(event);
			event = new Event("CouncilOfCalcedon", "Council of Calcedon");
			event.setStartCentury(CENTURIES.AD05);
			event.setEndCentury(CENTURIES.AD05);
			event.addTag("ecumenical");
			event.addTag("council");
			event.setDescription("Repudiated the Eutychian doctrine of Monophysitism, described and delineated the two natures of Christ, human and divine; adopted the Chalcedonian Creed. This and all following councils are not recognized by Oriental Orthodox Communion.");
			entries.add(event);
			event = new Event("SecondCouncilOfConstantinople", "Second Council of Constantinople");
			event.setStartCentury(CENTURIES.AD06);
			event.setEndCentury(CENTURIES.AD06);
			event.addTag("ecumenical");
			event.addTag("council");
			event.setDescription("Reaffirmed decisions and doctrines explicated by previous Councils, condemned new Arian, Nestorian, and Monophysite writings.");
			entries.add(event);
			event = new Event("ThirdCouncilOfConstantinople", "Third Council of Constantinople");
			event.setStartCentury(CENTURIES.AD07);
			event.setEndCentury(CENTURIES.AD07);
			event.addTag("ecumenical");
			event.addTag("council");
			event.setDescription("Repudiated Monothelitism, affirmed that Christ had both human and Divine wills.");
			entries.add(event);
			event = new Event("SecondCouncilOfNicea", "Second Council of Nicea");
			event.setStartCentury(CENTURIES.AD08);
			event.setEndCentury(CENTURIES.AD08);
			event.addTag("ecumenical");
			event.addTag("council");
			event.setDescription("Restoration of the veneration of icons and end of the first iconoclasm.");
			entries.add(event);
			event = new Event("CrucifixionOfChrist", "Crucifixion of Christ");
			event.setStartCentury(CENTURIES.AD01);
			event.setEndCentury(CENTURIES.AD01);
			entries.add(event);
			event = new Event("ResurrectionOfChrist", "Resurrection of Christ");
			event.setStartCentury(CENTURIES.AD01);
			event.setEndCentury(CENTURIES.AD01);
			entries.add(event);
		// Groups
			entries.add(new Group("CouncilOfTheFathers", "Council of the Fathers"));
			entries.add(new Group("Egyptians"));
			entries.add(new Group("Gentiles"));
			entries.add(new Group("Jews"));
			entries.add(new Group("Pharisees"));
			entries.add(new Group("Sadducees"));
			entries.add(new Group("Sanhedrin"));
			entries.add(new Group("Scribes"));
			entries.add(new Group("TheChurch", "The Church"));
		// Humans
			Human human = new Human("JesusChrist", "Jesus Christ");
			human.setGender(GENDERS.male);
			entries.add(human);
			human = new Human("MaryMotherOfJesusChrist", "Mary the Mother of Jesus Christ");
			human.setGender(GENDERS.female);
			entries.add(human);
			human = new Human("JohnTheForerunner", "John the Forerunner");
			human.setGender(GENDERS.male);
			entries.add(human);
			human = new Human("SaintPeterTheApostle", "Saint Peter the Apostle");
			human.setGender(GENDERS.male);
			entries.add(human);
			human = new Human("SaintJamesTheApostle", "Saint James the Apostle");
			human.setGender(GENDERS.male);
			entries.add(human);
			human = new Human("SaintJohnTheTheologion", "Saint John the Theologion");
			human.setGender(GENDERS.male);
			entries.add(human);
			human = new Human("SaintPaulTheApostle", "Saint Paul the Apostle");
			human.setGender(GENDERS.male);
			entries.add(human);
			human = new Human("JudasTheBetrayer", "Judas the Betrayer");
			human.setGender(GENDERS.male);
			entries.add(human);
		// Mysteries
			entries.add(new Mystery("Baptism", "Baptism (Το Άγιο Βάπτισμα )"));
			entries.add(new Mystery("Chrismation", "Chrismation (Το Χρίσμα)"));
			entries.add(new Mystery("Confession", "Confession (Η Εξομολόγηση / Μετάνοια)"));
			entries.add(new Mystery("HolyCommunion", "Holy Communion (Η Θεία Εχαριστία)"));
			entries.add(new Mystery("HolyMatrimony", "Holy Matrimony (Ο Γάμος)"));
			entries.add(new Mystery("HolyOrders", "Holy Orders / Ordination (Η Ιερωσύνη)"));
			entries.add(new Mystery("Unction", "Unction (Το ευχέλαιο)"));
		// Objects
			entries.add(new Object("TheHolyBread", "The Holy Bread"));
			entries.add(new Object("TheHolyCup", "The Holy Cup"));
			entries.add(new Object("TheHolyCross", "The Holy Cross"));
		// Places
			entries.add(new Place("RiverJordan", "River Jordan"));
			entries.add(new Place("Jerusalem"));
			entries.add(new Place("MountOfOlives", "Mount of Olives"));
			entries.add(new Place("Egypt"));
			entries.add(new Place("Bethlehem"));
			entries.add(new Place("Nazereth"));
			entries.add(new Place("Galilee"));
			entries.add(new Place("SeaOfGalilee", "Sea of Galilee"));
			entries.add(new Place("Capernaum"));
		// Plants
			entries.add(new Plant("Olive"));
			entries.add(new Plant("Oak"));
		// Roles
			entries.add(new Role("Apostle"));
			entries.add(new Role("Bishop"));
			entries.add(new Role("Bride"));
			entries.add(new Role("Brother (Spiritual)"));
			entries.add(new Role("Brother (Biological)"));
			entries.add(new Role("Deacon"));
			entries.add(new Role("Disciple"));
			entries.add(new Role("Evangelist"));
			entries.add(new Role("Father (Spiritual)"));
			entries.add(new Role("Father (Biological)"));
			entries.add(new Role("Groom"));
			entries.add(new Role("Hierarch"));
			entries.add(new Role("Husband"));
			entries.add(new Role("Lay Person"));
			entries.add(new Role("Messiah"));
			entries.add(new Role("Mother (Spiritual)"));
			entries.add(new Role("Mother (Biological)"));
			entries.add(new Role("Myrrh Bearer"));
			entries.add(new Role("Presbyter"));
			entries.add(new Role("Patriarch (Spiritual)"));
			entries.add(new Role("Patriarch (Biological)"));
			entries.add(new Role("Priest"));
			entries.add(new Role("Prophet"));
			entries.add(new Role("Psalmist"));
			entries.add(new Role("Reader"));
			entries.add(new Role("Savior"));
			entries.add(new Role("Singer"));
			entries.add(new Role("Sister (Spiritual)"));
			entries.add(new Role("Sister (Biological)"));
			entries.add(new Role("Teacher"));
			entries.add(new Role("Theologian"));
			entries.add(new Role("Wife"));
			entries.add(new Role("Writer"));
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
