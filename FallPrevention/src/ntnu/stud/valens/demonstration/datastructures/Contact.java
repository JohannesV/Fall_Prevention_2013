/*******************************************************************************
 * Licensed to UbiCollab.org under one or more contributor
 * license agreements.  See the NOTICE file distributed 
 * with this work for additional information regarding
 * copyright ownership. UbiCollab.org licenses this file
 * to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 ******************************************************************************/
package ntnu.stud.valens.demonstration.datastructures;

/**
 * Defines a program internal contact.
 * 
 * @author Elias
 *
 */
public class Contact {
	
	private String name, phoneNumber;
	private int id;
	
	/**
	 * Creates a contact without any information. The ID of the contact cannot
	 * be set later, don't use this if you intent for the contact to get an ID.
	 */
	public Contact (){
	}
	
	/**
	 * Creates a contact with a name and id. As the ID of the contact cannot be
	 * changed later, use this method if you want a contact with an ID.
	 * 
	 * @param The name of the contact
	 * @param The contact's ID in the database 
	 */
	public Contact(String name, int id) {
		this.name = name;
		this.id = id;
	}

	/**
	 * Gets the database ID of the contact
	 * 
	 * @return The ID
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Gets the Name field of the contact.
	 * 
	 * @return The Name field
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the Name field of the Contact to the value provided.
	 * 
	 * @param The new value for the Name field.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the value of the phone number field
	 * 
	 * @return The value of the phone number field 
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * Gives the Contact a new phone number.
	 * 
	 * @param The new phone number.
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}	
}
