package ioc.liturgical.ws.models.db.synch;

import com.google.gson.annotations.Expose;

import net.ages.alwb.utils.core.datastores.json.exceptions.BadIdException;
import net.ages.alwb.utils.core.datastores.json.models.LTK;

public class Transaction extends LTK {
	
	@Expose public ioc.liturgical.ws.models.db.supers.LTK transaction = null;
	
	public Transaction(
			String library
			, String topic
			, String key
			, ioc.liturgical.ws.models.db.supers.LTK transaction
			) throws BadIdException {
		super(library, topic, key);
		this.transaction = transaction;
	}

}
