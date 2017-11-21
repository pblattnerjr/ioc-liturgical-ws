package ioc.liturgical.ws.models.db.forms;


import ioc.liturgical.ws.constants.ID_PART_TYPES;
import ioc.liturgical.ws.constants.db.external.TOPICS;
import ioc.liturgical.ws.models.db.supers.LTKNoteCreateForm;

import com.github.reinert.jjschema.Attributes;

/**
 * This class provides a POJO for use in web forms to create or update a note made by a user
 * @author mac002
 *
 */
@Attributes(title = "User Note (Private)", description = "This form allows you to enter or read private notes you make about the text shown above.")
public class UserNoteCreateForm extends LTKNoteCreateForm {
	
	private static double serialVersion = 1.1;
	private static String schema = UserNoteCreateForm.class.getSimpleName();
	private static TOPICS ontoTopic = TOPICS.NOTE_USER;


	public UserNoteCreateForm(
			String usersLibrary
			, String textId
			, String key
			) {
		super(
				usersLibrary
				, textId
				, key
				, UserNoteCreateForm.schema
				,  UserNoteCreateForm.serialVersion
				, UserNoteCreateForm.ontoTopic
				);
		this.partTypeOfTopic = ID_PART_TYPES.NOTE_USING_ID_OF_SELECTED_TEXT;
		this.partTypeOfKey = ID_PART_TYPES.TIMESTAMP;
		this.setSeq(this.getId());
	}

}
