package net.ages.alwb.tasks.runner;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.ocmc.ioc.liturgical.schemas.constants.DATA_SOURCES;
import org.ocmc.ioc.liturgical.schemas.constants.HTTP_RESPONSE_CODES;
import org.ocmc.ioc.liturgical.schemas.models.ws.db.UtilityUdLoader;
import org.ocmc.ioc.liturgical.schemas.models.ws.response.RequestStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ages.alwb.tasks.UdTreebankDataCreateTask;

public class TaskRunnerUdLoader extends SuperTaskRunner {
	private static final Logger logger = LoggerFactory.getLogger(TaskRunnerUdLoader.class);

	private DATA_SOURCES source = DATA_SOURCES.UNKNOWN;
	boolean pullFirst = false;
	boolean deleteFirst = true;
	boolean disableSynch = true;
	long nbrSentencesToLoad = -1; // unlimited
	
	public TaskRunnerUdLoader(
			String uid
			, String pwd
			, String dbHost
			, DATA_SOURCES source
			, boolean disableSynch
			, boolean pullFirst
			, boolean deleteFirst
			, boolean simulate
			, long nbrSentencesToLoad
			) {
		super(uid, pwd, dbHost, disableSynch, simulate);
		this.source = source;
		this.disableSynch = disableSynch;
		this.deleteFirst = deleteFirst;
		this.pullFirst = pullFirst;
		if (pullFirst) {
			this.deleteFirst = true;
		}
		if (simulate) {
			this.deleteFirst = false;
		}
		this.nbrSentencesToLoad = nbrSentencesToLoad;
	}

	public void run() {
		UtilityUdLoader form = new UtilityUdLoader();
		form.setDataSource(this.source);
		form.setPullFirst(this.pullFirst);
		form.setDeleteFirst(this.deleteFirst);
		form.setSimulate(this.isSimulate());
		if (this.isSimulate()) {
			RequestStatus status = new RequestStatus();
			try {
				// run it as a thread
				ExecutorService executorService = Executors.newSingleThreadExecutor();
				executorService.execute(
						new UdTreebankDataCreateTask(
								null
								, "wsadmin"
								, form.getDataSource()
								, form.isPullFirst()
								, form.isDeleteFirst()
								, form.isSimulate()
								, this.nbrSentencesToLoad
							)
				);
				executorService.shutdown();
			} catch (Exception e) {
				status.setCode(HTTP_RESPONSE_CODES.SERVER_ERROR.code);
				status.setMessage(e.getMessage());
			}
		} else {
			this.getExternalManager().runUtilityUdTreebank(this.getUid(), form.toJsonString(), this.nbrSentencesToLoad);
		}
		logger.info("Finished");
	}
	public static void main(String[] args) {
		String uid = System.getenv("uid");
		String pwd = System.getenv("pwd");
		String dbHost = "159.203.89.233"; // "localhost";
		DATA_SOURCES source = DATA_SOURCES.UD_ANCIENT_GREEK_PERSEUS;
		/**
		 * pullFirst if
		 * true - will pull from Github repo
		 * false - will use local git repo.  If not found, will pull.
		 */
		boolean pullFirst = false; 

		/**
		 * deleteFirst if
		 * true - will delete the records from the database and load from the beginning
		 * false - will pick up where it left off.  Useful when we aborted prematurely
		 */
		boolean deleteFirst = false; 
		if (pullFirst) {
			deleteFirst = true; 
		}
		/**
		 * if simultate:
		 * 		won't start up DB Managers
		 *     won't write database
		 */
		boolean simulate = false;
		/**
		 * disableSynch = 
		 *   true - will block Neo4jConnectionManager.insertTransation.
		 *   false - won't.  Usually best if true.
		 */
		boolean disableSynch  = true;  
		
		long nbrSentencesToLoad = -1;
		
		TaskRunnerUdLoader task = new TaskRunnerUdLoader(
				uid
				, pwd
				, dbHost
				, source
				, disableSynch
				, pullFirst
				, deleteFirst
				, simulate
				, nbrSentencesToLoad
				);
		task.run();
	}

}
