package ioc.liturgical.ws.controllers.admin;

import org.slf4j.Logger;

public class ControllerUtils {
	public static void reportPath(Logger logger, String verb, String path) {
		logger.info("handles " + verb + " for path " + path);
	}
	public static void reportPath(Logger logger, String verb, String path, int i) {
		logger.info(i + " handles " + verb + " for path " + path);
	}
}
