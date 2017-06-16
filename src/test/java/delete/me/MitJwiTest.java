package delete.me;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.text.WordUtils;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.ILexFile;
import edu.mit.jwi.item.IPointer;
import edu.mit.jwi.item.ISenseEntry;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.ISynsetID;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;

public class MitJwiTest {

	public static void main(String[] args) {
		try {
			// construct the URL to the Wordnet dictionary directory
			URL url = MitJwiTest.class.getResource("/dict");
			
			// construct the dictionary object and open it
			IDictionary dict = new Dictionary(url);
			dict.open();
			for (POS pos : POS.values()) {
				IIndexWord idxWord = dict.getIndexWord("orange", pos);
				dumpIndexWord(idxWord);
				
				if (idxWord != null) {
					System.out.println("idx Id = " + idxWord.getID());
					System.out.println("idx lemma = " + idxWord.getLemma());
					for (IWordID id : idxWord.getWordIDs()) {
						dumpWord(id, dict);
					}
				}
			}
//			for (IPointer ptr : getIPointers(dict)) {
//				String name = ptr.getName();
//				String[] parts = name.split(" ");
//				StringBuffer sb = new StringBuffer();
//				
////				for (String p : parts) {
////					if (! p.equals("-")) {
////						p = p.toLowerCase();
////						p = WordUtils.capitalize(p);
////						sb.append(p);
////					}
////				}
//				String relName = ptr.getName()
//						.replaceAll(" -", "")
//						.replaceAll(" ", "_")
//						.toUpperCase();
//				sb = new StringBuffer();
//				sb.append("\t, ");
//				sb.append(relName);
//				sb.append("(");
//				sb.append("\n\t\t\"");
//				sb.append(relName);
//				sb.append("\"");
//				sb.append("\n\t\t, \"");
//				sb.append(ptr.getSymbol());
//				sb.append("\"");
//				sb.append("\n\t)");
//				System.out.println(sb.toString());
//			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Get the unique set of types of pointers
	 * @param dict
	 * @return
	 */
	public static List<IPointer> getIPointers(IDictionary dict) {
		List<IPointer> pointers = new ArrayList<IPointer>();
		for (POS p : POS.values()) {
			Iterator<ISynset> i = dict.getSynsetIterator(p);
			while(i.hasNext()) {
				ISynset syn = i.next();
				for (IPointer ptr : syn.getRelatedMap().keySet()) {
					if (! pointers.contains(ptr)) {
						pointers.add(ptr);
					}
				}
			}
		}
		return pointers;
	}
	
	public static void dumpWord(IWordID id, IDictionary dict) {
		IWord word = dict.getWord(id);
		System.out.println("Word Id = " + id);
		System.out.println("Word senseKey nbr = " + dict.getSenseEntry(word.getSenseKey()).getSenseNumber());
		System.out.println("Lemma = " + word.getLemma());
		System.out.println("POS = " + word.getPOS());
		System.out.println("\n");
		dumpSynset(word.getLemma(), word.getSynset());
	}

	public static void dumpSynset(String lemma, ISynset synset) {
		System.out.println("Synset ID = " + synset.getID());
		System.out.println("LexFile nbr = " + synset.getLexicalFile().getNumber());
		System.out.println("LexFile name = " + synset.getLexicalFile().getName());
		System.out.println("LexFile desc = " + synset.getLexicalFile().getDescription());
		System.out.println("Gloss = " + synset.getGloss());
		System.out.println("Synonyms = " + getSynsetWords(lemma, synset.getWords()));
		System.out.println("Type = " + synset.getType());
		Map<IPointer,List<ISynsetID>> map = synset.getRelatedMap();
		for (IPointer i : map.keySet()) {
			System.out.println(i.getName());
		}
	
	}
	public static String getSynsetWords(String lemma, List<IWord> list) {
		StringBuffer result = new StringBuffer();
		int s = list.size();
		for (int i=0; i < s; i++) {
			if (! list.get(i).getLemma().equals(lemma)) {
				if (result.length() > 0) {
					result.append(", ");
				}
				result.append(list.get(i).getLemma());
			}
		}
		return result.toString();
	}
	
	public static void dumpIndexWord(IIndexWord i) {
		try {
			
		} catch (Exception e) {
			
		}
		System.out.println("Index Word Dump - start");
		System.out.println(i.getID());
		System.out.println(i.getLemma());
		System.out.println(i.getTagSenseCount());
		System.out.println(i.getID());
		System.out.println(i.getPOS());
		System.out.println("Index Word Dump - end");
	}
	
	public static void dumpWord(IWord i) {
		System.out.println("Word Dump - start");
		System.out.println(i.getLemma());
		System.out.println(i.getLexicalID());
		System.out.println(i.getAdjectiveMarker());
		System.out.println(i.getID());
		System.out.println(i.getPOS());
		System.out.println(i.getSenseKey());
		System.out.println(i.getSynset());
		System.out.println("Word Dump - end");
	}
	
	public static void dumpSenseEntry(ISenseEntry i) {
		System.out.println("SenseEntry Dump - start");
		System.out.println(i.getSenseNumber());
		System.out.println(i.getTagCount());
		System.out.println(i.getPOS());
		System.out.println(i.getSenseKey());
		System.out.println("SenseEntry Dump - start");
	}
	
	public static void dumpSynset(ISynset i) {
		System.out.println("Synset Dump - start");
		System.out.println(i.getGloss());
		System.out.println(i.getType());
		System.out.println(i.isAdjectiveHead());	
		System.out.println(i.isAdjectiveSatellite());
		System.out.println(i.getID());
		System.out.println(i.getPOS());
		System.out.println("Synset Dump - start");
		dumpLexFile(i.getLexicalFile());
	}
	
	public static void dumpPointer(IPointer i ) {
		System.out.println("Pointer Dump - start");
		System.out.println(i.getName());
		System.out.println(i.getSymbol());
		System.out.println("Pointer Dump - end");
	}
	
	public static void dumpLexFile(ILexFile i) {
		System.out.println("LexFile Dump - start");
		System.out.println(i.getName());
		System.out.println(i.getDescription());
		System.out.println(i.getNumber());
		System.out.println(i.getPOS());
		System.out.println("LexFile Dump - end");
	}

}
