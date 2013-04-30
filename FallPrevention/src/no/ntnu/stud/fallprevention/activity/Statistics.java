package no.ntnu.stud.fallprevention.activity;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import com.androidplot.xy.FillDirection;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYStepMode;

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
        mRenderer.setYTitle("Steg");
        mRenderer.setXTitle("Dager siden");
        mRenderer.setYLabelsColor(0, Color.BLACK);
        mRenderer.setXLabelsColor(Color.BLACK);
        mRenderer.setMarginsColor(Color.WHITE);
    }
    
    private void addData() {
    	List<Integer> data = new ContentProviderHelper(getApplicationContext()).cpGetStepsHistoryWeek();
    	if(data.size() > 0){
    		int i = 7;
    		for(Integer dataset : data){
    			Log.v(TAG, "Added " + dataset.toString() + " to graph");
    			mCurrentSeries.add(i, dataset);
    			i--;
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
		LinearLayout layout = (LinearLayout) findViewById(R.id.linear_layout);
        if (mChart == null) {
            initChart("Test");
            addData();
            mChart = ChartFactory.getCubeLineChartView(this, mDataset, mRenderer, 0.1f);
            mChart.setBackgroundColor(Color.WHITE);
            layout.addView(mChart);
        } else {
            mChart.repaint();
        }
        
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
	}

	/**
	 * Inserts the content for XYStatistics model 
	 */
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}

}
