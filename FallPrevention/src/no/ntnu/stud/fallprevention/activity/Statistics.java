package no.ntnu.stud.fallprevention.activity;

import java.util.List;

import no.ntnu.stud.fallprevention.R;
import no.ntnu.stud.fallprevention.connectivity.ContentProviderHelper;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

/**
 * Shows the statistic screen
 * 
 * 
 */
public class Statistics extends Activity implements OnItemSelectedListener {

	private static final String TAG = "no.ntnu.stud.fallprevention.activity";
	
	private Spinner timeSpan, dataType;
	private GraphicalView mChart;
    private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
    private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
    private XYSeries mCurrentSeries;
    private XYSeriesRenderer mCurrentRenderer;
    
    private void initChart(String title) {
        mCurrentSeries = new XYSeries(title);
        mDataset.addSeries(mCurrentSeries);
        mCurrentRenderer = new XYSeriesRenderer();
        mRenderer.addSeriesRenderer(mCurrentRenderer);
        mRenderer.setLabelsTextSize(25);
        mRenderer.setLegendTextSize(25);
        mRenderer.setYLabelsColor(0, Color.BLACK);
        mRenderer.setXLabelsColor(Color.BLACK);
        mRenderer.setMarginsColor(Color.WHITE);
    }
    
    private void getData(int timeSpinner, int dataSourceSpinner) {
    	Log.v(TAG, "GET DATA: " + timeSpinner + ", " + dataSourceSpinner);
    	mCurrentSeries.clear();
    	List<Double> data;
    	
    	if (dataSourceSpinner == 1) {
    		// Gait speed, find time
    		int time;
    		if (timeSpinner == 0) {
    			time = 7;
    		} else if (timeSpinner == 1) {
    			time = 30;
    		} else {
    			time = 90;
    		}
            mRenderer.setYTitle(getResources().getString(R.string.statistics_y_label_gait_s));
            mRenderer.setXTitle(getResources().getString(R.string.statistics_x_label_days));
    		data = new ContentProviderHelper(getApplicationContext()).cpGetSpeedHistory(time);
    	} else if (dataSourceSpinner == 2) {
    		// Gait variability, find time
    		int time;
    		if (timeSpinner == 0) {
    			time = 7;
    		} else if (timeSpinner == 1) {
    			time = 30;
    		} else {
    			time = 90;
    		}
            mRenderer.setYTitle(getResources().getString(R.string.statistics_y_label_gait_v));
            mRenderer.setXTitle(getResources().getString(R.string.statistics_x_label_days));
    		data = new ContentProviderHelper(getApplicationContext()).cpGetVariabilityHistory(time);
    	} else {
    		// Steps, find number of intervals and interval length
    		int length, interval;
    		if (timeSpinner == 0) {
    			// 1 week = 84 bi-hourly intervals
    			interval = 2;
    			length = 84;
    			mRenderer.setXTitle(getResources().getString(R.string.statistics_x_label_hours));
    		} else if (timeSpinner == 1) {
    			// 1 month = 30 daily intervals
    			interval = 24;
    			length = 30;
    			mRenderer.setXTitle(getResources().getString(R.string.statistics_x_label_days));
    		} else {
    			// 3 months = 90 daily intervals
    			interval = 24; 
    			length = 90;
    			mRenderer.setXTitle(getResources().getString(R.string.statistics_x_label_days));
    		}
    		// Fetch data
            mRenderer.setYTitle(getResources().getString(R.string.statistics_y_label_steps));
    		data = new ContentProviderHelper(getApplicationContext()).cpGetStepsHistory(length, interval);
    	}
    	
    	// Extract data from content provider query
    	double x = 0; double y = 0;
    	boolean xSet = false;
    	for (double dp : data) {
    		if (xSet) {
    			y = dp;
    			mCurrentSeries.add(x, y);
    			xSet = false;
    		} else {
    			x = dp;
    			xSet = true;
    		}
    	}
    }
	
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

		// Display chart
		LinearLayout layout = (LinearLayout) findViewById(R.id.linear_layout);
        initChart("Test");
        mChart = ChartFactory.getCubeLineChartView(this, mDataset, mRenderer, 0.1f);
        mChart.setBackgroundColor(Color.WHITE);
        layout.addView(mChart);
	}
	
	private void repaint(int time, int source) {
		Log.v(TAG, "REPAINT " + time + ", " + source);
		getData(time, source);
        mChart.repaint();
	}

	/**
	 * Inserts the content for XYStatistics model 
	 */
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		Log.v(TAG, "item selected");
		repaint(timeSpan.getSelectedItemPosition(), dataType.getSelectedItemPosition());
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}

}
