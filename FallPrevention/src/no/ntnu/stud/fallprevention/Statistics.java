package no.ntnu.stud.fallprevention;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;

import com.androidplot.series.XYSeries;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;

public class Statistics extends Activity {
	
	private XYPlot mySimpleXyPlot;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_statistics);
		
		mySimpleXyPlot = (XYPlot) findViewById(R.id.mySimpleXYPlot);
		
		// Get risk history values from the database 
		List<Double> riskHistory = new DatabaseHelper(this).dbGetRiskHistory();
		        
        // Turn the above arrays into XYSeries':
        XYSeries riskSeries = new SimpleXYSeries(
                riskHistory,
                SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, // Y_VALS_ONLY means use the element index as the x value
                "Risk History");                             // Set the display title of the series
        
        // Create a formatter to use for drawing a series using LineAndPointRenderer:
        LineAndPointFormatter riskSeriesFormat = new LineAndPointFormatter(
                Color.rgb(0, 200, 0),                   // line color
                Color.rgb(0, 100, 0),                   // point color
                null);                                  // fill color (none)
        
        // add a new series' to the xyplot:
        mySimpleXyPlot.addSeries(riskSeries, riskSeriesFormat);
        
        // reduce the number of range labels
        mySimpleXyPlot.setTicksPerRangeLabel(1);
        
        // by default, AndroidPlot displays developer guides to aid in laying out your plot.
        // To get rid of them call disableAllMarkup():
        mySimpleXyPlot.disableAllMarkup(); 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_statistics, menu);
		return true;
	}

}
