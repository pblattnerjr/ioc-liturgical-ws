package net.ages.alwb.utils.core.error.handling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Provides a standardized error reporting mechanism
 * @author mac002
 *
 */
public class ErrorUtils {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ErrorUtils.class);
	
	private static int errorCount = 0;
	private static boolean hadErrors = false;
	private static int overallErrorCount = 0;

	public static void report(Logger logger, Exception e) {
		logger.error(org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(e));
	}

	public static void report(Logger logger, Exception e, String message) {
		incrementErrorCount();
		logger.error(message);
		logger.error(org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(e));
	}
	
	private static void incrementErrorCount() {
		errorCount++;
		overallErrorCount++;
		hadErrors = true;
	}
	
	public static int getErrorCount() {
		return errorCount;
	}
	
	public static void resetErrorCount() {
		errorCount = 0;
	}
	
	public static boolean hadErrors() {
		return errorCount > 0;
	}
	
	/**
	 * Reports error count and provides supplied message.
	 * Resets error count to zero.
	 * @param message
	 */
	public static void reportAnyErrors(String message) {
		if (ErrorUtils.hadErrors()) {
			LOGGER.info(message);
			LOGGER.info("Number of errors: " + ErrorUtils.getErrorCount());
			LOGGER.info("Please check the log for details...");
		}
		resetErrorCount();
	}

	/**
	 * Reports error count and provides supplied message.
	 * Resets error count to zero.
	 * @param message
	 */
	public static void reportTotalErrors(String message) {
		if (ErrorUtils.hadErrors()) {
			LOGGER.info(message);
			LOGGER.info("Number of errors: " + ErrorUtils.getErrorCount());
			LOGGER.info("Please check the log for details...");
		}
	}
}
