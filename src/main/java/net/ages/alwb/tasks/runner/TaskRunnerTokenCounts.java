package net.ages.alwb.tasks.runner;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.ocmc.ioc.liturgical.schemas.constants.HTTP_RESPONSE_CODES;
import org.ocmc.ioc.liturgical.schemas.models.ws.response.RequestStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ages.alwb.tasks.UdTokenCountTask;

public class TaskRunnerTokenCounts extends SuperTaskRunner {
	private static final Logger logger = LoggerFactory.getLogger(TaskRunnerTokenCounts.class);
	private String topicStartsWith = "";
	private String out = "";
	
	public TaskRunnerTokenCounts(
			String uid
			, String pwd
			, String dbHost
			, String topicStartsWith
			, String out
			) {
		super(uid, pwd, dbHost, false, false);
		this.topicStartsWith = topicStartsWith;
		this.out = out;
	}
	
	public void run() {
		RequestStatus status = new RequestStatus();
		try {
			// run it as a thread
			ExecutorService executorService = Executors.newSingleThreadExecutor();
			executorService.execute(
					new UdTokenCountTask(
							this.getExternalManager()
							, this.getUid()
							, this.topicStartsWith
							, this.out
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
		String dbHost =   "159.203.89.233"; // "localhost";; 
		String idTopic = "oc.";
		String out = "/Users/mac002/canBeRemoved/octoechos.txt";
		TaskRunnerTokenCounts task = new TaskRunnerTokenCounts(
				uid
				, pwd
				, dbHost
				, idTopic
				, out
				);
		task.run();
	}

}
