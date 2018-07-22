package net.ages.alwb.tasks;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.google.gson.JsonObject;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ioc.liturgical.ws.managers.databases.external.neo4j.ExternalDbManager;

import org.ocmc.ioc.liturgical.schemas.constants.DATA_SOURCES;
import org.ocmc.ioc.liturgical.schemas.models.db.docs.nlp.UDtbSentence;
import org.ocmc.ioc.liturgical.schemas.models.db.docs.nlp.UDtbWord;
import org.ocmc.ioc.liturgical.schemas.models.ws.response.ResultJsonObjectArray;
import org.ocmc.ioc.liturgical.utils.ErrorUtils;
import org.ocmc.ioc.liturgical.utils.FileUtils;
import net.ages.alwb.utils.nlp.utils.PerseusPostagMapper;
import net.ages.alwb.utils.nlp.utils.UDfeatsMapper;

/**
 * Runs a task (separate thread) to read the Perseus Treebank data
 * from Github and write it to the database
 * .
 * @author mac002
 *
 */
public class UdTreebankDataCreateTask implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(UdTreebankDataCreateTask.class);

	private ExternalDbManager manager = null;
	private String requestor = "";
	boolean deleteFirst = true;
	boolean pullFirst = true;
	boolean simulate = true;
	private String label = "";
	private String queryLabel = ":UDtb:";
	private String queryLabelSentence = "UDtbSentence";
	private String queryLabelWord = "UDtbWord";
	private String repoUrl = "https://github.com/UniversalDependencies/UD_Ancient_Greek-Perseus.git";
	private String localRepoPath = "repos";
	private String xmlPath = localRepoPath + "/v2.1/Greek/texts";
	private String filename = "grc_perseus-ud-train.conllu";
	private long breakAtSentCount = -1; // -1 means unlimited
	private DATA_SOURCES dataSource = DATA_SOURCES.GITHUB;
	private UDtbSentence lastSentence = null;
	private UDtbWord lastWord = null;
	private UDtbWord lastRoot = null;
	
	public UdTreebankDataCreateTask (
			ExternalDbManager manager
			, String requestor
			, DATA_SOURCES source
			, boolean pullFirst
			, boolean deleteFirst
			, boolean simulate
			, long numberOfSentences
			) {
		this.manager = manager;
		this.requestor = requestor;
		this.deleteFirst = deleteFirst;
		this.pullFirst = pullFirst;
		this.dataSource = source;
		this.repoUrl = source.gitCloneUrl;
		this.filename = source.trainingFile;
		this.label = source.nodeLabel;
		this.simulate = simulate;
		this.breakAtSentCount = numberOfSentences;
		this.setQueryLabels();
		this.reportParms();
	}

	private void reportParms() {
		logger.info("Running task to load Universal Dependency Treebank information");
		logger.info("repo url = "  + this.repoUrl);
		logger.info("repo file = "  + this.filename);
		logger.info("delete first = "  + this.deleteFirst);
		logger.info("pull first = "  + this.pullFirst);
		logger.info("simulate = "  + this.simulate);
	}
	private void setQueryLabels() {
		try {
			this.queryLabel = this.queryLabel + this.label;
			this.queryLabelSentence = this.queryLabelSentence + this.queryLabel;
			this.queryLabelWord = this.queryLabelWord + this.queryLabel;
		} catch (Exception e) {
			ErrorUtils.report(logger, e);
		}
	}
	
	private void initializeRepository() {
		File f = new File(this.localRepoPath+ "/" + this.label);
		try {
			Git git = null;
			if (f.exists()) {
				try {
					logger.info("Pulling treebank_data from Github...");
					git = Git.open(f);
					git.pull();
					logger.info("Pulled treebank_data from Github to " + f.getAbsolutePath());
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				logger.info("Cloning treebank_data from Github...");
				FileUtils.createDir(f.getAbsolutePath());
				git = Git.cloneRepository()
						  .setURI( repoUrl )
						  .setDirectory( f)
						  .call();
				logger.info("Cloned treebank_data to " + f.getAbsolutePath());
			}
		} catch (InvalidRemoteException e) {
			e.printStackTrace();
		} catch (TransportException e) {
			e.printStackTrace();
		} catch (GitAPIException e) {
			e.printStackTrace();
		}
	}

	private void deleteFirst() {
		ResultJsonObjectArray result = null;

		String query  = "MATCH (n" + this.queryLabel + ") return count(n) as count";
		result = this.manager.getForQuery(
				query
				, false
				, false
				);
		int nodeCount = result.getFirstObject().get("count").getAsInt();
		if (nodeCount == 0) {
			logger.info("No matches found for label " + this.queryLabel + ". So nothing to delete from database.");
		} else {
			logger.info("Deleting " + nodeCount + " " + this.queryLabel + " UDtbSentence and UDtbWord nodes and relationships");
			try {
				while (nodeCount > 0) {
					query = "MATCH (n" 
							+ this.queryLabel 
							+ ") optional MATCH (n" 
							+ this.queryLabel 
							+ ")<-[r]-("
							+ this.queryLabel
							+ ") with n,r limit 10000 delete n, r return count(n) as deletedNodesCount";
					result = this.manager.getForQuery(
							query
							, false
							, false
							);
					int deleted = result.getFirstObject().get("deletedNodesCount").getAsInt();
					nodeCount = nodeCount - deleted;
					logger.info("Deleted " + deleted + ". Remaining node count = " + nodeCount);
				}
				logger.info("Deleted " + result.getValueCount() + " nodes and relationships.");
				ExternalDbManager.neo4jManager.dropConstraint("UtbSentence", "id");
				ExternalDbManager.neo4jManager.dropConstraint("UtbWord", "id");
			} catch (Exception e) {
				ErrorUtils.report(logger, e);
			}
		}
	}
	
	private long load() {
		long count = 0;
		this.findStartingPoint();
		File f = new File(this.localRepoPath + "/" + this.label + "/" + this.filename);
		if (! f.exists()) {
			this.initializeRepository();
		}
		// see format info at http://universaldependencies.org/docs/format.html
		int linecount = 0;
		long tokenCount = 0;
		int sentCount = 0;
		int docCount = 0;
		List<String> depsList = new ArrayList<String>();
		List<String> miscList = new ArrayList<String>();

		UDtbSentence sentence = null;
		Map<String,UDtbWord> words = new TreeMap<String,UDtbWord>();
		
		String docId = "";
		String sentId = "";
		String text = "";
		boolean foundStartingSentence = (this.lastSentence == null);

		for (String line : FileUtils.linesFromFile(f)) {
			boolean processLine = true;
			if (! foundStartingSentence && ! line.startsWith("#")) {
				processLine = false; // use this to fast forward to the sentence we want to start from
			}
			if (line.trim().length() > 0 && processLine) {
				if (line.startsWith("#")) {
					String [] parts = line.split("= ");
					if (line.startsWith("# newdoc")) {
						docId = parts[1].trim();
						docCount++;
					} else if (line.startsWith("# sent_id")) {
						if (sentId.length() > 0) {
							if (foundStartingSentence) {
				    			if (this.deleteFirst) {
					    			this.manager.addLTKDbObject(requestor, sentence.toJsonString());
				    			} else {
					    			this.manager.mergeLTKDbObject(requestor, sentence.toJsonString());
				    			}
				    			this.createRelationships(sentence, words);
								logger.info(" Added " + sentCount + " of 11,475 " + sentence.getId());
				    			words = new TreeMap<String,UDtbWord>();
				    			sentId = "";
				    			text = "";
				    			if (sentCount == breakAtSentCount) {
				    				break;
				    			}
							} else	if (this.lastSentence.getId().equals(sentence.getId())) {
								foundStartingSentence = true;
								logger.info("Found last sentence " + sentCount + " of 11,475 "+ sentence.getId());
							} else {
								logger.info("Skipping " + sentCount + " of 11,475 "+ sentence.getId());
							}
						}
						sentId = parts[1].trim();
						String [] idParts = sentId.split("@");
						if (idParts.length == 2) {
							sentId = idParts[1];
						}
						sentId = sentId.replace(docId, "");
						if (sentId.startsWith("-")) {
							sentId = sentId.substring(1);
						}
						sentence = new UDtbSentence(docId, sentId);
						sentence.setDataSource(this.dataSource);
						sentence.setAdHocLabel(this.label);
						sentence.setValue(text);
						sentCount++;
					} else if (line.startsWith("# source")) {
						String [] sourceParts = line.split("= ");
						if (sourceParts.length == 2) {
							docId = sourceParts[1].trim();
							docId = docId.replaceAll(",", "");
							docId = docId.replaceAll(" ", "");
							docId = this.label + "." + docId;
						} else {
							docId = line;
							logger.info("Could not split source " + line);
						}
						docCount++;
					} else if (line.startsWith("# text")) {
						text = parts[1].trim();
						if (sentence != null) {
							sentence.setValue(text);
						}
					} else {
						System.out.println("Can't identify line type");
						System.out.println(line);
						break;
					}
				} else {
					String [] parts = line.split("\t");
					if (parts.length == 10) {
						tokenCount++;
						String id = parts[0];
						String form = parts[1];
						String lemma = parts[2];
						String upostag = parts[3];
						String xpostag = parts[4];
						String feats = parts[5];
						String head = parts[6];
						String deprel = parts[7];
						String deps = parts[8];
						String misc = parts[9];
						UDtbWord word = new UDtbWord(sentence.getTopic() + "~" + sentence.getKey(), id);
						word.setAdHocLabel(this.label);
						word.setDataSource(this.dataSource);
						if (head.toLowerCase().equals("root") || head.equals("0")) {
							head = "Root";
						}
						word.setDependsOn(head);
						word.setDeps(deps);
						word.setFeats(feats);
						word.setLabel(deprel);
						word.setLemma(lemma);
						word.setMisc(misc);
						word.setPos(upostag);
						word.setToken(form);
						word.setUpostag(upostag);
						word.setXpostag(xpostag);
						words.put(word.getId(), word);
			    		if (xpostag.length() == 9) {
			    			word.setGrammar(PerseusPostagMapper.postagToGrammar(xpostag));
			    			word.setgCase(PerseusPostagMapper.postagCase(xpostag));
			    			word.setGender(PerseusPostagMapper.postagGender(xpostag));
			    			word.setMood(PerseusPostagMapper.postagMood(xpostag));
			    			word.setNumber(PerseusPostagMapper.postagNumber(xpostag));
			    			word.setPerson(PerseusPostagMapper.postagPerson(xpostag));
			    			word.setTense(PerseusPostagMapper.postagTense(xpostag));
			    			word.setVoice(PerseusPostagMapper.postagVoice(xpostag));
			    		} else {
			    			if (feats.length() > 0) {
			    				word.setGrammar(UDfeatsMapper.featsToGrammar(word));
			    			} else {
				    			word.setGrammar(xpostag);
			    			}
			    		}
			    		words.put(word.getId(), word);
			    		if (simulate) {
							word.setPrettyPrint(true);
							System.out.println(word.toJsonString());
							System.out.println(line);
			    		} else {
			    			if (this.deleteFirst) {
				    			this.manager.addLTKDbObject(requestor, word.toJsonString());
			    			} else {
				    			this.manager.mergeLTKDbObject(requestor, word.toJsonString());
			    			}
			    		}
						if (! deps.equals("_")) {
							if (! depsList.contains(deps)) {
								depsList.add(deps);
							}
						}
						if (! misc.equals("_")) {
							if (! miscList.contains(misc)) {
								miscList.add(misc);
							}
						}
					} else {
						System.out.println("Unexpected tab count");
						System.out.println(line);
						break;
					}
				}
				linecount++;
			}
		}
//		System.out.println("P 9");
//		for (String p : depsList) {
//			System.out.println(p);
//		}
//		System.out.println("P 10");
//		for (String p : miscList) {
//			System.out.println(p);
//		}
		System.out.println(this.label);
		System.out.println("Docs = " + docCount);
		System.out.println("Sents = " + sentCount);
		System.out.println("Tokens = " + tokenCount);
		return tokenCount;
	}
	
	private void findStartingPoint() {
		JsonObject s = this.manager.getMostRecentNode(this.queryLabelSentence);
		if (s == null) {
			// ignore
		} else {
			lastSentence = manager.gson.fromJson(
					s.toString()
					, UDtbSentence.class
			);
			// one off override
			lastSentence.setTopic("tlg0012.tlg001.perseus-grc1.tb.xml");
			lastSentence.setKey("s2277282");
			JsonObject w = this.manager.getMostRecentNode(this.queryLabelWord);
			if (w == null) { 
				// ignore
			} else {
				lastWord = manager.gson.fromJson(
						w.toString()
						, UDtbWord.class
						);
				if (lastWord.getToken().equals("Root")) {
					lastRoot = lastWord;
					lastWord = null;
				}
			}
		}
}

	/**
	 * Search the database to get the most recently created Word,
	 * that is, the last one created.
	 * @return
	 */

	@Override
	public void run() {
		synchronized(this) {
			try {
					if (this.deleteFirst) {
						if (this.simulate) {
							System.out.println("Simulating delete first");
						} else {
							 this.deleteFirst();
						}
					}
					if (this.pullFirst) {
						   this.initializeRepository();
					}
					this.load();
			} catch (Exception e) {
				ErrorUtils.report(logger, e);
			}
		}
	}
	
	private void createRelationships(
			UDtbSentence sentence
			, Map<String,UDtbWord> words
			) {
		try {
//			for (String key : words.keySet()) {
//				logger.info(key);
//			}
			// create the root of the dependency diagram
	    	UDtbWord root = new UDtbWord(sentence.toTopicKey(), "root");
	    	root.setToken("Root");
	    	root.setAdHocLabel(this.label);
	    	// save it to the database
	    	if (deleteFirst) {
		    	this.manager.addLTKDbObject(requestor, root.toJsonString());
	    	} else {
		    	this.manager.mergeLTKDbObject(requestor, root.toJsonString());
	    	}
	    	// create the relationship between the sentence and the root of the dependency tree
	    	ExternalDbManager.neo4jManager.createRelationship(
	    			root.getId()
	    			, root.getOntologyTopic().label
	    			, sentence.getId()
	    			, sentence.getOntologyTopic().label
	    			, "TREE"
	    			);
	    	// find all the UtbWords for this UtbSentence that do not yet have a relationship.  This is so we can restart from where we left off
			String query = "MATCH (n" 
					+ this.queryLabelWord 
					+ ") WHERE n.id starts with '" 
					+ sentence.getId() 
					+ "~' and NOT (n)--(" 
					+ this.queryLabelWord
					+ ") RETURN n.id as id;";
			ResultJsonObjectArray queryResult = 
					this.manager.getForQuery(query, false, false);
			for (JsonObject o : queryResult.values) {
				String id = o.get("id").getAsString();
				if (! id.endsWith("root")) {
					UDtbWord from = null;
					String toKey = null;
					UDtbWord to = null;
					String fromId = null;
					String fromOntologyTopic = null;
					String fromLabel = null;
					String toId = null;
					String toOntologyTopic = null;
		    		try {
//		    			logger.info("from id = " + id);
		    			from = words.get(id); 
		    			if (from != null) {
				    		toKey = from.getDependsOn();
				    		if (toKey.length() > 0 && ! toKey.equals("_")) {
				        		to = null;
				        		if (toKey == null || toKey.length() == 0 || toKey.equals("0") || toKey.toLowerCase().equals("root")) {
				        			to = root;
//					    			logger.info("to id = " + root.getId());
				        		} else {
				        			to = words.get(sentence.getId() + "~" + toKey);
//					    			logger.info("to id = " + sentence.getId() + "~" + toKey);
				        		}
				        		if (from == null || to == null) {
				        			logger.error("From or To node null");
				        		} else {
				        			fromId = from.getId();
				        			fromLabel = from.getLabel();
				        			fromOntologyTopic = from.getOntologyTopic().label;
				        			toId = to.getId();
				        			toOntologyTopic = to.getOntologyTopic().label;
				            		ExternalDbManager.neo4jManager.createRelationship(
				            				fromId
				            				, fromOntologyTopic
				            				, toId
				            				, toOntologyTopic
				            				, fromLabel
				            		);
				        		}
				    		} else {
				    			logger.error("Word " + from.getId() + " missing head value");
				    		}
		    			} else {
		    				logger.error(id + " missing from list of words.");
		    			}
		    		} catch (Exception inner) {
	    			 ErrorUtils.report(logger, inner);
				    }
				}
	    	}
		} catch (Exception e) {
			ErrorUtils.report(logger, e);
		}
	}
	
}
