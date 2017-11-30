package net.ages.alwb.utils.core.datastores.json.exceptions;

public class BadIdException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BadIdException(String message) {
        super(message);
    }

    public BadIdException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
