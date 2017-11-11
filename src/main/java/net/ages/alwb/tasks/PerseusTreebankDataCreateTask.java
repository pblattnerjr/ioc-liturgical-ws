package net.ages.alwb.tasks;
import java.io.File;
import java.io.IOException;
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
import ioc.liturgical.ws.models.ResultJsonObjectArray;
import ioc.liturgical.ws.models.db.docs.nlp.PtbSentence;
import ioc.liturgical.ws.models.db.docs.nlp.PtbWord;
import ioc.liturgical.ws.models.db.supers.LTK;
import net.ages.alwb.utils.core.error.handling.ErrorUtils;
import net.ages.alwb.utils.core.file.AlwbFileUtils;
import net.ages.alwb.utils.nlp.utils.PerseusPostagMapper;

/**
 * Runs a task (separate thread) to read the Perseus Treebank data
 * from Github and write it to the database
 * .
 * @author mac002
 *
 */
public class PerseusTreebankDataCreateTask implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(PerseusTreebankDataCreateTask.class);

	private ExternalDbManager manager = null;
	private String requestor = "";
	boolean deleteFirst = false;
	private String repoUrl = "https://github.com/PerseusDL/treebank_data.git";
	private String localRepoPath = "repo";
	private String xmlPath = localRepoPath + "/v2.1/Greek/texts";
	
	public PerseusTreebankDataCreateTask (
			ExternalDbManager manager
			, String requestor
			, boolean deleteFirst
			) {
		this.manager = manager;
		this.requestor = requestor;
		this.deleteFirst = deleteFirst;
	}
	
	private void initializeRepository() {
		File f = new File(localRepoPath);
		try {
			Git git = null;
			if (f.exists()) {
				try {
					logger.info("Pulling perseusDL treebank_data from Github...");
					git = Git.open(f);
					git.pull();
					logger.info("Pulled perseusDL treebank_data from Github to " + f.getAbsolutePath());
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				logger.info("Cloning perseusDL treebank_data from Github...");
				git = Git.cloneRepository()
						  .setURI( repoUrl )
						  .setDirectory( f)
						  .call();
				logger.info("Cloned perseusDL treebank_data to " + f.getAbsolutePath());
			}
		} catch (InvalidRemoteException e) {
			e.printStackTrace();
		} catch (TransportException e) {
			e.printStackTrace();
		} catch (GitAPIException e) {
			e.printStackTrace();
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
				String startWithXmlFile = "tlg0012.tlg002.perseus-grc1.tb.xml";
				boolean foundStartingFile = false;
				
				PtbSentence lastSentence = null;
				PtbWord lastRoot = null;
				PtbWord lastWord = null;
				boolean foundLastSentenceInXml = true;
				boolean foundLastWordInXml = true;
				ResultJsonObjectArray result = null;
				
				if (deleteFirst) {
					result = this.manager.getForQuery(
							"MATCH (n:PerseusTreebank) return count(n) as count"
							, false
							, false
							);
					int nodeCount = result.getFirstObject().get("count").getAsInt();
					logger.info("Deleting " + nodeCount + " PtbSentence and PtbWord nodes and relationships");
					try {
						while (nodeCount > 0) {
							result = this.manager.getForQuery(
									"MATCH (n:PerseusTreebank) optional MATCH (n:PerseusTreebank)<-[r]-(:PerseusTreebank) with n,r limit 10000 delete n, r return count(n) as deletedNodesCount"
									, false
									, false
									);
							int deleted = result.getFirstObject().get("deletedNodesCount").getAsInt();
							nodeCount = nodeCount - deleted;
							logger.info("Deleted " + deleted + ". Remaining node count = " + nodeCount);
						}
						logger.info("Deleted " + result.getValueCount() + " nodes and relationships.");
						ExternalDbManager.neo4jManager.dropConstraint("PtbSentence", "id");
						ExternalDbManager.neo4jManager.dropConstraint("PtbWord", "id");
					} catch (Exception e) {
						ErrorUtils.report(logger, e);
					}
//				} else {
//					logger.info("Gathering information to resume where last left off...");
//					/**
//					 * The algorithm used for creating the nodes and relationships for the Treebank
//					 * is as follows:
//					 * 1. Create all the PtbWord nodes for the sentence
//					 * 2. Create the PtbSentence node
//					 * 3. Create the root PtbWord node
//					 * 4. Create the relationship between the sentence and the root
//					 * 5. Create relationships between the sentence's PtbWord nodes
//					 *     including the root node.
//					 *     
//					 *  If the thread crashed for some reason, then we want to restart at the
//					 *  appropriate point.  We will find the most recent sentence and most
//					 *  recent word (which might be a root node).
//					 *  
//					 *  The results will indicate where to start creating new nodes.
//					 */
//					lastSentence = manager.gson.fromJson(
//							this.manager.getMostRecentNode(
//									PtbSentence.class.getSimpleName()
//							  ).toString()
//							, PtbSentence.class
//							);
//					lastWord = manager.gson.fromJson(
//							this.manager.getMostRecentNode(
//									PtbWord.class.getSimpleName()
//							  ).toString()
//							, PtbWord.class
//							);
//					if (lastWord.getToken().equals("Root")) {
//						lastRoot = lastWord;
//						lastWord = null;
//					}
				}
			   this.initializeRepository();
			   List<File> files = AlwbFileUtils.getFilesInDirectory(xmlPath, "xml");
			   int filesFoundCount = files.size();
			   int currentFileIndex = 0;

			   logger.info("Found " + filesFoundCount + " xml files...");
			   for (File fXmlFile: files) {
				   currentFileIndex++;
				   if (startWithXmlFile.length() > 0) {
					   if (! foundStartingFile) {
						   foundStartingFile = fXmlFile.getName().equals(startWithXmlFile);
					   }
				   }
				   if (foundStartingFile) {
				    	logger.info("Processing " + currentFileIndex + " of " + filesFoundCount + " xml file " + fXmlFile.getName());
				    	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				    	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				    	Document doc = dBuilder.parse(fXmlFile);
				    	doc.getDocumentElement().normalize();
				    	String author = "";
				    	String title = "";
				    	NodeList nList = doc.getElementsByTagName("author");
				    	if (nList.getLength() > 0) {
				    		author = nList.item(0).getTextContent();
				    		logger.info("Author: " + author);
				    	}
				    	nList = doc.getElementsByTagName("title");
				    	if (nList.getLength() > 0) {
				    		title = nList.item(0).getTextContent();
				    		logger.info("Title: " + title);
				    	}
				    	nList = doc.getElementsByTagName("word");
				    	
				    	Map<String, PtbWord> words = new TreeMap<String,PtbWord>();
				    	PtbSentence sentence = null;
				    	StringBuffer text = new StringBuffer();
				    	
				    	// we have all the words across all sentences.  
				    	// This means we have to detect when we cross sentence boundaries.
				    	for (int temp = 0; temp < nList.getLength(); temp++) {
				    		Node nNode = nList.item(temp);
				   			Element eElement = (Element) nNode;
				   			Node parent = nNode.getParentNode();
				   			Element parentElement = (Element) parent;
				   			
				    		String parentId = parentElement.getAttribute("id");
				    		String parentDocumentId = parentElement.getAttribute("document_id");
				    		String sentenceId = "en_sys_linguistics~" + parentDocumentId + "~s" + parentId;
				    		
				    		if (lastSentence != null && ! foundLastSentenceInXml) {
				    				foundLastSentenceInXml = lastSentence.getId().equals(sentenceId);
				    		}
				    		
				    		if (sentence == null) {
				    			// Create the Sentence Object, but wait to save it to the database.
				    			// The reason is that we need to build the text for the sentence
				    			// by concatenating each of the sentence's xml entries for the words (tokens)
				    			sentence = new PtbSentence(
				    					parentDocumentId
				    					, parentId
				    					);
				    			if (author != null) {
					    			sentence.setAuthor(author);
				    			}
				    			if (title != null) {
					    			sentence.setTitle(title);
				    			}
				    		} else if (! sentence.getId().equals(sentenceId)) { // sentence has changed
				    			sentence.setValue(text.toString());
				    			// save the sentence to the database
				    			if (deleteFirst) {
					    			this.manager.addLTKDbObject(requestor, sentence.toJsonString());
				    			} else {
					    			this.manager.mergeLTKDbObject(requestor, sentence.toJsonString());
				    			}
				    			this.createRelationships(sentence, words);
				    			text = new StringBuffer();
				    			words = new TreeMap<String,PtbWord>();
				    			// create a new Sentence Object
				    			sentence = new PtbSentence(
				    					parentDocumentId
				    					, parentId
				    					);
				    		}
				    		
				    		String id = eElement.getAttribute("id");
				    		String form = eElement.getAttribute("form");
				    		String head = eElement.getAttribute("head");
				    		String lemma = eElement.getAttribute("lemma");
				    		String relation = eElement.getAttribute("relation");
				    		String postag = eElement.getAttribute("postag");
				    		
				    		if (text.length() > 0) {
					    		if (! postag.equals("u--------")) {
					    			text.append(" ");
					    		}
				    		}
				    		text.append(form);
				    		
				    		// create the JsonObject
				    		PtbWord word = new PtbWord(
			    					sentence.getTopicKey()
			    					, id
				    		);
				    		word.setToken(form);
				    		word.setLemma(lemma);
				    		word.setPostag(postag);
				    		if (postag.length() == 9) {
				    			word.setGrammar(PerseusPostagMapper.postagToGrammar(postag));
				    			word.setgCase(PerseusPostagMapper.postagCase(postag));
				    			word.setGender(PerseusPostagMapper.postagGender(postag));
				    			word.setMood(PerseusPostagMapper.postagMood(postag));
				    			word.setNumber(PerseusPostagMapper.postagNumber(postag));
				    			word.setPerson(PerseusPostagMapper.postagPerson(postag));
				    			word.setTense(PerseusPostagMapper.postagTense(postag));
				    			word.setVoice(PerseusPostagMapper.postagVoice(postag));
				    		} else {
				    			word.setGrammar(postag);
				    		}
				    		word.setLabel(relation);
				    		if (head == null || head.length() == 0 || head.equals("0")) {
				    			word.setDependsOn("Root");
				    		} else {
					    		word.setDependsOn(head);
				    		}
				    		if (eElement.hasAttribute("gloss")) {
				    			word.setGloss(eElement.getAttribute("gloss"));
				    		}
				    		// save it to the database
				    		if (deleteFirst) {
					    		this.manager.addLTKDbObject(requestor, word.toJsonString());
				    		} else {
					    		this.manager.mergeLTKDbObject(requestor, word.toJsonString());
				    		}
				    		// save it to the word list
				    		words.put(word.getId(), word);
				    	}
				   } else {
				    	logger.info("Skipping " + currentFileIndex + " of " + filesFoundCount + " xml file " + fXmlFile.getName());
				   }
			    }
			} catch (Exception e) {
				ErrorUtils.report(logger, e);
			}
		}
	}
	
	private void createRelationships(
			PtbSentence sentence
			, Map<String,PtbWord> words
			) {
		try {
			// create the root of the dependency diagram
	    	PtbWord root = new PtbWord(sentence.getTopicKey(), "root");
	    	root.setToken("Root");
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
	    	// find all the PtbWords for this PtbSentence that do not yet have a relationship.
			String query = "MATCH (n:PtbWord) WHERE n.id starts with '" + sentence.getId() + "~' and NOT (n)--(:PtbWord) RETURN n.id as id;";
			ResultJsonObjectArray queryResult = 
					this.manager.getForQuery(query, false, false);
			for (JsonObject o : queryResult.values) {
				String id = o.get("id").getAsString();
				if (! id.endsWith("root")) {
					PtbWord from = null;
					String toKey = null;
					PtbWord to = null;
					String fromId = null;
					String fromOntologyTopic = null;
					String fromLabel = null;
					String toId = null;
					String toOntologyTopic = null;
					String toLabel = null;
		    		try {
		    			from = words.get(id); 
		    			if (from != null) {
				    		toKey = from.getDependsOn();
				    		if (toKey.length() > 0) {
				        		to = null;
				        		if (toKey == null || toKey.length() == 0 || toKey.equals("0") || toKey.equals("root")) {
				        			to = root;
				        		} else {
				        			to = words.get(sentence.getId() + "~" + toKey);
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
