package no.ntnu.stud.fallprevention;

public class Contact {
	
	String name, phoneNumber;
	int id;
	
	public Contact (){
	}
	
	public Contact(String name, int id) {
		this.name = name;
		this.id = id;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}	
}
