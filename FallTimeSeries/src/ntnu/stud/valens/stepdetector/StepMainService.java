/*******************************************************************************
 * Licensed to UbiCollab.org under one or more contributor
 * license agreements.  See the NOTICE file distributed 
 * with this work for additional information regarding
 * copyright ownership. UbiCollab.org licenses this file
 * to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 ******************************************************************************/
package ntnu.stud.valens.stepdetector;

import java.util.ArrayList;
import java.util.List;

import ntnu.stud.valens.stepdetector.R;


import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.widget.Toast;

/**
 * A service that runs in the background, fetching sensor data from the
 * accelerometer. It calculates vector lengths of the sensor vector, and use
 * this data to detect steps.
 * 
 * When sufficient data has been collected to generate steps, it starts a new
 * DetectStepsThread to calculate steps asynchronously (so as to avoid losing
 * input from the sensors). Having started a new thread, it clears the data
 * locally, so the local storage keeps a constant max size.
 * 
 * @author Elias
 * 
 */
public class StepMainService extends Service implements SensorEventListener {
//	private StepsManager mStepsManager;
	private SensorManager mSensorManager;
	private Sensor mAccSensor;
	private List<Float> mVectorLengths;
	public List<Long> mTimeStamps;
	public double mMean, mStd;

	/**
	 * Method called by the startService() in the launch activity. Do no call
	 * this method directly! Create an intent and call startService(intent)
	 * instead.
	 * 
	 * This method has two functions: It can either start or stop the service.
	 * Which of these action it performs, depends on the value of the "stop"
	 * extra than can be put into the intent. If the "stop" extra is given the
	 * value "true", this method stops the service. Otherwise, it starts the
	 * service.
	 * 
	 * @param - All handled by the startService(intent) method.
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// If the service was called with a start parameter, we should start it
		if (!intent.getBooleanExtra("stop", false)) {

			// Initialize the service
			Intent intento = new Intent(this, LaunchActivity.class);
			intento.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_SINGLE_TOP);
			PendingIntent pendIntent = PendingIntent.getActivity(this, 0,
					intento, 0);

			Notification notice = new Notification(R.drawable.ic_launcher,
					getResources().getString(R.string.service_start),
					System.currentTimeMillis());

			notice.setLatestEventInfo(this,
					getResources().getString(R.string.service_title),
					getResources().getString(R.string.service_description),
					pendIntent);

			notice.flags |= Notification.FLAG_NO_CLEAR;
			startForeground(Values.MY_ID, notice);

			// Initialize lists
			mVectorLengths = new ArrayList<Float>();
			mTimeStamps = new ArrayList<Long>();
			
			// Fetch mean and std from prefrences file
			SharedPreferences sp = PreferenceManager
					.getDefaultSharedPreferences(this);
			mMean = sp.getFloat("mean", -1f);
			mStd = sp.getFloat("std", -1f);
			if (mMean == -1f || mStd == -1f) {
				Toast.makeText(this, "Mean and STD not set. Calibrate first!", Toast.LENGTH_SHORT).show();
				return Service.START_NOT_STICKY;
			}
			
			// Register sensor for orientation events
			mSensorManager = (SensorManager) getApplicationContext()
					.getSystemService(Context.SENSOR_SERVICE);
			for (Sensor sensor : mSensorManager
					.getSensorList(Sensor.TYPE_ACCELEROMETER)) {
				if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
					mAccSensor = sensor;
				}
			}

			// Check that we found a sensor object
			if (mAccSensor == null) {
				// We cannot continue if the required sensor is not found
				throw new Resources.NotFoundException(
						"Accelerometer not found!");
			}

			mSensorManager.registerListener(this, mAccSensor,
					SensorManager.SENSOR_DELAY_GAME);

			// Finally, start service
			return Service.START_NOT_STICKY;
		} else {
			// If the service was called with a "stop" option, we should stop
			// it.
			mSensorManager.unregisterListener(this);
			stopForeground(true);
			return Service.START_NOT_STICKY;
		}
	}

	/**
	 * Called by the system when new sensor data is provided. Stores the time
	 * stamp as well as the vector lengths of the sensor vector at the given
	 * point.
	 * 
	 * If there is enough data stored, it starts a thread to detect peaks, and
	 * flushed the locally stored data to prevent it from becoming too much.
	 * 
	 * @param event
	 *            - the SensorEvent that occured.
	 */
	@Override
	public void onSensorChanged(SensorEvent event) {
		Long timeStamp = System.currentTimeMillis();
		mVectorLengths.add(Methods.calculateVectorLength(event.values));
		mTimeStamps.add(timeStamp);
		// See if there is enough data to launch a calculation
		if (mVectorLengths.size() >= Values.COMMIT_DATA_THRESHOLD) {
			DetectStepsThread ds = new DetectStepsThread(new ArrayList<Float>(
					mVectorLengths), new ArrayList<Long>(mTimeStamps), this, mMean, mStd);
			(new Thread(ds)).start();
			discardData();
		}
	}

	/**
	 * Clears all the local data, except for the last 2*WINDOW_SIZE elements.
	 * These elements are retained, because the first WINDOW_SIZE elements are
	 * required for smoothing in the next set of data, and the second
	 * WINDOW_SIZE elements of the last data was used only for smoothing the old
	 * data, and has not yet had it's steps detected.
	 */
	private void discardData() {
		List<Long> newTimeStamps = new ArrayList<Long>();
		List<Float> newVectorLengths = new ArrayList<Float>();
		for (int i = mTimeStamps.size() - (Values.PEAK_STRENGTH_WINDOW * 2); i < mTimeStamps
				.size(); i++) {
			newTimeStamps.add(mTimeStamps.get(i));
			newVectorLengths.add(mVectorLengths.get(i));
		}
		mTimeStamps = newTimeStamps;
		mVectorLengths = newVectorLengths;
	}

	// Ordinary setter
	public void setMean(double mean) {
		mMean = mean;
	}

	// Ordinary setter
	public void setStd(double std) {
		mStd = std;
	}

	// We don't need any binders, so we don't use this method.
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	// We don't really care about this event, so we leave it blank.
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
	}

}
