package com.example.falltimeseries;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LaunchActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_launch);
	}

	public void startMain(View view) {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}
	
}