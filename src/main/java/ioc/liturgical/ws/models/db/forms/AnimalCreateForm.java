package ioc.liturgical.ws.models.db.forms;

import ioc.liturgical.ws.constants.ONTOLOGY_TOPICS;
import ioc.liturgical.ws.models.db.supers.LTKOntologyCreateFormEntry;

import com.github.reinert.jjschema.Attributes;
import com.google.gson.annotations.Expose;

/**
 * @author mac002
 *
 */
@Attributes(title = "Animal", description = "This is a doc that records information about an animal.")
public class AnimalCreateForm extends LTKOntologyCreateFormEntry {

	@Attributes(required = true, description = "Sound")
	@Expose String sound = "";

	public AnimalCreateForm(
			String key
			) {
		super(
				ONTOLOGY_TOPICS.ANIMAL
				, key
				, AnimalCreateForm.class.getSimpleName()
				,  1.1
				);
	}

	public String getSound() {
		return sound;
	}

	public void setSound(String sound) {
		this.sound = sound;
	}

}
