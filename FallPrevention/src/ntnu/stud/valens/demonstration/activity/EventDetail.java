
package ntnu.stud.valens.demonstration.activity;

import java.util.Map;

import ntnu.stud.valens.demonstration.R;
import ntnu.stud.valens.demonstration.connectivity.DatabaseContract;
import ntnu.stud.valens.demonstration.connectivity.DatabaseHelper;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * This class will provide more detailed information about the
 * Notifications/events form the event list
 */
public class EventDetail extends Activity {

    private int eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventdetail);

        // Get the event type and ID from the intent
        Intent motherIntent = getIntent();
        eventId = motherIntent
                .getIntExtra("no.ntnu.stud.fallprevention.ID", -1);

        // Fetch description and headline from database
        Map<String, String> eventInformation = new DatabaseHelper(this).dbGetEventInfo(eventId);

        // Fill in information
        TextView textView = (TextView) findViewById(R.id.headlineTextView);
        textView.setText(eventInformation.get(DatabaseContract.EventType.COLUMN_NAME_TITLE));
        TextView textView2 = (TextView) findViewById(R.id.mainTextView);
        String mTempDesc = getString(R.string.event_desc_two_params);
        if(textView.getText().toString().equalsIgnoreCase(getString(R.string.event_list_warning_title))){
            mTempDesc=getString(R.string.event_list_warning_description);
        }else{
        mTempDesc=mTempDesc.replaceAll("%1", eventInformation.get(DatabaseContract.Event.COLUMN_NAME_PARAMETER_1));
        mTempDesc=mTempDesc.replaceAll("%2", eventInformation.get(DatabaseContract.Event.COLUMN_NAME_PARAMETER_2));
        mTempDesc=mTempDesc.replaceAll("null", getString(R.string.event_null_parameter));
        
        }
        textView2.setText(mTempDesc);

    }

    /**
     * Fires an event to delete the event you are currently viewing Sends you
     * back to Eventlist afterwards.
     * 
     * @param view
     */
    public void fireDeleteButton(View view) {
        // Delete event from database
        new DatabaseHelper(this).dbDeleteEvent(eventId);
        // Then go back to EventList screen
        Intent intent = new Intent(this, EventList.class);
        startActivity(intent);
    }

    /**
     * Doesn't do anything to the Event Sends you back to the EventList
     * 
     * @param view
     */
    public void fireKeepButton(View view) {
        // Go back to EventList screen
        Intent intent = new Intent(this, EventList.class);
        startActivity(intent);
    }
}
