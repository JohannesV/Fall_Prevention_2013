package no.ntnu.stud.fallprevention.activity;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidplot.series.XYSeries;
import com.androidplot.xy.FillDirection;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYStepMode;

import java.util.ArrayList;
import java.util.List;

import no.ntnu.stud.fallprevention.R;
import no.ntnu.stud.fallprevention.R.array;
import no.ntnu.stud.fallprevention.R.id;
import no.ntnu.stud.fallprevention.R.layout;
import no.ntnu.stud.fallprevention.connectivity.ContentProviderHelper;

/**
 * Shows the statistic screen
 * 
 * 
 */
public class Statistics extends Activity implements OnItemSelectedListener {

	private XYPlot riskHistoryPlot;
	private Spinner timeSpan, dataType;

	/**
	 * Creates the viewable contents for the screen for later use
	 * e.g spinners and XYCoordinate System.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_statistics);

		// Fill the time span spinner with some info
		timeSpan = (Spinner) findViewById(R.id.time_span_spinner);
		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		ArrayAdapter<CharSequence> timeAdapter = ArrayAdapter
				.createFromResource(this, R.array.time_period_array,
						android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		timeAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		timeSpan.setAdapter(timeAdapter);
		timeSpan.setOnItemSelectedListener(this);

		// Do the same for the other spinner
		dataType = (Spinner) findViewById(R.id.data_type_spinner);
		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		ArrayAdapter<CharSequence> dataAdapter = ArrayAdapter
				.createFromResource(this, R.array.data_type_array,
						android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		dataType.setAdapter(dataAdapter);

		dataType.setOnItemSelectedListener(this);
		riskHistoryPlot = (XYPlot) findViewById(R.id.mySimpleXYPlot);

	}

	/**
	 * Inserts the content for XYStatistics model 
	 */
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		// Get risk history values from the database
		// TODO: make sure time back and interval for cpGetRiskHistory
		// corresponds to spinner box
		List<Double> statisticsData = new ArrayList< Double>();

		// Makes sure to keep the proper timeframe
		if (parent.getId() == R.id.data_type_spinner) {
			Log.v("Statistics screen",
					"Was Data Type Spinner: "
							+ (parent.getId() == R.id.data_type_spinner));
			pos = timeSpan.getSelectedItemPosition();

		} else if(parent.getId() == R.id.time_span_spinner){
			Log.v("Statistics screen",
					"Was Timespan Spinner: "
							+ (parent.getId() == R.id.time_span_spinner));
		}
		// Calls method appropriate to option selected
	
	
		 if (pos==0) {
			statisticsData = new ContentProviderHelper(getApplicationContext())
					.cpGetSpeedHistory(6 * (pos + 1), 2 * (pos + 1));
		} else if (pos==1) {
			statisticsData = new ContentProviderHelper(getApplicationContext())
					.cpGetStepsHistory(6 * (pos + 1), 2 * (pos + 1));
		} else if(timeSpan.isSelected()){
			int daysBack =0;
			switch (timeSpan.getSelectedItemPosition()){
			case 1: daysBack = 1;
			case 2: daysBack = 2;
			case 3: daysBack = 4;
			case 4: daysBack = 7;
			case 5: daysBack = 14;
			break;
			}
			statisticsData =  dataType.equals("Steps per minute")? new ContentProviderHelper(getApplicationContext())
			.cpGetSpeedHistory(1440*daysBack, 2 * (pos + 1)):
				new ContentProviderHelper(getApplicationContext())
			.cpGetStepsHistory(1440*daysBack, 2 * (pos + 1));
			
		}

		riskHistoryPlot.clear();

		// Turn the above arrays into XYSeries':
		XYSeries riskSeries = new SimpleXYSeries(statisticsData,
				SimpleXYSeries.ArrayFormat.XY_VALS_INTERLEAVED, // Y_VALS_ONLY
																// means use
				// the element index as
				// the x value
				"Risk History"); // Set the display title of the series

		// Create a formatter to use for drawing a series using
		// LineAndPointRenderer:
		LineAndPointFormatter riskSeriesFormat = new LineAndPointFormatter(
				Color.GREEN, // line color
				Color.BLACK, // point color
				null); // fill color (nothing)

		// Add fill paint
		Paint bgPaint = new Paint();
		bgPaint.setColor(Color.WHITE);
		riskSeriesFormat.setFillPaint(bgPaint);
		riskSeriesFormat.setFillDirection(FillDirection.BOTTOM);
		riskHistoryPlot.addSeries(riskSeries, riskSeriesFormat);

		// Make stuff look better

		// add a new series' to the xyplot:
		riskHistoryPlot.setRangeLabel("Steps");//Y title 
		riskHistoryPlot.setDomainLabel("Time");//X title
		riskHistoryPlot.setRangeStep(XYStepMode.INCREMENT_BY_VAL, 50);//Y increment
		riskHistoryPlot.setDomainStep(XYStepMode.INCREMENT_BY_VAL, 10);//X increment

		riskHistoryPlot.redraw();

	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}

}
