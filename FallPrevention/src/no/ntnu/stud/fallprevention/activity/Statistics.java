package no.ntnu.stud.fallprevention.activity;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
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

import java.util.List;

import no.ntnu.stud.fallprevention.R;
import no.ntnu.stud.fallprevention.R.array;
import no.ntnu.stud.fallprevention.R.id;
import no.ntnu.stud.fallprevention.R.layout;
import no.ntnu.stud.fallprevention.connectivity.ContentProviderHelper;

/**
 * Shows the statistic screen
 * 
 * @author Tayfun
 * 
 */
public class Statistics extends Activity implements OnItemSelectedListener {

	private XYPlot riskHistoryPlot;
	private Spinner timeSpan, dataType;

	/**
	 * Prepares the screen for the program and sets the activity for the
	 * statistics
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
	 * Puts the statistic computation
	 */
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		// Get risk history values from the database
		List<Double> riskHistory = new ContentProviderHelper(
				getApplicationContext()).cpGetRiskHistory(pos + 10,4);

		riskHistoryPlot.clear();

		// Turn the above arrays into XYSeries':
		XYSeries riskSeries = new SimpleXYSeries(riskHistory,
				SimpleXYSeries.ArrayFormat.XY_VALS_INTERLEAVED, // Y_VALS_ONLY means use
														// the element index as
														// the x value
				"Risk History"); // Set the display title of the series

		// Create a formatter to use for drawing a series using
		// LineAndPointRenderer:
		LineAndPointFormatter riskSeriesFormat = new LineAndPointFormatter(
				Color.GREEN, // line color
				Color.BLACK, // point color
				null); // fill color (nothing)

//		Add fill paint
		Paint bgPaint = new Paint();
		bgPaint.setColor(Color.WHITE);
		 riskSeriesFormat.setFillPaint(bgPaint);
		 riskSeriesFormat.setFillDirection(FillDirection.BOTTOM);
		 riskHistoryPlot.addSeries(riskSeries, riskSeriesFormat);
		 
		// Make stuff look better
		
		// add a new series' to the xyplot:
		riskHistoryPlot.setRangeLabel("Steps");
		riskHistoryPlot.setDomainLabel("Time");
		riskHistoryPlot.setRangeStep(XYStepMode.INCREMENT_BY_VAL, 10);
		riskHistoryPlot.setDomainStep(XYStepMode.INCREMENT_BY_VAL, 10);

		riskHistoryPlot.redraw();

	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}

}