package ioc.liturgical.ws.models.ws.response.column.editor;

public class MissingSeparatorException extends Exception {
	private static final long serialVersionUID = 1L;

	public MissingSeparatorException() {
		super("Missing topic key separator. Expected __");
	}

	public MissingSeparatorException(String value){
       super("Missing topic key separator. Expected __ for " + value);
    }
}
