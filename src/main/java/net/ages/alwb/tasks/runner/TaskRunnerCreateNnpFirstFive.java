package net.ages.alwb.tasks.runner;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.ocmc.ioc.liturgical.schemas.constants.HTTP_RESPONSE_CODES;
import org.ocmc.ioc.liturgical.schemas.models.ws.response.RequestStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ages.alwb.tasks.SetNnpFirstFiveTask;
import net.ages.alwb.tasks.UdTokenCountTask;

public class TaskRunnerCreateNnpFirstFive extends SuperTaskRunner {
	private static final Logger logger = LoggerFactory.getLogger(TaskRunnerCreateNnpFirstFive.class);
	
	public TaskRunnerCreateNnpFirstFive(
			String uid
			, String pwd
			, String dbHost
			) {
		super(uid, pwd, dbHost, false, false);
	}
	
	public void run() {
		RequestStatus status = new RequestStatus();
		try {
			// run it as a thread
			ExecutorService executorService = Executors.newSingleThreadExecutor();
			executorService.execute(
					new SetNnpFirstFiveTask(
							this.getExternalManager()
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
		String dbHost =   "db.olw.liml.org"; // "localhost";
		TaskRunnerCreateNnpFirstFive task = new TaskRunnerCreateNnpFirstFive(
				uid
				, pwd
				, dbHost
				);
		task.run();
	}

}
