package net.ages.alwb.utils.core.datastores.json.converters;

import org.ocmc.ioc.liturgical.schemas.models.db.internal.TKVString;

/**
 * Use this to get subclasses of TKVString
 * @author mac002
 *
 * @param <T>
 */
public class TKVStringConverter<T extends TKVString> {
	T instanceOfT = null;
	public TKVStringConverter(T instance) {
		this.instanceOfT = instance;
	}
	
	public T getInstance(TKVString s) {
		return (T) this.instanceOfT.fromJsonString(s.getValue());
	}
}
