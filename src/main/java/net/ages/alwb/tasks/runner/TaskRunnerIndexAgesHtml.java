package net.ages.alwb.tasks.runner;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.ocmc.ioc.liturgical.schemas.constants.HTTP_RESPONSE_CODES;
import org.ocmc.ioc.liturgical.schemas.models.ws.response.RequestStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ages.alwb.tasks.TaskIndexAgesHtml;

/**
 * Runs task to index AGES html
 * 
 * @author mac002
 *
 */
public class TaskRunnerIndexAgesHtml extends SuperTaskRunner {
	private static final Logger logger = LoggerFactory.getLogger(TaskRunnerIndexAgesHtml.class);
	
	public TaskRunnerIndexAgesHtml(
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
					new TaskIndexAgesHtml(
							this.getExternalManager()
							)
			);
			executorService.shutdown();
		} catch (Exception e) {
			status.setCode(HTTP_RESPONSE_CODES.SERVER_ERROR.code);
			status.setMessage(e.getMessage());
		}
		logger.info("Started task");
	}
	
	public static void main(String[] args) {
		String uid = System.getenv("uid");
		String pwd = System.getenv("pwd");
		String dbHost =  "209.97.147.166";// "localhost"; // "db.olw.liml.org"; // "209.97.147.166";
		
		TaskRunnerIndexAgesHtml task = new TaskRunnerIndexAgesHtml(
				uid
				, pwd
				, dbHost
				);
		task.run();
	}

}
