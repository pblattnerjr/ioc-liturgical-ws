package net.ages.alwb.utils.core.datastores.json.adaptors;

import java.io.IOException;

import org.ocmc.ioc.liturgical.schemas.models.db.internal.TKVString;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class TKVStringAdaptor  extends TypeAdapter<TKVString> {

	@Override
	public TKVString read(JsonReader in) throws IOException {
		final TKVString entry = new TKVString();
		in.beginObject();
		while (in.hasNext()) {
			String name = in.nextName();
			if (name.equals("_id")) {
				entry.set_id(in.nextString());
			} else if (name.equals("value")) {
				entry.setValue(in.nextName());
			}
		}
		in.endObject();
		return entry;
	}

	@Override
	public void write(JsonWriter arg0, TKVString arg1) throws IOException {
	}

}
