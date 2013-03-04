package no.ntnu.stud.fallprevention;

public class Contact {
	
	String name;
	int phoneNumber = 0;
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

	public int getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(int phoneNumber) {
		this.phoneNumber = phoneNumber;
	}	
}
