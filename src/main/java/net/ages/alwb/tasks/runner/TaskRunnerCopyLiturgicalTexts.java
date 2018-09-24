package net.ages.alwb.tasks.runner;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.ocmc.ioc.liturgical.schemas.constants.HTTP_RESPONSE_CODES;
import org.ocmc.ioc.liturgical.schemas.models.ws.response.RequestStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ages.alwb.tasks.CopyLiturgicalTexts;
import net.ages.alwb.tasks.FixCalDays;
import net.ages.alwb.tasks.SetNnpFirstFiveTask;
import net.ages.alwb.tasks.UdTokenCountTask;

/**
 * One time (?) task to fix the number of the day for Guatemala calendar
 * @author mac002
 *
 */
public class TaskRunnerCopyLiturgicalTexts extends SuperTaskRunner {
	private static final Logger logger = LoggerFactory.getLogger(TaskRunnerCopyLiturgicalTexts.class);
	private String fromLibrary = "";
	private String toLibrary = "";
	
	public TaskRunnerCopyLiturgicalTexts(
			String uid
			, String pwd
			, String dbHost
			, String fromLibrary
			, String toLibrary
			) {
		super(uid, pwd, dbHost, false, false);
		this.fromLibrary  = fromLibrary;
		this.toLibrary = toLibrary;
	}
	
	public void run() {
		RequestStatus status = new RequestStatus();
		try {
			// run it as a thread
			ExecutorService executorService = Executors.newSingleThreadExecutor();
			executorService.execute(
					new CopyLiturgicalTexts(
							this.getExternalManager()
							, fromLibrary
							, toLibrary
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
		String dbHost =   "db.olw.liml.org"; // "localhost";
		String fromLibrary = "en_us_jrepass";
		String toLibrary = "en_us_repass";
		
		TaskRunnerCopyLiturgicalTexts task = new TaskRunnerCopyLiturgicalTexts(
				uid
				, pwd
				, dbHost
				, fromLibrary
				, toLibrary
				);
		task.run();
	}

}
