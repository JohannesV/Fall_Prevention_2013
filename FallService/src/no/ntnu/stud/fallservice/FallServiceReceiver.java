package no.ntnu.stud.fallservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class FallServiceReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		Intent service = new Intent(context, FallService.class);
		context.startService(service);
	}
}
