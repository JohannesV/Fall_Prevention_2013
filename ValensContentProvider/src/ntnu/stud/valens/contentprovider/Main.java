package ntnu.stud.valens.contentprovider;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        switch (which){
		        case DialogInterface.BUTTON_POSITIVE:
					SQLiteDatabase db = new CPValensDB(getApplicationContext()).getWritableDatabase();
					new CPValensDB(getApplicationContext()).reset(db);
					Toast.makeText(getApplicationContext(), "Deleted data", Toast.LENGTH_SHORT).show();
		            break;

		        case DialogInterface.BUTTON_NEGATIVE:
		            //No button clicked
		            break;
		        }
		    }
		};
		final AlertDialog.Builder alertDeleteYesNo = new AlertDialog.Builder(this).setPositiveButton("Yes", dialogClickListener)
			    .setNegativeButton("No", dialogClickListener);
					
		final Button button = (Button) findViewById(R.id.btnDeleteDB);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				alertDeleteYesNo.setMessage("Are you sure?").show();
			}
		});
	}
}
