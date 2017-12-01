package ioc.liturgical.test.framework;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.ocmc.ioc.liturgical.schemas.models.ws.forms.UserUpdateForm;

public class DeleteMe {

	public static void main(String[] args) {
		UserUpdateForm form = new UserUpdateForm();
		System.out.println(form.toUiSchema());
	}

}
