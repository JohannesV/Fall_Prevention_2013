package no.ntnu.stud.fallprevention;

public class Contact {
	
	String firstName, surName;
	int phoneNumber = 0;
	
	public Contact (){
		
	}
	public Contact(String firstname, String surName, int phoneNumber) {
		this.firstName = firstname;
		this.surName = surName;
		this.phoneNumber = phoneNumber;
	}
	public void setFirstName(String firstName){
		this.firstName = firstName;
	}
	public void setSurName(String surName) {
		this.surName = surName;
		
	}
	public void setPhoneNumber (int phoneNumber){
		this.phoneNumber = phoneNumber;
	}
	public String getFirstName (){
		return this.firstName;
	}
	public String getSurName (){
		return this.surName;
	}
	public int getPhoneNumber (){
		return this.phoneNumber;
	}
	
}
