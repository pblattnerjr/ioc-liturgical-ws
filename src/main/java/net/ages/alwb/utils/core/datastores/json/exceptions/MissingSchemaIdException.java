package net.ages.alwb.utils.core.datastores.json.exceptions;

public class MissingSchemaIdException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String rootMessage = "Add schema ID to constants.SCHEMA_CLASSES. DB does not contain schema ID ";
	
	public MissingSchemaIdException() {
		super(rootMessage);
	}

	public MissingSchemaIdException(String message) {
        super(rootMessage + message);
    }

    public MissingSchemaIdException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
