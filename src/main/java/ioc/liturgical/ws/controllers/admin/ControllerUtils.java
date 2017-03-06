package ioc.liturgical.ws.controllers.admin;

import org.slf4j.Logger;

public class ControllerUtils {
	public static void reportPath(Logger logger, String verb, String path) {
		logger.info("handles " + verb + " for path " + path);
	}
}
