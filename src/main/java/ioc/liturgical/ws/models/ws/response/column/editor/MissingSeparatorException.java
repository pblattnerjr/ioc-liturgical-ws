package ioc.liturgical.ws.models.ws.response.column.editor;

import ioc.liturgical.ws.constants.Constants;

public class MissingSeparatorException extends Exception {
	private static final long serialVersionUID = 1L;

	public MissingSeparatorException() {
		super("Missing topic key separator. Expected " + Constants.ID_DELIMITER);
	}

	public MissingSeparatorException(String value){
       super("Missing topic key separator. Expected "+ Constants.ID_DELIMITER + " for " + value);
    }
}
