package delete.me;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.joda.time.Instant;
import org.ocmc.ioc.liturgical.schemas.models.db.docs.notes.TextualNote;

public class NoteSequence {
	
	public List<TextualNote> sortNotes(List<TextualNote> list) {
		List<TextualNote> result = new ArrayList<TextualNote>();
		Map<String, List<String>> noteFollowers = new TreeMap<String,List<String>>();
		Map<String, TextualNote> noteMap = new TreeMap<String,TextualNote>();
		
		// create the maps
		for (TextualNote note : list) {
			noteMap.put(note.id, note);
			String follows = "Root";
			if (note.followsNoteId.length() > 0) {
				follows = note.followsNoteId;
			}
			List<String> followers = new ArrayList<String>();
			if (noteFollowers.containsKey(follows)) {
				followers = noteFollowers.get(follows);
			}
			followers.add(note.getId());
			noteFollowers.put(follows, followers);
		}
		// create the sorted result
		List<String> followers = this.getFollowers("Root", noteFollowers);
		for (String id : followers) {
			result.add(noteMap.get(id));
		}
		return result;
	}
	
	// makes a recursive call
	public List<String> getFollowers(String forId, Map<String,List<String>> map) {
		List<String> result = new ArrayList<String>();
		if (map.containsKey(forId)) {
			List<String> followers = map.get(forId);
			if (followers != null) {
				for (String follower : followers) {
					if (! result.contains(follower)) { // just in case this is cyclical instead of a linked list
						result.add(follower);
						List<String> moreFollowers = this.getFollowers(follower, map);
						for (String id : moreFollowers) {
							result.add(id);
						}
					}
				}
			}
		}
		return result;
	}

	public static void main(String[] args) {
		NoteSequence n = new NoteSequence();
		String lib = "en_us_mcolburn";
		String topic = "gr_gr_cog~he.h.m2~VythouAnekalypse.text";
		List<TextualNote> notes = new ArrayList<TextualNote>();
		TextualNote n1 = new TextualNote(lib, topic, "1"); 
		n1.setValue("Note 1 text");
		TextualNote n2 = new TextualNote(lib, topic, "2"); 
		n2.setValue("Note 2 text");
		TextualNote n3 = new TextualNote(lib, topic, "3"); 
		n3.setValue("Note 3 text");
		TextualNote n4 = new TextualNote(lib, topic, "4"); 
		n4.setValue("Note 4 text");

		n1.setFollowsNoteId(n3.getId());
		n2.setFollowsNoteId(n1.getId());
		n4.setFollowsNoteId(n2.getId());

		notes.add(n1);
		notes.add(n2);
		notes.add(n3);
		notes.add(n4);
		for (TextualNote note : notes) {
			System.out.println(note.id + " " + note.value + " follows " + note.followsNoteId);
		}
		System.out.println("Sorted list: ");
		for (TextualNote note : n.sortNotes(notes)) {
			System.out.println(note.id + " " + note.value);
		}
	}

}
