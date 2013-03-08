package no.ntnu.stud.fallprovider;

import java.util.Date;

public class Movement {
	
	private int id;
	private int steps;
	private double distance;
	private Date timeStart;
	private Date timeEnd;
	
	public Movement (int id, int steps, double distance, Date timeStart, Date timeEnd) {
		this.id = id;
		this.steps = steps;
		this.distance = distance;
		this.timeStart = timeStart;
		this.timeEnd = timeEnd;
	}
}
