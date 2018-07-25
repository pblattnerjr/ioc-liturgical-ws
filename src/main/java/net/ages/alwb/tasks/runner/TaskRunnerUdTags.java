package net.ages.alwb.tasks.runner;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.ocmc.ioc.liturgical.schemas.constants.HTTP_RESPONSE_CODES;
import org.ocmc.ioc.liturgical.schemas.constants.nlp.ANSWER_TO;
import org.ocmc.ioc.liturgical.schemas.constants.nlp.UD_ASPECT;
import org.ocmc.ioc.liturgical.schemas.constants.nlp.UD_CASE;
import org.ocmc.ioc.liturgical.schemas.constants.nlp.UD_DEFINITE;
import org.ocmc.ioc.liturgical.schemas.constants.nlp.UD_DEGREE;
import org.ocmc.ioc.liturgical.schemas.constants.nlp.UD_DEPENDENCY_LABELS;
import org.ocmc.ioc.liturgical.schemas.constants.nlp.UD_GENDER;
import org.ocmc.ioc.liturgical.schemas.constants.nlp.UD_MOOD;
import org.ocmc.ioc.liturgical.schemas.constants.nlp.UD_NUMBER;
import org.ocmc.ioc.liturgical.schemas.constants.nlp.UD_NUM_TYPE;
import org.ocmc.ioc.liturgical.schemas.constants.nlp.UD_POS;
import org.ocmc.ioc.liturgical.schemas.constants.nlp.UD_POSS;
import org.ocmc.ioc.liturgical.schemas.constants.nlp.UD_PRON_TYPE;
import org.ocmc.ioc.liturgical.schemas.constants.nlp.UD_PERSON;
import org.ocmc.ioc.liturgical.schemas.constants.nlp.UD_TENSE;
import org.ocmc.ioc.liturgical.schemas.constants.nlp.UD_VERB_FORM;
import org.ocmc.ioc.liturgical.schemas.constants.nlp.UD_VOICE;
import org.ocmc.ioc.liturgical.schemas.models.ws.response.RequestStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonArray;

import net.ages.alwb.tasks.UdCreateTagsTask;

public class TaskRunnerUdTags extends SuperTaskRunner {
	private static final Logger logger = LoggerFactory.getLogger(TaskRunnerUdTags.class);

	boolean deleteFirst = true;
	boolean recordQuery = false;
	long nbrToLoad = -1; // unlimited
	Map<String,JsonArray> tags = new TreeMap<String,JsonArray>();
	
	public TaskRunnerUdTags(
			String uid
			, String pwd
			, String dbHost
			, Map<String,JsonArray> tags
			, boolean recordQuery
			, boolean deleteFirst
			, long nbrToLoad
			) {
		super(uid, pwd, dbHost, recordQuery, false);
		this.tags = tags;
		this.deleteFirst = deleteFirst;
		this.nbrToLoad = nbrToLoad;
		this.recordQuery = recordQuery;
	}
	
	public void run() {
		RequestStatus status = new RequestStatus();
		try {
			// run it as a thread
			ExecutorService executorService = Executors.newSingleThreadExecutor();
			executorService.execute(
					new UdCreateTagsTask(
							this.getExternalManager()
							, this.getUid()
							, this.tags
							, this.deleteFirst
							, this.nbrToLoad
						)
			);
			executorService.shutdown();
		} catch (Exception e) {
			status.setCode(HTTP_RESPONSE_CODES.SERVER_ERROR.code);
			status.setMessage(e.getMessage());
		}
		logger.info("Finished");
	}
	
	public static void main(String[] args) {
		String uid = System.getenv("uid");
		String pwd = System.getenv("pwd");
		String dbHost =  "localhost"; // ; "159.203.89.233"
		Map<String,JsonArray> tags = new TreeMap<String,JsonArray>();
//		tags.put("Answertags", ANSWER_TO.toJsonArray());
//		tags.put("UDtagsDepRel", UD_DEP_REL_LABEL.toJsonArray());
//		tags.put("UDtagsAspect", UD_ASPECT.toJsonArray());
//		tags.put("UDtagsCase", UD_CASE.toJsonArray());
//		tags.put("UDtagsDefinite", UD_DEFINITE.toJsonArray());
//		tags.put("UDtagsDegree", UD_DEGREE.toJsonArray());
//		tags.put("UDtagsGender", UD_GENDER.toJsonArray());
//		tags.put("UDtagsMood", UD_MOOD.toJsonArray());
//		tags.put("UDtagsNumber", UD_NUMBER.toJsonArray());
		tags.put("UDtagsNumType", UD_NUM_TYPE.toJsonArray());
//		tags.put("UDtagsPos", UD_POS.toJsonArray());
		tags.put("UDtagsPoss", UD_POSS.toJsonArray());
//		tags.put("UDtagsPerson", UD_PERSON.toJsonArray());
		tags.put("UDtagsPronType", UD_PRON_TYPE.toJsonArray());
//		tags.put("UDtagsTense", UD_TENSE.toJsonArray());
//		tags.put("UDtagsVerbForm", UD_VERB_FORM.toJsonArray());
//		tags.put("UDtagsVoice", UD_VOICE.toJsonArray());

		/**
		 * deleteFirst if
		 * true - will delete the records from the database and load from the beginning
		 * false - will pick up where it left off.  Useful when we aborted prematurely
		 */
		boolean deleteFirst = true; 
		boolean recordQuery = false;
		long nbrToLoad = -1;
		
		TaskRunnerUdTags task = new TaskRunnerUdTags(
				uid
				, pwd
				, dbHost
				, tags
				, recordQuery
				, deleteFirst
				, nbrToLoad
				);
		task.run();
	}

}
