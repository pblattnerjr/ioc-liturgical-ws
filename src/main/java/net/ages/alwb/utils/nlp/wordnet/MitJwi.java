package net.ages.alwb.utils.nlp.wordnet;

import java.io.IOException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import delete.me.MitJwiTest;
import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import net.ages.alwb.utils.core.error.handling.ErrorUtils;

public class MitJwi {
	private static final Logger logger = LoggerFactory.getLogger(MitJwi.class);

	private IDictionary dict = null;
	
	public MitJwi() {
		try {
			dict = new Dictionary(MitJwiTest.class.getResource("/dict"));
			dict.open();
		} catch (IOException e) {
			ErrorUtils.report(logger, e);
		}
	}

}
