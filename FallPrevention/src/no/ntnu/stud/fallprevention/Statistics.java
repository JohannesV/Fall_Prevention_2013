package no.ntnu.stud.fallprevention;

import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidplot.series.XYSeries;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYStepMode;

public class Statistics extends Activity implements OnItemSelectedListener {
	
	private XYPlot riskHistoryPlot;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_statistics);
		
		Spinner spinner = (Spinner) findViewById(R.id.spinner1);
		
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        R.array.time_period_array, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(this);
		
		riskHistoryPlot = (XYPlot) findViewById(R.id.mySimpleXYPlot);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_statistics, menu);
		return true;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		Toast.makeText(this, "ID: " + pos, Toast.LENGTH_SHORT).show();
		// Get risk history values from the database 
		List<Double> riskHistory = new DatabaseHelper(this).dbGetRiskHistory(pos+10);
		        
		riskHistoryPlot.clear();
		
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
        riskHistoryPlot.addSeries(riskSeries, riskSeriesFormat);
        
        // Make stuff look better
        riskHistoryPlot.setRangeLabel("");
        riskHistoryPlot.setDomainLabel("");
        riskHistoryPlot.setRangeStep(XYStepMode.INCREMENT_BY_VAL, 1.0);
        riskHistoryPlot.setDomainStep(XYStepMode.INCREMENT_BY_VAL, 1.0);
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}

}
