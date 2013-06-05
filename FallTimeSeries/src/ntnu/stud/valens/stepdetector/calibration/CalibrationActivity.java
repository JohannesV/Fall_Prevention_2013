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
package ntnu.stud.valens.stepdetector.calibration;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Timer;

import ntnu.stud.valens.stepdetector.LaunchActivity;
import ntnu.stud.valens.stepdetector.R;
import ntnu.stud.valens.stepdetector.Values;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Describes the calibration activity screen, and the functionality surrounding
 * it, i.e. what happens when the user presses the button.
 * 
 * @author Elias
 */
public class CalibrationActivity extends Activity {

    // Used to play the "beep"-sound
    private MediaPlayer mMediaPlayer;

    @Override
    // Standard onCreate
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calibration);
        mMediaPlayer = MediaPlayer.create(this, R.raw.beep);
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(this);
       long mCalibLong=sp.getLong("lastCalibrationDate", -1);
        Date mCalibDate= new Date(mCalibLong);
        String mCalibString;
        if(mCalibLong==-1){
         mCalibString=getString(R.string.calibrated_never) ; 
         
        }else{
            mCalibString=getString(R.string.calibrated_sometime);
            mCalibString=mCalibString.replaceFirst("%%1",mCalibDate.toString());
        }
        TextView mInstructions=(TextView) findViewById(R.id.instructions);
        mInstructions.append(mCalibString);
        
        

    }

    /**
     * This is an onClick-event listener. Called by clicking the
     * "Calibrate"-button. Starts the countdown before starting a
     * CalibrationStartTask.
     * 
     * @param view - Provided by the system
     */
    public void startCalibration(View view) {
        // Wait before starting
        Timer timer = new Timer();
        CalibrationStartTask cst = new CalibrationStartTask(this);
        timer.schedule(cst, Values.CAL_WAIT);
        // Disable calibration button, so the user does not start multiple
        // calibration threads at the same time.
        Button calibrationButton = (Button) findViewById(R.id.btnStart);
        calibrationButton.setEnabled(false);
    }

    /**
     * The method is fired by the CalibrationThread when mean and std are fully
     * calculated. It plays a sound to show the user that the calibration is
     * finished, and stores the mean and std to file.
     * 
     * @param mean - the calculated mean
     * @param std - the calculated std
     */
    public void finishCalibration(double mean, double std) {
        // Play sound
        playSound();
        // Show message
        Toast.makeText(this, getResources().getString(R.string.calibration_complete),
                Toast.LENGTH_LONG).show();
        // Store information in phone using SharedPreferences
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sp.edit();
        editor.putFloat("mean", (float) mean);
        editor.putFloat("std", (float) std);
        editor.putLong("lastCalibrationDate", System.currentTimeMillis());
        editor.commit();
        // Enable calibration button again, in case the user wants to calibrate
        // again
        Button calibrationButton = (Button) findViewById(R.id.btnStart);
        calibrationButton.setEnabled(true);
        // Go back to main screen
        Intent intent = new Intent(this, LaunchActivity.class);
        startActivity(intent);
    }

    /**
     * Plays the "beep" sound.
     */
    public void playSound() {
        mMediaPlayer.setVolume(1.0f, 1.0f);
        mMediaPlayer.start();

        vibrate(500);
    }

    /**
     * Makes the phone vibrate for ms milliseconds
     * 
     * @param ms
     */
    private void vibrate(int ms) {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(ms);
    }
}
