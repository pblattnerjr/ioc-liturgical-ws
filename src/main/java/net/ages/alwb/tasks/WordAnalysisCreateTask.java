package net.ages.alwb.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ioc.liturgical.ws.constants.Constants;
import ioc.liturgical.ws.managers.databases.external.neo4j.ExternalDbManager;

import org.ocmc.ioc.liturgical.schemas.constants.HTTP_RESPONSE_CODES;
import org.ocmc.ioc.liturgical.schemas.constants.LIBRARIES;
import org.ocmc.ioc.liturgical.schemas.constants.TOPICS;
import org.ocmc.ioc.liturgical.schemas.models.db.docs.nlp.TokenAnalysis;
import org.ocmc.ioc.liturgical.schemas.models.db.docs.nlp.WordAnalysis;
import org.ocmc.ioc.liturgical.schemas.models.ws.response.RequestStatus;
import org.ocmc.ioc.liturgical.schemas.models.ws.response.ResultJsonObjectArray;
import org.ocmc.ioc.liturgical.utils.ErrorUtils;
import org.ocmc.ioc.liturgical.utils.GeneralUtils;

/**
 * Runs a task (separate thread) to create a new WordAnalysis
 * from a TokenAnalysis if the WordAnalysis does not
 * already exist.
 * 
 * @author mac002
 *
 */
public class WordAnalysisCreateTask implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(WordAnalysisCreateTask.class);

	ExternalDbManager manager = null;
	String requestor = "";
	TokenAnalysis token = null;
	
	public WordAnalysisCreateTask (
			ExternalDbManager manager
			, String requestor
			, TokenAnalysis token
			) {
		this.manager = manager;
		this.requestor = requestor;
		this.token = token;
	}
	
	@Override
	public void run() {
		synchronized(this) {
			String lowerToken = GeneralUtils.toNfc(token.getToken()).toLowerCase();
			String lowerLemma = GeneralUtils.toNfc(token.getLemma()).toLowerCase();
			String topic = lowerToken;
			String key = lowerLemma 
					+ "/"
					+ token.getGrammar()
					;
			String id = LIBRARIES.LINGUISTICS.toSystemDomain()
					+ Constants.ID_DELIMITER
					+ topic
					+ Constants.ID_DELIMITER
					+ key
					;
			try {
				ResultJsonObjectArray result = manager.getForId(id, TOPICS.WORD_GRAMMAR.label );
				if (result.valueCount == 0) {
					WordAnalysis wordAnalysis = new WordAnalysis();
					wordAnalysis.setTopic(topic);
					wordAnalysis.setKey(key);
					wordAnalysis.setToken(token.getToken());
					wordAnalysis.setPartOfSpeech(token.getPos());
					wordAnalysis.setLemmaGreek(token.getLemma());
					wordAnalysis.setGlosses(token.getGloss());
					wordAnalysis.setParse(token.getGrammar());
					StringBuffer sb = new StringBuffer();
					sb.append("[");
					sb.append(lowerToken);
					sb.append("/");
					sb.append(token.getGloss());
					sb.append("/");
					sb.append(token.getGrammar());
					sb.append("/");
					sb.append(token.getLemma());
					sb.append("]");
					wordAnalysis.setConcise(sb.toString());
					wordAnalysis.setSource(this.requestor);
					RequestStatus status = manager.addLTKDbObject(this.requestor, wordAnalysis.toJsonString());
					if (status.code != HTTP_RESPONSE_CODES.CREATED.code) {
						logger.error("Status code " + status.code + " when creating " + id + " " + status.developerMessage);
					}
				}
			} catch (Exception e) {
				ErrorUtils.report(logger, e);
			}
		}
	}
	
}
