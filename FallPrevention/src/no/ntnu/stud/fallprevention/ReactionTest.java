package no.ntnu.stud.fallprevention;

import java.util.List;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.View;
import android.widget.ImageView;


public class ReactionTest {
	long startTime;
	long endTime, random; // principle of random use (see net)
	char colour;
	private View view;
	private int [] Time;
	
	public ReactionTest (){
		int [] Time = new int[6];
		boolean[] Correctness;
		random = 0;
		colour = 'w';
	}
	

public void setColour(int r){
	setContentView(R.layout.activity_reaction_test);	
	ImageView imageView = findViewById(R.id.headlineTextView);
	if(r > 50) view.setBackgroundColor(Color.GREEN); //imageView.setImageResource(R.layout.activity_reaction_test.White);
	else view.setBackgroundColor(Color.RED); //imageView.setImageResource(R.layout.activity_reaction_test.Black);

}	
	
//public void resetColour(){
	//#if(this.colour == 'r' || this.colour = 'g') activity make this.colour = 'w'

	public int average (int [] Time){
		int average;
		for(int i = 0, i <= Time.length, i++){
			average += Time[i];
		}
		average = average/Time.length;
		return average;
	}	

	//to recognize that the pressed happened
	
	
	
	public boolean onSensorChanged(SensorEvent event, int r) {
	    if (event.sensor.getType() == Sensor.TYPE_PRESSURE) {
	      setColour(r);
	      return true;
	    }
	    else return false;
	  }	
	
public int reactionMeasure(SensorEvent event){
	for(int i = 0; i < 5 ; i++){
	SensorManager sensorManager;
	List<Sensor> touchSensor = sensorManager.getSensorList(Sensor.TYPE_PRESSURE);
	// register a SensorEventListener object on the touchSensor
	SensorEventListener listener = new SensorEventListener();
	touchSensor.listener;
	Random r = new Random();
	int i1=r.nextInt(100-1) + 1;
	Random w = new Random();
	int i2=r.nextInt(5-1) + 1;
	wait(w*1000);
	//measuring time:
	startTime = System.currentTimeMillis();
	while(!onSensorChanged(event,r))
	endTime = System.currentTimeMillis();
	int difference = endTime - startTime;
	Time[i] = difference;
	}
	display("Your reactionTime:" average(Time));

}

