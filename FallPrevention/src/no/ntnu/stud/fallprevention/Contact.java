package no.ntnu.stud.fallprevention;

public class Contact {
	
	String name, id;
	int phoneNumber = 0;
	
	public Contact (){
	}
	
	public Contact(String name, String id) {
		this.name = name;
		this.id = id;
	}
	
	public Contact(String name, int phoneNumber) {
		this.name = name;
		this.phoneNumber = phoneNumber;
	}

	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(int phoneNumber) {
		this.phoneNumber = phoneNumber;
	}	
}
