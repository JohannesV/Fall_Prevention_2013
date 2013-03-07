package no.ntnu.stud.fallservice;

import java.util.Date;

public class Movement {
	
	private int id;
	private Date datetime;
	private Double x_axis;
	private Double y_axis;
	private Double z_axis;
	
	public Movement (int id, Date datetime, Double x_axis, Double y_axis, Double z_axis) {
		this.id = id;
		this.datetime = datetime;
		this.x_axis = x_axis;
		this.y_axis = y_axis;
		this.z_axis = z_axis;
	}
}
