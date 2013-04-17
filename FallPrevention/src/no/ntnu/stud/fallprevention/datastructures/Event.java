package no.ntnu.stud.fallprevention.datastructures;

/**
 * Object used to contain information about event to display in the EventList.
 * 
 * @author elias
 *
 */

public class Event {
	
	private String title;
	private int id; 
	private String icon;
	
	public Event(String title, int id, String icon) {
		this.title = title;
		this.id = id;
		this.icon = icon;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
}
