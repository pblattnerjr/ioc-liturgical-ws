package net.ages.alwb.utils.core.datastores.db.h2.examples;

import com.google.gson.annotations.Expose;

import net.ages.alwb.utils.core.datastores.json.models.AbstractModel;

public class ExampleUser extends AbstractModel {
	@Expose String firstname = "";
	@Expose String surname = "";
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}

}
