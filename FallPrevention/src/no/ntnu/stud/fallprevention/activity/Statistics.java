package no.ntnu.stud.fallprevention.activity;

import java.util.ArrayList;
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
 * Class for handling the functionality of the statistics screen
 * 
 * @author fiLLLip, Elias
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

	/**
	 * Creates the viewable contents for the screen, e.g spinners and
	 * XYCoordinate System.
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

		// Create the chart
		initChart();
	}

	/**
	 * Initializes the chart, defines the visuals and which data series to
	 * display.
	 */
	private void initChart() {
		mCurrentSeries = new XYSeries(""); // We don't want any label, it is
											// just annoying.
		mDataset.addSeries(mCurrentSeries);
		mCurrentRenderer = new XYSeriesRenderer();
		mRenderer.addSeriesRenderer(mCurrentRenderer);
		mRenderer.setLabelsTextSize(25);
		mRenderer.setLegendTextSize(25);
		mRenderer.setYLabelsColor(0, Color.BLACK);
		mRenderer.setXLabelsColor(Color.BLACK);
		mRenderer.setMarginsColor(Color.WHITE);
		mChart = ChartFactory.getCubeLineChartView(this, mDataset, mRenderer,
				0.1f);
		mChart.setBackgroundColor(Color.WHITE);
		LinearLayout layout = (LinearLayout) findViewById(R.id.linear_layout);
		layout.addView(mChart);
	}

	/**
	 * Fetches data from the content provider, based on the positions of the
	 * spinners. The displays the data in the chart.
	 * 
	 * @param timeSpinner
	 *            - the index of the position of the time window spinner.
	 * @param dataSourceSpinner
	 *            - the index of the position of the data source spinner.
	 */
	private void setData(int timeSpinner, int dataSourceSpinner) {
		Log.v(TAG, "GET DATA: " + timeSpinner + ", " + dataSourceSpinner);
		// Clear the existing data, otherwise the graph would display multiple
		// lines at the same time
		mCurrentSeries.clear();

		// Fetch information from the CP. A complex if-else chain is required to
		// get the correct information
		List<Double> data;
		if (dataSourceSpinner == 1) {
			// Data source is gait speed, still needs to find the time window
			int time;
			if (timeSpinner == 0) {
				time = 7;
			} else if (timeSpinner == 1) {
				time = 30;
			} else {
				time = 90;
			}
			// Set the X and Y titles
			mRenderer.setYTitle(getResources().getString(
					R.string.statistics_y_label_gait_s));
			mRenderer.setXTitle(getResources().getString(
					R.string.statistics_x_label_days));
			// Actually fetch data
			data = new ContentProviderHelper(getApplicationContext())
					.cpGetSpeedHistory(time);
			// Transform to actual (x, y)-pairs
			data = createXYPairs(data, 1);
		} else if (dataSourceSpinner == 2) {
			// Data source is gait variability, still needs to find the time
			// window
			int time;
			if (timeSpinner == 0) {
				time = 7;
			} else if (timeSpinner == 1) {
				time = 30;
			} else {
				time = 90;
			}
			// Set the X and Y titles
			mRenderer.setYTitle(getResources().getString(
					R.string.statistics_y_label_gait_v));
			mRenderer.setXTitle(getResources().getString(
					R.string.statistics_x_label_days));
			// Actually fetch data
			data = new ContentProviderHelper(getApplicationContext())
					.cpGetVariabilityHistory(time);
			// Transform to actual (x, y)-pairs
			data = createXYPairs(data, 1);
		} else {
			// Data source is steps, still need to find the time window.
			// Additionally, in the case of steps, one needs to decide the
			// interval width one wants to count steps in.
			int length, interval, unit;
			if (timeSpinner == 0) {
				// 1 week = 84 bi-hour intervals.
				unit = 2;
				interval = 2;
				length = 84;
				mRenderer.setXTitle(getResources().getString(
						R.string.statistics_x_label_hours));
			} else if (timeSpinner == 1) {
				// 1 month = 30 day intervals.
				unit = 1;
				interval = 24;
				length = 30;
				mRenderer.setXTitle(getResources().getString(
						R.string.statistics_x_label_days));
			} else {
				// 3 months = 90 day intervals
				unit = 1;
				interval = 24;
				length = 90;
				mRenderer.setXTitle(getResources().getString(
						R.string.statistics_x_label_days));
			}
			// Set label on Y axis. Because the X axis label varies based on
			// time gap, we needed to set that in the previous if-else chain.
			mRenderer.setYTitle(getResources().getString(
					R.string.statistics_y_label_steps));
			// Really fetch data
			data = new ContentProviderHelper(getApplicationContext())
					.cpGetStepsHistory(length, interval);
			// Transform data into actual (x,y)-pairs.
			data = createXYPairs(data, unit);
		}

		// We have a list of alternating x- and y-values, with x coming first.
		// Therefore we need to iterate through the list and extract the (x,
		// y)-pairs before adding the to the mCurrentSeries.
		double x = 0;
		double y = 0;
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
	 * Creates realistic (x, y)-pairs out of a list of unrealistic (x, y)-pairs.
	 * 
	 * This is needed because the x values provided by the ContentProviderHelper
	 * are not the ones we want to display. Therefore, this method exists to
	 * turn the x values into the ones that the statistics screen wants to
	 * display.
	 * 
	 * The method gives the newest data point (each y value) a x value 0, and
	 * every other data point the value of the data point that's next minus the
	 * unit parameter.
	 * 
	 * @param data
	 *            - a list of doubles, where the elements are alternatingly x
	 *            and y elements, starting with x.
	 * @param units
	 *            - the number of units of the unit in question each data point
	 *            represents.
	 * @return a list of doubles, where the elements are alternatingly x and y
	 *         elements, starting with x.
	 */
	private List<Double> createXYPairs(List<Double> data, int units) {
		List<Double> mReturner = new ArrayList<Double>();

		double y = 0;
		double x = -((units * data.size()) / 2);
		boolean xSet = false;
		for (double dp : data) {
			if (xSet) {
				y = dp;
				mReturner.add(x);
				mReturner.add(y);
				xSet = false;
			} else {
				x += units;
				xSet = true;
			}
		}

		return mReturner;
	}

	/**
	 * Repaints the graph, and fetches the data to make sure it is updated
	 * according the spinner values.
	 * 
	 */
	private void repaint() {
		setData(timeSpan.getSelectedItemPosition(),
				dataType.getSelectedItemPosition());
		mChart.repaint();
	}

	/**
	 * Fired when the user selects an item from one of the spinners. (And once
	 * when the screen is created). Repaints the graph to keep it updated with
	 * the spinner settings.
	 */
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		Log.v(TAG, "Item selected");
		repaint();
	}

	// This method is required by the interface, but to our knowledge it is never called.
	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}

}
