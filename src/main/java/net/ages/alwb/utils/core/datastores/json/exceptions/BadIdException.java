package net.ages.alwb.utils.core.datastores.json.exceptions;

public class BadIdException extends Exception {
	
	public BadIdException(String message) {
        super(message);
    }

    public BadIdException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
